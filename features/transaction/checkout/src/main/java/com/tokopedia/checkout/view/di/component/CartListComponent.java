package com.tokopedia.checkout.view.di.component;

import android.content.Context;

import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.checkout.view.di.module.CartListModule;
import com.tokopedia.checkout.view.di.scope.CartListScope;
import com.tokopedia.checkout.view.view.cartlist.CartFragment;

import dagger.Component;

/**
 * @author anggaprasetiyo on 18/01/18.
 */
@CartListScope
@Component(modules = CartListModule.class, dependencies = BaseAppComponent.class)
public interface CartListComponent {

    @ApplicationContext
    Context context();

    UserSession userSession();

    void inject(CartFragment cartFragment);
}
