package com.tokopedia.posapp.data.source.cloud;

import com.tokopedia.posapp.PosConstants;
import com.tokopedia.posapp.data.mapper.GetBankInstallmentMapper;
import com.tokopedia.posapp.data.source.cloud.api.GatewayBankApi;
import com.tokopedia.posapp.domain.model.bank.BankDomain;

import java.util.List;

import rx.Observable;

/**
 * Created by okasurya on 9/5/17.
 */

public class BankCloudSource {
    GatewayBankApi gatewayBankApi;
    GetBankInstallmentMapper getBankInstallmentMapper;

    public BankCloudSource(GatewayBankApi gatewayBankApi,
                           GetBankInstallmentMapper getBankMapper) {
        this.gatewayBankApi = gatewayBankApi;
        this.getBankInstallmentMapper = getBankMapper;
    }

    public Observable<List<BankDomain>> getBankInstallment() {
        return Observable.zip(
                gatewayBankApi.getBankInstallment(PosConstants.Payment.MERCHANT_CODE, PosConstants.Payment.PROFILE_CODE),
                gatewayBankApi.getBins(),
                getBankInstallmentMapper
        );
    }
}
