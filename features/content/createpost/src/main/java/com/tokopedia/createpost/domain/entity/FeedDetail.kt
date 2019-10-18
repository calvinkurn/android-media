package com.tokopedia.createpost.domain.entity

import com.tokopedia.createpost.view.viewmodel.MediaModel


data class FeedDetail (
        val id: String = "",
        val type: String = "",
        val media: List<MediaModel> = listOf(),
        val postTagId: List<String> = listOf(),
        val caption: String = ""
)