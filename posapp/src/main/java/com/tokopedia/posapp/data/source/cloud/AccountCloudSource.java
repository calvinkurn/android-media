package com.tokopedia.posapp.data.source.cloud;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.posapp.data.mapper.ValidatePasswordMapper;
import com.tokopedia.posapp.data.source.cloud.api.AccountApi;
import com.tokopedia.posapp.domain.model.CheckPasswordDomain;

import rx.Observable;

/**
 * Created by okasurya on 9/28/17.
 */

public class AccountCloudSource {
    private AccountApi accountApi;
    private ValidatePasswordMapper validatePasswordMapper;

    public AccountCloudSource(AccountApi accountApi,
                              ValidatePasswordMapper validatePasswordMapper) {
        this.accountApi = accountApi;
        this.validatePasswordMapper = validatePasswordMapper;
    }

    public Observable<CheckPasswordDomain> validatePassword(RequestParams params) {
        return accountApi.validatePassword(params.getParamsAllValueInString()).map(validatePasswordMapper);
    }
}
