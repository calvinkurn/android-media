package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.productcardcarousel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.di.DaggerDiscoveryComponent
import com.tokopedia.discovery2.usecase.productCardCarouselUseCase.ProductCardCarouselUseCase
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class ProductCardCarouselViewModel(val application: Application, components: ComponentsItem) : DiscoveryBaseViewModel(), CoroutineScope {
    private val productCarouselComponentData: MutableLiveData<ComponentsItem> = MutableLiveData()
    private val productCarouselList: MutableLiveData<ArrayList<ComponentsItem>> = MutableLiveData()
    @Inject
    lateinit var productCardCarouselUseCase: ProductCardCarouselUseCase

    private val RPC_PAGE_NUMBER_KEY = "rpc_page_number"
    private val RPC_PAGE_SIZE = "rpc_page_size"
    private var pageNumber = 1
    private var productPerPage = 6

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


    fun getProductCarouselComponentData() = productCarouselComponentData
    fun getProductCarouselItemsListData() = productCarouselList

    fun fetchProductCarouselData(pageEndPoint: String) {
        launchCatchError(block = {
            productCarouselList.value = productCardCarouselUseCase.getProductCardCarouselUseCase(productCarouselComponentData.value?.id!!, getQueryParameterMap(), pageEndPoint)
        }, onError = {
            it.printStackTrace()
        })
    }

    private fun getQueryParameterMap(): MutableMap<String, Any> {
        val queryParameterMap = mutableMapOf<String, Any>()
        queryParameterMap[RPC_PAGE_NUMBER_KEY] = pageNumber.toString()
        queryParameterMap[RPC_PAGE_SIZE] = productPerPage.toString()
        return queryParameterMap
    }
}