package com.tokopedia.product.manage.feature.filter.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.tokopedia.design.base.BaseCustomView
import com.tokopedia.product.manage.feature.filter.presentation.adapter.viewmodel.SelectViewModel
import kotlinx.android.synthetic.main.widget_select.view.*

class SelectWidget : BaseCustomView {

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private var isVisible = false

    private fun init() {
        View.inflate(context, com.tokopedia.product.manage.R.layout.widget_select, this)
    }

    fun bind(element: SelectViewModel, selectClickListener: SelectClickListener) {
        this.tv_header.text = element.name
        if(element.isSelected) {
            this.check.visibility = View.VISIBLE
            isVisible = true
        }
        this.setOnClickListener {
            toggleVisibility()
            selectClickListener.onSelectClick(element)
        }
    }

    private fun toggleVisibility() {
        if(isVisible) {
            this.check.visibility = View.GONE
            isVisible = false
        } else {
            this.check.visibility = View.VISIBLE
            isVisible = true
        }
    }
}

interface SelectClickListener {
    fun onSelectClick(element: SelectViewModel)
}