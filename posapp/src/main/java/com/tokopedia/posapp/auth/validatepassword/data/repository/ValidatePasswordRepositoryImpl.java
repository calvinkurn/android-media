package com.tokopedia.posapp.auth.validatepassword.data.repository;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.posapp.auth.validatepassword.data.factory.ValidatePasswordFactory;
import com.tokopedia.posapp.auth.validatepassword.domain.model.ValidatePasswordDomain;

import rx.Observable;

/**
 * Created by okasurya on 9/28/17.
 */

public class ValidatePasswordRepositoryImpl implements ValidatePasswordRepository {
    private ValidatePasswordFactory validatePasswordFactory;

    public ValidatePasswordRepositoryImpl(ValidatePasswordFactory validatePasswordFactory) {
        this.validatePasswordFactory = validatePasswordFactory;
    }

    @Override
    public Observable<ValidatePasswordDomain> validatePassword(RequestParams requestParams) {
        return validatePasswordFactory.cloud().validatePassword(requestParams);
    }
}
