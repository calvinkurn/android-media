package com.tokopedia.seller.menu.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.gm.common.domain.interactor.GetShopCreatedInfoUseCase
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.product.manage.common.feature.list.domain.usecase.GetProductListMetaUseCase
import com.tokopedia.seller.menu.common.view.uimodel.base.partialresponse.PartialSettingSuccessInfoType
import com.tokopedia.seller.menu.common.view.uimodel.shopinfo.SettingShopInfoUiModel
import com.tokopedia.seller.menu.domain.usecase.GetAllShopInfoUseCase
import com.tokopedia.seller.menu.domain.usecase.GetSellerNotificationUseCase
import com.tokopedia.seller.menu.domain.usecase.GetShopScoreLevelUseCase
import com.tokopedia.seller.menu.presentation.uimodel.ShopInfoUiModel
import com.tokopedia.seller.menu.presentation.uimodel.compose.SellerMenuComposeItem
import com.tokopedia.seller.menu.presentation.uimodel.compose.SellerMenuUIEvent
import com.tokopedia.seller.menu.presentation.uimodel.compose.SellerMenuUIState
import com.tokopedia.seller.menu.presentation.util.SellerMenuComposeList
import com.tokopedia.seller.menu.presentation.util.SellerMenuComposeUiMapper
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SellerMenuComposeViewModel @Inject constructor(
    private val getAllShopInfoUseCase: GetAllShopInfoUseCase,
    private val getShopCreatedInfoUseCase: GetShopCreatedInfoUseCase,
    private val getProductListMetaUseCase: GetProductListMetaUseCase,
    private val getSellerMenuNotifications: GetSellerNotificationUseCase,
    private val getShopScoreLevelUseCase: GetShopScoreLevelUseCase,
    private val userSession: UserSessionInterface,
    private val dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.main) {

    private val _shopAge = MutableStateFlow(0L)
    private val _currentMenuList = MutableStateFlow<List<SellerMenuComposeItem>>(listOf())

    private val _uiState = MutableStateFlow<SellerMenuUIState>(SellerMenuUIState.Idle)
    val uiState get() = _uiState.asStateFlow()

    private val _uiEvent = MutableStateFlow<SellerMenuUIEvent>(SellerMenuUIEvent.Idle)
    val uiEvent get() = _uiEvent.asStateFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing get() = _isRefreshing.asStateFlow()

    private val _isToasterAlreadyShown = MutableStateFlow<Boolean>(false)
    val isToasterAlreadyShown get() = _isToasterAlreadyShown.asStateFlow()

    fun onEvent(event: SellerMenuUIEvent) {
        viewModelScope.launch {
            when (event) {
                is SellerMenuUIEvent.GetInitialMenu -> {
                    _currentMenuList.value = listOf()
                    getInitialMenu()
                }
                is SellerMenuUIEvent.OnRefresh -> {
                    _isRefreshing.emit(true)
                    getInitialMenu()
                    getAllSettingShopInfo()
                }
                is SellerMenuUIEvent.GetShopInfo -> {
                    getAllSettingShopInfo()
                    getProductCount()
                    getNotifications()
                }
                else -> {
                    // NO-OP
                }
            }
        }
    }

    private suspend fun setIsToasterAlreadyShown(isAlreadyShown: Boolean) {
        _isToasterAlreadyShown.emit(isAlreadyShown)
    }

    fun getShopAccountInfo() {
        viewModelScope.launchCatchError(block = {
            _uiEvent.emit(SellerMenuUIEvent.Idle)
            val data = withContext(dispatchers.io) {
                getShopCreatedInfoUseCase.requestParams = GetShopCreatedInfoUseCase.createParams(userSession.shopId.toLongOrZero())
                getShopCreatedInfoUseCase.executeOnBackground()
            }
            _shopAge.value = data.shopAge
            _uiEvent.emit(SellerMenuUIEvent.OnSuccessGetShopInfo(data.shopAge, data.isNewSeller))
        }, onError = { throwable ->
                val errorMenuList =
                    SellerMenuComposeUiMapper.getErrorMenuList(
                        _currentMenuList.value.toMutableList(),
                        userSession.shopAvatar,
                        userSession.shopName,
                        _shopAge.value
                    )
                _currentMenuList.value = errorMenuList
                _uiState.emit(
                    SellerMenuUIState.OnFailedGetMenuList(
                        throwable,
                        errorMenuList
                    )
                )
                _isRefreshing.emit(false)
            })
    }

    fun getAllSettingShopInfo(
        isToasterRetry: Boolean = false
    ) {
        if (isToasterRetry) {
            checkDelayErrorResponseTrigger()
        }
        getAllShopInfoData(_shopAge.value)
    }

    private fun getProductCount() {
        viewModelScope.launchCatchError(block = {
            val response = withContext(dispatchers.io) {
                getProductListMetaUseCase.setParams(userSession.shopId)
                getProductListMetaUseCase.executeOnBackground()
                    .productListMetaWrapper
                    .productListMetaData
                    .tabs
            }
            val updatedProductMenuList = SellerMenuComposeUiMapper.getUpdatedProductSection(
                _currentMenuList.value.toMutableList(),
                userSession.isShopOwner,
                response
            )
            _currentMenuList.value = updatedProductMenuList
            _uiState.emit(
                SellerMenuUIState.OnSuccessGetMenuList(
                    updatedProductMenuList
                )
            )
        }, onError = {
                // No-op
            })
    }

    private fun getNotifications() {
        viewModelScope.launchCatchError(block = {
            val data = withContext(dispatchers.io) {
                getSellerMenuNotifications.executeOnBackground()
            }
            val updatedNotificationMenuList = SellerMenuComposeUiMapper.getUpdatedNotificationSection(
                _currentMenuList.value.toMutableList(),
                userSession.isShopOwner,
                data.notifications.sellerOrderStatus.newOrder,
                data.notifications.sellerOrderStatus.readyToShip,
                data.notifications.sellerOrderStatus.inResolution
            )
            _currentMenuList.value = updatedNotificationMenuList
            _uiState.emit(
                SellerMenuUIState.OnSuccessGetMenuList(
                    updatedNotificationMenuList
                )
            )
        }, onError = {
                // No-op
            })
    }

    private fun getInitialMenu() {
        val initialMenu: List<SellerMenuComposeItem>
        val isInitialValue: Boolean
        if (_currentMenuList.value.isEmpty()) {
            initialMenu = SellerMenuComposeList.createInitialItems()
            isInitialValue = true
        } else {
            initialMenu = SellerMenuComposeUiMapper.getLoadingMenuList(
                _currentMenuList.value.toMutableList()
            )
            isInitialValue = false
        }
        _currentMenuList.value = initialMenu
        _uiState.tryEmit(SellerMenuUIState.OnSuccessGetMenuList(initialMenu, isInitialValue))
    }

    private fun getAllShopInfoData(shopAge: Long) {
        viewModelScope.launchCatchError(
            block = {
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

                val successMenuList = SellerMenuComposeUiMapper.getSuccessMenuList(
                    _currentMenuList.value.toMutableList(),
                    data,
                    userSession.shopAvatar,
                    userSession.shopName
                )

                _isRefreshing.tryEmit(false)
                _currentMenuList.value = successMenuList
                _uiState.emit(SellerMenuUIState.OnSuccessGetMenuList(successMenuList))
            },
            onError = {
                val errorMenuList =
                    SellerMenuComposeUiMapper.getErrorMenuList(
                        _currentMenuList.value.toMutableList(),
                        userSession.shopAvatar,
                        userSession.shopName,
                        _shopAge.value
                    )
                _isRefreshing.tryEmit(false)
                _currentMenuList.value = errorMenuList
                _uiState.emit(SellerMenuUIState.OnFailedGetMenuList(it, errorMenuList))
            }
        )
    }

    private fun checkDelayErrorResponseTrigger() {
        viewModelScope.launch {
            _isToasterAlreadyShown.value.let { isToasterAlreadyShown ->
                if (!isToasterAlreadyShown) {
                    setIsToasterAlreadyShown(true)
                    delay(DELAY_TIME)
                    setIsToasterAlreadyShown(false)
                }
            }
        }
    }

    companion object {
        private const val ERROR_EXCEPTION_MESSAGE = "seller menu shop info and topads failed"
        private const val DELAY_TIME = 5000L
    }
}
