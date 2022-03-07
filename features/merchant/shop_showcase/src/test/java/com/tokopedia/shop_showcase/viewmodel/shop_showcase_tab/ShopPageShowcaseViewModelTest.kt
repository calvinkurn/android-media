package com.tokopedia.shop_showcase.viewmodel.shop_showcase_tab

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.shop.common.graphql.data.shopetalase.ShopEtalaseModel
import com.tokopedia.shop.common.graphql.domain.usecase.shopetalase.GetShopEtalaseByShopUseCase
import com.tokopedia.shop_showcase.shop_showcase_tab.domain.model.GetFeaturedShowcase
import com.tokopedia.shop_showcase.shop_showcase_tab.domain.model.ShopFeaturedShowcase
import com.tokopedia.shop_showcase.shop_showcase_tab.domain.usecase.GetFeaturedShowcaseUseCase
import com.tokopedia.shop_showcase.shop_showcase_tab.presentation.model.ShowcasesBuyerUiModel
import com.tokopedia.shop_showcase.shop_showcase_tab.presentation.viewmodel.ShopPageShowcaseViewModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
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
        viewModel =
            com.tokopedia.shop_showcase.shop_showcase_tab.presentation.viewmodel.ShopPageShowcaseViewModel(
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
        } returns GetFeaturedShowcase(
            result = listOf(ShopFeaturedShowcase())
        )
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
        } returns GetFeaturedShowcase(
            result = listOf(ShopFeaturedShowcase())
        )
        viewModel?.getShowcasesInitialData(mockShopId)
        assert((viewModel?.showcasesBuyerUiModel?.value as? Success)?.data?.isAllShowcaseError == true)
    }

    @Test
    fun `check getShowcasesInitialData showcasesBuyerUiModel value is Fail if exception is thrown`() {
        val mockShopId = "123"
        coEvery {
            getShopEtalaseByShopUseCase.createObservable(any()).toBlocking().first()
        } returns arrayListOf()
        coEvery {
            getFeaturedShowcaseUseCase.executeOnBackground()
        } returns GetFeaturedShowcase(
            result = listOf(ShopFeaturedShowcase())
        )
        val observer = mockk<Observer<Result<ShowcasesBuyerUiModel>>>(relaxed = true)
        viewModel?.showcasesBuyerUiModel?.observeForever(observer)
        every { observer.onChanged(any<Success<ShowcasesBuyerUiModel>>()) } throws Exception()
        viewModel?.getShowcasesInitialData(mockShopId)
        assert(viewModel?.showcasesBuyerUiModel?.value is Fail)
    }

    @Test
    fun `check whether featuredShowcaseList post success value`() {
        val mockShopId = "123"
        coEvery {
            getFeaturedShowcaseUseCase.executeOnBackground()
        } returns GetFeaturedShowcase(
            result = listOf(ShopFeaturedShowcase())
        )
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
        assert(viewModel?.
        isMyShop(mockShopId) == true)
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