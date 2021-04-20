package com.tokopedia.product.manage.feature.filter.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.tokopedia.product.manage.feature.filter.presentation.adapter.viewmodel.SelectUiModel
import com.tokopedia.unifycomponents.BaseCustomView
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

    fun bind(element: SelectUiModel, selectClickListener: SelectClickListener) {
        this.title.text = element.name
        if (element.isSelected) {
            this.check.visibility = View.VISIBLE
            isVisible = true
        } else {
            this.check.visibility = View.GONE
        }
        this.setOnClickListener {
            selectClickListener.onSelectClick(element)
        }
    }

    fun bindPayload(element: SelectUiModel, selectClickListener: SelectClickListener) {
        this.check.visibility = if (element.isSelected ) View.VISIBLE else View.GONE
        this.setOnClickListener {
            selectClickListener.onSelectClick(element)
        }
    }
}

interface SelectClickListener {
    fun onSelectClick(element: SelectUiModel)
}