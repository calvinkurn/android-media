package com.tokopedia.posapp.data.mapper;

import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.posapp.domain.model.bank.BankInstallmentDomain;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by okasurya on 9/5/17.
 */

public class GetBankInstallmentMapper implements Func1<Response<TkpdResponse>, BankInstallmentDomain> {
    @Override
    public BankInstallmentDomain call(Response<TkpdResponse> response) {
        return null; // TODO: 9/5/17 map the result
    }
}
