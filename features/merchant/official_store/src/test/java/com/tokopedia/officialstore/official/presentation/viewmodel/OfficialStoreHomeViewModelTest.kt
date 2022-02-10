package com.tokopedia.officialstore.official.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.model.ChannelStyle
import com.tokopedia.home_component.model.DynamicChannelLayout
import com.tokopedia.home_component.usecase.featuredshop.DisplayHeadlineAdsEntity
import com.tokopedia.home_component.usecase.featuredshop.GetDisplayHeadlineAds
import com.tokopedia.home_component.usecase.featuredshop.mappingTopAdsHeaderToChannelGrid
import com.tokopedia.home_component.visitable.FeaturedShopDataModel
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.officialstore.category.data.model.Category
import com.tokopedia.officialstore.official.data.model.OfficialStoreBanners
import com.tokopedia.officialstore.official.data.model.OfficialStoreBenefits
import com.tokopedia.officialstore.official.data.model.OfficialStoreChannel
import com.tokopedia.officialstore.official.data.model.OfficialStoreFeaturedShop
import com.tokopedia.officialstore.official.data.model.dynamic_channel.Channel
import com.tokopedia.officialstore.official.domain.GetOfficialStoreBannerUseCase
import com.tokopedia.officialstore.official.domain.GetOfficialStoreBenefitUseCase
import com.tokopedia.officialstore.official.domain.GetOfficialStoreDynamicChannelUseCase
import com.tokopedia.officialstore.official.domain.GetOfficialStoreFeaturedUseCase
import com.tokopedia.officialstore.official.presentation.adapter.datamodel.OfficialTopAdsHeadlineDataModel
import com.tokopedia.officialstore.official.presentation.adapter.datamodel.ProductRecommendationWithTopAdsHeadline
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.recommendation_widget_common.widget.bestseller.mapper.BestSellerMapper
import com.tokopedia.topads.sdk.domain.interactor.TopAdsWishlishedUseCase
import com.tokopedia.topads.sdk.domain.model.WishlistModel
import com.tokopedia.topads.sdk.domain.usecase.GetTopAdsHeadlineUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlist.common.listener.WishListActionListener
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import rx.Observable
import rx.observables.BlockingObservable

@ExperimentalCoroutinesApi
class OfficialStoreHomeViewModelTest {

    @RelaxedMockK
    lateinit var getOfficialStoreBannersUseCase: GetOfficialStoreBannerUseCase

    @RelaxedMockK
    lateinit var getOfficialStoreBenefitUseCase: GetOfficialStoreBenefitUseCase

    @RelaxedMockK
    lateinit var getOfficialStoreFeaturedShopUseCase: GetOfficialStoreFeaturedUseCase

    @RelaxedMockK
    lateinit var getOfficialStoreDynamicChannelUseCase: GetOfficialStoreDynamicChannelUseCase

    @RelaxedMockK
    lateinit var getRecommendationUseCase: GetRecommendationUseCase

    @RelaxedMockK
    lateinit var userSessionInterface: UserSessionInterface

    @RelaxedMockK
    lateinit var addWishListUseCase: AddWishListUseCase

    @RelaxedMockK
    lateinit var topAdsWishlishedUseCase: TopAdsWishlishedUseCase

    @RelaxedMockK
    lateinit var removeWishListUseCase: RemoveWishListUseCase

    @RelaxedMockK
    lateinit var getDisplayHeadlineAds: GetDisplayHeadlineAds

    @RelaxedMockK
    lateinit var getTopAdsHeadlineUseCase: GetTopAdsHeadlineUseCase

    @RelaxedMockK
    lateinit var getRecommendationUseCaseCoroutine: com.tokopedia.recommendation_widget_common.domain.coroutines.GetRecommendationUseCase

    @RelaxedMockK
    lateinit var bestSellerMapper: BestSellerMapper

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    private val viewModel by lazy {
        OfficialStoreHomeViewModel(
            getOfficialStoreBannersUseCase,
            getOfficialStoreBenefitUseCase,
            getOfficialStoreFeaturedShopUseCase,
            getOfficialStoreDynamicChannelUseCase,
            getRecommendationUseCase,
            userSessionInterface,
            addWishListUseCase,
            topAdsWishlishedUseCase,
            removeWishListUseCase,
            getDisplayHeadlineAds,
            getRecommendationUseCaseCoroutine,
            bestSellerMapper,
            getTopAdsHeadlineUseCase,
            CoroutineTestDispatchersProvider
        )
    }

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
            val osDynamicChannel = mutableListOf<OfficialStoreChannel>()

