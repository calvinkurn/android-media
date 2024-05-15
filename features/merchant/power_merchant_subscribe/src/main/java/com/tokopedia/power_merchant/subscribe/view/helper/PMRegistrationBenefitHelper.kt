package com.tokopedia.power_merchant.subscribe.view.helper

import android.content.Context
import com.tokopedia.gm.common.constant.PMConstant
import com.tokopedia.gm.common.data.source.local.model.PMBenefitItemUiModel
import com.tokopedia.gm.common.data.source.local.model.PMGradeWithBenefitsUiModel
import com.tokopedia.gm.common.data.source.local.model.PMShopInfoUiModel
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.common.constant.Constant

/**
 * Created by @ilhamsuaib on 22/04/22.
 */

object PMRegistrationBenefitHelper {

    fun getPMBenefitList(
        context: Context,
        shopInfo: PMShopInfoUiModel?
    ): List<PMGradeWithBenefitsUiModel> {
        return if (shopInfo == null) {
            emptyList()
        } else {
            listOf(
                getPMBenefits(context, shopInfo),
                getPMProBenefits(context, shopInfo),
            )
        }
    }

    private fun getPMProUltimateBenefits(
        context: Context,
        shopInfo: PMShopInfoUiModel
    ): PMGradeWithBenefitsUiModel {
        return PMGradeWithBenefitsUiModel.PMProUltimate(
            gradeName = Constant.PM_PRO_ULTIMATE,
            isTabActive = shopInfo.shopLevel == PMConstant.ShopLevel.FOUR && shopInfo.isEligiblePmPro,
            tabLabel = context.getString(R.string.pm_pro_ultimate),
            tabResIcon = IconUnify.BADGE_PMPRO_FILLED,
            benefitList = listOf(
                getTopAdsBenefit(
                    context,
                    Constant.PM_PRO_ULT_TOP_ADS_CREDIT,
                    Constant.PM_PRO_ULT_BROAD_CAST_CHAT
                ),
                getSpecialReleaseBenefit(context, Constant.PM_PRO_ULT_SPECIAL_RELEASE),
                getProductBundlingBenefit(context, Constant.PM_PRO_ULT_PRODUCT_BUNDLING),
                getFlashSaleBenefit(context)
            )
        )
    }

    private fun getPMProExpertBenefits(
        context: Context,
        shopInfo: PMShopInfoUiModel
    ): PMGradeWithBenefitsUiModel {
        return PMGradeWithBenefitsUiModel.PMProExpert(
            gradeName = Constant.PM_PRO_EXPERT,
            isTabActive = shopInfo.shopLevel == PMConstant.ShopLevel.THREE && shopInfo.isEligiblePmPro,
            tabLabel = context.getString(R.string.pm_pro_expert),
            tabResIcon = IconUnify.BADGE_PMPRO_FILLED,
            benefitList = listOf(
                getTopAdsBenefit(
                    context,
                    Constant.PM_PRO_EXP_TOP_ADS_CREDIT,
                    Constant.PM_PRO_EXP_BROAD_CAST_CHAT
                ),
                getSpecialReleaseBenefit(context, Constant.PM_PRO_EXP_SPECIAL_RELEASE),
                getProductBundlingBenefit(context, Constant.PM_PRO_EXP_PRODUCT_BUNDLING),
                getFlashSaleBenefit(context)
            )
        )
    }

    private fun getPMProAdvanceBenefits(
        context: Context,
        shopInfo: PMShopInfoUiModel
    ): PMGradeWithBenefitsUiModel {
        return PMGradeWithBenefitsUiModel.PMProAdvance(
            gradeName = Constant.PM_PRO_ADVANCED,
            isTabActive = shopInfo.shopLevel == PMConstant.ShopLevel.TWO && shopInfo.isEligiblePmPro,
            tabLabel = context.getString(R.string.pm_pro_advanced),
            tabResIcon = IconUnify.BADGE_PMPRO_FILLED,
            benefitList = listOf(
                getTopAdsBenefit(
                    context,
                    Constant.PM_PRO_ADV_TOP_ADS_CREDIT,
                    Constant.PM_PRO_ADV_BROAD_CAST_CHAT
                ),
                getSpecialReleaseBenefit(context, Constant.PM_PRO_ADV_SPECIAL_RELEASE),
                getProductBundlingBenefit(context, Constant.PM_PRO_ADV_PRODUCT_BUNDLING),
                PMBenefitItemUiModel(
                    iconUrl = Constant.Image.IC_PM_FLASH_SALE,
                    benefitDescription = context.getString(R.string.pm_benefit_flash_sale)
                )
            )
        )
    }

