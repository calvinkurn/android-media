package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.productcardliststate

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.discovery2.ComponentNames
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.datamapper.getComponent
import com.tokopedia.discovery2.usecase.MerchantVoucherUseCase
import com.tokopedia.discovery2.usecase.productCardCarouselUseCase.ProductCardsUseCase
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class ErrorLoadViewModel(val application: Application,
                         val components: ComponentsItem, val position: Int) :
        DiscoveryBaseViewModel(), CoroutineScope {

    private val showLoader: MutableLiveData<Boolean> = MutableLiveData()
    @Inject
    lateinit var productCardUseCase: ProductCardsUseCase

    @Inject
    lateinit var merchantVoucherUseCase: MerchantVoucherUseCase

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + SupervisorJob()

    fun getShowLoaderStatus(): LiveData<Boolean> = showLoader

    fun reloadComponentData() {
        showLoader.value = true
        launchCatchError(block = {
            getComponent(components.parentComponentId, components.pageEndPoint)?.let {
                if (it.noOfPagesLoaded == 0) {
                    syncData.value = when (components.parentComponentName) {
                        ComponentNames.MerchantVoucherList.componentName ->
                            merchantVoucherUseCase.loadFirstPageComponents(
                                components.parentComponentId,
                                components.pageEndPoint
                            )
                        else ->
                            productCardUseCase.loadFirstPageComponents(
                                components.parentComponentId,
                                components.pageEndPoint
                            )
                    }
                } else {
                    syncData.value =
                        when (components.parentComponentName) {
                            ComponentNames.MerchantVoucherList.componentName ->
                                merchantVoucherUseCase.getVoucherUseCase(
                                    components.id,
                                    components.pageEndPoint
                                )
                            else ->
                                productCardUseCase.getProductCardsUseCase(
                                    components.id,
                                    components.pageEndPoint
                                )
                        }
                }

            }
        }, onError = {
            showLoader.value = false
        })
    }
}