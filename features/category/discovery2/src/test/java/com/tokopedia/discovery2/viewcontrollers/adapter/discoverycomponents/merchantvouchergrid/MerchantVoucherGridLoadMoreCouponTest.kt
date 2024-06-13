package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.merchantvouchergrid

import com.tokopedia.discovery2.common.TestUtils.mockPrivateField
import com.tokopedia.discovery2.data.ComponentAdditionalInfo
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.Redirection
import com.tokopedia.discovery2.data.TotalProductData
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.merchantvouchergrid.MerchantVoucherGridComponentExtension.addShimmer
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.merchantvouchergrid.MerchantVoucherGridComponentExtension.addVoucherList
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.merchantvouchergrid.MerchantVoucherGridViewModel.Companion.ERROR_MESSAGE_EMPTY_DATA
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.unit.test.ext.verifyValueEquals
import org.junit.Test

class MerchantVoucherGridLoadMoreCouponTest : MerchantVoucherGridViewModelFixture() {
    @Test
    fun `When load more but still in progress hitting another gql then should not execute more code`() {
        // stub necessary data
        viewModel
            .mockPrivateField("isLoading", true)

        // inject use case
        viewModel.useCase = null

        // load more
        viewModel.loadMore(true)

        // compare to the expected result
        viewModel.couponList
            .verifySuccessEquals(null)
    }

    @Test
    fun `When load more but scrolling not yet reached the bottom of the page then should not execute more code`() {
        // stub necessary data
        viewModel
            .mockPrivateField("isLoading", false)

        // inject use case
        viewModel.useCase = null

        // load more
        viewModel.loadMore(false)

        // compare to the expected result
        viewModel.couponList
            .verifySuccessEquals(null)
    }

    @Test
    fun `When use case is null then the result of loading more pages should be null`() {
        // stub necessary data
        viewModel
            .mockPrivateField("isLoading", false)

        // inject use case
        viewModel.useCase = null

        // load more
        viewModel.loadMore(true)

        // compare to the expected result
        viewModel.couponList
            .verifySuccessEquals(null)
    }

    @Test
    fun `When use case is false then the result of loading more pages should be null`() {
        // stub necessary data
        viewModel
            .mockPrivateField("isLoading", false)

        stubComponent(
            componentAdditionalInfo = ComponentAdditionalInfo(
                nextPage = "p",
                enabled = false,
                totalProductData = TotalProductData()
            ),
            componentItems = emptyList()
        )

        stubLoadMore(
            hasLoaded = false
        )

        // load more
        viewModel.loadMore(true)

        // compare to the expected result
        viewModel.couponList
            .verifySuccessEquals(null)
    }

    @Test
    fun `When use case throws an error then the result of loading more pages should be an error too`() {
        // stub necessary data
        viewModel
            .mockPrivateField("isLoading", false)

        stubComponent(
            componentAdditionalInfo = ComponentAdditionalInfo(
                nextPage = "p",
                enabled = false,
                totalProductData = TotalProductData()
            ),
            componentItems = emptyList()
        )

        val throwable = Throwable(ERROR_MESSAGE_EMPTY_DATA)
        stubLoadMore(
            throwable = throwable
        )

        // load more
        viewModel.loadMore(true)

        // compare to the expected result
        viewModel.couponList
            .verifyFailEquals(ERROR_MESSAGE_EMPTY_DATA)
    }

    @Test
    fun `When use case is true but voucher list is empty, then the result of loading more pages should be an error`() {
        // stub necessary data
        viewModel
            .mockPrivateField("isLoading", false)

        stubComponent(
            componentAdditionalInfo = ComponentAdditionalInfo(
                nextPage = "p",
                enabled = false,
                totalProductData = TotalProductData()
            ),
            componentItems = emptyList()
        )

        stubLoadMore(
            hasLoaded = true
        )

        // load more
        viewModel.loadMore(true)

        // compare to the expected result
        viewModel.couponList
            .verifyFailEquals(ERROR_MESSAGE_EMPTY_DATA)
    }

    @Test
    fun `When use case is true but voucher list is null, then the result of loading more pages should be an error`() {
        // stub necessary data
        viewModel
            .mockPrivateField("isLoading", false)

        stubComponent(
            componentAdditionalInfo = ComponentAdditionalInfo(
                nextPage = "p",
                enabled = false,
                totalProductData = TotalProductData()
            ),
            componentItems = null
        )

        stubLoadMore(
            hasLoaded = true
        )

        // load more
        viewModel.loadMore(true)

        // compare to the expected result
        viewModel.couponList
            .verifyFailEquals(ERROR_MESSAGE_EMPTY_DATA)
    }

