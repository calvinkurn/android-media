package com.tokopedia.productcard.options.topadswishlist

import com.tokopedia.topads.sdk.domain.TopAdsWishlistService
import com.tokopedia.topads.sdk.domain.interactor.TopAdsWishlishedUseCase.WISHSLIST_URL
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable

internal class TopAdsWishlistUseCase(
        private val wishlistService: TopAdsWishlistService
): UseCase<Boolean>() {

    override fun createObservable(requestParams: RequestParams): Observable<Boolean> {
        return wishlistService.wishlistUrl(
                requestParams.getString(WISHSLIST_URL, "")
        ).map { response ->
            response?.body()
        }.map {
            it?.data?.isSuccess == true
        }
    }
}