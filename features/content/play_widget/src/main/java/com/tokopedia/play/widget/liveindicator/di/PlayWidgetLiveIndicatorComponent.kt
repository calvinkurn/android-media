package com.tokopedia.play.widget.liveindicator.di

import androidx.lifecycle.ViewModel
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.play.widget.liveindicator.analytic.PlayWidgetLiveIndicatorAnalytic
import com.tokopedia.play.widget.di.PlayWidgetInternalModule
import com.tokopedia.play.widget.di.PlayWidgetScope
import dagger.Component

@PlayWidgetScope
@Component(
    dependencies = [BaseAppComponent::class],
    modules = [PlayWidgetInternalModule::class],
)
internal abstract class PlayWidgetLiveIndicatorComponent : ViewModel() {

    abstract fun getAnalytic(): PlayWidgetLiveIndicatorAnalytic
}

