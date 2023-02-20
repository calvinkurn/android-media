package com.tokopedia.power_merchant.subscribe.view.helper

import android.content.Context
import com.tokopedia.gm.common.constant.PMConstant
import com.tokopedia.gm.common.data.source.local.model.PMShopInfoUiModel
import com.tokopedia.gm.common.utils.PMCommonUtils
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.common.constant.Constant
import com.tokopedia.power_merchant.subscribe.view.model.PMProBenefitUiModel
import com.tokopedia.power_merchant.subscribe.view.model.PmActiveTermUiModel
import com.tokopedia.power_merchant.subscribe.view.model.RegistrationTermUiModel
import com.tokopedia.utils.text.currency.CurrencyFormatHelper

object PMActiveTermHelper {

    fun getPmProRegistrationTerms(
        context: Context,
        shopInfo: PMShopInfoUiModel,
        isPmProSelected: Boolean
    ): List<PmActiveTermUiModel> {
        return listOf(
            getShopScoreTerm(context, shopInfo, isPmProSelected),
            getTotalOrderTerm(context, shopInfo),
            getNetItemValueTerm(context, shopInfo),
        )
    }

    private fun getShopScoreTerm(
        context: Context,
        shopInfo: PMShopInfoUiModel,
        isPmProSelected: Boolean
    ): PmActiveTermUiModel.ShopScore {
        val isFirstMondayNewSeller = shopInfo.is30DaysFirstMonday
        val isEligibleShopScore =
            (isPmProSelected && shopInfo.isEligibleShopScorePmPro()) || (!isPmProSelected && shopInfo.isEligibleShopScore())
        val (shopScoreResIcon, isChecked) = getResDrawableIcon(
            shopInfo,
            isEligibleShopScore
        )

        val title: String
        val description: String
        var ctaText: String? = null
        var ctaAppLink: String? = null
        val shopScoreFmt = PMCommonUtils.getShopScoreFmt(shopInfo.shopScore)


        if (isEligibleShopScore) {
            val textColor = PMCommonUtils.getHexColor(
                context,
                com.tokopedia.unifyprinciples.R.color.Unify_G500
            )
            title = context.getString(R.string.pm_term_shop_score, textColor, shopScoreFmt)
            description = context.getString(
                com.tokopedia.power_merchant.subscribe.R.string.pm_active_shop_score_description,
                shopInfo.shopScorePmProThreshold
            )
        } else {
            val textColor = PMCommonUtils.getHexColor(
                context,
                com.tokopedia.unifyprinciples.R.color.Unify_R600
            )
            title = context.getString(R.string.pm_term_shop_score, textColor, shopScoreFmt)
            description = context.getString(
                com.tokopedia.power_merchant.subscribe.R.string.pm_active_shop_score_description,
                shopInfo.shopScorePmProThreshold
            )
            ctaText = String.EMPTY
            ctaAppLink = String.EMPTY
        }

        return PmActiveTermUiModel.ShopScore(
            title = title,
            descriptionHtml = description,
            resDrawableIcon = shopScoreResIcon,
            clickableText = ctaText,
            appLinkOrUrl = ctaAppLink,
            isChecked = isChecked,
        )
    }

    private fun getTotalOrderTerm(
        context: Context,
        shopInfo: PMShopInfoUiModel
    ): PmActiveTermUiModel.Order {
        val isEligibleOrder = shopInfo.itemSoldOneMonth >= shopInfo.itemSoldPmProThreshold
        val isFirstMondayNewSeller = shopInfo.is30DaysFirstMonday

        val (resDrawableIcon, isChecked) = getResDrawableIcon(
            shopInfo,
            isEligibleOrder
        )

        val title: String
        val description: String

        val getItemOrderTerm = getItemOrderTerm(shopInfo, isEligibleOrder, context)
        title = getItemOrderTerm.first
        description = getItemOrderTerm.second

        return PmActiveTermUiModel.Order(
            title = title,
            descriptionHtml = description,
            resDrawableIcon = resDrawableIcon,
            clickableText = null,
            appLinkOrUrl = null,
            isChecked = isChecked
        )
    }

