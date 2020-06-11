package com.tokopedia.discovery2.viewcontrollers.activity

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.tokopedia.discovery2.data.ComponentsItem
import kotlin.reflect.KFunction


/** Future Improvement : Please don't remove any commented code from this file. Need to work on this **/
class DiscoveryListViewModel(private val applicationContext: Application) : AndroidViewModel(applicationContext) {
    //    private val viewHolderViewModelList = ArrayList<DiscoveryBaseViewModel>()
    private var mapOfViewModels = mutableMapOf<Int, DiscoveryBaseViewModel>()

    fun getViewHolderModel(viewModel: KFunction<DiscoveryBaseViewModel>, componentItem: ComponentsItem, position: Int): DiscoveryBaseViewModel {

        if (mapOfViewModels[position] == null) {
            val viewModelObject = viewModel.call(applicationContext, componentItem, position)
            mapOfViewModels[position] = viewModelObject
        }
        return mapOfViewModels[position]!!
    }

    //temp code
    fun getInnerComponentViewModel(position: Int): DiscoveryBaseViewModel? {
        return mapOfViewModels[position]
    }

    fun clearList() {
        mapOfViewModels.clear()
    }


    override fun onCleared() {
        super.onCleared()
        for ((k, v) in mapOfViewModels) {
            v.onCleared()
        }
//        viewHolderViewModelList.forEach { it.onCleared() }
    }
}