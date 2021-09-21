package com.tokopedia.shop.product.domain.interactor

import com.tokopedia.shop.product.data.source.cloud.model.ShopProductCampaign
import com.tokopedia.shop.product.domain.repository.ShopProductRepository
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import com.tokopedia.wishlist.common.data.source.cloud.mapper.WishListProductListMapper
import rx.Observable

/**
 * Created by normansyahputa on 3/14/18.
 */
class GetProductCampaignsUseCase(private val shopProductRepository: ShopProductRepository) : UseCase<List<ShopProductCampaign?>?>() {
    override fun createObservable(requestParams: RequestParams): Observable<List<ShopProductCampaign?>?> {
        val productIdList = requestParams.getObject(PRODUCT_IDS) as List<String>
        return shopProductRepository.getProductCampaigns(WishListProductListMapper.convertCommaValue(productIdList))
    }

    companion object {
        private const val PRODUCT_IDS = "PRODUCT_IDS"
        fun createRequestParam(productIdList: List<String?>?): RequestParams {
            val requestParams = RequestParams.create()
            requestParams.putObject(PRODUCT_IDS, productIdList)
            return requestParams
        }
    }
}