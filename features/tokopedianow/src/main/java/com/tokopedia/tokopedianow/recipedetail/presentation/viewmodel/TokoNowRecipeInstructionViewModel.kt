package com.tokopedia.tokopedianow.recipedetail.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import javax.inject.Inject

class TokoNowRecipeInstructionViewModel @Inject constructor(
    dispatchers: CoroutineDispatchers
): BaseViewModel(dispatchers.io) {

    val itemList: LiveData<List<Visitable<*>>>
        get() = _itemList

    private val _itemList = MutableLiveData<List<Visitable<*>>>()

    fun onViewCreated(items: List<Visitable<*>>) {
        _itemList.postValue(items)
    }
}