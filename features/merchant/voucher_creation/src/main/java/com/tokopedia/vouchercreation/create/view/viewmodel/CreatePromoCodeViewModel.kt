package com.tokopedia.vouchercreation.create.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.vouchercreation.create.domain.usecase.PromoCodeValidationUseCase
import com.tokopedia.vouchercreation.create.view.uimodel.validation.PromoCodeValidation
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named

class CreatePromoCodeViewModel @Inject constructor(
        @Named("Main") dispatcher: CoroutineDispatcher,
        private val promoCodeValidationUseCase: PromoCodeValidationUseCase
) : BaseViewModel(dispatcher) {

    private val mPromoCodeValidationLiveData = MutableLiveData<Result<PromoCodeValidation>>()
    val promoCodeValidationLiveData: LiveData<Result<PromoCodeValidation>>
        get() = mPromoCodeValidationLiveData

    fun validatePromoCode(promoCode: String) {
        launchCatchError(
                block = {
                    withContext(Dispatchers.IO) {
                        promoCodeValidationUseCase.params = PromoCodeValidationUseCase.createRequestParam(promoCode)
                        mPromoCodeValidationLiveData.postValue(Success(promoCodeValidationUseCase.executeOnBackground()))
                    }
                },
                onError = {
                    mPromoCodeValidationLiveData.value = Fail(it)
                }
        )
    }

}