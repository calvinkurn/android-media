package com.tokopedia.seller.menu.common.domain.mapper

import com.tokopedia.abstraction.common.utils.view.DateFormatUtils
import com.tokopedia.gm.common.constant.*
import com.tokopedia.gm.common.utils.GoldMerchantUtil
import com.tokopedia.kotlin.extensions.orFalse
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

    fun mapToUserShopInfoUiModel(dateShopCreated: String,
                                 shopInfoByIDResult: UserShopInfoResponse.ShopInfoByID.Result?,
                                 periodTypePmPro: String,
                                 goldGetPMShopInfo: UserShopInfoResponse.GoldGetPMShopInfo,
                                 goldGetPMOSStatusData: UserShopInfoResponse.GoldGetPMOSStatus.Data): UserShopInfoWrapper {
        val targetDateText = "2021-06-14"
        val isBeforeOnDate = isBeforeOnDate(dateShopCreated, targetDateText)
        val goldOsResult = shopInfoByIDResult?.goldOS
        val txStatsValue =
            shopInfoByIDResult?.statsByDate?.find { it.identifier == Constant.TRANSACTION_RM_SUCCESS }?.value.orZero()
        return UserShopInfoWrapper(
            shopType = getShopType(
                goldGetPMOSStatusData,
                goldGetPMShopInfo,
                shopInfoByIDResult?.goldOS?.shopGrade
            ),
            userShopInfoUiModel = UserShopInfoWrapper.UserShopInfoUiModel(
                isBeforeOnDate = isBeforeOnDate,
                onDate = targetDateText,
                dateCreated = dateShopCreated,
                totalTransaction = txStatsValue,
                badge = goldOsResult?.badge ?: "",
                shopTierName = goldOsResult?.shopTierWording ?: "",
                shopTier = goldOsResult?.shopTier ?: -1,
                pmProGradeName = goldOsResult?.shopGradeWording ?: "",
                periodTypePmPro = periodTypePmPro,
                isNewSeller = GoldMerchantUtil.isNewSeller(dateShopCreated),
                isEligiblePm = goldGetPMShopInfo.isEligiblePm.orFalse(),
                isEligiblePmPro = goldGetPMShopInfo.isEligiblePmPro.orFalse(),
                statusInfoUiModel = UserShopInfoWrapper.UserShopInfoUiModel.StatusInfoUiModel(
                    statusTitle = shopInfoByIDResult?.statusInfo?.statusTitle.orEmpty(),
                    statusMessage = shopInfoByIDResult?.statusInfo?.statusMessage.orEmpty(),
                    tickerType = shopInfoByIDResult?.statusInfo?.tickerType.orEmpty()
                )
            )
        )
    }

    private fun getShopType(goldPMStatus: UserShopInfoResponse.GoldGetPMOSStatus.Data,
                            goldGetPMShopInfo: UserShopInfoResponse.GoldGetPMShopInfo,
                            shopGrade: Int?): ShopType? {
        val statusPM = goldPMStatus.powerMerchant.status
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
                                messageShopTypeErrorCrashlytics(
                                    goldPMStatus.powerMerchant.pmTier,
                                    shopGrade
                                ),
                                SellerMenuErrorHandler.ERROR_GET_SHOP_TYPE
                            )
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
                        when {
                            goldGetPMShopInfo.isPendingKyc() -> {
                                RegularMerchant.Pending
                            }
                            goldGetPMShopInfo.isVerifiedKyc() -> {
                                RegularMerchant.Verified
                            }
                            else -> {
                                RegularMerchant.NeedUpgrade
                            }
                        }
                    }
                }
            }
            else -> {
                SellerMenuErrorHandler.logExceptionToCrashlytics(
                    messageShopTypeErrorCrashlytics(goldPMStatus.powerMerchant.pmTier, shopGrade),
                    SellerMenuErrorHandler.ERROR_GET_SHOP_TYPE
                )
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
            val simpleDateFormat =
                SimpleDateFormat(PATTERN_DATE_SHOP_CREATED_INFO, DateFormatUtils.DEFAULT_LOCALE)
            val joinDate = simpleDateFormat.parse(createdDate)
            val targetDate = simpleDateFormat.parse(targetDateText)
            joinDate?.before(targetDate) ?: false
        } catch (e: Exception) {
            SellerMenuErrorHandler.logExceptionToCrashlytics(
                e, SellerMenuErrorHandler.ERROR_GET_BEFORE_ON_DATE
            )
            false
        }
    }

    private fun messageShopTypeErrorCrashlytics(pmTier: Int, shopGrade: Int?): SellerMenuException {
        val message = "Shop Id: ${userSession.shopId} | PM Tier: $pmTier | Shop grade: $shopGrade"
        return SellerMenuException(message)
    }
}