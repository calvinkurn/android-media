package com.tokopedia.shop.product.domain.interactor

import com.tokopedia.abstraction.common.data.model.response.PagingList
import com.tokopedia.shop.product.data.source.cloud.model.ShopProduct
import com.tokopedia.shop.product.domain.model.ShopProductRequestModel
import com.tokopedia.shop.product.domain.repository.ShopProductRepository
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import javax.inject.Inject

/**
 * Created by normansyahputa on 2/8/18.
 */
class GetShopProductListUseCase @Inject constructor(private val shopProductRepository: ShopProductRepository) : UseCase<PagingList<ShopProduct?>>() {
    override fun createObservable(requestParams: RequestParams): Observable<PagingList<ShopProduct?>>? {
        val shopProductRequestModel = requestParams.getObject(SHOP_REQUEST) as ShopProductRequestModel
        return shopProductRepository.getShopProductList(shopProductRequestModel)
    }

    companion object {
        private const val SHOP_REQUEST = "SHOP_REQUEST"
        fun createRequestParam(shopProductRequestModel: ShopProductRequestModel?): RequestParams {
            val requestParams = RequestParams.create()
            requestParams.putObject(SHOP_REQUEST, shopProductRequestModel)
            return requestParams
        }
    }
}