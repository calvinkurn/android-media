package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.tokopoints

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.di.DaggerDiscoveryComponent
import com.tokopedia.discovery2.usecase.tokopointsusecase.TokopointsListDataUseCase
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext


class TokopointsViewModel(val application: Application, val component: ComponentsItem, val position: Int) : DiscoveryBaseViewModel(), CoroutineScope {
    private val tokopointsComponentData: MutableLiveData<ComponentsItem> = MutableLiveData()
    private val tokopointsList: MutableLiveData<ArrayList<ComponentsItem>> = MutableLiveData()

    @Inject
    lateinit var tokopointsListDataUseCase: TokopointsListDataUseCase


    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + SupervisorJob()


    init {
        tokopointsComponentData.value = component
    }


    fun getTokopointsComponentData() = tokopointsComponentData
    fun getTokopointsItemsListData() = tokopointsList

    fun fetchTokopointsListData(pageEndPoint: String) {
        if (tokopointsList.value.isNullOrEmpty()) {
            launchCatchError(block = {
                if (tokopointsListDataUseCase.getTokopointsDataUseCase(component.id, pageEndPoint)) {
                    tokopointsList.value = component.getComponentsItem() as ArrayList<ComponentsItem>?
                }
            }, onError = {
                it.printStackTrace()
            })
        }
    }


}