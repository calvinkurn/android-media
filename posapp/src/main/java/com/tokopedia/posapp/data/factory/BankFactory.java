package com.tokopedia.posapp.data.factory;

import com.tokopedia.posapp.data.mapper.GetBankInstallmentMapper;
import com.tokopedia.posapp.data.source.cloud.BankCloudSource;
import com.tokopedia.posapp.data.source.cloud.api.GatewayBankApi;
import com.tokopedia.posapp.data.source.local.BankLocalSource;

/**
 * Created by okasurya on 9/5/17.
 */

public class BankFactory {
    private GatewayBankApi gatewayBankApi;
    private GetBankInstallmentMapper getBankInstallmentMapper;

    public BankFactory(GatewayBankApi gatewayBankApi,
                       GetBankInstallmentMapper getBankInstallmentMapper) {
        this.gatewayBankApi = gatewayBankApi;
        this.getBankInstallmentMapper = getBankInstallmentMapper;
    }

    public BankCloudSource cloud() {
        return new BankCloudSource(gatewayBankApi, getBankInstallmentMapper);
    }

    public BankLocalSource local() {
        return new BankLocalSource();
    }
}
