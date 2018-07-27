package com.tokopedia.shop.page.domain.interactor;

import com.tokopedia.shop.common.domain.interactor.DeleteShopInfoUseCase;
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
    private final DeleteShopInfoUseCase deleteShopInfoUseCase;

    @Inject
    public ToggleFavouriteShopAndDeleteCacheUseCase(ToggleFavouriteShopUseCase toggleFavouriteShopUseCase,
                                                    DeleteShopInfoUseCase deleteShopInfoUseCase) {
        this.toggleFavouriteShopUseCase = toggleFavouriteShopUseCase;
        this.deleteShopInfoUseCase = deleteShopInfoUseCase;
    }

    @Override
    public Observable<Boolean> createObservable(RequestParams requestParams) {
        return toggleFavouriteShopUseCase.createObservable(requestParams)
                .flatMap(new Func1<Boolean, Observable<Boolean>>() {
            @Override
            public Observable<Boolean> call(Boolean aBoolean) {
                if (aBoolean) {
                    return deleteShopInfoUseCase.createObservable()
                            .onErrorResumeNext(new Func1<Throwable, Observable<? extends Boolean>>() {
                                @Override
                                public Observable<? extends Boolean> call(Throwable throwable) {
                                    return Observable.just(aBoolean);
                                }
                            });
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
        deleteShopInfoUseCase.unsubscribe();
        toggleFavouriteShopUseCase.unsubscribe();
    }
}