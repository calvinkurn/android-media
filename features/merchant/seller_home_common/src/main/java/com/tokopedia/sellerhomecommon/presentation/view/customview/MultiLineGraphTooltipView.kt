package com.tokopedia.sellerhomecommon.presentation.view.customview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.tokopedia.kotlin.extensions.view.getResDrawable
import com.tokopedia.sellerhomecommon.R
import com.tokopedia.sellerhomecommon.databinding.ShcMultiLineGraphTooltipViewBinding

/**
 * Created By @ilhamsuaib on 03/11/20
 */

class MultiLineGraphTooltipView : LinearLayout {

    constructor(context: Context) : super(context) {
        initView(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initView(context)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initView(context)
    }

    private var binding: ShcMultiLineGraphTooltipViewBinding? = null

    private fun initView(context: Context) {
        binding = ShcMultiLineGraphTooltipViewBinding.inflate(
            LayoutInflater.from(context), this, true
        )
    }

    fun showDot(color: Int, isDashed: Boolean = false) {
        binding?.run {
            imgShcTooltipDot.setColorFilter(color, android.graphics.PorterDuff.Mode.SRC_IN)
            val dot = if (isDashed) {
                context.getResDrawable(R.drawable.ic_shc_tooltip_chart_dot_dashed)
            } else {
                context.getResDrawable(R.drawable.ic_shc_tooltip_chart_dot)
            }
            imgShcTooltipDot.setImageDrawable(dot)
        }
    }

    fun setContent(title: String, value: String) {
        binding?.run {
            tvShcTooltipTitle.text = title
            tvShcTooltipValue.text = value
        }
    }
}