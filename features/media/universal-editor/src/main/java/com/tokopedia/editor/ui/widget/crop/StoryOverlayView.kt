package com.tokopedia.editor.ui.widget.crop

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import com.yalantis.ucrop.view.OverlayView
import android.os.Handler

class StoryOverlayView : OverlayView {
    constructor(context: Context) : super(context)
    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet)

    private var handlerRef: Handler? = null

    var gapDifferent = 0f
    var listener: Listener? = null

    override fun onDetachedFromWindow() {
        handlerRef?.removeCallbacksAndMessages(null)
        super.onDetachedFromWindow()
    }

    fun processStyledAttributesOpen(a: TypedArray) {
        super.processStyledAttributes(a)
    }

    fun setTargetAspectRatioStory(targetAspectRatio: Float) {
        super.setTargetAspectRatio(targetAspectRatio)

        // set top space 0 then distribute the space to bottom
        cropViewRect.let {
            it.set(
                it.left,
                0f,
                it.right,
                it.bottom - it.top
            )
        }

        handlerRef = Handler().apply {
            this.postDelayed({
                gapDifferent = (this@StoryOverlayView.height - cropViewRect.bottom) / 2

                listener?.onAspectRatioChange()

                handlerRef = null
            }, ANIMATION_UPDATE_DELAY)
        }
    }

    interface Listener {
        fun onAspectRatioChange()
    }

    companion object {
        private const val ANIMATION_UPDATE_DELAY = 200L
    }
}
