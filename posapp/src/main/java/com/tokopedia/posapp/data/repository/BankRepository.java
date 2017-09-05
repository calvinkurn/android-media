package com.tokopedia.posapp.data.repository;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.posapp.domain.model.bank.BankInstallmentDomain;

import rx.Observable;

/**
 * Created by okasurya on 9/5/17.
 */

public interface BankRepository {
    Observable<BankInstallmentDomain> getBankInstallment(RequestParams params);
}
