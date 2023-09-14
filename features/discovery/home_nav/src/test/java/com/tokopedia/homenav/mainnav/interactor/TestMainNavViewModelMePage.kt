package com.tokopedia.homenav.mainnav.interactor

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.homenav.base.datamodel.HomeNavMenuDataModel
import com.tokopedia.homenav.common.util.ClientMenuGenerator.Companion.ID_ALL_TRANSACTION
import com.tokopedia.homenav.common.util.ClientMenuGenerator.Companion.ID_HOME
import com.tokopedia.homenav.common.util.ClientMenuGenerator.Companion.ID_REVIEW
import com.tokopedia.homenav.mainnav.MainNavConst
import com.tokopedia.homenav.mainnav.domain.model.NavPaymentOrder
import com.tokopedia.homenav.mainnav.domain.model.NavProductOrder
import com.tokopedia.homenav.mainnav.domain.model.NavReviewModel
import com.tokopedia.homenav.mainnav.domain.usecases.GetPaymentOrdersNavUseCase
import com.tokopedia.homenav.mainnav.domain.usecases.GetReviewProductUseCase
import com.tokopedia.homenav.mainnav.domain.usecases.GetUohOrdersNavUseCase
import com.tokopedia.homenav.mainnav.view.datamodel.*
import com.tokopedia.homenav.mainnav.view.datamodel.review.ReviewListDataModel
import com.tokopedia.homenav.mainnav.view.datamodel.review.ShimmerReviewDataModel
import com.tokopedia.homenav.mainnav.view.presenter.MainNavViewModel
import com.tokopedia.searchbar.navigation_component.NavSource
import com.tokopedia.unit.test.rule.CoroutineTestRule
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class TestMainNavViewModelMePage {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val rule = CoroutineTestRule()

    private lateinit var viewModel: MainNavViewModel
    private val mockGetUohOrdersNavUseCase = mockk<GetUohOrdersNavUseCase>()
    private val mockGetPaymentOrdersNavUseCase = mockk<GetPaymentOrdersNavUseCase>()
    private val mockGetReviewProductUseCase = mockk<GetReviewProductUseCase>()

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        setIsMePageRollence(true)
    }

    @Test
    fun `given launched from home then still show separator after profile section`() {
        viewModel = createViewModel(userSession = getUserSession(true))
        viewModel.setPageSource(NavSource.HOME)

        Assert.assertNull(viewModel.mainNavLiveData.value?.dataList?.find {
            it is HomeNavMenuDataModel && it.id == ID_HOME
        })
        Assert.assertNull(viewModel.mainNavLiveData.value?.dataList?.find {
            it is SeparatorDataModel && it.sectionId == MainNavConst.Section.HOME
        })
        Assert.assertNull(viewModel.mainNavLiveData.value?.dataList?.find {
            it is SeparatorDataModel && it.sectionId == MainNavConst.Section.PROFILE
        })
    }

    @Test
    fun `given user not logged in then show no visual cards`() {
        viewModel = createViewModel(userSession = getUserSession(false))

        viewModel.setInitialState()
        viewModel.getMainNavData(true)

        Assert.assertNull(viewModel.mainNavLiveData.value?.dataList?.find { it is InitialShimmerTransactionRevampDataModel })
        Assert.assertNull(viewModel.mainNavLiveData.value?.dataList?.find { it is ShimmerReviewDataModel })
        Assert.assertNull(viewModel.mainNavLiveData.value?.dataList?.find { it is TransactionListItemDataModel })
        Assert.assertNull(viewModel.mainNavLiveData.value?.dataList?.find { it is ReviewListDataModel })
        Assert.assertNotNull(viewModel.mainNavLiveData.value?.dataList?.find {
            it is HomeNavMenuDataModel && it.id == ID_ALL_TRANSACTION && !it.showCta
        })
        Assert.assertNotNull(viewModel.mainNavLiveData.value?.dataList?.find {
            it is HomeNavMenuDataModel && it.id == ID_REVIEW && !it.showCta
        })
    }

    @Test
    fun `given user logged in then add shimmer placeholder for transaction and review`() {
        viewModel = createViewModel(userSession = getUserSession(true))
        viewModel.setInitialState()

        Assert.assertNotNull(viewModel.mainNavLiveData.value?.dataList?.find { it is InitialShimmerTransactionRevampDataModel })
        Assert.assertNotNull(viewModel.mainNavLiveData.value?.dataList?.find { it is ShimmerReviewDataModel })
        Assert.assertNotNull(viewModel.mainNavLiveData.value?.dataList?.find {
            it is HomeNavMenuDataModel && it.id == ID_ALL_TRANSACTION && it.showCta
        })
        Assert.assertNotNull(viewModel.mainNavLiveData.value?.dataList?.find {
            it is HomeNavMenuDataModel && it.id == ID_REVIEW && it.showCta
        })
    }

    // Transaction section
    @Test
    fun `given logged in user has ongoing payment then show payment cards and menu cta`() {
        coEvery { mockGetPaymentOrdersNavUseCase.executeOnBackground() } returns listOf(NavPaymentOrder())
        viewModel = createViewModel(
            userSession = getUserSession(true),
            getPaymentOrdersNavUseCase = mockGetPaymentOrdersNavUseCase
        )

        viewModel.setInitialState()
        viewModel.getMainNavData(true)

        val transactionModel = viewModel.mainNavLiveData.value?.dataList?.find { it is TransactionListItemDataModel } as TransactionListItemDataModel
        Assert.assertTrue(transactionModel.orderListModel.paymentList.isNotEmpty())
        Assert.assertNotNull(viewModel.mainNavLiveData.value?.dataList?.find {
            it is HomeNavMenuDataModel && it.id == ID_ALL_TRANSACTION && it.showCta
        })
    }

    @Test
    fun `given logged in user has ongoing orders then show order cards and menu cta`() {
        coEvery { mockGetUohOrdersNavUseCase.executeOnBackground() } returns listOf(NavProductOrder())
        viewModel = createViewModel(
            userSession = getUserSession(true),
            getUohOrdersNavUseCase = mockGetUohOrdersNavUseCase
        )

        viewModel.setInitialState()
        viewModel.getMainNavData(true)

        val transactionModel = viewModel.mainNavLiveData.value?.dataList?.find { it is TransactionListItemDataModel } as TransactionListItemDataModel
        Assert.assertTrue(transactionModel.orderListModel.orderList.isNotEmpty())
        Assert.assertNotNull(viewModel.mainNavLiveData.value?.dataList?.find {
            it is HomeNavMenuDataModel && it.id == ID_ALL_TRANSACTION && it.showCta
        })
    }

    @Test
    fun `given logged in user has more than 5 transactions then show maximum 5 items with payments being prioritized`() {
        val mockOrders = listOf(
            NavProductOrder(),
            NavProductOrder(),
            NavProductOrder(),
            NavProductOrder(),
        )
        val mockPayments = listOf(
            NavPaymentOrder(),
            NavPaymentOrder(),
            NavPaymentOrder(),
        )

        coEvery { mockGetUohOrdersNavUseCase.executeOnBackground() } returns mockOrders
        coEvery { mockGetPaymentOrdersNavUseCase.executeOnBackground() } returns mockPayments
        viewModel = createViewModel(
            userSession = getUserSession(true),
            getUohOrdersNavUseCase = mockGetUohOrdersNavUseCase,
            getPaymentOrdersNavUseCase = mockGetPaymentOrdersNavUseCase,
        )

        viewModel.setInitialState()
        viewModel.getMainNavData(true)

        val resultTransactionModel = viewModel.mainNavLiveData.value?.dataList?.find { it is TransactionListItemDataModel } as TransactionListItemDataModel
        val resultOrdersCount = resultTransactionModel.orderListModel.orderList.size
        val resultPaymentsCount = resultTransactionModel.orderListModel.paymentList.size

        Assert.assertTrue(resultOrdersCount + resultPaymentsCount == 5)
        Assert.assertTrue(resultPaymentsCount == 3)
        Assert.assertTrue(resultOrdersCount == 2)
    }

    @Test
    fun `given logged in user has no ongoing orders and payments then remove visual cards and menu cta`() {
        coEvery { mockGetUohOrdersNavUseCase.executeOnBackground() } returns listOf()
        coEvery { mockGetPaymentOrdersNavUseCase.executeOnBackground() } returns listOf()
        viewModel = createViewModel(
            userSession = getUserSession(true),
            getUohOrdersNavUseCase = mockGetUohOrdersNavUseCase,
            getPaymentOrdersNavUseCase = mockGetPaymentOrdersNavUseCase,
        )

        viewModel.setInitialState()
        viewModel.getMainNavData(true)

        Assert.assertNull(viewModel.mainNavLiveData.value?.dataList?.find { it is InitialShimmerTransactionRevampDataModel })
        Assert.assertNull(viewModel.mainNavLiveData.value?.dataList?.find { it is TransactionListItemDataModel })
        Assert.assertNotNull(viewModel.mainNavLiveData.value?.dataList?.find {
            it is HomeNavMenuDataModel && it.id == ID_ALL_TRANSACTION && !it.showCta
        })
    }

    @Test
    fun `given logged in user gets error when fetching transaction then remove transaction cards and menu cta`() {
        coEvery { mockGetUohOrdersNavUseCase.executeOnBackground() } throws Exception()

        viewModel = createViewModel(
            userSession = getUserSession(true),
            getUohOrdersNavUseCase = mockGetUohOrdersNavUseCase,
        )

        viewModel.setInitialState()
        viewModel.getMainNavData(true)

        Assert.assertNull(viewModel.mainNavLiveData.value?.dataList?.find { it is InitialShimmerTransactionRevampDataModel })
        Assert.assertNull(viewModel.mainNavLiveData.value?.dataList?.find { it is TransactionListItemDataModel })
        Assert.assertNotNull(viewModel.mainNavLiveData.value?.dataList?.find {
            it is HomeNavMenuDataModel && it.id == ID_ALL_TRANSACTION && !it.showCta
        })
    }

    // Review section
    @Test
    fun `given logged in user has ongoing review then show review cards and menu cta`() {
        coEvery { mockGetReviewProductUseCase.executeOnBackground() } returns listOf(NavReviewModel(), NavReviewModel())

        viewModel = createViewModel(
            userSession = getUserSession(true),
            getReviewProductUseCase = mockGetReviewProductUseCase
        )

        viewModel.setInitialState()
        viewModel.getMainNavData(true)

        Assert.assertNull(viewModel.mainNavLiveData.value?.dataList?.find { it is ShimmerReviewDataModel })
        Assert.assertNotNull(viewModel.mainNavLiveData.value?.dataList?.find { it is ReviewListDataModel })
        Assert.assertNotNull(viewModel.mainNavLiveData.value?.dataList?.find {
            it is HomeNavMenuDataModel && it.id == ID_REVIEW && it.showCta
        })
    }

    @Test
    fun `given logged in user has no review then remove review cards`() {
        coEvery { mockGetReviewProductUseCase.executeOnBackground() } returns listOf()

        viewModel = createViewModel(
            userSession = getUserSession(true),
            getReviewProductUseCase = mockGetReviewProductUseCase
        )

        viewModel.setInitialState()
        viewModel.getMainNavData(true)
        Assert.assertNull(viewModel.mainNavLiveData.value?.dataList?.find { it is ShimmerReviewDataModel })
        Assert.assertNull(viewModel.mainNavLiveData.value?.dataList?.find { it is ReviewListDataModel })
        Assert.assertNotNull(viewModel.mainNavLiveData.value?.dataList?.find {
            it is HomeNavMenuDataModel && it.id == ID_REVIEW && !it.showCta
        })
    }

    @Test
    fun `given logged in user gets error when fetching review then remove review cards`() {
        coEvery { mockGetReviewProductUseCase.executeOnBackground() } throws Exception()

        viewModel = createViewModel(
            userSession = getUserSession(true),
            getReviewProductUseCase = mockGetReviewProductUseCase
        )

        viewModel.setInitialState()
        viewModel.getMainNavData(true)
        Assert.assertNull(viewModel.mainNavLiveData.value?.dataList?.find { it is ShimmerReviewDataModel })
        Assert.assertNull(viewModel.mainNavLiveData.value?.dataList?.find { it is ReviewListDataModel })
        Assert.assertNotNull(viewModel.mainNavLiveData.value?.dataList?.find {
            it is HomeNavMenuDataModel && it.id == ID_REVIEW && !it.showCta
        })
    }
}
