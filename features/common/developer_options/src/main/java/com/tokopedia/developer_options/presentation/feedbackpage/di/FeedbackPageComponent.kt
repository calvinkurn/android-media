package com.tokopedia.developer_options.presentation.feedbackpage.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.developer_options.presentation.feedbackpage.ui.FeedbackPageFragment
import dagger.Component

@FeedbackPageScope
@Component(modules = [FeedbackPageModule::class], dependencies = [BaseAppComponent::class])
interface FeedbackPageComponent {
    fun inject (feedbackPageFragment: FeedbackPageFragment)
}