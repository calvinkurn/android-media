package com.tokopedia.digital.home.old.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.digital.home.old.domain.*
import com.tokopedia.digital.home.old.model.DigitalHomePageItemModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class DigitalHomePageViewModel @Inject constructor(
        private val digitalHomePageUseCase: DigitalHomePageUseCase,
        private val dispatcher: CoroutineDispatchers)
    : BaseViewModel(dispatcher.main) {

    private val mutableDigitalHomePageList = MutableLiveData<List<DigitalHomePageItemModel>>()
    val digitalHomePageList: LiveData<List<DigitalHomePageItemModel>>
        get() = mutableDigitalHomePageList
    private val mutableIsAllError = MutableLiveData<Boolean>()
    val isAllError: LiveData<Boolean>
        get() = mutableIsAllError

    fun initialize(queryList: Map<String, String>) {
        val list: List<DigitalHomePageItemModel> = digitalHomePageUseCase.getEmptyList()
        digitalHomePageUseCase.queryList = queryList
        digitalHomePageUseCase.sectionOrdering = SECTION_ORDERING
        mutableDigitalHomePageList.value = list
        mutableIsAllError.value = false
    }

    fun getData(isLoadFromCloud: Boolean) {
        digitalHomePageUseCase.isFromCloud = isLoadFromCloud
        launch(dispatcher.io) {
            val data = digitalHomePageUseCase.executeOnBackground()
            if (data.isEmpty() || checkError(data)) {
                mutableIsAllError.postValue(true)
            } else {
                mutableDigitalHomePageList.postValue(data)
            }
        }
    }

    private fun checkError(data: List<DigitalHomePageItemModel>): Boolean {
        return data.all { !it.isSuccess }
    }

    companion object {
        const val CATEGORY_SECTION_ORDER: Int = 7
        val SECTION_ORDERING = mapOf(
                DigitalHomePageUseCase.BANNER_ORDER to 0,
                DigitalHomePageUseCase.FAVORITES_ORDER to 1,
                DigitalHomePageUseCase.TRUST_MARK_ORDER to 2,
                DigitalHomePageUseCase.RECOMMENDATION_ORDER to 3,
                DigitalHomePageUseCase.NEW_USER_ZONE_ORDER to 4,
                DigitalHomePageUseCase.SPOTLIGHT_ORDER to 5,
                DigitalHomePageUseCase.SUBSCRIPTION_ORDER to 6,
                DigitalHomePageUseCase.CATEGORY_ORDER to CATEGORY_SECTION_ORDER,
                DigitalHomePageUseCase.PROMO_ORDER to 8
        )
    }
}
