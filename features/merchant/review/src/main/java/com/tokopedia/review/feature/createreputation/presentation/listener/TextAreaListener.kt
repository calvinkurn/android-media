package com.tokopedia.review.feature.createreputation.presentation.listener

interface TextAreaListener {
    fun onExpandButtonClicked(text: String)
    fun onCollapseButtonClicked(text: String)
    fun onDismissBottomSheet(text: String, templates: List<String>)
    fun scrollToShowTextArea()
    fun trackWhenHasFocus(textLength: Int)
    fun onTextChanged(textLength: Int)
    fun hideText()
}