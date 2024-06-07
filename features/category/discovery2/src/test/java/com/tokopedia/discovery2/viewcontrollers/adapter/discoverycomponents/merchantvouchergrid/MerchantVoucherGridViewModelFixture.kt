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
import com.tokopedia.discovery2.usecase.MerchantVoucherUseCase
import com.tokopedia.discovery2.usecase.MerchantVoucherUseCase.Companion.VOUCHER_PER_PAGE
import com.tokopedia.filter.newdynamicfilter.controller.FilterController
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
open class MerchantVoucherGridViewModelFixture {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val useCase: MerchantVoucherUseCase = mockk(relaxed = true)
    private val component: ComponentsItem = mockk(relaxed = true)

    private val application: Application = mockk()
    private val position: Int = 99
    private val componentId = "12324432"
    private val componentPageEndPoint = "discopagev2-mvc-grid-infinite-test"

    protected lateinit var viewModel: MerchantVoucherGridViewModel
    protected lateinit var searchParameter: SearchParameter
    protected lateinit var filterController: FilterController

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

    @Test
    fun testVariables() {
        viewModel.component
            .verifyEquals(component)
        viewModel.position
            .verifyEquals(position)
    }

    private fun stubUrlParser() {
        every {
            anyConstructed<URLParser>().paramKeyValueMapDecoded
        } returns hashMapOf()
    }

    protected fun stubLoadFirstPage(
        hasLoaded: Boolean
    ) {
        coEvery {
            useCase.loadFirstPageComponents(
                componentId = componentId,
                pageEndPoint = componentPageEndPoint
            )
        } returns hasLoaded
    }

    protected fun stubLoadFirstPage(
        throwable: Throwable
    ) {
        coEvery {
            useCase.loadFirstPageComponents(
                componentId = componentId,
                pageEndPoint = componentPageEndPoint
            )
        } throws throwable
    }

    protected fun stubLoadMore(
        hasLoaded: Boolean
    ) {
        coEvery {
            useCase.getCarouselPaginatedData(
                componentId = componentId,
                pageEndPoint = componentPageEndPoint
            )
        } returns hasLoaded
    }

    protected fun stubLoadMore(
        throwable: Throwable
    ) {
        coEvery {
            useCase.getCarouselPaginatedData(
                componentId = componentId,
                pageEndPoint = componentPageEndPoint
            )
        } throws throwable
    }

    protected fun stubNextPageAvailable(
        hasNextPage: Boolean
    ) {
        every {
            Utils.nextPageAvailable(component, VOUCHER_PER_PAGE)
        } returns hasNextPage
    }

    protected fun stubComponent(
        componentAdditionalInfo: ComponentAdditionalInfo?,
        componentItems: List<ComponentsItem>?,
        hasNextPage: Boolean = false
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

        every {
            component.nextPageKey
        } returns if (hasNextPage) "p" else {
            null
        }
    }

    protected fun LiveData<Result<ArrayList<ComponentsItem>>>.verifySuccessEquals(
        expected: ArrayList<ComponentsItem>?
    ) {
        val expectedResult = expected?.map { component ->
            component.copy(
                searchParameter = searchParameter,
                filterController = filterController
            )
        }
        val actualResult = (value as? Success<ArrayList<ComponentsItem>>)?.data?.map { component ->
            component.copy(
                searchParameter = searchParameter,
                filterController = filterController
            )
        }
        actualResult
            .verifyEquals(expectedResult)
    }

    protected fun <T : Any> LiveData<Result<T>>.verifyFailEquals(
        message: String
    ) {
        val actualResult = (value as? Fail)?.throwable?.message
        actualResult
            .verifyEquals(message)
    }
}
