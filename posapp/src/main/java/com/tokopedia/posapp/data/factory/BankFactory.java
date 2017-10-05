package com.tokopedia.posapp.data.factory;

import com.tokopedia.posapp.data.mapper.GetBankInstallmentMapper;
import com.tokopedia.posapp.data.source.cloud.BankCloudSource;
import com.tokopedia.posapp.data.source.cloud.api.CreditCardApi;
import com.tokopedia.posapp.data.source.local.BankLocalSource;
import com.tokopedia.posapp.database.manager.BankDbManager;

/**
 * Created by okasurya on 9/5/17.
 */

public class BankFactory {
    private CreditCardApi creditCardApi;
    private GetBankInstallmentMapper getBankInstallmentMapper;

    public BankFactory(CreditCardApi creditCardApi,
                       GetBankInstallmentMapper getBankInstallmentMapper) {
        this.creditCardApi = creditCardApi;
        this.getBankInstallmentMapper = getBankInstallmentMapper;
    }

    public BankCloudSource cloud() {
        return new BankCloudSource(creditCardApi, getBankInstallmentMapper);
    }

    public BankLocalSource local() {
        return new BankLocalSource();
    }
}
