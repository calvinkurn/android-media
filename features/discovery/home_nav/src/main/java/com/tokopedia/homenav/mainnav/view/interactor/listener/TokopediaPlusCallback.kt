package com.tokopedia.homenav.mainnav.view.interactor.listener

import com.tokopedia.homenav.mainnav.view.analytics.TrackingProfileSection
import com.tokopedia.searchbar.navigation_component.NavSource
import com.tokopedia.usercomponents.tokopediaplus.common.TokopediaPlusListener
import com.tokopedia.usercomponents.tokopediaplus.domain.TokopediaPlusDataModel

class TokopediaPlusCallback constructor(
    private val source: NavSource,
    private val pageSourcePath: String,
    private val onRefreshTokopediaPlus: () -> Unit
) : TokopediaPlusListener {

    override fun isShown(isShown: Boolean, pageSource: String, model: TokopediaPlusDataModel) = Unit

    override fun onClick(pageSource: String, model: TokopediaPlusDataModel) {
        TrackingProfileSection.onClickTokopediaPlus(model.isSubscriber, source, pageSourcePath)
    }

    override fun onRetry() = onRefreshTokopediaPlus()
}
