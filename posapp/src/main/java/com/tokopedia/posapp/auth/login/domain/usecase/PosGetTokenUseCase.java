package com.tokopedia.posapp.auth.login.domain.usecase;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.posapp.auth.login.data.source.PosGetTokenDataSource;
import com.tokopedia.session.domain.interactor.GetTokenUseCase;
import com.tokopedia.session.domain.pojo.token.TokenViewModel;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author okasurya on 3/9/18.
 */

public class PosGetTokenUseCase extends GetTokenUseCase {

    private static final String SCOPE = "scope";
    public static final String O2O_DELEGATE = "o2o_delegate";

    @Inject
    public PosGetTokenUseCase(ThreadExecutor threadExecutor,
                              PostExecutionThread postExecutionThread,
                              PosGetTokenDataSource repository) {
        super(threadExecutor, postExecutionThread, repository);
    }

    @Override
    public Observable<TokenViewModel> createObservable(RequestParams requestParams) {
        requestParams.putString(SCOPE, O2O_DELEGATE);
        return super.createObservable(requestParams);
    }
}
