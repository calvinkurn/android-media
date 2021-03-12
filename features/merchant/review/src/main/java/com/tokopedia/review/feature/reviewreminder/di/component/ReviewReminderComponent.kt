package com.tokopedia.review.feature.reviewreminder.di.component

import com.tokopedia.review.common.di.ReviewComponent
import com.tokopedia.review.feature.reviewreminder.di.module.ReviewReminderViewModelModule
import com.tokopedia.review.feature.reviewreminder.di.scope.ReviewReminderScope
import com.tokopedia.review.feature.reviewreminder.view.fragment.ReminderMessageFragment
import com.tokopedia.review.feature.reviewreminder.view.fragment.ReminderPerformanceFragment
import dagger.Component

@ReviewReminderScope
@Component(modules = [ReviewReminderViewModelModule::class], dependencies = [ReviewComponent::class])
interface ReviewReminderComponent {
    fun inject(reminderMessageFragment: ReminderMessageFragment)
    fun inject(reminderPerformanceFragment: ReminderPerformanceFragment)
}