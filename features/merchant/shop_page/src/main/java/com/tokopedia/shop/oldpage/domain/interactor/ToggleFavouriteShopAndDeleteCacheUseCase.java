package com.tokopedia.shop.oldpage.domain.interactor;

import com.tokopedia.shop.common.domain.interactor.DeleteShopInfoCacheUseCase;
import com.tokopedia.shop.common.domain.interactor.ToggleFavouriteShopUseCase;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by zulfikarrahman on 7/7/17.
 */

public class ToggleFavouriteShopAndDeleteCacheUseCase extends UseCase<Boolean> {

    private static final String SHOP_ID = "SHOP_ID";

    private final ToggleFavouriteShopUseCase toggleFavouriteShopUseCase;
    private final DeleteShopInfoCacheUseCase deleteShopInfoCacheUseCase;
    private final DeleteFavoriteListCacheUseCase deleteFavoriteListCacheUseCase;

    @Inject
    public ToggleFavouriteShopAndDeleteCacheUseCase(ToggleFavouriteShopUseCase toggleFavouriteShopUseCase,
                                                    DeleteShopInfoCacheUseCase deleteShopInfoCacheUseCase,
                                                    DeleteFavoriteListCacheUseCase deleteFavoriteListCacheUseCase) {
        this.toggleFavouriteShopUseCase = toggleFavouriteShopUseCase;
        this.deleteShopInfoCacheUseCase = deleteShopInfoCacheUseCase;
        this.deleteFavoriteListCacheUseCase = deleteFavoriteListCacheUseCase;
    }

    @Override
    public Observable<Boolean> createObservable(RequestParams requestParams) {
        return toggleFavouriteShopUseCase.createObservable(requestParams)
                .flatMap(new Func1<Boolean, Observable<Boolean>>() {
                    @Override
                    public Observable<Boolean> call(Boolean aBoolean) {
                        if (aBoolean) {
                            try {
                                deleteFavoriteListCacheUseCase.executeSync();
                                deleteShopInfoCacheUseCase.executeSync();
                            } catch (Throwable e) {
                                // no-op
                            }
                            return Observable.just(aBoolean);
                        } else {
                            return Observable.just(aBoolean);
                        }
                    }
                });
    }

    public static RequestParams createRequestParam(String shopId) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(SHOP_ID, shopId);
        return requestParams;
    }

    @Override
    public void unsubscribe() {
        super.unsubscribe();
        deleteShopInfoCacheUseCase.unsubscribe();
        toggleFavouriteShopUseCase.unsubscribe();
        deleteFavoriteListCacheUseCase.unsubscribe();
    }
}