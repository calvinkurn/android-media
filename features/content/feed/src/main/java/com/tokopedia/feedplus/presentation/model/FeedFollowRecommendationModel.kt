package com.tokopedia.feedplus.presentation.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.feedplus.presentation.adapter.FeedAdapterTypeFactory

/**
 * Created By : Jonathan Darwin on July 04, 2023
 */
data class FeedFollowRecommendationModel(
    val id: String,
    val title: String,
    val description: String,
    val data: List<Profile>,
    val cursor: String,
    val isFetch: Boolean,
) : Visitable<FeedAdapterTypeFactory> {

    val hasNext: Boolean
        get() = cursor.isNotEmpty()

    override fun type(typeFactory: FeedAdapterTypeFactory): Int = typeFactory.type(this)

    data class Profile(
        val id: String,
        val encryptedId: String,
        val name: String,
        val badge: String,
        val type: ProfileType,
        val imageUrl: String,
        val thumbnailUrl: String,
        val videoUrl: String,
        val isFollowed: Boolean,
    ) {
        val isShop: Boolean
            get() = type == ProfileType.Seller
    }

    enum class ProfileType {
        Seller, Ugc, Unknown;

        companion object {
            fun mapType(type: Int): ProfileType {
                return when (type) {
                    2 -> Seller
                    3 -> Ugc
                    else -> Unknown
                }
            }
        }
    }

    companion object {
        val Empty: FeedFollowRecommendationModel
            get() = FeedFollowRecommendationModel(
                id = "",
                title = "",
                description = "",
                data = emptyList(),
                cursor = "",
                isFetch = false,
            )
    }
}
