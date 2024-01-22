package com.tokopedia.product.detail.view.componentization

import androidx.fragment.app.Fragment
import com.tokopedia.product.detail.view.util.PdpUiUpdater
import com.tokopedia.product.detail.view.viewmodel.product_detail.DynamicProductDetailViewModel
import com.tokopedia.trackingoptimizer.TrackingQueue

/**
 * Created by yovi.putra on 14/11/23"
 * Project name: android-tokopedia-core
 **/

interface PdpComponentCallbackMediator : ComponentCallbackMediator {

    val rootView: Fragment

    val pdpViewModel: DynamicProductDetailViewModel

    val uiUpdater: PdpUiUpdater?

    val queueTracker: TrackingQueue
}
