package com.tokopedia.tokofood.feature.search.container.di.component

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.tokofood.common.di.TokoFoodScope
import com.tokopedia.tokofood.feature.search.container.di.module.SearchContainerModule
import com.tokopedia.tokofood.feature.search.container.di.scope.SearchContainerScope
import com.tokopedia.tokofood.feature.search.container.presentation.fragment.SearchContainerFragment
import dagger.Component

@SearchContainerScope
@Component(modules = [SearchContainerModule::class], dependencies = [BaseAppComponent::class])
internal interface SearchContainerComponent {
    fun inject(searchContainerFragment: SearchContainerFragment)
}