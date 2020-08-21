package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.quickfilter

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.datamapper.getComponent
import com.tokopedia.discovery2.di.DaggerDiscoveryComponent
import com.tokopedia.discovery2.repository.quickFilter.QuickFilterRepository
import com.tokopedia.discovery2.usecase.QuickFilterUseCase
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import java.util.HashMap
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class QuickFilterViewModel(val application: Application, val components: ComponentsItem, val position: Int) : DiscoveryBaseViewModel(), CoroutineScope {

    @Inject
    lateinit var quickFilterRepository: QuickFilterRepository
    @Inject
    lateinit var quickFilterUseCase: QuickFilterUseCase

    private val dynamicFilterModel: MutableLiveData<DynamicFilterModel> = MutableLiveData()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + SupervisorJob()

    init {
        initDaggerInject()
    }

    override fun initDaggerInject() {
        DaggerDiscoveryComponent.builder()
                .baseAppComponent((application.applicationContext as BaseMainApplication).baseAppComponent)
                .build()
                .inject(this)
    }

    fun getTargetComponent(): ComponentsItem? {
        return getComponent(components.properties?.targetId ?: "", components.pageEndPoint)
    }

    fun onFilterApplied(selectedFilter: HashMap<String, String>?, selectedSort: HashMap<String, String>?) {
        getTargetComponent()?.let { component ->
            component.selectedFilters = selectedFilter
            component.selectedSort = selectedSort
            launchCatchError(block = {
                if (quickFilterUseCase.onFilterApplied(component, selectedFilter, selectedSort)) {
                    syncData.value = true
                }
            }, onError = {
                it.printStackTrace()
            })
        }
    }

    fun fetchDynamicFilterModel() {
        launchCatchError(block = {
            dynamicFilterModel.value = quickFilterRepository.getQuickFilterData(components.id, mutableMapOf(), components.pageEndPoint)
        }, onError = {
            it.printStackTrace()
        })
    }

    fun getDynamicFilterModelLiveData() = dynamicFilterModel
}