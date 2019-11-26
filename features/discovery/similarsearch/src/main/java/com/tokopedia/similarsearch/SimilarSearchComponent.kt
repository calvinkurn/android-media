package com.tokopedia.similarsearch

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import dagger.Component

@SimilarSearchModuleScope
@Component(modules = [SimilarSearchViewModelFactoryModule::class], dependencies = [BaseAppComponent::class])
internal interface SimilarSearchComponent {

    fun inject(similarSearchActivity: SimilarSearchActivity)
}