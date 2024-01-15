package com.tokopedia.shopdiscount.info.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.campaign.entity.RemoteTicker
import com.tokopedia.campaign.usecase.GetTargetedTickerUseCase
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.shopdiscount.bulk.data.response.GetSlashPriceBenefitResponse
import com.tokopedia.shopdiscount.bulk.domain.usecase.GetSlashPriceBenefitUseCase
import com.tokopedia.shopdiscount.bulk.domain.usecase.GetSlashPriceSellerStatusUseCase
import com.tokopedia.shopdiscount.info.data.response.GetSlashPriceTickerResponse
import com.tokopedia.shopdiscount.info.util.ShopDiscountSellerInfoMapper
import com.tokopedia.shopdiscount.info.data.uimodel.ShopDiscountSellerInfoUiModel
import com.tokopedia.shopdiscount.info.data.uimodel.ShopDiscountTickerUiModel
import com.tokopedia.shopdiscount.info.domain.usecase.GetSlashPriceTickerUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ShopDiscountSellerInfoBottomSheetViewModel @Inject constructor(
    private val dispatcherProvider: CoroutineDispatchers,
    private val getSlashPriceBenefitUseCase: GetSlashPriceBenefitUseCase,
    private val getSlashPriceTickerUseCase: GetSlashPriceTickerUseCase,
    private val getSlashPriceSellerStatusUseCase: GetSlashPriceSellerStatusUseCase,
    private val getTargetedTickerUseCase: GetTargetedTickerUseCase
) : BaseViewModel(dispatcherProvider.main) {

    val sellerInfoLiveData: LiveData<Result<ShopDiscountSellerInfoUiModel>>
        get() = _sellerInfoBenefitLiveData
    private val _sellerInfoBenefitLiveData =
        MutableLiveData<Result<ShopDiscountSellerInfoUiModel>>()

    val slashPriceTickerLiveData: LiveData<Result<ShopDiscountTickerUiModel>>
        get() = _slashPriceTickerLiveData
    private val _slashPriceTickerLiveData =
        MutableLiveData<Result<ShopDiscountTickerUiModel>>()

    private val _targetedTickerData = MutableLiveData<Result<List<RemoteTicker>>>()
    val targetedTickerData: LiveData<Result<List<RemoteTicker>>>
        get() = _targetedTickerData

    fun getSellerInfoBenefitData() {
        launchCatchError(dispatcherProvider.io, block = {
            val response = getSlashPriceBenefitData()
            val mappedUiModel =
                ShopDiscountSellerInfoMapper.mapToShopDiscountSellerInfoBenefitUiModel(response)
            _sellerInfoBenefitLiveData.postValue(Success(mappedUiModel))
        }) {
            _sellerInfoBenefitLiveData.postValue(Fail(it))
        }
    }

    private suspend fun getSlashPriceBenefitData(): GetSlashPriceBenefitResponse {
        getSlashPriceBenefitUseCase.setParams()
        return getSlashPriceBenefitUseCase.executeOnBackground()
    }

    fun getTickerData() {
        launchCatchError(dispatcherProvider.io, block = {
            val response = getSlashPriceTickerData()
            val uiModel = ShopDiscountSellerInfoMapper.mapToShopDiscountTickerUiModel(response)
            _slashPriceTickerLiveData.postValue(Success(uiModel))
        }) {
            _slashPriceTickerLiveData.postValue(Fail(it))
        }
    }

    private suspend fun getSlashPriceTickerData(): GetSlashPriceTickerResponse {
        getSlashPriceTickerUseCase.setParams(ShopDiscountSellerInfoMapper.mapToGetSlashPriceTickerRequest())
        return getSlashPriceTickerUseCase.executeOnBackground()
    }

    fun getTargetedTickerData() {
        launchCatchError(block = {
            val targetedTickerKey = withContext(dispatcherProvider.io) {
                getSlashPriceSellerStatusUseCase.setParams()
                getSlashPriceSellerStatusUseCase.executeOnBackground().getSlashPriceSellerStatus.listKey
            }
            val tickerData = withContext(dispatcherProvider.io) {
                getTargetedTickerUseCase.execute(
                    GetTargetedTickerUseCase.Param(
                        page = "seller.selpart-flash-sale",
                        targets = listOf(
                            GetTargetedTickerUseCase.Param.Target(
                                type = "rollence_name",
                                values = listOf()
                            )
                        )
                    )
                )
            }
            _targetedTickerData.postValue(Success(tickerData))
        }, onError = {
            _targetedTickerData.value = Fail(it)
        })
    }

}
