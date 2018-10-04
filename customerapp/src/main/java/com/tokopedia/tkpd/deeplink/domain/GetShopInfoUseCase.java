package com.tokopedia.tkpd.deeplink.domain;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.shopinfo.models.shopmodel.ShopModel;
import com.tokopedia.tkpd.deeplink.data.repository.ShopInfoRepository;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by okasurya on 1/5/18.
 */

/**
 * Get shop info object from tkpd shop
 */
@Deprecated
public class GetShopInfoUseCase extends UseCase<ShopModel> {
    private ShopInfoRepository shopInfoRepository;

    @Inject
    public GetShopInfoUseCase(ThreadExecutor threadExecutor,
                              PostExecutionThread postExecutionThread,
                              ShopInfoRepository shopInfoRepository) {
        super(threadExecutor, postExecutionThread);
        this.shopInfoRepository = shopInfoRepository;
    }

    @Override
    public Observable<ShopModel> createObservable(RequestParams requestParams) {
        return shopInfoRepository.getShopInfo(requestParams);
    }
}
