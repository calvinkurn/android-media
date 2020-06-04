package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.productcardcarousel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.di.DaggerDiscoveryComponent
import com.tokopedia.discovery2.usecase.productCardCarouselUseCase.ProductCardsUseCase
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

private const val RPC_ROWS = "rpc_Rows"
private const val RPC_START = "rpc_Start"
private const val PRODUCT_PER_PAGE = 20
private const val START_POINT = 0

class ProductCardCarouselViewModel(val application: Application, components: ComponentsItem, val position: Int) : DiscoveryBaseViewModel(), CoroutineScope {
    private val productCarouselComponentData: MutableLiveData<ComponentsItem> = MutableLiveData()
    private val productCarouselList: MutableLiveData<ArrayList<ComponentsItem>> = MutableLiveData()

    @Inject
    lateinit var productCardsUseCase: ProductCardsUseCase


    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + SupervisorJob()


    init {
        initDaggerInject()
        productCarouselComponentData.value = components
    }

    override fun initDaggerInject() {
        DaggerDiscoveryComponent.builder()
                .baseAppComponent((application.applicationContext as BaseMainApplication).baseAppComponent)
                .build()
                .inject(this)
    }


    fun getProductCarouselItemsListData(): LiveData<ArrayList<ComponentsItem>> = productCarouselList

    fun fetchProductCarouselData(pageEndPoint: String) {
        if (productCarouselList.value.isNullOrEmpty()) {
            launchCatchError(block = {
                productCarouselList.value = productCardsUseCase.getProductCardsUseCase(productCarouselComponentData.value?.id.toIntOrZero(), getQueryParameterMap(), pageEndPoint, productCarouselComponentData.value?.name)
            }, onError = {
                it.printStackTrace()
            })
        }
    }

    private fun getQueryParameterMap(): MutableMap<String, Any> {
        val queryParameterMap = mutableMapOf<String, Any>()
        queryParameterMap[RPC_ROWS] = PRODUCT_PER_PAGE.toString()
        queryParameterMap[RPC_START] = START_POINT.toString()
        return queryParameterMap
    }
}