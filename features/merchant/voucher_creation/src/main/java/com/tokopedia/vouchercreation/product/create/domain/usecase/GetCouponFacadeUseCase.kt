package com.tokopedia.vouchercreation.product.create.domain.usecase

import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.vouchercreation.product.create.data.mapper.CouponPreviewMapper
import com.tokopedia.vouchercreation.product.create.data.response.GetProductsByProductIdResponse
import com.tokopedia.vouchercreation.product.create.domain.entity.Coupon
import com.tokopedia.vouchercreation.product.create.domain.entity.CouponProduct
import com.tokopedia.vouchercreation.product.create.domain.entity.CouponUiModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import javax.inject.Inject

class GetCouponFacadeUseCase @Inject constructor(
    private val getCouponDetailUseCase: GetCouponDetailUseCase,
    private val getProductsUseCase: GetProductsUseCase,
    private val userSession: UserSessionInterface,
    private val couponPreviewMapper: CouponPreviewMapper
) {


    suspend fun execute(scope: CoroutineScope, couponId: Long): Coupon {
        val couponDetailDeferred = scope.async { getCouponDetail(couponId) }
        val couponDetail = couponDetailDeferred.await()

        val productsDeferred = scope.async { getProducts(couponDetail.productIds) }
        val products = productsDeferred.await()

        val couponProducts = mutableListOf<CouponProduct>()

        couponDetail.productIds.forEach { productId ->
            val imageUrl = getCouponImageUrl(productId.toString(), products.data)
            couponProducts.add(CouponProduct(productId.toString(), 0, 0f, imageUrl, 0))
        }

        return couponPreviewMapper.map(couponDetail, couponProducts)
    }


    private suspend fun getCouponDetail(couponId: Long): CouponUiModel {
        getCouponDetailUseCase.params = GetCouponDetailUseCase.createRequestParam(couponId)
        return getCouponDetailUseCase.executeOnBackground()
    }


    private suspend fun getProducts(productIds: List<Long>): GetProductsByProductIdResponse.GetProductListData {
        getProductsUseCase.params = GetProductsUseCase.createParams(userSession.shopId, productIds)
        return getProductsUseCase.executeOnBackground()
    }

    private fun getCouponImageUrl(
        childProductId: String,
        productIds: List<GetProductsByProductIdResponse.Data>
    ): String {
        productIds.forEach { product ->
            if (childProductId == product.id) {
                return getImageUrlOrEmpty(product.pictures)
            }
        }

        return ""
    }

    private fun getImageUrlOrEmpty(pictures : List<GetProductsByProductIdResponse.Picture>): String {
        if (pictures.isEmpty()) {
            return ""
        }

        return pictures[0].urlThumbnail
    }
}