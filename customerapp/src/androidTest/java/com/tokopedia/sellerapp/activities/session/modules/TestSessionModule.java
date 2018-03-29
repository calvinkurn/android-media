package com.tokopedia.sellerapp.activities.session.modules;

import android.support.test.espresso.core.deps.dagger.Module;

import com.tokopedia.di.SessionModule;
import com.tokopedia.session.register.domain.interactor.registerinitial.GetFacebookCredentialUseCase;

import dagger.Provides;

import static org.mockito.Mockito.mock;

/**
 * Created by normansyahputa on 3/29/18.
 */
@Module
public class TestSessionModule extends SessionModule {

    private GetFacebookCredentialUseCase getFacebookCredentialUseCase;

    @Override
    public GetFacebookCredentialUseCase provideOverridenGetFacebookCredentialUseCase() {
        return getFacebookCredentialUseCase = mock(GetFacebookCredentialUseCase.class);
    }

    public GetFacebookCredentialUseCase getGetFacebookCredentialUseCase() {
        return getFacebookCredentialUseCase;
    }
}
