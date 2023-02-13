package com.tokopedia.sellerhome.view.helper

import androidx.lifecycle.MutableLiveData
import com.tokopedia.sellerhomecommon.presentation.model.BaseDataUiModel
import com.tokopedia.sellerhomecommon.presentation.model.CardDataUiModel
import com.tokopedia.sellerhomecommon.presentation.model.MilestoneDataUiModel
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.withContext

/**
 * Created by @ilhamsuaib on 22/11/22.
 */

internal suspend fun Flow<BaseDataUiModel?>.handleSseMessage(
    cardLiveData: MutableLiveData<Result<List<CardDataUiModel>>>,
    milestoneLiveData: MutableLiveData<Result<List<MilestoneDataUiModel>>>
) {
    collect { data ->
        withContext(Dispatchers.Main) {
            when (data) {
                is CardDataUiModel -> {
                    cardLiveData.value = Success(listOf(data))
                }
                is MilestoneDataUiModel -> {
                    milestoneLiveData.value = Success(listOf(data))
                }
            }
        }
    }
}