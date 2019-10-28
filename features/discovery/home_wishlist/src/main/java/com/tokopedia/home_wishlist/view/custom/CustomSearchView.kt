package com.tokopedia.home_wishlist.view.custom

import android.content.Context
import android.util.AttributeSet
import com.tokopedia.design.text.SearchInputView
import com.tokopedia.home_wishlist.R

class CustomSearchView : SearchInputView{
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun getLayout(): Int {
        return R.layout.custom_search_view
    }
}