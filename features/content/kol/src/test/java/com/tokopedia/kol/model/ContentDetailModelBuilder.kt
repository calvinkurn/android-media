package com.tokopedia.kol.model

import com.tokopedia.feedcomponent.data.feedrevamp.FeedXCard
import com.tokopedia.kol.feature.postdetail.view.datamodel.ContentDetailUiModel
import com.tokopedia.kol.feature.postdetail.view.datamodel.LikeContentModel
import com.tokopedia.kol.feature.postdetail.view.datamodel.type.ContentLikeAction

/**
 * Created by meyta.taliti on 07/08/22.
 */
class ContentDetailModelBuilder {

    fun getContentDetail(
        contents: List<FeedXCard> = emptyList(),
        cursor: String = ""
    ) = ContentDetailUiModel(
        postList = contents,
        cursor = cursor,
    )

    fun getLikeContentModel(
        likeAction: ContentLikeAction,
        rowNumber: Int = 0,
    ) = LikeContentModel(
        rowNumber = rowNumber,
        action = likeAction
    )
}