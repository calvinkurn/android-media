package com.tokopedia.play.broadcaster.di.setup

import com.tokopedia.play.broadcaster.di.broadcast.PlayBroadcastComponent
import com.tokopedia.play.broadcaster.view.bottomsheet.PlayBroadcastSetupBottomSheet
import com.tokopedia.play.broadcaster.view.fragment.edit.*
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

    fun inject(bottomSheet: ProductSetupBottomSheet)

    fun inject(bottomSheet: TitleAndTagsEditBottomSheet)

    fun inject(bottomSheet: ProductEditFragment)

    fun inject(emptyFragment: CoverEditFragment)

    fun inject(bottomSheet: SetupBroadcastScheduleBottomSheet)

    @Component.Builder
    interface Builder {

        fun setBroadcastComponent(broadcastComponent: PlayBroadcastComponent): Builder
        fun build(): PlayBroadcastSetupComponent
    }
}