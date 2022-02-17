package com.tokopedia.vouchercreation.product.share.domain.usecase

import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.universal_sharing.usecase.ImageGeneratorUseCase
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.vouchercreation.common.consts.NumberConstant
import com.tokopedia.vouchercreation.common.mapper.CouponMapper
import com.tokopedia.vouchercreation.product.create.data.request.GenerateImageParams
import com.tokopedia.vouchercreation.product.create.data.response.GetProductsByProductIdResponse
import com.tokopedia.vouchercreation.product.create.domain.entity.*
import com.tokopedia.vouchercreation.product.create.domain.usecase.GenerateImageUseCase
import com.tokopedia.vouchercreation.product.create.domain.usecase.GetProductsUseCase
import com.tokopedia.vouchercreation.product.create.util.GenerateImageParamsBuilder
import com.tokopedia.vouchercreation.product.share.domain.entity.CouponImageWithShop
import com.tokopedia.vouchercreation.shop.voucherlist.domain.model.ShopBasicDataResult
import com.tokopedia.vouchercreation.shop.voucherlist.domain.usecase.ShopBasicDataUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import javax.inject.Inject

class GenerateImageWithStatisticsFacadeUseCase @Inject constructor(
    private val getShopBasicDataUseCase: ShopBasicDataUseCase,
    private val getProductsUseCase: GetProductsUseCase,
    private val userSession: UserSessionInterface,
    private val imageBuilder: GenerateImageParamsBuilder,
    private val mapper : CouponMapper
) {

    companion object {
        private const val EMPTY_STRING = ""
    }

    suspend fun execute(
        scope: CoroutineScope,
        sourceId: String,
        couponUiModel: CouponUiModel
    ): CouponImageWithShop {

        val coupon = mapper.map(couponUiModel)

        val shopDeferred = scope.async { getShopBasicDataUseCase.executeOnBackground() }
        val shop = shopDeferred.await()

        val productIds = coupon.products.map { it.id.toLong() }
        val productsDeferred = scope.async { getProducts(productIds) }
        val products = productsDeferred.await()

        val couponProducts = mutableListOf<CouponProduct>()
        productIds.forEach { productId ->
            val pair = getCouponImageUrlAndSoldCount(productId.toString(), products.data)
            couponProducts.add(CouponProduct(productId.toString(), pair.first, pair.second))
        }

        val generateImageDeferred = scope.async {
            generateImage(
                sourceId,
                ImageRatio.HORIZONTAL,
                coupon.information,
                coupon.settings,
                shop,
                couponProducts
            )
        }

        val imageUrl = generateImageDeferred.await()

        return CouponImageWithShop(imageUrl, shop)
    }

    private suspend fun generateImage(
        sourceId: String,
        imageRatio: ImageRatio,
        couponInformation: CouponInformation,
        couponSettings: CouponSettings,
        shop: ShopBasicDataResult,
        products : List<CouponProduct>
    ): String {
        val imageParams = imageBuilder.build(imageRatio, couponInformation, couponSettings, products, shop.logo, shop.shopName)

        val requestParams = arrayListOf(
            GenerateImageParams("platform", imageParams.platform),
            GenerateImageParams("is_public", imageParams.isPublic),
            GenerateImageParams("voucher_benefit_type", imageParams.voucherBenefitType),
            GenerateImageParams("voucher_cashback_type", imageParams.voucherCashbackType),
            GenerateImageParams("voucher_cashback_percentage", imageParams.voucherCashbackPercentage),
            GenerateImageParams("voucher_nominal_amount", imageParams.voucherNominalAmount),
            GenerateImageParams("voucher_nominal_symbol", imageParams.voucherNominalSymbol),
            GenerateImageParams("shop_logo", imageParams.shopLogo),
            GenerateImageParams("shop_name", imageParams.shopName),
            GenerateImageParams("voucher_code", imageParams.voucherCode),
            GenerateImageParams("voucher_start_time", imageParams.voucherStartTime),
            GenerateImageParams("voucher_finish_time", imageParams.voucherFinishTime),
            GenerateImageParams("product_count", imageParams.productCount),
            GenerateImageParams("product_image_1", imageParams.productImage1),
            GenerateImageParams("product_image_2", imageParams.productImage2),
            GenerateImageParams("product_image_3", imageParams.productImage3),
            GenerateImageParams("audience_target", imageParams.audienceTarget)
        )

        val imageGeneratorUseCase =
            ImageGeneratorUseCase(GraphqlInteractor.getInstance().graphqlRepository)
        val params = GenerateImageUseCase.createParam(sourceId, requestParams)
        imageGeneratorUseCase.params = params
        return imageGeneratorUseCase.executeOnBackground()
    }

    private suspend fun getProducts(productIds: List<Long>): GetProductsByProductIdResponse.GetProductListData {
        getProductsUseCase.params = GetProductsUseCase.createParams(userSession.shopId, productIds)
        return getProductsUseCase.executeOnBackground()
    }

    private fun getCouponImageUrlAndSoldCount(
        childProductId: String,
        productIds: List<GetProductsByProductIdResponse.Data>
    ): Pair<String, Int> {
        productIds.forEach { product ->
            if (childProductId == product.id) {
                val imageUrl = getImageUrlOrEmpty(product.pictures)
                return Pair(imageUrl, product.txStats.sold)
            }
        }

        return Pair(EMPTY_STRING, NumberConstant.ZERO)
    }

    private fun getImageUrlOrEmpty(pictures : List<GetProductsByProductIdResponse.Picture>): String {
        if (pictures.isEmpty()) {
            return EMPTY_STRING
        }

        return pictures[0].urlThumbnail
    }
}