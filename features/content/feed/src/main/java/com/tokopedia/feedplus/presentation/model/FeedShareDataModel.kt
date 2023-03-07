package com.tokopedia.feedplus.presentation.model

data class FeedShareDataModel(
    val id: String,
    val name: String,
    val tnTitle: String,
    val tnImage: String,
    val ogUrl: String
) {
    companion object {
        const val PAGE = "Feed Page"
        const val FEATURE = "share"
    }
}
