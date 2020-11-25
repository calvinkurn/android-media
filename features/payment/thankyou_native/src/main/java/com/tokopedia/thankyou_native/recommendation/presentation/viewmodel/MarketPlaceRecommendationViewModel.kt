package com.tokopedia.thankyou_native.recommendation.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.productcard.v2.BlankSpaceConfig
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.thankyou_native.recommendation.di.qualifier.CoroutineBackgroundDispatcher
import com.tokopedia.thankyou_native.recommendation.di.qualifier.CoroutineMainDispatcher
import com.tokopedia.thankyou_native.recommendation.model.MarketPlaceRecommendationModel
import com.tokopedia.thankyou_native.recommendation.model.MarketPlaceRecommendationResult
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
import kotlinx.coroutines.withContext
import rx.Subscriber
import java.io.IOException
import javax.inject.Inject

class MarketPlaceRecommendationViewModel @Inject constructor(
        @CoroutineMainDispatcher val mainDispatcher: dagger.Lazy<CoroutineDispatcher>,
        @CoroutineBackgroundDispatcher val backgroundDispatcher: dagger.Lazy<CoroutineDispatcher>,
        private val getRecommendationUseCase: dagger.Lazy<GetRecommendationUseCase>,
        private val addWishListUseCase: dagger.Lazy<AddWishListUseCase>,
        private val removeWishListUseCase: dagger.Lazy<RemoveWishListUseCase>,
        private val topAdsWishlishedUseCase: dagger.Lazy<TopAdsWishlishedUseCase>,
        val userSession: dagger.Lazy<UserSessionInterface>) : BaseViewModel(mainDispatcher.get()) {

    private var currentPage = 0

    val recommendationMutableData = MutableLiveData<Result<MarketPlaceRecommendationResult>>()

    fun loadRecommendationData() {
        launchCatchError(block = {
            val data = loadRecommendationDataFromApi()
            data?.let {
                val recommendationItemList = data.first().recommendationItemList
                if (!recommendationItemList.isNullOrEmpty()) {
                    val marketPlaceRecommendationModelList = getProductCardModel(recommendationItemList)
                    val blankSpaceConfig = getBlankSpaceConfig(marketPlaceRecommendationModelList)
                    val marketPlaceRecommendationResult = MarketPlaceRecommendationResult(
                            data.first().title,
                            marketPlaceRecommendationModelList,
                            blankSpaceConfig)
                    recommendationMutableData.value = Success(marketPlaceRecommendationResult)
                } else {
                    recommendationMutableData.value = Fail(IOException(NO_DATA_FOUND))
                }
            } ?: run {
                recommendationMutableData.value = Fail(IOException(NO_DATA_FOUND))
            }
        }, onError = {
            recommendationMutableData.value = Fail(it)
        })
    }

    private suspend fun getProductCardModel(recommendationItemList: List<RecommendationItem>)
            : List<MarketPlaceRecommendationModel> = withContext(backgroundDispatcher.get()) {
        recommendationItemList.map { recommendationItem ->
            MarketPlaceRecommendationModel(recommendationItem,
                    ProductCardModel(
                            slashedPrice = recommendationItem.slashedPrice,
                            productName = recommendationItem.name,
                            formattedPrice = recommendationItem.price,
                            productImageUrl = recommendationItem.imageUrl,
                            isTopAds = recommendationItem.isTopAds,
                            discountPercentage = recommendationItem.discountPercentage.toString(),
                            reviewCount = recommendationItem.countReview,
                            ratingCount = recommendationItem.rating,
                            shopLocation = recommendationItem.location,
                            isWishlistVisible = true,
                            isWishlisted = recommendationItem.isWishlist,
                            shopBadgeList = recommendationItem.badgesUrl.map {
                                ProductCardModel.ShopBadge(imageUrl = it ?: "")
                            },
                            freeOngkir = ProductCardModel.FreeOngkir(
                                    isActive = recommendationItem.isFreeOngkirActive,
                                    imageUrl = recommendationItem.freeOngkirImageUrl)
                    )
            )
        }
    }

    private suspend fun getBlankSpaceConfig(
            marketPlaceRecommendationModelList: List<MarketPlaceRecommendationModel>)
            : BlankSpaceConfig = withContext(backgroundDispatcher.get()) {
        val blankSpaceConfig = BlankSpaceConfig(twoLinesProductName = true)
        marketPlaceRecommendationModelList.forEach {
            val productCardModel = it.productCardModel
            if (productCardModel.freeOngkir.isActive) blankSpaceConfig.freeOngkir = true
            if (productCardModel.shopName.isNotEmpty()) blankSpaceConfig.shopName = true
            if (productCardModel.productName.isNotEmpty()) blankSpaceConfig.productName = true
            if (productCardModel.labelPromo.title.isNotEmpty()) blankSpaceConfig.labelPromo = true
            if (productCardModel.slashedPrice.isNotEmpty()) blankSpaceConfig.slashedPrice = true
            if (productCardModel.discountPercentage.isNotEmpty()) blankSpaceConfig.discountPercentage = true
            if (productCardModel.formattedPrice.isNotEmpty()) blankSpaceConfig.price = true
            if (productCardModel.shopBadgeList.isNotEmpty()) blankSpaceConfig.shopBadge = true
            if (productCardModel.shopLocation.isNotEmpty()) blankSpaceConfig.shopLocation = true
            if (productCardModel.ratingCount != 0) blankSpaceConfig.ratingCount = true
            if (productCardModel.reviewCount != 0) blankSpaceConfig.reviewCount = true
            if (productCardModel.labelCredibility.title.isNotEmpty()) blankSpaceConfig.labelCredibility = true
            if (productCardModel.labelOffers.title.isNotEmpty()) blankSpaceConfig.labelOffers = true
            if (productCardModel.isTopAds) blankSpaceConfig.topAdsIcon = true
        }
        return@withContext blankSpaceConfig
    }

    private suspend fun loadRecommendationDataFromApi(): List<RecommendationWidget>? =
            withContext(backgroundDispatcher.get()) {
                try {
                    val data = getRecommendationUseCase.get()
                            .createObservable(getRecommendationUseCase.get().getRecomParams(
                                    pageNumber = currentPage,
                                    xSource = X_SOURCE,
                                    pageName = PAGE_NAME,
                                    productIds = arrayListOf()
                            )).toBlocking()
                    return@withContext data?.first()?.let {
                        data.first()
                    } ?: run {
                        null
                    }
                } catch (e: Throwable) {
                    null
                }
            }

    fun addToWishList(model: RecommendationItem, callback: ((Boolean, Throwable?) -> Unit)) {
        if (model.isTopAds) {
            val params = RequestParams.create()
            params.putString(TopAdsWishlishedUseCase.WISHSLIST_URL, model.wishlistUrl)
            topAdsWishlishedUseCase.get().execute(params, object : Subscriber<WishlistModel>() {
                override fun onCompleted() {
                }

                override fun onError(e: Throwable) {
                    callback.invoke(false, e)
                }

                override fun onNext(wishlistModel: WishlistModel) {
                    if (wishlistModel.data != null) {
                        callback.invoke(true, null)
                    }
                }
            })
        } else {
            addWishListUseCase.get()
                    .createObservable(model.productId.toString(), userSession.get().userId,
                            object : WishListActionListener {
                                override fun onErrorAddWishList(errorMessage: String?, productId: String?) {
                                    callback.invoke(false, Throwable(errorMessage))
                                }

                                override fun onSuccessAddWishlist(productId: String?) {
                                    callback.invoke(true, null)
                                }

                                override fun onErrorRemoveWishlist(errorMessage: String?, productId: String?) {
                                    // do nothing
                                }

                                override fun onSuccessRemoveWishlist(productId: String?) {
                                    // do nothing
                                }
                            })
        }
    }

    fun removeFromWishList(model: RecommendationItem, wishListCallback: (((Boolean, Throwable?) -> Unit))) {
        removeWishListUseCase.get().createObservable(model.productId.toString(),
                userSession.get().userId, object : WishListActionListener {
            override fun onErrorAddWishList(errorMessage: String?, productId: String?) {
                // do nothing
            }

            override fun onSuccessAddWishlist(productId: String?) {
                // do nothing
            }

            override fun onErrorRemoveWishlist(errorMessage: String?, productId: String?) {
                wishListCallback.invoke(false, Throwable(errorMessage))
            }

            override fun onSuccessRemoveWishlist(productId: String?) {
                wishListCallback.invoke(true, null)
            }
        })
    }

    companion object {
        const val X_SOURCE = "recom_widget"
        const val PAGE_NAME = "thankyou_page"
        const val NO_DATA_FOUND = "No Data Found"
    }

    override fun onCleared() {
        super.onCleared()
        topAdsWishlishedUseCase.get().unsubscribe()
        removeWishListUseCase.get().unsubscribe()
        addWishListUseCase.get().unsubscribe()
    }

}