            onGetOfficialStoreBanners_thenReturn(osBanners)
            onGetOfficialStoreBenefits_thenReturn(osBenefits)
            onGetOfficialStoreFeaturedShop_thenReturn(osFeatured)
            onGetDynamicChannel_thenReturn(osDynamicChannel)
            onSetupDynamicChannelParams_thenCompleteWith(channelType)

            viewModel.loadFirstData(category, "")

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

    @Test
    fun given_get_data_error__when_load_first_data__should_set_error_value() {
        runBlocking {
            val error = NullPointerException()
            val prefixUrl = "prefix"
            val slug = "slug"
            val category = createCategory(prefixUrl, slug)
            val channelType = "$prefixUrl$slug"

            onGetOfficialStoreData_thenReturn(error)
            onSetupDynamicChannelParams_thenCompleteWith(channelType)

            viewModel.loadFirstData(category, "")
            val expectedError = Fail(NullPointerException())

            verifyLiveDataValueError(expectedError)
            verifyDynamicChannelParamsEquals(channelType)
        }
    }

    @Test
    fun given_get_data_success__when_load_more__should_set_value_with_first_product_recommendation() {
        val page = 1
        val categoryId = "0"     // "65, 20, 60, 288, 297, 578, 2099
        val listOfRecom = mutableListOf(RecommendationWidget())
        val productRecommendationWithTopAdsHeadline = ProductRecommendationWithTopAdsHeadline(listOfRecom.first(), null)

        coEvery {
            getRecommendationUseCase.createObservable(any()).toBlocking().first()
        } returns listOfRecom

        viewModel.loadMoreProducts(categoryId, page)

        coVerify {
            getRecommendationUseCase.createObservable(any())
        }
        print(viewModel.productRecommendation.value)
        Assert.assertEquals((viewModel.productRecommendation.value as Success).data, productRecommendationWithTopAdsHeadline)
    }

    @Test
    fun given_get_data_success__when_load_more__should_set_value_with_first_product_recommendation_with_topads_headline_ads() {
        val page = 1
        val categoryId = "0"     // "65, 20, 60, 288, 297, 578, 2099
        val listOfRecom = mutableListOf(RecommendationWidget())
        val topAdsHeadlineAd = OfficialTopAdsHeadlineDataModel()
        val productRecommendationWithTopAdsHeadline = ProductRecommendationWithTopAdsHeadline(listOfRecom.first(), topAdsHeadlineAd)

        coEvery {
            getRecommendationUseCase.createObservable(any()).toBlocking().first()
        } returns listOfRecom

//        coEvery { viewModel.isFeaturedShopAllowed } returns true

        coEvery {
            viewModel.getTopAdsHeadlineData(page)
        } returns topAdsHeadlineAd

        viewModel.loadMoreProducts(categoryId, page)

        coVerify {
            getRecommendationUseCase.createObservable(any())
        }
        print(viewModel.productRecommendation.value)
        Assert.assertEquals((viewModel.productRecommendation.value as Success).data, productRecommendationWithTopAdsHeadline)
    }

    @Test
    fun given_get_data_error__when_load_more__should_set_product_recommendation_error_value() {
        val page = 1
        val categoryId = "0"     // "65, 20, 60, 288, 297, 578, 2099

        coEvery {
            getRecommendationUseCase.createObservable(any()).toBlocking()
        } throws Throwable()

        viewModel.loadMoreProducts(categoryId, page)

        coVerify {
            getRecommendationUseCase.createObservable(any())
        }
        print(viewModel.productRecommendation.value)
        Assert.assertTrue(viewModel.productRecommendation.value is Fail)
    }

