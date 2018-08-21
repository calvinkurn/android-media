package com.tokopedia.shop.open.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.shop.open.data.model.response.ResponseCreateShop;
import com.tokopedia.shop.open.domain.ShopOpenSaveInfoRepository;

import javax.inject.Inject;

import rx.Observable;


public class ShopOpenCreateUseCase extends UseCase<ResponseCreateShop> {

    private final ShopOpenSaveInfoRepository shopOpenSaveInfoRepository;

    @Inject
    public ShopOpenCreateUseCase(ThreadExecutor threadExecutor,
                                 PostExecutionThread postExecutionThread,
                                 ShopOpenSaveInfoRepository shopOpenSaveInfoRepository) {
        super(threadExecutor, postExecutionThread);
        this.shopOpenSaveInfoRepository = shopOpenSaveInfoRepository;
    }

    @Override
    public Observable<ResponseCreateShop> createObservable(RequestParams requestParams) {
        return shopOpenSaveInfoRepository.createShop();
    }
}
