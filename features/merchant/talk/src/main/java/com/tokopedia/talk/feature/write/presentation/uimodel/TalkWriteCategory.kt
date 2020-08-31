package com.tokopedia.talk.feature.write.presentation.uimodel

data class TalkWriteCategory(
        val categoryName: String,
        val content: String,
        var isSelected: Boolean
)