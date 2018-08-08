package com.tokopedia.product.edit.common.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.shopinfo.models.shopmodel.ShopModel;
import com.tokopedia.product.edit.common.domain.repository.ShopInfoRepository;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by User on 9/8/2017.
 */

/**
 * Get shop info object from tkpd shop
 */
@Deprecated
public class GetShopInfoUseCase extends UseCase<ShopModel> {

    private ShopInfoRepository shopInfoRepository;

    @Inject
    public GetShopInfoUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread,
                              ShopInfoRepository shopInfoRepository) {
        super(threadExecutor, postExecutionThread);
        this.shopInfoRepository = shopInfoRepository;
    }

    public Observable<ShopModel> createObservable() {
        return createObservable(RequestParams.EMPTY);
    }

    @Override
    public Observable<ShopModel> createObservable(RequestParams requestParams) {
        return shopInfoRepository.getShopInfo();
    }
}
