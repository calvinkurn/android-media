package com.tokopedia.feedplus.data

import com.tokopedia.feedplus.presentation.model.ActiveTabSource
import com.tokopedia.feedplus.presentation.model.ContentCreationTypeItem
import com.tokopedia.feedplus.presentation.model.CreateContentType
import com.tokopedia.feedplus.presentation.model.CreatorType
import com.tokopedia.feedplus.presentation.model.FeedDataModel
import com.tokopedia.feedplus.presentation.model.FeedTabModel
import com.tokopedia.feedplus.presentation.model.FeedTabsModel
import com.tokopedia.feedplus.presentation.model.MetaModel

/**
 * Created by meyta.taliti on 09/08/23.
 */
class FeedTabsModelBuilder {

    fun buildUiModel(
        meta: MetaModel = MetaModel.Empty,
        data: List<FeedDataModel> = emptyList()
    ) = FeedTabsModel(
        meta = meta,
        tab = FeedTabModel(data, ActiveTabSource.Empty)
    )

    fun buildDefaultMetaModel(
        eligibleCreationEntryPoints: List<ContentCreationTypeItem> = emptyList()
    ) = MetaModel(
        profileApplink = "",
        profilePhotoUrl = "",
        showMyProfile = false,
        isCreationActive = false,
        showLive = false,
        liveApplink = "",
        entryPoints = eligibleCreationEntryPoints,
        showBrowse = false,
        browseApplink = ""
    )

    fun buildDefaultTabsModel() = listOf(
        FeedDataModel(
            isActive = true,
            position = 1,
            type = "foryou",
            title = "Untuk Kamu",
            key = "foryou",
            isSelected = false,
            hasNewContent = false
        ),
        FeedDataModel(
            isActive = true,
            position = 2,
            type = "following",
            title = "Following",
            key = "following",
            isSelected = false,
            hasNewContent = true
        )
    )

    fun buildCustomTabsModel() = listOf(
        FeedDataModel(
            isActive = false,
            position = 1,
            type = "foryou",
            title = "Untuk Kamu",
            key = "foryou",
            isSelected = false,
            hasNewContent = false
        ),
        FeedDataModel(
            isActive = true,
            position = 2,
            type = "following",
            title = "Following",
            key = "following",
            isSelected = false,
            hasNewContent = true
        )
    )

    fun buildCreationEntryPointUiModel(
        authorType: CreatorType,
        creationType: CreateContentType
    ) = ContentCreationTypeItem(
        name = "",
        imageSrc = "",
        type = creationType,
        creatorType = authorType
    )
}
