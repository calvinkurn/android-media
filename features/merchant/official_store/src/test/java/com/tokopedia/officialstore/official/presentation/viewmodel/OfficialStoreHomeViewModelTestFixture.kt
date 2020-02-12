package com.tokopedia.officialstore.official.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
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
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlist.common.listener.WishListActionListener
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import io.mockk.CapturingSlot
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import rx.Observable
import rx.Scheduler
import rx.android.plugins.RxAndroidPlugins
import rx.android.plugins.RxAndroidSchedulersHook
import rx.observables.BlockingObservable
import rx.schedulers.Schedulers

abstract class OfficialStoreHomeViewModelTestFixture {

    @get:Rule
    val rule = InstantTaskExecutorRule()
    
    private lateinit var getOfficialStoreBannersUseCase: GetOfficialStoreBannerUseCase
    private lateinit var getOfficialStoreBenefitUseCase: GetOfficialStoreBenefitUseCase
    private lateinit var getOfficialStoreFeaturedShopUseCase: GetOfficialStoreFeaturedUseCase
    private lateinit var getOfficialStoreDynamicChannelUseCase: GetOfficialStoreDynamicChannelUseCase
    private lateinit var getRecommendationUseCase: GetRecommendationUseCase
    private lateinit var userSessionInterface: UserSessionInterface
    private lateinit var addWishListUseCase: AddWishListUseCase
    private lateinit var topAdsWishlishedUseCase: TopAdsWishlishedUseCase
    private lateinit var removeWishListUseCase: RemoveWishListUseCase

    protected lateinit var viewModel: OfficialStoreHomeViewModel

    @Before
    fun setUp() {
        registerRxSchedulerHook()

        getOfficialStoreBannersUseCase = mockk(relaxed = true)
        getOfficialStoreBenefitUseCase = mockk(relaxed = true)
        getOfficialStoreFeaturedShopUseCase = mockk(relaxed = true)
        getOfficialStoreDynamicChannelUseCase = mockk(relaxed = true)
        getRecommendationUseCase = mockk(relaxed = true)
        userSessionInterface = mockk(relaxed = true)
        addWishListUseCase = mockk(relaxed = true)
        topAdsWishlishedUseCase = mockk(relaxed = true)
        removeWishListUseCase = mockk(relaxed = true)

        viewModel = OfficialStoreHomeViewModel(
            getOfficialStoreBannersUseCase,
            getOfficialStoreBenefitUseCase,
            getOfficialStoreFeaturedShopUseCase,
            getOfficialStoreDynamicChannelUseCase,
            getRecommendationUseCase,
            userSessionInterface,
            addWishListUseCase,
            topAdsWishlishedUseCase,
            removeWishListUseCase,
            Dispatchers.Unconfined
        )
    }

    @After
    fun reset() {
        clearAllMocks()
        resetRxSchedulerHook()
    }

    // region stub
    protected fun onGetOfficialStoreBanners_thenReturn(osBanners: OfficialStoreBanners) {
        coEvery { getOfficialStoreBannersUseCase.executeOnBackground() } returns osBanners
    }

    protected fun onGetOfficialStoreBenefits_thenReturn(osBenefits: OfficialStoreBenefits) {
        coEvery { getOfficialStoreBenefitUseCase.executeOnBackground() } returns osBenefits
    }

    protected fun onGetOfficialStoreFeaturedShop_thenReturn(osFeatured: OfficialStoreFeaturedShop) {
        coEvery { getOfficialStoreFeaturedShopUseCase.executeOnBackground() } returns osFeatured
    }

    protected fun onGetOfficialStoreProductRecommendation_thenReturn(recommendations: List<RecommendationWidget>) {
        coEvery { getRecommendationUseCase.getOfficialStoreRecomParams(any(), any(), any()) } returns RequestParams()
        coEvery { getRecommendationUseCase.createObservable(any()) } returns mockObservable(recommendations)
    }

    protected fun onSetupDynamicChannelParams_thenCompleteWith(channelType: String) {
        coEvery { getOfficialStoreDynamicChannelUseCase.setupParams(channelType) } returns Unit
    }

    protected fun onAddTopAdsWishList_thenReturn(wishList: WishlistModel) {
        coEvery { topAdsWishlishedUseCase.createObservable(any()) } returns mockObservable(wishList)
    }

    protected fun onAddWishList_thenCompleteWith(productId: String, userId: String) {
        coEvery { userSessionInterface.userId } returns userId
        coEvery { addWishListUseCase.createObservable(productId, userId, any()) } returns Unit
    }

    protected fun onRemoveWishList_thenCompleteWith(productId: String, userId: String) {
        coEvery { userSessionInterface.userId } returns userId
        coEvery { removeWishListUseCase.createObservable(productId, userId, any()) } returns Unit
    }

    protected fun onGetUserSessionIsLoggedIn_thenReturn(loggedIn: Boolean) {
        every { userSessionInterface.isLoggedIn } returns loggedIn
    }

