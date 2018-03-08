package com.tokopedia.posapp.auth.login.di.component;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.di.SessionScope;
import com.tokopedia.posapp.auth.login.di.module.PosLoginModule;

import dagger.Component;

/**
 * @author okasurya on 3/6/18.
 */
@SessionScope
@Component(modules = PosLoginModule.class, dependencies = BaseAppComponent.class)
public interface PosLoginComponent {
}
