package com.tokopedia.play.view.pip

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.ui.PlayerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.UriUtil
import com.tokopedia.floatingwindow.FloatingWindowAdapter
import com.tokopedia.play.PLAY_KEY_SOURCE_ID
import com.tokopedia.play.PLAY_KEY_SOURCE_TYPE
import com.tokopedia.play.R
import com.tokopedia.play.analytic.PlayPiPAnalytic
import com.tokopedia.play.view.fragment.PlayVideoFragment
import com.tokopedia.play.view.type.PlaySource
import com.tokopedia.play.view.uimodel.PiPInfoUiModel
import com.tokopedia.play_common.player.PlayVideoWrapper
import com.tokopedia.play_common.player.state.ExoPlayerStateProcessorImpl
import com.tokopedia.play_common.state.PlayVideoState
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import java.util.concurrent.TimeUnit

/**
 * Created by jegul on 27/11/20
 */
class PlayViewerPiPView : ConstraintLayout {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
//    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
//    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    private var mVideoPlayer: PlayVideoWrapper = PlayVideoWrapper.Builder(context).build()

    private val userSession: UserSessionInterface = UserSession(context.applicationContext)
    private val pipAnalytic: PlayPiPAnalytic = PlayPiPAnalytic(userSession)

    private var timePipAttached: Long? = null

    private val pipAdapter = FloatingWindowAdapter(context)

    private val videoListener = object : PlayVideoWrapper.Listener {
        override fun onVideoPlayerChanged(player: ExoPlayer) {
            pvVideo.player = player
        }

        override fun onPlayerStateChanged(state: PlayVideoState) {
            when (state) {
                PlayVideoState.Buffering -> onVideoBuffering()
                PlayVideoState.Playing, PlayVideoState.Pause -> onVideoStarted()
                PlayVideoState.Ended -> onVideoEnded()
            }
        }
    }

    private var mPiPInfo: PiPInfoUiModel? = null

    private val pvVideo: PlayerView
    private val flLoading: FrameLayout
    private val ivLoading: LoaderUnify
    private val flCloseArea: FrameLayout

    private var mPauseOnDetached: Boolean = true

    private val playerStateProcessor = ExoPlayerStateProcessorImpl()

    init {
        val view = View.inflate(context, R.layout.view_play_viewer_pip, this)

        pvVideo = view.findViewById(R.id.pv_video)
        flLoading = view.findViewById(R.id.fl_loading)
        ivLoading = view.findViewById(R.id.iv_loading)
        flCloseArea = view.findViewById(R.id.fl_close_area)

        setupView()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        val pipInfo = mPiPInfo
        if (pipInfo != null) {
            mVideoPlayer.playUri(
                    uri = Uri.parse(pipInfo.videoPlayer.params.videoUrl),
                    bufferControl = pipInfo.videoPlayer.params.buffer
            )
            mVideoPlayer.mute(false)
        }

        timePipAttached = System.currentTimeMillis()

        videoListener.onPlayerStateChanged(
                playerStateProcessor.processState(
                        playWhenReady = mVideoPlayer.videoPlayer.playWhenReady,
                        playbackState = mVideoPlayer.videoPlayer.playbackState,
                )
        )
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        mVideoPlayer.removeListener(videoListener)

        if (mPiPInfo?.stopOnClose == true) {
            if (mVideoPlayer.isVideoLive()) mVideoPlayer.release()
            else mVideoPlayer.stop()
        } else if (mPauseOnDetached) {
            mVideoPlayer.pause(false)
        }

        pipRemovedAnalytic()
    }

    fun setPlayer(playVideoPlayer: PlayVideoWrapper) {
        mVideoPlayer = playVideoPlayer
    }

    fun setPiPInfo(pipInfo: PiPInfoUiModel) {
        mPiPInfo = pipInfo

        pvVideo.resizeMode = if (!pipInfo.videoStream.orientation.isHorizontal) {
            AspectRatioFrameLayout.RESIZE_MODE_ZOOM
        } else AspectRatioFrameLayout.RESIZE_MODE_FIT

        setBackgroundColor(
                MethodChecker.getColor(rootView.context,
                        if (pipInfo.videoStream.orientation.isHorizontal) R.color.play_dms_background
                        else R.color.transparent
                )
        )
    }

    fun getPlayerView(): PlayerView = pvVideo

    fun setPauseOnDetached(shouldPause: Boolean) {
        mPauseOnDetached = shouldPause
    }

    private fun setupView() {
        mVideoPlayer.addListener(videoListener)
        flCloseArea.setOnClickListener {
            removePiP()
        }

        setOnClickListener {
            mPiPInfo?.let { pipInfo ->
                removePiP()
                val intent = RouteManager.getIntent(
                        context,
                        UriUtil.buildUriAppendParams(
                                ApplinkConst.PLAY_DETAIL,
                                getApplinkQueryParams(pipInfo)
                        ),
                        pipInfo.channelId
                ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        .putExtra(EXTRA_PLAY_START_VOD_MILLIS, mVideoPlayer.getCurrentPosition())
                        .putExtra(EXTRA_PLAY_IS_FROM_PIP, true)

                context.startActivity(intent)
            }
        }
    }

    private fun onVideoStarted() {
        flLoading.visibility = View.GONE
    }

    private fun onVideoBuffering() {
        flLoading.visibility = View.VISIBLE
        ivLoading.visibility = View.VISIBLE
    }

    private fun onVideoEnded() {
        flLoading.visibility = View.VISIBLE
        ivLoading.visibility = View.GONE
    }

    private fun removePiP() {
        pipAdapter.removeByKey(PlayVideoFragment.FLOATING_WINDOW_KEY)
    }

    private fun pipRemovedAnalytic() {
        val pipInfo = mPiPInfo ?: return

        val timePiPStart = timePipAttached ?: return
        val timeSeconds = TimeUnit.SECONDS
        val durationPiPAttachedInSeconds = timeSeconds.convert(System.currentTimeMillis() - timePiPStart, TimeUnit.MILLISECONDS)

        pipAnalytic.exitPiP(
                channelId = pipInfo.channelId,
                shopId = pipInfo.partnerId,
                channelType = pipInfo.channelType,
                durationInSecond = durationPiPAttachedInSeconds
        )
    }

    private fun getApplinkQueryParams(pipInfo: PiPInfoUiModel): Map<String, Any> {
        return when (pipInfo.source) {
            PlaySource.Unknown -> emptyMap()
            is PlaySource.Shop -> mapOf(
                    PLAY_KEY_SOURCE_TYPE to pipInfo.source.key,
                    PLAY_KEY_SOURCE_ID to pipInfo.source.sourceId
            )
            else -> mapOf(
                    PLAY_KEY_SOURCE_TYPE to pipInfo.source.key,
            )
        }
    }

    companion object {

        private const val EXTRA_PLAY_START_VOD_MILLIS = "start_vod_millis"
        private const val EXTRA_PLAY_IS_FROM_PIP = "is_from_pip"
    }
}