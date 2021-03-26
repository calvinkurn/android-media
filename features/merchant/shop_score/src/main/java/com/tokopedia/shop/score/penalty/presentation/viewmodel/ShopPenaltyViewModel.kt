package com.tokopedia.shop.score.penalty.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.shop.score.penalty.domain.mapper.PenaltyMapper
import com.tokopedia.shop.score.penalty.presentation.model.BasePenaltyPage
import com.tokopedia.shop.score.penalty.presentation.model.PenaltyFilterUiModel
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.launch
import javax.inject.Inject

class ShopPenaltyViewModel @Inject constructor(
        coroutineDispatchers: CoroutineDispatchers,
        private val penaltyMapper: PenaltyMapper
) : BaseViewModel(coroutineDispatchers.main) {

    private val _penaltyPageData = MutableLiveData<Result<List<BasePenaltyPage>>>()
    val penaltyPageData: LiveData<Result<List<BasePenaltyPage>>>
        get() = _penaltyPageData

    private val _filterPenaltyData = MutableLiveData<List<PenaltyFilterUiModel>>()
    val filterPenaltyData: LiveData<List<PenaltyFilterUiModel>>
        get() = _filterPenaltyData

    fun getDataDummyPenalty() {
        launch {
            _penaltyPageData.value = Success(penaltyMapper.mapToPenaltyVisitableDummy())
        }
    }

    fun getFilterPenalty() {
        launch {
            _filterPenaltyData.value = penaltyMapper.mapToPenaltyFilterBottomSheet()
        }
    }
}