package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.productcardrevamp

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

private const val RPC_PAGE_NUMBER_KEY = "rpc_page_number"
private const val RPC_PAGE_SIZE = "rpc_page_size"

class ProductCardRevampViewModel(val application: Application, components: ComponentsItem, val position: Int) : DiscoveryBaseViewModel(), CoroutineScope {
    private val productCarouselComponentData: MutableLiveData<ComponentsItem> = MutableLiveData()
    private val productCarouselList: MutableLiveData<ArrayList<ComponentsItem>> = MutableLiveData()

    @Inject
    lateinit var productCardsUseCase: ProductCardsUseCase

    private var pageNumber = 1
    private var productPerPage = 20
    private var productPerPageSize = 20

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


    fun getProductCarouselItemsListData(): LiveData<ArrayList<ComponentsItem>> {

        return productCarouselList
    }

    fun fetchProductsData(pageEndPoint: String, queryMap: MutableMap<String, Any> = getQueryParameterMap()) {
        if (productCarouselList.value.isNullOrEmpty()) {
            launchCatchError(block = {
                val list = productCardsUseCase.getProductCardsUseCase(
                        productCarouselComponentData.value?.id.toIntOrZero(),
                        queryMap,
                        pageEndPoint, productCarouselComponentData.value?.name)
                productPerPageSize = list.size
                productCarouselList.value = list
            }, onError = {
                it.printStackTrace()
            })
        }
    }

    private fun getQueryParameterMap(pageNum: Int = pageNumber): MutableMap<String, Any> {
        val queryParameterMap = mutableMapOf<String, Any>()
        queryParameterMap[RPC_PAGE_NUMBER_KEY] = pageNum.toString()
        queryParameterMap[RPC_PAGE_SIZE] = productPerPage.toString()
        return queryParameterMap
    }

    fun fetchProductCarouselDataSecond(pageEndPoint: String) {
        pageNumber++
        if (productPerPageSize >= 20) {
            fetchProductsData(pageEndPoint, getQueryParameterMap(pageNumber))
        }


    }
}