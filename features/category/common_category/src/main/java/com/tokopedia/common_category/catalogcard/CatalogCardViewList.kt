package com.tokopedia.common_category.catalogcard

import android.content.Context
import android.util.AttributeSet
import com.tokopedia.common_category.R

class CatalogCardViewList : CatalogCardView {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(
            context: Context,
            attrs: AttributeSet?,
            defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr)


    override fun getLayout(): Int {
        return R.layout.catalog_card_layout_list
    }
}