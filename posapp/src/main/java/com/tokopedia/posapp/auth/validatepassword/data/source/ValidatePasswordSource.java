package com.tokopedia.posapp.auth.validatepassword.data.source;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.posapp.auth.validatepassword.data.mapper.ValidatePasswordMapper;
import com.tokopedia.posapp.auth.AccountApi;
import com.tokopedia.posapp.auth.validatepassword.domain.model.ValidatePasswordDomain;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by okasurya on 9/28/17.
 */

public class ValidatePasswordSource {
    private AccountApi accountApi;
    private ValidatePasswordMapper validatePasswordMapper;

    public ValidatePasswordSource(AccountApi accountApi,
                                  ValidatePasswordMapper validatePasswordMapper) {
        this.accountApi = accountApi;
        this.validatePasswordMapper = validatePasswordMapper;
    }

    public Observable<ValidatePasswordDomain> validatePassword(RequestParams params) {
        return accountApi.validatePassword(params.getParamsAllValueInString()).map(validatePasswordMapper);
    }
}
