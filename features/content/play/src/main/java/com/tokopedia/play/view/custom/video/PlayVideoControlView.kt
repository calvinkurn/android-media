package com.tokopedia.play.view.custom.video

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.exoplayer2.ui.PlayerControlView
import com.google.android.exoplayer2.ui.TimeBar
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.play.R
import com.tokopedia.play.util.video.millisToFormattedVideoDuration
import com.google.android.exoplayer2.ui.R as exoR

/**
 * Created by kenny.hadisaputra on 30/03/22
 */
class PlayVideoControlView : ConstraintLayout {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)

    private val tvTimeRemaining: TextView
    private val playerControl: PlayerControlView
    private val timeBar: TimeBar

    init {
        val view = View.inflate(context, R.layout.view_play_video_control, this)
        tvTimeRemaining = view.findViewById(R.id.tv_time_remaining)
        playerControl = view.findViewById(R.id.player_control)
        timeBar = playerControl.findViewById(exoR.id.exo_progress)

        setupView()
    }

    private val progressUpdateListener = PlayerControlView.ProgressUpdateListener { pos, _ ->
        val player = playerControl.player ?: return@ProgressUpdateListener
        tvTimeRemaining.text = (player.contentDuration.orZero() - pos)
            .millisToFormattedVideoDuration()
    }

    private fun setupView() {
        timeBar.addListener(object : TimeBar.OnScrubListener {
            override fun onScrubStart(timeBar: TimeBar, position: Long) {

            }

            override fun onScrubMove(timeBar: TimeBar, position: Long) {

            }

            override fun onScrubStop(timeBar: TimeBar, position: Long, canceled: Boolean) {

            }
        })
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        playerControl.setProgressUpdateListener(progressUpdateListener)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        playerControl.setProgressUpdateListener(null)
    }
}