package com.tokopedia.product.manage.feature.filter.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.tokopedia.design.base.BaseCustomView
import com.tokopedia.design.item.DeletableItemView
import com.tokopedia.product.manage.oldlist.R
import kotlinx.android.synthetic.main.widget_chip.view.*

class ChipWidget : BaseCustomView {

    constructor(context: Context) : super(context) {
        initView()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initView()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initView()
    }

    private fun initView() {
        View.inflate(context, R.layout.widget_chip, this)
    }

    fun bind(name: String) {
        item_name.text = name
    }
}