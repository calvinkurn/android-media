package com.tokopedia.posapp.auth.validatepassword.data.factory;

import com.tokopedia.posapp.auth.validatepassword.data.mapper.ValidatePasswordMapper;
import com.tokopedia.posapp.auth.validatepassword.data.source.ValidatePasswordSource;
import com.tokopedia.posapp.auth.AccountApi;

import javax.inject.Inject;

/**
 * Created by okasurya on 9/28/17.
 */

public class ValidatePasswordFactory {
    private AccountApi accountApi;
    private ValidatePasswordMapper validatePasswordMapper;

    @Inject
    public ValidatePasswordFactory(AccountApi accountApi,
                                   ValidatePasswordMapper validatePasswordMapper) {
        this.accountApi = accountApi;
        this.validatePasswordMapper = validatePasswordMapper;
    }

    public ValidatePasswordSource cloud() {
        return new ValidatePasswordSource(accountApi, validatePasswordMapper);
    }
}
