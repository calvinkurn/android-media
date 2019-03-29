package com.tokopedia.posapp.auth.login.di.module;

import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.di.SessionModule;
import com.tokopedia.di.SessionScope;
import com.tokopedia.posapp.auth.login.domain.usecase.PosLoginEmailUseCase;
import com.tokopedia.posapp.auth.login.view.PosLogin;
import com.tokopedia.posapp.auth.login.view.presenter.PosLoginPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * @author okasurya on 3/6/18.
 */

@SessionScope
@Module(includes = SessionModule.class)
public class PosLoginModule {
    @SessionScope
    @Provides
    PosLogin.Presenter providePosLoginPresenter(PosLoginEmailUseCase posLoginEmailUseCase,
                                                SessionHandler sessionHandler) {
        return new PosLoginPresenter(posLoginEmailUseCase, sessionHandler);
    }
}
