package com.tokopedia.sessioncommon.domain.usecase;

import com.tokopedia.sessioncommon.data.model.LoginEmailDomain;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import rx.Observable;

/**
 * @author by nisie on 10/12/18.
 */
public class LoginEmailUseCase extends UseCase<LoginEmailDomain> {

    @Override
    public Observable<LoginEmailDomain> createObservable(RequestParams requestParams) {
        return null;
    }
}
