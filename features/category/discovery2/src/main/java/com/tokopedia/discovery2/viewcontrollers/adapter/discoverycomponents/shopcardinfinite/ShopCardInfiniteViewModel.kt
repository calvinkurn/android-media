package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.shopcardinfinite

import android.app.Application
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.datamapper.getComponent
import com.tokopedia.discovery2.usecase.shopcardusecase.ShopCardUseCase
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext


class ShopCardInfiniteViewModel(val application: Application, val components: ComponentsItem, val position: Int) : DiscoveryBaseViewModel(), CoroutineScope {

    @Inject
    lateinit var shopCardInfiniteUseCase: ShopCardUseCase

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + SupervisorJob()

    override fun onAttachToViewHolder() {
        super.onAttachToViewHolder()
        launchCatchError(block = {
            this@ShopCardInfiniteViewModel.syncData.value = shopCardInfiniteUseCase.loadFirstPageComponents(components.id, components.pageEndPoint)
        }, onError = {
            getComponent(components.id, components.pageEndPoint)?.verticalProductFailState = true
            this@ShopCardInfiniteViewModel.syncData.value = true
        })
    }

}