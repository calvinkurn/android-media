package com.tokopedia.thankyou_native.recommendation.presentation.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.productcard.utils.getMaxHeightForGridView
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.thankyou_native.R
import com.tokopedia.thankyou_native.recommendation.di.qualifier.CoroutineBackgroundDispatcher
import com.tokopedia.thankyou_native.recommendation.di.qualifier.CoroutineMainDispatcher
import com.tokopedia.thankyou_native.recommendation.model.ProductRecommendationData
import com.tokopedia.thankyou_native.recommendation.model.ThankYouProductCardModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject

class MarketPlaceRecommendationViewModel @Inject constructor(
        @ApplicationContext val context: Context,
        @CoroutineMainDispatcher val mainDispatcher: dagger.Lazy<CoroutineDispatcher>,
        @CoroutineBackgroundDispatcher val backgroundDispatcher: dagger.Lazy<CoroutineDispatcher>,
        private val getRecommendationUseCase: dagger.Lazy<GetRecommendationUseCase>,
        val userSession: dagger.Lazy<UserSessionInterface>) : BaseViewModel(mainDispatcher.get()) {

    private var currentPage = 0

    val recommendationMutableData = MutableLiveData<Result<ProductRecommendationData>>()

    fun loadRecommendationData() {
        launchCatchError(block = {
            val data = loadRecommendationDataFromApi()
            data?.let {
                val recommendationItemList = data.first().recommendationItemList
                if (!recommendationItemList.isNullOrEmpty()) {
                    val marketPlaceRecommendationModelList = getProductCardModel(recommendationItemList)
                    val maxHeight = getMaxHeight(marketPlaceRecommendationModelList)
                    val marketPlaceRecommendationResult = ProductRecommendationData(
                            data.first().title,
                            maxHeight,
                            marketPlaceRecommendationModelList)
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

    private suspend fun getMaxHeight(marketPlaceRecommendationModelList: List<ThankYouProductCardModel>)
            : Int = withContext(backgroundDispatcher.get()) {
        val data = marketPlaceRecommendationModelList.map {
            it.productCardModel
        }
        return@withContext data.getMaxHeightForGridView(
                context = context,
                coroutineDispatcher = backgroundDispatcher.get(),
                productImageWidth = context.resources.getDimensionPixelSize(R.dimen.thank_success_img_height)
        )
    }


    private suspend fun getProductCardModel(recommendationItemList: List<RecommendationItem>)
            : List<ThankYouProductCardModel> = withContext(backgroundDispatcher.get()) {
        recommendationItemList.map { recommendationItem ->
            ThankYouProductCardModel(recommendationItem,
                    ProductCardModel(
                            slashedPrice = recommendationItem.slashedPrice,
                            productName = recommendationItem.name,
                            formattedPrice = recommendationItem.price,
                            productImageUrl = recommendationItem.imageUrl,
                            isTopAds = recommendationItem.isTopAds,
                            discountPercentage = if (recommendationItem.discountPercentageInt > 0)
                                recommendationItem.discountPercentage else "",
                            reviewCount = recommendationItem.countReview,
                            ratingCount = recommendationItem.rating,
                            shopLocation = recommendationItem.location,
                            shopBadgeList = recommendationItem.badgesUrl.map {
                                ProductCardModel.ShopBadge(imageUrl = it
                                        ?: "")
                            },
                            freeOngkir = ProductCardModel.FreeOngkir(
                                    isActive = recommendationItem.isFreeOngkirActive,
                                    imageUrl = recommendationItem.freeOngkirImageUrl
                            ),
                            labelGroupList = recommendationItem.labelGroupList.map { recommendationLabel ->
                                ProductCardModel.LabelGroup(
                                        position = recommendationLabel.position,
                                        title = recommendationLabel.title,
                                        type = recommendationLabel.type
                                )
                            }
                    )
            )
        }


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


    companion object {
        const val X_SOURCE = "recom_widget"
        const val PAGE_NAME = "thankyou_page"
        const val NO_DATA_FOUND = "No Data Found"
    }

    override fun onCleared() {
        super.onCleared()
        getRecommendationUseCase.get().unsubscribe()
       /* topAdsWishlishedUseCase.get().unsubscribe()
        removeWishListUseCase.get().unsubscribe()*/
        //addWishListUseCase.get().unsubscribe()
    }

}

/*
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
    }*/
