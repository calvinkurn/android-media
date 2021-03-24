package com.tokopedia.shop.showcase.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.asyncCatchError
import com.tokopedia.shop.common.graphql.data.shopetalase.ShopEtalaseModel
import com.tokopedia.shop.common.graphql.domain.usecase.shopetalase.GetShopEtalaseByShopUseCase
import com.tokopedia.shop.common.util.ShopUtil
import com.tokopedia.shop.showcase.domain.model.GetFeaturedShowcase
import com.tokopedia.shop.showcase.domain.model.GetFeaturedShowcaseRequestParams
import com.tokopedia.shop.showcase.domain.usecase.GetFeaturedShowcaseUseCase
import com.tokopedia.shop.showcase.presentation.model.ShowcasesBuyerUiModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by Rafli Syam on 05/03/2021
 */
class ShopPageShowcaseViewModel @Inject constructor(
        private val userSession: UserSessionInterface,
        private val dispatcherProvider: CoroutineDispatchers,
        private val getShopEtalaseByShopUseCase: GetShopEtalaseByShopUseCase,
        private val getFeaturedShowcaseUseCase: GetFeaturedShowcaseUseCase
) : BaseViewModel(dispatcherProvider.main) {

    val showcasesBuyerUiModel: LiveData<Result<ShowcasesBuyerUiModel>>
        get() = _showcasesBuyerUiModel
    private val _showcasesBuyerUiModel = MutableLiveData<Result<ShowcasesBuyerUiModel>>()

    val featuredShowcaseList: LiveData<Result<GetFeaturedShowcase>>
        get() = _featuredShowcaseList
    private val _featuredShowcaseList = MutableLiveData<Result<GetFeaturedShowcase>>()

    val showcaseList: LiveData<Result<List<ShopEtalaseModel>>>
        get() = _showcaseList
    private val _showcaseList = MutableLiveData<Result<List<ShopEtalaseModel>>>()

    fun getShowcasesInitialData(shopId: String) {
        launchCatchError(block = {

            val showcasesBuyerUiModelResponse = ShowcasesBuyerUiModel()

            val featuredShowcaseAsyncCall = asyncCatchError(
                    dispatcherProvider.io,
                    block = {
                        getFeaturedShowcaseListCall(shopId)
                    },
                    onError = {
                        showcasesBuyerUiModelResponse.isFeaturedShowcaseError = true
                        null
                    }
            )

            val allShowcaseAsyncCall = asyncCatchError(
                    dispatcherProvider.io,
                    block = {
                        getAllShowcaseListCall(shopId)
                    },
                    onError = {
                        showcasesBuyerUiModelResponse.isAllShowcaseError = true
                        null
                    }
            )

            featuredShowcaseAsyncCall.await()?.let { featuredShowcaseResponse ->
                showcasesBuyerUiModelResponse.getFeaturedShowcase = featuredShowcaseResponse
            }
            allShowcaseAsyncCall.await()?.let { allShowcaseList ->
                showcasesBuyerUiModelResponse.allShowcaseList = allShowcaseList
            }

            _showcasesBuyerUiModel.postValue(Success(showcasesBuyerUiModelResponse))

        }) {
            _showcasesBuyerUiModel.postValue(Fail(it))
        }
    }

    fun getFeaturedShowcaseList(shopId: String) {
        launchCatchError(block = {
            withContext(dispatcherProvider.io) {
                val featuredShowcaseList = getFeaturedShowcaseListCall(shopId)
                _featuredShowcaseList.postValue(Success(featuredShowcaseList))
            }
        }) {
            _featuredShowcaseList.postValue(Fail(it))
        }
    }

    fun getShowcaseList(shopId: String) {
        launchCatchError(block = {
            withContext(dispatcherProvider.io) {
                val showcaseList = getAllShowcaseListCall(shopId)
                _showcaseList.postValue(Success(showcaseList))
            }
        }) {
            _showcaseList.postValue(Fail(it))
        }
    }

    private suspend fun getFeaturedShowcaseListCall(shopId: String): GetFeaturedShowcase {
        getFeaturedShowcaseUseCase.params = GetFeaturedShowcaseUseCase.createRequestParams(
                GetFeaturedShowcaseRequestParams(shopId)
        )
        return getFeaturedShowcaseUseCase.executeOnBackground()
    }

    private fun getAllShowcaseListCall(shopId: String): List<ShopEtalaseModel> {
        getShopEtalaseByShopUseCase.clearCache()
        getShopEtalaseByShopUseCase.isFromCacheFirst = false

        val requestParams = GetShopEtalaseByShopUseCase.createRequestParams(
                shopId = shopId,
                hideNoCount = GetShopEtalaseByShopUseCase.Companion.BuyerQueryParam.HIDE_NO_COUNT_VALUE,
                hideShowCaseGroup = GetShopEtalaseByShopUseCase.Companion.BuyerQueryParam.HIDE_SHOWCASE_GROUP_VALUE,
                isOwner = ShopUtil.isMyShop(shopId, userSession.shopId)
        )
        return getShopEtalaseByShopUseCase.createObservable(requestParams).toBlocking().first()
    }

}