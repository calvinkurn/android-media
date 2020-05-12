package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.productcardrevamp

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.di.DaggerDiscoveryComponent
import com.tokopedia.discovery2.usecase.productCardCarouselUseCase.ProductCardCarouselUseCase
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class ProductCardRevampViewModel(val application: Application, components: ComponentsItem) : DiscoveryBaseViewModel(), CoroutineScope {
    private val productCarouselComponentData: MutableLiveData<ComponentsItem> = MutableLiveData()
    private val productCarouselList: MutableLiveData<ArrayList<ComponentsItem>> = MutableLiveData()
    @Inject
    lateinit var productCardCarouselUseCase: ProductCardCarouselUseCase

    private val RPC_PAGE_NUMBER_KEY = "rpc_page_number"
    private val RPC_PAGE_SIZE = "rpc_page_size"
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


    fun getProductCarouselItemsListData(last: Boolean): LiveData<ArrayList<ComponentsItem>> {

        return productCarouselList
    }

    fun fetchProductCarouselData(pageEndPoint: String, queryMap: MutableMap<String, Any> = getQueryParameterMap()) {
        launchCatchError(block = {
            val list = productCardCarouselUseCase.getProductCardCarouselUseCase(
                    productCarouselComponentData.value?.id.toIntOrZero(),
                    queryMap,
                    pageEndPoint)
            Log.d("page no", pageNumber.toString())
            productPerPageSize = list.size
            productCarouselList.value = list
        }, onError = {
            it.printStackTrace()
        })
    }

    private fun getQueryParameterMap(pageNum: Int = pageNumber): MutableMap<String, Any> {
        val queryParameterMap = mutableMapOf<String, Any>()
        queryParameterMap[RPC_PAGE_NUMBER_KEY] = pageNum.toString()
        queryParameterMap[RPC_PAGE_SIZE] = productPerPage.toString()
        return queryParameterMap
    }

    fun fetchProductCarouselDataSecond(pageEndPoint: String) {
        pageNumber++
        Log.d("page no", pageNumber.toString())
        if (!(productPerPageSize < 20)) {
            fetchProductCarouselData(pageEndPoint, getQueryParameterMap(pageNumber))
        }


    }
}