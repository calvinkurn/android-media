package com.tokopedia.posapp.etalase.domain;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.posapp.etalase.data.repository.EtalaseRepository;
import com.tokopedia.posapp.shop.domain.model.EtalaseDomain;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by okasurya on 9/18/17.
 */

public class GetEtalaseUseCase extends UseCase<List<EtalaseDomain>> {
    private EtalaseRepository etalaseRepository;

    @Inject
    public GetEtalaseUseCase(ThreadExecutor threadExecutor,
                             PostExecutionThread postExecutionThread,
                             EtalaseRepository etalaseRepository) {
        super(threadExecutor, postExecutionThread);
        this.etalaseRepository = etalaseRepository;
    }

    @Override
    public Observable<List<EtalaseDomain>> createObservable(RequestParams requestParams) {
        return etalaseRepository.getEtalase(requestParams);
    }
}
