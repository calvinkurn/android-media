package com.tokopedia.tokopedianow.annotation.di.component

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.tokopedianow.annotation.di.module.AllAnnotationModule
import com.tokopedia.tokopedianow.annotation.di.module.AllAnnotationUseCaseModule
import com.tokopedia.tokopedianow.annotation.di.module.AllAnnotationViewModelModule
import com.tokopedia.tokopedianow.annotation.di.scope.AllAnnotationScope
import com.tokopedia.tokopedianow.annotation.presentation.fragment.TokoNowAllAnnotationFragment
import dagger.Component

@AllAnnotationScope
@Component(
    modules = [
        AllAnnotationModule::class,
        AllAnnotationViewModelModule::class,
        AllAnnotationUseCaseModule::class
    ],
    dependencies = [BaseAppComponent::class]
)
interface AllAnnotationComponent {
    fun inject(fragment: TokoNowAllAnnotationFragment)
}
