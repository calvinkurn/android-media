package com.tokopedia.shop.settings.common.di;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.shop.settings.basicinfo.view.fragment.ShopEditBasicInfoFragment;
import com.tokopedia.shop.settings.basicinfo.view.fragment.ShopSettingsInfoFragment;

import dagger.Component;

/**
 * Created by zulfikarrahman on 6/21/18.
 */

@ShopSettingsScope
@Component(dependencies = BaseAppComponent.class)
public interface ShopSettingsComponent {
    void inject(ShopSettingsInfoFragment shopSettingsInfoFragment);
    void inject(ShopEditBasicInfoFragment shopEditBasicInfoFragment);
}
