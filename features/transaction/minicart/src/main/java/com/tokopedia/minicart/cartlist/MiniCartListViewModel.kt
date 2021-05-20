package com.tokopedia.minicart.cartlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.minicart.common.domain.usecase.GetMiniCartListUseCase
import com.tokopedia.minicart.common.domain.usecase.GetMiniCartWidgetDataUseCase
import com.tokopedia.minicart.common.widget.uimodel.MiniCartWidgetUiModel
import javax.inject.Inject

class MiniCartListViewModel @Inject constructor(private val executorDispatchers: CoroutineDispatchers,
                                                private val getMiniCartListUseCase: GetMiniCartListUseCase,
                                                private val miniCartListViewHolderMapper: MiniCartListViewHolderMapper) : BaseViewModel(executorDispatchers.main) {

    // Cart List UI Model
    private val _cartListUiModel = MutableLiveData<MutableList<Visitable<*>>>()
    val cartListUiModel: LiveData<MutableList<Visitable<*>>>
        get() = _cartListUiModel

    fun getCartList() {
        getMiniCartListUseCase.execute(onSuccess = {
            _cartListUiModel.value = miniCartListViewHolderMapper.mapUiModel(it)
        }, onError = {
            _cartListUiModel.value = mutableListOf<Visitable<*>>()
        })

    }
}