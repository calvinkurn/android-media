package com.tokopedia.product.manage.feature.list.view.model

import com.tokopedia.iconunify.IconUnify
import com.tokopedia.product.manage.R
import com.tokopedia.product.manage.data.createProductViewModel
import com.tokopedia.product.manage.feature.list.view.adapter.factory.ProductMenuAdapterFactory
import com.tokopedia.product.manage.feature.list.view.model.ProductMenuViewModel.*
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@Ignore
@RunWith(JUnit4::class)
class ProductMenuViewModelTest {

    @Test
    fun `when get view model type should return view type resourceId`() {
        val viewType = 10000
        val product = createProductViewModel()
        val menu: ProductMenuViewModel = Preview(product)
        val typeFactory = mockk<ProductMenuAdapterFactory>(relaxed = true)

        every { typeFactory.type(menu) } returns viewType

        assertEquals(viewType, menu.type(typeFactory))
        assertEquals(product, menu.product)
    }

    @Test
    fun `when menu is preview should have preview menu data`() {
        val titleId =  R.string.product_manage_preview_menu
        val iconId = IconUnify.VISIBILITY
        val product = createProductViewModel()

        val menu = Preview(product)

        assertEquals(titleId, menu.title)
        assertEquals(iconId, menu.icon)
        assertEquals(product, menu.product)
    }

    @Test
    fun `when menu is duplicate product should have duplicate product menu data`() {
        val titleId =  R.string.product_manage_duplicate_product_menu
        val iconId =  IconUnify.COPY
        val product = createProductViewModel()

        val menu = Duplicate(product)

        assertEquals(titleId, menu.title)
        assertEquals(iconId, menu.icon)
        assertEquals(product, menu.product)
    }

    @Test
    fun `when menu is stock reminder should have stock reminder menu data`() {
        val titleId =  R.string.product_manage_stock_reminder_menu
        val iconId =  IconUnify.BELL
        val product = createProductViewModel()

        val menu = StockReminder(product)

        assertEquals(titleId, menu.title)
        assertEquals(iconId, menu.icon)
        assertEquals(product, menu.product)
    }

    @Test
    fun `when menu is delete product should have delete product menu data`() {
        val titleId =  R.string.product_manage_menu_delete_product
        val iconId =  IconUnify.DELETE
        val product = createProductViewModel()

        val menu = Delete(product)

        assertEquals(titleId, menu.title)
        assertEquals(iconId, menu.icon)
        assertEquals(product, menu.product)
    }

    @Test
    fun `when menu is set top ads should have set top ads menu data`() {
        val titleId =  R.string.product_manage_set_promo_ads_menu
        val iconId =  IconUnify.SPEAKER
        val product = createProductViewModel()

        val menu = SetTopAds(product)

        assertEquals(titleId, menu.title)
        assertEquals(iconId, menu.icon)
        assertEquals(product, menu.product)
    }

    @Test
    fun `when menu is show top ads performance should have show top ads performance menu data`() {
        val titleId =  R.string.product_manage_see_promo_ads_menu
        val iconId =  IconUnify.SPEAKER
        val product = createProductViewModel()

        val menu = SeeTopAds(product)

        assertEquals(titleId, menu.title)
        assertEquals(iconId, menu.icon)
        assertEquals(product, menu.product)
    }

    @Test
    fun `when menu is set cashBack should have set cashBack menu data`() {
        val titleId =  R.string.product_manage_menu_set_cashback
        val iconId =  IconUnify.DISCOUNT
        val product = createProductViewModel()

        val menu = SetCashBack(product)

        assertEquals(titleId, menu.title)
        assertEquals(iconId, menu.icon)
        assertEquals(product, menu.product)
    }

    @Test
    fun `when menu is set featured product should have set featured product menu data`() {
        val titleId =  R.string.product_manage_set_featured_menu
        val iconId =  IconUnify.STAR_CIRCLE
        val product = createProductViewModel()

        val menu = SetFeaturedProduct(product)

        assertEquals(titleId, menu.title)
        assertEquals(iconId, menu.icon)
        assertEquals(product, menu.product)
    }

    @Test
    fun `when menu is remove featured product should have remove featured product menu data`() {
        val titleId =  R.string.product_manage_remove_featured_menu
        val iconId =  IconUnify.STAR_CIRCLE
        val product = createProductViewModel()

        val menu = RemoveFeaturedProduct(product)

        assertEquals(titleId, menu.title)
        assertEquals(iconId, menu.icon)
        assertEquals(product, menu.product)
    }
}