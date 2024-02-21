package com.tokopedia.product.detail.view.componentization

import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.product.detail.view.util.PdpUiUpdater
import com.tokopedia.product.detail.view.viewmodel.product_detail.DynamicProductDetailViewModel
import com.tokopedia.remoteconfig.RemoteConfig
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

    val recyclerViewPool: RecyclerView.RecycledViewPool?

    val pdpRemoteConfig: RemoteConfig

    fun updateUi()
    fun logException(t: Throwable)
}
