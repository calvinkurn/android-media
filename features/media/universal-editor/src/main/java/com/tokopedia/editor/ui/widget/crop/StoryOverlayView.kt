package com.tokopedia.editor.ui.widget.crop

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import com.yalantis.ucrop.view.OverlayView

class StoryOverlayView: OverlayView {
    constructor(context: Context): super(context)
    constructor(context: Context, attributeSet: AttributeSet): super(context, attributeSet)

    var gapTop = 0f;

    fun processStyledAttributesOpen(a: TypedArray) {
        super.processStyledAttributes(a)
    }

    override fun setTargetAspectRatio(targetAspectRatio: Float) {
        super.setTargetAspectRatio(targetAspectRatio)

        // set top space 0 then distribute the space to bottom
        cropViewRect.let {
            gapTop = it.top
            it.set(
                it.left,
                0f,
                it.right,
                it.bottom - it.top
            )
        }
    }
}
