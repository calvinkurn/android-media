package com.tokopedia.posapp.bank.data.factory;

import com.tokopedia.posapp.bank.data.source.cloud.api.BankApi;
import com.tokopedia.posapp.bank.data.mapper.GetBankInstallmentMapper;
import com.tokopedia.posapp.bank.data.source.cloud.BankCloudSource;
import com.tokopedia.posapp.bank.data.source.local.BankLocalSource;

/**
 * Created by okasurya on 9/5/17.
 * Deprecated because of unnecessary layer
 */
@Deprecated
public class BankFactory {
    private BankApi bankApi;
    private GetBankInstallmentMapper getBankInstallmentMapper;

    public BankFactory(BankApi bankApi,
                       GetBankInstallmentMapper getBankInstallmentMapper) {
        this.bankApi = bankApi;
        this.getBankInstallmentMapper = getBankInstallmentMapper;
    }

    public BankCloudSource cloud() {
        return new BankCloudSource(bankApi, getBankInstallmentMapper);
    }

    public BankLocalSource local() {
        return new BankLocalSource();
    }
}
