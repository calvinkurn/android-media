package com.tokopedia.posapp.data.repository;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.posapp.domain.model.bank.BankDomain;
import com.tokopedia.posapp.domain.model.bank.BankInstallmentDomain;
import com.tokopedia.posapp.domain.model.result.BankSavedResult;

import java.util.List;

import rx.Observable;

/**
 * Created by okasurya on 9/5/17.
 */

public interface BankRepository {
    Observable<List<BankDomain>> getBankInstallment();

    Observable<BankSavedResult> storeBankToCache(BankInstallmentDomain data);
}
