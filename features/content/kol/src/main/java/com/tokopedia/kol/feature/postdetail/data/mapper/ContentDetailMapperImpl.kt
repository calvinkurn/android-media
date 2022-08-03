package com.tokopedia.kol.feature.postdetail.data.mapper

import com.tokopedia.kol.feature.postdetail.domain.mapper.ContentDetailMapper
import com.tokopedia.kol.feature.postdetail.view.datamodel.DeleteContentModel
import com.tokopedia.kol.feature.postdetail.view.datamodel.ShopFollowModel
import com.tokopedia.kol.feature.postdetail.view.datamodel.type.ShopFollowAction

/**
 * Created by meyta.taliti on 03/08/22.
 */
class ContentDetailMapperImpl : ContentDetailMapper {

    override fun mapShopFollow(rowNumber: Int, action: ShopFollowAction) = ShopFollowModel(
        rowNumber = rowNumber,
        action = action
    )

    override fun mapDeleteContent(rowNumber: Int) = DeleteContentModel(rowNumber)
}