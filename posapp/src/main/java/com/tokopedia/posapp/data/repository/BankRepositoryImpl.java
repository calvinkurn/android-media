package com.tokopedia.posapp.data.repository;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.posapp.data.factory.BankFactory;
import com.tokopedia.posapp.domain.model.bank.BankInstallmentDomain;

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
    public Observable<BankInstallmentDomain> getBankInstallment(RequestParams params) {
        return bankFactory.cloud().getBankInstallment(params);
    }
}
