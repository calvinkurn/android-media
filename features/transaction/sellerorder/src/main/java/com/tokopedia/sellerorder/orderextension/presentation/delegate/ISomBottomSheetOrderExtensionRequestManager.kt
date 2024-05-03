package com.tokopedia.sellerorder.orderextension.presentation.delegate

import android.content.Context
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import com.tokopedia.sellerorder.orderextension.presentation.bottomsheet.SomBottomSheetOrderExtensionRequest
import com.tokopedia.sellerorder.orderextension.presentation.viewmodel.SomOrderExtensionViewModel

interface ISomBottomSheetOrderExtensionRequestManager {

    var somBottomSheetOrderExtensionRequest: SomBottomSheetOrderExtensionRequest?
    fun registerSomBottomSheetOrderExtensionRequest(mediator: Mediator, listener: Listener)
    fun showSomBottomSheetOrderExtensionRequest()

    interface Mediator {
        fun getContext(): Context?
        fun getFragmentManager(): FragmentManager?
        fun getViewLifecycleOwner(): LifecycleOwner
        fun getBottomSheetContainer(): CoordinatorLayout?
        fun getSomOrderExtensionOrderId(): String
        fun getSomOrderExtensionViewModel(): SomOrderExtensionViewModel
    }

    interface Listener {
        fun onSuccessRequestOrderExtension(message: String)
        fun onFailedRequestOrderExtension(message: String, throwable: Throwable?)
        fun onSellerOrderExtensionRequestDismissed()
    }
}
