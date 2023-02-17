package com.tokopedia.feedplus.domain.mapper

import com.tokopedia.feedplus.data.FeedXHeader
import com.tokopedia.feedplus.presentation.model.*

/**
 * Created By : Muhammad Furqan on 09/02/23
 */
object MapperFeedTabs {
    fun transform(header: FeedXHeader): FeedTabsModel =
        FeedTabsModel(
            meta = MetaModel(
                selectedIndex = 0,
                profileApplink = header.data?.userProfile?.applink ?: "",
                profileWeblink = header.data?.userProfile?.weblink ?: "",
                profilePhotoUrl = header.data?.userProfile?.image ?: "",
                showMyProfile = header.data?.userProfile?.isShown ?: false
            ),
            data = header.data?.tab?.items?.map {
                FeedDataModel(
                    title = it.title ?: "",
                    key = it.key ?: "",
                    type = it.type ?: "",
                    position = it.position ?: 0,
                    isActive = it.isActive ?: false
                )
            }?.toList() ?: emptyList()
        )

    fun getCreationBottomSheetData(header: FeedXHeader): List<ContentCreationItem> =
        header.data?.creation?.authors?.map { author ->
            ContentCreationItem(
                id = author.id ?: 0,
                name = author.name ?: "",
                image = author.image ?: "",
                type = CreatorType.values().find { it.value == author.type } ?: CreatorType.NONE,
                hasUsername = author.hasUsername ?: false,
                hasAcceptTnC = author.hasAcceptTnC ?: false,
                items = author.items.map {
                    ContentCreationTypeItem(
                        name = it.title ?: "",
                        isActive = it.isActive ?: false,
                        imageSrc = it.image ?: "",
                        type = CreateContentType.values().find { createContentType ->
                            createContentType.value == it.type
                        } ?: CreateContentType.NONE,
                        weblink = it.weblink ?: "",
                        applink = it.applink ?: ""
                    )
                }.toList()
            )
        } ?: emptyList()
}
