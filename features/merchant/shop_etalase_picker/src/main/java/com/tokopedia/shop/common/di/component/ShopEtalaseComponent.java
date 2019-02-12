package com.tokopedia.shop.common.di.component;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.shop.common.di.scope.ShopEtalaseScope;
import com.tokopedia.shop.etalase.view.fragment.ShopEtalaseFragment;

import dagger.Component;

/**
 * Created by hendry on 18/01/18.
 */
@ShopEtalaseScope
@Component(dependencies = BaseAppComponent.class)
public interface ShopEtalaseComponent {

    void inject(ShopEtalaseFragment shopEtalaseFragment);

}
