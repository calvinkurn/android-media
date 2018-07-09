package com.tokopedia.posapp.bank.domain.usecase;

import com.tokopedia.posapp.bank.data.repository.BankCloudRepository;
import com.tokopedia.posapp.bank.data.repository.BankRepository;
import com.tokopedia.posapp.bank.domain.model.BankDomain;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by okasurya on 9/5/17.
 */

public class GetBankUseCase extends UseCase<List<BankDomain>> {
    BankRepository bankRepository;

    @Inject
    public GetBankUseCase(BankCloudRepository bankRepository) {
        this.bankRepository = bankRepository;
    }

    @Override
    public Observable<List<BankDomain>> createObservable(RequestParams requestParams) {
        return bankRepository.getBankInstallment();
    }
}
