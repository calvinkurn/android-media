package com.tokopedia.posapp.outlet.domain.usecase;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.posapp.outlet.data.repository.OutletRepository;
import com.tokopedia.posapp.outlet.domain.model.OutletDomain;

import rx.Observable;

/**
 * Created by okasurya on 7/31/17.
 */

public class GetOutletUseCase extends UseCase<OutletDomain> {
    public OutletRepository outletRepository;

    public GetOutletUseCase(ThreadExecutor threadExecutor,
                            PostExecutionThread postExecutionThread,
                            OutletRepository outletRepository) {
        super(threadExecutor, postExecutionThread);
        this.outletRepository = outletRepository;
    }

    @Override
    public Observable<OutletDomain> createObservable(RequestParams requestParams) {
        return outletRepository.getOutlet(requestParams);
    }
}
