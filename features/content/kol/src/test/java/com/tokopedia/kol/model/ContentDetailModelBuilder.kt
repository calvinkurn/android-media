package com.tokopedia.kol.model

import com.tokopedia.feedcomponent.data.feedrevamp.FeedXCard
import com.tokopedia.kol.feature.postdetail.view.datamodel.*
import com.tokopedia.kol.feature.postdetail.view.datamodel.type.ContentLikeAction
import com.tokopedia.kol.feature.postdetail.view.datamodel.type.ShopFollowAction

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

    fun getShopFollowModel(
        followAction: ShopFollowAction,
        rowNumber: Int = 0,
    ) = ShopFollowModel(
        rowNumber = rowNumber,
        action = followAction,
    )

    fun getVisitContentModel(rowNumber: Int = 0) = VisitContentModel(rowNumber)

    fun getDeleteContentModel(rowNumber: Int = 0) = DeleteContentModel(rowNumber)

    fun getWishlistModel(
        rowNumber: Int = 0,
        productId: String = ""
    ) = WishlistContentModel(
        rowNumber = rowNumber,
        productId = productId
    )

    fun getReportContentModel(rowNumber: Int = 0) = ReportContentModel(rowNumber)
}