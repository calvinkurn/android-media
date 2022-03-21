package com.tokopedia.tokofood.purchase.purchasepage.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class TokoFoodPurchaseViewModel @Inject constructor(dispatcher: CoroutineDispatcher)
    : BaseViewModel(dispatcher) {

    private val _visitables = MutableLiveData<Visitable<*>>()
    val visitables: LiveData<Visitable<*>>
        get() = _visitables


}