package com.tokopedia.posapp.data.factory;

import com.tokopedia.posapp.data.mapper.ValidatePasswordMapper;
import com.tokopedia.posapp.data.source.cloud.AccountCloudSource;
import com.tokopedia.posapp.data.source.cloud.api.AccountApi;

/**
 * Created by okasurya on 9/28/17.
 */

public class AccountFactory {
    private AccountApi accountApi;
    private ValidatePasswordMapper validatePasswordMapper;

    public AccountFactory(AccountApi accountApi,
                          ValidatePasswordMapper validatePasswordMapper) {
        this.accountApi = accountApi;
        this.validatePasswordMapper = validatePasswordMapper;
    }

    public AccountCloudSource cloud() {
        return new AccountCloudSource(accountApi, validatePasswordMapper);
    }
}
