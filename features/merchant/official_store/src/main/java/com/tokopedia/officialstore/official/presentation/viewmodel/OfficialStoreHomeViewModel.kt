package com.tokopedia.officialstore.official.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.officialstore.category.data.model.Category
import com.tokopedia.officialstore.common.handleResult
import com.tokopedia.officialstore.official.data.model.OfficialStoreBanners
import com.tokopedia.officialstore.official.data.model.OfficialStoreBenefits
import com.tokopedia.officialstore.official.data.model.OfficialStoreChannel
import com.tokopedia.officialstore.official.data.model.OfficialStoreFeaturedShop
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
import kotlinx.coroutines.launch
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
        private val dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.main) {

    var currentSlug: String = ""
        private set
    var currentSlugDC: String = ""
        private set

    val officialStoreBannersResult: LiveData<Result<OfficialStoreBanners>>
        get() = _officialStoreBannersResult


    val officialStoreBenefitsResult: LiveData<Result<OfficialStoreBenefits>>
        get() = _officialStoreBenefitResult


    val officialStoreFeaturedShopResult: LiveData<Result<OfficialStoreFeaturedShop>>
        get() = _officialStoreFeaturedShopResult

    val officialStoreDynamicChannelResult: LiveData<Result<List<OfficialStoreChannel>>>
        get() = _officialStoreDynamicChannelResult

    val topAdsWishlistResult: LiveData<Result<WishlistModel>>
        get() = _topAdsWishlistResult

    private val _officialStoreBannersResult by lazy {
        MutableLiveData<Result<OfficialStoreBanners>>()
    }

    private val _officialStoreBenefitResult by lazy {
        MutableLiveData<Result<OfficialStoreBenefits>>()
    }

    private val _officialStoreFeaturedShopResult by lazy {
        MutableLiveData<Result<OfficialStoreFeaturedShop>>()
    }

    private val _officialStoreDynamicChannelResult = MutableLiveData<Result<List<OfficialStoreChannel>>>()

    private val _productRecommendation = MutableLiveData<Result<RecommendationWidget>>()
    val productRecommendation: LiveData<Result<RecommendationWidget>>
        get() = _productRecommendation

    private val _topAdsWishlistResult by lazy {
        MutableLiveData<Result<WishlistModel>>()
    }

    fun loadFirstData(category: Category?, location: String = "") {
        launchCatchError(block = {
            val categoryId = category?.categoryId?.toIntOrNull() ?: 0
            currentSlug = "${category?.prefixUrl}${category?.slug}"
            currentSlugDC = category?.slug ?: ""

            _officialStoreBannersResult.value = getOfficialStoreBanners(currentSlug, true)
            _officialStoreBannersResult.value = getOfficialStoreBanners(currentSlug, false)
            _officialStoreBenefitResult.value = getOfficialStoreBenefit()
            _officialStoreFeaturedShopResult.value = getOfficialStoreFeaturedShop(categoryId)

            getOfficialStoreDynamicChannel(currentSlug, location)
        }) {
            _officialStoreBannersResult.value = Fail(it)
            _officialStoreBenefitResult.value = Fail(it)
            _officialStoreFeaturedShopResult.value = Fail(it)
        }
    }

    fun loadMoreProducts(categoryId: String, pageNumber: Int, pageName: String = "official-store") {
        launch {
            try {
                withContext(dispatchers.io) {
                    val recomData = getRecommendationUseCase.createObservable(getRecommendationUseCase
                            .getOfficialStoreRecomParams(pageNumber, pageName, categoryId)).toBlocking()
                    _productRecommendation.postValue(Success(recomData.first().get(0)))
                }
            } catch (e: Throwable) {
                _productRecommendation.value = Fail(e)
            }
        }
    }

    private suspend fun getOfficialStoreBanners(categoryId: String, isCache:Boolean): Result<OfficialStoreBanners> {
        return withContext(dispatchers.io) {
            try {
                getOfficialStoreBannersUseCase.params = GetOfficialStoreBannerUseCase.createParams(categoryId)
                val banner = getOfficialStoreBannersUseCase.executeOnBackground(isCache)
                Success(banner)
            } catch (t: Throwable) {
                Fail(t)
            }
        }
    }

    private suspend fun getOfficialStoreBenefit(): Result<OfficialStoreBenefits> {
        return withContext(dispatchers.io) {
            try {
                val benefits = getOfficialStoreBenefitUseCase.executeOnBackground()
                Success(benefits)
            } catch (t: Throwable) {
                Fail(t)
            }
        }
    }

    private suspend fun getOfficialStoreFeaturedShop(categoryId: Int): Result<OfficialStoreFeaturedShop> {
        return withContext(dispatchers.io) {
            try {
                getOfficialStoreFeaturedShopUseCase.params = GetOfficialStoreFeaturedUseCase.createParams(categoryId)
                val featuredShop = getOfficialStoreFeaturedShopUseCase.executeOnBackground()
                Success(featuredShop)
            } catch (t: Throwable) {
                Fail(t)
            }
        }
    }

    private fun getOfficialStoreDynamicChannel(channelType: String, location: String) {
        launchCatchError(coroutineContext, block = {
            getOfficialStoreDynamicChannelUseCase.setupParams(channelType, location)
            _officialStoreDynamicChannelResult.postValue(Success(getOfficialStoreDynamicChannelUseCase.executeOnBackground()))
        }){
            _officialStoreDynamicChannelResult.postValue(Fail(it))
        }
    }

    private suspend fun addTopAdsWishlist(model: RecommendationItem): Result<WishlistModel> {
        return withContext(dispatchers.io) {
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
            launchCatchError(block = {
                _topAdsWishlistResult.value = addTopAdsWishlist(model)
                _topAdsWishlistResult.value?.handleResult(callback)
            }) {
                callback.invoke(false, it)
            }
        } else {
            addWishListUseCase.createObservable(model.productId.toString(), userSessionInterface.userId, object : WishListActionListener {
                override fun onErrorAddWishList(errorMessage: String?, productId: String?) {
                    callback.invoke(false, Throwable(errorMessage))
                }

                override fun onSuccessAddWishlist(productId: String?) {
                    callback.invoke(true, null)
                }

                override fun onErrorRemoveWishlist(errorMessage: String?, productId: String?) {}

                override fun onSuccessRemoveWishlist(productId: String?) {}

            })
        }
    }

    fun removeWishlist(model: RecommendationItem, callback: ((Boolean, Throwable?) -> Unit)) {
        removeWishListUseCase.createObservable(model.productId.toString(), userSessionInterface.userId, object : WishListActionListener {
            override fun onErrorAddWishList(errorMessage: String?, productId: String?) {}

            override fun onSuccessAddWishlist(productId: String?) {}

            override fun onErrorRemoveWishlist(errorMessage: String?, productId: String?) {
                callback.invoke(false, Throwable(errorMessage))
            }

            override fun onSuccessRemoveWishlist(productId: String?) {
                callback.invoke(true, null)
            }
        })
    }

    fun isLoggedIn() = userSessionInterface.isLoggedIn

    fun getUserId() = userSessionInterface.userId

    override fun onCleared() {
        super.onCleared()
        getRecommendationUseCase.unsubscribe()
        addWishListUseCase.unsubscribe()
        topAdsWishlishedUseCase.unsubscribe()
        removeWishListUseCase.unsubscribe()
    }
}
