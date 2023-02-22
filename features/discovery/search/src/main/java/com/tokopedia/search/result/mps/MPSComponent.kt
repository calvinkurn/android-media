package com.tokopedia.search.result.mps

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.search.di.scope.SearchScope
import com.tokopedia.search.result.mps.domain.usecase.MPSUseCaseModule
import dagger.Component

@SearchScope
@Component(
    modules = [
        MPSStateModule::class,
        MPSViewModelModule::class,
        MPSUseCaseModule::class,
    ],
    dependencies = [BaseAppComponent::class]
)
interface MPSComponent {

    fun inject(mpsFragment: MPSFragment)
}
