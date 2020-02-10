package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.tokopoints

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.di.DaggerDiscoveryComponent
import com.tokopedia.discovery2.usecase.tokopointsUseCase.TokopointsListDataUseCase
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class TokopointsViewModel(val application: Application, components: ComponentsItem) : DiscoveryBaseViewModel(), CoroutineScope {
    private val tokopointsComponentData: MutableLiveData<ComponentsItem> = MutableLiveData()
    private lateinit var tokopointsList: ArrayList<DataItem>
    private val tokopointsMutableComponentData: MutableLiveData<ArrayList<ComponentsItem>> = MutableLiveData()
    @Inject
    lateinit var tokopointsListDataUseCase: TokopointsListDataUseCase
    private val RPC_PAGE_NUMBER_KEY = "rpc_page_number"
    private val RPC_PAGE_SIZE = "rpc_page_size"
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

    fun getComponentData() = tokopointsComponentData
    fun getTokopointsMutableListData() = tokopointsMutableComponentData

    fun fetchTokopointsListData() {
        tokopointsList = ArrayList()
        launchCatchError(block = {
            tokopointsMutableComponentData.value = tokopointsListDataUseCase.getTokopointsDataUseCase(tokopointsComponentData.value?.id!!, getQueryParamterMap())
        }, onError = {
            it.printStackTrace()
        })
    }

    private fun getQueryParamterMap(): MutableMap<String, Any> {
        val queryParamterMap = mutableMapOf<String, Any>()
        queryParamterMap[RPC_PAGE_NUMBER_KEY] = pageNumber.toString()
        queryParamterMap[RPC_PAGE_SIZE] = productPerPage.toString()
        return queryParamterMap
    }

}