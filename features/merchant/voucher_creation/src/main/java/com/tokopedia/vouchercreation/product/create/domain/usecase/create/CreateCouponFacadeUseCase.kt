package com.tokopedia.vouchercreation.product.create.domain.usecase.create

import com.tokopedia.vouchercreation.common.consts.GqlQueryConstant
import com.tokopedia.vouchercreation.common.domain.usecase.InitiateVoucherUseCase
import com.tokopedia.vouchercreation.common.extension.parseTo
import com.tokopedia.vouchercreation.common.utils.DateTimeUtils
import com.tokopedia.vouchercreation.product.create.data.request.GenerateImageParams
import com.tokopedia.vouchercreation.product.create.domain.entity.*
import com.tokopedia.vouchercreation.product.create.domain.usecase.GenerateImageUseCase
import com.tokopedia.vouchercreation.shop.create.view.uimodel.initiation.InitiateVoucherUiModel
import com.tokopedia.vouchercreation.shop.voucherlist.domain.model.ShopBasicDataResult
import com.tokopedia.vouchercreation.shop.voucherlist.domain.usecase.ShopBasicDataUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import javax.inject.Inject

class CreateCouponFacadeUseCase @Inject constructor(
    private val createCouponProductUseCase: CreateCouponProductUseCase,
    private val initiateVoucherUseCase: InitiateVoucherUseCase,
    private val getShopBasicDataUseCase: ShopBasicDataUseCase,
    private val generateImageUseCase: GenerateImageUseCase
) {

    companion object {
        private const val IS_UPDATE_MODE = false
        private const val EMPTY_STRING = ""
        private const val THOUSAND = 1_000f
        private const val MILLION = 1_000_000f
    }

    suspend fun execute(
        scope: CoroutineScope,
        sourceId: String,
        couponInformation: CouponInformation,
        couponSettings: CouponSettings,
        couponProducts: List<CouponProduct>,
        firstImageUrl: String,
        secondImageUrl: String,
        thirdImageUrl: String
    ): Int {
        val initiateVoucherDeferred = scope.async { initiateVoucher(IS_UPDATE_MODE) }
        val shopDeferred = scope.async { getShopBasicDataUseCase.executeOnBackground() }

        val shop = shopDeferred.await()

        /*val uploadImageDeferred = scope.async {
          uploadImage(
              sourceId,
              couponInformation,
              couponSettings,
              shop,
              couponProducts.size,
              firstImageUrl,
              secondImageUrl,
              thirdImageUrl
          )
      }

      val imageUrl = uploadImageDeferred.await()
    val voucher = initiateVoucherDeferred.await()

      val createCouponDeferred = scope.async {
          createCoupon(
              couponInformation,
              couponSettings,
              couponProducts,
              voucher.token,
              imageUrl
          )
      }

      val createdCouponId = createCouponDeferred.await()

      return createdCouponId*/
        return -1
    }

    private suspend fun createCoupon(
        couponInformation: CouponInformation,
        couponSettings: CouponSettings,
        couponProducts: List<CouponProduct>,
        token: String,
        imageUrl: String
    ): Int {

        val params = createCouponProductUseCase.createRequestParam(
            couponInformation,
            couponSettings,
            couponProducts,
            token,
            imageUrl
        )
        createCouponProductUseCase.params = params
        return createCouponProductUseCase.executeOnBackground()

    }

    /*private suspend fun uploadImage(
        sourceId: String,
        couponInformation: CouponInformation,
        couponSettings: CouponSettings,
        shop: ShopBasicDataResult,
        productCount : Int,
        firstImageUrl : String,
        secondImageUrl : String,
        thirdImageUrl : String
    ): String {
        val couponVisibility = when (couponInformation.target) {
            CouponInformation.Target.PUBLIC -> "public"
            CouponInformation.Target.SPECIAL -> "private"
        }

        val benefitType = when (couponSettings.type) {
            CouponType.NONE -> EMPTY_STRING
            CouponType.CASHBACK -> "cashback"
            CouponType.FREE_SHIPPING -> "gratis-ongkir"
        }

        val cashbackType = when {
            couponSettings.type == CouponType.FREE_SHIPPING -> "nominal"
            couponSettings.type == CouponType.CASHBACK && couponSettings.discountType == DiscountType.NOMINAL -> "nominal"
            couponSettings.type == CouponType.CASHBACK && couponSettings.discountType == DiscountType.PERCENTAGE -> "percentage"
            else -> EMPTY_STRING
        }

        val symbol = when {
            couponSettings.discountAmount < THOUSAND -> EMPTY_STRING
            couponSettings.discountAmount >= MILLION -> "jt"
            couponSettings.discountAmount >= THOUSAND -> "rb"
            else -> EMPTY_STRING
        }

        val formattedDiscountAmount: Float = when {
            couponSettings.discountAmount < THOUSAND -> couponSettings.discountAmount.toFloat()
            couponSettings.discountAmount >= MILLION -> (couponSettings.discountAmount / MILLION)
            couponSettings.discountAmount >= THOUSAND -> (couponSettings.discountAmount / THOUSAND)
            else -> couponSettings.discountAmount.toFloat()
        }

        val nominalAmount = if (isInteger(formattedDiscountAmount)) {
            formattedDiscountAmount.toInt()
        } else {
            formattedDiscountAmount
        }

        val startTime = couponInformation.period.startDate.parseTo(DateTimeUtils.DATE_FORMAT)
        val endTime = couponInformation.period.endDate.parseTo(DateTimeUtils.DATE_FORMAT)

        val audienceTarget = "all-users"

        val imageParams = arrayListOf(
            GenerateImageParams("platform", "square"),
            GenerateImageParams("is_public", couponVisibility),
            GenerateImageParams("voucher_benefit_type", benefitType),
            GenerateImageParams("voucher_cashback_type", cashbackType),
            GenerateImageParams("voucher_cashback_percentage", couponSettings.discountPercentage),
            GenerateImageParams("voucher_nominal_amount", nominalAmount),
            GenerateImageParams("voucher_nominal_symbol", symbol),
            GenerateImageParams("shop_logo", shop.logo),
            GenerateImageParams("shop_name", shop.shopName),
            GenerateImageParams("voucher_code", couponInformation.code),
            GenerateImageParams("voucher_start_time", startTime),
            GenerateImageParams("voucher_finish_time", endTime),
            GenerateImageParams("product_count", productCount),
            GenerateImageParams("product_image_1", firstImageUrl),
            GenerateImageParams("product_image_2", secondImageUrl),
            GenerateImageParams("product_image_3", thirdImageUrl),
            GenerateImageParams("audience_target", audienceTarget)
        )

        generateImageUseCase.setParams(sourceId, imageParams)

        return generateImageUseCase.executeOnBackground()
    }*/

    private suspend fun initiateVoucher(isUpdateMode: Boolean): InitiateVoucherUiModel {
        initiateVoucherUseCase.query = GqlQueryConstant.GET_INIT_VOUCHER_ELIGIBILITY_QUERY
        initiateVoucherUseCase.params = InitiateVoucherUseCase.createRequestParam(isUpdateMode)
        return initiateVoucherUseCase.executeOnBackground()
    }

    private fun isInteger(number: Float): Boolean {
        return number % 1 == 0.0f
    }
}