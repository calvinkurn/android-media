package com.tokopedia.seller.menu.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.gm.common.domain.interactor.GetShopCreatedInfoUseCase
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.product.manage.common.feature.list.domain.usecase.GetProductListMetaUseCase
import com.tokopedia.seller.menu.common.view.uimodel.UserShopInfoWrapper
import com.tokopedia.seller.menu.common.view.uimodel.base.partialresponse.PartialSettingSuccessInfoType
import com.tokopedia.seller.menu.common.view.uimodel.shopinfo.SettingShopInfoUiModel
import com.tokopedia.seller.menu.domain.usecase.GetAllShopInfoUseCase
import com.tokopedia.seller.menu.domain.usecase.GetSellerNotificationUseCase
import com.tokopedia.seller.menu.domain.usecase.GetShopScoreLevelUseCase
import com.tokopedia.seller.menu.presentation.uimodel.ShopInfoUiModel
import com.tokopedia.seller.menu.presentation.uimodel.compose.SellerMenuComposeItem
import com.tokopedia.seller.menu.presentation.uimodel.compose.SellerMenuInfoLoadingUiModel
import com.tokopedia.seller.menu.presentation.uimodel.compose.SellerMenuInfoUiModel
import com.tokopedia.seller.menu.presentation.uimodel.compose.SellerMenuUIEvent
import com.tokopedia.seller.menu.presentation.uimodel.compose.SellerMenuUIState
import com.tokopedia.seller.menu.presentation.util.SellerMenuList
import com.tokopedia.shopadmin.common.util.AdminPermissionMapper
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SellerMenuViewModel @Inject constructor(
    private val getAllShopInfoUseCase: GetAllShopInfoUseCase,
    private val getShopCreatedInfoUseCase: GetShopCreatedInfoUseCase,
    private val getProductListMetaUseCase: GetProductListMetaUseCase,
    private val getSellerMenuNotifications: GetSellerNotificationUseCase,
    private val getShopScoreLevelUseCase: GetShopScoreLevelUseCase,
    private val userSession: UserSessionInterface,
    private val mapper: AdminPermissionMapper,
    private val dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.main) {

    private val _shopAge = MutableStateFlow(0L)
    private val _currentMenuList = MutableStateFlow<List<SellerMenuComposeItem>>(listOf())

    private val _uiState = MutableStateFlow<SellerMenuUIState>(SellerMenuUIState.Idle)
    val uiState get() = _uiState.asStateFlow()

    private val _uiEvent = MutableStateFlow<SellerMenuUIEvent>(SellerMenuUIEvent.Idle)
    val uiEvent get() = _uiEvent.asStateFlow()

    private val _isToasterAlreadyShown = MutableStateFlow<Boolean?>(false)
    val isToasterAlreadyShown get() = _isToasterAlreadyShown.asStateFlow()

    fun onEvent(event: SellerMenuUIEvent) {
        viewModelScope.launch {
            when (event) {
                is SellerMenuUIEvent.GetInitialMenu -> {
                    getInitialMenu()
                }
                else -> {
                }
            }
        }
    }

    fun setIsToasterAlreadyShown(isAlreadyShown: Boolean) {
        _isToasterAlreadyShown.tryEmit(isAlreadyShown)
    }

    fun getShopAccountInfo() {
        launchCatchError(block = {
            val data = withContext(dispatchers.io) {
                getShopCreatedInfoUseCase.requestParams = GetShopCreatedInfoUseCase.createParams(userSession.shopId.toLongOrZero())
                getShopCreatedInfoUseCase.executeOnBackground()
            }
            _shopAge.value = data.shopAge
            _uiEvent.tryEmit(SellerMenuUIEvent.OnSuccessGetShopInfoUse(data.shopAge, data.isNewSeller))
        }, onError = {
                _uiState.tryEmit(SellerMenuUIState.OnFailedGetMenuList(it))
            })
    }

    fun getAllSettingShopInfo(isToasterRetry: Boolean = false) {
        if (isToasterRetry) {
            checkDelayErrorResponseTrigger()
        }
        getAllShopInfoData(_shopAge.value)
    }

    private fun getInitialMenu() {
        val initialMenu = SellerMenuList.createInitialItems()
        _currentMenuList.value = initialMenu
        _uiState.tryEmit(SellerMenuUIState.OnSuccessGetMenuList(initialMenu, true))
    }

    private fun getAllShopInfoData(shopAge: Long) {
        launchCatchError(block = {
            val shopId = userSession.shopId
            val getShopInfo = withContext(dispatchers.io) {
                async {
                    val response = getAllShopInfoUseCase.executeOnBackground()

                    if (response.first is PartialSettingSuccessInfoType || response.second is PartialSettingSuccessInfoType) {
                        SettingShopInfoUiModel(response.first, response.second, userSession)
                    } else {
                        throw MessageErrorException(ERROR_EXCEPTION_MESSAGE)
                    }
                }
            }

            val getShopScore = withContext(dispatchers.io) {
                async {
                    getShopScoreLevelUseCase.execute(shopId).shopScore
                }
            }

            val shopInfoResponse = getShopInfo.await()
            val shopScoreResponse = getShopScore.await()

            val data = ShopInfoUiModel(shopInfoResponse, shopScore = shopScoreResponse, shopAge = shopAge)

            _uiState.tryEmit(SellerMenuUIState.OnSuccessGetMenuList(getSuccessMenuList(data)))
        }, onError = {
                _uiState.tryEmit(SellerMenuUIState.OnFailedGetMenuList(it))
            })
    }

    private fun getSuccessMenuList(uiModel: ShopInfoUiModel): List<SellerMenuComposeItem> {
        val successMenuList = _currentMenuList.value.toMutableList()
        val loadingIndex = successMenuList.indexOfFirst { it is SellerMenuInfoLoadingUiModel }
        if (loadingIndex >= RecyclerView.NO_POSITION) {
            successMenuList.run {
                removeAt(loadingIndex)
                add(
                    loadingIndex,
                    SellerMenuInfoUiModel(
                        shopAvatarUrl = userSession.shopAvatar,
                        shopScore = uiModel.shopScore,
                        shopName = userSession.shopName,
                        shopAge = uiModel.shopAge,
                        shopFollowers = uiModel.shopInfo.shopFollowersUiModel?.shopFollowers.orZero(),
                        shopBadgeUrl = uiModel.shopInfo.shopBadgeUiModel?.shopBadgeUrl.orEmpty(),
                        userShopInfoWrapper = uiModel.shopInfo.shopStatusUiModel?.userShopInfoWrapper
                            ?: UserShopInfoWrapper(null, null),
                        partialResponseStatus = uiModel.shopInfo.partialResponseStatus,
                        balanceValue = uiModel.shopInfo.saldoBalanceUiModel?.balanceValue.orEmpty()
                    )
                )
            }
        }
        _currentMenuList.value = successMenuList
        return successMenuList
    }

    private fun checkDelayErrorResponseTrigger() {
        launch(coroutineContext) {
            _isToasterAlreadyShown.value?.let { isToasterAlreadyShown ->
                if (!isToasterAlreadyShown) {
                    setIsToasterAlreadyShown(true)
                    delay(DELAY_TIME)
                    setIsToasterAlreadyShown(false)
                }
            }
        }
    }

    companion object {
        private const val DELAY_TIME = 5000L
        private const val ERROR_EXCEPTION_MESSAGE = "seller menu shop info and topads failed"
    }
}
