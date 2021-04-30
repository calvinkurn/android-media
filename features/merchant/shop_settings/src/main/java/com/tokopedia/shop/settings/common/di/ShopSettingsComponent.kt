package com.tokopedia.shop.settings.common.di

import android.content.Context
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.shop.settings.basicinfo.view.activity.ShopEditScheduleActivity
import com.tokopedia.shop.settings.basicinfo.view.fragment.ShopEditBasicInfoFragment
import com.tokopedia.shop.settings.basicinfo.view.fragment.ShopSettingsInfoFragment
import com.tokopedia.shop.settings.basicinfo.view.fragment.ShopSettingsOperationalHoursFragment
import com.tokopedia.shop.settings.basicinfo.view.fragment.ShopSettingsSetOperationalHoursFragment
import com.tokopedia.shop.settings.etalase.view.fragment.ShopSettingsEtalaseAddEditFragment
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

    fun inject(shopSettingsInfoFragment: ShopSettingsInfoFragment)
    fun inject(shopEditBasicInfoFragment: ShopEditBasicInfoFragment)
    fun inject(shopEditScheduleActivity: ShopEditScheduleActivity)
    fun inject(shopSettingsNotesFragment: ShopSettingsNotesListFragment)
    fun inject(shopSettingsNotesFragment: ShopSettingsNotesReorderFragment)
    fun inject(shopSettingsOperationalHoursFragment: ShopSettingsOperationalHoursFragment)
    fun inject(shopSettingsSetOperationalHoursFragment: ShopSettingsSetOperationalHoursFragment)

    fun inject(fragment: ShopSettingsNotesAddEditFragment)
    fun inject(fragment: ShopSettingsEtalaseAddEditFragment)
}
