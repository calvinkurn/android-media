package com.tokopedia.common_category.customview

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.tokopedia.common_category.R
import com.tokopedia.unifycomponents.BaseCustomView
import kotlinx.android.synthetic.main.layout_nav_no_product.view.*

class NoDataView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null,
        defStyleAttr: Int = 0)
    : BaseCustomView(context, attrs, defStyleAttr) {

    init {
        context.run {
            View.inflate(this, R.layout.layout_nav_no_product, this@NoDataView)
            setHeaderText(R.string.category_nav_product_no_data_title)
            setDescriptionText(R.string.category_nav_product_no_data_description)
        }
    }

    fun setHeaderText(headerTextId: Int) {
        txt_no_data_header.text = resources.getText(headerTextId)
    }

    fun setDescriptionText(descriptionTextId: Int) {
        txt_no_data_description.text = resources.getText(descriptionTextId)
    }
}