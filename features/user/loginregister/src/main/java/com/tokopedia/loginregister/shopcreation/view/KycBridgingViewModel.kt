package com.tokopedia.loginregister.shopcreation.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.loginregister.common.domain.usecase.RegisterCheckUseCase
import com.tokopedia.loginregister.shopcreation.common.ShopCreationConstant
import com.tokopedia.loginregister.shopcreation.data.ShopStatus
import com.tokopedia.loginregister.shopcreation.domain.GetShopStatusUseCase
import com.tokopedia.loginregister.shopcreation.domain.GetUserProfileCompletionUseCase
import com.tokopedia.loginregister.shopcreation.domain.ProjectInfoResult
import com.tokopedia.loginregister.shopcreation.domain.ProjectInfoUseCase
import com.tokopedia.loginregister.shopcreation.domain.ShopInfoUseCase
import com.tokopedia.loginregister.shopcreation.domain.UpdateUserProfileUseCase
import com.tokopedia.loginregister.shopcreation.domain.ValidateUserProfileUseCase
import com.tokopedia.sessioncommon.domain.usecase.GetUserInfoAndSaveSessionUseCase
import com.tokopedia.sessioncommon.domain.usecase.RegisterUseCase
import com.tokopedia.utils.lifecycle.SingleLiveEvent
import kotlinx.coroutines.launch
import javax.inject.Inject

class KycBridgingViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase,
    private val registerCheckUseCase: RegisterCheckUseCase,
    private val getUserProfileCompletionUseCase: GetUserProfileCompletionUseCase,
    private val validateUserProfileUseCase: ValidateUserProfileUseCase,
    private val updateUserProfileUseCase: UpdateUserProfileUseCase,
    private val getProfileUseCase: GetUserInfoAndSaveSessionUseCase,
    private val shopInfoUseCase: ShopInfoUseCase,
    private val projectInfoUseCase: ProjectInfoUseCase,
    private val getShopStatusUseCase: GetShopStatusUseCase,
    private val dispatchers: CoroutineDispatchers
) : ShopCreationViewModel(
    registerUseCase,
    registerCheckUseCase,
    getUserProfileCompletionUseCase,
    validateUserProfileUseCase,
    updateUserProfileUseCase,
    getProfileUseCase,
    shopInfoUseCase,
    dispatchers
) {
    private val _selectedShopType = MutableLiveData<Int>()
    val selectedShopType: LiveData<Int>
        get() = _selectedShopType

    private val _buttonLoader = SingleLiveEvent<Boolean>()
    val buttonLoader: SingleLiveEvent<Boolean>
        get() = _buttonLoader

    private val _projectInfo = MutableLiveData<ProjectInfoResult>()
    val projectInfo: LiveData<ProjectInfoResult>
        get() = _projectInfo

    private val _shopStatus = MutableLiveData<ShopStatus>()
    val shopStatus: LiveData<ShopStatus>
        get() = _shopStatus

    fun setSelectedShopType(int: Int) {
        _selectedShopType.value = int
    }

    fun checkKycStatus() {
        launch {
            showLoader(true)
            try {
                val resp = projectInfoUseCase(ShopCreationConstant.OPEN_SHOP_KYC_PROJECT_ID.toIntOrZero())
                _projectInfo.value = resp
            } catch (e: Exception) {
                _projectInfo.value = ProjectInfoResult.Failed(e)
            } finally {
                showLoader(false)
            }
        }
    }

    fun getShopStatus() {
        launch {
            showLoader(true)
            try {
                val resp = getShopStatusUseCase("")
                _shopStatus.value = resp
            } catch (e: Exception) {
                _shopStatus.value = ShopStatus.Error(e)
            } finally {
                showLoader(false)
            }
        }
    }

    fun showLoader(isShowing: Boolean) {
        _buttonLoader.value = isShowing
    }
}
