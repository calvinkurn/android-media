package com.tokopedia.mvc.domain.usecase

import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.mvc.data.request.GenerateImageParams
import com.tokopedia.mvc.domain.entity.ProductResult
import com.tokopedia.mvc.domain.entity.ShopData
import com.tokopedia.mvc.domain.entity.UpdateVoucher
import com.tokopedia.mvc.domain.entity.VoucherCreationMetadata
import com.tokopedia.mvc.domain.entity.enums.ImageRatio
import com.tokopedia.mvc.util.GenerateImageParamsBuilder
import com.tokopedia.mvc.util.constant.ImageGeneratorConstant
import com.tokopedia.mvc.util.constant.NumberConstant
import com.tokopedia.universal_sharing.usecase.ImageGeneratorUseCase
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class UpdateCouponFacadeUseCase @Inject constructor(
    private val updateCouponUseCase: UpdateVoucherPeriodUseCase,
    private val initiateCouponUseCase: GetInitiateVoucherPageUseCase,
    private val getShopBasicDataUseCase: ShopBasicDataUseCase,
    private val imageBuilder: GenerateImageParamsBuilder,
    private val productListUseCase: ProductListUseCase
) {
    suspend fun execute(
        updateVoucher: UpdateVoucher,
        parentProductId: List<Long>,
        startDate: String,
        startHour: String,
        endDate: String,
        endHour: String
    ): Boolean {
        return coroutineScope {
            val initiateVoucherDeferred = async { initiateCoupon() }
            val shopDeferred = async { getShopBasicDataUseCase.execute() }
            val topProductsDeferred = async { getMostSoldProducts(parentProductId) }

            val shop = shopDeferred.await()
            val topProducts = topProductsDeferred.await()
            val topProductImageUrls = topProducts.products.map { it.picture }

            val generateImageDeferred = async {
                generateImage(
                    ImageGeneratorConstant.IMAGE_TEMPLATE_COUPON_PRODUCT_SOURCE_ID,
                    ImageRatio.HORIZONTAL,
                    updateVoucher,
                    shop,
                    topProductImageUrls
                )
            }

            val generateSquareImageDeferred = async {
                generateImage(
                    ImageGeneratorConstant.IMAGE_TEMPLATE_COUPON_PRODUCT_SOURCE_ID,
                    ImageRatio.SQUARE,
                    updateVoucher,
                    shop,
                    topProductImageUrls
                )
            }

            val generatePortraitImage = async {
                generateImage(
                    ImageGeneratorConstant.IMAGE_TEMPLATE_COUPON_PRODUCT_SOURCE_ID,
                    ImageRatio.VERTICAL,
                    updateVoucher,
                    shop,
                    topProductImageUrls
                )
            }

            val imageUrl = generateImageDeferred.await()
            val squareImageUrl = generateSquareImageDeferred.await()
            val portraitImageUrl = generatePortraitImage.await()

            val voucher = initiateVoucherDeferred.await()

            val updateCouponDeferred = async {
                updateCoupon(
                    updateVoucher,
                    voucher.token,
                    startDate,
                    startHour,
                    endDate,
                    endHour,
                    imageUrl,
                    squareImageUrl,
                    portraitImageUrl
                )
            }

            return@coroutineScope updateCouponDeferred.await()
        }
    }

    private suspend fun updateCoupon(
        voucher: UpdateVoucher,
        token: String,
        startDate: String,
        startHour: String,
        endDate: String,
        endHour: String,
        imageUrl: String,
        imageSquare: String,
        imagePortrait: String
    ): Boolean {
        return updateCouponUseCase.updateVoucherPeriod(
            voucher, token, startDate, startHour, endDate, endHour, imageUrl, imageSquare, imagePortrait
        )
    }

    private suspend fun generateImage(
        sourceId: String,
        imageRatio: ImageRatio,
        updateVoucher: UpdateVoucher,
        shop: ShopData,
        parentProductsImageUrls: List<String> = emptyList()
    ): String {
        val imageParams = imageBuilder.build(
            imageRatio,
            parentProductsImageUrls,
            shop.logo,
            shop.name,
            updateVoucher
        )

        val requestParams = arrayListOf(
            GenerateImageParams(ImageGeneratorConstant.COUPON_PRODUCT_PLATFORM, imageParams.platform),
            GenerateImageParams(ImageGeneratorConstant.COUPON_PRODUCT_IS_PUBLIC, imageParams.isPublic),
            GenerateImageParams(ImageGeneratorConstant.COUPON_PRODUCT_VOUCHER_BENEFIT_TYPE, imageParams.voucherBenefitType),
            GenerateImageParams(ImageGeneratorConstant.COUPON_PRODUCT_VOUCHER_CASHBACK_TYPE, imageParams.voucherCashbackType),
            GenerateImageParams(ImageGeneratorConstant.COUPON_PRODUCT_VOUCHER_CASHBACK_PERCENTAGE, imageParams.voucherCashbackPercentage),
            GenerateImageParams(ImageGeneratorConstant.COUPON_PRODUCT_VOUCHER_NOMINAL_AMOUNT, imageParams.voucherNominalAmount),
            GenerateImageParams(ImageGeneratorConstant.COUPON_PRODUCT_VOUCHER_NOMINAL_SYMBOL, imageParams.voucherNominalSymbol),
            GenerateImageParams(ImageGeneratorConstant.COUPON_PRODUCT_SHOP_LOGO, imageParams.shopLogo),
            GenerateImageParams(ImageGeneratorConstant.COUPON_PRODUCT_SHOP_NAME, imageParams.shopName),
            GenerateImageParams(ImageGeneratorConstant.COUPON_PRODUCT_VOUCHER_CODE, imageParams.voucherCode),
            GenerateImageParams(ImageGeneratorConstant.COUPON_PRODUCT_VOUCHER_START_TIME, imageParams.voucherStartDate),
            GenerateImageParams(ImageGeneratorConstant.COUPON_PRODUCT_VOUCHER_FINISH_TIME, imageParams.voucherEndDate),
            GenerateImageParams(ImageGeneratorConstant.COUPON_PRODUCT_PRODUCT_COUNT, imageParams.productCount),
            GenerateImageParams(ImageGeneratorConstant.COUPON_PRODUCT_AUDIENCE_TARGET, imageParams.audienceTarget)
        )

        if (parentProductsImageUrls.isNotEmpty()) {
            requestParams.add(GenerateImageParams(ImageGeneratorConstant.COUPON_PRODUCT_FIRST_PRODUCT_IMAGE, imageParams.productImage1))
        }

        if (parentProductsImageUrls.size >= SECOND_IMAGE_URL_INDEX) {
            requestParams.add(GenerateImageParams(ImageGeneratorConstant.COUPON_PRODUCT_SECOND_PRODUCT_IMAGE, imageParams.productImage2))
        }

        if (parentProductsImageUrls.size >= THIRD_IMAGE_URL_INDEX) {
            requestParams.add(GenerateImageParams(ImageGeneratorConstant.COUPON_PRODUCT_THIRD_PRODUCT_IMAGE, imageParams.productImage3))
        }

        val modifiedParams = arrayListOf<GenerateImageParams>()
        modifiedParams.addAll(requestParams)

        val imageGeneratorUseCase =
            ImageGeneratorUseCase(GraphqlInteractor.getInstance().graphqlRepository)
        val params = GenerateImageUseCase.createParam(sourceId, requestParams)
        imageGeneratorUseCase.params = params
        return imageGeneratorUseCase.executeOnBackground()
    }

    private suspend fun initiateCoupon(): VoucherCreationMetadata {
        return initiateCouponUseCase.getToken()
    }

    private suspend fun getMostSoldProducts(productIds: List<Long>): ProductResult {
        val productListParam = ProductListUseCase.Param(
            searchKeyword = "",
            warehouseId = 0,
            categoryIds = emptyList(),
            showcaseIds = emptyList(),
            page = NumberConstant.FIRST_PAGE,
            pageSize = productIds.size,
            sortId = SOLD,
            sortDirection = SORT_DIRECTION,
            productIdInclude = productIds
        )
        return productListUseCase.execute(productListParam)
    }

    companion object {
        const val SOLD = "SOLD"
        const val SORT_DIRECTION = "DESC"
        private const val SECOND_IMAGE_URL_INDEX = 1
        private const val THIRD_IMAGE_URL_INDEX = 2
    }
}
