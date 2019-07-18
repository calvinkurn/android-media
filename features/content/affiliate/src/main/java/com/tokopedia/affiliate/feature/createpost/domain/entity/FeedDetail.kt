package com.tokopedia.affiliate.feature.createpost.domain.entity

import com.tokopedia.affiliate.feature.createpost.view.viewmodel.MediaModel

data class FeedDetail (
        val id: String = "",
        val type: String = "",
        val media: List<MediaModel> = listOf(),
        val postTagId: List<String> = listOf(),
        val caption: String = ""
)