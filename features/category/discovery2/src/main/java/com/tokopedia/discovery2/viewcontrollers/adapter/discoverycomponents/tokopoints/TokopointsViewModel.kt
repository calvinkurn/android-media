package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.tokopoints

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.di.DaggerDiscoveryComponent
import com.tokopedia.discovery2.usecase.tokopointsUseCase.TokopointsListDataUseCase
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

class TokopointsViewModel(val application: Application, components: ComponentsItem, val position: Int) : DiscoveryBaseViewModel(), CoroutineScope {
    private val tokopointsComponentData: MutableLiveData<ComponentsItem> = MutableLiveData()
    private val tokopointsList: MutableLiveData<ArrayList<ComponentsItem>> = MutableLiveData()
    @Inject
    lateinit var tokopointsListDataUseCase: TokopointsListDataUseCase
    private var pageNumber = 1
    private var productPerPage = 6

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + SupervisorJob()


    init {
        initDaggerInject()
        tokopointsComponentData.value = components
    }

    override fun initDaggerInject() {
        DaggerDiscoveryComponent.builder()
                .baseAppComponent((application.applicationContext as BaseMainApplication).baseAppComponent)
                .build()
                .inject(this)
    }

    fun getTokopointsComponentData() = tokopointsComponentData
    fun getTokopointsItemsListData() = tokopointsList

    fun fetchTokopointsListData(pageEndPoint: String) {
        if(tokopointsList.value.isNullOrEmpty()) {
            launchCatchError(block = {
                tokopointsList.value = tokopointsListDataUseCase.getTokopointsDataUseCase(tokopointsComponentData.value?.id.toIntOrZero(), getQueryParameterMap(), pageEndPoint)
            }, onError = {
                it.printStackTrace()
            })
        }
    }

    private fun getQueryParameterMap(): MutableMap<String, Any> {
        val queryParameterMap = mutableMapOf<String, Any>()
        queryParameterMap[RPC_PAGE_NUMBER_KEY] = pageNumber.toString()
        queryParameterMap[RPC_PAGE_SIZE] = productPerPage.toString()
        return queryParameterMap
    }
}