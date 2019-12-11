package com.tokopedia.home.beranda.presentation.view.helper

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.source.ConcatenatingMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.source.dash.DashMediaSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.source.smoothstreaming.SsMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.*
import com.google.android.exoplayer2.upstream.cache.*
import com.google.android.exoplayer2.util.Util
import com.tokopedia.home.R
import timber.log.Timber
import java.io.File


@SuppressWarnings("WeakerAccess")
class TokopediaPlayerHelper(
        private val context: Context,
        private val exoPlayerView: PlayerView
) :
        View.OnClickListener,
        View.OnTouchListener,
        ExoPlayerControl,
        ExoPlayerStatus,
        Player.EventListener {

    companion object {
        const val PARAM_AUTO_PLAY = "PARAM_AUTO_PLAY"
        const val PARAM_WINDOW = "PARAM_WINDOW"
        const val PARAM_POSITION = "PARAM_POSITION"
        const val PARAM_IS_AD_WAS_SHOWN = "PARAM_IS_AD_WAS_SHOWN"
    }


    private var mPlayer: SimpleExoPlayer? = null
    private var mDataSourceFactory: DataSource.Factory? = null
    private var mLoadControl: DefaultLoadControl? = null
    private var mMediaSource: MediaSource? = null

    private var mProgressBar: ProgressBar? = null
    private var mThumbImage: ImageView? = null

    private var mExoPlayerListener: ExoPlayerListener? = null
    private var mExoThumbListener: ExoThumbListener? = null


    private var mVideosUris: Array<Uri>? = null
    private var mTagUrl: String? = null
    private var mResumePosition = C.TIME_UNSET
    private var mResumeWindow = C.INDEX_UNSET
    private var mTempCurrentVolume = 0f
    private var isVideoMuted = false
    private var isRepeatModeOn = false
    private var isAutoPlayOn = false
    private var isResumePlayWhenReady = false
    private var isAdWasShown = false
    private var isPlayerPrepared = false
    private var isToPrepareOnResume = true
    private var isThumbImageViewEnabled = false
    private var isLiveStreamSupportEnabled = false
    private var mBottomProgress: LinearLayout? = null

    init {
        setControllerListener()
        init()
    }

    private fun addProgressBar(color: Int) {
        val frameLayout = exoPlayerView.overlayFrameLayout
        mProgressBar = frameLayout?.findViewById(R.id.progressBar)
        if (mProgressBar != null) {
            return
        }
        mProgressBar = ProgressBar(context, null, R.attr.progressBarStyle)
        mProgressBar?.id = R.id.progressBar
        val params = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT)
        params.gravity = Gravity.CENTER
        mProgressBar?.layoutParams = params
        mProgressBar?.isIndeterminate = true
        mProgressBar?.indeterminateDrawable?.setColorFilter(
                if (color == 0) Color.RED else color,
                PorterDuff.Mode.SRC_IN)
        mProgressBar?.visibility = View.GONE
        frameLayout?.addView(mProgressBar)
    }

    private fun setVideoClickable() {
        exoPlayerView.setOnTouchListener(this)
    }

    private fun setControllerListener() {

    }

    private fun init(){
        // Measures bandwidth during playback. Can be null if not required.
        // Measures bandwidth during playback. Can be null if not required.
        val bandwidthMeter = DefaultBandwidthMeter.Builder(context).build()

        // Produces DataSource instances through which media data is loaded.
        // Produces DataSource instances through which media data is loaded.
        mDataSourceFactory = DefaultDataSourceFactory(context,
                Util.getUserAgent(context, context.getString(R.string.app_name)), bandwidthMeter)


        // LoadControl that controls when the MediaSource buffers more media, and how much media is buffered.
        // LoadControl is injected when the player is created.
        //removed deprecated DefaultLoadControl creation method
        // LoadControl that controls when the MediaSource buffers more media, and how much media is buffered.
        // LoadControl is injected when the player is created.
        //removed deprecated DefaultLoadControl creation method
        val builder = DefaultLoadControl.Builder()
        builder.setAllocator(DefaultAllocator(true, 2 * 1024 * 1024))
        builder.setBufferDurationsMs(5000, 5000, 5000, 5000)
        builder.setPrioritizeTimeOverSizeThresholds(true)
        mLoadControl = builder.createDefaultLoadControl()
    }

    // Player creation and release
    private fun setVideoUrls(vararg urls: String) {
        mVideosUris = Array(urls.size){
            Uri.parse(urls[it])
        }
    }

    private fun createMediaSource() {
        // A MediaSource defines the media to be played, loads the media, and from which the loaded media can be read.
        // A MediaSource is injected via ExoPlayer.prepare at the start of playback.
        mVideosUris?.let {
            val mediaSources: MutableList<MediaSource> = mutableListOf()
            it.forEach {uri ->
                mediaSources.add(buildMediaSource(uri))
            }
            mMediaSource = if(mediaSources.size == 1)  mediaSources[0] else ConcatenatingMediaSource(*mediaSources.toTypedArray())

        }
    }

    private fun buildMediaSource(uri: Uri?): MediaSource {
        return when (val type = Util.inferContentType(uri)) {
            C.TYPE_SS -> SsMediaSource.Factory(mDataSourceFactory).createMediaSource(uri)
            C.TYPE_DASH -> DashMediaSource.Factory(mDataSourceFactory).createMediaSource(uri)
            C.TYPE_HLS -> HlsMediaSource.Factory(mDataSourceFactory).createMediaSource(uri)
            C.TYPE_OTHER -> ProgressiveMediaSource.Factory(mDataSourceFactory).createMediaSource(uri)
            else -> throw IllegalStateException("Unsupported type: $type")
        }
    }

    private fun setProgressVisible(visible: Boolean) {
        if (mProgressBar != null) {
            mProgressBar!!.visibility = if (visible) View.VISIBLE else View.GONE
        }
    }

    private fun addThumbImageView() {
        if (mThumbImage != null) {
            return;
        }
        val frameLayout = exoPlayerView.findViewById<AspectRatioFrameLayout>(R.id.exo_content_frame);
        mThumbImage = ImageView(context)
        mThumbImage?.id = R.id.thumbnail
        val  params = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT)
        params.gravity = Gravity.CENTER
        mThumbImage?.layoutParams = params
        mThumbImage?.setBackgroundColor(Color.BLACK)
        frameLayout?.addView(mThumbImage)

        mExoThumbListener?.onThumbImageViewReady(mThumbImage)
    }

    private fun removeThumbImageView() {
        if (mThumbImage != null) {
            val frameLayout: AspectRatioFrameLayout = exoPlayerView.findViewById(R.id.exo_content_frame)
            frameLayout.removeView(mThumbImage)
            mThumbImage = null
        }
    }

    private fun enableCache(maxCacheSizeMb: Long) {
        val evictor = LeastRecentlyUsedCacheEvictor(maxCacheSizeMb * 1024 * 1024)
        val file = File(context.cacheDir, "media")
        Timber.tag(TokopediaPlayerHelper::class.java.name).d("enableCache (" + maxCacheSizeMb + " MB), file: " + file.absolutePath)
        val simpleCache = SimpleCache(file, evictor)
        mDataSourceFactory = CacheDataSourceFactory(
                simpleCache,
                mDataSourceFactory,
                FileDataSourceFactory(),
                CacheDataSinkFactory(simpleCache, 2 * 1024 * 1024),
                CacheDataSource.FLAG_BLOCK_ON_CACHE or CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR,
                object : CacheDataSource.EventListener {
                    override fun onCacheIgnored(reason: Int) {
                        Timber.tag(TokopediaPlayerHelper::class.java.name).d("onCacheIgnored")
                    }

                    override fun onCachedBytesRead(cacheSizeBytes: Long, cachedBytesRead: Long) {
                        Timber.tag(TokopediaPlayerHelper::class.java.name).d("%s%s", "onCachedBytesRead , cacheSizeBytes: " + cacheSizeBytes + "   cachedBytesRead: ", cachedBytesRead);
                    }
                })
    }

    override fun onClick(v: View?) {
        if (mExoPlayerListener != null && v?.id == R.id.exo_content_frame) {
            mExoPlayerListener?.onVideoTapped()
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(view: View, motionEvent: MotionEvent): Boolean {
        if (motionEvent.action == MotionEvent.ACTION_UP && mExoPlayerListener != null) {
            if (view.id == exoPlayerView.id) {
                mExoPlayerListener?.onVideoTapped()
            }
        }

        // Player block
        val layout = exoPlayerView.overlayFrameLayout
        return layout != null && view.id == layout.id
    }

    // Resume position saving
    private fun addSavedInstanceState(savedInstanceState: Bundle) {
        isAdWasShown = savedInstanceState.getBoolean(PARAM_IS_AD_WAS_SHOWN, false);
        isResumePlayWhenReady = savedInstanceState.getBoolean(PARAM_AUTO_PLAY, true);
        mResumeWindow = savedInstanceState.getInt(PARAM_WINDOW, C.INDEX_UNSET);
        mResumePosition = savedInstanceState.getLong(PARAM_POSITION, C.TIME_UNSET);
    }

    private fun updateResumePosition() {
        isResumePlayWhenReady = mPlayer?.playWhenReady ?: false
        mResumeWindow = mPlayer?.currentWindowIndex ?: C.INDEX_UNSET
        mResumePosition = Math.max(0, mPlayer?.contentPosition ?: 0)
    }

    private fun clearResumePosition() {
        mResumeWindow = C.INDEX_UNSET
        mResumePosition = C.TIME_UNSET
    }


    private fun getNextWindowIndex(): Int {
        return mPlayer?.currentTimeline?.getNextWindowIndex(mPlayer!!.currentWindowIndex, mPlayer!!.repeatMode, false) ?: -1
    }

    private fun getPreviousWindowIndex(): Int {
        return mPlayer?.currentTimeline?.getPreviousWindowIndex(mPlayer!!.currentWindowIndex, mPlayer!!.repeatMode, false) ?: -1
    }


    // Player events, internal handle
    private fun onPlayerBuffering() {
        if (mPlayer!!.playWhenReady) {
            setProgressVisible(true)
        }
    }

    private fun onPlayerPlaying() {
        setProgressVisible(false)
        removeThumbImageView()
    }

    private fun onPlayerLoadingChanged() {
        liveStreamCheck()
    }

    private fun liveStreamCheck() {
        if (isLiveStreamSupportEnabled) {
            val isLiveStream = mPlayer!!.isCurrentWindowDynamic || !mPlayer!!.isCurrentWindowSeekable
/*            Log.e("ZAQ", "isCurrentWindowDynamic: " + mPlayer.isCurrentWindowDynamic());
            Log.e("ZAQ", "isCurrentWindowSeekable: " + mPlayer.isCurrentWindowDynamic());
            Log.e("ZAQ", "isLiveStream: " + isLiveStream);*/mBottomProgress!!.visibility = if (isLiveStream) View.GONE else View.VISIBLE
        }
    }

    private fun onPlayerPaused() {
        setProgressVisible(false)
    }

    @SuppressLint("SyntheticAccessor")
    class Builder(context: Context, exoPlayerView: PlayerView) {
        private val mExoPlayerHelper: TokopediaPlayerHelper = TokopediaPlayerHelper(context, exoPlayerView)

        fun setVideoUrls(vararg urls: String): Builder {
            mExoPlayerHelper.setVideoUrls(*urls)
            return this
        }

        fun setTagUrl(tagUrl: String?): Builder {
            mExoPlayerHelper.mTagUrl = tagUrl
            return this
        }

        fun setRepeatModeOn(isOn: Boolean): Builder {
            mExoPlayerHelper.isRepeatModeOn = isOn
            return this
        }

        fun setAutoPlayOn(isAutoPlayOn: Boolean): Builder {
            mExoPlayerHelper.isAutoPlayOn = isAutoPlayOn
            return this
        }

        fun setExoPlayerEventsListener(exoPlayerListener: ExoPlayerListener): Builder {
            mExoPlayerHelper.setExoPlayerEventsListener(exoPlayerListener)
            return this
        }


        fun addSavedInstanceState(savedInstanceState: Bundle): Builder {
            mExoPlayerHelper.addSavedInstanceState(savedInstanceState)
            return this
        }

        fun setThumbImageViewEnabled(exoThumbListener: ExoThumbListener?): Builder {
            mExoPlayerHelper.setExoThumbListener(exoThumbListener)
            return this
        }

        fun enableCache(maxCacheSizeMb: Int): Builder {
            mExoPlayerHelper.enableCache(maxCacheSizeMb.toLong())
            return this
        }

        /**
         * If you have a list of videos set isToPrepareOnResume to be false
         * to prevent auto prepare on activity onResume/onCreate
         */
        fun setToPrepareOnResume(toPrepareOnResume: Boolean): Builder {
            mExoPlayerHelper.isToPrepareOnResume = toPrepareOnResume
            return this
        }

        fun enableLiveStreamSupport(): Builder {
            mExoPlayerHelper.isLiveStreamSupportEnabled = true
            return this
        }

        fun addProgressBarWithColor(colorAccent: Int): Builder {
            mExoPlayerHelper.addProgressBar(colorAccent)
            return this
        }

        /**
         * Probably you will feel a need to use that method when you need to show pre-roll ad
         * and you not interested in auto play. That method allows to separate player creation
         * from calling prepare()
         * Note: To play ad/content you ned to call preparePlayer()
         *
         * @return ExoPlayerHelper instance
         */
        fun create(): TokopediaPlayerHelper {
            mExoPlayerHelper.createPlayer(false)
            return mExoPlayerHelper
        }

        /**
         * Note: If you added tagUrl ad would start playing automatic even if you had set setAutoPlayOn(false)
         *
         * @return ExoPlayerHelper instance
         */
        fun createAndPrepare(): TokopediaPlayerHelper {
            mExoPlayerHelper.createPlayer(true)
            return mExoPlayerHelper
        }

    }

    override fun createPlayer(isToPrepare: Boolean) {
        mExoPlayerListener?.createExoPlayerCalled(isToPrepare)

        if (mPlayer != null) {
            return;
        }

        if (isThumbImageViewEnabled) {
            addThumbImageView()
        }

        mPlayer = ExoPlayerFactory.newSimpleInstance(
                context,
                DefaultRenderersFactory(context),
                DefaultTrackSelector(),
                mLoadControl)

        exoPlayerView.player = mPlayer
        exoPlayerView.controllerShowTimeoutMs = 1500
        exoPlayerView.controllerHideOnTouch = false

        mTempCurrentVolume = mPlayer?.volume ?: 0f

        mPlayer?.repeatMode = if(isRepeatModeOn) Player.REPEAT_MODE_ALL else Player.REPEAT_MODE_OFF
        mPlayer?.playWhenReady = isAutoPlayOn
        mPlayer?.addListener(this)

        createMediaSource()

        if (isToPrepare) {
            preparePlayer()
        }
    }

    override fun preparePlayer() {
        if (mPlayer == null || isPlayerPrepared) {
            return
        }
        isPlayerPrepared = true

        mPlayer?.prepare(mMediaSource);

        if (mResumeWindow != C.INDEX_UNSET) {
            mPlayer?.playWhenReady = isResumePlayWhenReady
            mPlayer?.seekTo(mResumeWindow, mResumePosition + 100)
            mExoPlayerListener?.onVideoResumeDataLoaded(mResumeWindow, mResumePosition, isResumePlayWhenReady)
        }
    }

    override fun releasePlayer() {
        isPlayerPrepared = false

        mExoPlayerListener?.releaseExoPlayerCalled()

        if (mPlayer != null) {
            updateResumePosition()
            removeThumbImageView()
            mPlayer?.release()
            mPlayer = null
        }
    }

    override fun updateVideoUrls(vararg urls: String) {
        if (!isPlayerPrepared) {
            setVideoUrls(*urls)
            createMediaSource()
        } else {
            throw IllegalStateException("Can't update url's when player is prepared")
        }
    }

    override fun playerPause() {
        mPlayer?.playWhenReady = false
    }

    override fun playerPlay() {
        mPlayer?.playWhenReady = true
    }

    override fun seekTo(windowIndex: Int, positionMs: Long) {
        mPlayer?.seekTo(windowIndex, positionMs)
    }

    override fun seekToDefaultPosition() {
        mPlayer?.seekToDefaultPosition()
    }

    override fun setExoPlayerEventsListener(pExoPlayerListenerListener: ExoPlayerListener?) {
        mExoPlayerListener = pExoPlayerListenerListener;
    }

    override fun setExoThumbListener(exoThumbListener: ExoThumbListener?) {
        isThumbImageViewEnabled = true
        mExoThumbListener = exoThumbListener
    }

    override fun onActivityStart() {
        if (Util.SDK_INT > 23) {
            createPlayer(isToPrepareOnResume)
        }
    }

    override fun onActivityResume() {
        if ((Util.SDK_INT <= 23 || mPlayer == null)) {
            createPlayer(isToPrepareOnResume)
        }
    }

    override fun onActivityPause() {
        if (Util.SDK_INT <= 23) {
            releasePlayer()
        }
    }

    override fun onActivityStop() {
        if (Util.SDK_INT > 23) {
            releasePlayer()
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        mPlayer?.let {
            outState?.putBoolean(PARAM_IS_AD_WAS_SHOWN, !it.isPlayingAd)
            outState?.putBoolean(PARAM_AUTO_PLAY, it.playWhenReady)
            outState?.putInt(PARAM_WINDOW, it.currentWindowIndex)
            outState?.putLong(PARAM_POSITION, it.contentPosition)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun playerBlock() {
        if (exoPlayerView.overlayFrameLayout != null) {
            exoPlayerView.overlayFrameLayout?.setOnTouchListener(this)
        }
    }

    override fun playerUnBlock() {
        if (exoPlayerView.overlayFrameLayout != null) {
            exoPlayerView.overlayFrameLayout?.setOnTouchListener(null)
        }
    }

    override fun isPlayerVideoMuted(): Boolean {
        return isVideoMuted
    }

    override fun currentWindowIndex(): Int {
        return if (mPlayer != null) {
            mPlayer?.currentWindowIndex ?: 0
        } else {
            0
        }
    }

    override fun currentPosition(): Long {
        return if (mPlayer != null) {
            mPlayer?.currentPosition ?: 0
        } else {
            0
        }
    }

    override fun duration(): Long {
        return if (mPlayer != null) {
            mPlayer?.duration ?: 0
        } else {
            0
        }
    }

    override fun isPlayerCreated(): Boolean {
        return mPlayer != null
    }

    override fun isPlayerPrepared(): Boolean {
        return isPlayerPrepared
    }
}