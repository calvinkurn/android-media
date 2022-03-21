package com.tokopedia.shopadmin.feature.invitationconfirmation.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.shopadmin.feature.invitationconfirmation.domain.usecase.GetAdminInfoUseCaseCase
import com.tokopedia.shopadmin.feature.invitationconfirmation.domain.usecase.GetShopAdminInfoUseCase
import com.tokopedia.shopadmin.feature.invitationconfirmation.presentation.model.AdminInfoUiModel
import com.tokopedia.shopadmin.feature.invitationconfirmation.presentation.model.ShopAdminInfoUiModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import dagger.Lazy
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AdminInvitationConfirmationViewModel @Inject constructor(
    private val coroutineDispatchers: CoroutineDispatchers,
    private val getAdminInfoUseCaseCase: Lazy<GetAdminInfoUseCaseCase>,
    private val getShopAdminInfoUseCase: Lazy<GetShopAdminInfoUseCase>
) : BaseViewModel(coroutineDispatchers.main) {

    private val _shopAdminInfo = MutableLiveData<Result<ShopAdminInfoUiModel>>()
    val shopAdminInfo: LiveData<Result<ShopAdminInfoUiModel>>
        get() = _shopAdminInfo

    private val _adminInfo = MutableLiveData<Result<AdminInfoUiModel>>()
    val adminInfo: LiveData<Result<AdminInfoUiModel>>
        get() = _adminInfo

    fun getAdminInfo(shopID: Long) {
        launchCatchError(block = {
            val adminInfoData = withContext(coroutineDispatchers.io) {
                getAdminInfoUseCaseCase.get().execute(shopID)
            }
            _adminInfo.value = Success(adminInfoData)
        }, onError = {
            _adminInfo.value = Fail(it)
        })
    }

    fun getShopAdminInfo() {
        launchCatchError(block = {
            val shopAdminInfoData = withContext(coroutineDispatchers.io) {
                getShopAdminInfoUseCase.get().execute()
            }
            _shopAdminInfo.value = Success(shopAdminInfoData)
        }, onError = {
            _shopAdminInfo.value = Fail(it)
        })
    }

    fun adminConfirmationReg() {
        launchCatchError(block = {

        }, onError = {

        })
    }
}