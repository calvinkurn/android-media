package com.tokopedia.checkout.view.di.component;

import android.content.Context;

import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.checkout.view.di.module.SingleAddressShipmentModule;
import com.tokopedia.checkout.view.di.scope.SingleAddressShipmentScope;
import com.tokopedia.checkout.view.view.shipmentform.SingleAddressShipmentFragment;

import dagger.Component;

/**
 * @author Aghny A. Putra on 31/01/18.
 */

@SingleAddressShipmentScope
@Component(modules = SingleAddressShipmentModule.class, dependencies = BaseAppComponent.class)
public interface SingleAddressShipmentComponent {

    @ApplicationContext
    Context context();

    UserSession userSession();

    void inject(SingleAddressShipmentFragment singleAddressShipmentFragment);

}