    @Test
    fun given_recommendation_is_top_ads__when_add_to_wishlist__should_set_success_value() {
        runBlocking {
            val isTopAds = true
            val wishList = WishlistModel()
            val recommendation = RecommendationItem(isTopAds = isTopAds)
            val callback = mockk<((Boolean, Throwable?) -> Unit)>()


            coEvery {
                topAdsWishlishedUseCase.createObservable(any())
            } returns mockObservable(wishList)

            viewModel.addWishlist(recommendation, callback)
            coVerify { topAdsWishlishedUseCase.createObservable(any()) }

            verify { callback.invoke(any(), any()) }

            print(viewModel.topAdsWishlistResult)
            Assert.assertTrue(viewModel.topAdsWishlistResult.value is Success)
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

            coEvery {
                topAdsWishlishedUseCase.createObservable(any())
            } throws error

            viewModel.addWishlist(recommendation, callback)
            val expectedError = Fail(NullPointerException())
            coVerify { topAdsWishlishedUseCase.createObservable(any()) }

            viewModel.topAdsWishlistResult.assertError(expectedError)
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
            val listener = CapturingSlot<WishListActionListener>()

            coVerify {
                addWishListUseCase.createObservable(productId, userId, capture(listener))
            }

            listener.captured.onSuccessAddWishlist(productId)
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
            val listener = CapturingSlot<WishListActionListener>()

            coVerify {
                addWishListUseCase.createObservable(productId, userId, capture(listener))
            }

            listener.captured.onErrorAddWishList(expectedError.message, productId)
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

            coEvery { userSessionInterface.userId } returns userId
            coEvery { removeWishListUseCase.createObservable(productId, userId, any()) } returns Unit

            viewModel.removeWishlist(recommendation, callback)
            val listener = CapturingSlot<WishListActionListener>()

            coVerify {
                removeWishListUseCase.createObservable(productId, userId, capture(listener))
            }

            listener.captured.onSuccessRemoveWishlist(productId)
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

            coEvery { userSessionInterface.userId } returns userId
            coEvery { removeWishListUseCase.createObservable(productId, userId, any()) } returns Unit

            viewModel.removeWishlist(recommendation, callback)
            val expectedError = Throwable("Error Message")
            val listener = CapturingSlot<WishListActionListener>()

            coVerify {
                removeWishListUseCase.createObservable(productId, userId, capture(listener))
            }

            listener.captured.onErrorRemoveWishlist(expectedError.message, productId)
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

    @Test
    fun given_get_headlineAds_success_then_pass_to_view() {
        val prefixUrl = "prefix"
        val slug = "slug"
        val category = createCategory(prefixUrl, slug)
        val channelId = "123"

        val dynamicChannelResponse: MutableList<OfficialStoreChannel> = mutableListOf()
        dynamicChannelResponse.addAll(
                listOf(
                        OfficialStoreChannel(channel = Channel(layout = DynamicChannelLayout.LAYOUT_FEATURED_SHOP, id = channelId))
                )
        )

        val headlineAdsResponse: MutableList<DisplayHeadlineAdsEntity.DisplayHeadlineAds> = mutableListOf()
        headlineAdsResponse.addAll(
                listOf(
                        DisplayHeadlineAdsEntity.DisplayHeadlineAds(id = "1"),
                        DisplayHeadlineAdsEntity.DisplayHeadlineAds(id = "2"),
                        DisplayHeadlineAdsEntity.DisplayHeadlineAds(id = "3"),
                ))

        val featureShopResult = FeaturedShopDataModel(
                channelModel = ChannelModel(
                        id = channelId,
                        groupId = "",
                        channelGrids = headlineAdsResponse.mappingTopAdsHeaderToChannelGrid(),
                        style = ChannelStyle.ChannelOS,

                )
        )
        coEvery { getOfficialStoreDynamicChannelUseCase.executeOnBackground() } returns dynamicChannelResponse
        coEvery { getDisplayHeadlineAds.executeOnBackground() } returns headlineAdsResponse

        viewModel.loadFirstData(category, "")

        Assert.assertEquals((viewModel.featuredShopResult.value as Success).data.channelModel.id, featureShopResult.channelModel.id)
        Assert.assertEquals((viewModel.featuredShopResult.value as Success).data.channelModel.channelGrids.size, featureShopResult.channelModel.channelGrids.size)
    }

    @Test
    fun given_get_headlineAds_empty_list_then_pass_error_to_view() {
        val prefixUrl = "prefix"
        val slug = "slug"
        val category = createCategory(prefixUrl, slug)
        val channelId = "123"
        val sizeZero = 0

        val dynamicChannelResponse: MutableList<OfficialStoreChannel> = mutableListOf()
        dynamicChannelResponse.addAll(
                listOf(
                        OfficialStoreChannel(channel = Channel(layout = DynamicChannelLayout.LAYOUT_FEATURED_SHOP, id = channelId))
                )
        )

        val headlineAdsResponse: MutableList<DisplayHeadlineAdsEntity.DisplayHeadlineAds> = mutableListOf()

        coEvery { getOfficialStoreDynamicChannelUseCase.executeOnBackground() } returns dynamicChannelResponse
        coEvery { getDisplayHeadlineAds.executeOnBackground() } returns headlineAdsResponse

        viewModel.loadFirstData(category, "")

        Assert.assertEquals(viewModel.featuredShopRemove.value?.channelModel?.channelGrids?.size ?: sizeZero, sizeZero)
    }


    // ===================================== //
    private fun verifyIsLoggedInEquals(expectedLoggedInStatus: Boolean) {
        val actualLoggedInStatus = viewModel.isLoggedIn()
        assertEquals(expectedLoggedInStatus, actualLoggedInStatus)
    }

    private fun onGetUserSessionIsLoggedIn_thenReturn(loggedIn: Boolean) {
        every { userSessionInterface.isLoggedIn } returns loggedIn
    }

    private fun onGetOfficialStoreBanners_thenReturn(osBanners: OfficialStoreBanners) {
        coEvery { getOfficialStoreBannersUseCase.executeOnBackground(any()) } returns osBanners
    }

    private fun onGetOfficialStoreBenefits_thenReturn(osBenefits: OfficialStoreBenefits) {
        coEvery { getOfficialStoreBenefitUseCase.executeOnBackground() } returns osBenefits
    }

    private fun onGetOfficialStoreFeaturedShop_thenReturn(osFeatured: OfficialStoreFeaturedShop) {
        coEvery { getOfficialStoreFeaturedShopUseCase.executeOnBackground() } returns osFeatured
    }

    private fun onGetDynamicChannel_thenReturn(list: List<OfficialStoreChannel>) {
        coEvery { getOfficialStoreDynamicChannelUseCase.executeOnBackground() } returns list
    }

    private fun onAddWishList_thenCompleteWith(productId: String, userId: String) {
        coEvery { userSessionInterface.userId } returns userId
        coEvery { addWishListUseCase.createObservable(productId, userId, any()) } returns Unit
    }

    private fun createCategory(prefixUrl: String, slug: String): Category {
        return Category(prefixUrl = prefixUrl, slug = slug)
    }

    private fun onGetOfficialStoreData_thenReturn(error: NullPointerException) {
        onGetOfficialStoreBanners_thenReturn(error)
        onGetOfficialStoreBenefits_thenReturn(error)
        onGetOfficialStoreFeaturedShop_thenReturn(error)
        onGetDynamicChannel_thenReturn(error)
    }

    private fun onGetOfficialStoreBanners_thenReturn(error: Throwable) {
        coEvery { getOfficialStoreBannersUseCase.executeOnBackground(any()) } throws error
    }

    private fun onGetOfficialStoreBenefits_thenReturn(error: Throwable) {
        coEvery { getOfficialStoreBenefitUseCase.executeOnBackground() } throws error
    }

    private fun onGetOfficialStoreFeaturedShop_thenReturn(error: Throwable) {
        coEvery { getOfficialStoreFeaturedShopUseCase.executeOnBackground() } throws error
    }

    private fun onGetDynamicChannel_thenReturn(error: Throwable) {
        coEvery { getOfficialStoreDynamicChannelUseCase.executeOnBackground() } throws error
    }

    private fun onSetupDynamicChannelParams_thenCompleteWith(channelType: String) {
        coEvery { getOfficialStoreDynamicChannelUseCase.setupParams(channelType, "") } returns Unit
    }

    private fun verifyOfficialStoreBannersEquals(
            expectedOSBanners: Success<OfficialStoreBanners>
    ) {
        verifyGetOfficialStoreBannersUseCaseCalled()

        viewModel.officialStoreBannersResult
                .assertPairSuccess(expectedOSBanners)
    }

    private fun verifyOfficialStoreBenefitsEquals(
            expectedOSBenefits: Success<OfficialStoreBenefits>
    ) {
        verifyGetOfficialStoreBenefitsUseCaseCalled()

        viewModel.officialStoreBenefitsResult
                .assertSuccess(expectedOSBenefits)
    }

    private fun verifyOfficialStoreFeaturedShopEquals(
            expectedFeaturedOS: Success<OfficialStoreFeaturedShop>
    ) {
        verifyGetOfficialStoreFeaturedShopUseCaseCalled()

        viewModel.officialStoreFeaturedShopResult
                .assertSuccess(expectedFeaturedOS)
    }

    private fun verifyOfficialStoreDynamicChannelEquals(
            expectedDynamicChannel: Success<List<OfficialStoreChannel>>
    ) {
        verifyGetOfficialDynamicChannelCalled()

        viewModel.officialStoreDynamicChannelResult
                .assertSuccess(expectedDynamicChannel)
    }

    private fun verifyGetOfficialStoreBannersUseCaseCalled() {
        coVerify { getOfficialStoreBannersUseCase.executeOnBackground(any()) }
    }

    private fun verifyGetOfficialStoreBenefitsUseCaseCalled() {
        coVerify { getOfficialStoreBenefitUseCase.executeOnBackground() }
    }

    private fun verifyGetOfficialStoreFeaturedShopUseCaseCalled() {
        coVerify { getOfficialStoreFeaturedShopUseCase.executeOnBackground() }
    }

    private fun verifyGetOfficialDynamicChannelCalled() {
        coVerify {
            getOfficialStoreDynamicChannelUseCase.executeOnBackground()
        }
    }

    private fun verifyLiveDataValueError(expectedError: Fail) {
        verifyOfficialStoreBannersError(expectedError)
        verifyOfficialStoreBenefitsError(expectedError)
        verifyOfficialStoreFeaturedShopError(expectedError)
        verifyOfficialStoreDynamicChannelError(expectedError)
    }

    private fun verifyOfficialStoreBannersError(expectedError: Fail) {
        coVerify { getOfficialStoreBannersUseCase.executeOnBackground(any()) }

        viewModel.officialStoreBannersResult
                .assertPairError(expectedError)
    }

    private fun verifyOfficialStoreBenefitsError(expectedError: Fail) {
        coVerify { getOfficialStoreBannersUseCase.executeOnBackground(any()) }

        viewModel.officialStoreBenefitsResult
                .assertError(expectedError)
    }

    private fun verifyOfficialStoreFeaturedShopError(expectedError: Fail) {
        coVerify { getOfficialStoreFeaturedShopUseCase.executeOnBackground() }

        viewModel.officialStoreFeaturedShopResult
                .assertError(expectedError)
    }

    private fun verifyOfficialStoreDynamicChannelError(expectedError: Fail) {
        val error = expectedError.throwable
        verifyGetOfficialDynamicChannelCalled(error)

        viewModel.officialStoreDynamicChannelResult
                .assertError(expectedError)
    }

    private fun verifyGetOfficialDynamicChannelCalled(error: Throwable) {
        coVerify { getOfficialStoreDynamicChannelUseCase.executeOnBackground() }
    }

    private fun verifyDynamicChannelParamsEquals(channelType: String) {
        coVerify { getOfficialStoreDynamicChannelUseCase.setupParams(channelType, "") }
    }


    private fun createRecommendation(productId: String, isTopAds: Boolean): RecommendationItem {
        return RecommendationItem(productId = productId.toLongOrZero(), isTopAds = isTopAds)
    }

    private fun <T> mockObservable(data: T): Observable<T> {
        val obs = mockk<Observable<T>>()
        val blockingObs = mockk<BlockingObservable<T>>()

        coEvery { blockingObs.first() } returns data
        coEvery { obs.toBlocking() } returns blockingObs

        return obs
    }

    private fun <T> LiveData<T>.assertSuccess(expectedValue: Success<*>) {
        val actualValue = value
        assertEquals(expectedValue, actualValue)
    }

    private fun <T> LiveData<T>.assertPairSuccess(expectedValue: Success<*>) {
        val actualValue = (value as? Pair<Any, Any>)?.second
        assertEquals(expectedValue, actualValue)
    }

    private fun ((Boolean, Throwable?) -> Unit).assertSuccess() {
        coVerify { this@assertSuccess.invoke(true, null) }
    }

    private fun <T> LiveData<T>.assertError(error: Fail) {
        val actualError = value.toString()
        val expectedError = error.toString()
        assertEquals(expectedError, actualError)
    }

    private fun <T> LiveData<T>.assertPairError(error: Fail) {
        val actualError = (value as? Pair<Any, Any>)?.second.toString()
        val expectedError = error.toString()
        assertEquals(expectedError, actualError)
    }

    private fun ((Boolean, Throwable?) -> Unit).assertError(error: Throwable?) {
        val throwable = CapturingSlot<Throwable>()
        coVerify { this@assertError.invoke(false, capture(throwable)) }

        val expectedError = error.toString().trim()
        val actualError = throwable.captured.toString().trim()

        assertEquals(expectedError, actualError)
    }
}