package com.tokopedia.discovery2.viewcontrollers.activity

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.tokopedia.discovery2.data.ComponentsItem
import kotlin.reflect.KFunction

class DiscoveryListViewModel(val applicationContext: Application) : AndroidViewModel(applicationContext) {
    val viewHolderViewModelList = ArrayList<DiscoveryBaseViewModel>()

    fun getViewModelList(viewModel: KFunction<DiscoveryBaseViewModel>, componentItem: ComponentsItem, position : Int): DiscoveryBaseViewModel {
        if (viewHolderViewModelList.size - 1 >= position) {
            return viewHolderViewModelList.get(position)
        }
        val viewModelObject = viewModel.call(componentItem, applicationContext);
        viewHolderViewModelList.add(viewModelObject)
        return viewModelObject

    }

    override fun onCleared() {
        super.onCleared()
        viewHolderViewModelList.forEach { it.onCleared() }
    }
}