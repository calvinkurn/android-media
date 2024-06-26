package com.tokopedia.shop.score.penalty.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.shop.score.penalty.domain.mapper.PenaltyDetailMapper
import com.tokopedia.shop.score.penalty.presentation.model.ItemPenaltyUiModel
import com.tokopedia.shop.score.penalty.presentation.model.ShopPenaltyDetailUiModel
import kotlinx.coroutines.launch
import javax.inject.Inject

class ShopPenaltyDetailViewModel @Inject constructor(
    dispatchers: CoroutineDispatchers,
    private val penaltyDetailMapper: PenaltyDetailMapper
) : BaseViewModel(dispatchers.main) {

    private val _penaltyDetailData = MutableLiveData<ShopPenaltyDetailUiModel>()
    val penaltyDetailData: LiveData<ShopPenaltyDetailUiModel>
        get() = _penaltyDetailData

    fun getPenaltyDetailData(itemPenaltyUiModel: ItemPenaltyUiModel) {
        launch {
            _penaltyDetailData.value = penaltyDetailMapper.mapToPenaltyDetail(itemPenaltyUiModel)
        }
    }
}
