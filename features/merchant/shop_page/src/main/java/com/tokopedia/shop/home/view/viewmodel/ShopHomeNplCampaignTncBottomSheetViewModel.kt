package com.tokopedia.shop.home.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.shop.home.domain.GetShopHomeCampaignNplTncUseCase
import com.tokopedia.shop.home.util.CoroutineDispatcherProvider
import com.tokopedia.shop.home.util.mapper.ShopPageHomeMapper
import com.tokopedia.shop.home.view.model.ShopHomeCampaignNplTncUiModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ShopHomeNplCampaignTncBottomSheetViewModel @Inject constructor(
        private val dispatcherProvider: CoroutineDispatcherProvider,
        private val userSession: UserSessionInterface,
        private val getCampaignNplTncUseCase: GetShopHomeCampaignNplTncUseCase
) : BaseViewModel(dispatcherProvider.main()) {

    val userSessionShopId: String
        get() = userSession.shopId ?: ""

    val campaignTncLiveData: LiveData<Result<ShopHomeCampaignNplTncUiModel>>
        get() = _campaignTncLiveData
    private val _campaignTncLiveData = MutableLiveData<Result<ShopHomeCampaignNplTncUiModel>>()

    fun getTnc(campaignId: String) {
        launchCatchError(block = {
            val tncData = withContext(dispatcherProvider.io()){
                getTncResponse(campaignId)
            }
            _campaignTncLiveData.postValue(Success(tncData))
        }){
            _campaignTncLiveData.postValue(Fail(it))
        }
    }

    private suspend fun getTncResponse(campaignId: String): ShopHomeCampaignNplTncUiModel {
        getCampaignNplTncUseCase.params = GetShopHomeCampaignNplTncUseCase.createParams(campaignId)
        return ShopPageHomeMapper.mapToShopHomeCampaignNplTncUiModel(
                getCampaignNplTncUseCase.executeOnBackground()
        )
    }

}