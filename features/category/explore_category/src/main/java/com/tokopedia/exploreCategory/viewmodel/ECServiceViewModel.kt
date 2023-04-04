package com.tokopedia.exploreCategory.viewmodel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.exploreCategory.ECAnalytics
import com.tokopedia.exploreCategory.model.ECDynamicHomeIconData.DynamicHomeIcon.CategoryGroup
import com.tokopedia.exploreCategory.ui.viewholder.viewmodel.ECAccordionVHViewModel
import com.tokopedia.exploreCategory.usecase.ECDynamicHomeIconUseCase
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.trackingoptimizer.TrackingQueue
import javax.inject.Inject
import kotlin.collections.ArrayList

class ECServiceViewModel @Inject constructor(
        private val useCase: ECDynamicHomeIconUseCase,
        private val trackingQueue: TrackingQueue) : BaseECViewModel() {

    val categories = MutableLiveData<ArrayList<Visitable<*>>>()
    val notifyAdapter = MutableLiveData<Int>()
    val visitable = ArrayList<Visitable<*>>()
    var currentActiveCategory = 0

    override fun doOnCreate() {
        super.doOnCreate()
        getHomeIconData()
    }

    private fun getHomeIconData() {
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
            if ((currentActiveCategory) < categoryGroup.size) {
                categoryGroup[currentActiveCategory]?.isOpen = true
            }
            categoryGroup[currentActiveCategory]?.let {
                ECAnalytics.trackEventImpressionFeature(categoryGroup, it.title, it.id)
                ECAnalytics.trackEventImpressionIcon(it, trackingQueue)
            }
            for (item in categoryGroup) {
                visitable.add(ECAccordionVHViewModel(item))
            }
            categories.value = visitable
        }
    }

    fun onAccordionClicked(categoryIndex: Int) {
        val newCurrentCG = (visitable[categoryIndex] as ECAccordionVHViewModel).categoryGroup
        if (newCurrentCG?.isOpen == false) {
            ECAnalytics.trackEventImpressionIcon(newCurrentCG, trackingQueue)
            ECAnalytics.trackEventClickAccordion(newCurrentCG.title)
        }
        notifyAdapter.value = currentActiveCategory
        if (categoryIndex != currentActiveCategory) {
            (visitable[currentActiveCategory] as ECAccordionVHViewModel).categoryGroup?.isOpen = false
            (visitable[categoryIndex] as ECAccordionVHViewModel).categoryGroup?.isOpen = true
            currentActiveCategory = categoryIndex
        } else {
            (visitable[categoryIndex] as ECAccordionVHViewModel).categoryGroup?.isOpen = !(newCurrentCG?.isOpen
                    ?: false)
        }
        notifyAdapter.value = categoryIndex

    }


    override fun doOnPause() {
        super.doOnPause()
        trackingQueue.sendAll()
    }

}