package com.tokopedia.posapp.data.source.cloud;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.posapp.data.mapper.GetBankInstallmentMapper;
import com.tokopedia.posapp.data.pojo.BankInstallmentResponse;
import com.tokopedia.posapp.data.pojo.CreditCardBinResponse;
import com.tokopedia.posapp.data.source.cloud.api.CreditCardApi;
import com.tokopedia.posapp.domain.model.bank.BankInstallmentDomain;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;
import rx.functions.Func2;

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

    public Observable<BankInstallmentDomain> getBankInstallment() {
        return creditCardApi
                .getBankInstallment()
                .map(getBankInstallmentMapper);
    }

    public Observable<CreditCardBinDomain> getBins() {
        return creditCardApi.getBins().map(new Func1<Response<CreditCardBinResponse>, CreditCardBinDomain>() {
            @Override
            public CreditCardBinDomain call(Response<CreditCardBinResponse> creditCardBinResponseResponse) {
                return null;
            }
        });
    }
}
