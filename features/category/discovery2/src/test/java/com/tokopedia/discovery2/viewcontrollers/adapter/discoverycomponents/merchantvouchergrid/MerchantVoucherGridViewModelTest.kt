package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.merchantvouchergrid

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import com.tokopedia.discovery.common.model.SearchParameter
import com.tokopedia.discovery.common.utils.URLParser
import com.tokopedia.discovery2.Utils
import com.tokopedia.discovery2.common.TestUtils.verifyEquals
import com.tokopedia.discovery2.data.ComponentAdditionalInfo
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.Redirection
import com.tokopedia.discovery2.data.TotalProductData
import com.tokopedia.discovery2.usecase.MerchantVoucherUseCase
import com.tokopedia.discovery2.usecase.MerchantVoucherUseCase.Companion.VOUCHER_PER_PAGE
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.merchantvouchergrid.MerchantVoucherGridComponentExtension.addShimmer
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.merchantvouchergrid.MerchantVoucherGridComponentExtension.addVoucherList
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.merchantvouchergrid.MerchantVoucherGridViewModel.Companion.ERROR_MESSAGE_EMPTY_DATA
import com.tokopedia.filter.newdynamicfilter.controller.FilterController
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.unit.test.ext.verifyValueEquals
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.mockkObject
import io.mockk.spyk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
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
    private val componentId = "12324432"
    private val componentPageEndPoint = "discopagev2-mvc-grid-infinite-test"

    private lateinit var viewModel: MerchantVoucherGridViewModel
    private lateinit var searchParameter: SearchParameter
    private lateinit var filterController: FilterController

    @Before
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        MockKAnnotations.init(this)
        mockkObject(Utils.Companion)
        mockkConstructor(URLParser::class)
        stubUrlParser()

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
    }

    private fun stubUrlParser() {
        every {
            anyConstructed<URLParser>().paramKeyValueMapDecoded
        } returns hashMapOf()
    }

    @Test
    fun `test 1`() {
        viewModel.useCase = null

        // create expected result
        val expected = arrayListOf<ComponentsItem>()

        expected.addShimmer()

        // load first page
        viewModel.loadFirstPageCoupon()

        // compare to the expected result
        viewModel.couponList
            .verifySuccessEquals(expected)
    }

    @Test
    fun `test 2 voucher list empty`() {
        // stub necessary data
        stubComponent(
            componentAdditionalInfo = ComponentAdditionalInfo(
                nextPage = String.EMPTY,
                enabled = false,
                totalProductData = TotalProductData()
            ),
            componentItems = emptyList()
        )

        stubLoadFirstPage(
            hasLoaded = true
        )

        // load first page
        viewModel.loadFirstPageCoupon()

        // compare to the expected result
        viewModel.couponList
            .verifyFailEquals(ERROR_MESSAGE_EMPTY_DATA)
    }

    @Test
    fun `test 3 voucher list empty`() {
        // stub necessary data
        stubComponent(
            componentAdditionalInfo = ComponentAdditionalInfo(
                nextPage = String.EMPTY,
                enabled = false,
                totalProductData = TotalProductData()
            ),
            componentItems = null
        )

        stubLoadFirstPage(
            hasLoaded = true
        )

        // load first page
        viewModel.loadFirstPageCoupon()

        // compare to the expected result
        viewModel.couponList
            .verifyFailEquals(ERROR_MESSAGE_EMPTY_DATA)
    }

    @Test
    fun `test 3 voucher list not empty doesn't have next page & redirection is null`()  {
        // stub necessary data
        val componentItems = listOf(
            ComponentsItem(
                searchParameter = searchParameter,
                filterController = filterController,
            ),
            ComponentsItem(
                searchParameter = searchParameter,
                filterController = filterController,
            ),
            ComponentsItem(
                searchParameter = searchParameter,
                filterController = filterController,
            )
        )

        stubComponent(
            componentAdditionalInfo = ComponentAdditionalInfo(
                nextPage = String.EMPTY,
                enabled = false,
                totalProductData = TotalProductData(),
                redirection = null
            ),
            componentItems = componentItems
        )

        stubNextPageAvailable(
            hasNextPage = false
        )

        stubLoadFirstPage(
            hasLoaded = true
        )

        // create expected result
        val expected = arrayListOf<ComponentsItem>()

        expected.addVoucherList(componentItems)

        // load first page
        viewModel.loadFirstPageCoupon()

        // compare to the expected result
        viewModel.noMorePages
            .verifyValueEquals(Unit)
        viewModel.seeMore
            .verifyValueEquals(null)
        viewModel.couponList
            .verifySuccessEquals(expected)
    }

    @Test
    fun `test 3-2 voucher list not empty doesn't have next page & cta is empty`()  {
        // stub necessary data
        val redirection = Redirection(
            ctaText = String.EMPTY
        )
        val componentItems = listOf(
            ComponentsItem(
                searchParameter = searchParameter,
                filterController = filterController,
            ),
            ComponentsItem(
                searchParameter = searchParameter,
                filterController = filterController,
            ),
            ComponentsItem(
                searchParameter = searchParameter,
                filterController = filterController,
            )
        )

        stubComponent(
            componentAdditionalInfo = ComponentAdditionalInfo(
                nextPage = String.EMPTY,
                enabled = false,
                totalProductData = TotalProductData(),
                redirection = redirection
            ),
            componentItems = componentItems
        )

        stubNextPageAvailable(
            hasNextPage = false
        )

        stubLoadFirstPage(
            hasLoaded = true
        )

        // create expected result
        val expected = arrayListOf<ComponentsItem>()

        expected.addVoucherList(componentItems)

        // load first page
        viewModel.loadFirstPageCoupon()

        // compare to the expected result
        viewModel.noMorePages
            .verifyValueEquals(Unit)
        viewModel.seeMore
            .verifyValueEquals(redirection)
        viewModel.couponList
            .verifySuccessEquals(expected)
    }

    @Test
    fun `test 3-3 voucher list not empty doesn't have next page & cta is null`()  {
        // stub necessary data
        val redirection = Redirection(
            ctaText = null
        )
        val componentItems = listOf(
            ComponentsItem(
                searchParameter = searchParameter,
                filterController = filterController,
            ),
            ComponentsItem(
                searchParameter = searchParameter,
                filterController = filterController,
            ),
            ComponentsItem(
                searchParameter = searchParameter,
                filterController = filterController,
            )
        )

        stubComponent(
            componentAdditionalInfo = ComponentAdditionalInfo(
                nextPage = String.EMPTY,
                enabled = false,
                totalProductData = TotalProductData(),
                redirection = redirection
            ),
            componentItems = componentItems
        )

        stubNextPageAvailable(
            hasNextPage = false
        )

        stubLoadFirstPage(
            hasLoaded = true
        )

        // create expected result
        val expected = arrayListOf<ComponentsItem>()

        expected.addVoucherList(componentItems)

        // load first page
        viewModel.loadFirstPageCoupon()

        // compare to the expected result
        viewModel.noMorePages
            .verifyValueEquals(Unit)
        viewModel.seeMore
            .verifyValueEquals(redirection)
        viewModel.couponList
            .verifySuccessEquals(expected)
    }

    @Test
    fun `test 4 voucher list not empty has page & redirection is null`()  {
        // stub necessary data
        val componentItems = listOf(
            ComponentsItem(
                searchParameter = searchParameter,
                filterController = filterController,
            ),
            ComponentsItem(
                searchParameter = searchParameter,
                filterController = filterController,
            ),
            ComponentsItem(
                searchParameter = searchParameter,
                filterController = filterController,
            )
        )

        stubComponent(
            componentAdditionalInfo = ComponentAdditionalInfo(
                nextPage = "p",
                enabled = false,
                totalProductData = TotalProductData(),
                redirection = null
            ),
            componentItems = componentItems
        )

        stubNextPageAvailable(
            hasNextPage = true
        )

        stubLoadFirstPage(
            hasLoaded = true
        )

        // create expected result
        val expected = arrayListOf<ComponentsItem>()

        expected.addVoucherList(componentItems)
        expected.addShimmer()

        // load first page
        viewModel.loadFirstPageCoupon()

        // compare to the expected result
        viewModel.couponList
            .verifySuccessEquals(expected)
    }

    @Test
    fun `test 4-1 voucher list not empty has page & cta text is null`()  {
        // stub necessary data
        val redirection = Redirection(
            ctaText = null
        )
        val componentItems = listOf(
            ComponentsItem(
                searchParameter = searchParameter,
                filterController = filterController,
            ),
            ComponentsItem(
                searchParameter = searchParameter,
                filterController = filterController,
            ),
            ComponentsItem(
                searchParameter = searchParameter,
                filterController = filterController,
            )
        )

        stubComponent(
            componentAdditionalInfo = ComponentAdditionalInfo(
                nextPage = "p",
                enabled = false,
                totalProductData = TotalProductData(),
                redirection = redirection
            ),
            componentItems = componentItems
        )

        stubNextPageAvailable(
            hasNextPage = true
        )

        stubLoadFirstPage(
            hasLoaded = true
        )

        // create expected result
        val expected = arrayListOf<ComponentsItem>()

        expected.addVoucherList(componentItems)
        expected.addShimmer()

        // load first page
        viewModel.loadFirstPageCoupon()

        // compare to the expected result
        viewModel.couponList
            .verifySuccessEquals(expected)
    }

    @Test
    fun `test 4-1 voucher list not empty has page & cta text is empty`()  {
        // stub necessary data
        val redirection = Redirection(
            ctaText = String.EMPTY
        )
        val componentItems = listOf(
            ComponentsItem(
                searchParameter = searchParameter,
                filterController = filterController,
            ),
            ComponentsItem(
                searchParameter = searchParameter,
                filterController = filterController,
            ),
            ComponentsItem(
                searchParameter = searchParameter,
                filterController = filterController,
            )
        )

        stubComponent(
            componentAdditionalInfo = ComponentAdditionalInfo(
                nextPage = "p",
                enabled = false,
                totalProductData = TotalProductData(),
                redirection = redirection
            ),
            componentItems = componentItems
        )

        stubNextPageAvailable(
            hasNextPage = true
        )

        stubLoadFirstPage(
            hasLoaded = true
        )

        // create expected result
        val expected = arrayListOf<ComponentsItem>()

        expected.addVoucherList(componentItems)
        expected.addShimmer()

        // load first page
        viewModel.loadFirstPageCoupon()

        // compare to the expected result
        viewModel.couponList
            .verifySuccessEquals(expected)
    }

    @Test
    fun `test 4-3 voucher list not empty has page & has cta text`()  {
        // stub necessary data
        val redirection = Redirection(
            ctaText = "Lihat Semua Kupon"
        )
        val componentItems = listOf(
            ComponentsItem(
                searchParameter = searchParameter,
                filterController = filterController,
            ),
            ComponentsItem(
                searchParameter = searchParameter,
                filterController = filterController,
            ),
            ComponentsItem(
                searchParameter = searchParameter,
                filterController = filterController,
            )
        )

        stubComponent(
            componentAdditionalInfo = ComponentAdditionalInfo(
                nextPage = "p",
                enabled = false,
                totalProductData = TotalProductData(),
                redirection = redirection
            ),
            componentItems = componentItems
        )

        stubNextPageAvailable(
            hasNextPage = true
        )

        stubLoadFirstPage(
            hasLoaded = true
        )

        // create expected result
        val expected = arrayListOf<ComponentsItem>()

        expected.addVoucherList(componentItems)

        // load first page
        viewModel.loadFirstPageCoupon()

        // compare to the expected result
        viewModel.noMorePages
            .verifyValueEquals(Unit)
        viewModel.seeMore
            .verifyValueEquals(redirection)
        viewModel.couponList
            .verifySuccessEquals(expected)
    }

    @Test
    fun `test 3-4 voucher list not empty doesn't have next page & has cta text`()  {
        // stub necessary data
        val redirection = Redirection(
            ctaText = "Lihat Semua Kupon"
        )
        val componentItems = listOf(
            ComponentsItem(
                searchParameter = searchParameter,
                filterController = filterController,
            ),
            ComponentsItem(
                searchParameter = searchParameter,
                filterController = filterController,
            ),
            ComponentsItem(
                searchParameter = searchParameter,
                filterController = filterController,
            )
        )

        stubComponent(
            componentAdditionalInfo = ComponentAdditionalInfo(
                nextPage = String.EMPTY,
                enabled = false,
                totalProductData = TotalProductData(),
                redirection = redirection
            ),
            componentItems = componentItems
        )

        stubNextPageAvailable(
            hasNextPage = false
        )

        stubLoadFirstPage(
            hasLoaded = true
        )

        // create expected result
        val expected = arrayListOf<ComponentsItem>()

        expected.addVoucherList(componentItems)

        // load first page
        viewModel.loadFirstPageCoupon()

        // compare to the expected result
        viewModel.noMorePages
            .verifyValueEquals(Unit)
        viewModel.seeMore
            .verifyValueEquals(redirection)
        viewModel.couponList
            .verifySuccessEquals(expected)
    }

    private fun stubLoadFirstPage(
        hasLoaded: Boolean
    ) {
        coEvery {
            useCase.loadFirstPageComponents(componentId = componentId, pageEndPoint = componentPageEndPoint)
        } returns hasLoaded
    }

    private fun stubNextPageAvailable(
        hasNextPage: Boolean
    ) {
        every {
            Utils.nextPageAvailable(component, VOUCHER_PER_PAGE)
        } returns hasNextPage
    }

    private fun stubComponent(
        componentAdditionalInfo: ComponentAdditionalInfo,
        componentItems: List<ComponentsItem>?
    ) {
        every {
            component.id
        } returns componentId

        every {
            component.pageEndPoint
        } returns componentPageEndPoint

        every {
            component.getComponentAdditionalInfo()
        } returns componentAdditionalInfo

        every {
            component.getComponentsItem()
        } returns componentItems
    }

    private fun LiveData<Result<ArrayList<ComponentsItem>>>.verifySuccessEquals(
        expected: ArrayList<ComponentsItem>
    ) {
        val expectedResult = expected.map { component ->
            component.copy(
                searchParameter = searchParameter,
                filterController = filterController,
            )
        }
        val actualResult = (value as? Success<ArrayList<ComponentsItem>>)?.data?.map { component ->
            component.copy(
                searchParameter = searchParameter,
                filterController = filterController,
            )
        }
        actualResult
            .verifyEquals(expectedResult)
    }

    private fun LiveData<Result<ArrayList<ComponentsItem>>>.verifyFailEquals(
        message: String
    ) {
        val actualResult = (value as? Fail)?.throwable?.message
        actualResult
            .verifyEquals(message)
    }
}

