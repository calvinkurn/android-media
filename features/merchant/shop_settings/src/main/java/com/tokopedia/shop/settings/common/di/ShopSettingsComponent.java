package com.tokopedia.shop.settings.common.di;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.shop.settings.address.view.ShopSettingAddressAddEditFragment;
import com.tokopedia.shop.settings.address.view.ShopSettingAddressFragment;
import com.tokopedia.shop.settings.basicinfo.view.activity.ShopEditBasicInfoActivity;
import com.tokopedia.shop.settings.basicinfo.view.activity.ShopEditScheduleActivity;
import com.tokopedia.shop.settings.basicinfo.view.fragment.ShopSettingsInfoFragment;
import com.tokopedia.shop.settings.etalase.view.fragment.ShopSettingsEtalaseAddEditFragment;
import com.tokopedia.shop.settings.etalase.view.fragment.ShopSettingsEtalaseListFragment;
import com.tokopedia.shop.settings.etalase.view.fragment.ShopSettingsEtalaseReorderFragment;
import com.tokopedia.shop.settings.notes.view.ShopSettingsNotesListFragment;
import com.tokopedia.shop.settings.notes.view.ShopSettingsNotesReorderFragment;
import com.tokopedia.shop.settings.notes.view.fragment.ShopSettingsNotesAddEditFragment;

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
    void inject(ShopSettingsNotesListFragment shopSettingsNotesFragment);
    void inject(ShopSettingsNotesReorderFragment shopSettingsNotesFragment);

    void inject(ShopSettingAddressFragment fragment);
    void inject(ShopSettingAddressAddEditFragment fragment);
    void inject(ShopSettingsNotesAddEditFragment fragment);

    void inject(ShopSettingsEtalaseAddEditFragment fragment);
    void inject(ShopSettingsEtalaseListFragment fragment);
    void inject(ShopSettingsEtalaseReorderFragment fragment);
}
