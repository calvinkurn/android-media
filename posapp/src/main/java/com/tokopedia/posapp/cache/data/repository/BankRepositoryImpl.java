package com.tokopedia.posapp.cache.data.repository;

import com.tokopedia.posapp.bank.data.factory.BankFactory;
import com.tokopedia.posapp.bank.domain.model.BankDomain;
import com.tokopedia.posapp.bank.domain.model.BankInstallmentDomain;
import com.tokopedia.posapp.bank.domain.model.BankSavedResult;

import java.util.List;

import rx.Observable;

/**
 * Created by okasurya on 9/5/17.
 */

public class BankRepositoryImpl implements BankRepository {
    private BankFactory bankFactory;

    public BankRepositoryImpl(BankFactory bankFactory) {
        this.bankFactory = bankFactory;
    }

    @Override
    public Observable<List<BankDomain>> getBankInstallment() {
        return bankFactory.cloud().getBankInstallment();
    }

    @Override
    public Observable<BankSavedResult> storeBankToCache(BankInstallmentDomain data) {
        return bankFactory.local().storeBankToCache(data);
    }
}
