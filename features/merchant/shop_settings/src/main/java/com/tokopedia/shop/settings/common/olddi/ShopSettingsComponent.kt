package com.tokopedia.shop.settings.common.olddi

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
//import com.tokopedia.shop.settings.address.view.ShopSettingAddressAddEditFragment
//import com.tokopedia.shop.settings.address.view.ShopSettingAddressFragment
import com.tokopedia.shop.settings.basicinfo.oldview.activity.ShopEditBasicInfoActivity
import com.tokopedia.shop.settings.basicinfo.oldview.activity.ShopEditScheduleActivity
import com.tokopedia.shop.settings.basicinfo.oldview.fragment.ShopSettingsInfoFragment
//import com.tokopedia.shop.settings.etalase.view.fragment.ShopSettingsEtalaseAddEditFragment
//import com.tokopedia.shop.settings.etalase.view.fragment.ShopSettingsEtalaseListFragment
//import com.tokopedia.shop.settings.etalase.view.fragment.ShopSettingsEtalaseReorderFragment
//import com.tokopedia.shop.settings.notes.view.fragment.ShopSettingsNotesAddEditFragment
//import com.tokopedia.shop.settings.notes.view.fragment.ShopSettingsNotesListFragment
//import com.tokopedia.shop.settings.notes.view.fragment.ShopSettingsNotesReorderFragment
import dagger.Component

/**
 * Created by zulfikarrahman on 6/21/18.
 */

@ShopSettingsScope
@Component(modules = arrayOf(ShopSettingsModule::class), dependencies = arrayOf(BaseAppComponent::class))
interface ShopSettingsComponent {
    fun inject(shopSettingsInfoFragment: ShopSettingsInfoFragment)
    fun inject(shopEditBasicInfoActivity: ShopEditBasicInfoActivity)
    fun inject(shopEditScheduleActivity: ShopEditScheduleActivity)
//    fun inject(shopSettingsNotesFragment: ShopSettingsNotesListFragment)
//    fun inject(shopSettingsNotesFragment: ShopSettingsNotesReorderFragment)
//
//    fun inject(fragment: ShopSettingAddressFragment)
//    fun inject(fragment: ShopSettingAddressAddEditFragment)
//    fun inject(fragment: ShopSettingsNotesAddEditFragment)
//
//    fun inject(fragment: ShopSettingsEtalaseAddEditFragment)
//    fun inject(fragment: ShopSettingsEtalaseListFragment)
//    fun inject(fragment: ShopSettingsEtalaseReorderFragment)
}
