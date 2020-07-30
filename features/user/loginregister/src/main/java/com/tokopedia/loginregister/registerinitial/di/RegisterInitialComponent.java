package com.tokopedia.loginregister.registerinitial.di;

import com.tokopedia.loginregister.common.di.LoginRegisterComponent;
import com.tokopedia.loginregister.registerinitial.view.fragment.RegisterEmailFragment;
import com.tokopedia.loginregister.registerinitial.view.fragment.RegisterInitialFragment;

import dagger.Component;

/**
 * @author by nisie on 10/25/18.
 */
@RegisterInitialScope
@Component(modules = {
        RegisterInitialModule.class,
        RegisterInitialQueryModule.class,
        RegisterInitialUseCaseModule.class,
        RegisterInitialViewModelModule.class
}, dependencies = LoginRegisterComponent.class)
public interface RegisterInitialComponent {

    public void inject(RegisterInitialFragment fragment);
    public void inject(RegisterEmailFragment fragment);

}