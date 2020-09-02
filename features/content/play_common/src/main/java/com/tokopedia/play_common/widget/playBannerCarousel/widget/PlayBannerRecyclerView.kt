package com.tokopedia.play_common.widget.playBannerCarousel.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Point
import android.net.Uri
import android.os.CountDownTimer
import android.os.Handler
import android.util.AttributeSet
import android.view.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.ParserException
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.source.dash.DashMediaSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.source.smoothstreaming.SsMediaSource
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.upstream.DefaultLoadErrorHandlingPolicy
import com.google.android.exoplayer2.upstream.LoadErrorHandlingPolicy
import com.google.android.exoplayer2.upstream.Loader
import com.google.android.exoplayer2.util.Util
import com.tokopedia.config.GlobalConfig
import com.tokopedia.play_common.R
import com.tokopedia.play_common.util.PlayConnectionCommon
import com.tokopedia.play_common.widget.playBannerCarousel.helper.GravitySnapHelper
import com.tokopedia.play_common.widget.playBannerCarousel.model.PlayBannerCarouselItemDataModel
import com.tokopedia.play_common.widget.playBannerCarousel.model.PlayBannerWidgetType
import com.tokopedia.play_common.widget.playBannerCarousel.model.ViewPlayerModel
import com.tokopedia.play_common.widget.playBannerCarousel.typeFactory.BasePlayBannerCarouselModel
import com.tokopedia.play_common.widget.playBannerCarousel.viewHolder.PlayBannerCarouselItemViewHolder
import timber.log.Timber
import java.io.FileNotFoundException
import java.io.IOException
import java.util.*

