package com.tokopedia.seller.menu.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.gm.common.domain.interactor.GetShopCreatedInfoUseCase
import com.tokopedia.gm.common.presentation.model.ShopInfoPeriodUiModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.product.manage.common.feature.list.domain.usecase.GetProductListMetaUseCase
import com.tokopedia.seller.menu.common.view.uimodel.base.partialresponse.PartialSettingSuccessInfoType
import com.tokopedia.seller.menu.common.view.uimodel.shopinfo.SettingShopInfoUiModel
import com.tokopedia.seller.menu.domain.usecase.GetAllShopInfoUseCase
import com.tokopedia.seller.menu.domain.usecase.GetSellerNotificationUseCase
import com.tokopedia.seller.menu.domain.usecase.GetShopScoreLevelUseCase
import com.tokopedia.seller.menu.presentation.uimodel.NotificationUiModel
import com.tokopedia.seller.menu.presentation.uimodel.ShopInfoUiModel
import com.tokopedia.seller.menu.presentation.uimodel.ShopProductUiModel
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

class SellerMenuViewModelOld @Inject constructor(
    private val getAllShopInfoUseCase: GetAllShopInfoUseCase,
    private val getShopCreatedInfoUseCase: GetShopCreatedInfoUseCase,
    private val getProductListMetaUseCase: GetProductListMetaUseCase,
    private val getSellerMenuNotifications: GetSellerNotificationUseCase,
    private val getShopScoreLevelUseCase: GetShopScoreLevelUseCase,
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

    val shopAccountInfo: LiveData<Result<ShopInfoPeriodUiModel>>
        get() = _shopAccountInfo

    private val _settingShopInfoLiveData = MutableLiveData<Result<ShopInfoUiModel>>()
    private val _shopProductLiveData = MutableLiveData<Result<ShopProductUiModel>>()
    private val _sellerMenuNotification = MutableLiveData<Result<NotificationUiModel>>()
    private val _isToasterAlreadyShown = MutableLiveData<Boolean>()
    private val _shopAccountInfo = MutableLiveData<Result<ShopInfoPeriodUiModel>>()

    fun setIsToasterAlreadyShown(isAlreadyShown: Boolean) {
        _isToasterAlreadyShown.value = isAlreadyShown
    }

    fun getShopAccountInfo() {
        launchCatchError(block = {
            val data = withContext(dispatchers.io) {
                getShopCreatedInfoUseCase.requestParams = GetShopCreatedInfoUseCase.createParams(userSession.shopId.toLongOrZero())
                getShopCreatedInfoUseCase.executeOnBackground()
            }
            _shopAccountInfo.postValue(Success(data))
        }, onError = {
                _shopAccountInfo.postValue(Fail(it))
            })
    }

    fun getAllSettingShopInfo(isToasterRetry: Boolean = false, shopAge: Long) {
        if (isToasterRetry) {
            checkDelayErrorResponseTrigger()
        }
        getAllShopInfoData(shopAge)
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

            _settingShopInfoLiveData.value = Success(data)
        }, onError = {
                _settingShopInfoLiveData.value = Fail(it)
            })
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
}
