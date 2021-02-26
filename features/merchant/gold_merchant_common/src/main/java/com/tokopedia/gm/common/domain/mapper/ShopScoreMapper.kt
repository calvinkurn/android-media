package com.tokopedia.gm.common.domain.mapper

import com.tokopedia.gm.common.constant.PATTERN_DATE_SHOP_INFO
import com.tokopedia.gm.common.data.source.cloud.model.PMPeriodTypeResponse
import com.tokopedia.gm.common.data.source.cloud.model.ShopInfoByIDResponse
import com.tokopedia.gm.common.presentation.ShopInfoTransitionUiModel
import com.tokopedia.kotlin.extensions.view.orZero
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.math.abs

object ShopScoreMapper {

    fun mapToGetShopInfo(shopInfoByID: ShopInfoByIDResponse.ShopInfoByID,
                         goldPMSettingInfoData: PMPeriodTypeResponse.GoldGetPMSettingInfo.Data): ShopInfoTransitionUiModel {
        val shopInfoTransitionUiModel = ShopInfoTransitionUiModel()
        shopInfoTransitionUiModel.joinDate = shopInfoByID.result.firstOrNull()?.createInfo?.shopCreated.orEmpty()
        shopInfoTransitionUiModel.periodType = goldPMSettingInfoData.periodType
        shopInfoTransitionUiModel.isNewSeller = shopInfoTransitionUiModel.joinDate.isNewSeller()
        return shopInfoTransitionUiModel
    }

    private fun String.isNewSeller(): Boolean {
        val simpleDateFormat = SimpleDateFormat(PATTERN_DATE_SHOP_INFO, Locale.getDefault())
        val joinDate = simpleDateFormat.parse(this)
        val diffMillies: Long = abs(System.currentTimeMillis() - joinDate?.time.orZero())
        val diff = TimeUnit.DAYS.convert(diffMillies, TimeUnit.MILLISECONDS)
        return diff < 90
    }
}