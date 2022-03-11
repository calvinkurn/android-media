package com.tokopedia.product.addedit.tooltip.presentation

import android.content.Context
import android.text.Spanned
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.text.toSpanned
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.product.addedit.R
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifyprinciples.Typography

class TooltipCardView : BaseCustomView, MotionLayout.TransitionListener {

    var text: Spanned = "".toSpanned()
    var tvTipsText: Typography? = null
    var layoutTips: LinearLayout? = null
    var motionLayoutTips: MotionLayout? = null

    constructor(context: Context) : super(context) {
        init(null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(attrs)
    }

    override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {
        updateHeight()
    }

    override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, p3: Float) {
        // no-op
    }

    override fun onTransitionCompleted(p0: MotionLayout?, p1: Int) {
        // no-op
    }

    override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) {
        // no-op
    }

    private fun init(attrs: AttributeSet?) {
        val view = View.inflate(context, R.layout.add_edit_product_partial_tooltip_card, this)
        motionLayoutTips = view.findViewById(R.id.motion_layout_tips)
        layoutTips = view.findViewById(R.id.layout_tips)
        tvTipsText = view.findViewById(R.id.tv_tips_text)

        motionLayoutTips?.setTransitionListener(this)
        defineCustomAttributes(attrs)
        refreshViews()
    }

    private fun defineCustomAttributes(attrs: AttributeSet?) {
        if (attrs != null) {
            val styledAttributes = context.obtainStyledAttributes(attrs, R.styleable.TooltipCardView, 0, 0)

            try {
                val htmlText: String = styledAttributes.getString(R.styleable.TooltipCardView_text).orEmpty()
                text = MethodChecker.fromHtml(htmlText)
            } finally {
                styledAttributes.recycle()
            }
        }
    }

    private fun refreshViews() {
        tvTipsText?.text = text
    }

    private fun updateHeight() {
        val matchParentMeasureSpec = MeasureSpec.makeMeasureSpec((parent as View).width, MeasureSpec.EXACTLY)
        val wrapContentMeasureSpec = MeasureSpec.makeMeasureSpec(Int.ZERO, MeasureSpec.UNSPECIFIED)
        layoutTips?.measure(matchParentMeasureSpec, wrapContentMeasureSpec)
        val targetHeight = layoutTips?.measuredHeight

        if (targetHeight != null) {
            motionLayoutTips?.layoutParams?.height = targetHeight
            motionLayoutTips?.requestLayout()
        }
    }
}