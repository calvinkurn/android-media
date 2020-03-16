package com.tokopedia.entertainment.search.adapter.viewmodel

import com.tokopedia.entertainment.search.adapter.DetailEventItem
import com.tokopedia.entertainment.search.adapter.factory.DetailTypeFactory
import com.tokopedia.entertainment.search.adapter.viewholder.CategoryTextViewHolder

class CategoryTextViewModel(val listsCategory: MutableList<CategoryTextViewHolder.CategoryTextBubble>) : DetailEventItem<DetailTypeFactory> {

    override fun type(typeFactory: DetailTypeFactory): Int {
        return typeFactory.type(this)
    }
}