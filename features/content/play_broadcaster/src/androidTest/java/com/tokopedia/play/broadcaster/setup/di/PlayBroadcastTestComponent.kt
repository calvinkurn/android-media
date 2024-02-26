package com.tokopedia.play.broadcaster.setup.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.content.common.onboarding.di.UGCOnboardingModule
import com.tokopedia.content.product.picker.ugc.di.module.ContentCreationProductTagBindModule
import com.tokopedia.play.broadcaster.di.*
import dagger.Component

/**
 * Created By : Jonathan Darwin on April 12, 2023
 */
@Component(
    dependencies = [BaseAppComponent::class],
    modules = [
        PlayBroadcastRepositoryTestModule::class,
        PlayBroadcastTestModule::class,
        PlayBroadcastBindTestModule::class,

        PlayBroadcastFragmentModule::class,
        PlayBroadcastViewModelModule::class,
        PlayBroadcastConfigStoreModule::class,
        PlayBroadcastDataStoreModule::class,
        PlayBroadcastNetworkModule::class,
        ContentCreationProductTagBindModule::class,
        UGCOnboardingModule::class
    ]
)
@ActivityRetainedScope
abstract class PlayBroadcastTestComponent : ActivityRetainedComponent()
