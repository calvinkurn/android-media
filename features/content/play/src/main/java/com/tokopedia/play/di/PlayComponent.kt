package com.tokopedia.play.di

import androidx.appcompat.app.AppCompatActivity
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.play.di.module.PlayRepositoryModule
import com.tokopedia.play.view.activity.PlayActivity
import com.tokopedia.play.widget.di.PlayWidgetModule
import dagger.BindsInstance
import dagger.Component
import dagger.Component.Builder

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

    @Component.Factory
    interface Factory {
        fun create(
            baseAppComponent: BaseAppComponent,
            @BindsInstance activity: AppCompatActivity
        ): PlayComponent
    }
}
