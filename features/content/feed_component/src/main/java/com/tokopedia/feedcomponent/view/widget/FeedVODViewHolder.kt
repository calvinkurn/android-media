package com.tokopedia.feedcomponent.view.widget

import android.content.Context
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import com.google.android.exoplayer2.ui.PlayerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.feedcomponent.R
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXCard
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXMedia
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXProduct
import com.tokopedia.feedcomponent.util.util.hideViewWithAnimationVod
import com.tokopedia.feedcomponent.util.util.productThousandFormatted
import com.tokopedia.feedcomponent.util.util.showViewWithAnimationVOD
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.unifyprinciples.Typography
import kotlinx.coroutines.*

class FeedVODViewHolder @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : ConstraintLayout(context, attrs, defStyleAttr), LifecycleObserver {
    private val layoutVideo: ConstraintLayout
    private val vodPreviewImage: ImageUnify
    private val videoView: View
    private val layoutFrameView: ConstraintLayout
    private val layoutPlayerView : PlayerView
    private val vodLihatProdukBtn : Typography
    private val vodPlayIcon : ImageUnify
    private val vodFullScreenIcon : ImageView
    private val vodVolumeIcon : ImageView
    private val vodLanjutMemontomBtn : Typography
    private val vodTimerView : Typography
    private val vodFrozenView : View
    private val vodLoader : LoaderUnify
    private var mListener : VODListener? = null
    private var mOnClickVolumeListener: (() -> Unit)? = null
    private var mUpdateViewsText: ((String) -> Unit)? = null
    private var mFeedXMedia: FeedXMedia? = null
    private var mFeedXCard: FeedXCard? = null
    private var mPostionInFeed: Int = 0
    private var mIsMute : Boolean = false
    private var mRatio: String = "1"
    private var isPaused = false
    private var handlerAnim: Handler? = null
    private var productVideoJob: Job? = null
    private val scope = CoroutineScope(Dispatchers.Main)
    private var secondCountDownTimer: CountDownTimer? = null
    var isVODVideoViewFrozen = true
    private var mShouldTrack = false
    private var videoPlayer: FeedExoPlayer? = null
    private var feedAddViewJob: Job? = null

    private var mProducts: List<FeedXProduct> = emptyList()


    init {
        (context as? LifecycleOwner)?.lifecycle?.addObserver(this)
        val view =
            LayoutInflater.from(context).inflate(R.layout.item_post_long_video_vod, this, true)
        val param = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        view?.layoutParams = param

        layoutVideo = findViewById(R.id.vod_layout_main)
        vodPreviewImage = findViewById(R.id.vod_videoPreviewImage)
        videoView = findViewById(R.id.vod_view)
        layoutFrameView = findViewById(R.id.vod_frame_video)
        layoutPlayerView = findViewById(R.id.vod_layout_video)
        vodLihatProdukBtn = findViewById(R.id.vod_lihat_product)
        vodPlayIcon = findViewById(R.id.ic_vod_play)
        vodFullScreenIcon = findViewById(R.id.vod_full_screen_icon)
        vodVolumeIcon = findViewById(R.id.vod_volumeIcon)
        vodLanjutMemontomBtn = findViewById(R.id.vod_lanjut_menonton_btn)
        vodFrozenView = findViewById(R.id.vod_frozen_view)
        vodLoader = findViewById(R.id.vod_loader)
        vodTimerView = findViewById(R.id.vod_timer_view)
    }

    fun setData(
        feedXCard: FeedXCard,
        feedXMedia: FeedXMedia,
        ratio: String,
        products: List<FeedXProduct>,
        positionInFeed: Int
    ) {
        this.mRatio = ratio
        this.mFeedXMedia = feedXMedia
        this.mFeedXCard = feedXCard
        this.mProducts = products
        this.mPostionInFeed = positionInFeed
    }

    private fun setConstraintsForVODLayout(){
        val constraintSetForVideoCoveMedia = ConstraintSet()
        constraintSetForVideoCoveMedia.clone(layoutVideo)
        constraintSetForVideoCoveMedia.setDimensionRatio(vodPreviewImage.id, mRatio)
        constraintSetForVideoCoveMedia.setDimensionRatio(videoView.id, mRatio)
        constraintSetForVideoCoveMedia.applyTo(layoutVideo)
    }

