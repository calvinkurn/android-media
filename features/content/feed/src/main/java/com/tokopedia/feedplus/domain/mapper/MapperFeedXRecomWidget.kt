package com.tokopedia.feedplus.domain.mapper

import com.tokopedia.feedplus.data.FeedXRecomWidgetEntity
import com.tokopedia.feedplus.presentation.model.FeedFollowRecommendationModel
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on July 26, 2023
 */
class MapperFeedXRecomWidget @Inject constructor(

) {

    fun transform(
        entity: FeedXRecomWidgetEntity,
        widgetId: String
    ): FeedFollowRecommendationModel {
        val data = entity.wrapper

        return FeedFollowRecommendationModel(
            id = widgetId,
            title = data.title,
            description = data.subtitle,
            data = data.items.map { profile ->
                FeedFollowRecommendationModel.Profile(
                    id = profile.id,
                    encryptedId = profile.encryptedId,
                    name = profile.name,
                    badge = profile.badgeImageUrl,
                    type = FeedFollowRecommendationModel.ProfileType.mapType(profile.type),
                    imageUrl = profile.logoImageUrl,
                    thumbnailUrl = profile.coverUrl,
                    videoUrl = profile.mediaUrl,
                    applink = profile.applink,
                    isFollowed = false,
                )
            },
            cursor = data.nextCursor,
            status = FeedFollowRecommendationModel.Status.Success,
        )
    }
}
