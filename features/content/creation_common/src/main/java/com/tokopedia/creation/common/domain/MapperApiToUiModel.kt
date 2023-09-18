package com.tokopedia.creation.common.domain

import com.tokopedia.creation.common.data.ContentCreationAuthorEntity
import com.tokopedia.creation.common.data.ContentCreationAuthorItemEntity
import com.tokopedia.creation.common.data.ContentCreationConfigEntity
import com.tokopedia.creation.common.presentation.model.ContentCreationAuthorEnum
import com.tokopedia.creation.common.presentation.model.ContentCreationConfigAuthorModel
import com.tokopedia.creation.common.presentation.model.ContentCreationConfigModel
import com.tokopedia.creation.common.presentation.model.ContentCreationItemModel
import com.tokopedia.creation.common.presentation.model.ContentCreationMediaModel
import com.tokopedia.creation.common.presentation.model.ContentCreationTypeEnum
import com.tokopedia.iconunify.IconUnify

/**
 * Created By : Muhammad Furqan on 08/09/23
 */
fun ContentCreationConfigEntity.toConfigModel(): ContentCreationConfigModel =
    ContentCreationConfigModel(
        isActive = this.isActive,
        statisticApplink = this.statistic.applink,
        authors = this.authors.map { it.toConfigAuthorModel() },
        creationItems = this.authors.flatMap { author ->
            author.items.mapNotNull { item ->
                if (!item.isActive) {
                    null
                } else {
                    item.toCreationItemModel(author.typeEnum)
                }
            }
        }.sortedBy {
            it.type.ordinal
        }.distinctBy { it.type }
    )

fun ContentCreationAuthorEntity.toConfigAuthorModel(): ContentCreationConfigAuthorModel =
    ContentCreationConfigAuthorModel(
        id = this.id,
        name = this.name,
        type = this.type,
        image = this.image,
        hasUsername = this.hasUsername,
        hasAcceptTnc = this.hasAcceptTnC
    )

fun ContentCreationAuthorItemEntity.toCreationItemModel(authorTypeEnum: ContentCreationAuthorEnum): ContentCreationItemModel? =
    this.typeEnum?.let {
        ContentCreationItemModel(
            isActive = this.isActive,
            type = it,
            title = this.title,
            applink = this.applink,
            media = this.media.toCreationModel(),
            descriptionApplink = this.descriptionCTA.applink,
            drawableIconId = getDefaultDrawableForCreationOption(it),
            authorType = authorTypeEnum
        )
    }

fun ContentCreationAuthorItemEntity.ContentCreationMediaEntity.toCreationModel(): ContentCreationMediaModel =
    ContentCreationMediaModel(
        type = this.type,
        id = this.id,
        coverUrl = this.coverUrl,
        mediaUrl = this.mediaUrl
    )

private fun getDefaultDrawableForCreationOption(type: ContentCreationTypeEnum): Int =
    when (type) {
        ContentCreationTypeEnum.LIVE -> IconUnify.VIDEO
        ContentCreationTypeEnum.POST -> IconUnify.IMAGE
        ContentCreationTypeEnum.SHORT -> IconUnify.SHORT_VIDEO
        ContentCreationTypeEnum.STORY -> IconUnify.PHONE
    }
