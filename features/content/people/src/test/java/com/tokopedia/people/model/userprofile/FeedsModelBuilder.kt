package com.tokopedia.people.model.userprofile

import com.tokopedia.people.views.uimodel.content.MediaUiModel
import com.tokopedia.people.views.uimodel.content.PaginationUiModel
import com.tokopedia.people.views.uimodel.content.PostUiModel
import com.tokopedia.people.views.uimodel.content.UserFeedPostsUiModel

/**
 * Created by fachrizalmrsln at 21/11/2022
 */
class FeedsModelBuilder {

    fun mockFeedsPost(isEmpty: Boolean = false): UserFeedPostsUiModel {
        return UserFeedPostsUiModel(
            pagination = PaginationUiModel(
                cursor = "123",
                hasNext = true,
                totalData = 10,
            ),
            posts = if (isEmpty) emptyList() else posts(),
        )
    }

    private fun posts(): List<PostUiModel> {
        return listOf(
            PostUiModel(
                id = "123",
                appLink = "applink",
                media = listOf(
                    MediaUiModel(
                        appLink = "applink media",
                        coverURL = "coverUrl",
                        mediaURL = "mediaUrl",
                        id = "321",
                        type = "video",
                        webLink = "weblink",
                    ),
                ),
            ),
        )
    }

}
