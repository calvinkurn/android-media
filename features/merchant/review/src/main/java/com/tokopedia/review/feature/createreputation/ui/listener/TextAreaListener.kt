package com.tokopedia.review.feature.createreputation.ui.listener

interface TextAreaListener {
    fun onExpandButtonClicked(text: String)
    fun onCollapseButtonClicked(text: String)
    fun onHasFocus()
}