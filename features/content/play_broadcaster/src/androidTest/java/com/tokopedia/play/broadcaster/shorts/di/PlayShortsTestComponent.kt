package com.tokopedia.play.broadcaster.shorts.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.content.common.onboarding.di.UGCOnboardingModule
import com.tokopedia.content.common.producttag.di.module.ContentCreationProductTagBindModule
import com.tokopedia.play.broadcaster.shorts.container.PlayShortsTestActivity
import com.tokopedia.play_common.shortsuploader.di.PlayShortsUploaderModule
import dagger.Component

/**
 * Created By : Jonathan Darwin on December 12, 2022
 */
@PlayShortsScope
@Component(
    modules = [
        PlayShortsTestModule::class,
        PlayShortsBindTestModule::class,
        PlayShortsViewModelModule::class,
        PlayShortsFragmentModule::class,
        ContentCreationProductTagBindTestModule::class,
        UGCOnboardingTestModule::class,
        PlayShortsUploaderTestModule::class,
    ],
    dependencies = [BaseAppComponent::class]
)
interface PlayShortsTestComponent {

    fun inject(activity: PlayShortsTestActivity)
}
