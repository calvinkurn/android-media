package com.tokopedia.sellerhomecommon.presentation.view.customview

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.tokopedia.kotlin.extensions.view.loadImageDrawable
import com.tokopedia.sellerhomecommon.R
import kotlinx.android.synthetic.main.shc_multi_line_graph_legend_view.view.*

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

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initView(context)
    }

    private fun initView(context: Context) {
        View.inflate(context, R.layout.shc_multi_line_graph_legend_view, this)
    }

    fun showDashLine() {
        icShcMlgLegendLine.loadImageDrawable(R.drawable.ic_shc_legend_dash_line)
    }

    fun showLine() {
        icShcMlgLegendLine.loadImageDrawable(R.drawable.ic_shc_legend_line)
    }

    fun setText(s: String) {
        tvShcMlgLegendName.text = s
    }
}