package com.tokopedia.similarsearch.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.similarsearch.SimilarSearchViewModel
import com.tokopedia.similarsearch.getsimilarproducts.model.SimilarProductModel
import com.tokopedia.similarsearch.testutils.stubExecute
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.usecase.coroutines.UseCase
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlistcommon.domain.AddToWishlistV2UseCase
import com.tokopedia.wishlistcommon.domain.DeleteWishlistV2UseCase
import io.mockk.every
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import com.tokopedia.usecase.UseCase as rxUseCase

internal abstract class SimilarSearchTestFixtures {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    val getSimilarProductsUseCase = mockk<UseCase<SimilarProductModel>>(relaxed = true)
    val addToWishlistV2UseCase = mockk<AddToWishlistV2UseCase>(relaxed = true)
    val deleteWishlistV2UseCase = mockk<DeleteWishlistV2UseCase>(relaxed = true)
    val userSession = mockk<UserSessionInterface>(relaxed = true)
    val addToCartUseCase = mockk<rxUseCase<AddToCartDataModel>>(relaxed = true)
    lateinit var similarSearchViewModel: SimilarSearchViewModel

    @Before
    open fun setUp() {
        similarSearchViewModel = createSimilarSearchViewModel()
    }

    open fun createSimilarSearchViewModel(): SimilarSearchViewModel =
        SimilarSearchViewModel(
            CoroutineTestDispatchersProvider,
            getSimilarSearchQuery(),
            getSimilarProductsUseCase,
            addToWishlistV2UseCase,
            deleteWishlistV2UseCase,
            addToCartUseCase,
            userSession
        )

    fun getSimilarSearchQuery() = "samsung"

    fun `Given get similar product will be successful`(
        similarProductModel: SimilarProductModel
    ) {
        getSimilarProductsUseCase.stubExecute().returns(similarProductModel)
    }

    fun `Given get similar product will fail with exception`(exception: Throwable) {
        getSimilarProductsUseCase.stubExecute().throws(exception)
    }

    fun `Given view is created`() {
        similarSearchViewModel.onViewCreated()
    }

    fun `Given view already created and has similar search data`(
        similarProductModelCommon: SimilarProductModel
    ) {
        `Given get similar product will be successful`(similarProductModelCommon)
        `Given view is created`()
    }

    fun `Given user is not logged in`() {
        every { userSession.isLoggedIn }.returns(false)
    }

    fun `Given user is logged in`() {
        every { userSession.isLoggedIn }.returns(true)
    }

    fun `Given user id`(userId: String) {
        every { userSession.userId } returns (userId)
    }

    fun `Given user is logged in with user id`(userId: String) {
        `Given user is logged in`()
        `Given user id`(userId)
    }

    fun `When handle view is created`() {
        similarSearchViewModel.onViewCreated()
    }
}