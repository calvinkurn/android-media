package com.tokopedia.feedback_form.feedbackpage.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.feedback_form.feedbackpage.ui.feedbackpage.FeedbackPageFragment
import dagger.Component

@FeedbackPageScope
@Component(modules = [FeedbackPageModule::class], dependencies = [BaseAppComponent::class])
interface FeedbackPageComponent {
    fun inject (feedbackPageFragment: FeedbackPageFragment)
}