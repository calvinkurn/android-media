package com.tokopedia.sellerhomecommon.presentation.view.customview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.tokopedia.media.loader.loadImage
import com.tokopedia.sellerhomecommon.R
import com.tokopedia.sellerhomecommon.databinding.ShcMultiLineGraphLegendViewBinding

/**
 * Created By @ilhamsuaib on 03/11/20
 */

class MultiLineGraphLegendView : LinearLayout {

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

    private var binding: ShcMultiLineGraphLegendViewBinding? = null

    private fun initView(context: Context) {
        binding = ShcMultiLineGraphLegendViewBinding.inflate(
            LayoutInflater.from(context), this, true
        )
    }

    fun showDashLine() {
        binding?.icShcMlgLegendLine?.loadImage(R.drawable.ic_shc_legend_dash_line)
    }

    fun showLine() {
        binding?.icShcMlgLegendLine?.loadImage(R.drawable.ic_shc_legend_line)
    }

    fun setText(s: String) {
        binding?.tvShcMlgLegendName?.text = s
    }
}