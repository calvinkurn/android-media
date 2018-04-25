package com.tokopedia.checkout.view.di.component;

import android.content.Context;

import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.checkout.view.di.module.MultipleAddressModule;
import com.tokopedia.checkout.view.di.scope.MultipleAddressScope;
import com.tokopedia.checkout.view.view.multipleaddressform.MultipleAddressFragment;

import dagger.Component;

/**
 * Created by kris on 2/5/18. Tokopedia
 */

@MultipleAddressScope
@Component(modules = MultipleAddressModule.class, dependencies = BaseAppComponent.class)
public interface MultipleAddressComponent {

    @ApplicationContext
    Context context();

    UserSession userSession();

    void inject(MultipleAddressFragment multipleAddressFragment);
}
