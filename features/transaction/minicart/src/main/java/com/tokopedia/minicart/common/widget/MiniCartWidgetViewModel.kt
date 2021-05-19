package com.tokopedia.minicart.common.widget

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.minicart.common.domain.usecase.GetMiniCartWidgetDataUseCase
import com.tokopedia.minicart.common.widget.uimodel.MiniCartWidgetUiModel
import javax.inject.Inject

class MiniCartWidgetViewModel @Inject constructor(private val executorDispatchers: CoroutineDispatchers,
                                                  private val getMiniCartWidgetDataUseCase: GetMiniCartWidgetDataUseCase) : BaseViewModel(executorDispatchers.main) {

    // Cart List UI Model
    private val _miniCartWidgetUiModel = MutableLiveData<MiniCartWidgetUiModel>()
    val miniCartWidgetUiModel: LiveData<MiniCartWidgetUiModel>
        get() = _miniCartWidgetUiModel

    fun getLatestState() {
        getMiniCartWidgetDataUseCase.execute(onSuccess = {
            _miniCartWidgetUiModel.value = MiniCartWidgetUiModel(
                    state = MiniCartWidgetUiModel.STATE_NORMAL,
                    totalProductCount = it.miniCartData.totalProductCount,
                    totalProductPrice = it.miniCartData.totalProductPrice,
                    totalProductError = it.miniCartData.totalProductError
            )
        }, onError = {
            _miniCartWidgetUiModel.value = MiniCartWidgetUiModel(
                    state = MiniCartWidgetUiModel.STATE_NORMAL,
                    totalProductCount = 0,
                    totalProductPrice = 0,
                    totalProductError = 0
            )
        })

    }
}