package com.tokopedia.play.view.custom.video

import android.content.Context
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.exoplayer2.ui.DefaultTimeBar
import com.google.android.exoplayer2.ui.PlayerControlView
import com.google.android.exoplayer2.ui.TimeBar
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.play.R
import com.tokopedia.play.util.video.millisToFormattedVideoDuration
import kotlin.math.max
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
    private val tvTimeBarTime: TextView
    private val playerControl: PlayerControlView
    private val timeBar: DefaultTimeBar

    private var alreadyUpdateProgress = false

    init {
        val view = View.inflate(context, R.layout.view_play_video_control, this)
        tvTimeRemaining = view.findViewById(R.id.tv_time_remaining)
        tvTimeBarTime = view.findViewById(R.id.tv_timebar_time)
        playerControl = view.findViewById(R.id.player_control)
        timeBar = playerControl.findViewById(exoR.id.exo_progress)

        setupView()
    }

    private val progressUpdateListener = PlayerControlView.ProgressUpdateListener { pos, _ ->
        val player = playerControl.player ?: return@ProgressUpdateListener
        tvTimeRemaining.text = max((player.contentDuration.orZero() - pos), 0)
            .millisToFormattedVideoDuration()

        if (!alreadyUpdateProgress) {
            updateTimeBarTimePosition(timeBar, pos, player.contentDuration)
            alreadyUpdateProgress = true
        }
    }

    private fun setupView() {
        timeBar.addListener(object : TimeBar.OnScrubListener {
            override fun onScrubStart(timeBar: TimeBar, position: Long) {
                if (timeBar !is DefaultTimeBar) return
                val player = playerControl.player ?: return

                updateTimeBarTimePosition(timeBar, position, player.contentDuration)
                tvTimeBarTime.visible()
            }

            override fun onScrubMove(timeBar: TimeBar, position: Long) {
                if (timeBar !is DefaultTimeBar) return
                val player = playerControl.player ?: return

                updateTimeBarTimePosition(timeBar, position, player.contentDuration)
            }

            override fun onScrubStop(timeBar: TimeBar, position: Long, canceled: Boolean) {
                tvTimeBarTime.gone()
            }
        })
    }

    private fun updateTimeBarTimePosition(
        timeBar: DefaultTimeBar,
        currentPos: Long,
        totalDuration: Long,
    ) {
        tvTimeBarTime.text = currentPos.millisToFormattedVideoDuration()

        val excessScrubberSize = dpToPx(
            resources.displayMetrics,
            DefaultTimeBar.DEFAULT_SCRUBBER_DRAGGED_SIZE_DP
        ) / 2

        val timeBarWidth = timeBar.width - (excessScrubberSize * 2)
        val timeBarPos = (currentPos.toFloat() / totalDuration) * timeBarWidth
        tvTimeBarTime.translationX = timeBar.x + timeBarPos - (tvTimeBarTime.measuredWidth / 2) + excessScrubberSize
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        playerControl.setProgressUpdateListener(progressUpdateListener)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        playerControl.setProgressUpdateListener(null)
    }

    private fun dpToPx(displayMetrics: DisplayMetrics, dps: Int): Int {
        return (dps * displayMetrics.density).toInt()
    }
}