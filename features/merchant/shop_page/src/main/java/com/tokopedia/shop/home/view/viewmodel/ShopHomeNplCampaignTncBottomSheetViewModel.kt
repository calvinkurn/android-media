package com.tokopedia.shop.home.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.asyncCatchError
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.network.exception.UserNotLoginException
import com.tokopedia.shop.common.domain.interactor.GQLGetShopFavoriteStatusUseCase
import com.tokopedia.shop.common.domain.interactor.ToggleFavouriteShopUseCase
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo
import com.tokopedia.shop.home.domain.GetShopHomeCampaignNplTncUseCase
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.shop.home.util.mapper.ShopPageHomeMapper
import com.tokopedia.shop.home.view.model.ShopHomeCampaignNplTncUiModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ShopHomeNplCampaignTncBottomSheetViewModel @Inject constructor(
    private val dispatcherProvider: CoroutineDispatchers,
    private val userSession: UserSessionInterface,
    private val getCampaignNplTncUseCase: GetShopHomeCampaignNplTncUseCase,
    private val gqlGetShopFavoriteStatusUseCase: GQLGetShopFavoriteStatusUseCase,
    private val toggleFavouriteShopUseCase: ToggleFavouriteShopUseCase
) : BaseViewModel(dispatcherProvider.main) {

    val userSessionShopId: String
        get() = userSession.shopId ?: ""

    val userId: String
        get() = userSession.userId ?: ""

    val campaignTncLiveData: LiveData<Result<ShopHomeCampaignNplTncUiModel>>
        get() = _campaignTncLiveData
    private val _campaignTncLiveData = MutableLiveData<Result<ShopHomeCampaignNplTncUiModel>>()

    val campaignFollowStatusLiveData: LiveData<Result<ShopInfo.FavoriteData>>
        get() = _campaignFollowStatusLiveData
    private val _campaignFollowStatusLiveData = MutableLiveData<Result<ShopInfo.FavoriteData>>()

    val followUnfollowShopLiveData: LiveData<Result<Boolean>>
        get() = _followUnfollowShopLiveData
    private val _followUnfollowShopLiveData = MutableLiveData<Result<Boolean>>()


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
                getShopFavoriteStatus(shopId).takeIf { !isOwner }
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

    private suspend fun getShopFavoriteStatus(shopId: String): ShopInfo.FavoriteData {
        val id = shopId.toIntOrZero()
        gqlGetShopFavoriteStatusUseCase.params = GQLGetShopFavoriteStatusUseCase.createParams(if (id == 0) listOf() else listOf(id), "")
        return gqlGetShopFavoriteStatusUseCase.executeOnBackground().favoriteData
    }

    private suspend fun getTncResponse(campaignId: String): ShopHomeCampaignNplTncUiModel {
        getCampaignNplTncUseCase.params = GetShopHomeCampaignNplTncUseCase.createParams(campaignId)
        return ShopPageHomeMapper.mapToShopHomeCampaignNplTncUiModel(
                getCampaignNplTncUseCase.executeOnBackground()
        )
    }

    fun followUnfollowShop(shopId: String) {
        launchCatchError(block = {
            val onSuccessFollowUnfollowShop = withContext(dispatcherProvider.io) {
                toggleFavorite(shopId)
            }
            _followUnfollowShopLiveData.postValue(Success(onSuccessFollowUnfollowShop  ?: false))
        }) {
            _followUnfollowShopLiveData.postValue(Fail(it))
        }
    }

    private fun toggleFavorite(shopID: String): Boolean? {
        if (!userSession.isLoggedIn) {
            throw UserNotLoginException()
        }
        return toggleFavouriteShopUseCase.createObservable(
                ToggleFavouriteShopUseCase.createRequestParam(shopID)
        ).toBlocking().first()
    }

}