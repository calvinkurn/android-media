package com.tokopedia.shop.showcase.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.shop.common.graphql.data.shopetalase.ShopEtalaseModel
import com.tokopedia.shop.common.graphql.domain.usecase.shopetalase.GetShopEtalaseByShopUseCase
import com.tokopedia.shop.showcase.domain.model.GetFeaturedShowcase
import com.tokopedia.shop.showcase.domain.model.ShopFeaturedShowcase
import com.tokopedia.shop.showcase.domain.usecase.GetFeaturedShowcaseUseCase
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ShopPageShowcaseViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @RelaxedMockK
    lateinit var userSessionInterface: UserSessionInterface

    @RelaxedMockK
    lateinit var getShopEtalaseByShopUseCase: GetShopEtalaseByShopUseCase

    @RelaxedMockK
    lateinit var getFeaturedShowcaseUseCase: GetFeaturedShowcaseUseCase

    private val testCoroutineDispatcherProvider by lazy {
        CoroutineTestDispatchersProvider
    }

    private var viewModel: ShopPageShowcaseViewModel? = null

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = ShopPageShowcaseViewModel(
                userSessionInterface,
                testCoroutineDispatcherProvider,
                getShopEtalaseByShopUseCase,
                getFeaturedShowcaseUseCase
        )
    }

    @Test
    fun `check whether showcasesBuyerUiModel post success value`() {
        val mockShopId = "123"
        coEvery {
            getShopEtalaseByShopUseCase.createObservable(any()).toBlocking().first()
        } returns arrayListOf(ShopEtalaseModel())
        coEvery {
            getFeaturedShowcaseUseCase.executeOnBackground()
        } returns GetFeaturedShowcase(result = listOf(ShopFeaturedShowcase()))
        viewModel?.getShowcasesInitialData(mockShopId)
        assert(viewModel?.showcasesBuyerUiModel?.value is Success)
    }

    @Test
    fun `check whether isFeaturedShowcaseError value is true if getFeaturedShowcaseListCall error`() {
        val mockShopId = "123"
        coEvery {
            getShopEtalaseByShopUseCase.createObservable(any()).toBlocking().first()
        } returns arrayListOf(ShopEtalaseModel())
        coEvery {
            getFeaturedShowcaseUseCase.executeOnBackground()
        } throws Exception()
        viewModel?.getShowcasesInitialData(mockShopId)
        assert((viewModel?.showcasesBuyerUiModel?.value as? Success)?.data?.isFeaturedShowcaseError == true)
    }

    @Test
    fun `check whether isAllShowcaseError value is true if getAllShowcaseListCall error`() {
        val mockShopId = "123"
        coEvery {
            getShopEtalaseByShopUseCase.createObservable(any()).toBlocking().first()
        } throws Exception()
        coEvery {
            getFeaturedShowcaseUseCase.executeOnBackground()
        } returns GetFeaturedShowcase(result = listOf(ShopFeaturedShowcase()))
        viewModel?.getShowcasesInitialData(mockShopId)
        assert((viewModel?.showcasesBuyerUiModel?.value as? Success)?.data?.isAllShowcaseError == true)
    }

    @Test
    fun `check whether featuredShowcaseList post success value`() {
        val mockShopId = "123"
        coEvery {
            getFeaturedShowcaseUseCase.executeOnBackground()
        } returns GetFeaturedShowcase(result = listOf(ShopFeaturedShowcase()))
        viewModel?.getFeaturedShowcaseList(mockShopId)
        assert(viewModel?.featuredShowcaseList?.value is Success)
    }

    @Test
    fun `check whether featuredShowcaseList post fail value`() {
        val mockShopId = "123"
        coEvery {
            getFeaturedShowcaseUseCase.executeOnBackground()
        } throws Exception()
        viewModel?.getFeaturedShowcaseList(mockShopId)
        assert(viewModel?.featuredShowcaseList?.value is Fail)
    }

    @Test
    fun `check whether showcaseList post success value`() {
        val mockShopId = "123"
        coEvery {
            getShopEtalaseByShopUseCase.createObservable(any()).toBlocking().first()
        } returns arrayListOf(ShopEtalaseModel())
        viewModel?.getShowcaseList(mockShopId)
        assert(viewModel?.showcaseList?.value is Success)
    }

    @Test
    fun `check whether showcaseList post fail value`() {
        val mockShopId = "123"
        coEvery {
            getShopEtalaseByShopUseCase.createObservable(any()).toBlocking().first()
        } throws Exception()
        viewModel?.getShowcaseList(mockShopId)
        assert(viewModel?.showcaseList?.value is Fail)
    }

    @Test
    fun `check whether isMyShop return true if shopId match`() {
        val mockShopId = "123"
        every {
            userSessionInterface.shopId
        } returns mockShopId
        assert(viewModel?.isMyShop(mockShopId) == true)
    }

    @Test
    fun `check whether userId value match with mocked data`() {
        val mockUserId = "123"
        every {
            userSessionInterface.userId
        } returns mockUserId
        assert(viewModel?.userId.orEmpty() == mockUserId)
    }

}