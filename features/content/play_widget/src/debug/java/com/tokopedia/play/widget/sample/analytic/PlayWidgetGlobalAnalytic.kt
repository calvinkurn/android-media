package com.tokopedia.play.widget.sample.analytic

import com.tokopedia.play.widget.analytic.list.PlayWidgetInListAnalyticListener
import com.tokopedia.play.widget.analytic.list.medium.PlayWidgetInListMediumAnalyticListener
import com.tokopedia.play.widget.analytic.medium.PlayWidgetMediumAnalyticListener
import com.tokopedia.play.widget.sample.analytic.global.PlayWidgetAnalyticModel
import com.tokopedia.play.widget.sample.analytic.global.PlayWidgetMediumGlobalAnalytic
import com.tokopedia.play.widget.ui.PlayWidgetMediumView
import com.tokopedia.play.widget.ui.PlayWidgetSmallView
import com.tokopedia.play.widget.ui.PlayWidgetView
import com.tokopedia.play.widget.ui.model.PlayWidgetBackgroundUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetBannerUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetUiModel
import com.tokopedia.play.widget.ui.type.PlayWidgetChannelType
import com.tokopedia.play.widget.ui.type.PlayWidgetPromoType
import com.tokopedia.track.TrackApp
import com.tokopedia.user.session.UserSessionInterface
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import javax.inject.Inject

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