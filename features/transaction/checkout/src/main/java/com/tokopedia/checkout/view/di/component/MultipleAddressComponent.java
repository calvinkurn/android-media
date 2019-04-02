package com.tokopedia.checkout.view.di.component;

import com.tokopedia.checkout.view.di.module.MultipleAddressModule;
import com.tokopedia.checkout.view.di.scope.MultipleAddressScope;
import com.tokopedia.checkout.view.feature.multipleaddressform.MultipleAddressFragment;

import dagger.Component;

/**
 * Created by kris on 2/5/18. Tokopedia
 */

@MultipleAddressScope
@Component(modules = MultipleAddressModule.class, dependencies = CartComponent.class)
public interface MultipleAddressComponent {

    void inject(MultipleAddressFragment multipleAddressFragment);
}
