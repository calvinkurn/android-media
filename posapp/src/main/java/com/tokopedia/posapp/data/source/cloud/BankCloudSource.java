package com.tokopedia.posapp.data.source.cloud;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.posapp.data.mapper.GetBankInstallmentMapper;
import com.tokopedia.posapp.data.pojo.BankInstallmentResponse;
import com.tokopedia.posapp.data.source.cloud.api.CreditCardApi;
import com.tokopedia.posapp.domain.model.bank.BankInstallmentDomain;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

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
        return creditCardApi
                .getBankInstallment(params.getParamsAllValueInString())
                .map(getBankInstallmentMapper);
    }

    public Observable<CreditCardBinDomain> getBins(RequestParams params) {
        return creditCardApi.getBins(params.getParamsAllValueInString()).map(new Func1<Response<BankInstallmentResponse>, CreditCardBinDomain>() {
            @Override
            public CreditCardBinDomain call(Response<BankInstallmentResponse> bankInstallmentResponseResponse) {
                return null;
            }
        });
    }
}
