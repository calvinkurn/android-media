package com.tokopedia.sellerhome.settings.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.sellerhome.settings.domain.mapToGeneralShopInfo
import com.tokopedia.sellerhome.settings.domain.usecase.GetSettingShopInfoUseCase
import com.tokopedia.sellerhome.settings.domain.usecase.GetShopBadgeUseCase
import com.tokopedia.sellerhome.settings.domain.usecase.GetShopTotalFollowersUseCase
import com.tokopedia.sellerhome.settings.view.uimodel.GeneralShopInfoUiModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject
import javax.inject.Named

class OtherMenuViewModel @Inject constructor(
        @Named("Main") dispatcher: CoroutineDispatcher,
        private val userSession: UserSessionInterface,
        private val getSettingShopInfoUseCase: GetSettingShopInfoUseCase,
        private val getShopBadgeUseCase: GetShopBadgeUseCase,
        private val getShopTotalFollowersUseCase: GetShopTotalFollowersUseCase
): BaseViewModel(dispatcher) {

    private val _generalShopInfoLiveData = MutableLiveData<Result<GeneralShopInfoUiModel>>()
    private val _totalFollowersLiveData = MutableLiveData<Result<Int>>()
    private val _shopBadgeLiveData = MutableLiveData<Result<String>>()
    private val _isGeneralShopInfoAlreadyLoadedLiveData = MutableLiveData<Boolean>().apply { value = false }
    private val _isShopBadgeAlreadyLoadedLiveData = MutableLiveData<Boolean>().apply { value = false }
    private val _isTotalFollowersAlreadyLoadedLiveData = MutableLiveData<Boolean>().apply { value = false }

    val generalShopInfoLiveData: LiveData<Result<GeneralShopInfoUiModel>>
        get() = _generalShopInfoLiveData
    val totalFollowersLiveData: LiveData<Result<Int>>
        get() = _totalFollowersLiveData
    val shopBadgeLiveData: LiveData<Result<String>>
        get() = _shopBadgeLiveData
    val isGeneralShopInfoAlreadyLoaded: LiveData<Boolean>
        get() = _isGeneralShopInfoAlreadyLoadedLiveData
    val isShopBadgeAlreadyLoadedLiveData: LiveData<Boolean>
        get() = _isShopBadgeAlreadyLoadedLiveData
    val isTotalFollowersAlreadyLoadedLiveData: LiveData<Boolean>
        get() = _isTotalFollowersAlreadyLoadedLiveData

    fun getAllSettingShopInfo() {
        userSession.run {
            getSettingShopInfo()
            getShopTotalFollowers()
            getShopBadge()
        }
    }

    private fun getSettingShopInfo() {
        val userId = userSession.userId.toIntOrZero()
        launchCatchError(block = {
            getSettingShopInfoUseCase.params = GetSettingShopInfoUseCase.createRequestParams(userId)
            val shopInfo = getSettingShopInfoUseCase.executeOnBackground()
            val generalShopInfoUiModel = shopInfo.mapToGeneralShopInfo()
            _generalShopInfoLiveData.value = Success(generalShopInfoUiModel)
            _isGeneralShopInfoAlreadyLoadedLiveData.value = true
        }, onError = {
            _generalShopInfoLiveData.value = Fail(it)
        })
    }

    private fun getShopTotalFollowers() {
        val shopId = userSession.shopId.toIntOrZero()
        launchCatchError(block = {
            getShopTotalFollowersUseCase.params = GetShopTotalFollowersUseCase.createRequestParams(shopId)
            val totalFollowers = getShopTotalFollowersUseCase.executeOnBackground()
            _totalFollowersLiveData.value = Success(totalFollowers)
            _isTotalFollowersAlreadyLoadedLiveData.value = true
        }, onError = {
            _totalFollowersLiveData.value = Fail(it)
        })
    }

    private fun getShopBadge() {
        val shopId = userSession.shopId.toIntOrZero()
        launchCatchError(block = {
            getShopBadgeUseCase.params = GetShopBadgeUseCase.createRequestParams(shopId)
            val shopBadgeUrl = getShopBadgeUseCase.executeOnBackground()
            _shopBadgeLiveData.value = Success(shopBadgeUrl)
            _isShopBadgeAlreadyLoadedLiveData.value = true
        }, onError = {
            _shopBadgeLiveData.value = Fail(it)
        })
    }

}