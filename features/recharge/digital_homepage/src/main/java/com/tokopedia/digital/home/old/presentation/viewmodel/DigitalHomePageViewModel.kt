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
import com.tokopedia.digital.home.old.presentation.util.DigitalHomePageDispatchersProvider
import kotlinx.coroutines.launch
import javax.inject.Inject

class DigitalHomePageViewModel @Inject constructor(
        private val digitalHomePageUseCase: DigitalHomePageUseCase,
        private val dispatcher: DigitalHomePageDispatchersProvider)
    : BaseViewModel(dispatcher.Main) {

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
        launch(dispatcher.IO) {
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
                BANNER_ORDER to 0,
                FAVORITES_ORDER to 1,
                TRUST_MARK_ORDER to 2,
                RECOMMENDATION_ORDER to 3,
                NEW_USER_ZONE_ORDER to 4,
                SPOTLIGHT_ORDER to 5,
                SUBSCRIPTION_ORDER to 6,
                CATEGORY_ORDER to CATEGORY_SECTION_ORDER,
                PROMO_ORDER to 8
        )
    }
}
