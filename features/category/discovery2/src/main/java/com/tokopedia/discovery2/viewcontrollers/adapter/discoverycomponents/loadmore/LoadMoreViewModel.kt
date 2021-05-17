package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.loadmore

import android.app.Application
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.datamapper.getComponent
import com.tokopedia.discovery2.usecase.productCardCarouselUseCase.ProductCardsUseCase
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class LoadMoreViewModel(val application: Application, private val components: ComponentsItem, val position: Int) : DiscoveryBaseViewModel(), CoroutineScope {

    @Inject
    lateinit var productCardUseCase: ProductCardsUseCase
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + SupervisorJob()


    fun getViewOrientation() = components.loadForHorizontal

    override fun onAttachToViewHolder() {
        super.onAttachToViewHolder()
        launchCatchError(block = {
            if (!getViewOrientation()) syncData.value = productCardUseCase.getProductCardsUseCase(components.id, components.pageEndPoint)
        }, onError = {
            getComponent(components.parentComponentId, components.pageEndPoint)?.productListFailState = true
            syncData.value = true
        })
    }

    // Remove
//    override fun onAttachToViewHolder() {
//        super.onAttachToViewHolder()
//        launchCatchError(block = {
//            throw Exception("Error message")
//        }, onError = {
//            getComponent(components.parentComponentId, components.pageEndPoint)?.productListFailState = true
//            syncData.value = true
//        })
//    }

}
