package com.tokopedia.kol.feature.postdetail.domain.mapper

import com.tokopedia.kol.feature.postdetail.view.datamodel.*
import com.tokopedia.kol.feature.postdetail.view.datamodel.type.ContentLikeAction
import com.tokopedia.kol.feature.postdetail.view.datamodel.type.ShopFollowAction

/**
 * Created by meyta.taliti on 03/08/22.
 */
interface ContentDetailMapper {

    fun mapLikeContent(rowNumber: Int, action: ContentLikeAction): LikeContentModel

    fun mapShopFollow(rowNumber: Int, action: ShopFollowAction): ShopFollowModel

    fun mapDeleteContent(rowNumber: Int): DeleteContentModel

    fun mapReportContent(rowNumber: Int): ReportContentModel

    fun mapVisitChannel(rowNumber: Int): VisitChannelModel
}