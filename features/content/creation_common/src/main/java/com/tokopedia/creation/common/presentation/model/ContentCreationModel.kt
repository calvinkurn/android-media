package com.tokopedia.creation.common.presentation.model

/**
 * Created By : Muhammad Furqan on 06/09/23
 */
data class ContentCreationItemModel(
    val isActive: Boolean,
    val type: ContentCreationTypeEnum,
    val title: String,
    val applink: String,
    val media: ContentCreationMediaModel,
    val descriptionApplink: String,
    val drawableIconId: Int,
    val authorType: ContentCreationAuthorEnum
)

data class ContentCreationMediaModel(
    val type: String,
    val id: String,
    val coverUrl: String,
    val mediaUrl: String,
)
