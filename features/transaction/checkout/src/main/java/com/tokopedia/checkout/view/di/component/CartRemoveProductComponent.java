package com.tokopedia.checkout.view.di.component;

import android.content.Context;

import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.checkout.view.di.module.CartRemoveProductModule;
import com.tokopedia.checkout.view.di.scope.CartRemoveProductScope;
import com.tokopedia.checkout.view.view.cartlist.CartRemoveProductFragment;

import dagger.Component;

/**
 * @author Aghny A. Putra on 31/01/18.
 */
@CartRemoveProductScope
@Component(modules = CartRemoveProductModule.class, dependencies = BaseAppComponent.class)
public interface CartRemoveProductComponent {
    @ApplicationContext
    Context context();

    UserSession userSession();

    void inject(CartRemoveProductFragment cartRemoveProductFragment);
}
