package com.tokopedia.posapp.data.factory;

import com.tokopedia.posapp.data.mapper.GetBankInstallmentMapper;
import com.tokopedia.posapp.data.source.cloud.BankCloudSource;
import com.tokopedia.posapp.data.source.cloud.api.CreditCardApi;

/**
 * Created by okasurya on 9/5/17.
 */

public class BankFactory {
    CreditCardApi creditCardApi;
    GetBankInstallmentMapper getBankInstallmentMapper;

    public BankFactory(CreditCardApi creditCardApi,
                       GetBankInstallmentMapper getBankInstallmentMapper) {
        this.creditCardApi = creditCardApi;
        this.getBankInstallmentMapper = getBankInstallmentMapper;
    }

    public BankCloudSource cloud() {
        return new BankCloudSource(creditCardApi, getBankInstallmentMapper);
    }
}
