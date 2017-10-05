package com.tokopedia.posapp.domain.usecase;

import com.tokopedia.core.base.domain.DefaultParams;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.posapp.base.domain.UseCaseWithParams;
import com.tokopedia.posapp.data.repository.BankRepository;
import com.tokopedia.posapp.domain.model.bank.BankInstallmentDomain;
import com.tokopedia.posapp.domain.model.result.BankSavedResult;

import rx.Observable;

/**
 * Created by okasurya on 9/8/17.
 */

public class StoreBankUsecase extends UseCase<BankSavedResult> {
    public static final String BANK_INSTALLMENT_DOMAIN = "BANK_INSTALLMENT_DOMAIN";

    private BankRepository bankRepository;

    public StoreBankUsecase(ThreadExecutor threadExecutor,
                            PostExecutionThread postExecutionThread,
                            BankRepository bankRepository) {
        super(threadExecutor, postExecutionThread);
        this.bankRepository = bankRepository;
    }
    @Override
    public Observable<BankSavedResult> createObservable(RequestParams requestParams) {
        BankInstallmentDomain data = (BankInstallmentDomain) requestParams.getObject(BANK_INSTALLMENT_DOMAIN);
        if(data != null) return bankRepository.storeBankToCache(data);

        return Observable.empty();
    }
}
