package com.tokopedia.checkout.view.di.component;

import com.tokopedia.checkout.view.di.module.CartAddressChoiceModule;
import com.tokopedia.checkout.view.di.scope.CartAddressChoiceScope;
import com.tokopedia.checkout.view.view.addressoptions.CartAddressChoiceFragment;

import dagger.Component;

/**
 * @author Aghny A. Putra on 27/02/18
 */

@CartAddressChoiceScope
@Component(modules = CartAddressChoiceModule.class)
public interface CartAddressChoiceComponent {

    void inject(CartAddressChoiceFragment cartAddressChoiceFragment);

}
