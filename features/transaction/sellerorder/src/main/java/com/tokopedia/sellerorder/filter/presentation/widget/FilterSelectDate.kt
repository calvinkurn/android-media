package com.tokopedia.sellerorder.filter.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat
import com.tokopedia.sellerorder.R
import kotlinx.android.synthetic.main.item_widget_filter_date.view.*

class FilterSelectDate: RelativeLayout {

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        View.inflate(context, R.layout.item_widget_filter_date, this)
    }

    fun setDateLabel(date: String) {
        tvSelectDateText?.text = date
        tvSelectDateText?.setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N700))
    }

    fun setDateLabelEmpty(date: String) {
        tvSelectDateText?.text = date
        tvSelectDateText?.setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N700_44))
    }
}