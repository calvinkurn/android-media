package com.tokopedia.topads.view.model

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.topads.common.domain.usecase.TopAdsGroupValidateNameUseCase
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

/**
 * Author errysuprayogi on 06,November,2019
 */
class CreateGroupAdsViewModel @Inject constructor(
        @Named("Main")
        private val dispatcher: CoroutineDispatcher,
        private val userSession: UserSessionInterface,
        private val topAdsGroupValidateNameUseCase: TopAdsGroupValidateNameUseCase) : BaseViewModel(dispatcher) {

    fun validateGroup(groupName: String, onSuccess: (() -> Unit),
                      onError: ((Throwable) -> Unit)) {
        launch {
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
        }
    }
}