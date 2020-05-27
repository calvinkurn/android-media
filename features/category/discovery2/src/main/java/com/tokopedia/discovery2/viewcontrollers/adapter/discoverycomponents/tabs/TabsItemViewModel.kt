package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.tabs

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.usecase.tabsusecase.TabsUseCase
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class TabsItemViewModel(val application: Application, components: ComponentsItem) : DiscoveryBaseViewModel(), CoroutineScope {

    var tabsUseCase: TabsUseCase = TabsUseCase()

    private val componentData: MutableLiveData<ComponentsItem> = MutableLiveData()
    private val compositeComponentList: MutableLiveData<List<ComponentsItem>> = MutableLiveData()

    init {
        componentData.value = components
    }

    fun getComponentLiveData(): LiveData<ComponentsItem> {
        return componentData
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + SupervisorJob()

    override fun initDaggerInject() {

    }

    fun getCompositeComponentLiveData(): LiveData<List<ComponentsItem>> {
        return compositeComponentList
    }

    fun populateTabCompositeComponents(selectedTabData: DataItem) {
        compositeComponentList.value = tabsUseCase.getComponentsWithID(selectedTabData)
    }
}