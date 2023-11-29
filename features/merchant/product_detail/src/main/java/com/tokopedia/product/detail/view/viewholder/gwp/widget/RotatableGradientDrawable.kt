package com.tokopedia.product.detail.view.viewholder.gwp.widget

import android.graphics.drawable.GradientDrawable
import android.graphics.Canvas

class RotatableGradientDrawable : GradientDrawable {

    private var rotationDegrees = 0f

    constructor() : super()

    constructor(orientation: Orientation, colors: IntArray) : super(orientation, colors)

    fun setRotationDegrees(degrees: Float) {
        rotationDegrees = degrees
        invalidateSelf()
    }

    override fun draw(canvas: Canvas) {
        canvas.save()
        canvas.rotate(rotationDegrees, 0f, 0f)
        super.draw(canvas)
        canvas.restore()
    }
}
