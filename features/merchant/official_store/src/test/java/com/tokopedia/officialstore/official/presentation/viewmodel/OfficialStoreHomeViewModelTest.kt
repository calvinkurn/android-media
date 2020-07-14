package com.tokopedia.officialstore.official.presentation.viewmodel

import com.tokopedia.officialstore.category.data.model.Category
import com.tokopedia.officialstore.official.data.model.OfficialStoreBanners
import com.tokopedia.officialstore.official.data.model.OfficialStoreBenefits
import com.tokopedia.officialstore.official.data.model.OfficialStoreFeaturedShop
import com.tokopedia.officialstore.official.data.model.dynamic_channel.DynamicChannel
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.topads.sdk.domain.model.WishlistModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Test

class OfficialStoreHomeViewModelTest: OfficialStoreHomeViewModelTestFixture() {

    @Test
    fun given_get_data_success__when_load_first_data__should_set_success_value() {
        runBlocking {
            val prefixUrl = "prefix"
            val slug = "slug"

            val category = createCategory(prefixUrl, slug)
            val channelType = "$prefixUrl$slug"

            val osBanners = OfficialStoreBanners()
            val osBenefits = OfficialStoreBenefits()
            val osFeatured = OfficialStoreFeaturedShop()
            val osDynamicChannel = DynamicChannel()

            onGetOfficialStoreBanners_thenReturn(osBanners)
            onGetOfficialStoreBenefits_thenReturn(osBenefits)
            onGetOfficialStoreFeaturedShop_thenReturn(osFeatured)
            onSetupDynamicChannelParams_thenCompleteWith(channelType)

            viewModel.loadFirstData(category)

            val expectedOSBanners = Success(osBanners)
            val expectedOSBenefits = Success(osBenefits)
            val expectedOSFeaturedShop = Success(osFeatured)
            val expectedOSDynamicChannel = Success(osDynamicChannel)

            verifyOfficialStoreBannersEquals(expectedOSBanners)
            verifyOfficialStoreBenefitsEquals(expectedOSBenefits)
            verifyOfficialStoreFeaturedShopEquals(expectedOSFeaturedShop)
            verifyOfficialStoreDynamicChannelEquals(expectedOSDynamicChannel)
            verifyDynamicChannelParamsEquals(channelType)
        }
    }

//    @Test
//    fun given_get_data_error__when_load_first_data__should_set_error_value() {
//        runBlocking {
//            val error = NullPointerException()
//            val prefixUrl = "prefix"
//            val slug = "slug"
//
//            val category = createCategory(prefixUrl, slug)
//            val channelType = "$prefixUrl$slug"
//
//            onGetOfficialStoreData_thenReturn(error)
//            onSetupDynamicChannelParams_thenCompleteWith(channelType)
//
//            viewModel.loadFirstData(category)
//
//            val expectedError = Fail(NullPointerException())
//
//            verifyLiveDataValueError(expectedError)
//            verifyDynamicChannelParamsEquals(channelType)
//        }
//    }

    @Test
    fun given_get_data_success__when_load_more__should_set_value_with_first_product_recommendation() {
       runBlocking {
           val page = 1
           val category = Category()
           val productRecommendation = listOf(
               RecommendationWidget(title = "Recommendation 1"),
               RecommendationWidget(title = "Recommendation 2")
           )

           onGetOfficialStoreProductRecommendation_thenReturn(productRecommendation)

           viewModel.loadMore(category, page)

           val expectedProductRecommendation = Success(productRecommendation[0])
           verifyOfficialStoreProductRecommendationEquals(expectedProductRecommendation)
       }
    }

    @Test
    fun given_get_data_error__when_load_more__should_set_product_recommendation_error_value() {
        runBlocking {
            val page = 1
            val category = Category()
            val error = NullPointerException()

            onGetOfficialStoreProductRecommendation_thenReturn(error)

            viewModel.loadMore(category, page)

            val expectedError = Fail(NullPointerException())
            verifyOfficialStoreProductRecommendationError(expectedError)
        }
    }

