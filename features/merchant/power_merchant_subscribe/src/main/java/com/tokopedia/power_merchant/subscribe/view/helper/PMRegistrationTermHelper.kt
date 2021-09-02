package com.tokopedia.power_merchant.subscribe.view.helper

import android.content.Context
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.gm.common.constant.KYCStatusId
import com.tokopedia.gm.common.constant.PMConstant
import com.tokopedia.gm.common.data.source.local.model.PMShopInfoUiModel
import com.tokopedia.gm.common.utils.PMCommonUtils
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.common.constant.Constant
import com.tokopedia.power_merchant.subscribe.view.model.PMProBenefitUiModel
import com.tokopedia.power_merchant.subscribe.view.model.RegistrationTermUiModel
import com.tokopedia.utils.text.currency.CurrencyFormatHelper

/**
 * Created By @ilhamsuaib on 15/03/21
 */

object PMRegistrationTermHelper {

    fun getPmRegistrationTerms(
        context: Context,
        shopInfo: PMShopInfoUiModel,
        isPmProSelected: Boolean
    ): List<RegistrationTermUiModel> {
        val firstTerm = if (shopInfo.isNewSeller) {
            getActiveProductTerm(context, shopInfo)
        } else {
            getShopScoreTerm(context, shopInfo, isPmProSelected)
        }
        return listOf(firstTerm, getKycTerm(context, shopInfo, isPmProSelected))
    }

    fun getPmProRegistrationTerms(
        context: Context,
        shopInfo: PMShopInfoUiModel,
        isPmProSelected: Boolean
    ): List<RegistrationTermUiModel> {
        return listOf(
            getShopScoreTerm(context, shopInfo, isPmProSelected), getTotalOrderTerm(context, shopInfo),
            getNetItemValueTerm(context, shopInfo), getKycTerm(context, shopInfo, isPmProSelected)
        )
    }

