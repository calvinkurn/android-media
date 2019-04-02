package com.tokopedia.topads.sdk.domain.interactor;

import com.tokopedia.topads.sdk.domain.TopAdsWishlistService;
import com.tokopedia.topads.sdk.domain.model.WishlistModel;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * Author errysuprayogi on 04,December,2018
 */
public class TopAdsWishlishedUseCase extends UseCase<WishlistModel> {

    public static final String WISHSLIST_URL = "wishlist_url";
    private final TopAdsWishlistService wishlistService;

    public TopAdsWishlishedUseCase(TopAdsWishlistService wishlistService) {
        this.wishlistService = wishlistService;
    }

    @Override
    public Observable<WishlistModel> createObservable(RequestParams requestParams) {
        return wishlistService.wishlistUrl(requestParams.getString(WISHSLIST_URL,
                "")).map(new Func1<Response<WishlistModel>, WishlistModel>() {
            @Override
            public WishlistModel call(Response<WishlistModel> response) {
                return response.body();
            }
        });
    }
}
