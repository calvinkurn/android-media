package com.tokopedia.content.product.preview.view.components.player

import android.content.Context
import android.util.AttributeSet
import com.google.android.exoplayer2.ui.PlayerControlView
import com.google.android.exoplayer2.ui.R
import com.google.android.exoplayer2.ui.TimeBar

class ProductPreviewPlayerControl : PlayerControlView {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private val timeBar: ProductPreviewTimeBar = findViewById(R.id.exo_progress)

    private var mListener: Listener? = null

    init {
        timeBar.addListener(object : TimeBar.OnScrubListener {
            override fun onScrubStart(timeBar: TimeBar, position: Long) {
                val player = this@ProductPreviewPlayerControl.player ?: return

                mListener?.onScrubbing(
                    this@ProductPreviewPlayerControl,
                    position,
                    player.contentDuration,
                )
            }

            override fun onScrubMove(timeBar: TimeBar, position: Long) {
                val player = this@ProductPreviewPlayerControl.player ?: return

                mListener?.onScrubbing(
                    this@ProductPreviewPlayerControl,
                    position,
                    player.contentDuration,
                )
            }

            override fun onScrubStop(timeBar: TimeBar, position: Long, canceled: Boolean) {
                val player = this@ProductPreviewPlayerControl.player ?: return
                mListener?.onStopScrubbing(
                    this@ProductPreviewPlayerControl,
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
