package com.tokopedia.seller.menu.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.gm.common.domain.interactor.GetShopScoreUseCase
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.product.manage.common.feature.list.domain.usecase.GetProductListMetaUseCase
import com.tokopedia.seller.menu.common.domain.usecase.GetAllShopInfoUseCase
import com.tokopedia.seller.menu.common.view.uimodel.ShopProductUiModel
import com.tokopedia.seller.menu.common.view.uimodel.base.partialresponse.PartialSettingSuccessInfoType
import com.tokopedia.seller.menu.common.view.uimodel.shopinfo.SettingShopInfoUiModel
import com.tokopedia.seller.menu.common.view.uimodel.shopinfo.ShopInfoUiModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.gm.common.domain.interactor.GetShopInfoPeriodUseCase
import com.tokopedia.gm.common.presentation.model.ShopInfoPeriodUiModel
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.seller.menu.common.view.uimodel.TickerShopScoreUiModel
import com.tokopedia.seller.menu.domain.usecase.GetSellerNotificationUseCase
import com.tokopedia.seller.menu.presentation.uimodel.NotificationUiModel
import com.tokopedia.seller.menu.presentation.util.SellerUiModelMapper
import com.tokopedia.seller.menu.presentation.util.SellerUiModelMapper.mapToNotificationUiModel
import com.tokopedia.seller.menu.presentation.util.SellerUiModelMapper.mapToProductUiModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SellerMenuViewModel @Inject constructor(
        private val getAllShopInfoUseCase: GetAllShopInfoUseCase,
        private val getShopInfoPeriodUseCase: GetShopInfoPeriodUseCase,
        private val getProductListMetaUseCase: GetProductListMetaUseCase,
        private val getSellerMenuNotifications: GetSellerNotificationUseCase,
        private val getShopScoreUseCase: GetShopScoreUseCase,
        private val userSession: UserSessionInterface,
        private val dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.main) {

    companion object {
        private const val DELAY_TIME = 5000L
        private const val ERROR_EXCEPTION_MESSAGE = "seller menu shop info and topads failed"
    }

    val settingShopInfoLiveData: LiveData<Result<ShopInfoUiModel>>
        get() = _settingShopInfoLiveData

    val shopProductLiveData: LiveData<Result<ShopProductUiModel>>
        get() = _shopProductLiveData

    val sellerMenuNotification: LiveData<Result<NotificationUiModel>>
        get() = _sellerMenuNotification

    val isToasterAlreadyShown: LiveData<Boolean>
        get() = _isToasterAlreadyShown

    val shopAccountTickerPeriod: LiveData<Result<ShopInfoPeriodUiModel>>
        get() = _shopAccountTickerPeriod

    private val _settingShopInfoLiveData = MutableLiveData<Result<ShopInfoUiModel>>()
    private val _shopProductLiveData = MutableLiveData<Result<ShopProductUiModel>>()
    private val _sellerMenuNotification = MutableLiveData<Result<NotificationUiModel>>()
    private val _isToasterAlreadyShown = MutableLiveData(false)
    private val _shopAccountTickerPeriod = MutableLiveData<Result<ShopInfoPeriodUiModel>>()

    fun getShopAccountTickerPeriod() {
        launchCatchError(block = {
            val data = withContext(dispatchers.io) {
                getShopInfoPeriodUseCase.requestParams = GetShopInfoPeriodUseCase.createParams(userSession.shopId.toIntOrZero())
                getShopInfoPeriodUseCase.executeOnBackground()
            }
            _shopAccountTickerPeriod.postValue(Success(data))
        }, onError = {
            _shopAccountTickerPeriod.postValue(Fail(it))
        })
    }

    fun getAllSettingShopInfo(isToasterRetry: Boolean = false) {
        if (isToasterRetry) {
            launch(coroutineContext) {
                checkDelayErrorResponseTrigger()
            }
        }
        getAllShopInfoData()
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

            _shopProductLiveData.value = Success(mapToProductUiModel(response, userSession.isShopOwner))
        }, onError = {
            _shopProductLiveData.value = Fail(it)
        })
    }

    fun getNotifications() {
        launchCatchError(block = {
            val data = withContext(dispatchers.io) {
                val response = getSellerMenuNotifications.executeOnBackground()
                mapToNotificationUiModel(response, userSession.isShopOwner)
            }

            _sellerMenuNotification.value = Success(data)
        }, onError = {
            _sellerMenuNotification.value = Fail(it)
        })
    }

    private fun getAllShopInfoData() {
        launchCatchError(block = {
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
                    val shopId = userSession.shopId
                    val requestParams = GetShopScoreUseCase.createRequestParams(shopId)
                    getShopScoreUseCase.getData(requestParams)
                }
            }

            val shopInfoResponse = getShopInfo.await()
            val shopScoreResponse = getShopScore.await()

            val shopScore = shopScoreResponse.data.value
            val data = ShopInfoUiModel(shopInfoResponse, shopScore)

            _settingShopInfoLiveData.value = Success(data)
        }, onError = {
            _settingShopInfoLiveData.value = Fail(it)
        })
    }

    private suspend fun checkDelayErrorResponseTrigger() {
        _isToasterAlreadyShown.value.let { isToasterAlreadyShown ->
            if (isToasterAlreadyShown == false) {
                _isToasterAlreadyShown.value = true
                delay(DELAY_TIME)
                _isToasterAlreadyShown.value = false
            }
        }
    }
}