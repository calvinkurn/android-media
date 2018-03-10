package com.tokopedia.posapp.auth.login.di.component;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.di.SessionComponent;
import com.tokopedia.di.SessionScope;
import com.tokopedia.posapp.auth.login.di.module.PosLoginModule;
import com.tokopedia.posapp.auth.login.view.fragment.PosLoginFragment;

import dagger.Component;

/**
 * @author okasurya on 3/6/18.
 */
@SessionScope
@Component(modules = PosLoginModule.class, dependencies = AppComponent.class)
public interface PosLoginComponent {
    void inject(PosLoginFragment fragment);
}
