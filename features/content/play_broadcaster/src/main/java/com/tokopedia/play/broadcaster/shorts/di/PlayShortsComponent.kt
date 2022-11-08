package com.tokopedia.play.broadcaster.shorts.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.play.broadcaster.shorts.view.activity.PlayShortsActivity
import dagger.Component

/**
 * Created By : Jonathan Darwin on November 08, 2022
 */
@Component(
    modules = [
        PlayShortsModule::class,
        PlayShortsViewModelModule::class,
        PlayShortsFragmentModule::class
    ],
    dependencies = [BaseAppComponent::class]
)
@PlayShortsScope
interface PlayShortsComponent {

    fun inject(activity: PlayShortsActivity)
}
