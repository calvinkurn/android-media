package com.tokopedia.checkout.view.di.component;

import android.content.Context;

import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.checkout.view.di.module.MultipleAddressShipmentModule;
import com.tokopedia.checkout.view.di.scope.MultipleAddressShipmentScope;
import com.tokopedia.checkout.view.view.shipmentform.MultipleAddressShipmentFragment;

import dagger.Component;

/**
 * Created by kris on 2/5/18. Tokopedia
 */
@MultipleAddressShipmentScope
@Component(modules = MultipleAddressShipmentModule.class, dependencies = BaseAppComponent.class)
public interface MultipleAddressShipmentComponent {

    @ApplicationContext
    Context context();

    UserSession userSession();

    void inject(MultipleAddressShipmentFragment multipleAddressShipmentFragment);
}
