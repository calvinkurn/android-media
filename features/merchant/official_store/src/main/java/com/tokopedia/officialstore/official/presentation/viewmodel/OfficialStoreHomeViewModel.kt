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
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
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
            currentSlug = "${category?.prefixUrl}${category?.slug}"
            _officialStoreBannersResult.value = Success(getOfficialStoreBanners(currentSlug).await())
            _officialStoreBenefitResult.value = Success(getOfficialStoreBenefit().await())
            _officialStoreFeaturedShopResult.value = Success(getOfficialStoreFeaturedShop(category?.categoryId?: "").await())
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
            _officialStoreProductRecommendation.value = Success(getOfficialStoreProductRecommendation(categoriesWithoutClosingSquare, page).await())
        }) {

        }
    }

    private fun getOfficialStoreBanners(categoryId: String): Deferred<OfficialStoreBanners> {
        return async(Dispatchers.IO) {
            var banner = OfficialStoreBanners()
            try {
                getOfficialStoreBannersUseCase.params = GetOfficialStoreBannerUseCase.
                        createParams(categoryId)
                banner = getOfficialStoreBannersUseCase.executeOnBackground()
            } catch (t:  Throwable) {
                _officialStoreFeaturedShopResult.value = Fail(t)
            }
            banner
        }
    }

    private fun getOfficialStoreBenefit(): Deferred<OfficialStoreBenefits> {
        return async(Dispatchers.IO) {
            var benefits = OfficialStoreBenefits()
            try {
                benefits = getOfficialStoreBenefitUseCase.executeOnBackground()
            } catch (t: Throwable) {
                _officialStoreBenefitResult.value = Fail(t)
            }
            benefits
        }
    }


    private fun getOfficialStoreFeaturedShop(categoryId: String): Deferred<OfficialStoreFeaturedShop>  {
       return async(Dispatchers.IO) {
           var featuredShop = OfficialStoreFeaturedShop()
           try {
               getOfficialStoreFeaturedShopUseCase.params = GetOfficialStoreFeaturedUseCase.
                       createParams(categoryId.toIntOrNull() ?: 0)
               featuredShop = getOfficialStoreFeaturedShopUseCase.executeOnBackground()
           } catch (t: Throwable) {
               _officialStoreFeaturedShopResult.value = Fail(t)
           }

           featuredShop
        }
    }

    private fun getOfficialStoreDynamicChannel(channelType: String) {
        getOfficialStoreDynamicChannelUseCase.setupParams(channelType)
        getOfficialStoreDynamicChannelUseCase.execute(
                { dynamicChannel -> _officialStoreDynamicChannelResult.value = Success(dynamicChannel) },
                { throwable -> _officialStoreDynamicChannelResult.value = Fail(throwable) }
        )
    }

    private fun getOfficialStoreProductRecommendation(categoryId: String, page: Int): Deferred<RecommendationWidget> {
        return async(Dispatchers.IO) {
            val defaultValue = ""
            val pageName = "official-store"
            val pageNumber = page
            var productRecommendation = RecommendationWidget(emptyList(), defaultValue, defaultValue, defaultValue, defaultValue, defaultValue, defaultValue,
                    defaultValue, pageNumber, 0, 0, true, pageName)

            try {
                val dataProduct = getRecommendationUseCase.createObservable(
                        getRecommendationUseCase.getOfficialStoreRecomParams(pageNumber, pageName, categoryId))
                        .toBlocking()
                dataProduct.first()[0].let {
                    productRecommendation = it
                }
            } catch (t: Throwable) {
                _officialStoreProductRecommendation.value = Fail(t)
            }

            productRecommendation
        }
    }

    private fun addTopAdsWishlist(model: RecommendationItem): Deferred<WishlistModel> {
        val params = RequestParams.create()
        params.putString(TopAdsWishlishedUseCase.WISHSLIST_URL, model.wishlistUrl)

        return async(Dispatchers.IO) {
            var wishlistResponse = WishlistModel()

            try {
                val dataTopAdsWishlist = topAdsWishlishedUseCase.createObservable(params).toBlocking()
                dataTopAdsWishlist.first().let {
                    wishlistResponse = it
                }
            } catch (t: Throwable) {
                _topAdsWishlistResult.value = Fail(t)
            }
            wishlistResponse
        }
    }

    fun addWishlist(model: RecommendationItem, callback: ((Boolean, Throwable?) -> Unit)) {
        if (model.isTopAds) {
            launchCatchError (block = {
                _topAdsWishlistResult.value = Success(addTopAdsWishlist(model).await())
                callback.invoke(true, null)
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

    fun isLoggedIn() = userSessionInterface.isLoggedIn

    override fun onCleared() {
        super.onCleared()
        getRecommendationUseCase.unsubscribe()
        addWishListUseCase.unsubscribe()
        topAdsWishlishedUseCase.unsubscribe()
        removeWishListUseCase.unsubscribe()
    }
}
