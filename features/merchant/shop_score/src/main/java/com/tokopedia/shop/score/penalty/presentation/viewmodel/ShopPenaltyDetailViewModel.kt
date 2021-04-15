package com.tokopedia.shop.score.penalty.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.shop.score.penalty.domain.mapper.PenaltyMapper
import com.tokopedia.shop.score.penalty.presentation.model.ShopPenaltyDetailUiModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class ShopPenaltyDetailViewModel @Inject constructor(
        private val dispatchers: CoroutineDispatchers,
        private val penaltyMapper: PenaltyMapper
) : BaseViewModel(dispatchers.main) {

    private val _penaltyDetailData = MutableLiveData<Result<ShopPenaltyDetailUiModel>>()
    val penaltyDetailData: LiveData<Result<ShopPenaltyDetailUiModel>>
        get() = _penaltyDetailData

    fun getPenaltyDetailData(statusPenalty: String) {
        launchCatchError(block = {
            _penaltyDetailData.value = Success(penaltyMapper.mapToPenaltyDetailDummy(statusPenalty))
        }, onError = {
            _penaltyDetailData.value = Fail(it)
        })
    }
}