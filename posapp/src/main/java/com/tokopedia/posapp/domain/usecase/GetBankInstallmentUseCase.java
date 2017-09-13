package com.tokopedia.posapp.domain.usecase;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.posapp.data.repository.BankRepository;
import com.tokopedia.posapp.data.repository.PaymentRepository;
import com.tokopedia.posapp.domain.model.bank.BankInstallmentDomain;

import rx.Observable;

/**
 * Created by okasurya on 9/5/17.
 */

public class GetBankInstallmentUseCase extends UseCase<BankInstallmentDomain>{
    BankRepository bankRepository;

    public GetBankInstallmentUseCase(ThreadExecutor threadExecutor,
                                     PostExecutionThread postExecutionThread,
                                     BankRepository bankRepository) {
        super(threadExecutor, postExecutionThread);
        this.bankRepository = bankRepository;
    }

    @Override
    public Observable<BankInstallmentDomain> createObservable(RequestParams requestParams) {
        return bankRepository.getBankInstallment();
    }
}
