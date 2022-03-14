package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.shopbannerinfinite

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.datamapper.getComponent
import com.tokopedia.discovery2.usecase.productCardCarouselUseCase.ProductCardsUseCase
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.factory.ComponentsList
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext


class ShopBannerInfiniteViewModel(val application: Application, val components: ComponentsItem, val position: Int) : DiscoveryBaseViewModel(), CoroutineScope {

    @Inject
    lateinit var productCardsUseCase: ProductCardsUseCase

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + SupervisorJob()

    override fun onAttachToViewHolder() {
        super.onAttachToViewHolder()
        launchCatchError(block = {
            this@ShopBannerInfiniteViewModel.syncData.value = productCardsUseCase.loadFirstPageComponents(components.id, components.pageEndPoint)
        }, onError = {
            getComponent(components.id, components.pageEndPoint)?.verticalProductFailState = true
            this@ShopBannerInfiniteViewModel.syncData.value = true
        })
    }

}