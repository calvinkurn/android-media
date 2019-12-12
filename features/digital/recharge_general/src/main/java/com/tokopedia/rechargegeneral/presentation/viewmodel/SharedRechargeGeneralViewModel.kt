package com.tokopedia.rechargegeneral.presentation.viewmodel

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

    val recommendationItem = MutableLiveData<TopupBillsRecommendation>()
    val promoItem = MutableLiveData<Int>()

    fun setRecommendationItem(recommendation: TopupBillsRecommendation) {
        this.recommendationItem.value = recommendation
    }

    fun setPromoSelected(promoId: Int) {
        this.promoItem.value = promoId
    }
}