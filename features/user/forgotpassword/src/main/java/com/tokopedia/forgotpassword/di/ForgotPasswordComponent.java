package com.tokopedia.forgotpassword.di;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.forgotpassword.view.fragment.ForgotPasswordFragment;

import dagger.Component;

/**
 * @author by nisie on 9/25/18.
 */

@ForgotPasswordScope
@Component(modules = ForgotPasswordModule.class, dependencies = BaseAppComponent.class)
public interface ForgotPasswordComponent {

    void inject(ForgotPasswordFragment fragment);
}
