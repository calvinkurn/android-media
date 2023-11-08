package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.merchantvouchergrid

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.discovery.common.model.SearchParameter
import com.tokopedia.discovery.common.utils.URLParser
import com.tokopedia.discovery2.common.TestUtils.verifyEquals
import com.tokopedia.discovery2.data.ComponentAdditionalInfo
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.TotalProductData
import com.tokopedia.discovery2.usecase.MerchantVoucherUseCase
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.merchantvouchergrid.MerchantVoucherGridComponentExtension.addShimmer
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.merchantvouchergrid.MerchantVoucherGridComponentExtension.addVoucherList
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.merchantvouchergrid.MerchantVoucherGridViewModel.Companion.ERROR_MESSAGE_EMPTY_DATA
import com.tokopedia.filter.newdynamicfilter.controller.FilterController
import com.tokopedia.unit.test.ext.getOrAwaitValue
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.spyk
import io.mockk.unmockkConstructor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MerchantVoucherGridViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val useCase: MerchantVoucherUseCase = mockk(relaxed = true)
    private val component: ComponentsItem = mockk(relaxed = true)

    private val application: Application = mockk()
    private val position: Int = 99

    private lateinit var viewModel: MerchantVoucherGridViewModel
    private lateinit var searchParameter: SearchParameter
    private lateinit var filterController: FilterController

    @Before
    fun setUp() {
        Dispatchers.setMain(Dispatchers.Default)
        MockKAnnotations.init(this)
        mockkConstructor(URLParser::class)
        mockUrlParser()

        viewModel = spyk(
            MerchantVoucherGridViewModel(
                application = application,
                component = component,
                position = position
            )
        )

        viewModel.useCase = useCase
        searchParameter = SearchParameter()
        filterController = FilterController()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkConstructor(URLParser::class)
    }

    private fun mockUrlParser() {
        every {
            anyConstructed<URLParser>().paramKeyValueMapDecoded
        } returns hashMapOf()
    }

    @Test
    fun `test 1`() = runTest {
        viewModel.loadFirstPageCoupon()

        val expected = arrayListOf<ComponentsItem>()

        expected.addShimmer()

        val couponListActualResult = viewModel.couponList.getOrAwaitValue()

        couponListActualResult
            .verifySuccessEquals(expected)
    }

    @Test
    fun `test 2 voucher list empty`() = runTest {
        val componentId = (0..Integer.MAX_VALUE).random().toString()
        val pageEndpoint = "discopagev2-mvc-grid-infinite-test"

        every {
            component.id
        } returns componentId

        every {
            component.pageEndPoint
        } returns pageEndpoint

        every {
            component.getComponentsItem()
        } returns listOf()

        every {
            component.getComponentAdditionalInfo()
        } returns ComponentAdditionalInfo(nextPage = "", enabled = false, totalProductData = TotalProductData())

        coEvery {
            useCase.loadFirstPageComponents(componentId = componentId, pageEndPoint = pageEndpoint)
        } returns true

        viewModel.loadFirstPageCoupon()

        val couponListActualResult = viewModel.couponList.getOrAwaitValue()

        couponListActualResult
            .verifyFailEquals(ERROR_MESSAGE_EMPTY_DATA)
    }

    @Test
    fun `test 3 voucher list not empty`() = runTest  {
        val componentId = (0..Integer.MAX_VALUE).random().toString()
        val pageEndpoint = "discopagev2-mvc-grid-infinite-test"

        val componentList = listOf(ComponentsItem(
            searchParameter = searchParameter,
            filterController = filterController,
        ), ComponentsItem(
            searchParameter = searchParameter,
            filterController = filterController,
        ), ComponentsItem(
            searchParameter = searchParameter,
            filterController = filterController,
        )
        )

        every {
            component.id
        } returns componentId

        every {
            component.pageEndPoint
        } returns pageEndpoint

        every {
            component.getComponentAdditionalInfo()
        } returns ComponentAdditionalInfo(nextPage = "", enabled = false, totalProductData = TotalProductData())

        every {
            component.getComponentsItem()
        } returns componentList

        coEvery {
            useCase.loadFirstPageComponents(componentId = componentId, pageEndPoint = pageEndpoint)
        } returns true

        viewModel.loadFirstPageCoupon()

        val expected = arrayListOf<ComponentsItem>()

        expected.addVoucherList(componentList)

        val noMorePagesActualResult = viewModel.noMorePages.getOrAwaitValue()
        val seeMoreActualResult = viewModel.seeMore.getOrAwaitValue()
        val couponListActualResult = viewModel.couponList.getOrAwaitValue()

        noMorePagesActualResult
            .verifyEquals(Unit)
        seeMoreActualResult
            .verifyEquals(null)
        couponListActualResult
            .verifySuccessEquals(expected)
    }

    private fun Result<ArrayList<ComponentsItem>>.verifySuccessEquals(
        expected: ArrayList<ComponentsItem>
    ) {
        val expectedResult = expected.map { component ->
            component.copy(
                searchParameter = searchParameter,
                filterController = filterController,
            )
        }
        val actualResult = (this as? Success<ArrayList<ComponentsItem>>)?.data?.map { component ->
            component.copy(
                searchParameter = searchParameter,
                filterController = filterController,
            )
        }
        actualResult
            ?.verifyEquals(expectedResult)
    }

    private fun Result<ArrayList<ComponentsItem>>?.verifyFailEquals(
        message: String
    ) {
        val actualResult = (this as? Fail)?.throwable?.message
        actualResult
            ?.verifyEquals(message)
    }
}



