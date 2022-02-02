package com.tokopedia.play.broadcaster.di.setup

import com.tokopedia.play.broadcaster.di.ActivityRetainedComponent
import com.tokopedia.play.broadcaster.view.bottomsheet.PlayBroadcastSetupBottomSheet
import com.tokopedia.play.broadcaster.view.fragment.edit.*
import dagger.Component

@Component(
        dependencies = [ActivityRetainedComponent::class],
        modules = [PlayBroadcastSetupViewModelModule::class,
            PlayBroadcastSetupBindModule::class,
            PlayBroadcastSetupModule::class,
            PlayBroadcastSetupFragmentModule::class]
)
@PlayBroadcastSetupScope
interface PlayBroadcastSetupComponent {

    fun inject(bottomSheet: PlayBroadcastSetupBottomSheet)

    fun inject(bottomSheet: ProductSetupBottomSheet)

    fun inject(bottomSheet: ProductEditFragment)

    fun inject(emptyFragment: CoverEditFragment)

    fun inject(bottomSheet: SetupBroadcastScheduleBottomSheet)

    @Component.Builder
    interface Builder {

        fun setActivityRetainedComponent(retainedComponent: ActivityRetainedComponent): Builder
        fun build(): PlayBroadcastSetupComponent
    }
}