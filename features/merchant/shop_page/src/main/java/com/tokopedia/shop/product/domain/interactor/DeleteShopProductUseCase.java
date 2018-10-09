package com.tokopedia.shop.product.domain.interactor;

import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

/**
 * Created by normansyahputa on 2/23/18.
 */

public class DeleteShopProductUseCase extends UseCase<Boolean> {

    private final DeleteShopProductAceUseCase deleteShopProductAceUseCase;
    private final DeleteShopProductTomeUseCase deleteShopProductTomeUseCase;

    @Inject
    public DeleteShopProductUseCase(DeleteShopProductAceUseCase deleteShopProductAceUseCase,
                                    DeleteShopProductTomeUseCase deleteShopProductTomeUseCase) {
        this.deleteShopProductAceUseCase = deleteShopProductAceUseCase;
        this.deleteShopProductTomeUseCase = deleteShopProductTomeUseCase;
    }

    public Observable<Boolean> createObservable() {
        return createObservable(RequestParams.create());
    }

    @Override
    public Observable<Boolean> createObservable(RequestParams requestParams) {
        return Observable.zip(deleteShopProductAceUseCase.createObservable().subscribeOn(Schedulers.io()),
                deleteShopProductTomeUseCase.createObservable().subscribeOn(Schedulers.io()),
                new Func2<Boolean, Boolean, Boolean>() {
                    @Override
                    public Boolean call(Boolean aBoolean, Boolean aBoolean2) {
                        return aBoolean && aBoolean2;
                    }
                });
    }
}
