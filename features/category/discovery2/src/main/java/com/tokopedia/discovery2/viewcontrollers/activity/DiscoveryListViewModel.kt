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

//        if (viewHolderViewModelList.size - 1 >= position) {
//            Log.d("getViewHolderModel", position.toString() + " " + viewHolderViewModelList[position])
//            return viewHolderViewModelList[position]
//        }
//        val viewModelObject = viewModel.call(applicationContext, componentItem, position)
//        viewHolderViewModelList.add(viewModelObject)
//        Log.d("getViewHolderModelafter", position.toString() + " " + viewModelObject + (viewHolderViewModelList.size - 1))
//        return viewModelObject

//        if (viewHolderViewModelList.size > position && viewHolderViewModelList[position] != null) {
//            return viewHolderViewModelList[position]
//        }
//        val viewModelObject = viewModel.call(applicationContext, componentItem, position)
//        if(viewHolderViewModelList.size < position){
//            for(i in viewHolderViewModelList.size until position){
//                viewHolderViewModelList.add(null)
//            }
//        }
//        viewHolderViewModelList.add(position, viewModelObject)
//        return viewModelObject
    }


//    //temp code
//    fun getInnerComponentViewModel(position: Int): DiscoveryBaseViewModel? {
//        if (viewHolderViewModelList.size - 1 >= position) {
//            return viewHolderViewModelList[position]
//        }
//        return null
//    }

    //temp code
    fun getInnerComponentViewModel(position: Int): DiscoveryBaseViewModel? {
        return mapOfViewModels[position]
    }

    fun clearList() {
        mapOfViewModels.clear()
//        viewHolderViewModelList.clear()
    }


    override fun onCleared() {
        super.onCleared()
        for ((k, v) in mapOfViewModels) {
            v.onCleared()
        }
//        viewHolderViewModelList.forEach { it.onCleared() }
    }
}