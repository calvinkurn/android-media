package com.tokopedia.minicart.cartlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.minicart.cartlist.uimodel.MiniCartUiModel
import com.tokopedia.minicart.common.domain.usecase.GetMiniCartListUseCase
import javax.inject.Inject

class MiniCartListViewModel @Inject constructor(private val executorDispatchers: CoroutineDispatchers,
                                                private val getMiniCartListUseCase: GetMiniCartListUseCase,
                                                private val miniCartListViewHolderMapper: MiniCartListViewHolderMapper) : BaseViewModel(executorDispatchers.main) {

    private val _miniCartUiModel = MutableLiveData<MiniCartUiModel>()
    val miniCartUiModel: LiveData<MiniCartUiModel>
        get() = _miniCartUiModel

    fun getCartList(shopIds: List<String>) {
        getMiniCartListUseCase.setParams(shopIds)
        getMiniCartListUseCase.execute(onSuccess = {
            _miniCartUiModel.value = miniCartListViewHolderMapper.mapUiModel(it)
        }, onError = {
            _miniCartUiModel.value = MiniCartUiModel().apply {
                title = "template: Belanjaanmu di TokoNOW!"
            }
        })

    }
}