package com.tokopedia.abstraction.common.di.module;

import android.content.Context;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.di.module.net.NetModule;

import dagger.Module;

import static org.mockito.Mockito.mock;

@Module(includes = {NetModule.class})
public class TestAppModule extends AppModule {
    public static UserSession userSession;

    public TestAppModule(Context context) {
        super(context);
    }

    @Override
    protected UserSession realProvideUserSession(AbstractionRouter abstractionRouter) {
        return userSession == null ? userSession = mock(UserSession.class) : userSession;
    }
}
