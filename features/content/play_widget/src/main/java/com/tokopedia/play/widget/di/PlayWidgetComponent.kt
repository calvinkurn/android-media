package com.tokopedia.play.widget.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.play.widget.analytic.PlayWidgetGlobalAnalytic
import dagger.Component

/**
 * Created by jegul on 19/10/20
 */
@PlayWidgetScope
@Component(
    dependencies = [BaseAppComponent::class],
    modules = [PlayWidgetInternalModule::class],
)
internal interface PlayWidgetComponent {

    fun getGlobalAnalyticFactory(): PlayWidgetGlobalAnalytic.Factory
}