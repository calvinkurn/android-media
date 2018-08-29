package com.tokopedia.posapp.bank.domain.usecase;

import com.tokopedia.posapp.bank.data.repository.BankLocalRepository;
import com.tokopedia.posapp.bank.data.repository.BankRepository;
import com.tokopedia.posapp.bank.domain.model.BankInstallmentDomain;
import com.tokopedia.posapp.bank.domain.model.BankSavedResult;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by okasurya on 9/8/17.
 */

public class StoreBankUsecase extends UseCase<BankSavedResult> {
    public static final String BANK_INSTALLMENT_DOMAIN = "BANK_INSTALLMENT_DOMAIN";

    private BankRepository bankRepository;

    @Inject
    public StoreBankUsecase(BankLocalRepository bankRepository) {
        this.bankRepository = bankRepository;
    }
    @Override
    public Observable<BankSavedResult> createObservable(RequestParams requestParams) {
        BankInstallmentDomain data = (BankInstallmentDomain) requestParams.getObject(BANK_INSTALLMENT_DOMAIN);
        if(data != null) return bankRepository.save(data);

        return Observable.empty();
    }
}
