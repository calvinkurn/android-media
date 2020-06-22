package com.tokopedia.play.broadcaster.di.setup

import com.tokopedia.play.broadcaster.di.broadcast.PlayBroadcastComponent
import com.tokopedia.play.broadcaster.view.bottomsheet.PlayBroadcastSetupBottomSheet
import com.tokopedia.play.broadcaster.view.fragment.edit.CoverEditFragment
import dagger.Component

@Component(
        dependencies = [PlayBroadcastComponent::class],
        modules = [PlayBroadcastSetupViewModelModule::class,
            PlayBroadcastSetupBindModule::class,
            PlayBroadcastSetupModule::class,
            PlayBroadcastSetupFragmentModule::class]
)
@PlayBroadcastSetupScope
interface PlayBroadcastSetupComponent {

    fun inject(bottomSheet: PlayBroadcastSetupBottomSheet)

    fun inject(emptyFragment: CoverEditFragment)

    @Component.Builder
    interface Builder {

        fun setBroadcastComponent(broadcastComponent: PlayBroadcastComponent): Builder
        fun build(): PlayBroadcastSetupComponent
    }
}