package com.tokopedia.seller.menu.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.gm.common.domain.interactor.GetShopCreatedInfoUseCase
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.product.manage.common.feature.list.data.model.filter.Tab
import com.tokopedia.product.manage.common.feature.list.domain.usecase.GetProductListMetaUseCase
import com.tokopedia.seller.menu.common.analytics.SettingTrackingConstant
import com.tokopedia.seller.menu.common.constant.MenuItemType
import com.tokopedia.seller.menu.common.view.uimodel.UserShopInfoWrapper
import com.tokopedia.seller.menu.common.view.uimodel.base.partialresponse.PartialSettingSuccessInfoType
import com.tokopedia.seller.menu.common.view.uimodel.shopinfo.SettingShopInfoUiModel
import com.tokopedia.seller.menu.domain.usecase.GetAllShopInfoUseCase
import com.tokopedia.seller.menu.domain.usecase.GetSellerNotificationUseCase
import com.tokopedia.seller.menu.domain.usecase.GetShopScoreLevelUseCase
import com.tokopedia.seller.menu.presentation.uimodel.ShopInfoUiModel
import com.tokopedia.seller.menu.presentation.uimodel.compose.SellerMenuActionClick
import com.tokopedia.seller.menu.presentation.uimodel.compose.SellerMenuComposeItem
import com.tokopedia.seller.menu.presentation.uimodel.compose.SellerMenuInfoLoadingUiModel
import com.tokopedia.seller.menu.presentation.uimodel.compose.SellerMenuInfoUiModel
import com.tokopedia.seller.menu.presentation.uimodel.compose.SellerMenuItemUiModel
import com.tokopedia.seller.menu.presentation.uimodel.compose.SellerMenuOrderUiModel
import com.tokopedia.seller.menu.presentation.uimodel.compose.SellerMenuProductUiModel
import com.tokopedia.seller.menu.presentation.uimodel.compose.SellerMenuUIEvent
import com.tokopedia.seller.menu.presentation.uimodel.compose.SellerMenuUIState
import com.tokopedia.seller.menu.presentation.util.SellerMenuComposeList
import com.tokopedia.seller.menu.presentation.util.SellerUiModelMapper
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import com.tokopedia.seller.menu.common.R as sellermenucommonR

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

    private val _isToasterAlreadyShown = MutableStateFlow<Boolean?>(false)
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
                else -> {
                }
            }
        }
    }

    private fun setIsToasterAlreadyShown(isAlreadyShown: Boolean) {
        _isToasterAlreadyShown.value = isAlreadyShown
    }

    fun getShopAccountInfo() {
        launchCatchError(block = {
            _uiEvent.emit(SellerMenuUIEvent.Idle)
            val data = withContext(dispatchers.io) {
                getShopCreatedInfoUseCase.requestParams = GetShopCreatedInfoUseCase.createParams(userSession.shopId.toLongOrZero())
                getShopCreatedInfoUseCase.executeOnBackground()
            }
            _shopAge.value = data.shopAge
            _uiEvent.emit(SellerMenuUIEvent.OnSuccessGetShopInfo(data.shopAge, data.isNewSeller))
        }, onError = {
                _uiState.emit(SellerMenuUIState.OnFailedGetMenuList(it, getErrorMenuList()))
                _isRefreshing.emit(false)
            })
    }

    fun getAllSettingShopInfo(isToasterRetry: Boolean = false) {
        if (isToasterRetry) {
            checkDelayErrorResponseTrigger()
        }
        getAllShopInfoData(_shopAge.value)
    }

    fun getProductCount() {
        launchCatchError(block = {
            val response = withContext(dispatchers.io) {
                getProductListMetaUseCase.setParams(userSession.shopId)
                getProductListMetaUseCase.executeOnBackground()
                    .productListMetaWrapper
                    .productListMetaData
                    .tabs
            }
            _uiState.emit(
                SellerMenuUIState.OnSuccessGetMenuList(
                    getUpdatedProductSection(
                        getProductCount(response)
                    )
                )
            )
        }, onError = {
                // No-op
            })
    }

    fun getNotifications() {
        launchCatchError(block = {
            val data = withContext(dispatchers.io) {
                getSellerMenuNotifications.executeOnBackground()
            }
            _uiState.emit(
                SellerMenuUIState.OnSuccessGetMenuList(
                    getUpdatedNotificationSection(
                        data.notifications.sellerOrderStatus.newOrder,
                        data.notifications.sellerOrderStatus.readyToShip,
                        data.notifications.sellerOrderStatus.inResolution
                    )
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
            initialMenu = getLoadingMenuList()
            isInitialValue = false
        }
        _currentMenuList.value = initialMenu
        _uiState.tryEmit(SellerMenuUIState.OnSuccessGetMenuList(initialMenu, isInitialValue))
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

            _isRefreshing.emit(false)
            _uiState.emit(SellerMenuUIState.OnSuccessGetMenuList(getSuccessMenuList(data)))
        }, onError = {
                _isRefreshing.emit(false)
                _uiState.emit(SellerMenuUIState.OnFailedGetMenuList(it, getErrorMenuList()))
            })
    }

    private fun getLoadingMenuList(): List<SellerMenuComposeItem> {
        val successMenuList = _currentMenuList.value.toMutableList()
        val loadingIndex = successMenuList.getTopIndex()
        if (loadingIndex >= RecyclerView.NO_POSITION) {
            successMenuList.run {
                removeAt(loadingIndex)
                add(
                    loadingIndex,
                    SellerMenuInfoLoadingUiModel
                )
            }
        }
        _currentMenuList.value = successMenuList
        return successMenuList
    }

    private fun getSuccessMenuList(uiModel: ShopInfoUiModel): List<SellerMenuComposeItem> {
        val successMenuList = _currentMenuList.value.toMutableList()
        val loadingIndex = successMenuList.getTopIndex()
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

    private fun getErrorMenuList(): List<SellerMenuComposeItem> {
        val errorMenuList = _currentMenuList.value.toMutableList()
        val topIndex = errorMenuList.getTopIndex()
        if (topIndex >= RecyclerView.NO_POSITION) {
            errorMenuList.run {
                removeAt(topIndex)
                add(
                    topIndex,
                    SellerMenuInfoUiModel(
                        shopAvatarUrl = userSession.shopAvatar,
                        shopScore = Long.ZERO,
                        shopName = userSession.shopName,
                        shopAge = _shopAge.value,
                        shopFollowers = Long.ZERO,
                        shopBadgeUrl = String.EMPTY,
                        userShopInfoWrapper = UserShopInfoWrapper(null, null),
                        partialResponseStatus = false to false,
                        balanceValue = String.EMPTY
                    )
                )
            }
        }
        _currentMenuList.value = errorMenuList
        return errorMenuList
    }

    private fun getUpdatedProductSection(productCount: Int): List<SellerMenuComposeItem> {
        val currentMenuList = _currentMenuList.value.toMutableList()
        val productIndex = currentMenuList.indexOfFirst { it is SellerMenuProductUiModel }
        if (productIndex >= RecyclerView.NO_POSITION) {
            currentMenuList.run {
                removeAt(productIndex)
                add(
                    productIndex,
                    SellerMenuProductUiModel(
                        productCount,
                        userSession.isShopOwner
                    )
                )
            }
        }
        _currentMenuList.value = currentMenuList
        return currentMenuList
    }

    private fun getUpdatedNotificationSection(
        newOrderCount: Int,
        readyToShipCount: Int,
        resolutionCount: Int
    ): List<SellerMenuComposeItem> {
        val currentMenuList = _currentMenuList.value.toMutableList()

        val orderIndex = currentMenuList.indexOfFirst { it is SellerMenuOrderUiModel }
        if (orderIndex >= RecyclerView.NO_POSITION) {
            currentMenuList.run {
                removeAt(orderIndex)
                add(
                    orderIndex,
                    SellerMenuOrderUiModel(
                        newOrderCount,
                        readyToShipCount,
                        userSession.isShopOwner
                    )
                )
            }
        }

        val resolutionIndex =
            currentMenuList.indexOfFirst { (it as? SellerMenuItemUiModel)?.titleRes == sellermenucommonR.string.setting_menu_complaint }
        if (resolutionIndex >= RecyclerView.NO_POSITION) {
            currentMenuList.run {
                removeAt(resolutionIndex)
                add(
                    resolutionIndex,
                    SellerMenuItemUiModel(
                        titleRes = sellermenucommonR.string.setting_menu_complaint,
                        type = MenuItemType.COMPLAIN,
                        eventActionSuffix = SettingTrackingConstant.COMPLAINT,
                        iconUnifyType = IconUnify.PRODUCT_INFO,
                        actionClick = SellerMenuActionClick.COMPLAINTS,
                        notificationCount = resolutionCount
                    )
                )
            }
        }

        _currentMenuList.value = currentMenuList
        return currentMenuList
    }

    private fun getProductCount(response: List<Tab>): Int {
        var totalProductCount = 0

        response.filter { SellerUiModelMapper.supportedProductStatus.contains(it.id) }.map {
            totalProductCount += it.value.toIntOrZero()
        }

        return totalProductCount
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

    private fun List<SellerMenuComposeItem>.getTopIndex(): Int {
        return indexOfFirst {
            it is SellerMenuInfoLoadingUiModel || it is SellerMenuInfoUiModel
        }
    }

    companion object {
        private const val ERROR_EXCEPTION_MESSAGE = "seller menu shop info and topads failed"
        private const val DELAY_TIME = 5000L
    }
}
