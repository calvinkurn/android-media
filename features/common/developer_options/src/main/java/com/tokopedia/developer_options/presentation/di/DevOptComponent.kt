package com.tokopedia.developer_options.presentation.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.developer_options.presentation.activity.DeveloperOptionActivity
import dagger.Component

@ActivityScope
@Component(modules = [DevOptModule::class], dependencies = [BaseAppComponent::class])
interface DevOptComponent {

    fun inject(activity: DeveloperOptionActivity)
}
