package com.tokopedia.purchase_platform.common.di.component;

import com.tokopedia.purchase_platform.common.di.module.MultipleAddressModule;
import com.tokopedia.purchase_platform.common.di.scope.MultipleAddressScope;
import com.tokopedia.purchase_platform.features.checkout.subfeature.multiple_address.view.MultipleAddressFragment;

import dagger.Component;

/**
 * Created by kris on 2/5/18. Tokopedia
 */

@MultipleAddressScope
@Component(modules = MultipleAddressModule.class, dependencies = CartComponent.class)
public interface MultipleAddressComponent {

    void inject(MultipleAddressFragment multipleAddressFragment);
}
