package com.tokopedia.feedplus.presentation.model

/**
 * Created By : Muhammad Furqan on 28/02/23
 */
data class FeedMediaModel(
    val type: String,
    val id: String,
    val coverUrl: String,
    val mediaUrl: String,
    val applink: String,
    val weblink: String,
    val tagging: List<FeedMediaTagging>
)

data class FeedMediaRatioModel(
    val width: Int = 0,
    val height: Int = 0
)

data class FeedMediaTagging(
    val tagIndex: Int,
    val posX: Double,
    val posY: Double
)
