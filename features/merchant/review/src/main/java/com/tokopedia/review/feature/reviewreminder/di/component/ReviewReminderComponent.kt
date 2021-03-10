package com.tokopedia.review.feature.reviewreminder.di.component

import com.tokopedia.review.common.di.ReviewComponent
import com.tokopedia.review.feature.reviewreminder.di.module.ReminderPerformanceViewModelModule
import com.tokopedia.review.feature.reviewreminder.di.scope.ReviewReminderScope
import com.tokopedia.review.feature.reviewreminder.view.fragment.ReminderPerformanceFragment
import dagger.Component

@ReviewReminderScope
@Component(modules = [ReminderPerformanceViewModelModule::class], dependencies = [ReviewComponent::class])
interface ReviewReminderComponent {
    fun inject(reminderPerformanceFragment: ReminderPerformanceFragment)
}