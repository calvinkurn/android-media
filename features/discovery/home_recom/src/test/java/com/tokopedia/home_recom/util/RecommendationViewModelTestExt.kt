package com.tokopedia.home_recom.util

import com.tokopedia.atc_common.domain.usecase.AddToCartOccUseCase
import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase
import com.tokopedia.home_recom.domain.usecases.GetPrimaryProductUseCase
import com.tokopedia.home_recom.view.dispatchers.RecommendationDispatcher
import com.tokopedia.home_recom.viewmodel.PrimaryProductViewModel
import com.tokopedia.home_recom.viewmodel.RecommendationPageViewModel
import com.tokopedia.home_recom.viewmodel.SimilarProductRecommendationViewModel
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.domain.GetSingleRecommendationUseCase
import com.tokopedia.topads.sdk.domain.interactor.TopAdsWishlishedUseCase
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.spekframework.spek2.dsl.TestBody
import org.spekframework.spek2.style.gherkin.FeatureBody

@ExperimentalCoroutinesApi
fun TestBody.createRecommendationPageViewModel(): RecommendationPageViewModel {
    val getRecommendationUseCase by memoized<GetRecommendationUseCase>()
    val addWishListUseCase by memoized<AddWishListUseCase>()
    val removeWishListUseCase by memoized<RemoveWishListUseCase>()
    val topAdsWishlishedUseCase by memoized<TopAdsWishlishedUseCase>()
    val userSessionInterface by memoized<UserSessionInterface>()
    val dispatcherProvider by memoized<RecommendationDispatcher>()

    return RecommendationPageViewModel(
            addWishListUseCase = addWishListUseCase,
            dispatcher = dispatcherProvider,
            getRecommendationUseCase = getRecommendationUseCase,
            removeWishListUseCase = removeWishListUseCase,
            topAdsWishlishedUseCase = topAdsWishlishedUseCase,
            userSessionInterface = userSessionInterface
    )
}

@ExperimentalCoroutinesApi
fun TestBody.createPrimaryProductViewModel(): PrimaryProductViewModel {
    val addToCartUseCase by memoized<AddToCartUseCase>()
    val addWishListUseCase by memoized<AddWishListUseCase>()
    val removeWishListUseCase by memoized<RemoveWishListUseCase>()
    val getPrimaryProductUseCase by memoized<GetPrimaryProductUseCase>()
    val userSessionInterface by memoized<UserSessionInterface>()
    val dispatcherProvider= RecommendationDispatcherTest()

    return PrimaryProductViewModel(
            addWishListUseCase = addWishListUseCase,
            dispatcher = dispatcherProvider,
            userSessionInterface = userSessionInterface,
            addToCartUseCase = addToCartUseCase,
            getPrimaryProductUseCase = getPrimaryProductUseCase,
            removeWishlistUseCase = removeWishListUseCase
    )
}

fun TestBody.createSimilarRecommendationViewModel(): SimilarProductRecommendationViewModel{
    val getSingleRecommendationUseCase by memoized<GetSingleRecommendationUseCase>()
    val addWishListUseCase by memoized<AddWishListUseCase>()
    val removeWishListUseCase by memoized<RemoveWishListUseCase>()
    val topAdsWishlishedUseCase by memoized<TopAdsWishlishedUseCase>()
    val userSessionInterface by memoized<UserSessionInterface>()
    val dispatcherProvider by memoized<RecommendationDispatcher>()

    return SimilarProductRecommendationViewModel(
            addWishListUseCase = addWishListUseCase,
            dispatcher = dispatcherProvider,
            removeWishListUseCase = removeWishListUseCase,
            singleRecommendationUseCase = getSingleRecommendationUseCase,
            topAdsWishlishedUseCase = topAdsWishlishedUseCase,
            userSessionInterface = userSessionInterface
    )
}

fun FeatureBody.createInstance() {
    val getRecommendationUseCase by memoized<GetRecommendationUseCase> { mockk(relaxed = true) }
    val getSingleRecommendationUseCase by memoized<GetSingleRecommendationUseCase> { mockk(relaxed = true) }
    val addToCartOccUseCase by memoized<AddToCartOccUseCase> { mockk(relaxed = true) }
    val addWishListUseCase by memoized<AddWishListUseCase> { mockk(relaxed = true) }
    val removeWishListUseCase by memoized<RemoveWishListUseCase> { mockk(relaxed = true) }
    val topAdsWishlishedUseCase by memoized<TopAdsWishlishedUseCase> { mockk(relaxed = true) }
    val userSessionInterface by memoized<UserSessionInterface> { mockk(relaxed = true) }
    val dispatcherProvider by memoized<RecommendationDispatcher> { mockk(relaxed = true) }
    val addToCartUseCase by memoized<AddToCartUseCase> { mockk(relaxed = true) }
    val getPrimaryProductUseCase by memoized<GetPrimaryProductUseCase> { mockk(relaxed = true) }
}
