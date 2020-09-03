package com.tokopedia.product.manage.feature.list.view.model

import com.tokopedia.product.manage.common.feature.list.model.PriceUiModel
import com.tokopedia.product.manage.data.createProductViewModel
import com.tokopedia.product.manage.common.feature.list.view.adapter.factory.ProductManageAdapterFactory
import com.tokopedia.shop.common.data.source.cloud.model.productlist.ProductStatus
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Test

class ProductViewModelTest {

    @Test
    fun `when get view model type should return view type resourceId`() {
        val viewType = 10000
        val typeFactory = mockk<ProductManageAdapterFactory>(relaxed = true)
        val viewModel = createProductViewModel()

        every { typeFactory.type(any()) } returns viewType

        assertEquals(viewType, viewModel.type(typeFactory))
    }

    @Test
    fun `when isVariant null should return isVariant false`() {
        val viewModel = createProductViewModel(isVariant = null)

        val expected = false
        val actual = viewModel.isVariant()

        assertEquals(expected, actual)
    }

    @Test
    fun `when isVariant true should return isVariant true`() {
        val viewModel = createProductViewModel(isVariant = true)

        val expected = true
        val actual = viewModel.isVariant()

        assertEquals(expected, actual)
    }

    @Test
    fun `when isVariant null should return isNotVariant true`() {
        val viewModel = createProductViewModel(isVariant = null)

        val expected = true
        val actual = viewModel.isNotVariant()

        assertEquals(expected, actual)
    }

    @Test
    fun `when isVariant true should return isNotVariant false`() {
        val viewModel = createProductViewModel(isVariant = true)

        val expected = false
        val actual = viewModel.isNotVariant()

        assertEquals(expected, actual)
    }

    @Test
    fun `when product status active should return isActive true`() {
        val viewModel = createProductViewModel(status = ProductStatus.ACTIVE)

        val expected = true
        val actual = viewModel.isActive()

        assertEquals(expected, actual)
    }

    @Test
    fun `when product status NOT active should return isActive false`() {
        val viewModel = createProductViewModel(status = ProductStatus.INACTIVE)

        val expected = false
        val actual = viewModel.isActive()

        assertEquals(expected, actual)
    }

    @Test
    fun `when product status inactive should return isInactive true`() {
        val viewModel = createProductViewModel(status = ProductStatus.INACTIVE)

        val expected = true
        val actual = viewModel.isInactive()

        assertEquals(expected, actual)
    }

    @Test
    fun `when product status NOT inactive should return isInactive false`() {
        val viewModel = createProductViewModel(status = ProductStatus.VIOLATION)

        val expected = false
        val actual = viewModel.isInactive()

        assertEquals(expected, actual)
    }

    @Test
    fun `when product status violation should return isViolation true`() {
        val viewModel = createProductViewModel(status = ProductStatus.VIOLATION)

        val expected = true
        val actual = viewModel.isViolation()

        assertEquals(expected, actual)
    }

    @Test
    fun `when product status NOT violation should return isViolation false`() {
        val viewModel = createProductViewModel(status = ProductStatus.ACTIVE)

        val expected = false
        val actual = viewModel.isViolation()

        assertEquals(expected, actual)
    }

    @Test
    fun `when product status violation should return isNotViolation false`() {
        val viewModel = createProductViewModel(status = ProductStatus.VIOLATION)

        val expected = false
        val actual = viewModel.isNotViolation()

        assertEquals(expected, actual)
    }

    @Test
    fun `when product status NOT violation should return isNotViolation true`() {
        val viewModel = createProductViewModel(status = ProductStatus.ACTIVE)

        val expected = true
        val actual = viewModel.isNotViolation()

        assertEquals(expected, actual)
    }

    @Test
    fun `when product status empty and stock zero should return isEmpty true`() {
        val viewModel = createProductViewModel(status = ProductStatus.ACTIVE, stock = 0)

        val expected = true
        val actual = viewModel.isEmpty()

        assertEquals(expected, actual)
    }

    @Test
    fun `when product status NOT empty and stock more than zero should return isEmpty false`() {
        val viewModel = createProductViewModel(status = ProductStatus.INACTIVE, stock = 1)

        val expected = false
        val actual = viewModel.isEmpty()

        assertEquals(expected, actual)
    }

    @Test
    fun `when product status null and stock more than zero should return isEmpty false`() {
        val viewModel = createProductViewModel(status = null, stock = 1)

        val expected = false
        val actual = viewModel.isEmpty()

        assertEquals(expected, actual)
    }

    @Test
    fun `when get view model field should return field value`() {
        val id = ""
        val name: String? = "Tolak Angin"
        val imageUrl: String? = "imageUrl"
        val minPrice: PriceUiModel? = PriceUiModel("10000", "Rp10.000")
        val maxPrice: PriceUiModel? = PriceUiModel("100000", "Rp100.000")
        val status: ProductStatus? = ProductStatus.ACTIVE
        val url: String? = "productUrl"
        val cashBack = 0
        val stock: Int? = 1
        val featured = false
        val isVariant: Boolean? = false
        val multiSelectActive = false
        val isChecked = false

       val viewModel = createProductViewModel(
            id,
            name,
            imageUrl,
            minPrice,
            maxPrice,
            status,
            url,
            cashBack,
            stock,
            featured,
            isVariant,
            multiSelectActive,
            isChecked
        )

        assertEquals(id, viewModel.id)
        assertEquals(name, viewModel.title)
        assertEquals(imageUrl, viewModel.imageUrl)
        assertEquals(minPrice, viewModel.minPrice)
        assertEquals(maxPrice, viewModel.maxPrice)
        assertEquals(status, viewModel.status)
        assertEquals(url, viewModel.url)
        assertEquals(cashBack, viewModel.cashBack)
        assertEquals(stock, viewModel.stock)
        assertEquals(featured, viewModel.isFeatured)
        assertEquals(isVariant, viewModel.isVariant)
        assertEquals(multiSelectActive, viewModel.multiSelectActive)
        assertEquals(isChecked, viewModel.isChecked)
    }
}