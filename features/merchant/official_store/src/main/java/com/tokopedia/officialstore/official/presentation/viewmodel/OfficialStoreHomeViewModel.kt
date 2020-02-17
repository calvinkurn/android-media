package com.tokopedia.officialstore.official.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.officialstore.category.data.model.Category
import com.tokopedia.officialstore.official.data.model.OfficialStoreBanners
import com.tokopedia.officialstore.official.data.model.OfficialStoreBenefits
import com.tokopedia.officialstore.official.data.model.OfficialStoreFeaturedShop
import com.tokopedia.officialstore.official.data.model.dynamic_channel.DynamicChannel
import com.tokopedia.officialstore.official.domain.GetOfficialStoreBannerUseCase
import com.tokopedia.officialstore.official.domain.GetOfficialStoreBenefitUseCase
import com.tokopedia.officialstore.official.domain.GetOfficialStoreDynamicChannelUseCase
import com.tokopedia.officialstore.official.domain.GetOfficialStoreFeaturedUseCase
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.topads.sdk.domain.interactor.TopAdsWishlishedUseCase
import com.tokopedia.topads.sdk.domain.model.WishlistModel
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlist.common.listener.WishListActionListener
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class OfficialStoreHomeViewModel @Inject constructor(
        private val getOfficialStoreBannersUseCase: GetOfficialStoreBannerUseCase,
        private val getOfficialStoreBenefitUseCase: GetOfficialStoreBenefitUseCase,
        private val getOfficialStoreFeaturedShopUseCase: GetOfficialStoreFeaturedUseCase,
        private val getOfficialStoreDynamicChannelUseCase: GetOfficialStoreDynamicChannelUseCase,
        private val getRecommendationUseCase: GetRecommendationUseCase,
        private val userSessionInterface: UserSessionInterface,
        private val addWishListUseCase: AddWishListUseCase,
        private val topAdsWishlishedUseCase: TopAdsWishlishedUseCase,
        private val removeWishListUseCase: RemoveWishListUseCase,
        dispatchers: CoroutineDispatcher
) : BaseViewModel(dispatchers) {

    var currentSlug: String = ""
        private set

    val userId: String
        get() = userSessionInterface.userId

    val officialStoreBannersResult: LiveData<Result<OfficialStoreBanners>>
        get() = _officialStoreBannersResult


    val officialStoreBenefitsResult: LiveData<Result<OfficialStoreBenefits>>
        get() = _officialStoreBenefitResult


    val officialStoreFeaturedShopResult: LiveData<Result<OfficialStoreFeaturedShop>>
        get() = _officialStoreFeaturedShopResult

    val officialStoreDynamicChannelResult: LiveData<Result<DynamicChannel>>
        get() = _officialStoreDynamicChannelResult

    val officialStoreProductRecommendationResult: LiveData<Result<RecommendationWidget>>
        get() = _officialStoreProductRecommendation

    val topAdsWishlistResult: LiveData<Result<WishlistModel>>
        get() = _topAdsWishlistResult

    val wishlistResult: LiveData<Result<WishlistModel>> by lazy {
        _wishlistResult
    }

    private val _officialStoreBannersResult by lazy {
        MutableLiveData<Result<OfficialStoreBanners>>()
    }

    private val _officialStoreBenefitResult by lazy {
        MutableLiveData<Result<OfficialStoreBenefits>>()
    }

    private val _officialStoreFeaturedShopResult by lazy {
        MutableLiveData<Result<OfficialStoreFeaturedShop>>()
    }

    private val _officialStoreDynamicChannelResult = MutableLiveData<Result<DynamicChannel>>()

    private val _officialStoreProductRecommendation by lazy {
        MutableLiveData<Result<RecommendationWidget>>()
    }

    private val _topAdsWishlistResult by lazy {
        MutableLiveData<Result<WishlistModel>>()
    }

    private val _wishlistResult by lazy {
        MutableLiveData<Result<WishlistModel>>()
    }

    fun loadFirstData(category: Category?) {
        launchCatchError(block = {
            val categoryId = category?.categoryId?.toIntOrNull() ?: 0
            currentSlug = "${category?.prefixUrl}${category?.slug}"

            _officialStoreBannersResult.value = getOfficialStoreBanners(currentSlug)
            _officialStoreBenefitResult.value = getOfficialStoreBenefit()
            _officialStoreFeaturedShopResult.value = getOfficialStoreFeaturedShop(categoryId)

            getOfficialStoreDynamicChannel(currentSlug)
        }) {
            _officialStoreBannersResult.value = Fail(it)
            _officialStoreBenefitResult.value = Fail(it)
            _officialStoreFeaturedShopResult.value = Fail(it)
        }
    }

    fun loadMore(category: Category?, page: Int) {
        val categories = category?.categories.toString()
        val categoriesWithoutOpeningSquare = categories.replace("[", "") // Remove Square bracket from the string
        val categoriesWithoutClosingSquare = categoriesWithoutOpeningSquare.replace("]", "") // Remove Square bracket from the string
        launchCatchError(block = {
            val recommendation = getOfficialStoreProductRecommendation(categoriesWithoutClosingSquare, page)
            _officialStoreProductRecommendation.value = recommendation
        }) {}
    }

    private suspend fun getOfficialStoreBanners(categoryId: String): Result<OfficialStoreBanners> {
        return withContext(Dispatchers.IO) {
            try {
                getOfficialStoreBannersUseCase.params = GetOfficialStoreBannerUseCase.createParams(categoryId)
                val banner = getOfficialStoreBannersUseCase.executeOnBackground()
                Success(banner)
            } catch (t:  Throwable) {
                Fail(t)
            }
        }
    }

    private suspend fun getOfficialStoreBenefit(): Result<OfficialStoreBenefits> {
        return withContext(Dispatchers.IO) {
            try {
                val benefits = getOfficialStoreBenefitUseCase.executeOnBackground()
                Success(benefits)
            } catch (t: Throwable) {
                Fail(t)
            }
        }
    }

    private suspend fun getOfficialStoreFeaturedShop(categoryId: Int): Result<OfficialStoreFeaturedShop> {
        return withContext(Dispatchers.IO) {
           try {
               getOfficialStoreFeaturedShopUseCase.params = GetOfficialStoreFeaturedUseCase.createParams(categoryId)
               val featuredShop = getOfficialStoreFeaturedShopUseCase.executeOnBackground()
               Success(featuredShop)
           } catch (t: Throwable) {
               Fail(t)
           }
        }
    }

    private fun getOfficialStoreDynamicChannel(channelType: String) {
        getOfficialStoreDynamicChannelUseCase.setupParams(channelType)
        getOfficialStoreDynamicChannelUseCase.execute(
                { dynamicChannel -> _officialStoreDynamicChannelResult.value = Success(dynamicChannel) },
                { throwable -> _officialStoreDynamicChannelResult.value = Fail(throwable) }
        )
    }

    private suspend fun getOfficialStoreProductRecommendation(
        categoryId: String,
        pageNumber: Int
    ): Result<RecommendationWidget> {
        return withContext(Dispatchers.IO) {
            try {
                val pageName = "official-store"
                val requestParams = getRecommendationUseCase.getOfficialStoreRecomParams(pageNumber, pageName, categoryId)
                val dataProduct = getRecommendationUseCase.createObservable(requestParams).toBlocking()
                val recommendationWidget = dataProduct.first()[0]

                Success(recommendationWidget)
            } catch (t: Throwable) {
                Fail(t)
            }
        }
    }

    private suspend fun addTopAdsWishlist(model: RecommendationItem): Result<WishlistModel> {
        return withContext(Dispatchers.IO) {
            try {
                val params = RequestParams.create().apply {
                    putString(TopAdsWishlishedUseCase.WISHSLIST_URL, model.wishlistUrl)
                }
                val dataTopAdsWishlist = topAdsWishlishedUseCase.createObservable(params).toBlocking()
                val topAdsWishList = dataTopAdsWishlist.first()

                Success(topAdsWishList)
            } catch (t: Throwable) {
                Fail(t)
            }
        }
    }

    fun addWishlist(model: RecommendationItem, callback: ((Boolean, Throwable?) -> Unit)) {
        if (model.isTopAds) {
            launchCatchError (block = {
                _topAdsWishlistResult.value = addTopAdsWishlist(model)
                _topAdsWishlistResult.value?.handleResult(callback)
            }) {
                callback.invoke(false, it)
            }
        } else {
            addWishListUseCase.createObservable(model.productId.toString(), userSessionInterface.userId, object: WishListActionListener {
                override fun onErrorAddWishList(errorMessage: String?, productId: String?) {
                    callback.invoke(false, Throwable(errorMessage))
                }

                override fun onSuccessAddWishlist(productId: String?) {
                    callback.invoke(true, null)
                }

                override fun onErrorRemoveWishlist(errorMessage: String?, productId: String?) { }

                override fun onSuccessRemoveWishlist(productId: String?) { }

            })
        }
    }

    fun removeWishlist(model: RecommendationItem, callback: ((Boolean, Throwable?) -> Unit)) {
        removeWishListUseCase.createObservable(model.productId.toString(), userSessionInterface.userId, object: WishListActionListener {
            override fun onErrorAddWishList(errorMessage: String?, productId: String?) { }

            override fun onSuccessAddWishlist(productId: String?) { }

            override fun onErrorRemoveWishlist(errorMessage: String?, productId: String?) {
                callback.invoke(false, Throwable(errorMessage))
            }

            override fun onSuccessRemoveWishlist(productId: String?) {
                callback.invoke(true, null)
            }
        })
    }

    private fun Result<Any>.handleResult(callback: (Boolean, Throwable?) -> Unit) {
        when(this) {
            is Success -> callback.invoke(true, null)
            is Fail -> callback.invoke(false, throwable)
        }
    }

    fun isLoggedIn() = userSessionInterface.isLoggedIn

    override fun onCleared() {
        super.onCleared()
        getRecommendationUseCase.unsubscribe()
        addWishListUseCase.unsubscribe()
        topAdsWishlishedUseCase.unsubscribe()
        removeWishListUseCase.unsubscribe()
    }
}
