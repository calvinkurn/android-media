package com.tokopedia.posapp.di.component;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.posapp.data.factory.CartFactory;
import com.tokopedia.posapp.di.module.CartModule;
import com.tokopedia.posapp.di.scope.CartScope;

import dagger.Component;

/**
 * Created by Herdi_WORK on 09.10.17.
 */

@CartScope
@Component(modules = CartModule.class, dependencies = AppComponent.class)
public interface CartComponent {
     CartFactory provideCartFactory();
}
