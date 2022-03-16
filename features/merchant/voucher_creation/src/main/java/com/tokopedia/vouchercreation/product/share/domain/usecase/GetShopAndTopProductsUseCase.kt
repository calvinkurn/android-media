package com.tokopedia.vouchercreation.product.share.domain.usecase

import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.vouchercreation.common.mapper.CouponMapper
import com.tokopedia.vouchercreation.product.create.data.response.GetProductsByProductIdResponse
import com.tokopedia.vouchercreation.product.create.domain.entity.CouponUiModel
import com.tokopedia.vouchercreation.product.create.domain.usecase.GetProductsUseCase
import com.tokopedia.vouchercreation.product.share.domain.entity.ShopWithTopProducts
import com.tokopedia.vouchercreation.shop.voucherlist.domain.usecase.ShopBasicDataUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import javax.inject.Inject

class GetShopAndTopProductsUseCase @Inject constructor(
    private val getShopBasicDataUseCase: ShopBasicDataUseCase,
    private val getProductsUseCase: GetProductsUseCase,
    private val userSession: UserSessionInterface,
    private val mapper : CouponMapper
) {

    companion object {
        private const val EMPTY_STRING = ""
    }

    suspend fun execute(
        scope: CoroutineScope,
        couponUiModel: CouponUiModel
    ): ShopWithTopProducts {

        val coupon = mapper.map(couponUiModel)

        val shopDeferred = scope.async { getShopBasicDataUseCase.executeOnBackground() }
        val shop = shopDeferred.await()

        val productIds = coupon.productIds.map { it.parentProductId }
        val productsDeferred = scope.async { getProducts(productIds) }
        val products = productsDeferred.await()

        val productImageUrls = products.data.map {
            getImageUrlOrEmpty(it.pictures)
        }

        return ShopWithTopProducts(productImageUrls, shop)
    }

    private suspend fun getProducts(productIds: List<Long>): GetProductsByProductIdResponse.GetProductListData {
        getProductsUseCase.params = GetProductsUseCase.createParams(userSession.shopId, productIds)
        return getProductsUseCase.executeOnBackground()
    }


    private fun getImageUrlOrEmpty(pictures : List<GetProductsByProductIdResponse.Picture>): String {
        if (pictures.isEmpty()) {
            return EMPTY_STRING
        }

        return pictures[0].urlThumbnail
    }
}