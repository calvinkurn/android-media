package com.tokopedia.review.feature.createreputation.presentation.listener

interface TextAreaListener {
    fun onExpandButtonClicked(text: String)
    fun onCollapseButtonClicked(text: String)
    fun onDismissBottomSheet(text: String)
    fun scrollToShowTextArea()
    fun trackWhenHasFocus(isEmpty: Boolean)
}