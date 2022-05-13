package com.tokopedia.home_account.explicitprofile.di.component

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.home_account.explicitprofile.di.module.ExplicitProfileModules
import com.tokopedia.home_account.explicitprofile.di.module.ExplicitProfileViewModelModules
import com.tokopedia.home_account.explicitprofile.features.ExplicitProfileFragment
import com.tokopedia.home_account.explicitprofile.features.categories.CategoryFragment
import dagger.Component

@ActivityScope
@Component(modules = [
    ExplicitProfileModules::class,
    ExplicitProfileViewModelModules::class
], dependencies = [
    BaseAppComponent::class
])

interface ExplicitProfileComponents {
    fun inject(fragment: ExplicitProfileFragment)
    fun inject(fragment: CategoryFragment)
}