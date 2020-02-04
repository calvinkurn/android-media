package com.tokopedia.officialstore.official.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import com.tokopedia.officialstore.official.data.model.OfficialStoreBanners
import com.tokopedia.officialstore.official.data.model.OfficialStoreBenefits
import com.tokopedia.officialstore.official.data.model.OfficialStoreFeaturedShop
import com.tokopedia.officialstore.official.data.model.dynamic_channel.DynamicChannel
import com.tokopedia.officialstore.official.domain.GetOfficialStoreBannerUseCase
import com.tokopedia.officialstore.official.domain.GetOfficialStoreBenefitUseCase
import com.tokopedia.officialstore.official.domain.GetOfficialStoreDynamicChannelUseCase
import com.tokopedia.officialstore.official.domain.GetOfficialStoreFeaturedUseCase
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.topads.sdk.domain.interactor.TopAdsWishlishedUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import io.mockk.CapturingSlot
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import rx.Observable
import rx.observables.BlockingObservable

abstract class OfficialStoreHomeViewModelTestFixture {

    @get:Rule
    val rule = InstantTaskExecutorRule()

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

    protected lateinit var viewModel: OfficialStoreHomeViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

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
        val recommendationObs = mockk<Observable<List<RecommendationWidget>>>()
        val recommendationBlockingObs = mockk<BlockingObservable<List<RecommendationWidget>>>()

        coEvery { recommendationBlockingObs.first() } returns recommendations
        coEvery { recommendationObs.toBlocking() } returns recommendationBlockingObs

        coEvery { getRecommendationUseCase.getRecomParams(any(), any(), any(), any()) } returns RequestParams()
        coEvery { getRecommendationUseCase.createObservable(any()) } returns recommendationObs
    }

    protected fun onGetOfficialStoreBanners_thenReturn(error: Throwable) {
        coEvery { getOfficialStoreBannersUseCase.executeOnBackground() } throws error
    }

    protected fun onGetOfficialStoreBenefits_thenReturn(error: Throwable) {
        coEvery { getOfficialStoreBenefitUseCase.executeOnBackground() } throws error
    }

    protected fun onGetOfficialStoreFeaturedShop_thenReturn(error: Throwable) {
        coEvery { getOfficialStoreFeaturedShopUseCase.executeOnBackground() } throws error
    }

    protected fun onGetOfficialStoreProductRecommendation_thenReturn(error: Throwable) {
        coEvery { getRecommendationUseCase.createObservable(any()) } throws error
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

    protected fun verifyOfficialStoreProductRecommendationEquals(
        expectedProductRecommendation: Success<RecommendationWidget>
    ) {
        verifyGetOfficialStoreProductRecommendationUseCaseCalled()

        viewModel.officialStoreProductRecommendationResult
            .assertSuccess(expectedProductRecommendation)
    }

    protected fun verifyOfficialStoreBannersError(expectedError: Fail) {
        verifyGetOfficialStoreBannersUseCaseCalled()

        viewModel.officialStoreBannersResult
            .assertError(expectedError)
    }

    protected fun verifyOfficialStoreBenefitsError(expectedError: Fail) {
        verifyGetOfficialStoreBannersUseCaseCalled()

        viewModel.officialStoreBenefitsResult
            .assertError(expectedError)
    }

    protected fun verifyOfficialStoreFeaturedShopError(expectedError: Fail) {
        verifyGetOfficialStoreFeaturedShopUseCaseCalled()

        viewModel.officialStoreFeaturedShopResult
            .assertError(expectedError)
    }

    protected fun verifyOfficialStoreDynamicChannelError(expectedError: Fail) {
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
    // endregion
    
    // region private methods
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
}