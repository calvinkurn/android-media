package com.tokopedia.topupbills.telco.view.viewmodel

import android.arch.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.topupbills.telco.data.TelcoProductDataCollection
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

/**
 * Created by nabillasabbaha on 20/05/19.
 */
class SharedProductTelcoViewModel @Inject constructor(val dispatcher: CoroutineDispatcher)
    : BaseViewModel(dispatcher) {

    val productItem = MutableLiveData<TelcoProductDataCollection>()
    val showTotalPrice = MutableLiveData<Boolean>()

    fun setProductSelected(productItem: TelcoProductDataCollection) {
        this.productItem.value = productItem
    }

    fun setShowTotalPrice(show: Boolean) {
        this.showTotalPrice.value = show
    }
}