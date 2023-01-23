package com.tokopedia.mvc.presentation.share

import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.campaign.utils.constant.DateConstant
import com.tokopedia.kotlin.extensions.view.formatTo
import com.tokopedia.mvc.domain.entity.enums.BenefitType
import com.tokopedia.mvc.domain.entity.enums.PromoType
import com.tokopedia.mvc.domain.entity.enums.VoucherTargetBuyer
import com.tokopedia.mvc.util.constant.ShareComponentConstant
import com.tokopedia.universal_sharing.constants.ImageGeneratorConstants
import com.tokopedia.universal_sharing.view.bottomsheet.UniversalShareBottomSheet
import com.tokopedia.universal_sharing.view.bottomsheet.listener.ShareBottomsheetListener
import com.tokopedia.universal_sharing.view.model.ShareModel
import com.tokopedia.user.session.UserSessionInterface
import java.util.*
import javax.inject.Inject

/**
 * Docs are defined on: https://docs.google.com/spreadsheets/d/10Kee8re2G87hS5elK4XASlHYaav8nsvjjiU9L5qbGKQ/edit#gid=0
 */
class ShareComponentInstanceBuilder @Inject constructor(
    private val userSession: UserSessionInterface
) {

    companion object {
        private const val FIRST_IMAGE_URL = 0
        private const val SECOND_IMAGE_URL = 1
        private const val THIRD_IMAGE_URL = 2
        private const val THOUSAND = 1_000f
        private const val MILLION = 1_000_000f
        private const val PRODUCT_COUNT_2 = 2
        private const val PRODUCT_COUNT_3 = 3
    }


    data class Param(
        val isVoucherProduct: Boolean,
        val voucherStartDate: Date,
        val voucherEndDate: Date,
        val voucherId: Long,
        val isPublic: Boolean,
        val voucherCode: String,
        val promoType: PromoType,
        val benefitType: BenefitType,
        val shopLogo: String,
        val shopName: String,
        val shopDomain: String,
        val discountAmount: Long,
        val discountAmountMax: Long,
        val productImageUrls: List<String>,
        val discountPercentage: Int,
        val targetBuyer: VoucherTargetBuyer
    )
    
    fun build(
        param: Param,
        title: String,
        onShareOptionsClicked: (ShareModel) -> Unit,
        onBottomSheetClosed: () -> Unit
    ): UniversalShareBottomSheet {
        val formattedShopName = MethodChecker.fromHtml(param.shopName).toString()
        return UniversalShareBottomSheet.createInstance().apply {
            val listener = object : ShareBottomsheetListener {
                override fun onShareOptionClicked(shareModel: ShareModel) {
                    onShareOptionsClicked(shareModel)
                }

                override fun onCloseOptionClicked() {
                    onBottomSheetClosed()
                }
            }

            init(listener)
            getImageFromMedia(getImageFromMediaFlag = true)
            setMediaPageSourceId(pageSourceId = ImageGeneratorConstants.ImageGeneratorSourceId.MVC_PRODUCT)

            val iconImageUrl = if (param.isVoucherProduct) {
                ShareComponentConstant.IMAGE_URL_PRODUCT_VOUCHER_ICON
            } else {
                ShareComponentConstant.IMAGE_URL_SHOP_VOUCHER_ICON
            }
            setMetaData(tnTitle = title, tnImage = iconImageUrl)

            setUtmCampaignData(
                pageName = ShareComponentConstant.PAGE_NAME,
                userId = userSession.userId,
                pageIdConstituents = listOf(userSession.shopId, param.voucherId.toString()),
                feature = ShareComponentConstant.FEATURE_NAME
            )

            val cashbackType = when {
                param.promoType == PromoType.FREE_SHIPPING -> ImageGeneratorConstants.CashbackType.NOMINAL
                param.promoType == PromoType.CASHBACK && param.benefitType == BenefitType.NOMINAL -> ImageGeneratorConstants.CashbackType.NOMINAL
                param.promoType == PromoType.CASHBACK && param.benefitType == BenefitType.PERCENTAGE -> ImageGeneratorConstants.CashbackType.PERCENTAGE
                else -> ImageGeneratorConstants.CashbackType.NOMINAL
            }

            val amount = when {
                param.promoType == PromoType.FREE_SHIPPING -> param.discountAmount
                param.promoType == PromoType.CASHBACK && param.benefitType == BenefitType.NOMINAL -> param.discountAmount
                param.promoType == PromoType.CASHBACK && param.benefitType == BenefitType.PERCENTAGE -> param.discountAmountMax
                else -> param.discountAmount
            }

            val formattedDiscountAmount : Int = when {
                amount < THOUSAND -> amount.toInt()
                amount >= MILLION -> (amount / MILLION).toInt()
                amount >= THOUSAND -> (amount / THOUSAND).toInt()
                else -> amount.toInt()
            }

            val symbol = when {
                amount < THOUSAND -> ImageGeneratorConstants.VoucherNominalSymbol.RB
                amount >= MILLION -> ImageGeneratorConstants.VoucherNominalSymbol.JT
                amount >= THOUSAND -> ImageGeneratorConstants.VoucherNominalSymbol.RB
                else -> ImageGeneratorConstants.VoucherNominalSymbol.RB
            }

            val isPublic = if (param.isPublic) {
                ImageGeneratorConstants.VoucherVisibility.PUBLIC
            } else {
                ImageGeneratorConstants.VoucherVisibility.PRIVATE
            }

            addImageGeneratorData(
                key = ImageGeneratorConstants.ImageGeneratorKeys.IS_PUBLIC,
                value = isPublic
            )

            val formattedBenefitType = when(param.promoType) {
                PromoType.FREE_SHIPPING -> ImageGeneratorConstants.VoucherBenefitType.GRATIS_ONGKIR
                PromoType.CASHBACK -> ImageGeneratorConstants.VoucherBenefitType.CASHBACK
                PromoType.DISCOUNT -> ImageGeneratorConstants.VoucherBenefitType.DISCOUNT
            }

            addImageGeneratorData(
                key = ImageGeneratorConstants.ImageGeneratorKeys.VOUCHER_BENEFIT_TYPE,
                value = formattedBenefitType
            )

            addImageGeneratorData(
                key = ImageGeneratorConstants.ImageGeneratorKeys.VOUCHER_CASHBACK_TYPE,
                value = cashbackType
            )
            addImageGeneratorData(
                key = ImageGeneratorConstants.ImageGeneratorKeys.VOUCHER_CASHBACK_PERCENTAGE,
                value = param.discountAmount.toString()
            )
            addImageGeneratorData(
                key = ImageGeneratorConstants.ImageGeneratorKeys.VOUCHER_NOMINAL_AMOUNT,
                value = formattedDiscountAmount.toString()
            )
            addImageGeneratorData(
                key = ImageGeneratorConstants.ImageGeneratorKeys.VOUCHER_NOMINAL_SYMBOL,
                value = symbol
            )
            val formattedDiscountType = when(param.benefitType) {
                BenefitType.NOMINAL -> ImageGeneratorConstants.CashbackType.NOMINAL
                BenefitType.PERCENTAGE -> ImageGeneratorConstants.CashbackType.PERCENTAGE
            }
            addImageGeneratorData(
                key = ImageGeneratorConstants.ImageGeneratorKeys.VOUCHER_DISCOUNT_TYPE,
                value = formattedDiscountType
            )
            addImageGeneratorData(
                key = ImageGeneratorConstants.ImageGeneratorKeys.VOUCHER_DISCOUNT_PERCENTAGE,
                value = param.discountPercentage.toString()
            )
            addImageGeneratorData(
                key = ImageGeneratorConstants.ImageGeneratorKeys.SHOP_LOGO_MVC,
                value = param.shopLogo
            )
            addImageGeneratorData(
                key = ImageGeneratorConstants.ImageGeneratorKeys.SHOP_NAME_MVC,
                value = formattedShopName
            )
            addImageGeneratorData(
                key = ImageGeneratorConstants.ImageGeneratorKeys.VOUCHER_CODE,
                value = param.voucherCode
            )

            val startDate = param.voucherStartDate.formatTo(DateConstant.DATE_YEAR_PRECISION)
            val endDate = param.voucherEndDate.formatTo(DateConstant.DATE_YEAR_PRECISION)

            addImageGeneratorData(
                key = ImageGeneratorConstants.ImageGeneratorKeys.VOUCHER_START_TIME,
                value = startDate
            )
            addImageGeneratorData(
                key = ImageGeneratorConstants.ImageGeneratorKeys.VOUCHER_FINISH_TIME,
                value = endDate
            )
            addImageGeneratorData(
                key = ImageGeneratorConstants.ImageGeneratorKeys.PRODUCT_COUNT,
                value = param.productImageUrls.size.toString()
            )

            if (param.productImageUrls.isNotEmpty()) {
                addImageGeneratorData(
                    key = ImageGeneratorConstants.ImageGeneratorKeys.PRODUCT_IMAGE_1,
                    value = param.productImageUrls.getIndexAtOrEmpty(FIRST_IMAGE_URL)
                )
            }
            if (param.productImageUrls.size >= PRODUCT_COUNT_2) {
                addImageGeneratorData(
                    key = ImageGeneratorConstants.ImageGeneratorKeys.PRODUCT_IMAGE_2,
                    value = param.productImageUrls.getIndexAtOrEmpty(SECOND_IMAGE_URL)
                )
            }
            if (param.productImageUrls.size >= PRODUCT_COUNT_3) {
                addImageGeneratorData(
                    key = ImageGeneratorConstants.ImageGeneratorKeys.PRODUCT_IMAGE_3,
                    value = param.productImageUrls.getIndexAtOrEmpty(THIRD_IMAGE_URL)
                )
            }

            val audienceTarget = when (param.targetBuyer) {
                VoucherTargetBuyer.ALL_BUYER -> ImageGeneratorConstants.AUDIENCE_TARGET.ALL_USERS
                VoucherTargetBuyer.NEW_FOLLOWER -> ImageGeneratorConstants.AUDIENCE_TARGET.NEW_FOLLOWER
                VoucherTargetBuyer.NEW_BUYER -> ImageGeneratorConstants.AUDIENCE_TARGET.NEW_USER
                VoucherTargetBuyer.MEMBER -> ImageGeneratorConstants.AUDIENCE_TARGET.MEMBER
            }

            addImageGeneratorData(
                key = ImageGeneratorConstants.ImageGeneratorKeys.AUDIENCE_TARGET,
                value = audienceTarget
            )
        }
    }

    private fun List<String>.getIndexAtOrEmpty(index : Int) : String {
        return try {
            this[index]
        } catch(e: Exception) {
            ""
        }
    }
}
