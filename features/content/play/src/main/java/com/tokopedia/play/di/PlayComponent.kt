package com.tokopedia.play.di

import android.content.Context
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.play.view.activity.PlayActivity
import com.tokopedia.play.view.fragment.*
import dagger.BindsInstance
import dagger.Component

/**
 * Created by jegul on 29/11/19
 */
@PlayScope
@Component(
        modules = [PlayModule::class, PlayViewModelModule::class, PlayViewerFragmentModule::class],
        dependencies = [BaseAppComponent::class]
)
interface PlayComponent {

    fun inject(playActivity: PlayActivity)

    @Component.Builder
    interface Builder {

        fun baseAppComponent(appComponent: BaseAppComponent): Builder
        fun context(@BindsInstance context: Context): Builder
        fun build(): PlayComponent
    }
}