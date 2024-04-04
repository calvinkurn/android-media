package com.tokopedia.loginregister.shopcreation.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.loginregister.common.domain.usecase.RegisterCheckUseCase
import com.tokopedia.loginregister.shopcreation.domain.GetUserProfileCompletionUseCase
import com.tokopedia.loginregister.shopcreation.domain.ShopInfoUseCase
import com.tokopedia.loginregister.shopcreation.domain.UpdateUserProfileUseCase
import com.tokopedia.loginregister.shopcreation.domain.ValidateUserProfileUseCase
import com.tokopedia.sessioncommon.domain.usecase.GetUserInfoAndSaveSessionUseCase
import com.tokopedia.sessioncommon.domain.usecase.RegisterUseCase
import javax.inject.Inject

class KycBridgingViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase,
    private val registerCheckUseCase: RegisterCheckUseCase,
    private val getUserProfileCompletionUseCase: GetUserProfileCompletionUseCase,
    private val validateUserProfileUseCase: ValidateUserProfileUseCase,
    private val updateUserProfileUseCase: UpdateUserProfileUseCase,
    private val getProfileUseCase: GetUserInfoAndSaveSessionUseCase,
    private val shopInfoUseCase: ShopInfoUseCase,
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

    fun setSelectedShopType(int: Int) {
        _selectedShopType.value = int
    }

    fun checkKycStatus() {

    }
}
