package com.tokopedia.tkpd.flashsale.presentation.chooseproduct

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import com.tokopedia.campaign.entity.ChooseProductItem
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.tkpd.flashsale.domain.entity.CriteriaCheckingResult
import com.tokopedia.tkpd.flashsale.domain.entity.CriteriaSelection
import com.tokopedia.tkpd.flashsale.domain.entity.FlashSale
import com.tokopedia.tkpd.flashsale.domain.entity.ProductReserveResult
import com.tokopedia.tkpd.flashsale.domain.entity.ProductToReserve
import com.tokopedia.tkpd.flashsale.domain.entity.enums.FlashSaleListPageTab
import com.tokopedia.tkpd.flashsale.domain.entity.enums.FlashSaleStatus
import com.tokopedia.tkpd.flashsale.domain.usecase.DoFlashSaleProductReserveUseCase
import com.tokopedia.tkpd.flashsale.domain.usecase.GetFlashSaleDetailForSellerUseCase
import com.tokopedia.tkpd.flashsale.domain.usecase.GetFlashSaleProductCriteriaCheckingUseCase
import com.tokopedia.tkpd.flashsale.domain.usecase.GetFlashSaleProductListToReserveUseCase
import com.tokopedia.tkpd.flashsale.domain.usecase.GetFlashSaleProductPerCriteriaUseCase
import com.tokopedia.tkpd.flashsale.presentation.chooseproduct.constant.ChooseProductConstant.FILTER_PRODUCT_CRITERIA_PASSED
import com.tokopedia.tkpd.flashsale.presentation.chooseproduct.constant.ChooseProductConstant.MAX_PER_PAGE
import com.tokopedia.tkpd.flashsale.presentation.chooseproduct.mapper.ChooseProductUiMapper
import com.tokopedia.tkpd.flashsale.presentation.chooseproduct.viewmodel.ChooseProductViewModel
import com.tokopedia.tkpd.flashsale.util.tracker.AddChooseProductTracker
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.unit.test.ext.getOrAwaitValue
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.MockKAnnotations
import io.mockk.MockKMatcherScope
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.unmockkAll
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.*

open class ChooseProductViewModelTestFixture {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @RelaxedMockK
    internal lateinit var getFlashSaleProductListToReserveUseCase: GetFlashSaleProductListToReserveUseCase
    @RelaxedMockK
    internal lateinit var getFlashSaleProductPerCriteriaUseCase: GetFlashSaleProductPerCriteriaUseCase
    @RelaxedMockK
    internal lateinit var doFlashSaleProductReserveUseCase: DoFlashSaleProductReserveUseCase
    @RelaxedMockK
    internal lateinit var getFlashSaleProductCriteriaCheckingUseCase: GetFlashSaleProductCriteriaCheckingUseCase
    @RelaxedMockK
    internal lateinit var getFlashSaleDetailForSellerUseCase: GetFlashSaleDetailForSellerUseCase
    @RelaxedMockK
    internal lateinit var userSession: UserSessionInterface
    @RelaxedMockK
    internal lateinit var tracker: AddChooseProductTracker

    internal lateinit var viewModel: ChooseProductViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        viewModel = ChooseProductViewModel (
            CoroutineTestDispatchersProvider,
            getFlashSaleProductListToReserveUseCase,
            getFlashSaleProductPerCriteriaUseCase,
            doFlashSaleProductReserveUseCase,
            getFlashSaleProductCriteriaCheckingUseCase,
            getFlashSaleDetailForSellerUseCase,
            userSession,
            tracker
        )
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    internal fun testErrorResponse(whenCondition: () -> Unit) {
        // given
        coEvery {
            getFlashSaleProductListToReserveUseCase.execute(any())
            getFlashSaleProductPerCriteriaUseCase.execute(any())
            doFlashSaleProductReserveUseCase.execute(any())
            getFlashSaleProductCriteriaCheckingUseCase.execute(any(), any(), 0)
            getFlashSaleDetailForSellerUseCase.execute(any())
        } throws MessageErrorException("this is an error")

        // when
        whenCondition.invoke()

        // then
        val throwable = viewModel.error.getOrAwaitValue()
        assert(throwable.message?.isNotEmpty().orFalse())
    }

    internal fun testGetProduct() {
        // given
        viewModel.filterCriteria = FILTER_PRODUCT_CRITERIA_PASSED
        viewModel.filterCategory = emptyList()
        viewModel.campaignId = 123
        viewModel.tabName = "ongoing"
        coEvery {
            getFlashSaleProductListToReserveUseCase.execute(any())
        } returns generateProductToReserve(MAX_PER_PAGE)

        // when
        viewModel.getProductList(0, 0, "")

        // then
        assert(viewModel.productList.getOrAwaitValue().size == MAX_PER_PAGE)
        assert(viewModel.filterCriteria == FILTER_PRODUCT_CRITERIA_PASSED)
        assert(viewModel.filterCategory.isEmpty())
        assert(viewModel.campaignId == 123L)
        assert(viewModel.tabName == "ongoing")
    }

    internal fun testGetProductEmpty() {
        // given
        coEvery {
            getFlashSaleProductListToReserveUseCase.execute(any())
        } returns generateProductToReserve(0)

        // when
        viewModel.getProductList(0, 0, "")

        // then
        assert(viewModel.productList.getOrAwaitValue().isEmpty())
    }

    internal fun generateProductToReserve(count: Int): ProductToReserve {
        return ProductToReserve(
            emptyList(),
            0,
            List(count) { ChooseProductItem(productId = it.toString()) }
        )
    }

    internal fun generateCriteriaSelection(count: Int): List<CriteriaSelection> {
        return List(count) {
            CriteriaSelection(criteriaId = it.toLong(), selectionCountMax = count * 10)
        }
    }

    internal fun generateMockFlashSaleData(): FlashSale {
        return FlashSale(
            campaignId = 0,
            cancellationReason = "",
            coverImage = "",
            description = "",
            endDateUnix = Date(),
            maxProductSubmission = 0,
            name = "",
            hasEligibleProduct = true,
            productMeta = FlashSale.ProductMeta(
                acceptedProduct = 0,
                rejectedProduct = 0,
                totalProduct = 0,
                totalProductStock = 0,
                totalStockSold = 0,
                transferredProduct = 0,
                totalSoldValue = 0.toDouble(),
            ),
            remainingQuota = 0,
            reviewEndDateUnix = Date(),
            reviewStartDateUnix = Date(),
            slug = "",
            startDateUnix = Date(),
            statusId = 0,
            statusText = "",
            submissionEndDateUnix = Date(),
            submissionStartDateUnix = Date(),
            useMultiLocation = false,
            formattedDate = FlashSale.FormattedDate("", ""),
            status = FlashSaleStatus.DEFAULT,
            productCriteria = emptyList(),
            tabName = FlashSaleListPageTab.UPCOMING
        )
    }
}
