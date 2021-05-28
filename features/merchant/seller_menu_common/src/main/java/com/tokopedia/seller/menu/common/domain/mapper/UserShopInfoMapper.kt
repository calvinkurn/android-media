package com.tokopedia.seller.menu.common.domain.mapper

import com.tokopedia.abstraction.common.utils.view.DateFormatUtils
import com.tokopedia.gm.common.constant.PMProTier
import com.tokopedia.gm.common.constant.PMStatus
import com.tokopedia.gm.common.constant.ShopTier
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
        val targetDateText = "2021-07-14"
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
                        pmProGradeName = goldOsResult?.shopGradeWording ?: ""
                )
        )
    }

    private fun getShopType(userShopInfoResponse: UserShopInfoResponse): ShopType? {
        return when (userShopInfoResponse.shopInfoByID.result.firstOrNull()?.goldOS?.shopTier) {
            ShopTier.RM -> {
                RegularMerchant.NeedUpgrade
            }
            ShopTier.PM -> {
                val statusPM = userShopInfoResponse.userShopInfo.owner.pmStatus
                if (getPowerMerchantNotActive(statusPM))
                    PowerMerchantStatus.NotActive
                else
                    PowerMerchantStatus.Active
            }
            ShopTier.OS -> {
                ShopType.OfficialStore
            }
            ShopTier.PM_PRO -> {
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
                    else -> {
                        null
                    }
                }
            }
            else -> {
                null
            }
        }
    }

    private fun getPowerMerchantNotActive(statusPM: String): Boolean {
        return statusPM == PMStatus.INACTIVE || statusPM == PMStatus.IDLE || statusPM == PMStatus.PENDING
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