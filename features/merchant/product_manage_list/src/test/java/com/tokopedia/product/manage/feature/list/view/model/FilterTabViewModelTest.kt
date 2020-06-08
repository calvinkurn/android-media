package com.tokopedia.product.manage.feature.list.view.model

import com.tokopedia.product.manage.R
import com.tokopedia.product.manage.feature.list.view.adapter.factory.ProductFilterAdapterFactory
import com.tokopedia.product.manage.feature.list.view.model.FilterTabViewModel.*
import com.tokopedia.shop.common.data.source.cloud.model.productlist.ProductStatus
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.*
import org.junit.Test

class FilterTabViewModelTest {

    @Test
    fun `when get view model type should return view type resourceId`() {
        val viewType = 10000
        val count = 1
        val tab: FilterTabViewModel = Active(count)
        val typeFactory = mockk<ProductFilterAdapterFactory>(relaxed = true)

        every { typeFactory.type(any()) } returns viewType

        assertEquals(viewType, tab.type(typeFactory))
        assertEquals(count, tab.count)
    }

    @Test
    fun `when filter tab is active should have active tab data`() {
        val titleId = R.string.product_manage_filter_active
        val status = ProductStatus.ACTIVE
        val productCount = 1

        val tab = Active(productCount)

        assertEquals(titleId, tab.titleId)
        assertEquals(status, tab.status)
        assertEquals(productCount, tab.count)
    }

    @Test
    fun `when filter tab is inactive should have inactive tab data`() {
        val titleId = R.string.product_manage_filter_inactive
        val status = ProductStatus.INACTIVE
        val productCount = 2

        val tab = InActive(productCount)

        assertEquals(titleId, tab.titleId)
        assertEquals(status, tab.status)
        assertEquals(productCount, tab.count)
    }

    @Test
    fun `when filter tab is violation should have violation tab data`() {
        val titleId = R.string.product_manage_filter_banned
        val status = ProductStatus.VIOLATION
        val productCount = 3

        val tab = Violation(productCount)

        assertEquals(titleId, tab.titleId)
        assertEquals(status, tab.status)
        assertEquals(productCount, tab.count)
    }
}