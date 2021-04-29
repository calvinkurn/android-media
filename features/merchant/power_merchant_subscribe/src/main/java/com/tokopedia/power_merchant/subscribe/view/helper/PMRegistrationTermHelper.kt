package com.tokopedia.power_merchant.subscribe.view.helper

import android.content.Context
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.gm.common.constant.KYCStatusId
import com.tokopedia.gm.common.data.source.local.model.PMShopInfoUiModel
import com.tokopedia.gm.common.utils.PMCommonUtils
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.view.model.RegistrationTermUiModel
import com.tokopedia.utils.text.currency.CurrencyFormatHelper

/**
 * Created By @ilhamsuaib on 15/03/21
 */

object PMRegistrationTermHelper {

    fun getPmRegistrationTerms(context: Context, shopInfo: PMShopInfoUiModel): List<RegistrationTermUiModel> {
        return listOf(getShopScoreOrProductTerm(context, shopInfo, false), getKycTerm(context, shopInfo))
    }

    fun getPmProRegistrationTerms(context: Context, shopInfo: PMShopInfoUiModel): List<RegistrationTermUiModel> {
        return listOf(getShopScoreOrProductTerm(context, shopInfo, true), getTotalOrderTerm(context, shopInfo), getNivTerm(context, shopInfo), getKycTerm(context, shopInfo))
    }

    private fun getTotalOrderTerm(context: Context, shopInfo: PMShopInfoUiModel): RegistrationTermUiModel.Order {
        val isEligibleOrder = shopInfo.itemSoldOneMonth >= shopInfo.itemSoldPmProThreshold

        val resDrawableIcon = if (isEligibleOrder) {
            R.drawable.ic_pm_checked
        } else {
            R.drawable.ic_pm_not_checked
        }
        val title: String
        val description: String
        var isChecked = false

        if (isEligibleOrder) {
            val textColor = PMCommonUtils.getHexColor(context, com.tokopedia.unifyprinciples.R.color.Unify_G500)
            title = context.getString(R.string.pm_number_of_order, textColor, shopInfo.itemSoldOneMonth)
            description = context.getString(R.string.pm_number_of_minimum_order, shopInfo.itemSoldPmProThreshold)
            isChecked = true
        } else {
            val textColor = PMCommonUtils.getHexColor(context, com.tokopedia.unifyprinciples.R.color.Unify_R600)
            title = context.getString(R.string.pm_number_of_order, textColor, shopInfo.itemSoldOneMonth)
            description = context.getString(R.string.pm_number_of_order_threshold, shopInfo.itemSoldPmProThreshold)
        }
        return RegistrationTermUiModel.Order(
                title = title,
                descriptionHtml = description,
                resDrawableIcon = resDrawableIcon,
                clickableText = null,
                appLinkOrUrl = null,
                isChecked = isChecked
        )
    }

    private fun getNivTerm(context: Context, shopInfo: PMShopInfoUiModel): RegistrationTermUiModel.Niv {
        val isEligible = shopInfo.nivOneMonth >= shopInfo.nivPmProThreshold

        val resDrawableIcon = if (isEligible) {
            R.drawable.ic_pm_checked
        } else {
            R.drawable.ic_pm_not_checked
        }

        val title: String
        var isChecked = false
        val nivFmt = CurrencyFormatHelper.convertToRupiah(shopInfo.nivOneMonth.toString())
        val nivThresholdFmt = CurrencyFormatHelper.convertToRupiah(shopInfo.nivPmProThreshold.toString())

        if (isEligible) {
            val textColor = PMCommonUtils.getHexColor(context, com.tokopedia.unifyprinciples.R.color.Unify_G500)
            title = context.getString(R.string.pm_niv_term, textColor, nivFmt)
            isChecked = true
        } else {
            val textColor = PMCommonUtils.getHexColor(context, com.tokopedia.unifyprinciples.R.color.Unify_R600)
            title = context.getString(R.string.pm_niv_term, textColor, nivFmt)
        }

        val description = context.getString(R.string.pm_niv_threshold_term, nivThresholdFmt)

        return RegistrationTermUiModel.Niv(
                title = title,
                descriptionHtml = description,
                resDrawableIcon = resDrawableIcon,
                clickableText = null,
                appLinkOrUrl = null,
                isChecked = isChecked
        )
    }

