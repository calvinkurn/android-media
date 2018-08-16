package com.tokopedia.shop.settings.common.di;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.shop.settings.basicinfo.view.activity.ShopEditBasicInfoActivity;
import com.tokopedia.shop.settings.basicinfo.view.activity.ShopEditScheduleActivity;
import com.tokopedia.shop.settings.basicinfo.view.fragment.ShopSettingsInfoFragment;
import com.tokopedia.shop.settings.notes.view.ShopSettingsNotesFragment;

import dagger.Component;

/**
 * Created by zulfikarrahman on 6/21/18.
 */

@ShopSettingsScope
@Component(modules = ShopSettingsModule.class, dependencies = BaseAppComponent.class)
public interface ShopSettingsComponent {
    void inject(ShopSettingsInfoFragment shopSettingsInfoFragment);
    void inject(ShopEditBasicInfoActivity shopEditBasicInfoActivity);
    void inject(ShopEditScheduleActivity shopEditScheduleActivity);
    void inject(ShopSettingsNotesFragment shopSettingsNotesFragment);
}
