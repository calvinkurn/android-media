package com.tokopedia.shop.showcase.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.asyncCatchError
import com.tokopedia.shop.common.graphql.data.shopetalase.ShopEtalaseModel
import com.tokopedia.shop.common.graphql.domain.usecase.shopetalase.GetShopEtalaseByShopUseCase
import com.tokopedia.shop.common.util.ShopUtil
import com.tokopedia.shop.common.view.model.ShopEtalaseUiModel
import com.tokopedia.shop.showcase.domain.model.GetFeaturedShowcase
import com.tokopedia.shop.showcase.domain.model.GetFeaturedShowcaseRequestParams
import com.tokopedia.shop.showcase.domain.model.ShopFeaturedShowcase
import com.tokopedia.shop.showcase.domain.usecase.GetFeaturedShowcaseUseCase
import com.tokopedia.shop.showcase.presentation.model.FeaturedShowcaseUiModel
import com.tokopedia.shop.showcase.presentation.model.FeaturedShowcaseUiModelResponse
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

    val featuredShowcaseList: LiveData<Result<FeaturedShowcaseUiModelResponse>>
        get() = _featuredShowcaseList
    private val _featuredShowcaseList = MutableLiveData<Result<FeaturedShowcaseUiModelResponse>>()

    val showcaseList: LiveData<Result<List<ShopEtalaseUiModel>>>
        get() = _showcaseList
    private val _showcaseList = MutableLiveData<Result<List<ShopEtalaseUiModel>>>()

    val userId: String?
        get() = userSession.userId

    fun isMyShop(shopId: String) = userSession.shopId == shopId

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

                showcasesBuyerUiModelResponse.featuredShowcaseUiModelResponse = FeaturedShowcaseUiModelResponse().apply {
                    errorResponse = featuredShowcaseResponse.error
                    featuredShowcaseList = mapToFeaturedShowcaseUiModel(
                            featuredShowcaseResponse.result
                    )
                }

            }
            allShowcaseAsyncCall.await()?.let { allShowcaseList ->
                showcasesBuyerUiModelResponse.allShowcaseList = mapToShopEtalaseUiModel(allShowcaseList)
            }

            _showcasesBuyerUiModel.postValue(Success(showcasesBuyerUiModelResponse))

        }) {
            _showcasesBuyerUiModel.postValue(Fail(it))
        }
    }

    fun getFeaturedShowcaseList(shopId: String) {
        launchCatchError(block = {
            withContext(dispatcherProvider.io) {
                val featuredShowcaseResponse = getFeaturedShowcaseListCall(shopId)
                val featuredShowcaseBuyerUiModelResponse = FeaturedShowcaseUiModelResponse().apply {
                    errorResponse = featuredShowcaseResponse.error
                    featuredShowcaseList = mapToFeaturedShowcaseUiModel(
                            featuredShowcaseResponse.result
                    )
                }
                _featuredShowcaseList.postValue(Success(featuredShowcaseBuyerUiModelResponse))
            }
        }) {
            _featuredShowcaseList.postValue(Fail(it))
        }
    }

    fun getShowcaseList(shopId: String) {
        launchCatchError(block = {
            withContext(dispatcherProvider.io) {
                val showcaseList = getAllShowcaseListCall(shopId)
                val showcaseListUiModel = mapToShopEtalaseUiModel(showcaseList)
                _showcaseList.postValue(Success(showcaseListUiModel))
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

    private fun mapToFeaturedShowcaseUiModel(
            featuredShowcaseList: List<ShopFeaturedShowcase>
    ): List<FeaturedShowcaseUiModel> {

        return featuredShowcaseList.map {
            FeaturedShowcaseUiModel().apply {
                id = it.id
                name = it.name
                count = it.count
                imageUrl = it.imageUrl
            }
        }

    }

    private fun mapToShopEtalaseUiModel(
            allShowcaseList: List<ShopEtalaseModel>
    ): List<ShopEtalaseUiModel> {

        return allShowcaseList.map {
            ShopEtalaseUiModel().apply {
                id = it.id
                name = it.name
                count = it.count
                imageUrl = it.imageUrl
                type = it.type
            }
        }
    }

}