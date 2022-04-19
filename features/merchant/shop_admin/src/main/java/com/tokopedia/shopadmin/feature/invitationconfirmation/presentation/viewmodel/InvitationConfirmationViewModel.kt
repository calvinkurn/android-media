package com.tokopedia.shopadmin.feature.invitationconfirmation.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.shopadmin.feature.invitationconfirmation.domain.param.InvitationConfirmationParam
import com.tokopedia.shopadmin.feature.invitationconfirmation.domain.usecase.AdminConfirmationRegUseCase
import com.tokopedia.shopadmin.feature.invitationconfirmation.domain.usecase.GetAdminTypeUseCaseCase
import com.tokopedia.shopadmin.feature.invitationconfirmation.domain.usecase.GetShopAdminInfoUseCase
import com.tokopedia.shopadmin.feature.invitationconfirmation.domain.usecase.ValidateAdminEmailUseCase
import com.tokopedia.shopadmin.feature.invitationconfirmation.presentation.model.AdminConfirmationRegUiModel
import com.tokopedia.shopadmin.feature.invitationconfirmation.presentation.model.AdminTypeUiModel
import com.tokopedia.shopadmin.feature.invitationconfirmation.presentation.model.ShopAdminInfoUiModel
import com.tokopedia.shopadmin.feature.invitationconfirmation.presentation.model.ValidateAdminEmailEvent
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import dagger.Lazy
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.withContext
import javax.inject.Inject

class InvitationConfirmationViewModel @Inject constructor(
    private val coroutineDispatchers: CoroutineDispatchers,
    private val getAdminTypeUseCaseCase: Lazy<GetAdminTypeUseCaseCase>,
    private val getShopAdminInfoUseCase: Lazy<GetShopAdminInfoUseCase>,
    private val adminConfirmationRegUseCase: Lazy<AdminConfirmationRegUseCase>,
    private val validateAdminEmailUseCase: Lazy<ValidateAdminEmailUseCase>,
    private val invitationConfirmationParam: InvitationConfirmationParam
) : BaseViewModel(coroutineDispatchers.main) {

    private val _shopAdminInfo = MutableLiveData<Result<ShopAdminInfoUiModel>>()
    val shopAdminInfo: LiveData<Result<ShopAdminInfoUiModel>>
        get() = _shopAdminInfo

    private val _adminType = MutableLiveData<Result<AdminTypeUiModel>>()
    val adminType: LiveData<Result<AdminTypeUiModel>>
        get() = _adminType

    private val _confirmationReg = MutableLiveData<Result<AdminConfirmationRegUiModel>>()
    val confirmationReg: LiveData<Result<AdminConfirmationRegUiModel>>
        get() = _confirmationReg

    private val _emailParam = MutableStateFlow("")

    private val _validateEmail = MutableSharedFlow<ValidateAdminEmailEvent>(replay = 1)
    val validateEmail = _validateEmail.shareIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 1)

    init {
        fetchValidateAdminEmail()
    }

    fun fetchAdminInfo() {
        launchCatchError(block = {
            val adminInfoData = withContext(coroutineDispatchers.io) {
                getAdminTypeUseCaseCase.get().execute()
            }
            _adminType.value = Success(adminInfoData)
        }, onError = {
            _adminType.value = Fail(it)
        })
    }

    fun fetchShopAdminInfo(shopId: Long) {
        launchCatchError(block = {
            val shopAdminInfoData = withContext(coroutineDispatchers.io) {
                getShopAdminInfoUseCase.get().execute(shopId)
            }
            _shopAdminInfo.value = Success(shopAdminInfoData)
        }, onError = {
            _shopAdminInfo.value = Fail(it)
        })
    }

    fun adminConfirmationReg(userId: String, email: String, acceptBecomeAdmin: Boolean) {
        launchCatchError(block = {
            val confirmationRegResponse = withContext(coroutineDispatchers.io) {
                adminConfirmationRegUseCase.get().execute(invitationConfirmationParam.getShopId(), userId, email,
                    invitationConfirmationParam.getOtpToken(),
                    acceptBecomeAdmin, invitationConfirmationParam.getShopManageId()
                )
            }
            _confirmationReg.value = Success(confirmationRegResponse)
        }, onError = {
            _confirmationReg.value = Fail(it)
        })
    }

    private fun fetchValidateAdminEmail() {
        viewModelScope.launchCatchError(block =  {
            _emailParam.debounce(DEBOUNCE_DELAY_MILLIS).distinctUntilChanged().collectLatest { email ->
                val validateEmailResponse = withContext(coroutineDispatchers.io) {
                    validateAdminEmailUseCase.get().execute(
                        invitationConfirmationParam.getShopId(),
                        email,
                        invitationConfirmationParam.getShopManageId()
                    )
                }
                _validateEmail.emit(ValidateAdminEmailEvent.Success(validateEmailResponse))
            }
        }, onError = {
            _validateEmail.emit(ValidateAdminEmailEvent.Error(it))
        })
    }

    fun validateAdminEmail(email: String) {
        _emailParam.tryEmit(email)
    }

    companion object {
        const val DEBOUNCE_DELAY_MILLIS = 300L
    }
}