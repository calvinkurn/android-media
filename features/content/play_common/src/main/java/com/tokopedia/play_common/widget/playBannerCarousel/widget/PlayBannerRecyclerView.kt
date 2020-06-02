package com.tokopedia.play_common.widget.playBannerCarousel.widget

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import android.view.View
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
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.upstream.DefaultLoadErrorHandlingPolicy
import com.google.android.exoplayer2.upstream.LoadErrorHandlingPolicy
import com.google.android.exoplayer2.upstream.Loader
import com.google.android.exoplayer2.util.Util
import com.tokopedia.play_common.player.PlayVideoManager
import com.tokopedia.play_common.widget.playBannerCarousel.helper.GravitySnapHelper
import com.tokopedia.play_common.widget.playBannerCarousel.model.PlayBannerCarouselItemDataModel
import com.tokopedia.play_common.widget.playBannerCarousel.model.ViewPlayerModel
import com.tokopedia.play_common.widget.playBannerCarousel.typeFactory.BasePlayBannerCarouselModel
import com.tokopedia.play_common.widget.playBannerCarousel.viewHolder.PlayBannerCarouselItemViewHolder
import java.io.FileNotFoundException
import java.io.IOException
import java.util.*

@SuppressLint("SyntheticAccessor","LogNotTimber","LogConditional")
class PlayBannerRecyclerView(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : RecyclerView(context, attrs, defStyleAttr) {

    private val snapListener = object : GravitySnapHelper.SnapListener{
        override fun onSnap(recyclerView: RecyclerView?, position: Int) {
            if (recyclerView?.canScrollHorizontally(1) == false) {
                playVideos()
            } else {
                playVideos()
            }
        }
    }

    private val gravitySnapHelper = GravitySnapHelper(Gravity.START, snapListener)

    /**
     * List of ExoPlayer (now playing)
     */
    private val videoPlayer1: ViewPlayerModel
    private val videoPlayer2: ViewPlayerModel

    /**
     * variable declaration
     */
    // Media List
    private val mediaObjects: MutableList<BasePlayBannerCarouselModel> = mutableListOf()
    private val mediaObjectsLastPosition = mutableListOf<Int>()

    constructor(context: Context) : this(context, null, 0)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    init {
        gravitySnapHelper.attachToRecyclerView(this)

        addOnChildAttachStateChangeListener(object : OnChildAttachStateChangeListener {
            override fun onChildViewAttachedToWindow(view: View) {}

            override fun onChildViewDetachedFromWindow(view: View) {
                resetVideoView(view.tag)
            }
        })

        //Create the player using ExoPlayerFactory
        val videoPlayer1 = SimpleExoPlayer.Builder(context).build()
        val videoPlayer2 = SimpleExoPlayer.Builder(context).build()
        videoPlayer1.volume = 0f
        videoPlayer2.volume = 0f

        this.videoPlayer1 = ViewPlayerModel(videoPlayer1, -1)
        this.videoPlayer2 = ViewPlayerModel(videoPlayer2, -1)
        videoPlayer1.addListener(object : Player.EventListener {
            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                when (playbackState) {
                    Player.STATE_BUFFERING -> {
                        Log.e(TAG, "onPlayerStateChanged 1: Buffering video.")
                    }
                    Player.STATE_ENDED -> {
                        Log.d(TAG, "onPlayerStateChanged 1: Video ended.")
                        this@PlayBannerRecyclerView.videoPlayer1.videoPlayer?.seekTo(0)
                    }
                    Player.STATE_IDLE -> { }
                    Player.STATE_READY -> {
                        Log.e(TAG, "onPlayerStateChanged 1: Ready to play.")
                    }
                    else -> { }
                }
            }
        })
        videoPlayer2.addListener(object : Player.EventListener {
            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                when (playbackState) {
                    Player.STATE_BUFFERING -> {
                        Log.e(TAG, "onPlayerStateChanged 2: Buffering video.")
                    }
                    Player.STATE_ENDED -> {
                        Log.d(TAG, "onPlayerStateChanged 2: Video ended.")
                        this@PlayBannerRecyclerView.videoPlayer2.videoPlayer?.seekTo(0)
                    }
                    Player.STATE_IDLE -> { }
                    Player.STATE_READY -> {
                        Log.e(TAG, "onPlayerStateChanged 2: Ready to play.")
                    }
                    else -> { }
                }
            }
        })
    }

    private fun playVideos() {
        try{
            val targetPositions: MutableList<Int> = mutableListOf()
                var startPosition: Int = (Objects.requireNonNull(
                        layoutManager) as LinearLayoutManager).findFirstVisibleItemPosition()
                var endPosition: Int = (layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                Log.d(TAG, "playVideo: origin range: $startPosition - $endPosition")

                // if first position is not play card (empty / another view holder), set startPosition + 1
                if(mediaObjects[startPosition] !is PlayBannerCarouselItemDataModel){
                    if(startPosition == endPosition){
                        return
                    }
                    startPosition++
                }

                // if end position is see more card, set end position - 1
                if(mediaObjects[endPosition] !is PlayBannerCarouselItemDataModel){
                    endPosition--
                }

                // something is wrong. return.
                if (startPosition < 0 || endPosition < 0) {
                    return
                }
                Log.d(TAG, "playVideo: range: $startPosition - $endPosition")

                // if there is more than 2 list-item on the screen
                // check percentage view visible < 49% will take the second item and third item
                // else will take first item and second item
                for(i in startPosition .. endPosition){
                    if(getVisibleVideoSurfaceWidth(i) > 0) targetPositions.add(i)
                    if(targetPositions.size == 2) break
                }


            Log.d(TAG, "playVideo: target position: $targetPositions")

            for (i in 0 until targetPositions.size){
                val playPosition = targetPositions[i]
                if(videoPlayer1.position == playPosition || videoPlayer2.position == playPosition){
                    /* do nothing skip video */
                } else if(!targetPositions.contains(videoPlayer1.position)){
                    Log.d(TAG, "playVideo: VideoPlayer1 prepare at $playPosition")
                    playVideo(videoPlayer1, playPosition)
                } else if(!targetPositions.contains(videoPlayer2.position)){
                    Log.d(TAG, "playVideo: VideoPlayer2 prepare at $playPosition")
                    playVideo(videoPlayer2, playPosition)
                }
            }
        } catch (exception: Exception) {
            Log.e(TAG, "Error exception at: ${exception.message}")
        }
    }

    private fun playVideo(videoPlayer: ViewPlayerModel, playPosition: Int){
        val holder = findViewHolderForAdapterPosition(playPosition) ?: return
        Log.d(TAG, "playVideo: current holder: $holder")
        if(holder is PlayBannerCarouselItemViewHolder){
            Log.d(TAG, "playVideo: remove video player at ${videoPlayer.position}")
            removeVideoView(videoPlayer)
            val exoPlayer = videoPlayer.videoPlayer
            holder.playerView.player = exoPlayer
            val mediaUrl: String = (mediaObjects[playPosition] as PlayBannerCarouselItemDataModel).videoUrl
            val videoSource: MediaSource = getMediaSourceBySource(context, Uri.parse(mediaUrl), "Tokopedia Android $playPosition")
            exoPlayer?.prepare(videoSource)
            exoPlayer?.playWhenReady = true
            videoPlayer.position = playPosition
            videoPlayer.viewHolderParent = holder.itemView
            exoPlayer?.seekTo(mediaObjectsLastPosition[playPosition].toLong())
            Log.d(TAG, "playVideo: prepare finish at $playPosition - seek to ${mediaObjectsLastPosition[playPosition].toLong()}")
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
    private fun removeVideoView(videoPlayer: ViewPlayerModel) {
        val playPosition = videoPlayer.position
        val viewHolder = videoPlayer.viewHolderParent?.tag
        Log.d(TAG, "playVideo: remove video ${viewHolder} at $playPosition")
        if (viewHolder != null && viewHolder is PlayBannerCarouselItemViewHolder) {
            viewHolder.playerView.player = null
            mediaObjectsLastPosition[playPosition] = videoPlayer.videoPlayer?.currentPosition?.toInt() ?: 0
            videoPlayer.videoPlayer?.stop(true)
        }
        videoPlayer.position = -1
        videoPlayer.viewHolderParent = null
    }

    private fun resetVideoView(viewHolder: Any?) {
        if(viewHolder != null && viewHolder is PlayBannerCarouselItemViewHolder && viewHolder.playerView.player != null){
            if(videoPlayer1.viewHolderParent == viewHolder){
                removeVideoView(videoPlayer1)
            } else if(videoPlayer2.viewHolderParent == viewHolder){
                removeVideoView(videoPlayer2)
            }
        }
    }

    fun releasePlayer() {
        videoPlayer1.videoPlayer?.release()
        videoPlayer2.videoPlayer?.release()
        resetVideoPlayer()
    }

    private fun resetVideoPlayer(){
        removeVideoView(videoPlayer1)
        removeVideoView(videoPlayer2)
    }

    fun setMedia(list: List<BasePlayBannerCarouselModel>){
        this.mediaObjects.clear()
        this.mediaObjects.addAll(list)
        this.mediaObjectsLastPosition.addAll(list.map { 0 })
        resetVideoPlayer()
        playVideos()
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
                else (errorCount * PlayVideoManager.RETRY_DELAY) + PlayVideoManager.RETRY_DELAY
            }

            override fun getMinimumLoadableRetryCount(dataType: Int): Int {
                return if (dataType == C.DATA_TYPE_MEDIA_PROGRESSIVE_LIVE) PlayVideoManager.RETRY_COUNT_LIVE else PlayVideoManager.RETRY_COUNT_DEFAULT
            }
        }
    }

    companion object{
        private const val TAG = "ExoPlayerRecyclerView"
    }
}