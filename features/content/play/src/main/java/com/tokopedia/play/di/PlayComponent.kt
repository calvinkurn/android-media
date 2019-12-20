package com.tokopedia.play.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.play.view.activity.PlayActivity
import com.tokopedia.play.view.fragment.PlayFragment
import com.tokopedia.play.view.fragment.PlayInteractionFragment
import com.tokopedia.play.view.fragment.PlayVideoFragment
import dagger.Component
import kotlinx.coroutines.CoroutineDispatcher

/**
 * Created by jegul on 29/11/19
 */
@PlayScope
@Component(
        modules = [PlayModule::class, PlayViewModelModule::class],
        dependencies = [BaseAppComponent::class]
)
interface PlayComponent {

    fun inject(playFragment: PlayFragment)

    fun inject(playVideoFragment: PlayVideoFragment)

    fun inject(playInteractionFragment: PlayInteractionFragment)

    fun inject(playActivity: PlayActivity)
}