package com.tokopedia.play_common.widget.playBannerCarousel.widget

import android.content.Context
import android.graphics.Point
import android.net.Uri
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.ProgressBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.ParserException
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.database.DatabaseProvider
import com.google.android.exoplayer2.database.ExoDatabaseProvider
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.source.dash.DashMediaSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.source.smoothstreaming.SsMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.upstream.DefaultLoadErrorHandlingPolicy
import com.google.android.exoplayer2.upstream.LoadErrorHandlingPolicy
import com.google.android.exoplayer2.upstream.Loader
import com.google.android.exoplayer2.upstream.cache.*
import com.google.android.exoplayer2.util.Util
import com.tokopedia.play_common.player.PlayVideoManager
import com.tokopedia.play_common.widget.playBannerCarousel.helper.GravitySnapHelper
import com.tokopedia.play_common.widget.playBannerCarousel.model.PlayBannerCarouselItemDataModel
import com.tokopedia.play_common.widget.playBannerCarousel.typeFactory.BasePlayBannerCarouselModel
import com.tokopedia.play_common.widget.playBannerCarousel.viewHolder.PlayBannerCarouselItemViewHolder
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.util.*


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
    private var listVideoPlayer = mutableListOf<SimpleExoPlayer>()

    /**
     * List of now playing
     */
    private var listPositionPlaying = mutableListOf<Int>()

    /**
     * PlayerViewHolder UI component
     * Watch PlayerViewHolder class
     */
//    private var mediaCoverImage: ImageView? = null
//    private var volumeControl: ImageView? = null
    private var progressBar: ProgressBar? = null
    private var viewHolderParent: View? = null
