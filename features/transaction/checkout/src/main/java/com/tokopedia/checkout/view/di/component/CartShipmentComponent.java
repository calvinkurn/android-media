package com.tokopedia.checkout.view.di.component;

import android.content.Context;

import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.checkout.view.di.module.CartShipmentModule;
import com.tokopedia.checkout.view.di.scope.CartShipmentActivityScope;
import com.tokopedia.checkout.view.view.shipmentform.CartShipmentActivity;

import dagger.Component;

/**
 * @author anggaprasetiyo on 05/03/18.
 */
@CartShipmentActivityScope
@Component(modules = CartShipmentModule.class, dependencies = BaseAppComponent.class)
public interface CartShipmentComponent {

    @ApplicationContext
    Context context();

    UserSession userSession();

    void inject(CartShipmentActivity cartShipmentActivity);
}
