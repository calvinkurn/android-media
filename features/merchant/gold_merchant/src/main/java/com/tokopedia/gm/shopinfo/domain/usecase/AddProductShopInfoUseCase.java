package com.tokopedia.gm.shopinfo.domain.usecase;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.seller.common.usecase.PostExecutionThread;
import com.tokopedia.seller.common.usecase.ThreadExecutor;
import com.tokopedia.gm.shopinfo.data.model.AddProductShopInfoDomainModel;
import com.tokopedia.gm.shopinfo.domain.repository.ShopInfoRepository;

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