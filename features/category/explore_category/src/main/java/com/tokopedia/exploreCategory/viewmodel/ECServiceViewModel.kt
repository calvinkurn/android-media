package com.tokopedia.exploreCategory.viewmodel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.exploreCategory.model.ECDynamicHomeIconData.DynamicHomeIcon.CategoryGroup
import com.tokopedia.exploreCategory.ui.viewholder.viewmodel.ECAccordionVHViewModel
import com.tokopedia.exploreCategory.usecase.ECDynamicHomeIconUseCase
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import javax.inject.Inject
import kotlin.collections.ArrayList

class ECServiceViewModel @Inject constructor(private val useCase: ECDynamicHomeIconUseCase) : BaseECViewModel() {

    var defaultCategory: Int = 0
    val categories = MutableLiveData<ArrayList<Visitable<*>>>()
    val notifyAdapter = MutableLiveData<Int>()
    val visitable = ArrayList<Visitable<*>>()
    var currentOpenCategory = 0

    fun getHomeIconData() {
        shimmerVisibility.value = true
        launchCatchError(block = {
            val response = useCase.getHomeIconData()
            setHomeIconLiveData(response.dynamicHomeIcon?.categoryGroup)
        }, onError = {
            shimmerVisibility.value = false
            it.printStackTrace()
            errorMessage.value = it.localizedMessage
        })
    }

    private fun setHomeIconLiveData(categoryGroup: List<CategoryGroup?>?) {
        shimmerVisibility.value = false
        if (categoryGroup.isNullOrEmpty()) {
            errorMessage.value = "Empty Response"
        } else {
            if ((defaultCategory) < categoryGroup.size) {
                categoryGroup[defaultCategory]?.isOpen = true
                currentOpenCategory = defaultCategory
            }
            for (item in categoryGroup) {
                visitable.add(ECAccordionVHViewModel(item))
            }
            categories.value = visitable
        }
    }

    fun setNewOpenCategory(categoryIndex: Int) {
        notifyAdapter.value = currentOpenCategory
        if (categoryIndex != currentOpenCategory) {
            (visitable[currentOpenCategory] as ECAccordionVHViewModel).categoryGroup?.isOpen = false
            (visitable[categoryIndex] as ECAccordionVHViewModel).categoryGroup?.isOpen = true
            currentOpenCategory = categoryIndex
        } else {
            val newIndexValue = (visitable[categoryIndex] as ECAccordionVHViewModel).categoryGroup?.isOpen
                    ?: false
            (visitable[categoryIndex] as ECAccordionVHViewModel).categoryGroup?.isOpen = !newIndexValue
        }
        notifyAdapter.value = categoryIndex
    }

}