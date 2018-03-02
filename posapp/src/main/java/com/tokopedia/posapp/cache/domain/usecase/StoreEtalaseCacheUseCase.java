package com.tokopedia.posapp.cache.domain.usecase;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.posapp.cache.data.repository.EtalaseRepository;
import com.tokopedia.posapp.base.domain.model.DataStatus;
import com.tokopedia.posapp.base.domain.model.ListDomain;
import com.tokopedia.posapp.shop.domain.model.EtalaseDomain;

import rx.Observable;

/**
 * Created by okasurya on 9/18/17.
 */

public class StoreEtalaseCacheUseCase extends UseCase<DataStatus>{
    public static final String DATA = "data";

    private EtalaseRepository etalaseRepository;

    public StoreEtalaseCacheUseCase(ThreadExecutor threadExecutor,
                                    PostExecutionThread postExecutionThread,
                                    EtalaseRepository etalaseRespository) {
        super(threadExecutor, postExecutionThread);
        this.etalaseRepository = etalaseRespository;
    }

    @Override
    public Observable<DataStatus> createObservable(RequestParams requestParams) {
        ListDomain<EtalaseDomain> data = (ListDomain<EtalaseDomain>) requestParams.getObject(DATA);
        return etalaseRepository.storeEtalaseToCache(data);

    }
}
