package com.tokopedia.feedplus.presentation.model

/**
 * Created By : Muhammad Furqan on 09/02/23
 */
data class FeedTabsModel(
    val data: List<DataModel>,
    val meta: MetaModel
)

data class DataModel(
    val title: String,
    val key: String,
    val type: String,
    val position: Int,
    val isActive: Boolean
)

data class MetaModel(
    val selectedIndex: Int,
    val profileApplink: String,
    val profileWeblink: String,
    val profilePhotoUrl: String,
    val showMyProfile: Boolean
)
