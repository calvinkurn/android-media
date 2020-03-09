package com.tokopedia.exploreCategory.viewmodel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.exploreCategory.ECAnalytics
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
            categoryGroup[defaultCategory]?.let {
                ECAnalytics.trackEventImpressionFeature(categoryGroup, it.title, it.id)
                ECAnalytics.trackEventImpressionIcon(categoryGroup[defaultCategory])
            }
            for (item in categoryGroup) {
                visitable.add(ECAccordionVHViewModel(item))
            }
            categories.value = visitable
        }
    }

    fun setNewOpenCategory(categoryIndex: Int) {
        val newCurrentCG = (visitable[categoryIndex] as ECAccordionVHViewModel).categoryGroup
        if (newCurrentCG?.isOpen == false)
            ECAnalytics.trackEventImpressionIcon(newCurrentCG)

        notifyAdapter.value = currentOpenCategory
        if (categoryIndex != currentOpenCategory) {
            (visitable[currentOpenCategory] as ECAccordionVHViewModel).categoryGroup?.isOpen = false
            (visitable[categoryIndex] as ECAccordionVHViewModel).categoryGroup?.isOpen = true
            currentOpenCategory = categoryIndex
        } else {
            (visitable[categoryIndex] as ECAccordionVHViewModel).categoryGroup?.isOpen = !(newCurrentCG?.isOpen
                    ?: false)
        }
        notifyAdapter.value = categoryIndex

        ECAnalytics.trackEventClickFeature(newCurrentCG, categoryIndex)
    }

    fun fireOnIconClickEvent(categoryTitle: String?, categoryId: Int?, categoryRow: CategoryGroup.CategoryRow?, position: Int) {
        ECAnalytics.trackEventClickIcon(categoryTitle,
                categoryId?.toString(),
                categoryRow,
                position)
    }

}