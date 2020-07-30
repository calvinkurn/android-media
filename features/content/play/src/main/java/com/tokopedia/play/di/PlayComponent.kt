package com.tokopedia.play.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.play.view.activity.PlayActivity
import com.tokopedia.play.view.fragment.*
import dagger.Component

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

    fun inject(playUserInteractionFragment: PlayUserInteractionFragment)

    fun inject(playMiniInteractionFragment: PlayMiniInteractionFragment)

    fun inject(playErrorFragment: PlayErrorFragment)

    fun inject(playBottomSheetFragment: PlayBottomSheetFragment)

    fun inject(playYouTubeFragment: PlayYouTubeFragment)

    fun inject(playActivity: PlayActivity)
}