package com.tokopedia.play.broadcaster.shorts.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.content.common.di.ContentCoachMarkSharedPrefModule
import com.tokopedia.content.common.onboarding.di.UGCOnboardingModule
import com.tokopedia.content.common.producttag.di.module.ContentCreationProductTagBindModule
import com.tokopedia.play_common.shortsuploader.di.uploader.PlayShortsUploaderModule
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
        ContentCreationProductTagBindModule::class,
        UGCOnboardingModule::class,
        PlayShortsUploaderModule::class,
        ContentCoachMarkSharedPrefModule::class,
    ],
    dependencies = [BaseAppComponent::class]
)
@PlayShortsScope
interface PlayShortsComponent {

    fun inject(activity: PlayShortsActivity)
}
