package com.tokopedia.posapp.bank.data.repository;

import com.tokopedia.posapp.bank.data.source.local.BankLocalSource;
import com.tokopedia.posapp.bank.domain.model.BankDomain;
import com.tokopedia.posapp.bank.domain.model.BankInstallmentDomain;
import com.tokopedia.posapp.bank.domain.model.BankSavedResult;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author okasurya on 4/20/18.
 */

public class BankLocalRepository implements BankRepository {
    private BankLocalSource bankLocalSource;

    @Inject
    BankLocalRepository(BankLocalSource bankLocalSource) {
        this.bankLocalSource = bankLocalSource;
    }

    @Override
    public Observable<List<BankDomain>> getBankInstallment() {
        return bankLocalSource.getAllBank();
    }

    @Override
    public Observable<List<BankDomain>> getBins() {
        return null;
    }

    @Override
    public Observable<BankSavedResult> save(BankInstallmentDomain data) {
        return bankLocalSource.storeBankToCache(data);
    }
}