    private fun setConstraintsForFrameView(){
        val constraintSetForVideoLayout = ConstraintSet()
        constraintSetForVideoLayout.clone(layoutFrameView)
        constraintSetForVideoLayout.setDimensionRatio(layoutPlayerView.id, mRatio)
        constraintSetForVideoLayout.applyTo(layoutFrameView)
    }
    private fun setCoverImage(){
        if (!vodPreviewImage.isVisible)
            vodPreviewImage.visible()
        mFeedXMedia?.let { vodPreviewImage.setImageUrl(it.coverUrl) }
    }

    fun bindData(isMute: Boolean) {
        mIsMute = isMute
        setConstraintsForVODLayout()
        setConstraintsForFrameView()
        setCoverImage()

        setListenersOnVODViewElements()
        updateVolumeButtonState()

    }
    private fun setListenersOnVODViewElements(){
        vodLihatProdukBtn.setOnClickListener {
            mListener?.let { listener ->
                mFeedXCard?.let { card ->
                    listener.onLihatProdukClicked(
                        feedXCard = card,
                        positionInFeed = mPostionInFeed,
                        products = mProducts
                    )
                }
            }
        }
        vodFullScreenIcon.setOnClickListener {
            isPaused = true
            vodLanjutMemontomBtn.gone()
            vodFrozenView.gone()
            val currentTime = videoPlayer?.getExoPlayer()?.currentPosition
            mFeedXCard?.let {  card ->
                mListener?.onFullScreenBtnClicked(
                    card,
                    mPostionInFeed,
                    card.appLink,
                    currentTime?:0L,
                    shouldTrack = true,
                    true
                )
            }
        }
        vodLanjutMemontomBtn.setOnClickListener {
            vodLanjutMemontomBtn.gone()
            vodFrozenView.gone()
            videoPlayer?.getExoPlayer()?.currentPosition?.let { currentTime ->
                mFeedXCard?.let { card ->
                    mListener?.onFullScreenBtnClicked(
                        card,
                        mPostionInFeed,
                        card.appLink,
                        currentTime,
                        false,
                        false
                    )
                }
            }
        }
        vodVolumeIcon.setOnClickListener {
            mIsMute = !mIsMute
            videoPlayer?.toggleVideoVolume(mIsMute)
            changeMuteStateVideoVOD(mIsMute)
            setMuteUnmuteVOD(isVideoTap = false, isMute = mIsMute)
        }
        vodPlayIcon.setOnClickListener {
            startVideoPlayer()
        }
    }

    fun setListener(listener : VODListener){
        this.mListener = listener
    }


