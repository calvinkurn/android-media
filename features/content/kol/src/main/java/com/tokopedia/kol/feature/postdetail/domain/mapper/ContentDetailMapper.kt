package com.tokopedia.kol.feature.postdetail.domain.mapper

import com.tokopedia.kol.feature.postdetail.view.datamodel.DeleteContentModel
import com.tokopedia.kol.feature.postdetail.view.datamodel.ReportContentModel
import com.tokopedia.kol.feature.postdetail.view.datamodel.ShopFollowModel
import com.tokopedia.kol.feature.postdetail.view.datamodel.type.ShopFollowAction

/**
 * Created by meyta.taliti on 03/08/22.
 */
interface ContentDetailMapper {

    fun mapShopFollow(rowNumber: Int, action: ShopFollowAction): ShopFollowModel

    fun mapDeleteContent(rowNumber: Int): DeleteContentModel

    fun mapReportContent(rowNumber: Int): ReportContentModel
}