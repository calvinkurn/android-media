package com.tokopedia.loginregister.login.di;

import com.tokopedia.loginregister.common.di.LoginRegisterComponent;
import com.tokopedia.loginregister.login.view.activity.LoginActivity;
import com.tokopedia.loginregister.login.view.fragment.LoginFragment;
import com.tokopedia.loginregister.login.view.fragment.LoginEmailPhoneFragment;

import dagger.Component;

/**
 * @author by nisie on 10/15/18.
 */

@LoginScope
@Component(modules = LoginModule.class, dependencies = LoginRegisterComponent.class)
public interface LoginComponent {

    public void inject(LoginActivity activity);

    public void inject(LoginFragment fragment);

    public void inject(LoginEmailPhoneFragment fragment);


}