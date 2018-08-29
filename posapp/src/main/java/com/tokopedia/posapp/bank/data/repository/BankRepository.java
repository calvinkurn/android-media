package com.tokopedia.posapp.bank.data.repository;

import com.tokopedia.posapp.bank.domain.model.BankDomain;
import com.tokopedia.posapp.bank.domain.model.BankInstallmentDomain;
import com.tokopedia.posapp.bank.domain.model.BankSavedResult;

import java.util.List;

import rx.Observable;

/**
 * Created by okasurya on 9/5/17.
 */

public interface BankRepository {
    Observable<List<BankDomain>> getBankInstallment();

    Observable<List<BankDomain>> getBins();

    Observable<BankSavedResult> save(BankInstallmentDomain data);
}
