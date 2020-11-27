package com.tokopedia.play.view.pip

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.ui.PlayerView
import com.tokopedia.play.R
import com.tokopedia.play_common.player.PlayVideoManager
import com.tokopedia.play_common.state.PlayVideoState
import com.tokopedia.unifycomponents.LoaderUnify

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

    private val pvVideo: PlayerView
    private val flLoading: FrameLayout
    private val ivLoading: LoaderUnify
    private val flCloseArea: FrameLayout

    init {
        val view = View.inflate(context, R.layout.view_play_viewer_pip, this)

        pvVideo = view.findViewById(R.id.pv_video)
        flLoading = view.findViewById(R.id.fl_loading)
        ivLoading = view.findViewById(R.id.iv_loading)
        flCloseArea = view.findViewById(R.id.fl_close_area)

        setupView()
    }

    fun getPlayerView(): PlayerView = pvVideo

    private fun setupView() {
        playVideoManager.addListener(videoListener)
        flCloseArea.setOnClickListener {
            // TODO click close
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
}