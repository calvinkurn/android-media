package com.tokopedia.rechargegeneral.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.common.topupbills.data.TopupBillsRecommendation
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import javax.inject.Inject

/**
 * Created by resakemal on 10/12/19.
 */
class SharedRechargeGeneralViewModel @Inject constructor(dispatcher: CoroutineDispatchers)
    : BaseViewModel(dispatcher.main) {

    private val mutableRecommendationItem = MutableLiveData<TopupBillsRecommendation>()
    val recommendationItem : LiveData<TopupBillsRecommendation>
        get() = mutableRecommendationItem

    fun setRecommendationItem(recommendation: TopupBillsRecommendation) {
        mutableRecommendationItem.postValue(recommendation)
    }
}