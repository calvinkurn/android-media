package com.tokopedia.vouchercreation.product.share

import com.tokopedia.universal_sharing.constants.ImageGeneratorConstants
import com.tokopedia.universal_sharing.view.bottomsheet.UniversalShareBottomSheet
import com.tokopedia.universal_sharing.view.bottomsheet.listener.ShareBottomsheetListener
import com.tokopedia.universal_sharing.view.model.ShareModel
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.date.toDate
import com.tokopedia.vouchercreation.common.consts.ShareComponentConstant
import com.tokopedia.vouchercreation.common.consts.VoucherTypeConst
import com.tokopedia.vouchercreation.common.extension.getIndexAtOrEmpty
import com.tokopedia.vouchercreation.common.extension.parseTo
import com.tokopedia.vouchercreation.common.utils.DateTimeUtils
import com.tokopedia.vouchercreation.product.create.domain.entity.CouponType
import com.tokopedia.vouchercreation.product.create.domain.entity.CouponUiModel
import com.tokopedia.vouchercreation.product.create.domain.entity.DiscountType
import javax.inject.Inject

class SharingComponentInstanceBuilder @Inject constructor(
    private val userSession: UserSessionInterface
) {

    companion object {
        private const val FIRST_IMAGE_URL = 0
        private const val SECOND_IMAGE_URL = 1
        private const val THIRD_IMAGE_URL = 2
        private const val DISCOUNT_TYPE_NOMINAL = "idr"
        private const val THOUSAND = 1_000f
        private const val MILLION = 1_000_000f
        private const val PRODUCT_COUNT_2 = 2
        private const val PRODUCT_COUNT_3 = 3
    }


    fun build(
        coupon : CouponUiModel,
        shopLogo : String,
        shopName : String,
        title: String,
        productImageUrls : List<String>,
        onShareOptionsClicked: (ShareModel) -> Unit,
        onCloseOptionClicked: () -> Unit
    ): UniversalShareBottomSheet {
        return UniversalShareBottomSheet.createInstance().apply {
            val listener = object : ShareBottomsheetListener {
                override fun onShareOptionClicked(shareModel: ShareModel) {
                    onShareOptionsClicked(shareModel)
                }

                override fun onCloseOptionClicked() {
                    onCloseOptionClicked()
                }
            }

            init(listener)
            getImageFromMedia(getImageFromMediaFlag = true)
            setMediaPageSourceId(pageSourceId = ImageGeneratorConstants.ImageGeneratorSourceId.MVC_PRODUCT)
            setMetaData(
                tnTitle = title,
                tnImage = ShareComponentConstant.VOUCHER_PRODUCT_THUMBNAIL_ICON_IMAGE_URL
            )
            setUtmCampaignData(
                pageName = ShareComponentConstant.VOUCHER_PRODUCT_PAGE_NAME,
                userId = userSession.userId,
                pageIdConstituents = listOf(userSession.shopId, coupon.id.toString()),
                feature = ShareComponentConstant.VOUCHER_PRODUCT_FEATURE
            )

            val isPublic = if (coupon.isPublic) {
                ImageGeneratorConstants.VoucherVisibility.PUBLIC
            } else {
                ImageGeneratorConstants.VoucherVisibility.PRIVATE
            }

            val couponType = if (coupon.type == VoucherTypeConst.FREE_ONGKIR) {
                CouponType.FREE_SHIPPING
            } else {
                CouponType.CASHBACK
            }

            val discountType = if (coupon.discountTypeFormatted == DISCOUNT_TYPE_NOMINAL) {
                DiscountType.NOMINAL
            } else {
                DiscountType.PERCENTAGE
            }

            val benefitType = if (coupon.type == VoucherTypeConst.FREE_ONGKIR) {
                ImageGeneratorConstants.VoucherBenefitType.GRATIS_ONGKIR
            } else {
                ImageGeneratorConstants.VoucherBenefitType.CASHBACK
            }

            val cashbackType = when {
                couponType == CouponType.FREE_SHIPPING -> ImageGeneratorConstants.CashbackType.NOMINAL
                couponType == CouponType.CASHBACK && discountType == DiscountType.NOMINAL -> ImageGeneratorConstants.CashbackType.NOMINAL
                couponType == CouponType.CASHBACK && discountType == DiscountType.PERCENTAGE -> ImageGeneratorConstants.CashbackType.PERCENTAGE
                else -> ImageGeneratorConstants.CashbackType.NOMINAL
            }

            val amount = when {
                couponType == CouponType.FREE_SHIPPING -> coupon.discountAmt
                couponType == CouponType.CASHBACK && discountType == DiscountType.NOMINAL -> coupon.discountAmt
                couponType == CouponType.CASHBACK && discountType == DiscountType.PERCENTAGE -> coupon.discountAmtMax
                else -> coupon.discountAmt
            }

            val formattedDiscountAmount : Int = when {
                amount < THOUSAND -> amount
                amount >= MILLION -> (amount / MILLION).toInt()
                amount >= THOUSAND -> (amount / THOUSAND).toInt()
                else -> amount
            }

            val symbol = when {
                amount < THOUSAND -> ImageGeneratorConstants.VoucherNominalSymbol.RB
                amount >= MILLION -> ImageGeneratorConstants.VoucherNominalSymbol.JT
                amount >= THOUSAND -> ImageGeneratorConstants.VoucherNominalSymbol.RB
                else -> ImageGeneratorConstants.VoucherNominalSymbol.RB
            }

            val startTime = coupon.startTime.toDate(DateTimeUtils.TIME_STAMP_FORMAT)
                .parseTo(DateTimeUtils.DATE_FORMAT)
            val endTime = coupon.finishTime.toDate(DateTimeUtils.TIME_STAMP_FORMAT)
                .parseTo(DateTimeUtils.DATE_FORMAT)

            addImageGeneratorData(
                key = ImageGeneratorConstants.ImageGeneratorKeys.IS_PUBLIC,
                value = isPublic
            )
            addImageGeneratorData(
                key = ImageGeneratorConstants.ImageGeneratorKeys.VOUCHER_BENEFIT_TYPE,
                value = benefitType
            )
            addImageGeneratorData(
                key = ImageGeneratorConstants.ImageGeneratorKeys.VOUCHER_CASHBACK_TYPE,
                value = cashbackType
            )
            addImageGeneratorData(
                key = ImageGeneratorConstants.ImageGeneratorKeys.VOUCHER_CASHBACK_PERCENTAGE,
                value = coupon.discountAmt.toString()
            )
            addImageGeneratorData(
                key = ImageGeneratorConstants.ImageGeneratorKeys.VOUCHER_NOMINAL_AMOUNT,
                value = formattedDiscountAmount.toString()
            )
            addImageGeneratorData(
                key = ImageGeneratorConstants.ImageGeneratorKeys.VOUCHER_NOMINAL_SYMBOL,
                value = symbol
            )
            addImageGeneratorData(
                key = ImageGeneratorConstants.ImageGeneratorKeys.SHOP_LOGO_MVC,
                value = shopLogo
            )
            addImageGeneratorData(
                key = ImageGeneratorConstants.ImageGeneratorKeys.SHOP_NAME_MVC,
                value = shopName
            )
            addImageGeneratorData(
                key = ImageGeneratorConstants.ImageGeneratorKeys.VOUCHER_CODE,
                value = coupon.code
            )
            addImageGeneratorData(
                key = ImageGeneratorConstants.ImageGeneratorKeys.VOUCHER_START_TIME,
                value = startTime
            )
            addImageGeneratorData(
                key = ImageGeneratorConstants.ImageGeneratorKeys.VOUCHER_FINISH_TIME,
                value = endTime
            )
            addImageGeneratorData(
                key = ImageGeneratorConstants.ImageGeneratorKeys.PRODUCT_COUNT,
                value = productImageUrls.size.toString()
            )
            if (productImageUrls.isNotEmpty()) {
                addImageGeneratorData(
                    key = ImageGeneratorConstants.ImageGeneratorKeys.PRODUCT_IMAGE_1,
                    value = productImageUrls.getIndexAtOrEmpty(FIRST_IMAGE_URL)
                )
            }
            if (productImageUrls.size >= PRODUCT_COUNT_2) {
                addImageGeneratorData(
                    key = ImageGeneratorConstants.ImageGeneratorKeys.PRODUCT_IMAGE_2,
                    value = productImageUrls.getIndexAtOrEmpty(SECOND_IMAGE_URL)
                )
            }
            if (productImageUrls.size >= PRODUCT_COUNT_3) {
                addImageGeneratorData(
                    key = ImageGeneratorConstants.ImageGeneratorKeys.PRODUCT_IMAGE_3,
                    value = productImageUrls.getIndexAtOrEmpty(THIRD_IMAGE_URL)
                )
            }
            addImageGeneratorData(
                key = ImageGeneratorConstants.ImageGeneratorKeys.AUDIENCE_TARGET,
                value = ImageGeneratorConstants.AUDIENCE_TARGET.ALL_USERS
            )
        }
    }
}