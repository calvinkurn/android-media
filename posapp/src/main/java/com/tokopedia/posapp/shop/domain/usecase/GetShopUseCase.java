package com.tokopedia.posapp.shop.domain.usecase;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.posapp.shop.data.repository.ShopRepository;
import com.tokopedia.posapp.shop.domain.model.ShopDomain;

import rx.Observable;

/**
 * Created by okasurya on 8/3/17.
 */

public class GetShopUseCase extends UseCase<ShopDomain> {
    ShopRepository shopRepository;

    public GetShopUseCase(ThreadExecutor threadExecutor,
                          PostExecutionThread postExecutionThread,
                          ShopRepository shopRepository) {
        super(threadExecutor, postExecutionThread);
        this.shopRepository = shopRepository;
    }

    @Override
    public Observable<ShopDomain> createObservable(RequestParams requestParams) {
        return shopRepository.getShop(requestParams);
    }
}
