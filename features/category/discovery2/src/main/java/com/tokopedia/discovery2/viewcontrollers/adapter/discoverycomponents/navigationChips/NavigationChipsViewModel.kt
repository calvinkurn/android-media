package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.navigationChips

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.usecase.ChildCategoryUseCase
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class NavigationChipsViewModel (val application: Application, val components: ComponentsItem, val position: Int) : DiscoveryBaseViewModel(), CoroutineScope {
    private val listData: MutableLiveData<ArrayList<ComponentsItem>> = MutableLiveData()

    @Inject
    lateinit var childCategoryUseCase: ChildCategoryUseCase

    init {
        getCategories()
    }

    private fun getCategories() {
        launchCatchError(block = {
            listData.value = childCategoryUseCase.getChildCategory(components.id, components.pageEndPoint) as ArrayList<ComponentsItem>
        }, onError = {
            it.printStackTrace()
        })
    }

    fun getListDataLiveData(): MutableLiveData<ArrayList<ComponentsItem>> {
        return listData
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + SupervisorJob()

}