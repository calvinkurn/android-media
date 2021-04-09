package com.tokopedia.shop.product.domain.interactor

import android.content.Context
import com.tokopedia.cacheapi.domain.interactor.CacheApiDataDeleteUseCase
import com.tokopedia.shop.common.constant.ShopCommonUrl
import com.tokopedia.shop.common.constant.ShopUrl
import com.tokopedia.usecase.RequestParams
import rx.Observable

/**
 * Created by zulfikarrahman on 7/7/17.
 */
class DeleteShopProductTomeUseCase(context: Context?) : CacheApiDataDeleteUseCase(context) {
    fun createObservable(): Observable<Boolean> {
        return createObservable(RequestParams.create())
    }

    override fun createObservable(requestParams: RequestParams): Observable<Boolean> {
        val newRequestParams = createParams(ShopCommonUrl.BASE_URL, ShopUrl.SHOP_PRODUCT_PATH)
        return super.createObservable(newRequestParams)
    }
}