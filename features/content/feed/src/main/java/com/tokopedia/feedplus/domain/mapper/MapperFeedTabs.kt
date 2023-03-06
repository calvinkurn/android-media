package com.tokopedia.feedplus.domain.mapper

import com.tokopedia.content.common.model.GetCheckWhitelistResponse
import com.tokopedia.feedplus.data.FeedXHeader
import com.tokopedia.feedplus.oldFeed.domain.model.feed.WhitelistDomain
import com.tokopedia.feedplus.presentation.model.*
import com.tokopedia.iconunify.IconUnify

/**
 * Created By : Muhammad Furqan on 09/02/23
 */
object MapperFeedTabs {
    fun transform(header: FeedXHeader): FeedTabsModel =
        FeedTabsModel(
            meta = MetaModel(
                selectedIndex = 0,
                profileApplink = header.data.userProfile.applink,
                profileWeblink = header.data.userProfile.weblink,
                profilePhotoUrl = header.data.userProfile.image,
                showMyProfile = header.data.userProfile.isShown,
                isLoggedIn = false,
                whiteListDomain = WhitelistDomain.Empty,
            ),
            data = header.data.tab.items.map {
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
            ContentCreationItem(
                id = author.id,
                name = author.name,
                image = author.image,
                type = CreatorType.values().find { it.value == author.type } ?: CreatorType.NONE,
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
                        applink = it.applink 
                    )
                }.toList()
            )
        }

    fun transform(
        header: FeedXHeader,
        whiteList: GetCheckWhitelistResponse,
        isLoggedIn: Boolean,
    ): FeedTabsModel {
        return FeedTabsModel(
            meta = MetaModel(
                selectedIndex = 0,
                profileApplink = header.data.userProfile.applink,
                profileWeblink = header.data.userProfile.weblink,
                profilePhotoUrl = header.data.userProfile.image,
                showMyProfile = header.data.userProfile.isShown,
                isLoggedIn = isLoggedIn,
                whiteListDomain = getWhitelistDomain(whiteList),
            ),
            data = header.data.tab.items.map {
                FeedDataModel(
                    title = it.title,
                    key = it.key,
                    type = it.type,
                    position = it.position,
                    isActive = it.isActive
                )
            }
        )
    }

    private fun getWhitelistDomain(query: GetCheckWhitelistResponse): WhitelistDomain {
        return WhitelistDomain(
            error = query.whitelist.error,
            url = query.whitelist.url,
            isWhitelist = query.whitelist.isWhitelist,
            title = query.whitelist.title,
            desc = query.whitelist.description,
            titleIdentifier = query.whitelist.titleIdentifier,
            postSuccessMessage = query.whitelist.postSuccessMessage,
            image = query.whitelist.imageUrl,
            authors = query.whitelist.authors
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
