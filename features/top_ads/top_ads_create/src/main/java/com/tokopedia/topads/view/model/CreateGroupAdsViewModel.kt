package com.tokopedia.topads.view.model

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.topads.common.domain.usecase.TopAdsGroupValidateNameUseCase
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * Author errysuprayogi on 06,November,2019
 */
class CreateGroupAdsViewModel @Inject constructor(
        dispatcher: CoroutineDispatchers,
        private val userSession: UserSessionInterface,
        private val topAdsGroupValidateNameUseCase: TopAdsGroupValidateNameUseCase) : BaseViewModel(dispatcher.main) {

    fun validateGroup(groupName: String, onSuccess: (() -> Unit),
                      onError: ((Throwable) -> Unit)) {
        launchCatchError( block = {
            topAdsGroupValidateNameUseCase.setParams(userSession.shopId.toIntOrZero(), groupName)
            topAdsGroupValidateNameUseCase.execute(
                    {
                        if (it.topAdsGroupValidateName.errors.isEmpty()) {
                            onSuccess()
                        } else {
                            onError(Exception(it.topAdsGroupValidateName.errors.first().detail))
                        }
                    },
                    {
                        onError(it)
                    }
            )
        }, onError = {
            onError(it)
        })
    }
}