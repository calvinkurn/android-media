package com.tokopedia.product.detail.view.componentization

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.product.detail.view.fragment.ProductDetailFragment
import com.tokopedia.product.detail.view.util.PdpUiUpdater
import com.tokopedia.product.detail.view.viewmodel.product_detail.ProductDetailViewModel
import com.tokopedia.trackingoptimizer.TrackingQueue

/**
 * Created by yovi.putra on 14/11/23"
 * Project name: android-tokopedia-core
 **/

interface PdpComponentCallbackMediator : ComponentCallbackMediator {

    val rootView: Fragment

    val pdpViewModel: ProductDetailViewModel

    val uiUpdater: PdpUiUpdater?

    val queueTracker: TrackingQueue

    val recyclerViewPool: RecyclerView.RecycledViewPool?

    val mvcLauncher: ActivityResultLauncher<Intent>
}
