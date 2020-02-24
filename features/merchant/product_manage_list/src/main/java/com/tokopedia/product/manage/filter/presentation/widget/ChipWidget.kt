package com.tokopedia.product.manage.filter.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.tokopedia.design.item.DeletableItemView
import com.tokopedia.product.manage.oldlist.R

class ChipWidget : DeletableItemView {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun initView(context: Context) {
        rootView = View.inflate(context, R.layout.widget_chip, this)
        textView = rootView.findViewById(R.id.item_name)
    }
}