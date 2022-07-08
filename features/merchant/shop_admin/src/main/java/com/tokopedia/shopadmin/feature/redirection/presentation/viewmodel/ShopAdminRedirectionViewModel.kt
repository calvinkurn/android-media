package com.tokopedia.shopadmin.feature.redirection.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.shopadmin.common.domain.usecase.GetAdminTypeUseCaseCase
import com.tokopedia.shopadmin.common.presentation.uimodel.AdminTypeUiModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import dagger.Lazy
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ShopAdminRedirectionViewModel @Inject constructor(
    private val coroutineDispatchers: CoroutineDispatchers,
    private val getAdminTypeUseCaseCase: Lazy<GetAdminTypeUseCaseCase>
) : BaseViewModel(coroutineDispatchers.main) {

    private val _adminType = MutableLiveData<Result<AdminTypeUiModel>>()
    val adminType: LiveData<Result<AdminTypeUiModel>>
        get() = _adminType

    fun fetchAdminType() {
        launchCatchError(block = {
            val adminTypeUiModel = withContext(coroutineDispatchers.io) {
                getAdminTypeUseCaseCase.get().execute()
            }
            _adminType.value = Success(adminTypeUiModel)
        }, onError = {
            _adminType.value = Fail(it)
        })
    }
}