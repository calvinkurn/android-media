package com.tokopedia.play.view.pip

import android.content.Context
import android.content.Intent
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
import com.tokopedia.floatingwindow.FloatingWindowAdapter
import com.tokopedia.play.R
import com.tokopedia.play.analytic.PlayPiPAnalytic
import com.tokopedia.play.view.fragment.PlayVideoFragment
import com.tokopedia.play.view.uimodel.PiPInfoUiModel
import com.tokopedia.play_common.player.PlayVideoManager
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
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    private val playVideoManager: PlayVideoManager
        get() = PlayVideoManager.getInstance(context)

    private val userSession: UserSessionInterface = UserSession(context.applicationContext)
    private val pipAnalytic: PlayPiPAnalytic = PlayPiPAnalytic(userSession)

    private var timePipAttached: Long? = null

    private val pipAdapter = FloatingWindowAdapter(context)

    private val videoListener = object : PlayVideoManager.Listener {
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

    private var isRoutingToRoom: Boolean = false

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

        playVideoManager.resumeOrPlayPreviousVideo(true)
        playVideoManager.mute(false)

        timePipAttached = System.currentTimeMillis()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        playVideoManager.removeListener(videoListener)
        if (!isRoutingToRoom) {
            if (playVideoManager.isVideoLive()) playVideoManager.release()
            else playVideoManager.stop()
        }

        pipRemovedAnalytic()
    }

    fun setPiPInfo(pipInfo: PiPInfoUiModel) {
        mPiPInfo = pipInfo

        pvVideo.resizeMode = if (!pipInfo.videoOrientation.isHorizontal) {
            AspectRatioFrameLayout.RESIZE_MODE_ZOOM
        } else AspectRatioFrameLayout.RESIZE_MODE_FIT

        setBackgroundColor(
                MethodChecker.getColor(rootView.context,
                        if (pipInfo.videoOrientation.isHorizontal) R.color.play_solid_black
                        else R.color.transparent
                )
        )
    }

    fun getPlayerView(): PlayerView = pvVideo

    private fun setupView() {
        playVideoManager.addListener(videoListener)
        flCloseArea.setOnClickListener {
            pipAdapter.removeByKey(PlayVideoFragment.FLOATING_WINDOW_KEY)
        }

        setOnClickListener {
            mPiPInfo?.let { pipInfo ->
                isRoutingToRoom = true
                val intent = RouteManager.getIntent(context, ApplinkConst.PLAY_DETAIL, pipInfo.channelId)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
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
}