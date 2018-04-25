package com.tokopedia.tkpd.transaction;

import android.content.Context;

import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.checkout.data.apiservice.CartApi;
import com.tokopedia.checkout.view.di.module.DataModule;
import com.tokopedia.checkout.view.di.scope.CartListScope;
import com.tokopedia.tkpd.ConsumerRouterApplication;

import dagger.Component;

/**
 * @author anggaprasetiyo on 24/04/18.
 */
@CartListScope
@Component(modules = DataModule.class, dependencies = BaseAppComponent.class)
public interface CartApiServiceComponent {
    @ApplicationContext
    Context context();

    UserSession userSession();

    void inject(CartApiServiceComponentInjector cartApiServiceComponentInjector);
}
