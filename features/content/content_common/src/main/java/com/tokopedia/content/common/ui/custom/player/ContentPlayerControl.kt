package com.tokopedia.content.common.ui.custom.player

import android.content.Context
import android.util.AttributeSet
import com.google.android.exoplayer2.ui.PlayerControlView
import com.google.android.exoplayer2.ui.R as exoplayeruiR
import com.google.android.exoplayer2.ui.TimeBar

/**
 * Created by kenny.hadisaputra on 04/04/23
 */
class ContentPlayerControl : PlayerControlView {

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
        playbackAttrs: AttributeSet?
    ) : super(context, attrs, defStyleAttr, playbackAttrs)

    private val timeBar: ContentTimeBar = findViewById(exoplayeruiR.id.exo_progress)

    private var mListener: Listener? = null

    init {
        timeBar.addListener(object : TimeBar.OnScrubListener {
            override fun onScrubStart(timeBar: TimeBar, position: Long) {
                val player = this@ContentPlayerControl.player ?: return

                mListener?.onScrubbing(
                    this@ContentPlayerControl,
                    position,
                    player.contentDuration,
                )
            }

            override fun onScrubMove(timeBar: TimeBar, position: Long) {
                val player = this@ContentPlayerControl.player ?: return

                mListener?.onScrubbing(
                    this@ContentPlayerControl,
                    position,
                    player.contentDuration,
                )
            }

            override fun onScrubStop(timeBar: TimeBar, position: Long, canceled: Boolean) {
                val player = this@ContentPlayerControl.player ?: return
                mListener?.onStopScrubbing(
                    this@ContentPlayerControl,
                    position,
                    player.contentDuration,
                )
            }
        })
    }

    fun setListener(listener: Listener?) {
        mListener = listener
    }

    interface Listener {

        fun onScrubbing(view: PlayerControlView, currPosition: Long, totalDuration: Long)
        fun onStopScrubbing(view: PlayerControlView, currPosition: Long, totalDuration: Long)
    }
}
