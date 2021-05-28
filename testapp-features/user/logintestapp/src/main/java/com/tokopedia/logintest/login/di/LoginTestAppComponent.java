package com.tokopedia.logintest.login.di;

import com.tokopedia.logintest.common.di.LoginRegisterTestAppComponent;
import com.tokopedia.logintest.login.view.activity.LoginTestAppActivity;
import com.tokopedia.logintest.login.view.fragment.LoginTestAppFragment;

import dagger.Component;

/**
 * @author by nisie on 10/15/18.
 */

@LoginTestAppScope
@Component(modules = {
        LoginTestAppModule.class,
        LoginTestAppQueryModule.class,
        LoginTestAppUseCaseModule.class
}, dependencies = LoginRegisterTestAppComponent.class)
public interface LoginTestAppComponent {

    public void inject(LoginTestAppActivity activity);

    public void inject(LoginTestAppFragment fragment);


}