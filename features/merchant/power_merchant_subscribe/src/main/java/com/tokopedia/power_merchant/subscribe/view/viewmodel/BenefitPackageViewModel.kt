package com.tokopedia.power_merchant.subscribe.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.power_merchant.subscribe.domain.usecase.GetBenefitPackageUseCase
import com.tokopedia.power_merchant.subscribe.view.model.BaseBenefitPackageUiModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import dagger.Lazy
import kotlinx.coroutines.withContext
import javax.inject.Inject

class BenefitPackageViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val userSession: UserSessionInterface,
    private val benefitPackageUseCase: Lazy<GetBenefitPackageUseCase>
) : BaseViewModel(dispatchers.main) {

    val benefitPackage: LiveData<Result<List<BaseBenefitPackageUiModel>>>
        get() = _benefitPackage

    private val _benefitPackage = MutableLiveData<Result<List<BaseBenefitPackageUiModel>>>()

    fun getBenefitPackage() {
        launchCatchError(block = {
            benefitPackageUseCase.get().setParams(userSession.shopId.toLongOrZero())
            val result = withContext(dispatchers.io) {
                return@withContext benefitPackageUseCase.get().executeOnBackground()
            }
            _benefitPackage.value = Success(result)
        }, onError = {
            _benefitPackage.value = Fail(it)
        })
    }
}