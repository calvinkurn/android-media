package com.tokopedia.usercomponents.tokopediaplus.ui

import androidx.lifecycle.LiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.usercomponents.common.wrapper.UserComponentsStateResult
import com.tokopedia.usercomponents.tokopediaplus.domain.TokopediaPlusDataModel
import com.tokopedia.usercomponents.tokopediaplus.domain.TokopediaPlusUseCase
import com.tokopedia.utils.lifecycle.SingleLiveEvent
import javax.inject.Inject

class TokopediaPlusViewModel @Inject constructor(
    private val tokopediaPlusUseCase: TokopediaPlusUseCase,
    dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.main) {

    private val _tokopediPlus = SingleLiveEvent<UserComponentsStateResult<TokopediaPlusDataModel>>()
    val tokopedisPlus: LiveData<UserComponentsStateResult<TokopediaPlusDataModel>>
        get() = _tokopediPlus

    fun loadTokopediPlus(source: String) {
        launchCatchError(coroutineContext, {
            _tokopediPlus.value = UserComponentsStateResult.Loading()

            val response = tokopediaPlusUseCase(mapOf(
                TokopediaPlusUseCase.PARAM_SOURCE to source
            ))

            _tokopediPlus.value = UserComponentsStateResult.Success(response.tokopediaPlus)
        }, {
            _tokopediPlus.value = UserComponentsStateResult.Fail(it)
        })
    }
}