    private fun changeMuteStateVideoVOD(isMute: Boolean) {
        mOnClickVolumeListener?.invoke()
        if (isMute) {
            vodVolumeIcon.setImageResource(R.drawable.ic_feed_volume_mute_large)
        } else {
            vodVolumeIcon.setImageResource(R.drawable.ic_feed_volume_up_large)
        }
    }
    private fun setMuteUnmuteVOD(isVideoTap: Boolean, isMute: Boolean) {
        val countDownTimer = object : CountDownTimer(TIME_THREE_SEC, TIME_SECOND) {
            override fun onTick(millisUntilFinished: Long) {

            }

            override fun onFinish() {
                vodVolumeIcon.gone()
            }
        }
        mFeedXCard?.let { card ->
            mListener?.onVolumeBtnClicked(
                card,
                isMute,
                mFeedXMedia?.type ?: ""
            )
        }
        if (!vodVolumeIcon.isVisible)
            vodVolumeIcon.visible()
        if (isVideoTap){
            if(countDownTimer != null)
                countDownTimer.cancel()
            countDownTimer.start()

        }
    }
    fun setVODControl(isMute: Boolean){
        this.mIsMute = isMute
        mShouldTrack = true
        isPaused = false
        if (videoPlayer == null)
            mFeedXMedia?.canPlay = true
        vodLanjutMemontomBtn.gone()
        vodFrozenView.gone()
        if (mProducts.isEmpty()) {
            vodLihatProdukBtn.gone()
        } else {
            vodLihatProdukBtn.visible()
            hideViewWithAnimationVod(vodLihatProdukBtn, context)
        }
        vodFullScreenIcon.visible()
        updateVolumeButtonState()

        showLihatProdukWithAnimation()
        startVideoPlayer()

    }
    private fun updateVolumeButtonState(){
        if (mIsMute) {
            vodVolumeIcon.setImageResource(R.drawable.ic_feed_volume_mute_large)
        } else {
            vodVolumeIcon.setImageResource(R.drawable.ic_feed_volume_up_large)
        }
    }
    private fun showLihatProdukWithAnimation(){
        if (handlerAnim == null) {
            handlerAnim = Handler(Looper.getMainLooper())
        }
        handlerAnim?.postDelayed({
            if (mProducts.isNotEmpty()) {
                showViewWithAnimationVOD(vodLihatProdukBtn, context)
            }
        }, TIME_SECOND)

    }
    private fun startVideoPlayer() {
        isVODVideoViewFrozen = false
        secondCountDownTimer = null
        productVideoJob?.cancel()
        if (videoPlayer == null)
            videoPlayer = FeedExoPlayer(context)
        productVideoJob = scope.launch {
            layoutPlayerView.player = videoPlayer?.getExoPlayer()
            layoutPlayerView.videoSurfaceView?.setOnClickListener {
                if (mFeedXMedia?.mediaUrl?.isNotEmpty() == true && !isVODVideoViewFrozen) {
                    mIsMute = !mIsMute
                    videoPlayer?.toggleVideoVolume(mIsMute)
                    changeMuteStateVideoVOD(mIsMute)
                    setMuteUnmuteVOD(isVideoTap = true, isMute = mIsMute)
                }
            }
            mFeedXMedia?.let { videoPlayer?.start(it.mediaUrl, mIsMute) }
            vodVolumeIcon.visible()
            videoPlayer?.setVideoStateListener(object : VideoStateListener {
                override fun onInitialStateLoading() {
                    showVODLoading()
                    isPaused = false
                    isVODVideoViewFrozen = false
                    vodLihatProdukBtn.showWithCondition(mProducts.isNotEmpty())

                }
                override fun onVideoReadyToPlay() {
                    hideVODLoading()

                    if (!isPaused) {
                        showLanjutMenontonAfterThirtySeconds()
                    }

                    addViewsAfterFiveSeconds()

                    if (!isPaused) {
                        updateTimerViewForThreeSeconds()
                    }
                }

                override fun onVideoStateChange(stopDuration: Long, videoDuration: Long) {
                    mFeedXMedia?.canPlay = false
                    mFeedXCard?.let { card ->
                        mListener?.addViewsToVOD(
                            card,
                            mPostionInFeed,
                            (videoPlayer?.getExoPlayer()?.currentPosition ?: 0L) / TIME_SECOND,
                            false
                        )
                        mListener?.onVODStopTrack(
                            this@FeedVODViewHolder,
                            videoPlayer?.getExoPlayer()?.currentPosition.orZero() / TIME_SECOND
                        )
                    }

                }
            })
        }
    }
    private fun addViewsAfterFiveSeconds(){
        feedAddViewJob?.cancel()
        feedAddViewJob = scope.launch {
            delay(TIME_FIVE_SEC)
            if (!isPaused) {
                val view = mFeedXCard?.views
                val count = view?.count?.let { it + 1 }
                count?.let {
                    if (view.count != 0) {
                        val viewText = MethodChecker.fromHtml(
                            context.getString(
                                R.string.feed_component_viewed_count_text,
                                it.productThousandFormatted(1)
                            )
                        )
                        mUpdateViewsText?.invoke(viewText.toString())
                    }
                }
                mFeedXCard?.let { card ->
                    mListener?.addViewsToVOD(
                        card,
                        mPostionInFeed,
                        TIME_FIVE_SEC,
                        true
                    )
                }
                mShouldTrack = false
            }
        }
    }
    private fun updateTimerViewForThreeSeconds(){
        vodTimerView.visible()
        var time = (videoPlayer?.getExoPlayer()?.duration
            ?: 0L) / TIME_SECOND + 1
        object : CountDownTimer(TIME_THREE_SEC, TIME_SECOND) {
            override fun onTick(millisUntilFinished: Long) {

                if (time < HOUR_IN_HOUR) {
                    vodTimerView.text =
                        String.format(
                            TIME_FORMAT_WITHOUT_HOURS,
                            (time / MINUTE_IN_HOUR) % MINUTE_IN_HOUR,
                            time % MINUTE_IN_HOUR
                        )
                } else {
                    vodTimerView.text =
                        String.format(
                            TIME_FORMAT_WITH_HOURS,
                            (time / HOUR_IN_HOUR) % HOUR_IN_HOUR,
                            (time / MINUTE_IN_HOUR) % MINUTE_IN_HOUR,
                            time % MINUTE_IN_HOUR
                        )
                }
                time -= 1
            }

            override fun onFinish() {
                vodTimerView.gone()
                vodVolumeIcon.gone()
            }
        }.start()
    }
    private fun showLanjutMenontonAfterThirtySeconds(){
        if (secondCountDownTimer != null) {
            secondCountDownTimer?.cancel()
            secondCountDownTimer?.start()
        }
        else {
            secondCountDownTimer =
                object : CountDownTimer(TIME_THIRTY_SEC, TIME_SECOND) {
                    override fun onTick(millisUntilFinished: Long) {
                    }
                    override fun onFinish() {
                        videoPlayer?.pause()
                        isPaused = true

                        vodLanjutMemontomBtn.visible()
                        vodVolumeIcon.gone()
                        vodFrozenView.visible()
                        vodFullScreenIcon.gone()
                        vodLihatProdukBtn.gone()
                        vodTimerView.gone()
                        isVODVideoViewFrozen = true

                    }
                }.start()
        }
    }
    //call this to set view text
    fun updateLikedText(callback : (String) -> Unit) {
         mUpdateViewsText = callback
    }
    fun setChangeVolumeStateCallback(callback: () -> Unit) {
        mOnClickVolumeListener = callback
    }
    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause() {
        videoPlayer?.pause()
        secondCountDownTimer?.cancel()
        if (feedAddViewJob != null) {
            feedAddViewJob?.cancel()
            feedAddViewJob = null
        }
    }
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        videoPlayer?.resume()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        onViewDetached()
    }

    private fun showVODLoading() {
        vodLoader.animate()
        vodLoader.visible()
        vodPlayIcon.visible()
    }
    private fun hideVODLoading() {
        vodLoader.gone()
        vodPlayIcon.gone()
        vodTimerView.visible()
        vodPreviewImage.gone()
        vodVolumeIcon.visible()
        vodFullScreenIcon.visible()
        vodLanjutMemontomBtn.gone()
        vodFrozenView.gone()
    }
    fun onViewAttached(){
        vodPreviewImage.visible()
        vodPlayIcon.visible()
    }
    fun onViewDetached(){
        isPaused = true
        if (feedAddViewJob != null) {
            feedAddViewJob?.cancel()
            feedAddViewJob = null
        }
        if (secondCountDownTimer != null) {
            secondCountDownTimer?.cancel()
            secondCountDownTimer = null
        }
        if (videoPlayer != null) {
            videoPlayer?.pause()
            videoPlayer?.setVideoStateListener(null)
            videoPlayer?.destroy()
            videoPlayer = null
            layoutPlayerView.player = null
        }
        mFeedXMedia?.canPlay = false
    }

    companion object {
        private const val TIME_THREE_SEC = 3000L
        private const val TIME_SECOND = 1000L
        private const val TIME_FIVE_SEC = 5000L
        private const val MINUTE_IN_HOUR = 60
        private const val HOUR_IN_HOUR = 3600
        private const val TIME_THIRTY_SEC = 30000L
        private const val TIME_FORMAT_WITH_HOURS = "%02d:%02d:%02d"
        private const val TIME_FORMAT_WITHOUT_HOURS = "%02d:%02d"
    }
    interface VODListener {
        fun onLihatProdukClicked(
            feedXCard: FeedXCard,
            positionInFeed: Int,
            products: List<FeedXProduct>
        )
        fun onFullScreenBtnClicked(
            feedXCard: FeedXCard,
            positionInFeed: Int,
            redirectUrl: String,
            currentTime: Long,
            shouldTrack: Boolean,
            isFullScreenButton: Boolean
        )
        fun onVolumeBtnClicked(feedXCard: FeedXCard, mute: Boolean, mediaType: String)
        fun addViewsToVOD(
            feedXCard: FeedXCard,
            rowNumber: Int,
            time: Long,
            hitTrackerApi: Boolean
        )
        fun onVODStopTrack(viewHolder: FeedVODViewHolder, lastPosition: Long)

    }
    
}
