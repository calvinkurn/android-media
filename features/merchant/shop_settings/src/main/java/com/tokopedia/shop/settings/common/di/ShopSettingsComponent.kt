package com.tokopedia.shop.settings.common.di

import android.content.Context
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.shop.settings.address.view.ShopSettingAddressAddEditFragment
import com.tokopedia.shop.settings.address.view.ShopSettingAddressFragment
import com.tokopedia.shop.settings.basicinfo.view.activity.ShopEditBasicInfoActivity
import com.tokopedia.shop.settings.basicinfo.view.activity.ShopEditScheduleActivity
import com.tokopedia.shop.settings.basicinfo.view.fragment.ShopEditBasicInfoFragment
import com.tokopedia.shop.settings.basicinfo.view.fragment.ShopSettingsInfoFragment
import com.tokopedia.shop.settings.etalase.view.fragment.ShopSettingsEtalaseAddEditFragment
import com.tokopedia.shop.settings.etalase.view.fragment.ShopSettingsEtalaseListFragment
import com.tokopedia.shop.settings.etalase.view.fragment.ShopSettingsEtalaseReorderFragment
import com.tokopedia.shop.settings.notes.view.fragment.ShopSettingsNotesAddEditFragment
import com.tokopedia.shop.settings.notes.view.fragment.ShopSettingsNotesListFragment
import com.tokopedia.shop.settings.notes.view.fragment.ShopSettingsNotesReorderFragment
import dagger.Component

/**
 * Created by zulfikarrahman on 6/21/18.
 */

@ShopSettingsScope
@Component(
    modules = [ShopSettingsModule::class, ViewModelModule::class, ShopSettingsInfoViewModelModule::class],
    dependencies = [BaseAppComponent::class]
)
interface ShopSettingsComponent {

    @ApplicationContext
    fun getContext(): Context
    fun getMultiRequestGraphqlUseCase(): MultiRequestGraphqlUseCase
//    fun getDispatcherProvider(): ShopSettingDispatcherProvider

    fun inject(shopSettingsInfoFragment: ShopSettingsInfoFragment)
    fun inject(shopEditBasicInfoActivity: ShopEditBasicInfoActivity)
    fun inject(shopEditBasicInfoFragment: ShopEditBasicInfoFragment)
    fun inject(shopEditScheduleActivity: ShopEditScheduleActivity)
    fun inject(shopSettingsNotesFragment: ShopSettingsNotesListFragment)
    fun inject(shopSettingsNotesFragment: ShopSettingsNotesReorderFragment)

    fun inject(fragment: ShopSettingAddressFragment)
    fun inject(fragment: ShopSettingAddressAddEditFragment)
    fun inject(fragment: ShopSettingsNotesAddEditFragment)

    fun inject(fragment: ShopSettingsEtalaseAddEditFragment)
    fun inject(fragment: ShopSettingsEtalaseListFragment)
    fun inject(fragment: ShopSettingsEtalaseReorderFragment)



//    fun inject(shopSettingsNotesFragment: ShopSettingsNotesListFragment)
//    fun inject(shopSettingsNotesFragment: ShopSettingsNotesReorderFragment)
//    fun inject(shopEditScheduleActivity: ShopEditScheduleActivity)
//    fun inject(shopEditBasicInfoFragment: ShopEditBasicInfoFragment)
//
//    fun inject(fragment: ShopSettingAddressFragment)
//    fun inject(fragment: ShopSettingAddressAddEditFragment)
//    fun inject(fragment: ShopSettingsNotesAddEditFragment)
//
//    fun inject(fragment: ShopSettingsEtalaseAddEditFragment)
//    fun inject(fragment: ShopSettingsEtalaseListFragment)
//    fun inject(fragment: ShopSettingsEtalaseReorderFragment)
//    fun inject(fragment: ShopSettingsInfoFragment)
}
