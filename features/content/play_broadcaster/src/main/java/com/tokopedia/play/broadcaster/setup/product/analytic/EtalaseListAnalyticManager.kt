package com.tokopedia.play.broadcaster.setup.product.analytic

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.content.product.picker.sgc.analytic.ContentProductPickerSGCAnalytic
import com.tokopedia.play.broadcaster.setup.product.view.bottomsheet.EtalaseListBottomSheet
import com.tokopedia.play.broadcaster.setup.product.view.viewcomponent.EtalaseListViewComponent
import com.tokopedia.play.broadcaster.util.eventbus.EventBus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by kenny.hadisaputra on 17/02/22
 */
class EtalaseListAnalyticManager @Inject constructor(
    private val analytic: ContentProductPickerSGCAnalytic,
    private val dispatchers: CoroutineDispatchers,
) {

    fun observe(
        scope: CoroutineScope,
        event: EventBus<Any>,
    ) {
        scope.launch(dispatchers.computation) {
            event.subscribe().collect {
                when (it) {
                    is EtalaseListBottomSheet.Event.ClickClose -> {
                        analytic.clickCloseOnProductFilterBottomSheet()
                    }
                    is EtalaseListBottomSheet.Event.ViewBottomSheet -> {
                        analytic.viewProductFilterBottomSheet()
                    }
                    is EtalaseListViewComponent.Event.OnEtalaseSelected -> {
                        analytic.clickEtalaseCard(it.etalase.title)
                    }
                    is EtalaseListViewComponent.Event.OnCampaignSelected -> {
                        analytic.clickCampaignCard(it.campaign.title)
                    }
                }
            }
        }
    }
}
