package com.tokopedia.product.manage.item.common.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.product.manage.item.common.domain.interactor.BaseShopInfoUseCase;
import com.tokopedia.product.manage.item.common.domain.model.AddProductShopInfoDomainModel;
import com.tokopedia.product.manage.item.common.domain.repository.ShopInfoRepository;
import com.tokopedia.product.manage.item.common.domain.model.AddProductShopInfoDomainModel;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by Hendry on 4/20/2017.
 */

public class AddProductShopInfoUseCase extends BaseShopInfoUseCase<AddProductShopInfoDomainModel> {

    @Inject
    public AddProductShopInfoUseCase(
            ThreadExecutor threadExecutor,
            PostExecutionThread postExecutionThread,
            ShopInfoRepository shopInfoRepository) {
        super(threadExecutor, postExecutionThread, shopInfoRepository);
    }

    @Override
    public Observable<AddProductShopInfoDomainModel> createObservable(RequestParams requestParams) {
        return getShopInfo();
    }

    protected Observable<AddProductShopInfoDomainModel> getShopInfo() {
        return shopInfoRepository.getAddProductShopInfo();
    }

}