package com.tokopedia.seller.menu.common.domain.mapper

import com.tokopedia.abstraction.common.utils.view.DateFormatUtils
import com.tokopedia.gm.common.constant.*
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.seller.menu.common.domain.entity.UserShopInfoResponse
import com.tokopedia.seller.menu.common.view.uimodel.UserShopInfoWrapper
import com.tokopedia.seller.menu.common.view.uimodel.base.PowerMerchantProStatus
import com.tokopedia.seller.menu.common.view.uimodel.base.PowerMerchantStatus
import com.tokopedia.seller.menu.common.view.uimodel.base.RegularMerchant
import com.tokopedia.seller.menu.common.view.uimodel.base.ShopType
import java.text.SimpleDateFormat
import javax.inject.Inject

class UserShopInfoMapper @Inject constructor() {

    fun mapToUserShopInfoUiModel(userShopInfoResponse: UserShopInfoResponse): UserShopInfoWrapper {
        val targetDateText = "2021-06-14"
        val isBeforeOnDate = isBeforeOnDate(userShopInfoResponse.userShopInfo.info.dateShopCreated, targetDateText)
        val goldOsResult = userShopInfoResponse.shopInfoByID.result.firstOrNull()?.goldOS
        return UserShopInfoWrapper(
                shopType = getShopType(userShopInfoResponse),
                userShopInfoUiModel = UserShopInfoWrapper.UserShopInfoUiModel(
                        isBeforeOnDate = isBeforeOnDate,
                        onDate = targetDateText,
                        totalTransaction = userShopInfoResponse.userShopInfo.stats.shopTotalTransaction.toIntOrZero(),
                        badge = goldOsResult?.badge ?: "",
                        shopTierName = goldOsResult?.shopTierWording ?: "",
                        shopTier = goldOsResult?.shopTier ?: -1,
                        pmProGradeName = goldOsResult?.shopGradeWording ?: "",
                        periodTypePmPro = userShopInfoResponse.goldGetPMSettingInfo.periodTypePmPro
                )
        )
    }

    private fun getShopType(userShopInfoResponse: UserShopInfoResponse): ShopType? {
        val goldPMStatus = userShopInfoResponse.goldGetPMOSStatus.data
        val statusPM = goldPMStatus.powerMerchant.status
        return when {
            goldPMStatus.officialStore.status == OSStatus.ACTIVE -> {
                ShopType.OfficialStore
            }
            goldPMStatus.powerMerchant.pmTier == PMTier.PRO -> {
                if (getPowerMerchantNotActive(statusPM)) {
                    PowerMerchantProStatus.InActive
                } else {
                    when (userShopInfoResponse.shopInfoByID.result.firstOrNull()?.goldOS?.shopGrade) {
                        PMProTier.ADVANCE -> {
                            PowerMerchantProStatus.Advanced
                        }
                        PMProTier.EXPERT -> {
                            PowerMerchantProStatus.Expert
                        }
                        PMProTier.ULTIMATE -> {
                            PowerMerchantProStatus.Ultimate
                        }
                        else -> null
                    }
                }
            }
            goldPMStatus.powerMerchant.pmTier == PMTier.REGULAR -> {
                when {
                    statusPM == PMStatusConst.ACTIVE -> {
                        PowerMerchantStatus.Active
                    }
                    getPowerMerchantNotActive(statusPM) -> {
                        PowerMerchantStatus.NotActive
                    }
                    else -> {
                        RegularMerchant.NeedUpgrade
                    }
                }
            }
            else -> null
        }
    }

    private fun getPowerMerchantNotActive(statusPM: String): Boolean {
        return statusPM == PMStatusConst.IDLE
    }

    private fun isBeforeOnDate(createdDate: String, targetDateText: String): Boolean {
        return try {
            val PATTERN_DATE_SHOP_CREATED_INFO = "yyyy-MM-dd"
            val simpleDateFormat = SimpleDateFormat(PATTERN_DATE_SHOP_CREATED_INFO, DateFormatUtils.DEFAULT_LOCALE)
            val joinDate = simpleDateFormat.parse(createdDate)
            val targetDate = simpleDateFormat.parse(targetDateText)
            joinDate?.before(targetDate) ?: false
        } catch (e: Exception) {
            false
        }
    }
}