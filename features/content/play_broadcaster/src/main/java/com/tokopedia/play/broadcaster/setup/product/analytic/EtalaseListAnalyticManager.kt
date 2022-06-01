package com.tokopedia.play.broadcaster.setup.product.analytic

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.play.broadcaster.analytic.setup.product.PlayBroSetupProductAnalytic
import com.tokopedia.play.broadcaster.setup.product.view.viewcomponent.EtalaseListViewComponent
import com.tokopedia.play.broadcaster.util.eventbus.EventBus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by kenny.hadisaputra on 17/02/22
 */
class EtalaseListAnalyticManager @Inject constructor(
    private val analytic: PlayBroSetupProductAnalytic,
    private val dispatchers: CoroutineDispatchers,
) {

    fun observe(
        scope: CoroutineScope,
        event: EventBus<Any>,
    ) {
        scope.launch(dispatchers.computation) {
            event.subscribe().collect {
                when (it) {
                    is EtalaseListViewComponent.Event.OnEtalaseSelected -> {
                        analytic.clickEtalaseCard()
                    }
                    is EtalaseListViewComponent.Event.OnCampaignSelected -> {
                        analytic.clickCampaignCard()
                    }
                }
            }
        }
    }
}