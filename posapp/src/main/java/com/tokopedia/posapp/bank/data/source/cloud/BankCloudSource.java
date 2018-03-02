package com.tokopedia.posapp.bank.data.source.cloud;

import com.tokopedia.posapp.PosConstants;
import com.tokopedia.posapp.bank.data.mapper.GetBankInstallmentMapper;
import com.tokopedia.posapp.bank.data.source.cloud.api.BankApi;
import com.tokopedia.posapp.bank.domain.model.BankDomain;

import java.util.List;

import rx.Observable;

/**
 * Created by okasurya on 9/5/17.
 */

public class BankCloudSource {
    BankApi bankApi;
    GetBankInstallmentMapper getBankInstallmentMapper;

    public BankCloudSource(BankApi bankApi,
                           GetBankInstallmentMapper getBankMapper) {
        this.bankApi = bankApi;
        this.getBankInstallmentMapper = getBankMapper;
    }

    public Observable<List<BankDomain>> getBankInstallment() {
        return Observable.zip(
                bankApi.getBankInstallment(PosConstants.Payment.MERCHANT_CODE, PosConstants.Payment.PROFILE_CODE),
                bankApi.getBins(),
                getBankInstallmentMapper
        );
    }
}
