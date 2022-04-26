package com.tokopedia.power_merchant.subscribe.view.helper

import android.content.Context
import com.tokopedia.gm.common.constant.PMConstant
import com.tokopedia.gm.common.data.source.local.model.PMBenefitItemUiModel
import com.tokopedia.gm.common.data.source.local.model.PMGradeWithBenefitsUiModel
import com.tokopedia.gm.common.data.source.local.model.PMShopInfoUiModel
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
                getPMProAdvanceBenefits(context, shopInfo),
                getPMProExpertBenefits(context, shopInfo),
                getPMProUltimateBenefits(context, shopInfo)
            )
        }
    }

    private fun getPMProUltimateBenefits(
        context: Context,
        shopInfo: PMShopInfoUiModel
    ): PMGradeWithBenefitsUiModel {
        return PMGradeWithBenefitsUiModel(
            gradeName = Constant.PM_PRO_ULTIMATE,
            isActive = shopInfo.shopLevel == PMConstant.ShopLevel.FOUR,
            benefitList = listOf(
                getTopAdsBenefit(
                    context,
                    Constant.PM_PRO_ULT_TOP_ADS_CREDIT,
                    Constant.PM_PRO_ULT_BROAD_CAST_CHAT
                ),
                getSpecialReleaseBenefit(context, Constant.PM_PRO_ULT_SPECIAL_RELEASE),
                getProductBundlingBenefit(context, Constant.PM_PRO_ULT_PRODUCT_BUNDLING),
                getFreeDeliveryBenefit(context),
                getFlashSaleBenefit(context),
                getCashBackBenefit(context)
            )
        )
    }

    private fun getPMProExpertBenefits(
        context: Context,
        shopInfo: PMShopInfoUiModel
    ): PMGradeWithBenefitsUiModel {
        return PMGradeWithBenefitsUiModel(
            gradeName = Constant.PM_PRO_EXPERT,
            isActive = shopInfo.shopLevel == PMConstant.ShopLevel.THREE,
            benefitList = listOf(
                getTopAdsBenefit(
                    context,
                    Constant.PM_PRO_EXP_TOP_ADS_CREDIT,
                    Constant.PM_PRO_EXP_BROAD_CAST_CHAT
                ),
                getSpecialReleaseBenefit(context, Constant.PM_PRO_EXP_SPECIAL_RELEASE),
                getProductBundlingBenefit(context, Constant.PM_PRO_EXP_PRODUCT_BUNDLING),
                getFreeDeliveryBenefit(context),
                getFlashSaleBenefit(context),
                getCashBackBenefit(context)
            )
        )
    }

    private fun getPMProAdvanceBenefits(
        context: Context,
        shopInfo: PMShopInfoUiModel
    ): PMGradeWithBenefitsUiModel {
        return PMGradeWithBenefitsUiModel(
            gradeName = Constant.PM_PRO_ADVANCED,
            isActive = shopInfo.shopLevel == PMConstant.ShopLevel.TWO,
            benefitList = listOf(
                getTopAdsBenefit(
                    context,
                    Constant.PM_PRO_ADV_TOP_ADS_CREDIT,
                    Constant.PM_PRO_ADV_BROAD_CAST_CHAT
                ),
                getSpecialReleaseBenefit(context, Constant.PM_PRO_ADV_SPECIAL_RELEASE),
                getProductBundlingBenefit(context, Constant.PM_PRO_ADV_PRODUCT_BUNDLING),
                getFreeDeliveryBenefit(context),
                getFlashSaleBenefit(context),
                getCashBackBenefit(context)
            )
        )
    }

    private fun getPMBenefits(
        context: Context,
        shopInfo: PMShopInfoUiModel
    ): PMGradeWithBenefitsUiModel {
        return PMGradeWithBenefitsUiModel(
            gradeName = Constant.POWER_MERCHANT,
            isActive = shopInfo.shopLevel == PMConstant.ShopLevel.ONE,
            benefitList = listOf(
                getTopAdsBenefit(context, Constant.PM_TOP_ADS_CREDIT, Constant.PM_BROAD_CAST_CHAT),
                getSpecialReleaseBenefit(context, Constant.PM_SPECIAL_RELEASE),
                getProductBundlingBenefit(context, Constant.PM_PRODUCT_BUNDLING),
                PMBenefitItemUiModel(
                    resIcon = R.drawable.ic_pm_product_bundling,
                    benefitDescription = context.getString(
                        R.string.pm_benefit_free_delivery_pm,
                        Constant.PM_FREE_DELIVERY
                    )
                )
            )
        )
    }

    private fun getCashBackBenefit(context: Context): PMBenefitItemUiModel {
        return PMBenefitItemUiModel(
            resIcon = R.drawable.ic_pm_cash_back,
            benefitDescription = context.getString(R.string.pm_benefit_cahs_back)
        )
    }

    private fun getFlashSaleBenefit(context: Context): PMBenefitItemUiModel {
        return PMBenefitItemUiModel(
            resIcon = R.drawable.ic_pm_flash_sale,
            benefitDescription = context.getString(R.string.pm_benefit_flash_sale)
        )
    }

    private fun getFreeDeliveryBenefit(context: Context): PMBenefitItemUiModel {
        return PMBenefitItemUiModel(
            resIcon = R.drawable.ic_pm_product_bundling,
            benefitDescription = context.getString(
                R.string.pm_benefit_free_delivery_pm_pro,
                Constant.PM_PRO_FREE_DELIVERY
            )
        )
    }

    private fun getProductBundlingBenefit(context: Context, quota: Int): PMBenefitItemUiModel {
        return PMBenefitItemUiModel(
            resIcon = R.drawable.ic_pm_product_bundling,
            benefitDescription = context.getString(R.string.pm_benefit_product_bundling, quota)
        )
    }

    private fun getSpecialReleaseBenefit(context: Context, quota: Int): PMBenefitItemUiModel {
        return PMBenefitItemUiModel(
            resIcon = R.drawable.ic_pm_special_release,
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
            resIcon = R.drawable.ic_pm_topads,
            benefitDescription = context.getString(
                R.string.pm_benefit_top_ads, topAdsCredit, broadcastChat
            )
        )
    }
}