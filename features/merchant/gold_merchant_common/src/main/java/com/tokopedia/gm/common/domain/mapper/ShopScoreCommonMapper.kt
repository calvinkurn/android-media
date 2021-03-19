package com.tokopedia.gm.common.domain.mapper

import com.tokopedia.gm.common.constant.END_OF_TENURE_SEVEN_DAYS
import com.tokopedia.gm.common.constant.GoldMerchantUtil.diffDays
import com.tokopedia.gm.common.constant.GoldMerchantUtil.totalDays
import com.tokopedia.gm.common.constant.NEW_SELLER_DAYS
import com.tokopedia.gm.common.constant.PATTERN_DATE_PARAM
import com.tokopedia.gm.common.constant.PATTERN_DATE_SHOP_INFO
import com.tokopedia.gm.common.data.source.cloud.model.PMPeriodTypeResponse
import com.tokopedia.gm.common.data.source.cloud.model.ShopInfoByIDResponse
import com.tokopedia.gm.common.presentation.model.ShopInfoPeriodUiModel
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.user.session.UserSessionInterface
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.math.abs

class ShopScoreCommonMapper @Inject constructor(private val userSession: UserSessionInterface) {

    fun mapToGetShopInfo(shopInfoByID: ShopInfoByIDResponse.ShopInfoByID,
                         goldPMSettingInfoData: PMPeriodTypeResponse.GoldGetPMSettingInfo.Data): ShopInfoPeriodUiModel {
        return ShopInfoPeriodUiModel(
                isOfficialStore = userSession.isShopOfficialStore,
                joinDate = shopInfoByID.result.firstOrNull()?.createInfo?.shopCreated.orEmpty()
                        joinDateFormatted (PATTERN_DATE_PARAM),
                periodType = goldPMSettingInfoData.periodType,
                isNewSeller = shopInfoByID.result.firstOrNull()?.createInfo?.shopCreated.orEmpty()
                        isNewSeller (NEW_SELLER_DAYS),
                isEndTenureNewSeller = shopInfoByID.result.firstOrNull()?.createInfo?.shopCreated.orEmpty()
                        isEndOfTenureNewSeller (END_OF_TENURE_SEVEN_DAYS),
                shopAge = shopInfoByID.result.firstOrNull()?.createInfo?.shopCreated.orEmpty().totalDays()
        )
    }

    private infix fun String.joinDateFormatted(pattern: String): String {
        val simpleDateFormat = SimpleDateFormat(pattern, Locale.getDefault())
        return simpleDateFormat.format(this)
    }

    private infix fun String.isNewSeller(days: Int) = diffDays(days)

    private infix fun String.isEndOfTenureNewSeller(days: Int) = diffDays(days)

}