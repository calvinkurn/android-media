package com.tokopedia.posapp.auth.validatepassword.data.repository;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.posapp.auth.validatepassword.domain.model.ValidatePasswordDomain;

import rx.Observable;

/**
 * Created by okasurya on 9/28/17.
 */

public interface ValidatePasswordRepository {
    Observable<ValidatePasswordDomain> validatePassword(RequestParams requestParams);
}
