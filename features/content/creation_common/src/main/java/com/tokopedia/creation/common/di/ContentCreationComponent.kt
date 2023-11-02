package com.tokopedia.creation.common.di

import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.creation.common.analytics.ContentCreationAnalytics
import dagger.Component

/**
 * Created By : Muhammad Furqan on 04/10/23
 */
@ContentCreationScope
@Component(modules = [ContentCreationModule::class], dependencies = [BaseAppComponent::class])
interface ContentCreationComponent {
    fun contentCreationFactory(): ViewModelProvider.Factory
    fun contentCreationAnalytics(): ContentCreationAnalytics
}
