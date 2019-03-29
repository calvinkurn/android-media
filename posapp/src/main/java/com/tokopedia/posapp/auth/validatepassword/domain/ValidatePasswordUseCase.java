package com.tokopedia.posapp.auth.validatepassword.domain;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.posapp.auth.validatepassword.data.repository.ValidatePasswordRepository;
import com.tokopedia.posapp.auth.validatepassword.domain.model.ValidatePasswordDomain;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by okasurya on 9/27/17.
 */

public class ValidatePasswordUseCase extends UseCase<ValidatePasswordDomain> {
    private ValidatePasswordRepository validatePasswordRepository;

    @Inject
    public ValidatePasswordUseCase(ThreadExecutor threadExecutor,
                                   PostExecutionThread postExecutionThread,
                                   ValidatePasswordRepository validatePasswordRepository) {
        super(threadExecutor, postExecutionThread);
        this.validatePasswordRepository = validatePasswordRepository;
    }

    @Override
    public Observable<ValidatePasswordDomain> createObservable(RequestParams requestParams) {
        return validatePasswordRepository.validatePassword(requestParams);
    }
}
