package com.tokopedia.product.manage.feature.filter.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.tokopedia.design.base.BaseCustomView
import com.tokopedia.product.manage.feature.filter.presentation.adapter.viewmodel.FilterViewModel
import kotlinx.android.synthetic.main.widget_header.view.*

class HeaderWidget : BaseCustomView {

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        View.inflate(context, com.tokopedia.product.manage.R.layout.widget_header, this)
    }

    fun bind(headerText: String) {
        title.text = headerText
    }
}

interface ShowChipsListener {
    fun onShowChips(element: FilterViewModel)
}