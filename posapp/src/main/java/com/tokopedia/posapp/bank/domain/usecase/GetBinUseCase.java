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
 * @author okasurya on 4/27/18.
 */

public class GetBinUseCase extends UseCase<List<BankDomain>> {
    BankRepository bankRepository;

    @Inject
    GetBinUseCase(BankCloudRepository bankRepository) {
        this.bankRepository = bankRepository;
    }

    @Override
    public Observable<List<BankDomain>> createObservable(RequestParams requestParams) {
        return bankRepository.getBins();
    }
}