    @Test
    fun given_recommendation_is_top_ads__when_add_to_wishlist__should_set_success_value() {
        runBlocking {
            val isTopAds = true
            val wishList = WishlistModel()
            val recommendation = RecommendationItem(isTopAds = isTopAds)
            val callback = mockk<((Boolean, Throwable?) -> Unit)>()

            onAddTopAdsWishList_thenReturn(wishList)

            viewModel.addWishlist(recommendation, callback)

            val expectedWishList = Success(wishList)
            verifyTopAdsWishListEquals(expectedWishList)

            callback.assertSuccess()
        }
    }

    @Test
    fun given_recommendation_is_top_ads__when_add_to_wishlist_failed__should_set_error_value() {
        runBlocking {
            val isTopAds = true
            val error = NullPointerException()
            val recommendation = RecommendationItem(isTopAds = isTopAds)
            val callback = mockk<((Boolean, Throwable?) -> Unit)>(relaxed = true)

            onAddTopAdsWishList_thenReturn(error)

            viewModel.addWishlist(recommendation, callback)

            val expectedError = Fail(NullPointerException())
            verifyTopAdsWishListError(expectedError)

            callback.assertError(error)
        }
    }

    @Test
    fun given_recommendation_is_NOT_top_ads__when_add_to_wishlist__should_invoke_callback_success() {
        runBlocking {
            val isTopAds = false
            val productId = "15000"
            val userId = "11000"

            val recommendation = createRecommendation(productId, isTopAds)
            val callback = mockk<((Boolean, Throwable?) -> Unit)>(relaxed = true)

            onAddWishList_thenCompleteWith(productId, userId)

            viewModel.addWishlist(recommendation, callback)

            verifyAddWishListUseCaseCalled(productId, userId)
            callback.assertSuccess()
        }
    }

    @Test
    fun given_recommendation_is_NOT_top_ads__when_add_to_wishlist_failed__should_invoke_callback_error() {
        runBlocking {
            val isTopAds = false
            val productId = "1900"
            val userId = "1350"

            val recommendation = createRecommendation(productId, isTopAds)
            val callback = mockk<((Boolean, Throwable?) -> Unit)>(relaxed = true)

            onAddWishList_thenCompleteWith(productId, userId)

            viewModel.addWishlist(recommendation, callback)

            val expectedError = Throwable("Error Message")
            verifyAddWishListUseCaseCalled(productId, userId, expectedError)

            callback.assertError(expectedError)
        }
    }

    @Test
    fun given_gql_call_success__when_remove_wishlist__should_invoke_callback_success() {
        runBlocking {
            val isTopAds = false
            val productId = "15000"
            val userId = "11000"

            val recommendation = createRecommendation(productId, isTopAds)
            val callback = mockk<((Boolean, Throwable?) -> Unit)>(relaxed = true)

            onRemoveWishList_thenCompleteWith(productId, userId)

            viewModel.removeWishlist(recommendation, callback)

            verifyRemoveWishListUseCaseCalled(productId, userId)
            callback.assertSuccess()
        }
    }

    @Test
    fun given_gql_call_error__when_remove_wishlist__should_invoke_callback_error() {
        runBlocking {
            val isTopAds = false
            val productId = "1900"
            val userId = "1350"

            val recommendation = createRecommendation(productId, isTopAds)
            val callback = mockk<((Boolean, Throwable?) -> Unit)>(relaxed = true)

            onRemoveWishList_thenCompleteWith(productId, userId)

            viewModel.removeWishlist(recommendation, callback)

            val expectedError = Throwable("Error Message")
            verifyRemoveWishListUseCaseCalled(productId, userId, expectedError)

            callback.assertError(expectedError)
        }
    }

    @Test
    fun given_user_session_logged_in__when_call_isLoggedIn__should_return_true() {
        val isLoggedIn = true

        onGetUserSessionIsLoggedIn_thenReturn(isLoggedIn)

        verifyIsLoggedInEquals(true)
    }

    @Test
    fun given_user_session_logged_out__when_call_isLoggedIn__should_return_false() {
        val isLoggedIn = false

        onGetUserSessionIsLoggedIn_thenReturn(isLoggedIn)

        verifyIsLoggedInEquals(false)
    }
}