    private fun onGetOfficialStoreBanners_thenReturn(error: Throwable) {
        coEvery { getOfficialStoreBannersUseCase.executeOnBackground() } throws error
    }

    private fun onGetOfficialStoreBenefits_thenReturn(error: Throwable) {
        coEvery { getOfficialStoreBenefitUseCase.executeOnBackground() } throws error
    }

    private fun onGetOfficialStoreFeaturedShop_thenReturn(error: Throwable) {
        coEvery { getOfficialStoreFeaturedShopUseCase.executeOnBackground() } throws error
    }

    protected fun onGetOfficialStoreProductRecommendation_thenReturn(error: Throwable) {
        coEvery { getRecommendationUseCase.createObservable(any()) } throws error
    }

    protected fun onAddTopAdsWishList_thenReturn(error: Throwable) {
        coEvery { topAdsWishlishedUseCase.createObservable(any()) } throws error
    }

    protected fun onGetOfficialStoreData_thenReturn(error: NullPointerException) {
        onGetOfficialStoreBanners_thenReturn(error)
        onGetOfficialStoreBenefits_thenReturn(error)
        onGetOfficialStoreFeaturedShop_thenReturn(error)
    }

    private fun<T> mockObservable(data: T): Observable<T>  {
        val obs = mockk<Observable<T>>()
        val blockingObs = mockk<BlockingObservable<T>>()

        coEvery { blockingObs.first() } returns data
        coEvery { obs.toBlocking() } returns blockingObs

        return obs
    }

    protected fun createRecommendation(productId: String, isTopAds: Boolean): RecommendationItem {
        return RecommendationItem(productId = productId.toInt(), isTopAds = isTopAds)
    }

    protected fun createCategory(prefixUrl: String, slug: String): Category {
        return Category(prefixUrl = prefixUrl, slug = slug)
    }
    // endregion

    // region verification
    protected fun verifyOfficialStoreBannersEquals(
        expectedOSBanners: Success<OfficialStoreBanners>
    ) {
        verifyGetOfficialStoreBannersUseCaseCalled()

        viewModel.officialStoreBannersResult
            .assertSuccess(expectedOSBanners)
    }

    protected fun verifyOfficialStoreBenefitsEquals(
        expectedOSBenefits: Success<OfficialStoreBenefits>
    ) {
        verifyGetOfficialStoreBenefitsUseCaseCalled()

        viewModel.officialStoreBenefitsResult
            .assertSuccess(expectedOSBenefits)
    }

    protected fun verifyOfficialStoreFeaturedShopEquals(
        expectedFeaturedOS: Success<OfficialStoreFeaturedShop>
    ) {
        verifyGetOfficialStoreFeaturedShopUseCaseCalled()

        viewModel.officialStoreFeaturedShopResult
            .assertSuccess(expectedFeaturedOS)
    }

    protected fun verifyOfficialStoreDynamicChannelEquals(
        expectedDynamicChannel: Success<DynamicChannel>
    ) {
        val dynamicChannel = expectedDynamicChannel.data
        verifyGetOfficialDynamicChannelCalled(dynamicChannel)

        viewModel.officialStoreDynamicChannelResult
            .assertSuccess(expectedDynamicChannel)
    }

    protected fun verifyDynamicChannelParamsEquals(channelType: String) {
        coVerify { getOfficialStoreDynamicChannelUseCase.setupParams(channelType) }
    }

    protected fun verifyLiveDataValueError(expectedError: Fail) {
        verifyOfficialStoreBannersError(expectedError)
        verifyOfficialStoreBenefitsError(expectedError)
        verifyOfficialStoreFeaturedShopError(expectedError)
        verifyOfficialStoreDynamicChannelError(expectedError)
    }

    protected fun verifyOfficialStoreProductRecommendationEquals(
        expectedProductRecommendation: Success<RecommendationWidget>
    ) {
        verifyGetOfficialStoreProductRecommendationUseCaseCalled()

        viewModel.officialStoreProductRecommendationResult
            .assertSuccess(expectedProductRecommendation)
    }

    protected fun verifyTopAdsWishListEquals(
        expectedTopAdsWishList: Success<WishlistModel>
    ) {
        verifyAddTopAdsWishListUseCaseCalled()

        viewModel.topAdsWishlistResult
            .assertSuccess(expectedTopAdsWishList)
    }

    protected fun verifyIsLoggedInEquals(expectedLoggedInStatus: Boolean) {
        val actualLoggedInStatus = viewModel.isLoggedIn()
        assertEquals(expectedLoggedInStatus, actualLoggedInStatus)
    }

    protected fun ((Boolean, Throwable?) -> Unit).assertSuccess() {
        coVerify { this@assertSuccess.invoke(true, null) }
    }

    protected fun ((Boolean, Throwable?) -> Unit).assertError(error: Throwable?) {
        val throwable = CapturingSlot<Throwable>()
        coVerify { this@assertError.invoke(false, capture(throwable)) }

        val expectedError = error.toString().trim()
        val actualError = throwable.captured.toString().trim()

        assertEquals(expectedError, actualError)
    }

