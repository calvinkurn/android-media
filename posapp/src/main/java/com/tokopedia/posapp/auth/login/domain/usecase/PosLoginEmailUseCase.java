package com.tokopedia.posapp.auth.login.domain.usecase;

import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.profilecompletion.domain.GetUserInfoUseCase;
import com.tokopedia.session.domain.interactor.MakeLoginUseCase;
import com.tokopedia.session.login.loginemail.domain.interactor.LoginEmailUseCase;

import javax.inject.Inject;

/**
 * @author okasurya on 3/9/18.
 */

public class PosLoginEmailUseCase extends LoginEmailUseCase {
    @Inject
    public PosLoginEmailUseCase(ThreadExecutor threadExecutor,
                                PostExecutionThread postExecutionThread,
                                PosGetTokenUseCase getTokenUseCase,
                                GetUserInfoUseCase getUserInfoUseCase,
                                MakeLoginUseCase makeLoginUseCase,
                                SessionHandler sessionHandler) {
        super(threadExecutor, postExecutionThread, getTokenUseCase,
                getUserInfoUseCase, makeLoginUseCase, sessionHandler);
    }
}
