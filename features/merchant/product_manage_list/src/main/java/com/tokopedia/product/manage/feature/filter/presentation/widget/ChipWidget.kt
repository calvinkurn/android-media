package com.tokopedia.product.manage.feature.filter.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.tokopedia.design.base.BaseCustomView
import com.tokopedia.product.manage.feature.filter.presentation.adapter.viewmodel.FilterDataViewModel
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
        View.inflate(context, com.tokopedia.product.manage.R.layout.widget_chip, this)
    }

    fun bind(data: FilterDataViewModel, chipClickListener: ChipClickListener, canSelectMany: Boolean, title: String) {
        item_name.text = data.name
        val originalColor = item_name.currentTextColor
        if(data.select) {
            this.isSelected = true
            item_name.setTextColor(ContextCompat.getColor(this.context,com.tokopedia.unifyprinciples.R.color.Green_G500))
        }
        this.setOnClickListener {
            toggleSelected(originalColor)
            chipClickListener.onChipClicked(data, canSelectMany, title)
        }
    }

    private fun toggleSelected(originalColor: Int) {
        isSelected = !isSelected
        if(isSelected) {
            item_name.setTextColor(ContextCompat.getColor(this.context,com.tokopedia.unifyprinciples.R.color.Green_G500))
        } else {
            item_name.setTextColor(originalColor)
        }
    }
}

interface ChipClickListener {
    fun onChipClicked(data: FilterDataViewModel, canSelectMany: Boolean, title: String)
}