    private fun verifyOfficialStoreBannersError(expectedError: Fail) {
        verifyGetOfficialStoreBannersUseCaseCalled()

        viewModel.officialStoreBannersResult
            .assertError(expectedError)
    }

    private fun verifyOfficialStoreBenefitsError(expectedError: Fail) {
        verifyGetOfficialStoreBannersUseCaseCalled()

        viewModel.officialStoreBenefitsResult
            .assertError(expectedError)
    }

    private fun verifyOfficialStoreFeaturedShopError(expectedError: Fail) {
        verifyGetOfficialStoreFeaturedShopUseCaseCalled()

        viewModel.officialStoreFeaturedShopResult
            .assertError(expectedError)
    }

    private fun verifyOfficialStoreDynamicChannelError(expectedError: Fail) {
        val error = expectedError.throwable
        verifyGetOfficialDynamicChannelCalled(error)

        viewModel.officialStoreDynamicChannelResult
            .assertError(expectedError)
    }

    protected fun verifyOfficialStoreProductRecommendationError(expectedError: Fail) {
        verifyGetOfficialStoreProductRecommendationUseCaseCalled()

        viewModel.officialStoreProductRecommendationResult
            .assertError(expectedError)
    }

    protected fun verifyTopAdsWishListError(expectedError: Fail) {
        verifyAddTopAdsWishListUseCaseCalled()

        viewModel.topAdsWishlistResult
            .assertError(expectedError)
    }

    private fun verifyGetOfficialStoreBannersUseCaseCalled() {
        coVerify { getOfficialStoreBannersUseCase.executeOnBackground() }
    }

    private fun verifyGetOfficialStoreBenefitsUseCaseCalled() {
        coVerify { getOfficialStoreBenefitUseCase.executeOnBackground() }
    }

    private fun verifyGetOfficialStoreFeaturedShopUseCaseCalled() {
        coVerify { getOfficialStoreFeaturedShopUseCase.executeOnBackground() }
    }

    private fun verifyGetOfficialDynamicChannelCalled(dynamicChannel: DynamicChannel) {
        val onSuccess = CapturingSlot<(DynamicChannel) -> Unit>()

        coVerify {
            getOfficialStoreDynamicChannelUseCase.execute(capture(onSuccess), any())
        }

        onSuccess.captured.invoke(dynamicChannel)
    }

    private fun verifyGetOfficialDynamicChannelCalled(error: Throwable) {
        val onError = CapturingSlot<(Throwable) -> Unit>()

        coVerify {
            getOfficialStoreDynamicChannelUseCase.execute(any(), capture(onError))
        }

        onError.captured.invoke(error)
    }

    private fun verifyGetOfficialStoreProductRecommendationUseCaseCalled() {
        coVerify { getRecommendationUseCase.createObservable(any()) }
    }

    private fun verifyAddTopAdsWishListUseCaseCalled() {
        coVerify { topAdsWishlishedUseCase.createObservable(any()) }
    }

    protected fun verifyAddWishListUseCaseCalled(productId: String, userId: String) {
        val listener = CapturingSlot<WishListActionListener>()

        coVerify {
            addWishListUseCase.createObservable(productId, userId, capture(listener))
        }

        listener.captured.onSuccessAddWishlist(productId)
    }

    protected fun verifyAddWishListUseCaseCalled(productId: String, userId: String, error: Throwable) {
        val listener = CapturingSlot<WishListActionListener>()

        coVerify {
            addWishListUseCase.createObservable(productId, userId, capture(listener))
        }

        listener.captured.onErrorAddWishList(error.message, productId)
    }

    protected fun verifyRemoveWishListUseCaseCalled(productId: String, userId: String) {
        val listener = CapturingSlot<WishListActionListener>()

        coVerify {
            removeWishListUseCase.createObservable(productId, userId, capture(listener))
        }

        listener.captured.onSuccessRemoveWishlist(productId)
    }

    protected fun verifyRemoveWishListUseCaseCalled(productId: String, userId: String, error: Throwable) {
        val listener = CapturingSlot<WishListActionListener>()

        coVerify {
            removeWishListUseCase.createObservable(productId, userId, capture(listener))
        }

        listener.captured.onErrorRemoveWishlist(error.message, productId)
    }

    private fun<T> LiveData<T>.assertSuccess(expectedValue: Success<*>) {
        val actualValue = value
        assertEquals(expectedValue, actualValue)
    }

    private fun<T> LiveData<T>.assertError(error: Fail) {
        val actualError = value.toString()
        val expectedError = error.toString()
        assertEquals(expectedError, actualError)
    }
    // endregion
    
    // region private methods
    private fun registerRxSchedulerHook() {
        RxAndroidPlugins.getInstance().registerSchedulersHook(object : RxAndroidSchedulersHook() {
            override fun getMainThreadScheduler(): Scheduler {
                return Schedulers.immediate()
            }
        })
    }

    private fun resetRxSchedulerHook() {
        @Suppress("UnstableApiUsage")
        RxAndroidPlugins.getInstance().reset()
    }
    // endregion
}