package com.tokopedia.people.views.uimodel.content

import com.tokopedia.library.baseadapter.BaseItem

/**
 * Created by fachrizalmrsln at 21/11/2022
 */
data class UserFeedPostsUiModel(
    val pagination: PaginationUiModel,
    val posts: List<PostUiModel>,
)

data class PaginationUiModel(
    val cursor: String = "",
    val hasNext: Boolean = false,
    val totalData: Int = 0,
)

data class PostUiModel(
    val id: String = "",
    val appLink: String = "",
    val media: List<MediaUiModel> = emptyList(),
) : BaseItem()

data class MediaUiModel(
    val appLink: String = "",
    val coverURL: String = "",
    val id: String = "",
    val mediaURL: String = "",
    val type: String = "",
    val webLink: String = "",
)
