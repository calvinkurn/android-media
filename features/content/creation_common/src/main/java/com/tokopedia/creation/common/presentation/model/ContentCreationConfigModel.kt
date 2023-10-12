package com.tokopedia.creation.common.presentation.model

/**
 * Created By : Muhammad Furqan on 08/09/23
 */
data class ContentCreationConfigModel(
    val isActive: Boolean,
    val statisticApplink: String,
    val authors: List<ContentCreationConfigAuthorModel>,
    val creationItems: List<ContentCreationItemModel>
) {
    companion object {
        val Empty = ContentCreationConfigModel(
            isActive = false,
            statisticApplink = "",
            authors = emptyList(),
            creationItems = emptyList()
        )
    }
}

data class ContentCreationConfigAuthorModel(
    val id: String,
    val name: String,
    val type: String,
    val image: String,
    val hasUsername: Boolean,
    val hasAcceptTnc: Boolean
)
