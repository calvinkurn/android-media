package com.tokopedia.editor.ui.components.custom.crop

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import com.yalantis.ucrop.view.OverlayView

class OverlayViewStories: OverlayView {
    constructor(context: Context): super(context)
    constructor(context: Context, attributeSet: AttributeSet): super(context, attributeSet)

    fun processStyledAttributesOpen(a: TypedArray) {
        super.processStyledAttributes(a)
    }
}
