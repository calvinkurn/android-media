package com.tokopedia.shop.pageheader.domain.interactor

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.cacheapi.domain.interactor.CacheApiDataDeleteUseCase
import com.tokopedia.shop.common.constant.ShopCommonUrl
import com.tokopedia.shop.common.constant.ShopUrl
import com.tokopedia.usecase.RequestParams
import rx.Observable
import javax.inject.Inject

/**
 * Created by zulfikarrahman on 7/7/17.
 */
class DeleteFavoriteListCacheUseCase @Inject constructor(@ApplicationContext context: Context?) : CacheApiDataDeleteUseCase(context) {
    fun createObservable(): Observable<Boolean> {
        return createObservable(RequestParams.create())
    }

    override fun createObservable(requestParams: RequestParams): Observable<Boolean> {
        val newRequestParams = createParams(ShopCommonUrl.BASE_WS_URL, ShopUrl.SHOP_FAVOURITE_USER)
        return super.createObservable(newRequestParams)
    }
}