    @Test
    fun `When use case is true and voucher list has 3 items with additional info that is component doesn't have next page && component additional info is null, so the result should get only voucher list`() {
        // stub necessary data
        viewModel
            .mockPrivateField("isLoading", false)

        val componentItems = listOf(
            ComponentsItem(
                searchParameter = searchParameter,
                filterController = filterController
            ),
            ComponentsItem(
                searchParameter = searchParameter,
                filterController = filterController
            ),
            ComponentsItem(
                searchParameter = searchParameter,
                filterController = filterController
            )
        )

        stubComponent(
            componentAdditionalInfo = null,
            componentItems = componentItems,
            hasNextPage = false
        )

        stubLoadMore(
            hasLoaded = true
        )

        // create expected result
        val expected = arrayListOf<ComponentsItem>()

        expected.addVoucherList(componentItems)

        // load more
        viewModel.loadMore(true)

        // compare to the expected result
        viewModel.noMorePages
            .verifyValueEquals(true)
        viewModel.couponList
            .verifySuccessEquals(expected)
    }

    @Test
    fun `When use case is true and voucher list has 3 items with additional info that component doesn't have next page && redirection is null, so the result should get only voucher list`() {
        // stub necessary data
        viewModel
            .mockPrivateField("isLoading", false)

        val componentItems = listOf(
            ComponentsItem(
                searchParameter = searchParameter,
                filterController = filterController
            ),
            ComponentsItem(
                searchParameter = searchParameter,
                filterController = filterController
            ),
            ComponentsItem(
                searchParameter = searchParameter,
                filterController = filterController
            )
        )

        stubComponent(
            componentAdditionalInfo = ComponentAdditionalInfo(
                nextPage = String.EMPTY,
                enabled = false,
                totalProductData = TotalProductData(),
                redirection = null
            ),
            componentItems = componentItems,
            hasNextPage = false
        )

        stubLoadMore(
            hasLoaded = true
        )

        // create expected result
        val expected = arrayListOf<ComponentsItem>()

        expected.addVoucherList(componentItems)

        // load more
        viewModel.loadMore(true)

        // compare to the expected result
        viewModel.noMorePages
            .verifyValueEquals(true)
        viewModel.couponList
            .verifySuccessEquals(expected)
    }

