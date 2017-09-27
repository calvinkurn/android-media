package com.tokopedia.posapp.di.component;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.posapp.di.module.CheckPasswordModule;
import com.tokopedia.posapp.di.module.OutletModule;
import com.tokopedia.posapp.di.module.ShopModule;
import com.tokopedia.posapp.di.scope.CheckPasswordScope;
import com.tokopedia.posapp.view.fragment.DialogPasswordFragment;

import dagger.Component;

/**
 * Created by okasurya on 9/27/17.
 */

@CheckPasswordScope
@Component(modules = CheckPasswordModule.class, dependencies = AppComponent.class)
public interface CheckPasswordComponent {
    void inject(DialogPasswordFragment fragment);
}
