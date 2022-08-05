package com.tokopedia.kol.feature.postdetail.data.mapper

import com.tokopedia.kol.feature.postdetail.domain.mapper.ContentDetailMapper
import com.tokopedia.kol.feature.postdetail.view.datamodel.*
import com.tokopedia.kol.feature.postdetail.view.datamodel.type.ContentLikeAction
import com.tokopedia.kol.feature.postdetail.view.datamodel.type.ShopFollowAction

/**
 * Created by meyta.taliti on 03/08/22.
 */
class ContentDetailMapperImpl : ContentDetailMapper {

    override fun mapLikeContent(rowNumber: Int, action: ContentLikeAction) = LikeContentModel(
        rowNumber = rowNumber,
        action = action
    )

    override fun mapShopFollow(rowNumber: Int, action: ShopFollowAction) = ShopFollowModel(
        rowNumber = rowNumber,
        action = action
    )

    override fun mapDeleteContent(rowNumber: Int) = DeleteContentModel(rowNumber)

    override fun mapReportContent(rowNumber: Int) = ReportContentModel(rowNumber)

    override fun mapVisitChannel(rowNumber: Int) = VisitContentModel(rowNumber)
}