package com.tokopedia.tokopedianow.shoppinglist.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.tokopedianow.shoppinglist.domain.mapper.VisitableMapper.addRecommendationProducts
import javax.inject.Inject

class ShoppingListOtherProductsBottomSheetViewModel @Inject constructor(
    dispatchers: CoroutineDispatchers
): BaseViewModel(dispatchers.io)  {
    private val layout = mutableListOf<Visitable<*>>()

    private val _productRecommendation = MutableLiveData<List<Visitable<*>>>()
    val productRecommendation: LiveData<List<Visitable<*>>>
        get() = _productRecommendation

    fun loadLayout() {
        layout.addRecommendationProducts()

        _productRecommendation.value = layout
    }
}
