package com.tokopedia.videoTabComponent.di

import android.content.Context
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.play.widget.di.PlayWidgetModule
import com.tokopedia.videoTabComponent.view.VideoTabFragment
import dagger.Component

@VideoTabScope
@Component(modules = [VideoViewModelModule::class, VideoTabModule::class, PlayWidgetModule::class],dependencies = [BaseAppComponent::class])
 interface VideoTabComponent {
    @ApplicationContext
    fun context(): Context?

    fun inject(videoTabFragment: VideoTabFragment)
}