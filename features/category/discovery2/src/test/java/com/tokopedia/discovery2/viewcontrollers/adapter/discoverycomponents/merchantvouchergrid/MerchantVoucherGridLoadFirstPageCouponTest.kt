package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.merchantvouchergrid

import com.tokopedia.discovery2.data.ComponentAdditionalInfo
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.Redirection
import com.tokopedia.discovery2.data.TotalProductData
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.merchantvouchergrid.MerchantVoucherGridComponentExtension.addShimmer
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.merchantvouchergrid.MerchantVoucherGridComponentExtension.addVoucherList
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.unit.test.ext.verifyValueEquals
import org.junit.Test

class MerchantVoucherGridLoadFirstPageCouponTest: MerchantVoucherGridViewModelFixture() {
    @Test
    fun `When use case is null then the result of loading the first page coupon should be shimmer`() {
        // inject use case
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
    fun `When use case is false then the result of loading the first page coupon should be shimmer`() {
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
            hasLoaded = false
        )

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
    fun `When use case throws an error then the result of loading the first page coupon should be an error too`() {
        // stub necessary data
        stubComponent(
            componentAdditionalInfo = ComponentAdditionalInfo(
                nextPage = String.EMPTY,
                enabled = false,
                totalProductData = TotalProductData()
            ),
            componentItems = emptyList()
        )

        val throwable = Throwable(MerchantVoucherGridViewModel.ERROR_MESSAGE_EMPTY_DATA)
        stubLoadFirstPage(
            throwable = throwable
        )

        // load first page
        viewModel.loadFirstPageCoupon()

        // compare to the expected result
        viewModel.couponList
            .verifyFailEquals(MerchantVoucherGridViewModel.ERROR_MESSAGE_EMPTY_DATA)
    }

    @Test
    fun `When use case is true but voucher list is empty, then the result of loading the first page coupon should be an error`() {
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
            .verifyFailEquals(MerchantVoucherGridViewModel.ERROR_MESSAGE_EMPTY_DATA)
    }

    @Test
    fun `When use case is true but voucher list is null, then the result of loading the first page coupon should be an error`() {
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
            .verifyFailEquals(MerchantVoucherGridViewModel.ERROR_MESSAGE_EMPTY_DATA)
    }

    @Test
    fun `When use case is true and voucher list has 3 items with additional info that is component doesn't have next page && component additional info is null, so the result should get only voucher list`()  {
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
            componentAdditionalInfo = null,
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
    fun `When use case is true and voucher list has 3 items with additional info that is component doesn't have next page && redirection is null, so the result should get only voucher list`()  {
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
    fun `When use case is true and voucher list has 3 items with additional info that component doesn't have next page && cta text is empty, so the result should get only voucher list`()  {
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
    fun `When use case is true and voucher list has 3 items with additional info that component doesn't have next page && cta text is null, so the result should get only voucher list`()  {
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
    fun `When use case is true and voucher list has 3 items with additional info that component has next page && cta text is not empty, so the result should get only voucher list`()  {
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
    fun `When use case is true and voucher list has 3 items with additional info that component doesn't have next page && cta text is not empty, so the result should get only voucher list`()  {
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

    @Test
    fun `When use case is true and voucher list has 3 items with additional info that component has next page && redirection is null, so the result should get voucher list and shimmer`()  {
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
    fun `When use case is true and voucher list has 3 items with additional info that component has next page && cta text is null, so the result should get voucher list and shimmer`()  {
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
    fun `When use case is true and voucher list has 3 items with additional info that component has next page && cta text is empty, so the result should get voucher list and shimmer`()  {
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
}