    private fun getShopScoreOrProductTerm(context: Context, shopInfo: PMShopInfoUiModel, isPmPro: Boolean): RegistrationTermUiModel.ShopScore {
        val shopScoreResIcon: Int = if ((shopInfo.isNewSeller && shopInfo.hasActiveProduct) || (!shopInfo.isNewSeller && shopInfo.isEligibleShopScore)) {
            R.drawable.ic_pm_checked
        } else {
            R.drawable.ic_pm_not_checked
        }

        val title: String
        val description: String
        var ctaText: String? = null
        var ctaAppLink: String? = null
        var isChecked = false
        val shopScoreThreshold = if (isPmPro) {
            shopInfo.shopScorePmProThreshold
        } else {
            shopInfo.shopScoreThreshold
        }

        if (shopInfo.isNewSeller) { //new seller
            if (shopInfo.hasActiveProduct) {
                title = context.getString(R.string.pm_already_have_one_active_product)
                description = context.getString(R.string.pm_label_already_have_one_active_product)
                isChecked = true
            } else {
                title = context.getString(R.string.pm_have_not_one_active_product_yet)
                description = context.getString(R.string.pm_label_have_not_one_active_product_yet)
                ctaText = context.getString(R.string.pm_add_product)
                ctaAppLink = ApplinkConst.SellerApp.PRODUCT_ADD
            }
        } else { //existing seller
            if (shopInfo.isEligibleShopScore) {
                val textColor = PMCommonUtils.getHexColor(context, com.tokopedia.unifyprinciples.R.color.Unify_G500)
                title = context.getString(R.string.pm_shop_score_eligible, textColor, shopInfo.shopScore)
                description = context.getString(R.string.pm_shop_score_eligible_description, shopScoreThreshold)
                isChecked = true
            } else {
                val textColor = PMCommonUtils.getHexColor(context, com.tokopedia.unifyprinciples.R.color.Unify_R600)
                title = context.getString(R.string.pm_shop_score_not_eligible, textColor, shopInfo.shopScore)
                description = context.getString(R.string.pm_shop_score_not_eligible_description, shopScoreThreshold)
                ctaText = context.getString(R.string.pm_learn_shop_performance)
                ctaAppLink = ApplinkConst.SHOP_SCORE_DETAIL
            }
        }
        return RegistrationTermUiModel.ShopScore(
                title = title,
                descriptionHtml = description,
                resDrawableIcon = shopScoreResIcon,
                clickableText = ctaText,
                appLinkOrUrl = ctaAppLink,
                isChecked = isChecked
        )
    }

    private fun getKycTerm(context: Context, shopInfo: PMShopInfoUiModel): RegistrationTermUiModel {
        val shopKycResIcon: Int
        val title: String
        val description: String
        var ctaText: String? = null
        var ctaAppLink: String? = null
        when (shopInfo.kycStatusId) {
            KYCStatusId.VERIFIED, KYCStatusId.APPROVED -> {
                title = context.getString(R.string.pm_kyc_verified)
                description = context.getString(R.string.pm_description_kyc_verified)
                shopKycResIcon = R.drawable.ic_pm_checked
            }
            KYCStatusId.NOT_VERIFIED, KYCStatusId.BLACKLIST -> {
                title = context.getString(R.string.pm_kyc_not_verified)
                when {
                    !shopInfo.isNewSeller && !shopInfo.isEligibleShopScore -> {
                        description = context.getString(R.string.pm_description_kyc_not_verified_existing_seller)
                        ctaText = context.getString(R.string.pm_verify_data_clickable)
                        ctaAppLink = ApplinkConst.KYC_SELLER_DASHBOARD
                    }
                    shopInfo.isNewSeller && !shopInfo.hasActiveProduct -> {
                        description = context.getString(R.string.pm_description_kyc_not_verified_new_seller)
                        ctaText = context.getString(R.string.pm_verify_data_clickable)
                        ctaAppLink = ApplinkConst.KYC_SELLER_DASHBOARD
                    }
                    else -> {
                        description = context.getString(R.string.pm_description_kyc_not_verified_directly_pm)
                        ctaText = null
                        ctaAppLink = null
                    }
                }
                shopKycResIcon = R.drawable.ic_pm_not_checked
            }
            KYCStatusId.PENDING -> {
                title = context.getString(R.string.pm_kyc_verification_waiting)
                description = context.getString(R.string.pm_description_kyc_verification_waiting)
                shopKycResIcon = R.drawable.ic_pm_waiting
            }
            else -> {
                title = context.getString(R.string.pm_verification_failed)
                if ((shopInfo.isNewSeller && !shopInfo.hasActiveProduct) || (!shopInfo.isNewSeller && !shopInfo.isEligibleShopScore)) {
                    description = context.getString(R.string.pm_description_kyc_verification_failed)
                    ctaText = context.getString(R.string.pm_verify_again_clickable)
                    ctaAppLink = ApplinkConst.KYC_SELLER_DASHBOARD
                } else {
                    description = context.getString(R.string.pm_description_kyc_verification_failed_directly_pm)
                }
                shopKycResIcon = R.drawable.ic_pm_failed
            }
        }
        return RegistrationTermUiModel.Kyc(
                title = title,
                descriptionHtml = description,
                resDrawableIcon = shopKycResIcon,
                clickableText = ctaText,
                appLinkOrUrl = ctaAppLink,
                isChecked = shopInfo.isKyc
        )
    }
}