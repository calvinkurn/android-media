package com.tokopedia.shop.home.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.shop.home.domain.GetShopHomeMerchantCampaignTncUseCase
import com.tokopedia.shop.home.util.mapper.ShopPageHomeMapper
import com.tokopedia.shop.home.view.model.ShopHomeFlashSaleTncUiModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ShopHomeFlashSaleTncBottomSheetViewModel @Inject constructor(
    private val dispatcherProvider: CoroutineDispatchers,
    private val getMerchantCampaignTncUseCase: GetShopHomeMerchantCampaignTncUseCase
) : BaseViewModel(dispatcherProvider.main) {

    private val flashSaleTncLiveData = MutableLiveData<Result<ShopHomeFlashSaleTncUiModel>>()
    val flashSaleTnc: LiveData<Result<ShopHomeFlashSaleTncUiModel>> get() = flashSaleTncLiveData

    fun getFlashSaleTermsAndConditions(campaignId: String) {
        launchCatchError(
            block = {
                Success(
                    withContext(dispatcherProvider.io) {
                        getMerchantCampaignTncUseCase.params = GetShopHomeMerchantCampaignTncUseCase.createParams(campaignId)
                        val tncModel = getMerchantCampaignTncUseCase.executeOnBackground()
                        flashSaleTncLiveData.postValue(Success(ShopPageHomeMapper.mapToShopHomeFlashSaleTncUiModel(tncModel)))
                    }
                )
            },
            onError = {
                flashSaleTncLiveData.value = Fail(it)
            }
        )
    }
}
