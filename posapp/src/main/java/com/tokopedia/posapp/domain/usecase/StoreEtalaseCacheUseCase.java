package com.tokopedia.posapp.domain.usecase;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.posapp.data.repository.EtalaseRepository;
import com.tokopedia.posapp.data.repository.ShopRepository;
import com.tokopedia.posapp.database.manager.base.DbStatus;
import com.tokopedia.posapp.domain.model.shop.ShopEtalaseDomain;

import java.util.List;

import rx.Observable;

/**
 * Created by okasurya on 9/18/17.
 */

public class StoreEtalaseCacheUseCase extends UseCase<List<DbStatus>>{
    EtalaseRepository etalaseRepository;

    public StoreEtalaseCacheUseCase(ThreadExecutor threadExecutor,
                                    PostExecutionThread postExecutionThread,
                                    EtalaseRepository etalaseRespository) {
        super(threadExecutor, postExecutionThread);
        this.etalaseRepository = etalaseRespository;
    }

    @Override
    public Observable<List<DbStatus>> createObservable(RequestParams requestParams) {
//        List<ShopEtalaseDomain>
//        return shopRepository.storeEtalaseToCache();
        return null;

    }
}
