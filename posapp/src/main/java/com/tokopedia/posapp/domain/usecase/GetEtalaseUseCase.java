package com.tokopedia.posapp.domain.usecase;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.posapp.data.repository.EtalaseRepository;
import com.tokopedia.posapp.domain.model.shop.ShopEtalaseDomain;

import java.util.List;

import rx.Observable;

/**
 * Created by okasurya on 9/18/17.
 */

public class GetEtalaseUseCase extends UseCase<List<ShopEtalaseDomain>> {
    EtalaseRepository etalaseRepository;

    public GetEtalaseUseCase(ThreadExecutor threadExecutor,
                             PostExecutionThread postExecutionThread,
                             EtalaseRepository etalaseRepository) {
        super(threadExecutor, postExecutionThread);
        this.etalaseRepository = etalaseRepository;
    }

    @Override
    public Observable<List<ShopEtalaseDomain>> createObservable(RequestParams requestParams) {
        return etalaseRepository.getEtalase(requestParams);
    }
}
