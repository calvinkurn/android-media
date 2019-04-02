package com.tokopedia.posapp.bank.data.repository;

import com.tokopedia.posapp.bank.data.pojo.CCBinResponse;
import com.tokopedia.posapp.bank.data.source.cloud.BankCloudSource;
import com.tokopedia.posapp.bank.domain.model.BankDomain;
import com.tokopedia.posapp.bank.domain.model.BankInstallmentDomain;
import com.tokopedia.posapp.bank.domain.model.BankSavedResult;
import com.tokopedia.posapp.base.data.pojo.ListResponse;
import com.tokopedia.posapp.base.data.pojo.PosResponse;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by okasurya on 9/5/17.
 */

public class BankCloudRepository implements BankRepository {
    private BankCloudSource bankCloudSource;

    @Inject
    public BankCloudRepository(BankCloudSource bankCloudSource) {
        this.bankCloudSource = bankCloudSource;
    }

    @Override
    public Observable<List<BankDomain>> getBankInstallment() {
        return bankCloudSource.getBankInstallment();
    }

    @Override
    public Observable<List<BankDomain>> getBins() {
        return bankCloudSource.getBins();
    }

    @Override
    public Observable<BankSavedResult> save(BankInstallmentDomain data) {
        return Observable.error(new RuntimeException("Unimplemented method"));
    }
}
