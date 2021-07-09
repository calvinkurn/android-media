package com.tokopedia.seller.menu.common.domain.mapper

import com.tokopedia.abstraction.common.utils.view.DateFormatUtils
import com.tokopedia.gm.common.constant.*
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.seller.menu.common.constant.Constant
import com.tokopedia.seller.menu.common.domain.entity.UserShopInfoResponse
import com.tokopedia.seller.menu.common.errorhandler.SellerMenuErrorHandler
import com.tokopedia.seller.menu.common.exception.SellerMenuException
import com.tokopedia.seller.menu.common.view.uimodel.UserShopInfoWrapper
import com.tokopedia.seller.menu.common.view.uimodel.base.PowerMerchantProStatus
import com.tokopedia.seller.menu.common.view.uimodel.base.PowerMerchantStatus
import com.tokopedia.seller.menu.common.view.uimodel.base.RegularMerchant
import com.tokopedia.seller.menu.common.view.uimodel.base.ShopType
import com.tokopedia.user.session.UserSessionInterface
import java.text.SimpleDateFormat
import javax.inject.Inject

class UserShopInfoMapper @Inject constructor(private val userSession: UserSessionInterface) {

    fun mapToUserShopInfoUiModel(userShopInfoResponse: UserShopInfoResponse): UserShopInfoWrapper {
        val targetDateText = "2021-06-14"
        val isBeforeOnDate = isBeforeOnDate(userShopInfoResponse.userShopInfo.info.dateShopCreated, targetDateText)
        val goldOsResult = userShopInfoResponse.shopInfoByID.result.firstOrNull()?.goldOS
        val txStatsValue = userShopInfoResponse.shopInfoByID.result.firstOrNull()?.statsByDate?.find { it.identifier == Constant.TRANSACTION_RM_SUCCESS }?.value.orZero()
        val dateCreated = userShopInfoResponse.userShopInfo.info.dateShopCreated
        return UserShopInfoWrapper(
                shopType = getShopType(userShopInfoResponse),
                userShopInfoUiModel = UserShopInfoWrapper.UserShopInfoUiModel(
                        isBeforeOnDate = isBeforeOnDate,
                        onDate = targetDateText,
                        dateCreated = dateCreated,
                        totalTransaction = txStatsValue,
                        badge = goldOsResult?.badge ?: "",
                        shopTierName = goldOsResult?.shopTierWording ?: "",
                        shopTier = goldOsResult?.shopTier ?: -1,
                        pmProGradeName = goldOsResult?.shopGradeWording ?: "",
                        periodTypePmPro = userShopInfoResponse.goldGetPMSettingInfo.periodTypePmPro,
                        isNewSeller = GoldMerchantUtil.isNewSeller(dateCreated)
                )
        )
    }

    private fun getShopType(userShopInfoResponse: UserShopInfoResponse): ShopType? {
        val goldPMStatus = userShopInfoResponse.goldGetPMOSStatus.data
        val statusPM = goldPMStatus.powerMerchant.status
        val shopGrade = userShopInfoResponse.shopInfoByID.result.firstOrNull()?.goldOS?.shopGrade
        return when {
            goldPMStatus.officialStore.status == OSStatus.ACTIVE -> {
                ShopType.OfficialStore
            }
            goldPMStatus.powerMerchant.pmTier == PMTier.PRO -> {
                if (getPowerMerchantNotActive(statusPM)) {
                    PowerMerchantProStatus.InActive
                } else {
                    when (shopGrade) {
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
                            SellerMenuErrorHandler.logExceptionToCrashlytics(
                                    messageShopTypeErrorCrashlytics(goldPMStatus.powerMerchant.pmTier, shopGrade),
                                    SellerMenuErrorHandler.ERROR_GET_SHOP_TYPE)
                            null
                        }
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
            else -> {
                SellerMenuErrorHandler.logExceptionToCrashlytics(
                        messageShopTypeErrorCrashlytics(goldPMStatus.powerMerchant.pmTier, shopGrade),
                        SellerMenuErrorHandler.ERROR_GET_SHOP_TYPE)
                null
            }
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
            SellerMenuErrorHandler.logExceptionToCrashlytics(
                    e, SellerMenuErrorHandler.ERROR_GET_BEFORE_ON_DATE)
            false
        }
    }

    private fun messageShopTypeErrorCrashlytics(pmTier: Int, shopGrade: Int?): SellerMenuException {
        val message = "Shop Id: ${userSession.shopId} | PM Tier: $pmTier | Shop grade: $shopGrade"
        return SellerMenuException(message)
    }
}