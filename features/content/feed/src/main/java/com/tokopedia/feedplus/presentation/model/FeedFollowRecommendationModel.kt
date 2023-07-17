package com.tokopedia.feedplus.presentation.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.feedplus.presentation.adapter.FeedAdapterTypeFactory

/**
 * Created By : Jonathan Darwin on July 04, 2023
 */
data class FeedFollowRecommendationModel(
    val title: String,
    val description: String,
    val data: List<Profile>,
    val hasNext: Boolean,
    val cursor: String,
) : Visitable<FeedAdapterTypeFactory> {

    override fun type(typeFactory: FeedAdapterTypeFactory): Int = typeFactory.type(this)

    data class Profile(
        val id: String,
        val name: String,
        val badge: String,
        val type: String,
        val imageUrl: String,
        val thumbnailUrl: String,
        val videoUrl: String,
        val isFollow: Boolean,
    )
}
