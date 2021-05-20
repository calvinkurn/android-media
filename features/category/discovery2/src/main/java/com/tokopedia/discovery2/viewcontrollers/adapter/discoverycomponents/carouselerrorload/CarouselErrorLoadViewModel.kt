package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.carouselerrorload

import android.app.Application
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.datamapper.getComponent
import com.tokopedia.discovery2.di.DaggerDiscoveryComponent
import com.tokopedia.discovery2.usecase.productCardCarouselUseCase.ProductCardsUseCase
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.productcardcarousel.PRODUCT_PER_PAGE
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class CarouselErrorLoadViewModel(val application: Application, private val components: ComponentsItem, val position: Int) : DiscoveryBaseViewModel(), CoroutineScope {

    @Inject
    lateinit var productCardUseCase: ProductCardsUseCase
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + SupervisorJob()


    fun getParentComponentPosition() = components.parentComponentPosition

    fun loadData(){
        launchCatchError(block = {
            syncData.value = productCardUseCase.getCarouselPaginatedData(components.parentComponentId, components.pageEndPoint)
        }, onError = {
            it.printStackTrace()
        })
    }
}
