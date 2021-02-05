package com.tokopedia.topads.view.model

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.topads.common.domain.usecase.TopAdsGroupValidateNameUseCase
import javax.inject.Inject

/**
 * Author errysuprayogi on 06,November,2019
 */
class CreateGroupAdsViewModel @Inject constructor(
        dispatcher: CoroutineDispatchers,
        private val topAdsGroupValidateNameUseCase: TopAdsGroupValidateNameUseCase) : BaseViewModel(dispatcher.main) {

    fun validateGroup(groupName: String, onSuccess: (() -> Unit),
                      onError: ((error: String) -> Unit)) {
        launchCatchError( block = {
            topAdsGroupValidateNameUseCase.setParams(groupName)
            topAdsGroupValidateNameUseCase.execute(
                    {
                        if (it.topAdsGroupValidateName.errors.isEmpty()) {
                            onSuccess()
                        } else {
                            onError(it.topAdsGroupValidateName.errors.first().detail)
                        }
                    },
                    {
                        onError(it.localizedMessage?:"")
                    }
            )
        }, onError = {
            onError(it.localizedMessage?:"")
        })
    }
}