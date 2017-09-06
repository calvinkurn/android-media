package com.tokopedia.posapp.data.source.cloud;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.posapp.data.mapper.GetBankInstallmentMapper;
import com.tokopedia.posapp.data.source.cloud.api.CreditCardApi;
import com.tokopedia.posapp.domain.model.bank.BankInstallmentDomain;

import rx.Observable;

/**
 * Created by okasurya on 9/5/17.
 */

public class BankCloudSource {
    CreditCardApi creditCardApi;
    GetBankInstallmentMapper getBankInstallmentMapper;

    public BankCloudSource(CreditCardApi creditCardApi,
                           GetBankInstallmentMapper getBankInstallmentMapper) {
        this.creditCardApi = creditCardApi;
        this.getBankInstallmentMapper = getBankInstallmentMapper;
    }

    public Observable<BankInstallmentDomain> getBankInstallment(RequestParams params) {
        return creditCardApi.getBankInstallment(params.getParamsAllValueInString()).map(getBankInstallmentMapper);
    }
}
