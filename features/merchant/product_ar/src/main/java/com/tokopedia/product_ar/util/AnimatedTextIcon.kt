package com.tokopedia.product_ar.util

import com.tokopedia.imageassets.ImageUrl

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.ImageView
import android.widget.LinearLayout
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImageWithoutPlaceholder
import com.tokopedia.product_ar.R
import com.tokopedia.unifyprinciples.Typography

class AnimatedTextIcon : LinearLayout {

    companion object {
        private const val GALERY_IMAGE = ImageUrl.GALERY_IMAGE
        private const val CAMERA_IMAGE = ImageUrl.CAMERA_IMAGE
    }

    constructor(context: Context) : super(context)

    constructor(context: Context, attrSet: AttributeSet) : super(context, attrSet)

    constructor(context: Context, attrSet: AttributeSet, defStyleAttr: Int) : super(context, attrSet, defStyleAttr)

    private var txtTitle: Typography? = null
    private var icImg: ImageView? = null
    private val fadeOutAnimation = AlphaAnimation(1.0f, 0.0f).apply {
        duration = 500
        startOffset = 5000
        setAnimationListener(object :
                Animation.AnimationListener {
            override fun onAnimationStart(p0: Animation?) {
            }

            override fun onAnimationEnd(p0: Animation?) {
                txtTitle?.hide()
            }

            override fun onAnimationRepeat(p0: Animation?) {
            }
        })
    }

    init {
        this.orientation = VERTICAL
        val view = View.inflate(context, R.layout.animated_text_icon_view, this)
        with(view) {
            txtTitle = findViewById(R.id.txt_title_animated)
            icImg = findViewById(R.id.ic_img_animated)
        }
    }

    fun getIconInstanceView(): View? {
        return icImg
    }

    fun renderText(text: String, iconUnify: Int) {
        txtTitle?.show()
        txtTitle?.text = text
        loadImage(iconUnify)
        txtTitle?.startAnimation(fadeOutAnimation)
        invalidate()
    }

    private fun loadImage(iconUnify: Int) {
        if (iconUnify == IconUnify.IMAGE) {
            icImg?.loadImageWithoutPlaceholder(GALERY_IMAGE)
        } else {
            icImg?.loadImageWithoutPlaceholder(CAMERA_IMAGE)
        }
    }
}