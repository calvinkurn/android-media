package com.tokopedia.play.broadcaster.shorts.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.content.common.di.ContentCoachMarkSharedPrefModule
import com.tokopedia.content.common.onboarding.di.UGCOnboardingModule
import com.tokopedia.content.product.picker.ugc.di.module.ContentCreationProductTagBindModule
import com.tokopedia.content.product.picker.seller.di.ProductPickerBindModule
import com.tokopedia.creation.common.upload.di.uploader.CreationUploaderComponent
import com.tokopedia.play.broadcaster.shorts.view.activity.PlayShortsActivity
import dagger.Component

/**
 * Created By : Jonathan Darwin on November 08, 2022
 */
@Component(
    modules = [
        PlayShortsModule::class,
        PlayShortsBindModule::class,
        PlayShortsViewModelModule::class,
        PlayShortsFragmentModule::class,
        ProductPickerBindModule::class,
        ContentCreationProductTagBindModule::class,
        UGCOnboardingModule::class,
        ContentCoachMarkSharedPrefModule::class,
    ],
    dependencies = [
        BaseAppComponent::class,
        CreationUploaderComponent::class,
    ]
)
@PlayShortsScope
interface PlayShortsComponent {

    fun inject(activity: PlayShortsActivity)
}
