package com.tokopedia.vouchercreation.product.create.domain.usecase

import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.vouchercreation.common.consts.GqlQueryConstant
import com.tokopedia.vouchercreation.common.consts.NumberConstant
import com.tokopedia.vouchercreation.product.create.data.mapper.CouponMapper
import com.tokopedia.vouchercreation.product.create.data.response.GetProductsByProductIdResponse
import com.tokopedia.vouchercreation.product.create.domain.entity.CouponProduct
import com.tokopedia.vouchercreation.product.create.domain.entity.CouponUiModel
import com.tokopedia.vouchercreation.product.create.domain.entity.CouponWithMetadata
import com.tokopedia.vouchercreation.shop.create.view.uimodel.initiation.InitiateVoucherUiModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import javax.inject.Inject

class GetCouponFacadeUseCase @Inject constructor(
    private val getCouponDetailUseCase: GetCouponDetailUseCase,
    private val getProductsUseCase: GetProductsUseCase,
    private val userSession: UserSessionInterface,
    private val couponMapper: CouponMapper,
    private val initiateCouponUseCase: InitiateCouponUseCase
) {
    
    companion object {
        private const val EMPTY_STRING = ""
        private const val IS_UPDATE_MODE = true
    }

    suspend fun execute(scope: CoroutineScope, couponId: Long, isToCreateNewCoupon : Boolean): CouponWithMetadata {
        val initiateVoucherDeferred = scope.async { initiateVoucher(IS_UPDATE_MODE, isToCreateNewCoupon) }

        val couponDetailDeferred = scope.async { getCouponDetail(couponId) }
        val couponDetail = couponDetailDeferred.await()

        val parentProductIds = couponDetail.products.map { it.parentProductId }
        val productsDeferred = scope.async { getProducts(parentProductIds) }
        val products = productsDeferred.await()

        val voucher = initiateVoucherDeferred.await()

        val couponProducts = mutableListOf<CouponProduct>()

        couponDetail.productIds.forEach { productId ->
            val pair = getCouponImageUrlAndSoldCount(productId.toString(), products.data)
            couponProducts.add(CouponProduct(productId.toString(), pair.first, pair.second))
        }

        val coupon = couponMapper.map(couponDetail, couponProducts)
        return CouponWithMetadata(coupon, voucher.maxProducts)
    }


    private suspend fun getCouponDetail(couponId: Long): CouponUiModel {
        getCouponDetailUseCase.params = GetCouponDetailUseCase.createRequestParam(couponId)
        return getCouponDetailUseCase.executeOnBackground()
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

    private suspend fun initiateVoucher(isUpdateMode: Boolean, isToCreateNewCoupon: Boolean): InitiateVoucherUiModel {
        initiateCouponUseCase.query = GqlQueryConstant.INITIATE_COUPON_PRODUCT_QUERY
        initiateCouponUseCase.params =
            InitiateCouponUseCase.createRequestParam(isUpdateMode, isToCreateNewCoupon)
        return initiateCouponUseCase.executeOnBackground()
    }
}