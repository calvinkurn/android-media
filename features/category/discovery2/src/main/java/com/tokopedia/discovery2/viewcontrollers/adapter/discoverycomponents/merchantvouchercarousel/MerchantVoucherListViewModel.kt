package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.merchantvouchercarousel

import android.app.Application
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.datamapper.getComponent
import com.tokopedia.discovery2.usecase.MerchantVoucherUseCase
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class MerchantVoucherListViewModel(application: Application, val components: ComponentsItem, val position: Int) : DiscoveryBaseViewModel(), CoroutineScope {
    @Inject
    lateinit var merchantVoucherUseCase: MerchantVoucherUseCase

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + SupervisorJob()

    override fun onAttachToViewHolder() {
        super.onAttachToViewHolder()
        fetchCouponData()
    }

    private fun fetchCouponData() {
        launchCatchError(block = {
            this@MerchantVoucherListViewModel.syncData.value = merchantVoucherUseCase.loadFirstPageComponents(components.id, components.pageEndPoint)
        }, onError = {
            getComponent(components.id, components.pageEndPoint)?.verticalProductFailState = true
            this@MerchantVoucherListViewModel.syncData.value = true
        })
    }
}