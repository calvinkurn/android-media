package com.tokopedia.posapp.data.repository;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.posapp.domain.model.CheckPasswordDomain;

import rx.Observable;

/**
 * Created by okasurya on 9/28/17.
 */

public interface AccountRepository {
    Observable<CheckPasswordDomain> validatePassword(RequestParams requestParams);
}
