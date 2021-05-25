package com.tokopedia.minicart.cartlist.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.minicart.cartlist.MiniCartListBottomSheet
import dagger.Component

@ActivityScope
@Component(modules = [MiniCartListViewModelModule::class, MiniCartListModule::class], dependencies = [BaseAppComponent::class])
interface MiniCartListComponent {
    fun inject(miniCartListBottomSheet: MiniCartListBottomSheet)
}