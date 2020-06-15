package com.tokopedia.discovery2.viewcontrollers.activity

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.tokopedia.discovery2.data.ComponentsItem
import kotlin.reflect.KFunction


class DiscoveryListViewModel(private val applicationContext: Application) : AndroidViewModel(applicationContext) {
    private var mapOfViewModels = mutableMapOf<Int, DiscoveryBaseViewModel>()

    fun getViewHolderModel(viewModel: (application: Application,  components: ComponentsItem, position: Int)->DiscoveryBaseViewModel, componentItem: ComponentsItem, position: Int): DiscoveryBaseViewModel {

        if (mapOfViewModels[position] == null) {
            val viewModelObject = viewModel(applicationContext, componentItem, position)
            mapOfViewModels[position] = viewModelObject
        }
        return mapOfViewModels[position]!!
    }

    fun clearList() {
        mapOfViewModels.clear()
    }


    override fun onCleared() {
        super.onCleared()
        for ((k, v) in mapOfViewModels) {
            v.onCleared()
        }
    }
}