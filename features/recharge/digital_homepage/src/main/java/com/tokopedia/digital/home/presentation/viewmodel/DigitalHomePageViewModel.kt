package com.tokopedia.digital.home.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.digital.home.domain.DigitalHomePageUseCase
import com.tokopedia.digital.home.domain.DigitalHomePageUseCase.Companion.BANNER_ORDER
import com.tokopedia.digital.home.domain.DigitalHomePageUseCase.Companion.CATEGORY_ORDER
import com.tokopedia.digital.home.domain.DigitalHomePageUseCase.Companion.FAVORITES_ORDER
import com.tokopedia.digital.home.domain.DigitalHomePageUseCase.Companion.NEW_USER_ZONE_ORDER
import com.tokopedia.digital.home.domain.DigitalHomePageUseCase.Companion.PROMO_ORDER
import com.tokopedia.digital.home.domain.DigitalHomePageUseCase.Companion.RECOMMENDATION_ORDER
import com.tokopedia.digital.home.domain.DigitalHomePageUseCase.Companion.SPOTLIGHT_ORDER
import com.tokopedia.digital.home.domain.DigitalHomePageUseCase.Companion.SUBSCRIPTION_ORDER
import com.tokopedia.digital.home.domain.DigitalHomePageUseCase.Companion.TRUST_MARK_ORDER
import com.tokopedia.digital.home.model.DigitalHomePageItemModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class DigitalHomePageViewModel @Inject constructor(
        dispatcher: CoroutineDispatcher,
        private val digitalHomePageUseCase: DigitalHomePageUseCase)
    : BaseViewModel(dispatcher) {

    private val _digitalHomePageList = MutableLiveData<List<DigitalHomePageItemModel>>()
    val digitalHomePageList: LiveData<List<DigitalHomePageItemModel>>
        get() = _digitalHomePageList
    private val _isAllError = MutableLiveData<Boolean>()
    val isAllError: LiveData<Boolean>
        get() = _isAllError

    fun initialize(queryList: Map<String, String>) {
        val list: List<DigitalHomePageItemModel> = digitalHomePageUseCase.getEmptyList()
        digitalHomePageUseCase.queryList = queryList
        digitalHomePageUseCase.sectionOrdering = SECTION_ORDERING
        _digitalHomePageList.value = list
        _isAllError.value = false
    }

    fun getData(isLoadFromCloud: Boolean) {
        digitalHomePageUseCase.isFromCloud = isLoadFromCloud
        launch(Dispatchers.IO) {
            val data = digitalHomePageUseCase.executeOnBackground()
            if (data.isEmpty() || checkError(data)) {
                _isAllError.postValue(true)
            } else {
                _digitalHomePageList.postValue(data)
            }
        }
    }

    private fun checkError(data: List<DigitalHomePageItemModel>): Boolean {
        return data.all { !it.isSuccess }
    }

    companion object {
        val SECTION_ORDERING = mapOf(
                BANNER_ORDER to 0,
                FAVORITES_ORDER to 1,
                TRUST_MARK_ORDER to 2,
                RECOMMENDATION_ORDER to 3,
                NEW_USER_ZONE_ORDER to 4,
                SPOTLIGHT_ORDER to 5,
                SUBSCRIPTION_ORDER to 6,
                CATEGORY_ORDER to 7,
                PROMO_ORDER to 8
        )
        val CATEGORY_SECTION_ORDER: Int = SECTION_ORDERING[CATEGORY_ORDER] ?: 0
    }
}
