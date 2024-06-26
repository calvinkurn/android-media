package com.tokopedia.sellerfeedback.di.component

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.mediauploader.common.di.MediaUploaderModule
import com.tokopedia.sellerfeedback.di.module.SellerFeedbackModule
import com.tokopedia.sellerfeedback.di.module.SellerFeedbackViewModelModule
import com.tokopedia.sellerfeedback.di.scope.SellerFeedbackScope
import com.tokopedia.sellerfeedback.presentation.fragment.SellerFeedbackFragment
import com.tokopedia.sellerfeedback.presentation.fragment.SellerFeedbackKmpFragment
import dagger.Component

@SellerFeedbackScope
@Component(modules = [
    SellerFeedbackModule::class,
    SellerFeedbackViewModelModule::class,
    MediaUploaderModule::class
], dependencies = [BaseAppComponent::class])
interface SellerFeedbackComponent {
    fun inject(sellerFeedbackFragment: SellerFeedbackFragment)
    fun inject(sellerFeedbackKmpFragment: SellerFeedbackKmpFragment)
}
