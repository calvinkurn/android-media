package com.tokopedia.discovery2.viewcontrollers.activity

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.tokopedia.discovery2.data.ComponentsItem
import kotlin.reflect.KFunction

class DiscoveryListViewModel(private val applicationContext: Application) : AndroidViewModel(applicationContext) {
    private val viewHolderViewModelList = ArrayList<DiscoveryBaseViewModel>()

    fun getViewHolderModel(viewModel: KFunction<DiscoveryBaseViewModel>, componentItem: ComponentsItem, position: Int): DiscoveryBaseViewModel {
        if (viewHolderViewModelList.size - 1 >= position) {
            return viewHolderViewModelList[position]
        }
        val viewModelObject = viewModel.call(applicationContext, componentItem);
        viewHolderViewModelList.add(viewModelObject)
        return viewModelObject
    }

    //temp code
    fun getInnerComponentViewModel(position: Int): DiscoveryBaseViewModel? {
        if (viewHolderViewModelList.size - 1 >= position) {
            return viewHolderViewModelList[position]
        }
        return null
    }

    fun clearList(){
        viewHolderViewModelList.clear()
    }


    override fun onCleared() {
        super.onCleared()
        viewHolderViewModelList.forEach { it.onCleared() }
    }
}