package com.tokopedia.vouchercreation.create.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.vouchercreation.create.domain.usecase.validation.PromoCodeValidationUseCase
import com.tokopedia.vouchercreation.create.view.uimodel.validation.PromoCodeValidation
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CreatePromoCodeViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val promoCodeValidationUseCase: PromoCodeValidationUseCase
) : BaseViewModel(dispatchers.main) {

    private val mPromoCodeValidationLiveData = MutableLiveData<Result<PromoCodeValidation>>()
    val promoCodeValidationLiveData: LiveData<Result<PromoCodeValidation>>
        get() = mPromoCodeValidationLiveData

    fun validatePromoCode(promoCode: String) {
        launchCatchError(
                block = {
                    mPromoCodeValidationLiveData.value = Success(withContext(dispatchers.io) {
                        promoCodeValidationUseCase.params = PromoCodeValidationUseCase.createRequestParam(promoCode)
                        promoCodeValidationUseCase.executeOnBackground()
                    })
                },
                onError = {
                    mPromoCodeValidationLiveData.value = Fail(it)
                }
        )
    }

}