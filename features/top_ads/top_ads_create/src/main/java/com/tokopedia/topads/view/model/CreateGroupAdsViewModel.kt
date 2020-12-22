package com.tokopedia.topads.view.model

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.topads.common.data.response.ResponseGroupValidateName
import com.tokopedia.topads.common.domain.usecase.ValidGroupUseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject
import javax.inject.Named

/**
 * Author errysuprayogi on 06,November,2019
 */
class CreateGroupAdsViewModel @Inject constructor(
        @Named("Main")
        private val dispatcher: CoroutineDispatcher,
        private val validGroupUseCase: ValidGroupUseCase) : BaseViewModel(dispatcher) {


    fun validateGroup(groupName: String, onSuccess: (() -> Unit),
                      onError: ((error: String) -> Unit)) {
        validGroupUseCase.setParams(groupName)
        validGroupUseCase.executeQuerySafeMode(
                {
                    if (it.errors.isEmpty())
                        onSuccess()
                    else
                        onError(it.errors.firstOrNull()?.detail ?: "")
                },
                { throwable ->
                    throwable.printStackTrace()
                })
    }
}