//    private var mediaContainer: FrameLayout? = null
//    private var videoSurfaceView: PlayerView? = null
    private var videoPlayer: SimpleExoPlayer? = null
    private var cache: Cache? = null

    /**
     * Cache
     */
    private val cacheFile: File
        get() = File(context.applicationContext.filesDir, CACHE_FOLDER_NAME)
    private val cacheEvictor: CacheEvictor
        get() = LeastRecentlyUsedCacheEvictor(MAX_CACHE_BYTES)
    private val cacheDbProvider: DatabaseProvider
        get() = ExoDatabaseProvider(context.applicationContext)

    /**
     * variable declaration
     */
    // Media List
    private val mediaObjects: MutableList<BasePlayBannerCarouselModel> = mutableListOf()
    private val mediaObjectsLastPosition = mutableListOf<Int>()

    private var videoSurfaceDefaultWidth = 0
    private var screenDefaultWidth = 0

    private var playPosition = -1
    private var isVideoViewAdded = false

    constructor(context: Context) : this(context, null, 0)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    init {
        val display = (Objects.requireNonNull(
                getContext().getSystemService(Context.WINDOW_SERVICE)) as WindowManager).defaultDisplay
        val point = Point()
        display.getSize(point)

        videoSurfaceDefaultWidth = point.x
        screenDefaultWidth = point.y
//        videoSurfaceView = PlayerView(context)
//        videoSurfaceView!!.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
        //Create the player using ExoPlayerFactory
        videoPlayer = SimpleExoPlayer.Builder(context).build()
        // Disable Player Control
//        videoSurfaceView!!.useController = false
        // Bind the player to the view.
//        videoSurfaceView!!.player = videoPlayer
        gravitySnapHelper.attachToRecyclerView(this)
//        addOnScrollListener(onScrollChangeListener)
        addOnChildAttachStateChangeListener(object : OnChildAttachStateChangeListener {
            override fun onChildViewAttachedToWindow(view: View) {}
            override fun onChildViewDetachedFromWindow(view: View) {
                if (viewHolderParent != null && viewHolderParent == view) {
                    resetVideoView()
                }
            }
        })

        videoPlayer!!.addListener(object : Player.EventListener {
            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                when (playbackState) {
                    Player.STATE_BUFFERING -> {
                        Log.e(TAG, "onPlayerStateChanged: Buffering video.")
                        if (progressBar != null) {
                            progressBar!!.visibility = View.VISIBLE
                        }
                    }
                    Player.STATE_ENDED -> {
                        Log.d(TAG, "onPlayerStateChanged: Video ended.")
                        videoPlayer!!.seekTo(0)
                    }
                    Player.STATE_IDLE -> {
                    }
                    Player.STATE_READY -> {
                        Log.e(TAG, "onPlayerStateChanged: Ready to play.")
                        if (progressBar != null) {
                            progressBar!!.visibility = View.GONE
                        }
                        if (!isVideoViewAdded) {
                            addVideoView()
                        }
                    }
                    else -> {
                    }
                }
            }
        })
    }

    fun playVideos(isEndOfList: Boolean) {
        try{
            val targetPosition: Int
            if (!isEndOfList) {
                var startPosition: Int = (Objects.requireNonNull(
                        layoutManager) as LinearLayoutManager).findFirstVisibleItemPosition()
                var endPosition: Int = (layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition()
                Log.d(TAG, "playVideo: origin range: $startPosition - $endPosition")
                // if first position is not play card (empty / another view holder), set startPosition + 1
                if(mediaObjects.get(startPosition) !is PlayBannerCarouselItemDataModel){
                    if(startPosition == endPosition){
                        return
                    }
                    startPosition++
                }
                // if end position is see more card, set end position - 1
                if(mediaObjects.get(endPosition) !is PlayBannerCarouselItemDataModel){
                    endPosition--
                }
                // if there is more than 2 list-items on the screen, set the difference to be 1
                if (endPosition - startPosition > 1) {
                    endPosition = startPosition + 1
                }
                // something is wrong. return.
                if (startPosition < 0 || endPosition < 0) {
                    return
                }
                Log.d(TAG, "playVideo: range: $startPosition - $endPosition")

                // if there is more than 1 list-item on the screen
                // check percentage view visible < 49% will take the second item
                // else will take first item
                targetPosition = if (startPosition != endPosition) {
                    val visibleWidthStartItem = getVisibleVideoSurfaceWidth(startPosition)
                    if (visibleWidthStartItem > 0) startPosition else endPosition
                } else {
                    startPosition
                }
//                targetPosition = startPosition
            } else {
                val lastPosition = mediaObjects.size - 1
                targetPosition = if(mediaObjects.get(lastPosition) !is PlayBannerCarouselItemDataModel) lastPosition-1 else lastPosition
            }
            Log.d(TAG, "playVideo: target position: $targetPosition")
            // video is already playing so return
            if (targetPosition == playPosition) {
                return
            }
            // remove any old surface views from previously playing videos
            removeVideoView()

            // set the position of the list-item that is to be played
            playPosition = targetPosition
//            if (videoSurfaceView == null) {
//                return
//            }

//            videoSurfaceView!!.visibility = View.INVISIBLE

//            val currentPosition: Int = targetPosition - (layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()
            val holder = findViewHolderForAdapterPosition(targetPosition) ?: return
            Log.d(TAG, "playVideo: current holder: $holder")
            if(holder !is PlayBannerCarouselItemViewHolder){
                playPosition = -1
                return
            }
//            mediaContainer = holder.mediaContainer
            progressBar = holder.progressBar
            viewHolderParent = holder.itemView;
//            videoSurfaceView!!.player = videoPlayer
            holder.playerView?.player = videoPlayer
            val mediaUrl: String = (mediaObjects[targetPosition] as PlayBannerCarouselItemDataModel).videoUrl
            val videoSource: MediaSource = getMediaSourceBySource(context, Uri.parse(mediaUrl))
            videoPlayer!!.prepare(videoSource)
            videoPlayer!!.seekTo(mediaObjectsLastPosition.get(playPosition).toLong())
            videoPlayer!!.playWhenReady = true
        }catch (exception: Exception){
            Log.e(TAG, "Error exception at: ${exception.message}")
        }

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
        Log.d(TAG, "getVisibleVideoSurfaceHeight: percentage: $$visibleWidthChild")
        return if(visibleWidthChild < 49){
            -1
        } else {
            1
        }
    }

    // Remove the old player
    private fun removeVideoView() {
        if(playPosition == -1) return

        val viewHolder = findViewHolderForAdapterPosition(playPosition)
        if(viewHolder != null && viewHolder is PlayBannerCarouselItemViewHolder){
            mediaObjectsLastPosition[playPosition] = videoPlayer?.currentPosition?.toInt() ?: 0
            viewHolder.playerView.player = null
            isVideoViewAdded = false
        }
//        val parent: ViewGroup = videoView.parent as ViewGroup? ?: return
//        val index: Int = parent.indexOfChild(videoView)
//        if (index >= 0) {
//            parent.removeViewAt(index)
//            isVideoViewAdded = false
//            viewHolderParent!!.setOnClickListener(null)
//        }
    }

    private fun addVideoView() {
//        mediaContainer!!.addView(videoSurfaceView)
        isVideoViewAdded = true
//        videoSurfaceView!!.requestFocus()
//        videoSurfaceView!!.visibility = View.VISIBLE
//        videoSurfaceView!!.alpha = 1f
//        mediaCoverImage!!.visibility = View.GONE
    }

    private fun resetVideoView() {
        if (isVideoViewAdded) {
            removeVideoView()
            playPosition = -1
//            videoSurfaceView!!.visibility = View.INVISIBLE
//            mediaCoverImage!!.visibility = View.VISIBLE
        }
    }

    fun releasePlayer() {
        if (videoPlayer != null) {
            videoPlayer!!.release()
            videoPlayer = null
        }
        viewHolderParent = null
    }

    fun onPausePlayer() {
        if (videoPlayer != null) {
            videoPlayer!!.stop(true)
        }
    }

    fun setMedia(list: List<BasePlayBannerCarouselModel>){
        this.mediaObjects.clear()
        this.mediaObjects.addAll(list)
        this.mediaObjectsLastPosition.addAll(list.map { 0 })
    }

    private fun getMediaSourceBySource(context: Context, uri: Uri): MediaSource {
        if(cache == null) cache = SimpleCache(
                cacheFile,
                cacheEvictor,
                cacheDbProvider
        )
        val mDataSourceFactory = DefaultDataSourceFactory(context, Util.getUserAgent(context, "Tokopedia Android"))
        val cacheDataSourceFactory = CacheDataSourceFactory(cache, mDataSourceFactory)
        val errorHandlingPolicy = getErrorHandlingPolicy()
        val mediaSource = when (val type = Util.inferContentType(uri)) {
            C.TYPE_SS -> SsMediaSource.Factory(cacheDataSourceFactory).setLoadErrorHandlingPolicy(errorHandlingPolicy)
            C.TYPE_DASH -> DashMediaSource.Factory(cacheDataSourceFactory).setLoadErrorHandlingPolicy(errorHandlingPolicy)
            C.TYPE_HLS -> HlsMediaSource.Factory(cacheDataSourceFactory).setLoadErrorHandlingPolicy(errorHandlingPolicy)
            C.TYPE_OTHER -> ProgressiveMediaSource.Factory(cacheDataSourceFactory).setLoadErrorHandlingPolicy(errorHandlingPolicy)
            else -> throw IllegalStateException("Unsupported type: $type")
        }
        return mediaSource.createMediaSource(uri)
    }

    private fun getErrorHandlingPolicy(): LoadErrorHandlingPolicy {
        return object : DefaultLoadErrorHandlingPolicy() {
            override fun getRetryDelayMsFor(dataType: Int, loadDurationMs: Long, exception: IOException?, errorCount: Int): Long {
                return if (exception is ParserException || exception is FileNotFoundException || exception is Loader.UnexpectedLoaderException) C.TIME_UNSET
                else (errorCount * PlayVideoManager.RETRY_DELAY) + PlayVideoManager.RETRY_DELAY
            }

            override fun getMinimumLoadableRetryCount(dataType: Int): Int {
                return if (dataType == C.DATA_TYPE_MEDIA_PROGRESSIVE_LIVE) PlayVideoManager.RETRY_COUNT_LIVE else PlayVideoManager.RETRY_COUNT_DEFAULT
            }
        }
    }
    companion object{
        private const val TAG = "ExoPlayerRecyclerView"
        private const val AppName = "Android ExoPlayer"
        private const val MAX_CACHE_BYTES: Long = 10 * 1024 * 1024
        private const val CACHE_FOLDER_NAME = "play_banner_video"
    }
}