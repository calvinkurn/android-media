package com.tokopedia.loginregister.welcomepage.di;

import com.tokopedia.loginregister.common.di.LoginRegisterComponent;
import com.tokopedia.loginregister.registerinitial.di.RegisterInitialScope;
import com.tokopedia.loginregister.welcomepage.WelcomePageFragment;

import dagger.Component;

/**
 * @author by nisie on 10/25/18.
 */
@WelcomePageScope
@Component(modules = WelcomePageModule.class, dependencies = LoginRegisterComponent.class)
public interface WelcomePageComponent {

    public void inject(WelcomePageFragment fragment);

}