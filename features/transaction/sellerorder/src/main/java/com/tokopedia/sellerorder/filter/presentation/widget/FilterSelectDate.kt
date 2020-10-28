package com.tokopedia.sellerorder.filter.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.tokopedia.sellerorder.R
import com.tokopedia.unifycomponents.BaseCustomView
import kotlinx.android.synthetic.main.item_widget_filter_date.view.*

class FilterSelectDate: BaseCustomView {

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
    }
}