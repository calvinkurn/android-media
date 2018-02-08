package com.tokopedia.posapp.domain.usecase;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.posapp.data.repository.EtalaseRepository;
import com.tokopedia.posapp.domain.model.DataStatus;
import com.tokopedia.posapp.domain.model.ListDomain;
import com.tokopedia.posapp.domain.model.shop.EtalaseDomain;

import java.util.List;

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