    private fun getNetItemValueTerm(
        context: Context,
        shopInfo: PMShopInfoUiModel
    ): PmActiveTermUiModel.NetItemValue {
        val isEligible = shopInfo.netItemValueOneMonth >= shopInfo.netItemValuePmProThreshold

        val (resDrawableIcon, isChecked) = getResDrawableIcon(
            shopInfo,
            isEligible
        )

        val title: String
        val description: String

        val netItemValueTerm = getNetItemValueTerm(shopInfo, isEligible, context)
        title = netItemValueTerm.first
        description = netItemValueTerm.second

        return PmActiveTermUiModel.NetItemValue(
            title = title,
            descriptionHtml = description,
            resDrawableIcon = resDrawableIcon,
            clickableText = null,
            appLinkOrUrl = null,
            isChecked = isChecked
        )
    }

    private fun getResDrawableIcon(
        shopInfo: PMShopInfoUiModel,
        isEligible: Boolean
    ): Pair<Int, Boolean> {
        val isNewSeller = shopInfo.isNewSeller
        val isFirstMondayNewSeller = shopInfo.is30DaysFirstMonday
        return if (isNewSeller) {
            if (!isFirstMondayNewSeller) {
                Pair(R.drawable.ic_not_completed_new_seller, false)
            } else {
                getTermIcon(isEligible)
            }
        } else {
            getTermIcon(isEligible)
        }
    }

    private fun getTermIcon(isEligible: Boolean): Pair<Int, Boolean> {
        return if (isEligible) {
            Pair(R.drawable.ic_pm_checked, true)
        } else {
            Pair(R.drawable.ic_pm_not_checked, false)
        }
    }

    private fun getItemOrderTerm(
        shopInfo: PMShopInfoUiModel,
        isEligible: Boolean,
        context: Context
    ): Triple<String, String, Boolean> {
        val title: String
        val description: String
        var isChecked = false
        if (isEligible) {
            val textColor =
                PMCommonUtils.getHexColor(
                    context,
                    com.tokopedia.unifyprinciples.R.color.Unify_G500
                )
            title = context.getString(
                R.string.pm_number_of_order,
                textColor,
                shopInfo.itemSoldOneMonth.toString()
            )
            description = context.getString(
                R.string.pm_number_of_minimum_order,
                shopInfo.itemSoldPmProThreshold.toString()
            )
            isChecked = true
        } else {
            val textColor =
                PMCommonUtils.getHexColor(
                    context,
                    com.tokopedia.unifyprinciples.R.color.Unify_R600
                )
            title = context.getString(
                R.string.pm_number_of_order,
                textColor,
                shopInfo.itemSoldOneMonth.toString()
            )
            description = context.getString(
                R.string.pm_number_of_minimum_order,
                shopInfo.itemSoldPmProThreshold.toString()
            )
        }
        return Triple(title, description, isChecked)
    }

    private fun getNetItemValueTerm(
        shopInfo: PMShopInfoUiModel,
        isEligible: Boolean,
        context: Context
    ): Triple<String, String, Boolean> {
        val title: String
        val description: String
        var isChecked = false
        val netItemValueFmt =
            CurrencyFormatHelper.convertToRupiah(shopInfo.netItemValueOneMonth.toString())
        val netItemValueThresholdFmt =
            CurrencyFormatHelper.convertToRupiah(shopInfo.netItemValuePmProThreshold.toString())
        if (isEligible) {
            val textColor =
                PMCommonUtils.getHexColor(
                    context,
                    com.tokopedia.unifyprinciples.R.color.Unify_G500
                )
            title = context.getString(R.string.pm_niv_term, textColor, netItemValueFmt)
            description =
                context.getString(R.string.pm_niv_threshold_term, netItemValueThresholdFmt)
            isChecked = true
        } else {
            val textColor =
                PMCommonUtils.getHexColor(
                    context,
                    com.tokopedia.unifyprinciples.R.color.Unify_R600
                )
            description =
                context.getString(R.string.pm_niv_threshold_term, netItemValueThresholdFmt)
            title = context.getString(R.string.pm_niv_term, textColor, netItemValueFmt)
        }
        return Triple(title, description, isChecked)
    }

    fun getBenefitList(context: Context): List<PMProBenefitUiModel> {
        return listOf(
            PMProBenefitUiModel(
                description = context.getString(R.string.pm_pro_general_benefit_1),
                imgUrl = PMConstant.Images.PM_PRO_BADGE
            ),
            PMProBenefitUiModel(
                description = context.getString(
                    R.string.pm_pro_general_benefit_2
                ),
                icon = IconUnify.PROMO_ADS
            ),
            PMProBenefitUiModel(
                description = context.getString(R.string.pm_pro_general_benefit_3),
                icon = IconUnify.PROMO
            )
        )
    }
}
