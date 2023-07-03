package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.shopcardinfinite

import android.app.Application
import com.tokopedia.discovery2.Utils.Companion.areFiltersApplied
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.ErrorState
import com.tokopedia.discovery2.datamapper.getComponent
import com.tokopedia.discovery2.usecase.shopcardusecase.ShopCardUseCase
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.shopcard.SHOP_PER_PAGE
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class ShopCardInfiniteViewModel(val application: Application, val components: ComponentsItem, val position: Int) : DiscoveryBaseViewModel(), CoroutineScope {

    @JvmField
    @Inject
    var shopCardInfiniteUseCase: ShopCardUseCase? = null

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + SupervisorJob()

    override fun onAttachToViewHolder() {
        super.onAttachToViewHolder()
        launchCatchError(block = {
            val shouldSync = shopCardInfiniteUseCase?.loadFirstPageComponents(
                components.id,
                components.pageEndPoint,
                SHOP_PER_PAGE
            )
            if (shouldSync == true) {
                getComponent(components.id, components.pageEndPoint)?.let {
                    if (it.getComponentsItem().isNullOrEmpty() && !it.areFiltersApplied()) {
                        it.verticalProductFailState = true
                        it.errorState = ErrorState.EmptyComponentState
                        components.shouldRefreshComponent = null
                    } else {
                        it.verticalProductFailState = false
                    }
                }
            }
            this@ShopCardInfiniteViewModel.syncData.value = shouldSync
        }, onError = {
                getComponent(components.id, components.pageEndPoint)?.verticalProductFailState = true
                this@ShopCardInfiniteViewModel.syncData.value = true
            })
    }
}