    private fun getTotalOrderTerm(
        context: Context,
        shopInfo: PMShopInfoUiModel
    ): RegistrationTermUiModel.Order {
        val isEligibleOrder = shopInfo.itemSoldOneMonth >= shopInfo.itemSoldPmProThreshold
        val isFirstMondayNewSeller = shopInfo.is30DaysFirstMonday
        val isNewSeller = shopInfo.isNewSeller

        val resDrawableIcon: Int = getResDrawableIcon(shopInfo, isEligibleOrder)

        val title: String
        val description: String
        var isChecked = false

        if (isNewSeller) {
            if (!isFirstMondayNewSeller) {
                title =
                    context.getString(R.string.pm_title_min_order_finished_term_new_seller_before_30_days)
                description = context.getString(
                    R.string.pm_desc_min_order_finished_term_new_seller_before_30_days,
                    shopInfo.itemSoldOneMonth.toString()
                )
            } else {
                val getItemOrderTerm = getItemOrderTerm(shopInfo, isEligibleOrder, context)
                title = getItemOrderTerm.first
                description = getItemOrderTerm.second
                isChecked = getItemOrderTerm.third
            }
        } else {
            val getItemOrderTerm = getItemOrderTerm(shopInfo, isEligibleOrder, context)
            title = getItemOrderTerm.first
            description = getItemOrderTerm.second
            isChecked = getItemOrderTerm.third
        }
        return RegistrationTermUiModel.Order(
            title = title,
            descriptionHtml = description,
            resDrawableIcon = resDrawableIcon,
            clickableText = null,
            appLinkOrUrl = null,
            isChecked = isChecked,
            isFirstMondayNewSeller = isFirstMondayNewSeller
        )
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
        context: Context,
        shopInfo: PMShopInfoUiModel
    ): RegistrationTermUiModel.NetItemValue {
        val isEligible = shopInfo.netItemValueOneMonth >= shopInfo.netItemValuePmProThreshold

        val resDrawableIcon: Int = getResDrawableIcon(shopInfo, isEligible)

        val title: String
        val description: String
        var isChecked = false
        val netItemValueFmt =
            CurrencyFormatHelper.convertToRupiah(shopInfo.netItemValueOneMonth.toString())

        if (shopInfo.isNewSeller) {
            if (!shopInfo.is30DaysFirstMonday) {
                title =
                    context.getString(R.string.pm_title_min_product_sold_term_new_seller_before_30_days)
                description = context.getString(
                    R.string.pm_desc_min_product_sold_term_new_seller_before_30_days,
                    netItemValueFmt
                )
            } else {
                val netItemValueTerm = getNetItemValueTerm(shopInfo, isEligible, context)
                title = netItemValueTerm.first
                description = netItemValueTerm.second
                isChecked = netItemValueTerm.third
            }
        } else {
            val netItemValueTerm = getNetItemValueTerm(shopInfo, isEligible, context)
            title = netItemValueTerm.first
            description = netItemValueTerm.second
            isChecked = netItemValueTerm.third
        }

        return RegistrationTermUiModel.NetItemValue(
            title = title,
            descriptionHtml = description,
            resDrawableIcon = resDrawableIcon,
            clickableText = null,
            appLinkOrUrl = null,
            isChecked = isChecked,
            isFirstMondayNewSeller = shopInfo.is30DaysFirstMonday
        )
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

    private fun getShopScoreTerm(
        context: Context,
        shopInfo: PMShopInfoUiModel,
        isPmPro: Boolean
    ): RegistrationTermUiModel.ShopScore {
        val isNewSeller = shopInfo.isNewSeller
        val isFirstMondayNewSeller = shopInfo.is30DaysFirstMonday
        val isEligibleShopScore =
            (isPmPro && shopInfo.isEligibleShopScorePmPro()) || (!isPmPro && shopInfo.isEligibleShopScore()) && !shopInfo.isNewSeller
        val shopScoreResIcon: Int = getResDrawableIcon(shopInfo, isEligibleShopScore)

        val title: String
        val description: String
        var ctaText: String? = null
        var ctaAppLink: String? = null
        val shopScoreFmt = PMCommonUtils.getShopScoreFmt(shopInfo.shopScore)

        val shopScoreThreshold = if (isPmPro) {
            shopInfo.shopScorePmProThreshold
        } else {
            shopInfo.shopScoreThreshold
        }

        if (isNewSeller) {
            if (!isFirstMondayNewSeller) {
                title =
                    context.getString(R.string.pm_title_shop_score_term_new_seller_before_30_days)
                description =
                    context.getString(
                        R.string.pm_desc_shop_score_term_new_seller_before_30_days,
                        shopScoreFmt
                    )
            } else {
                title = context.getString(R.string.pm_shop_score_not_eligible_new_seller)
                description = context.getString(R.string.pm_new_seller_shop_score_description)
            }
            ctaText = context.getString(R.string.pm_learn_shop_performance)
            ctaAppLink = ApplinkConst.SHOP_SCORE_DETAIL
        } else {
            if (isEligibleShopScore) {
                val textColor = PMCommonUtils.getHexColor(
                    context,
                    com.tokopedia.unifyprinciples.R.color.Unify_G500
                )
                title = context.getString(R.string.pm_term_shop_score, textColor, shopScoreFmt)
                description = context.getString(
                    R.string.pm_shop_score_eligible_description,
                    shopScoreThreshold
                )
            } else {
                val textColor = PMCommonUtils.getHexColor(
                    context,
                    com.tokopedia.unifyprinciples.R.color.Unify_R600
                )
                title = context.getString(R.string.pm_term_shop_score, textColor, shopScoreFmt)
                description = context.getString(
                    R.string.pm_shop_score_not_eligible_description,
                    shopScoreThreshold
                )
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
            isChecked = isEligibleShopScore,
            isFirstMondayNewSeller = isFirstMondayNewSeller
        )
    }


    private fun getTermIcon(isEligible: Boolean): Int {
        return if (isEligible) {
            R.drawable.ic_pm_checked
        } else {
            R.drawable.ic_pm_not_checked
        }
    }

    private fun getResDrawableIcon(shopInfo: PMShopInfoUiModel, isEligible: Boolean): Int {
        val isNewSeller = shopInfo.isNewSeller
        val isFirstMondayNewSeller = shopInfo.is30DaysFirstMonday
        return if (isNewSeller) {
            if (!isFirstMondayNewSeller) {
                R.drawable.ic_not_completed_new_seller
            } else {
                getTermIcon(isEligible)
            }
        } else {
            getTermIcon(isEligible)
        }
    }

    private fun getActiveProductTerm(
        context: Context,
        shopInfo: PMShopInfoUiModel
    ): RegistrationTermUiModel.ActiveProduct {
        val shopScoreResIcon: Int = if (shopInfo.hasActiveProduct) {
            R.drawable.ic_pm_checked
        } else {
            R.drawable.ic_pm_not_checked
        }

        val title: String
        val description: String
        var ctaText: String? = null
        var ctaAppLink: String? = null

        if (shopInfo.hasActiveProduct) {
            title = context.getString(R.string.pm_already_have_one_active_product)
            description = context.getString(R.string.pm_label_already_have_one_active_product)
        } else {
            title = context.getString(R.string.pm_have_not_one_active_product_yet)
            description = if (shopInfo.isNewSeller) {
                context.getString(R.string.pm_label_have_not_one_active_product_yet_new_seller)
            } else {
                context.getString(R.string.pm_label_have_not_one_active_product_yet)
            }
            ctaText = context.getString(R.string.pm_add_product)
            ctaAppLink = ApplinkConst.SellerApp.PRODUCT_ADD
        }

        return RegistrationTermUiModel.ActiveProduct(
            title = title,
            descriptionHtml = description,
            resDrawableIcon = shopScoreResIcon,
            clickableText = ctaText,
            appLinkOrUrl = ctaAppLink,
            isChecked = shopInfo.hasActiveProduct
        )
    }

    private fun getKycTerm(
        context: Context,
        shopInfo: PMShopInfoUiModel,
        isPmPro: Boolean
    ): RegistrationTermUiModel {
        val isEligibleShopScore = (!isPmPro && !shopInfo.isEligibleShopScore()) ||
                (isPmPro && !shopInfo.isEligibleShopScorePmPro())

        val kycAppLink = if (isPmPro) {
            PMConstant.AppLink.KYC_POWER_MERCHANT_PRO
        } else {
            PMConstant.AppLink.KYC_POWER_MERCHANT
        }

        var shopKycResIcon: Int
        var title: String
        var description: String
        var ctaText: String? = null
        var ctaAppLink: String? = null
        val isKycVerified =
            shopInfo.isKyc || shopInfo.kycStatusId == KYCStatusId.VERIFIED || shopInfo.kycStatusId == KYCStatusId.APPROVED
        val isKycNotVerified =
            shopInfo.kycStatusId == KYCStatusId.NOT_VERIFIED || shopInfo.kycStatusId == KYCStatusId.BLACKLIST
        when {
            isKycVerified -> {
                title = context.getString(R.string.pm_kyc_verified)
                description = context.getString(R.string.pm_description_kyc_verified)
                shopKycResIcon = R.drawable.ic_pm_checked
            }
            isKycNotVerified -> {
                title = context.getString(R.string.pm_kyc_not_verified)
                when {
                    !shopInfo.isNewSeller && isEligibleShopScore -> {
                        description =
                            context.getString(R.string.pm_description_kyc_not_verified_existing_seller)
                        ctaText = context.getString(R.string.pm_verify_data_clickable)
                        ctaAppLink = kycAppLink
                    }
                    shopInfo.isNewSeller && !shopInfo.hasActiveProduct -> {
                        description =
                            context.getString(R.string.pm_description_kyc_not_verified_new_seller)
                        ctaText = context.getString(R.string.pm_verify_data_clickable)
                        ctaAppLink = kycAppLink
                    }
                    else -> {
                        description =
                            context.getString(R.string.pm_description_kyc_not_verified_directly_pm)
                        ctaText = null
                        ctaAppLink = null
                    }
                }
                shopKycResIcon = R.drawable.ic_pm_not_checked
            }
            shopInfo.kycStatusId == KYCStatusId.PENDING -> {
                title = context.getString(R.string.pm_kyc_verification_waiting)
                description =
                    context.getString(R.string.pm_description_kyc_verification_waiting)
                shopKycResIcon = R.drawable.ic_pm_waiting
            }
            else -> {
                title = context.getString(R.string.pm_verification_failed)
                if ((shopInfo.isNewSeller && !shopInfo.hasActiveProduct) || !isEligibleShopScore) {
                    description =
                        context.getString(R.string.pm_description_kyc_verification_failed)
                    ctaText = context.getString(R.string.pm_verify_again_clickable)
                    ctaAppLink = kycAppLink
                } else {
                    description =
                        context.getString(R.string.pm_description_kyc_verification_failed_directly_pm)
                }
                shopKycResIcon = R.drawable.ic_pm_failed
            }
        }
        if (shopInfo.isNewSeller && isPmPro) {
            if (!shopInfo.is30DaysFirstMonday) {
                description = title
                title = context.getString(R.string.pm_kyc_verify_ktp)
                shopKycResIcon = R.drawable.ic_not_completed_new_seller
            }
        }
        return RegistrationTermUiModel.Kyc(
            title = title,
            descriptionHtml = description,
            resDrawableIcon = shopKycResIcon,
            clickableText = ctaText,
            appLinkOrUrl = ctaAppLink,
            isChecked = isKycVerified,
            isFirstMondayNewSeller = shopInfo.is30DaysFirstMonday
        )
    }

    fun getBenefitList(context: Context): List<PMProBenefitUiModel> {
        return listOf(
            PMProBenefitUiModel(
                description = context.getString(
                    R.string.pm_pro_general_benefit_1,
                    Constant.POWER_MERCHANT_PRO_CHARGING
                ),
                icon = IconUnify.COURIER_FAST
            ),
            PMProBenefitUiModel(
                description = context.getString(R.string.pm_pro_general_benefit_2),
                icon = IconUnify.SEARCH
            ),
            PMProBenefitUiModel(
                description = context.getString(R.string.pm_pro_general_benefit_3),
                icon = IconUnify.COURIER
            ),
            PMProBenefitUiModel(
                description = context.getString(R.string.pm_pro_general_benefit_4),
                imgUrl = PMConstant.Images.PM_PRO_BADGE
            ),
            PMProBenefitUiModel(
                description = context.getString(R.string.pm_pro_general_benefit_5),
                icon = IconUnify.CALL_CENTER
            ),
            PMProBenefitUiModel(
                description = context.getString(R.string.pm_pro_general_benefit_6),
                icon = IconUnify.STAR_CIRCLE
            )
        )
    }
}