package com.tokopedia.play.widget.sample.analytic

import com.tokopedia.play.widget.analytic.list.PlayWidgetInListAnalyticListener
import com.tokopedia.play.widget.analytic.list.medium.PlayWidgetInListMediumAnalyticListener
import com.tokopedia.play.widget.sample.analytic.global.model.PlayWidgetAnalyticModel
import com.tokopedia.play.widget.sample.analytic.global.PlayWidgetMediumGlobalAnalytic
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

/**
 * Created by kenny.hadisaputra on 31/05/22
 */
class PlayWidgetGlobalAnalytic @AssistedInject constructor(
    @Assisted val model: PlayWidgetAnalyticModel,
    private val mediumAnalytic: PlayWidgetMediumGlobalAnalytic.Factory,
) : PlayWidgetInListAnalyticListener,
    PlayWidgetInListMediumAnalyticListener by mediumAnalytic.create(model) {

    @AssistedFactory
    interface Factory {
        fun create(model: PlayWidgetAnalyticModel): PlayWidgetGlobalAnalytic
    }
}