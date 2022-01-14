package com.tokopedia.product_ar.util

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.product_ar.R
import com.tokopedia.unifyprinciples.Typography

class AnimatedTransparentDialogView : FrameLayout {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrSet: AttributeSet) : super(context, attrSet)

    constructor(context: Context, attrSet: AttributeSet, defStyleAttr: Int) : super(context, attrSet, defStyleAttr)

    private var textView: Typography? = null
    private val radius = context.resources.getDimension(R.dimen.image_comparison_size)
    private var view: View? = null
    var inAnimation: Animation? = null
    var outAnimation: Animation? = null

    private val shapeDrawable by lazy {
        val shapeDrawableModel = ShapeAppearanceModel()
                .toBuilder()
                .setAllCorners(CornerFamily.ROUNDED, radius)
                .build()
        MaterialShapeDrawable(shapeDrawableModel)
    }

    init {
        val viewInflater = LayoutInflater.from(context).inflate(R.layout.animated_transparent_dialog_view, this)
        with(viewInflater) {
            view = this
            textView = findViewById(R.id.txt_animated_transparent)
            textView?.text = context?.getString(R.string.message_face_not_detected) ?: ""
            textView?.background = shapeDrawable
            shapeDrawable.fillColor = ContextCompat.getColorStateList(
                    context,
                    com.tokopedia.unifyprinciples.R.color.Unify_Static_Black_68)
            inAnimation = AnimationUtils.loadAnimation(context, R.anim.fade_in_animation)
            outAnimation = AnimationUtils.loadAnimation(context, R.anim.fade_out_animation)
        }
    }

    fun showAnimated() {
        if (!isVisible) {
            this.show()
            this.startAnimation(inAnimation)
        }
    }

    fun hideAnimated() {
        if (isVisible) {
            this.startAnimation(outAnimation)
            this.hide()
        }
    }
}