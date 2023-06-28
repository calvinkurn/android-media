package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.merchantvouchercarousel

import android.app.Application
import com.tokopedia.discovery2.Utils.Companion.areFiltersApplied
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.ErrorState
import com.tokopedia.discovery2.datamapper.getComponent
import com.tokopedia.discovery2.usecase.MerchantVoucherUseCase
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class MerchantVoucherListViewModel(application: Application, val components: ComponentsItem, val position: Int) : DiscoveryBaseViewModel(), CoroutineScope {
    @JvmField
    @Inject
    var merchantVoucherUseCase: MerchantVoucherUseCase? = null

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + SupervisorJob()

    override fun onAttachToViewHolder() {
        super.onAttachToViewHolder()
        fetchCouponData()
    }

    private fun fetchCouponData() {
        launchCatchError(block = {
            val shouldSync = merchantVoucherUseCase?.loadFirstPageComponents(
                components.id,
                components.pageEndPoint
            )
            if (shouldSync == true) {
                getComponent(components.id, components.pageEndPoint)?.let {
                    if (it.getComponentsItem().isNullOrEmpty() && !it.areFiltersApplied()) {
                        it.verticalProductFailState = true
                        it.errorState = ErrorState.EmptyComponentState
                    } else {
                        it.verticalProductFailState = false
                    }
                }
            }
            this@MerchantVoucherListViewModel.syncData.value = shouldSync
        }, onError = {
                getComponent(components.id, components.pageEndPoint)?.let { comp ->
                    if (it is UnknownHostException || it is SocketTimeoutException) {
                        comp.errorState = ErrorState.NetworkErrorState
                    }
                    comp.verticalProductFailState = true
                }
                this@MerchantVoucherListViewModel.syncData.value = true
            })
    }
}
