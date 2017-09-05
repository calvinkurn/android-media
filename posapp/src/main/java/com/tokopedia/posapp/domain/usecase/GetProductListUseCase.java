package com.tokopedia.posapp.domain.usecase;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.posapp.data.repository.ShopRepository;
import com.tokopedia.posapp.domain.model.shop.ShopProductListDomain;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by okasurya on 8/28/17.
 */

public class GetProductListUseCase extends UseCase<ShopProductListDomain> {
    ShopRepository shopRepository;

    @Inject
    public GetProductListUseCase(ThreadExecutor threadExecutor,
                                 PostExecutionThread postExecutionThread,
                                 ShopRepository shopRepository) {
        super(threadExecutor, postExecutionThread);

        this.shopRepository = shopRepository;
    }

    @Override
    public Observable<ShopProductListDomain> createObservable(RequestParams requestParams) {
        return shopRepository.getShopProduct(requestParams);
    }
}
