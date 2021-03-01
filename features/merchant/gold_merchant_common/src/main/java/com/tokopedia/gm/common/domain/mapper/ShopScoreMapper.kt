package com.tokopedia.gm.common.domain.mapper

import com.tokopedia.gm.common.constant.END_OF_TENURE_SEVEN_DAYS
import com.tokopedia.gm.common.constant.NEW_SELLER_DAYS
import com.tokopedia.gm.common.constant.PATTERN_DATE_PARAM
import com.tokopedia.gm.common.constant.PATTERN_DATE_SHOP_INFO
import com.tokopedia.gm.common.data.source.cloud.model.GetIsOfficialResponse
import com.tokopedia.gm.common.data.source.cloud.model.PMPeriodTypeResponse
import com.tokopedia.gm.common.data.source.cloud.model.ShopInfoByIDResponse
import com.tokopedia.gm.common.presentation.model.OfficialStorePeriodUiModel
import com.tokopedia.gm.common.presentation.model.ShopInfoPeriodUiModel
import com.tokopedia.kotlin.extensions.view.orZero
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.math.abs

object ShopScoreMapper {

    fun mapToGetShopInfo(shopInfoByID: ShopInfoByIDResponse.ShopInfoByID,
                         goldPMSettingInfoData: PMPeriodTypeResponse.GoldGetPMSettingInfo.Data): ShopInfoPeriodUiModel {
        return ShopInfoPeriodUiModel(
                joinDate = shopInfoByID.result.firstOrNull()?.createInfo?.shopCreated.orEmpty()
                        joinDateFormatted (PATTERN_DATE_PARAM),
                periodType = goldPMSettingInfoData.periodType,
                isNewSeller = shopInfoByID.result.firstOrNull()?.createInfo?.shopCreated.orEmpty()
                        isNewSeller (NEW_SELLER_DAYS),
                isEndTenureNewSeller = shopInfoByID.result.firstOrNull()?.createInfo?.shopCreated.orEmpty()
                        isEndOfTenureNewSeller (END_OF_TENURE_SEVEN_DAYS)
        )
    }

    fun mapToGetIsOfficialStorePeriod(getIsOfficialStoreData: GetIsOfficialResponse.GetIsOfficial.Data,
                                      goldPMSettingInfoData: PMPeriodTypeResponse.GoldGetPMSettingInfo.Data): OfficialStorePeriodUiModel {
        return OfficialStorePeriodUiModel(
                isOfficialStore = getIsOfficialStoreData.isOfficial,
                periodType = goldPMSettingInfoData.periodType)
    }

    infix fun String.joinDateFormatted(pattern: String): String {
        val simpleDateFormat = SimpleDateFormat(pattern, Locale.getDefault())
        return simpleDateFormat.format(this)
    }

    private infix fun String.isNewSeller(days: Int) = diffDays(days)

    private infix fun String.isEndOfTenureNewSeller(days: Int) = diffDays(days)

    private infix fun String.diffDays(days: Int): Boolean {
        val simpleDateFormat = SimpleDateFormat(PATTERN_DATE_SHOP_INFO, Locale.getDefault())
        val joinDate = simpleDateFormat.parse(this)
        val diffInMs: Long = abs(System.currentTimeMillis() - joinDate?.time.orZero())
        val diff = TimeUnit.DAYS.convert(diffInMs, TimeUnit.MILLISECONDS)
        return diff < days
    }

}