package com.tokopedia.seller.menu.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.product.manage.common.list.domain.usecase.GetProductListMetaUseCase
import com.tokopedia.seller.menu.common.domain.usecase.GetAllShopInfoUseCase
import com.tokopedia.seller.menu.common.view.uimodel.ShopProductUiModel
import com.tokopedia.seller.menu.common.view.uimodel.base.partialresponse.PartialSettingSuccessInfoType
import com.tokopedia.seller.menu.common.view.uimodel.shopinfo.SettingShopInfoUiModel
import com.tokopedia.seller.menu.coroutine.CoroutineDispatchers
import com.tokopedia.seller.menu.presentation.util.SellerUiModelMapper.mapToProductUiModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SellerMenuViewModel @Inject constructor(
    private val getAllShopInfoUseCase: GetAllShopInfoUseCase,
    private val getProductListMetaUseCase: GetProductListMetaUseCase,
    private val userSession: UserSessionInterface,
    private val dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.main) {

    companion object {
        private const val DELAY_TIME = 5000L
        private const val ERROR_EXCEPTION_MESSAGE = "seller menu shop info and topads failed"
    }

    val settingShopInfoLiveData: LiveData<Result<SettingShopInfoUiModel>>
        get() = _settingShopInfoLiveData

    val shopProductLiveData: LiveData<Result<ShopProductUiModel>>
        get() = _shopProductLiveData

    val isToasterAlreadyShown: LiveData<Boolean>
        get() = _isToasterAlreadyShown

    private val _settingShopInfoLiveData = MutableLiveData<Result<SettingShopInfoUiModel>>()
    private val _shopProductLiveData = MutableLiveData<Result<ShopProductUiModel>>()
    private val _isToasterAlreadyShown = MutableLiveData(false)

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

            _shopProductLiveData.value = Success(mapToProductUiModel(response))
        }, onError = {
            _shopProductLiveData.value = Fail(it)
        })
    }

    private fun getAllShopInfoData() {
        launchCatchError(block = {
            _settingShopInfoLiveData.value = Success(
                withContext(dispatchers.io) {
                    with(getAllShopInfoUseCase.executeOnBackground()) {
                        if (first is PartialSettingSuccessInfoType || second is PartialSettingSuccessInfoType) {
                            SettingShopInfoUiModel(first, second, userSession)
                        } else {
                            throw MessageErrorException(ERROR_EXCEPTION_MESSAGE)
                        }
                    }
                }
            )
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