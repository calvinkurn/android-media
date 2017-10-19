package com.tokopedia.posapp.di.component;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.posapp.di.module.ValidatePasswordModule;
import com.tokopedia.posapp.di.scope.ValidatePasswordScope;
import com.tokopedia.posapp.view.widget.DialogPasswordFragment;

import dagger.Component;

/**
 * Created by okasurya on 9/27/17.
 */

@ValidatePasswordScope
@Component(modules = ValidatePasswordModule.class, dependencies = AppComponent.class)
public interface ValidatePasswordComponent {
    void inject(DialogPasswordFragment fragment);
}
