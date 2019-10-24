package com.tokopedia.tkpd.tkpdreputation.createreputation.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.tkpd.tkpdreputation.createreputation.ui.fragment.CreateReviewFragment
import dagger.Component

@CreateReviewScope
@Component(modules = [CreateReviewModule::class, ViewModelModule::class], dependencies = [BaseAppComponent::class])
interface CreateReviewComponent {
    fun inject(createReviewFragment: CreateReviewFragment)
}