    private fun getPMBenefits(
        context: Context,
        shopInfo: PMShopInfoUiModel
    ): PMGradeWithBenefitsUiModel {
        return PMGradeWithBenefitsUiModel.PM(
            gradeName = Constant.POWER_MERCHANT,
            isTabActive = shopInfo.isEligiblePm,
            tabLabel = context.getString(R.string.pm_power_merchant),
            benefitList = listOf(
                getBadgeBenefit(context, true),
                getAccessBenefit(context, true),
                getPotentialVisitorBenefit(context)
            )
        )
    }

    private fun getPMProBenefits(
        context: Context,
        shopInfo: PMShopInfoUiModel
    ): PMGradeWithBenefitsUiModel {
        return PMGradeWithBenefitsUiModel.PMProExpert(
            gradeName = Constant.POWER_MERCHANT,
            isTabActive = shopInfo.isEligiblePmPro,
            tabLabel = context.getString(R.string.pm_pm_pro),
            benefitList = listOf(
                getBadgeBenefit(context, false),
                getAccessBenefit(context, false),
                getPotentialVisitorBenefit(context)
            )
        )
    }

    private fun getFlashSaleBenefit(context: Context): PMBenefitItemUiModel {
        return PMBenefitItemUiModel(
            iconUrl = Constant.Image.IC_PM_FLASH_SALE,
            benefitDescription = context.getString(R.string.pm_benefit_flash_sale_and_discount)
        )
    }

    private fun getProductBundlingBenefit(context: Context, quota: Int): PMBenefitItemUiModel {
        return PMBenefitItemUiModel(
            iconUrl = Constant.Image.IC_PM_PRODUCT_BUNDLING,
            benefitDescription = context.getString(R.string.pm_benefit_product_bundling, quota)
        )
    }

    private fun getSpecialReleaseBenefit(context: Context, quota: Int): PMBenefitItemUiModel {
        return PMBenefitItemUiModel(
            iconUrl = Constant.Image.IC_PM_SPECIAL_RELEASE,
            benefitDescription = context.getString(
                R.string.pm_benefit_special_release,
                quota
            )
        )
    }

    private fun getTopAdsBenefit(
        context: Context,
        topAdsCredit: String,
        broadcastChat: String
    ): PMBenefitItemUiModel {
        return PMBenefitItemUiModel(
            iconUrl = Constant.Image.IC_PM_TOP_ADS,
            benefitDescription = context.getString(
                R.string.pm_benefit_top_ads, topAdsCredit, broadcastChat
            )
        )
    }

    private fun getBadgeBenefit(
        context: Context,
        isPM: Boolean): PMBenefitItemUiModel {
        return PMBenefitItemUiModel(
            icon = if (isPM) {
                IconUnify.BADGE_PM_FILLED
            } else {
                IconUnify.BADGE_PMPRO_FILLED
            },
            benefitDescription = if (isPM) {
                context.getString(R.string.pm_pm_badge_benefit)
            } else {
                context.getString(R.string.pm_pm_pro_badge_benefit)
            }
        )
    }
    private fun getAccessBenefit(
        context: Context,
        isPM: Boolean): PMBenefitItemUiModel {
        return PMBenefitItemUiModel(
            iconUrl = PMConstant.Images.PM_SHOP_PROMO_ICON,
            benefitDescription = if (isPM) {
                context.getString(R.string.pm_pm_access_benefit)
            } else {
                context.getString(R.string.pm_pm_pro_access_benefit)
            }
        )
    }
    private fun getPotentialVisitorBenefit(
        context: Context): PMBenefitItemUiModel {
        return PMBenefitItemUiModel(
            iconUrl = PMConstant.Images.PM_SEARCH_DISCOVERY_ICON,
            benefitDescription = context.getString(R.string.pm_pm_visitor_benefit)
        )
    }
}
