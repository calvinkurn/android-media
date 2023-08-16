package com.tokopedia.feedplus.domain.mapper

import com.tokopedia.content.common.model.Creation
import com.tokopedia.content.common.model.FeedXHeader
import com.tokopedia.feedplus.presentation.model.ActiveTabSource
import com.tokopedia.feedplus.presentation.model.ContentCreationTypeItem
import com.tokopedia.feedplus.presentation.model.CreateContentType
import com.tokopedia.feedplus.presentation.model.CreatorType
import com.tokopedia.feedplus.presentation.model.FeedDataModel
import com.tokopedia.feedplus.presentation.model.FeedTabModel
import com.tokopedia.feedplus.presentation.model.FeedTabsModel
import com.tokopedia.feedplus.presentation.model.MetaModel
import com.tokopedia.iconunify.IconUnify
import javax.inject.Inject

/**
 * Created By : Muhammad Furqan on 09/02/23
 */
class MapperFeedTabs @Inject constructor() {
    fun transform(
        header: FeedXHeader,
        activeTabSource: ActiveTabSource
    ): FeedTabsModel {
        return FeedTabsModel(
            meta = MetaModel(
                profileApplink = header.data.userProfile.applink,
                profilePhotoUrl = header.data.userProfile.image,
                showMyProfile = header.data.userProfile.isShown,
                isCreationActive = header.data.creation.isActive,
                showLive = header.data.live.isActive,
                liveApplink = header.data.live.applink,
                entryPoints = mapCreationItems(header.data.creation)
            ),tab = FeedTabModel(
                data = header.data.tab.items
                    .sortedBy { it.position }
                    .filter { it.isActive }
                    .map {
                        FeedDataModel(
                            title = it.title,
                            key = it.key,
                            type = it.type,
                            position = it.position,
                            isActive = it.isActive
                        )
                    },
                activeTabSource = activeTabSource.copy(
                    index = header.data.tab.meta?.selectedIndex ?: 0
                )
            )
        )
    }

    private fun mapCreationItems(data: Creation): List<ContentCreationTypeItem> {
        return data.authors
            .flatMap { author ->
                author.items.mapNotNull { item ->
                    if (!item.isActive) {
                        null
                    } else {
                        val type = CreateContentType.getTypeByValue(item.type)
                        ContentCreationTypeItem(
                            name = item.title,
                            imageSrc = item.image,
                            type = type,
                            creatorType = CreatorType.getTypeByValue(author.type),
                            drawableIconId = getDefaultDrawableForCreationOption(type)
                        )
                    }
                }
            }
            .sortedBy { it.type.ordinal * CreatorType.values().size + it.creatorType.ordinal }
            .distinctBy { it.type }
    }

    private fun getDefaultDrawableForCreationOption(type: CreateContentType): Int {
        return when (type) {
            CreateContentType.Live -> IconUnify.VIDEO
            CreateContentType.Post -> IconUnify.IMAGE
            CreateContentType.ShortVideo -> IconUnify.SHORT_VIDEO
            CreateContentType.NONE -> -1
        }
    }
}
