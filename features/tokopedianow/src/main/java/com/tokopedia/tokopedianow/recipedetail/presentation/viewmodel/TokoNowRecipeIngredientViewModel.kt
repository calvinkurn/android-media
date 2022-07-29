package com.tokopedia.tokopedianow.recipedetail.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.tokopedianow.recipedetail.presentation.uimodel.IngredientTabUiModel
import javax.inject.Inject

class TokoNowRecipeIngredientViewModel @Inject constructor(
    dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.io) {

    val itemList: LiveData<List<Visitable<*>>>
        get() = _itemList

    private val _itemList = MutableLiveData<List<Visitable<*>>>()

    fun getLayout(data: IngredientTabUiModel?) {
        data?.run {
            val items = mutableListOf<Visitable<*>>().apply {
                add(data.buyAllProductItem)
                addAll(data.productList)
            }

            _itemList.postValue(items)
        }
    }
}