package com.tokopedia.minicart.cartlist

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.cartcommon.domain.model.bmgm.response.BmGmGetGroupProductTickerResponse
import com.tokopedia.minicart.cartlist.uimodel.MiniCartGwpGiftUiModel
import com.tokopedia.minicart.cartlist.uimodel.MiniCartListUiModel
import com.tokopedia.minicart.cartlist.uimodel.MiniCartProgressiveInfoUiModel

object MiniCartListUiModelExtension {
    fun MutableLiveData<MiniCartListUiModel>.updateGwpSuccessState(
        uiModels: MutableList<Visitable<*>>,
        miniCartListUiModelMapper: MiniCartListUiModelMapper,
        response: BmGmGetGroupProductTickerResponse
    ) {
        value = value?.copy(
            visitables = uiModels.mapNotNull { uiModel ->
                when (uiModel) {
                    is MiniCartProgressiveInfoUiModel -> miniCartListUiModelMapper.updateMiniCartProgressiveInfoUiModel(
                        response = response,
                        uiModel = uiModel
                    )
                    is MiniCartGwpGiftUiModel ->  miniCartListUiModelMapper.updateMiniCartGwpGiftUiModel(
                        response = response,
                        uiModel = uiModel
                    )
                    else -> uiModel
                }
            }.toMutableList()
        )
    }

    fun MutableLiveData<MiniCartListUiModel>.updateGwpLoadingState(
        uiModels: MutableList<Visitable<*>>,
        miniCartListUiModelMapper: MiniCartListUiModelMapper,
        offerId: Long
    ) {
        value = value?.copy(
            visitables = uiModels.map { uiModel ->
                if (uiModel is MiniCartProgressiveInfoUiModel && uiModel.offerId == offerId) miniCartListUiModelMapper.updateMiniCartProgressiveInfoUiModel(uiModel, MiniCartProgressiveInfoUiModel.State.LOADING) else uiModel
            }.toMutableList()
        )
    }

    fun MutableLiveData<MiniCartListUiModel>.updateGwpErrorState(
        uiModels: MutableList<Visitable<*>>,
        miniCartListUiModelMapper: MiniCartListUiModelMapper,
        offerId: Long
    ) {
        value = value?.copy(
            visitables = uiModels.map { uiModel ->
                if (uiModel is MiniCartProgressiveInfoUiModel && uiModel.offerId == offerId) miniCartListUiModelMapper.updateMiniCartProgressiveInfoUiModel(uiModel, MiniCartProgressiveInfoUiModel.State.ERROR) else uiModel
            }.toMutableList()
        )
    }
}