@SuppressLint("SyntheticAccessor")
class PlayBannerRecyclerView(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : RecyclerView(context, attrs, defStyleAttr) {

    private val snapListener = object : GravitySnapHelper.SnapListener{
        override fun onSnap(recyclerView: RecyclerView?, position: Int) {
            if (recyclerView?.canScrollHorizontally(1) == false) {
                playVideos(true)
            } else {
                playVideos(false)
            }
        }
    }

    private val gravitySnapHelper = GravitySnapHelper(Gravity.START, snapListener)

    /**
     * List of ExoPlayer (now playing)
     */
    private val videoPlayers: MutableList<ViewPlayerModel> = mutableListOf()

    /**
     * Previous position
     */
    private val previousPosition = mutableListOf<Int>()

    /**
     * variable declaration
     */
    // Media List
    private val mediaObjects: MutableList<BasePlayBannerCarouselModel> = mutableListOf()
    private val mediaObjectsLastPosition = mutableListOf<Int>()
    private var isAutoPlay: Boolean = false

    /* In Millisecond */
    private var durationPlayWithData: Int = 0
    private var delayDuration: Int = 0

    /* Surface height */
    private val screenDefaultHeight: Int

    constructor(context: Context) : this(context, null, 0)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    init {
        gravitySnapHelper.attachToRecyclerView(this)

        addOnChildAttachStateChangeListener(object : OnChildAttachStateChangeListener {
            override fun onChildViewAttachedToWindow(view: View) {}

            override fun onChildViewDetachedFromWindow(view: View) {
                log("On Detach From Window: ${view.tag}")
                resetVideoView(view.tag)
            }
        })
        val display: Display = (Objects.requireNonNull(
                getContext().getSystemService(Context.WINDOW_SERVICE)) as WindowManager).defaultDisplay
        val point = Point()
        display.getSize(point)
        screenDefaultHeight = point.y
    }

    fun playVideos(isEndOfList: Boolean) {
        try{
            // check surface height first, if not visible 100% it will stop all videos
//            if(!getVisibleVideoSurfaceHeight()){
//                resetVideoPlayer()
//                return
//            }
            // check internet wifi or data available or auto play or video data is not empty
            if(!(PlayConnectionCommon.isConnectWifi(context) || PlayConnectionCommon.isConnectCellular(context)) || !isAutoPlay || mediaObjects.isEmpty()) return
            val targetPositions: MutableList<Int> = mutableListOf()
            var startPosition: Int = (Objects.requireNonNull(
                    layoutManager) as LinearLayoutManager).findFirstVisibleItemPosition()
            var endPosition: Int = (layoutManager as LinearLayoutManager).findLastVisibleItemPosition()

            // if first position is not play card (empty / another view holder), set startPosition + 1
            if(mediaObjects[startPosition] !is PlayBannerCarouselItemDataModel){
                if(startPosition != endPosition){
                    startPosition++
                }
            }

            // if end position is see more card, set end position - 1
            if(mediaObjects[endPosition] !is PlayBannerCarouselItemDataModel){
                endPosition--
            }

            log("Find position range: $startPosition - $endPosition")

            // if end of list play from right to start
            if(isEndOfList){
                // if there are 3 items visible, play from last and middle item
                if(endPosition - startPosition == 2){
                    startPosition += 1
                }
                log("End of list update position: $startPosition - $endPosition")
            }

            // something is wrong. return.
            if (startPosition < 0 || endPosition < 0) {
                return
            }

            // if there is more than n list-item on the screen
            // check percentage view visible < 49% will take the second item and third item
            // else will take first item and second item
            for(i in startPosition .. endPosition){
                if(getVisibleVideoSurfaceWidth(i) > 0 && mediaObjects[i] is PlayBannerCarouselItemDataModel
                        && (mediaObjects[i] as PlayBannerCarouselItemDataModel).videoUrl.isNotEmpty()
                        && (mediaObjects[i] as PlayBannerCarouselItemDataModel).widgetType != PlayBannerWidgetType.UPCOMING) targetPositions.add(i)
                if(targetPositions.size == videoPlayers.size) break
            }

            log("Position play: $targetPositions")

            log("Previous Position: $previousPosition")

            // find new position
            val newPositions = targetPositions.filter { !previousPosition.contains(it) }

            log("New Position: $newPositions")

            if(newPositions.isEmpty()){
                // find prev contain target position
                previousPosition.clear()
                for (targetPosition in targetPositions){
                    if(previousPosition.contains(targetPosition)){
                        previousPosition.add(targetPosition)
                    }
                }

                // find videoPlayer not playing and stop it
                for (videoPlayer in videoPlayers){
                    if(!targetPositions.contains(videoPlayer.position)){
                        removeVideoView(videoPlayer)
                    }
                }

                return
            }

            previousPosition.clear()
            previousPosition.addAll(targetPositions)

            for (playPosition in newPositions){
                for (videoPlayer in videoPlayers){
                    log("New Position Contains : ${videoPlayer.position}")
                    if(!targetPositions.contains(videoPlayer.position)){
                        log("On Remove Video View: at ${videoPlayer.position}")
                        removeVideoView(videoPlayer)
                        log("On Play Video: $playPosition")
                        playVideo(videoPlayer, playPosition)
                        break
                    }
                }
            }
        } catch (exception: Exception) {
            exception.printStackTrace()
            log(exception.toString())
        }
    }

    private fun playVideo(videoPlayer: ViewPlayerModel, playPosition: Int){
        val holder = findViewHolderForAdapterPosition(playPosition) ?: return
        val dataObject = (mediaObjects[playPosition] as PlayBannerCarouselItemDataModel)
        if(holder is PlayBannerCarouselItemViewHolder && dataObject.videoUrl.isNotEmpty() && dataObject.widgetType != PlayBannerWidgetType.UPCOMING){
            removeVideoView(videoPlayer)
            val exoPlayer = videoPlayer.videoPlayer
            val mediaUrl = dataObject.videoUrl
            val videoSource: MediaSource = getMediaSourceBySource(context, Uri.parse(mediaUrl), "Tokopedia Android $playPosition")
            exoPlayer.volume = 0f
            videoPlayer.position = playPosition
            videoPlayer.viewHolderParent = holder.itemView
            exoPlayer.seekTo(mediaObjectsLastPosition[playPosition].toLong())
            exoPlayer.prepare(videoSource)
            videoPlayer.timerAutoPlay?.start()
        }
    }

    private fun addVideoView(videoPlayer: ViewPlayerModel){
        val holder = videoPlayer.viewHolderParent?.tag
        if(holder != null && holder is PlayBannerCarouselItemViewHolder && videoPlayer.videoView.parent == null){
            holder.containerPlayer.addView(videoPlayer.videoView)
        }
    }

    private fun getVisibleVideoSurfaceHeight(): Boolean{
        val parent = this
        val location = IntArray(2)
        parent.getLocationInWindow(location)
        val yParentPosition = if(location[1] <= 0) screenDefaultHeight else location[1]
        return yParentPosition + parent.height < screenDefaultHeight && yParentPosition >= 0
    }

    /**
     * Returns the visible region of the video surface on the screen.
     * if some is cut off, it will return less than the @videoSurfaceDefaultHeight
     */
    private fun getVisibleVideoSurfaceWidth(playPosition: Int): Int {
        val viewHolder = findViewHolderForAdapterPosition(playPosition) ?: return 0
        val child = viewHolder.itemView
        val location = IntArray(2)
        child.getLocationInWindow(location)
        val xPosition = if(location[0] < 0) location[0] else 0
        val visibleWidthChild = ((child.width + xPosition).toDouble() / child.width.toDouble()) * 100
        return if(visibleWidthChild < 99){
            -1
        } else {
            1
        }
    }

    // Remove the old player
    private fun removeVideoView(videoPlayer: ViewPlayerModel) {
        val playPosition = videoPlayer.position
        val parent: ViewGroup = (videoPlayer.videoView.parent as? ViewGroup) ?: return
        val index = parent.indexOfChild(videoPlayer.videoView)
        if (index >= 0) {
            parent.removeViewAt(index)
            mediaObjectsLastPosition[playPosition] = videoPlayer.videoPlayer.currentPosition.toInt()
            videoPlayer.videoPlayer.stop(true)
        }
        videoPlayer.position = -1
        videoPlayer.viewHolderParent = null
        videoPlayer.timerAutoPlay?.cancel()
        videoPlayer.timerAutoPause?.cancel()
    }

    private fun resetVideoView(viewHolder: Any?) {
        if(viewHolder != null && viewHolder is PlayBannerCarouselItemViewHolder){
            for (videoPlayer in videoPlayers){
                if(videoPlayer.viewHolderParent == viewHolder && videoPlayer.videoView.parent != null){
                    removeVideoView(videoPlayer)
                    return
                }
            }
        }
    }

    fun pausePlayers(){
        for(videoPlayer in videoPlayers){
            videoPlayer.videoPlayer.playWhenReady = false
        }
    }

    fun resumePlayers(){
        for(videoPlayer in videoPlayers){
            videoPlayer.videoPlayer.playWhenReady = true
        }
    }

    fun releasePlayer() {
        for(videoPlayer in videoPlayers){
            videoPlayer.videoPlayer.release()
        }
        resetVideoPlayer()
    }

    fun resetVideoPlayer(){
        previousPosition.clear()
        for(videoPlayer in videoPlayers){
            removeVideoView(videoPlayer)
        }
    }

    fun setMedia(list: List<BasePlayBannerCarouselModel>){
        this.mediaObjects.clear()
        this.mediaObjects.addAll(list)
        this.mediaObjectsLastPosition.addAll(list.map { 0 })
        resetVideoPlayer()
        Handler().postDelayed({
            playVideos(false)
        }, 500)
    }

    /* in seconds */
    fun setDelayDuration(delayPlayVideo: Int, stopTimeVideoWihData: Int){
        /* convert to millisecond */
        delayDuration = delayPlayVideo * 1000
        durationPlayWithData = stopTimeVideoWihData * 1000
    }

    fun setAutoPlay(isAutoPlay: Boolean, autoPlayAmount: Int){
        this.isAutoPlay = isAutoPlay

        // clear old players
        for (videoPlayer in videoPlayers) {
            videoPlayer.videoPlayer.release()
            removeVideoView(videoPlayer)
        }
        videoPlayers.clear()

        for(i in autoPlayAmount - 1 downTo 0) {
            val exoPlayer = SimpleExoPlayer.Builder(context).build()
            exoPlayer.volume = 0f

            val videoSurfaceView = VideoPlayerView(context)
            videoSurfaceView.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
            videoSurfaceView.setPlayer(exoPlayer)
            val layoutParams = videoSurfaceView.findViewById<AspectRatioFrameLayout>(R.id.exo_content_frame).layoutParams
            layoutParams.width = this.resources.getDimensionPixelOffset(R.dimen.play_banner_item_carousel_width)
            layoutParams.height = this.resources.getDimensionPixelOffset(R.dimen.play_banner_item_carousel_height)
            videoSurfaceView.layoutParams = layoutParams
            videoSurfaceView.findViewById<AspectRatioFrameLayout>(R.id.exo_content_frame).requestLayout()
            var timerAutoPause: CountDownTimer?=null
            if(PlayConnectionCommon.isConnectCellular(context)) {
                timerAutoPause = object : CountDownTimer(durationPlayWithData.toLong(), 1000) {
                    override fun onFinish() {
                        videoSurfaceView.getPlayer()?.playWhenReady = false
                    }

                    override fun onTick(millisUntilFinished: Long) {}
                }
            }

            val timerAutoPlay = object : CountDownTimer(delayDuration.toLong(), 1000) {
                override fun onFinish() {
                    exoPlayer.playWhenReady = true
                    timerAutoPause?.start()
                }

                override fun onTick(millisUntilFinished: Long) {}
            }

            val videoPlayer = ViewPlayerModel(videoSurfaceView, exoPlayer,-1, timerAutoPlay = timerAutoPlay, timerAutoPause = timerAutoPause)
            exoPlayer.addListener(object : Player.EventListener {
                override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                    when (playbackState) {
                        Player.STATE_BUFFERING -> { }
                        Player.STATE_ENDED -> {
                            videoPlayer.videoPlayer.seekTo(0)
                        }
                        Player.STATE_IDLE -> { }
                        Player.STATE_READY -> {
                            addVideoView(videoPlayer)
                        }
                        else -> { }
                    }
                }
            })

            videoPlayers.add(videoPlayer)
        }
    }

    private fun getMediaSourceBySource(context: Context, uri: Uri, appName: String): MediaSource {
        val mDataSourceFactory = DefaultDataSourceFactory(context, Util.getUserAgent(context, appName))
        val errorHandlingPolicy = getErrorHandlingPolicy()
        val mediaSource = when (val type = Util.inferContentType(uri)) {
            C.TYPE_SS -> SsMediaSource.Factory(mDataSourceFactory).setLoadErrorHandlingPolicy(errorHandlingPolicy)
            C.TYPE_DASH -> DashMediaSource.Factory(mDataSourceFactory).setLoadErrorHandlingPolicy(errorHandlingPolicy)
            C.TYPE_HLS -> HlsMediaSource.Factory(mDataSourceFactory).setLoadErrorHandlingPolicy(errorHandlingPolicy)
            C.TYPE_OTHER -> ProgressiveMediaSource.Factory(mDataSourceFactory).setLoadErrorHandlingPolicy(errorHandlingPolicy)
            else -> throw IllegalStateException("Unsupported type: $type")
        }
        return mediaSource.createMediaSource(uri)
    }

    private fun getErrorHandlingPolicy(): LoadErrorHandlingPolicy {
        return object : DefaultLoadErrorHandlingPolicy() {
            override fun getRetryDelayMsFor(dataType: Int, loadDurationMs: Long, exception: IOException?, errorCount: Int): Long {
                return if (exception is ParserException || exception is FileNotFoundException || exception is Loader.UnexpectedLoaderException) C.TIME_UNSET
                else (errorCount * RETRY_DELAY) + RETRY_DELAY
            }

            override fun getMinimumLoadableRetryCount(dataType: Int): Int {
                return if (dataType == C.DATA_TYPE_MEDIA_PROGRESSIVE_LIVE) RETRY_COUNT_LIVE else RETRY_COUNT_DEFAULT
            }
        }
    }

    private fun log(message: String){
        if(GlobalConfig.DEBUG) Timber.tag(this::class.simpleName ?: "").e(message)
    }

    companion object{
        private const val RETRY_COUNT_LIVE = 1
        private const val RETRY_COUNT_DEFAULT = 2
        private const val RETRY_DELAY = 2000L

    }
}