package com.tokopedia.shop.pageheader.domain.interactor

import com.tokopedia.shop.common.domain.interactor.ToggleFavouriteShopUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import javax.inject.Inject

/**
 * Created by zulfikarrahman on 7/7/17.
 */
class ToggleFavouriteShopAndDeleteCacheUseCase @Inject constructor(private val toggleFavouriteShopUseCase: ToggleFavouriteShopUseCase) : UseCase<Boolean>() {
    override fun createObservable(requestParams: RequestParams): Observable<Boolean> {
        return toggleFavouriteShopUseCase.createObservable(requestParams)
                .flatMap { aBoolean ->
                    if (aBoolean) {
                        Observable.just(aBoolean)
                    } else {
                        Observable.just(aBoolean)
                    }
                }
    }

    override fun unsubscribe() {
        super.unsubscribe()
        toggleFavouriteShopUseCase.unsubscribe()
    }

    companion object {
        private const val SHOP_ID = "SHOP_ID"
        fun createRequestParam(shopId: String?): RequestParams {
            val requestParams = RequestParams.create()
            requestParams.putString(SHOP_ID, shopId)
            return requestParams
        }
    }
}