package com.tokopedia.product_ar.util

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.LinearLayout
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.product_ar.R
import com.tokopedia.unifyprinciples.Typography

class AnimatedTextIcon : LinearLayout {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrSet: AttributeSet) : super(context, attrSet)

    constructor(context: Context, attrSet: AttributeSet, defStyleAttr: Int) : super(context, attrSet, defStyleAttr)

    private var txtTitle: Typography? = null
    private var icImg: IconUnify? = null
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

    fun renderText(text: String, iconUnify: Int) {
        txtTitle?.show()
        txtTitle?.text = text
        icImg?.setImage(newIconId = iconUnify)
        txtTitle?.startAnimation(fadeOutAnimation)
        invalidate()
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        requestLayout()
        invalidate()
    }
}