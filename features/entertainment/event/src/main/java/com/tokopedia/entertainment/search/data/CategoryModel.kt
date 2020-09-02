package com.tokopedia.entertainment.search.data

import com.tokopedia.entertainment.search.adapter.viewholder.CategoryTextBubbleAdapter

data class CategoryModel(var listCategory: List<CategoryTextBubbleAdapter.CategoryTextBubble> = listOf(),
                         var hashSet: HashSet<String> = hashSetOf(),
                         var position: Int = -1) {
}