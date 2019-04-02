package com.tokopedia.posapp.auth.login.domain.usecase;

import com.tokopedia.profilecompletion.domain.GetUserInfoUseCase;
import com.tokopedia.session.domain.interactor.MakeLoginUseCase;

import javax.inject.Inject;

/**
 * @author okasurya on 3/9/18.
 */

public class PosLoginEmailUseCase extends LoginEmailUseCase {
    @Inject
    public PosLoginEmailUseCase(PosGetTokenUseCase getTokenUseCase,
                                GetUserInfoUseCase getUserInfoUseCase,
                                MakeLoginUseCase makeLoginUseCase) {
        super(getTokenUseCase, getUserInfoUseCase, makeLoginUseCase);
    }
}
