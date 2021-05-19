package com.tokopedia.minicart.cartlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.minicart.common.domain.usecase.GetMiniCartWidgetDataUseCase
import javax.inject.Inject

class MiniCartListViewModel @Inject constructor(private val executorDispatchers: CoroutineDispatchers,
                                                private val getMiniCartWidgetDataUseCase: GetMiniCartWidgetDataUseCase) : BaseViewModel(executorDispatchers.main) {

    // Cart List UI Model
    private val _cartListUiModel = MutableLiveData<MutableList<Visitable<*>>>()
    val cartListUiModel: LiveData<MutableList<Visitable<*>>>
        get() = _cartListUiModel

}