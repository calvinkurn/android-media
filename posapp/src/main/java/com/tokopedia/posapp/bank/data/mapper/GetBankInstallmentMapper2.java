package com.tokopedia.posapp.bank.data.mapper;

import com.tokopedia.posapp.bank.domain.model.BankDomain;

import java.util.List;

import javax.inject.Inject;

import retrofit2.Response;
import rx.functions.Func2;

/**
 * @author okasurya on 4/27/18.
 */

public class GetBankInstallmentMapper2 implements Func2<Response<String>, Response<String>, List<BankDomain>> {
    @Inject
    GetBankInstallmentMapper2() {}

    @Override
    public List<BankDomain> call(Response<String> stringResponse, Response<String> stringResponse2) {
        return null;
    }
}
