package com.tokopedia.core.drawer2.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.drawer2.data.pojo.deposit.DepositModel;
import com.tokopedia.core.drawer2.domain.DepositRepository;

import rx.Observable;

/**
 * Created by nisie on 5/4/17.
 */

public class DepositUseCase extends UseCase<DepositModel> {

    private final DepositRepository depositRepository;

    public DepositUseCase(ThreadExecutor threadExecutor,
                          PostExecutionThread postExecutionThread,
                          DepositRepository depositRepository) {
        super(threadExecutor, postExecutionThread);
        this.depositRepository = depositRepository;
    }

    @Override
    public Observable<DepositModel> createObservable(RequestParams requestParams) {
        return depositRepository.getDeposit(requestParams.getParameters());
    }
}
