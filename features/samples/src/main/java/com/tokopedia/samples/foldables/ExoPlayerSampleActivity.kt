package com.tokopedia.samples.foldables

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.ui.PlayerControlView
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.tokopedia.foldable.FoldableInfo
import com.tokopedia.foldable.FoldableSupportManager
import com.tokopedia.samples.R


class ExoPlayerSampleActivity : AppCompatActivity(), FoldableSupportManager.FoldableInfoCallback {

    private var playWhenReady = true
    private var currentWindow = 0
    private var playbackPosition = 0L

    private var player: SimpleExoPlayer? = null
    lateinit var constraintLayout: ConstraintLayout
    lateinit var playerView: PlayerView
    lateinit var controlView: PlayerControlView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.exo_player_sample_activity)
        constraintLayout = findViewById(R.id.parent_container)
        playerView = findViewById(R.id.player_view)
        controlView = findViewById(R.id.control_view)
        FoldableSupportManager(this,this)
    }

    override fun onChangeLayout(foldableInfo: FoldableInfo) {
        if (foldableInfo.isFoldableDevice() && foldableInfo.isTableTopMode()) {
            val set = ConstraintSet().apply { clone(constraintLayout) }
            val newSet = foldableInfo.alignSeparatorViewToFoldingFeatureBounds(
                set,
                findViewById<View>(android.R.id.content).rootView,
                R.id.separator
            )
            newSet.connect(R.id.player_view, ConstraintSet.BOTTOM, R.id.separator, ConstraintSet.TOP)
            newSet.connect(R.id.control_view, ConstraintSet.TOP, R.id.separator, ConstraintSet.BOTTOM)
            newSet.applyTo(constraintLayout)
            playerView.useController = false // use custom controls
            controlView.player = player
        } else {
            val set = ConstraintSet().apply { clone(constraintLayout) }
            set.connect(
                R.id.player_view,
                ConstraintSet.BOTTOM,
                ConstraintSet.PARENT_ID,
                ConstraintSet.BOTTOM
            )
            set.connect(
                R.id.control_view,
                ConstraintSet.TOP,
                ConstraintSet.PARENT_ID,
                ConstraintSet.BOTTOM
            )
            set.applyTo(constraintLayout)
            playerView.useController = true // use embedded controls
            controlView.player = player
        }
    }

    override fun onStart() {
        super.onStart()
        if (android.os.Build.VERSION.SDK_INT >= 24) {
            initializePlayer()
        }
    }

    override fun onResume() {
        super.onResume()
        hideSystemUi()
        if ((android.os.Build.VERSION.SDK_INT < 24 || player == null)) {
            initializePlayer()
        }
    }

    public override fun onPause() {
        super.onPause()
        if (android.os.Build.VERSION.SDK_INT < 24) {
            releasePlayer()
        }
    }

    public override fun onStop() {
        super.onStop()
        if (android.os.Build.VERSION.SDK_INT >= 24) {
            releasePlayer()
        }
    }

    @SuppressLint("InlinedApi")
    private fun hideSystemUi() {
        constraintLayout.systemUiVisibility = (View.SYSTEM_UI_FLAG_LOW_PROFILE
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
    }

    private fun releasePlayer() {
        player?.run {
            playbackPosition = this.currentPosition
            currentWindow = this.currentWindowIndex
            playWhenReady = this.playWhenReady
            release()
        }
        player = null
    }

    private fun initializePlayer() {
        player = SimpleExoPlayer.Builder(this)
            .build()
            .also { exoPlayer ->
                playerView.player = exoPlayer
                val mp4VideoUri = Uri.parse("https://ia600701.us.archive.org/26/items/SampleVideo1280x7205mb/SampleVideo_1280x720_5mb.mp4")
                val dataSourceFactory = DefaultDataSourceFactory(
                    this,
                    Util.getUserAgent(this, "MyApplication")
                )
                val videoSource: MediaSource = ExtractorMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(mp4VideoUri)
                exoPlayer.playWhenReady = playWhenReady
                exoPlayer.seekTo(currentWindow, playbackPosition)
                exoPlayer.prepare(videoSource)
            }
    }
}
