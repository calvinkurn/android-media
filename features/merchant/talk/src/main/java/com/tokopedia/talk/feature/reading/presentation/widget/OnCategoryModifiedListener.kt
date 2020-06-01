package com.tokopedia.talk.feature.reading.presentation.widget

interface OnCategoryModifiedListener {
    fun onCategorySelected(categoryName: String, chipType: String)
    fun onCategoriesCleared()
}