package com.tokopedia.shop.home.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.asyncCatchError
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.network.exception.UserNotLoginException
import com.tokopedia.shop.home.domain.GetShopHomeCampaignNplTncUseCase
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.shop.home.util.mapper.ShopPageHomeMapper
import com.tokopedia.shop.home.view.model.ShopHomeCampaignNplTncUiModel
import com.tokopedia.shop.common.data.source.cloud.model.followshop.FollowShopResponse
import com.tokopedia.shop.common.data.source.cloud.model.followstatus.FollowStatus
import com.tokopedia.shop.common.domain.interactor.GetFollowStatusUseCase
import com.tokopedia.shop.common.domain.interactor.UpdateFollowStatusUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class ShopHomeNplCampaignTncBottomSheetViewModel @Inject constructor(
        private val dispatcherProvider: CoroutineDispatchers,
        private val userSession: UserSessionInterface,
        private val getCampaignNplTncUseCase: GetShopHomeCampaignNplTncUseCase,
        private val updateFollowStatusUseCase: UpdateFollowStatusUseCase,
        private val getFollowStatusUseCase: GetFollowStatusUseCase,
) : BaseViewModel(dispatcherProvider.main) {

    val userSessionShopId: String
        get() = userSession.shopId ?: ""

    val userId: String
        get() = userSession.userId ?: ""

    val campaignTncLiveData: LiveData<Result<ShopHomeCampaignNplTncUiModel>>
        get() = _campaignTncLiveData
    private val _campaignTncLiveData = MutableLiveData<Result<ShopHomeCampaignNplTncUiModel>>()

    val campaignFollowStatusLiveData: LiveData<Result<FollowStatus>>
        get() = _campaignFollowStatusLiveData
    private val _campaignFollowStatusLiveData = MutableLiveData<Result<FollowStatus>>()

    val followUnfollowShopLiveData: LiveData<Result<FollowShopResponse>>
        get() = _followUnfollowShopLiveData
    private val _followUnfollowShopLiveData = MutableLiveData<Result<FollowShopResponse>>()

    val isUserLoggedIn: Boolean
        get() = userSession.isLoggedIn

    fun getTncBottomSheetData(campaignId: String, shopId: String, isOwner: Boolean) {
        launchCatchError(block = {
            val tncDataAsync = asyncCatchError(dispatcherProvider.io, block = {
                getTncResponse(campaignId)
            }, onError = {
                _campaignTncLiveData.postValue(Fail(it))
                null
            })

            val shopFollowStatusAsync = asyncCatchError(dispatcherProvider.io, block = {
                getFollowStatus(shopId).takeIf { !isOwner }
            }, onError = {
                if(!isOwner)
                    _campaignTncLiveData.postValue(Fail(it))
                null
            })

            val tncData = tncDataAsync.await()
            val shopFollowStatus = shopFollowStatusAsync.await()
            tncData?.let{
                _campaignTncLiveData.postValue(Success(it))
            }
            shopFollowStatus?.let{
                _campaignFollowStatusLiveData.postValue(Success(it))
            }
        }) {
            _campaignTncLiveData.postValue(Fail(it))
        }
    }

   private suspend fun getFollowStatus(shopId: String): FollowStatus? {
        getFollowStatusUseCase.params = GetFollowStatusUseCase.createParams(shopId, GetFollowStatusUseCase.SOURCE_NPL_TNC)
        return getFollowStatusUseCase.executeOnBackground().followStatus
    }

    private suspend fun getTncResponse(campaignId: String): ShopHomeCampaignNplTncUiModel {
        getCampaignNplTncUseCase.params = GetShopHomeCampaignNplTncUseCase.createParams(campaignId)
        return ShopPageHomeMapper.mapToShopHomeCampaignNplTncUiModel(
                getCampaignNplTncUseCase.executeOnBackground()
        )
    }

    fun updateFollowStatus(shopId: String, action: String) {
        if (!userSession.isLoggedIn) {
            _followUnfollowShopLiveData.value = Fail(UserNotLoginException())
            return
        }

        launchCatchError(dispatcherProvider.io, block = {
            updateFollowStatusUseCase.params = UpdateFollowStatusUseCase.createParams(shopId, action, UpdateFollowStatusUseCase.SOURCE_NPL_TNC)
            _followUnfollowShopLiveData.postValue(Success(updateFollowStatusUseCase.executeOnBackground()))
        }, onError = {
            _followUnfollowShopLiveData.postValue(Fail(it))
        })
    }
}