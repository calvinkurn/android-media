package com.tokopedia.posapp.auth.validatepassword.di;

import com.tokopedia.posapp.auth.di.AuthComponent;
import com.tokopedia.posapp.auth.validatepassword.view.fragment.ValidatePasswordFragment;

import dagger.Component;

/**
 * Created by okasurya on 9/27/17.
 */

@ValidatePasswordScope
@Component(modules = ValidatePasswordModule.class, dependencies = AuthComponent.class)
public interface ValidatePasswordComponent {
    void inject(ValidatePasswordFragment fragment);
}
