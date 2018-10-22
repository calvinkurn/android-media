package com.tokopedia.loginregister.activation.di;

import com.tokopedia.loginregister.activation.view.activity.ActivationActivity;
import com.tokopedia.loginregister.activation.view.fragment.ActivationFragment;
import com.tokopedia.loginregister.activation.view.fragment.ChangeEmailFragment;
import com.tokopedia.loginregister.common.di.LoginRegisterComponent;

import dagger.Component;

/**
 * @author by nisie on 10/22/18.
 */
@ActivationScope
@Component(modules = ActivationModule.class, dependencies = LoginRegisterComponent.class)
public interface ActivationComponent {

    void inject(ActivationActivity activity);

    void inject(ActivationFragment fragment);

    void inject(ChangeEmailFragment fragment);

}