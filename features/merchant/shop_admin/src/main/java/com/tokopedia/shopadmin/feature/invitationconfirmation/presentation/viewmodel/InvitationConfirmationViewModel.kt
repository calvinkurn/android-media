package com.tokopedia.shopadmin.feature.invitationconfirmation.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.shopadmin.common.domain.usecase.GetAdminTypeUseCaseCase
import com.tokopedia.shopadmin.common.presentation.uimodel.AdminTypeUiModel
import com.tokopedia.shopadmin.feature.invitationconfirmation.domain.param.InvitationConfirmationParam
import com.tokopedia.shopadmin.feature.invitationconfirmation.domain.usecase.AdminConfirmationRegUseCase
import com.tokopedia.shopadmin.feature.invitationconfirmation.domain.usecase.GetShopAdminInfoUseCase
import com.tokopedia.shopadmin.feature.invitationconfirmation.domain.usecase.UpdateUserProfileUseCase
import com.tokopedia.shopadmin.feature.invitationconfirmation.domain.usecase.ValidateAdminEmailUseCase
import com.tokopedia.shopadmin.feature.invitationconfirmation.presentation.uimodel.AdminConfirmationRegUiModel
import com.tokopedia.shopadmin.feature.invitationconfirmation.presentation.uimodel.ShopAdminInfoUiModel
import com.tokopedia.shopadmin.feature.invitationconfirmation.presentation.uimodel.UserProfileUpdateUiModel
import com.tokopedia.shopadmin.feature.invitationconfirmation.presentation.uimodel.ValidateAdminEmailUiModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import dagger.Lazy
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


class InvitationConfirmationViewModel @Inject constructor(
    private val coroutineDispatchers: CoroutineDispatchers,
    private val userSession: UserSessionInterface,
    private val getAdminTypeUseCaseCase: Lazy<GetAdminTypeUseCaseCase>,
    private val getShopAdminInfoUseCase: Lazy<GetShopAdminInfoUseCase>,
    private val adminConfirmationRegUseCase: Lazy<AdminConfirmationRegUseCase>,
    private val validateAdminEmailUseCase: Lazy<ValidateAdminEmailUseCase>,
    private val updateUserProfileUseCase: Lazy<UpdateUserProfileUseCase>,
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

    private val _updateUserProfile = MutableLiveData<Result<UserProfileUpdateUiModel>>()
    val updateUserProfile: LiveData<Result<UserProfileUpdateUiModel>>
        get() = _updateUserProfile

    private val _emailParam = MutableStateFlow("")

    private val _validateEmail = MutableSharedFlow<Result<ValidateAdminEmailUiModel>>(Int.ONE)
    val validateEmail: Flow<Result<ValidateAdminEmailUiModel>> = _validateEmail

    init {
        viewModelScope.launch {
            _emailParam
                .debounce(DEBOUNCE_DELAY_MILLIS)
                .flatMapLatest { email ->
                    fetchValidateEmailUseCase(
                        userSession.shopId,
                        email,
                        invitationConfirmationParam.getShopManageId()
                    ).catch { emit(Fail(it)) }
                }
                .flowOn(coroutineDispatchers.io)
                .collect {
                    _validateEmail.emit(it)
                }
        }
    }

    fun fetchAdminType() {
        launchCatchError(block = {
            val adminTypeData = withContext(coroutineDispatchers.io) {
                getAdminTypeUseCaseCase.get().execute()
            }
            _adminType.value = Success(adminTypeData)
        }, onError = {
            _adminType.value = Fail(it)
        })
    }

    fun fetchShopAdminInfo() {
        launchCatchError(block = {
            val shopAdminInfoData = withContext(coroutineDispatchers.io) {
                getShopAdminInfoUseCase.get().execute(userSession.shopId.toLongOrZero())
            }
            _shopAdminInfo.value = Success(shopAdminInfoData)
        }, onError = {
            _shopAdminInfo.value = Fail(it)
        })
    }

    fun adminConfirmationReg(acceptBecomeAdmin: Boolean) {
        launchCatchError(block = {
            val confirmationRegResponse = withContext(coroutineDispatchers.io) {
                adminConfirmationRegUseCase.get().execute(
                    userSession.shopId,
                    userSession.userId,
                    acceptBecomeAdmin,
                    invitationConfirmationParam.getShopManageId()
                )
            }
            _confirmationReg.value = Success(confirmationRegResponse)
        }, onError = {
            _confirmationReg.value = Fail(it)
        })
    }

    fun updateUserProfile(email: String) {
        launchCatchError(block = {
            val updateUserProfileData = withContext(coroutineDispatchers.io) {
                updateUserProfileUseCase.get().execute(email)
            }
            _updateUserProfile.value = Success(updateUserProfileData)
        }, onError = {
            _updateUserProfile.value = Fail(it)
        })
    }

    private fun fetchValidateEmailUseCase(
        shopId: String,
        email: String,
        manageID: String
    ): Flow<Result<ValidateAdminEmailUiModel>> {
        return flow {
            val result = validateAdminEmailUseCase.get().execute(shopId, email, manageID)
            emit(Success(result))
        }
    }

    fun validateAdminEmail(email: String) {
        _emailParam.tryEmit(email)
    }

    companion object {
        const val DEBOUNCE_DELAY_MILLIS = 300L
    }
}