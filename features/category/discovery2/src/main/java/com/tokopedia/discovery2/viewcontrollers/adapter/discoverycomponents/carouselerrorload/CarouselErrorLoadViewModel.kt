package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.carouselerrorload

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.usecase.productCardCarouselUseCase.ProductCardsUseCase
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class CarouselErrorLoadViewModel(val application: Application,
                                 private val components: ComponentsItem, val position: Int) :
        DiscoveryBaseViewModel(), CoroutineScope {

    private val showLoader: MutableLiveData<Boolean> = MutableLiveData()
    @Inject
    lateinit var productCardUseCase: ProductCardsUseCase
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + SupervisorJob()


    fun getParentComponentPosition() = components.parentComponentPosition
    fun getShowLoaderStatus(): LiveData<Boolean> = showLoader

    fun loadData() {
        showLoader.value = true
        launchCatchError(block = {
            syncData.value = productCardUseCase.getCarouselPaginatedData(components.parentComponentId, components.pageEndPoint)
        }, onError = {
            showLoader.value = false
        })
    }
}
