package com.tokopedia.rechargegeneral.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.common.topupbills.data.TopupBillsRecommendation
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

/**
 * Created by resakemal on 10/12/19.
 */
class SharedRechargeGeneralViewModel @Inject constructor(dispatcher: CoroutineDispatcher)
    : BaseViewModel(dispatcher) {

    private val _recommendationItem = MutableLiveData<TopupBillsRecommendation>()
    val recommendationItem : LiveData<TopupBillsRecommendation>
        get() = _recommendationItem

    fun setRecommendationItem(recommendation: TopupBillsRecommendation) {
        _recommendationItem.postValue(recommendation)
    }
}