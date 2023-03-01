package com.tokopedia.play.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.play.di.module.PlayRepositoryModule
import com.tokopedia.play.view.activity.PlayActivity
import com.tokopedia.play.widget.di.PlayWidgetModule
import dagger.Component

/**
 * Created by jegul on 29/11/19
 */
@PlayScope
@Component(
        modules = [
            PlayModule::class,
            PlayViewModelModule::class,
            PlayViewerFragmentModule::class,
            PlayBindModule::class,
            PlayRepositoryModule::class,
            PlayWidgetModule::class,
        ],
        dependencies = [BaseAppComponent::class]
)
interface PlayComponent {

    fun inject(playActivity: PlayActivity)
}
