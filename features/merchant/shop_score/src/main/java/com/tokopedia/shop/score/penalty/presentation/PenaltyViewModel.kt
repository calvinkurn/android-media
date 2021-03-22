package com.tokopedia.shop.score.penalty.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.shop.score.penalty.domain.mapper.PenaltyMapper
import com.tokopedia.shop.score.penalty.presentation.model.BasePenaltyPage
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.launch
import javax.inject.Inject

class PenaltyViewModel @Inject constructor(
        coroutineDispatchers: CoroutineDispatchers,
        private val penaltyMapper: PenaltyMapper
) : BaseViewModel(coroutineDispatchers.main) {

    private val _penaltyPageData = MutableLiveData<Result<List<BasePenaltyPage>>>()
    val penaltyPageData: LiveData<Result<List<BasePenaltyPage>>>
        get() = _penaltyPageData

    fun getPenaltyDummyData() {
        launch {
            _penaltyPageData.value = Success(penaltyMapper.mapToPenaltyVisitableDummy())
        }
    }
}