    @Test
    fun `When use case is true and voucher list has 3 items with additional info that component doesn't have next page && cta text is empty, so the result should get only voucher list`() {
        // stub necessary data
        viewModel
            .mockPrivateField("isLoading", false)

        val redirection = Redirection(
            ctaText = String.EMPTY
        )
        val componentItems = listOf(
            ComponentsItem(
                searchParameter = searchParameter,
                filterController = filterController
            ),
            ComponentsItem(
                searchParameter = searchParameter,
                filterController = filterController
            ),
            ComponentsItem(
                searchParameter = searchParameter,
                filterController = filterController
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

        stubLoadMore(
            hasLoaded = true
        )

        // create expected result
        val expected = arrayListOf<ComponentsItem>()

        expected.addVoucherList(componentItems)

        // load more
        viewModel.loadMore(true)

        // compare to the expected result
        viewModel.noMorePages
            .verifyValueEquals(true)
        viewModel.couponList
            .verifySuccessEquals(expected)
    }

    @Test
    fun `When use case is true and voucher list has 3 items with additional info that component doesn't have next page && cta text is null, so the result should get only voucher list`() {
        // stub necessary data
        viewModel
            .mockPrivateField("isLoading", false)

        val redirection = Redirection(
            ctaText = null
        )
        val componentItems = listOf(
            ComponentsItem(
                searchParameter = searchParameter,
                filterController = filterController
            ),
            ComponentsItem(
                searchParameter = searchParameter,
                filterController = filterController
            ),
            ComponentsItem(
                searchParameter = searchParameter,
                filterController = filterController
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

        stubLoadMore(
            hasLoaded = true
        )

        // create expected result
        val expected = arrayListOf<ComponentsItem>()

        expected.addVoucherList(componentItems)

        // load more
        viewModel.loadMore(true)

        // compare to the expected result
        viewModel.noMorePages
            .verifyValueEquals(true)
        viewModel.couponList
            .verifySuccessEquals(expected)
    }

    @Test
    fun `When use case is true and voucher list has 3 items with additional info that component has next page && cta text is not empty, so the result should get only voucher list`() {
        // stub necessary data
        viewModel
            .mockPrivateField("isLoading", false)

        val redirection = Redirection(
            ctaText = "Lihat Semua Kupon",
            applink = "tokopedia://discovery/mvc-next-page"
        )
        val componentItems = listOf(
            ComponentsItem(
                searchParameter = searchParameter,
                filterController = filterController
            ),
            ComponentsItem(
                searchParameter = searchParameter,
                filterController = filterController
            ),
            ComponentsItem(
                searchParameter = searchParameter,
                filterController = filterController
            )
        )

        stubComponent(
            componentAdditionalInfo = ComponentAdditionalInfo(
                nextPage = "p",
                enabled = false,
                totalProductData = TotalProductData(),
                redirection = redirection
            ),
            componentItems = componentItems,
            hasNextPage = true
        )

        stubLoadMore(
            hasLoaded = true
        )

        // create expected result
        val expected = arrayListOf<ComponentsItem>()

        expected.addVoucherList(componentItems)

        // load more
        viewModel.loadMore(true)

        // compare to the expected result
        viewModel.noMorePages
            .verifyValueEquals(true)
        viewModel.couponList
            .verifySuccessEquals(expected)
    }

    @Test
    fun `When use case is true and voucher list has 3 items with additional info that component doesn't have next page && cta text is not empty, so the result should get only voucher list`() {
        // stub necessary data
        viewModel
            .mockPrivateField("isLoading", false)

        val redirection = Redirection(
            ctaText = "Lihat Semua Kupon"
        )
        val componentItems = listOf(
            ComponentsItem(
                searchParameter = searchParameter,
                filterController = filterController
            ),
            ComponentsItem(
                searchParameter = searchParameter,
                filterController = filterController
            ),
            ComponentsItem(
                searchParameter = searchParameter,
                filterController = filterController
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

        stubLoadMore(
            hasLoaded = true
        )

        // create expected result
        val expected = arrayListOf<ComponentsItem>()

        expected.addVoucherList(componentItems)

        // load more
        viewModel.loadMore(true)

        // compare to the expected result
        viewModel.noMorePages
            .verifyValueEquals(true)
        viewModel.couponList
            .verifySuccessEquals(expected)
    }

    @Test
    fun `When use case is true and voucher list has 3 items with additional info that component has next page && redirection is null, so the result should get voucher list and shimmer`() {
        // stub necessary data
        viewModel
            .mockPrivateField("isLoading", false)

        val componentItems = listOf(
            ComponentsItem(
                searchParameter = searchParameter,
                filterController = filterController
            ),
            ComponentsItem(
                searchParameter = searchParameter,
                filterController = filterController
            ),
            ComponentsItem(
                searchParameter = searchParameter,
                filterController = filterController
            )
        )

        stubComponent(
            componentAdditionalInfo = ComponentAdditionalInfo(
                nextPage = "p",
                enabled = false,
                totalProductData = TotalProductData(),
                redirection = null
            ),
            componentItems = componentItems,
            hasNextPage = true
        )

        stubLoadMore(
            hasLoaded = true
        )

        // create expected result
        val expected = arrayListOf<ComponentsItem>()

        expected.addVoucherList(componentItems)
        expected.addShimmer()

        // load more
        viewModel.loadMore(true)

        // compare to the expected result
        viewModel.couponList
            .verifySuccessEquals(expected)
    }

    @Test
    fun `When use case is true and voucher list has 3 items with additional info that component has next page && cta text is null, so the result should get voucher list and shimmer`() {
        // stub necessary data
        viewModel
            .mockPrivateField("isLoading", false)

        val redirection = Redirection(
            ctaText = null
        )
        val componentItems = listOf(
            ComponentsItem(
                searchParameter = searchParameter,
                filterController = filterController
            ),
            ComponentsItem(
                searchParameter = searchParameter,
                filterController = filterController
            ),
            ComponentsItem(
                searchParameter = searchParameter,
                filterController = filterController
            )
        )

        stubComponent(
            componentAdditionalInfo = ComponentAdditionalInfo(
                nextPage = "p",
                enabled = false,
                totalProductData = TotalProductData(),
                redirection = redirection
            ),
            componentItems = componentItems,
            hasNextPage = true
        )

        stubLoadMore(
            hasLoaded = true
        )

        // create expected result
        val expected = arrayListOf<ComponentsItem>()

        expected.addVoucherList(componentItems)
        expected.addShimmer()

        // load more
        viewModel.loadMore(true)

        // compare to the expected result
        viewModel.couponList
            .verifySuccessEquals(expected)
    }

    @Test
    fun `When use case is true and voucher list has 3 items with additional info that component has next page && cta text is empty, so the result should get voucher list and shimmer`() {
        // stub necessary data
        viewModel
            .mockPrivateField("isLoading", false)

        val redirection = Redirection(
            ctaText = String.EMPTY
        )
        val componentItems = listOf(
            ComponentsItem(
                searchParameter = searchParameter,
                filterController = filterController
            ),
            ComponentsItem(
                searchParameter = searchParameter,
                filterController = filterController
            ),
            ComponentsItem(
                searchParameter = searchParameter,
                filterController = filterController
            )
        )

        stubComponent(
            componentAdditionalInfo = ComponentAdditionalInfo(
                nextPage = "p",
                enabled = false,
                totalProductData = TotalProductData(),
                redirection = redirection
            ),
            componentItems = componentItems,
            hasNextPage = true
        )

        stubLoadMore(
            hasLoaded = true
        )

        // create expected result
        val expected = arrayListOf<ComponentsItem>()

        expected.addVoucherList(componentItems)
        expected.addShimmer()

        // load more
        viewModel.loadMore(true)

        // compare to the expected result
        viewModel.couponList
            .verifySuccessEquals(expected)
    }
}
