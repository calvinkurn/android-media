package com.tokopedia.exploreCategory.di

import android.content.Context
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.exploreCategory.ui.activity.ECHomeActivity
import com.tokopedia.exploreCategory.ui.fragment.ECServiceFragment
import dagger.Component

@ECScope
@Component(modules = [ECModule::class, ECViewModelModule::class], dependencies = [BaseAppComponent::class])
interface ECComponent {

    @get:ApplicationContext
    val context: Context

    fun inject(ecHomeActivity: ECHomeActivity)
    fun inject(ecServiceFragment: ECServiceFragment)
}
