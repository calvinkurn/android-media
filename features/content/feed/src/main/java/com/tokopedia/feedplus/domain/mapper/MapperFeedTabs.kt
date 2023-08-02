package com.tokopedia.feedplus.domain.mapper

import com.tokopedia.content.common.model.FeedXHeader
import com.tokopedia.feedplus.presentation.model.ContentCreationItem
import com.tokopedia.feedplus.presentation.model.ContentCreationTypeItem
import com.tokopedia.feedplus.presentation.model.CreateContentType
import com.tokopedia.feedplus.presentation.model.CreatorType
import com.tokopedia.feedplus.presentation.model.FeedDataModel
import com.tokopedia.feedplus.presentation.model.FeedTabsModel
import com.tokopedia.feedplus.presentation.model.MetaModel
import com.tokopedia.iconunify.IconUnify

/**
 * Created By : Muhammad Furqan on 09/02/23
 */
object MapperFeedTabs {
    fun transform(
        header: FeedXHeader
    ): FeedTabsModel =
        FeedTabsModel(
            meta = MetaModel(
                selectedIndex = header.data.tab.meta?.selectedIndex ?: 0,
                profileApplink = header.data.userProfile.applink,
                profilePhotoUrl = header.data.userProfile.image,
                showMyProfile = header.data.userProfile.isShown,
                isCreationActive = header.data.creation.isActive,
                showLive = header.data.live.isActive,
                liveApplink = header.data.live.applink
            ),
            data = header.data.tab.items.sortedBy { it.position }
                .filter { it.isActive }.map {
                    FeedDataModel(
                        title = it.title,
                        key = it.key,
                        type = it.type,
                        position = it.position,
                        isActive = it.isActive
                    )
                }
        )

    fun getCreationBottomSheetData(header: FeedXHeader): List<ContentCreationItem> =
        header.data.creation.authors.map { author ->
            val creatorType = CreatorType.values().find { it.value == author.type } ?: CreatorType.NONE
            ContentCreationItem(
                id = author.id,
                name = author.name,
                image = author.image,
                type = creatorType,
                hasUsername = author.hasUsername,
                hasAcceptTnC = author.hasAcceptTnC,
                items = author.items.map {
                    val currentCreationType = CreateContentType.values().find { createContentType ->
                        createContentType.value == it.type
                    } ?: CreateContentType.NONE
                    ContentCreationTypeItem(
                        name = it.title,
                        isActive = it.isActive,
                        imageSrc = it.image,
                        drawableIconId = getDefaultDrawableForCreationOption(type = currentCreationType),
                        type = currentCreationType,
                        weblink = it.weblink,
                        applink = it.applink,
                        creatorType = creatorType
                    )
                }.toList()
            )
        }

    private fun getDefaultDrawableForCreationOption(type: CreateContentType): Int =
        when (type) {
            CreateContentType.CREATE_LIVE -> IconUnify.VIDEO
            CreateContentType.CREATE_POST -> IconUnify.IMAGE
            CreateContentType.CREATE_SHORT_VIDEO -> IconUnify.SHORT_VIDEO
            CreateContentType.NONE -> -1
        }
}
