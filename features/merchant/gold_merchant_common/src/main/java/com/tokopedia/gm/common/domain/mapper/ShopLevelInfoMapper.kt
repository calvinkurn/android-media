package com.tokopedia.gm.common.domain.mapper

import com.tokopedia.gm.common.data.source.cloud.model.ShopLevelResponse
import com.tokopedia.gm.common.presentation.model.ShopLevelUiModel
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.orZero
import javax.inject.Inject

/**
 * Created by @ilhamsuaib on 19/05/22.
 */

class ShopLevelInfoMapper @Inject constructor() {

    fun mapToUiModel(shopLevel: ShopLevelResponse.ShopLevelModel.ResultModel): ShopLevelUiModel {
        return ShopLevelUiModel(
            itemSold = shopLevel.itemSold.orZero(),
            nextUpdate = shopLevel.nextUpdate.orEmpty(),
            netItemValue = shopLevel.niv.orZero(),
            period = shopLevel.period.orEmpty(),
            shopLevel = shopLevel.shopLevel ?: Int.ONE
        )
    }
}