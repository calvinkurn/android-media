package com.tokopedia.product.manage.feature.quickedit.variant.presentation.viewmodel

import com.tokopedia.product.manage.data.createGetVariantResponse
import com.tokopedia.product.manage.data.createOptionResponse
import com.tokopedia.product.manage.data.createProductVariant
import com.tokopedia.product.manage.data.createProductVariantResponse
import com.tokopedia.product.manage.data.createSelectionResponse
import com.tokopedia.product.manage.common.feature.variant.presentation.data.EditVariantResult
import com.tokopedia.product.manage.common.feature.variant.presentation.data.GetVariantResult
import com.tokopedia.unit.test.ext.verifyValueEquals
import com.tokopedia.shop.common.data.source.cloud.model.productlist.ProductStatus
import org.junit.Test

class QuickEditVariantViewModelTest: QuickEditVariantViewModelTestFixture() {

    @Test
    fun `given variant list is NOT empty when get variants success should set variants result`() {
        val productName = "Tokopedia"
        val variantList = listOf(
            createProductVariantResponse(combination = listOf(0, 1)),
            createProductVariantResponse(combination = listOf(1, 0))
        )
        val firstOption = listOf(
            createOptionResponse(value = "Biru"),
            createOptionResponse(value = "Hijau")
        )
        val secondOption = listOf(
            createOptionResponse(value = "S"),
            createOptionResponse(value = "M")
        )
        val selections = listOf(
            createSelectionResponse(options = firstOption),
            createSelectionResponse(options = secondOption)
        )
        val response = createGetVariantResponse(
            productName,
            products = variantList,
            selections = selections
        )

        onGetProductVariant_thenReturn(response)

        viewModel.getData("1")

        val productVariants = listOf(
            createProductVariant(name = "Biru | M", combination = listOf(0, 1)),
            createProductVariant(name = "Hijau | S", combination = listOf(1, 0))
        )
        val expectedResult = GetVariantResult(productName, productVariants, selections, emptyList())

        viewModel.getProductVariantsResult
            .verifyValueEquals(expectedResult)

        verifyHideProgressBar()
    }

    @Test
    fun `given variant list is empty when get variants should hide progress bar and show error view`() {
        val response = createGetVariantResponse(products = emptyList())

        onGetProductVariant_thenReturn(response)

        viewModel.getData("1")

        verifyHideProgressBar()
        verifyShowErrorView()
    }

    @Test
    fun `when get variants error should hide progress bar and show error view`() {
        val error = NullPointerException()

        onGetProductVariant_thenError(error)

        viewModel.getData("1")

        verifyHideProgressBar()
        verifyShowErrorView()
    }

    @Test
    fun `when set variant price should update variant price`() {
        val productId = "1"
        val productName = "Tokopedia"
        val variantList = listOf(
            createProductVariantResponse(productID = "1", price = 100),
            createProductVariantResponse(productID = "2", price = 200)
        )
        val response = createGetVariantResponse(productName, products = variantList)

        onGetProductVariant_thenReturn(response)

        viewModel.getData(productId)
        viewModel.setVariantPrice("2", 300)
        viewModel.setVariantPrice("1", 150)
        viewModel.saveVariants()

        val productVariants = listOf(
            createProductVariant(id = "1", price = 150),
            createProductVariant(id = "2", price = 300)
        )
        val expectedResult = EditVariantResult(productId, productName, productVariants, emptyList(), emptyList())

        viewModel.onClickSaveButton
            .verifyValueEquals(expectedResult)
    }

    @Test
    fun `when set variant stock should update variant stock`() {
        val productId = "1"
        val productName = "Tokopedia"
        val variantList = listOf(
            createProductVariantResponse(productID = "1", stock = 2),
            createProductVariantResponse(productID = "2", stock = 3)
        )
        val response = createGetVariantResponse(productName, products = variantList)

        onGetProductVariant_thenReturn(response)

        viewModel.getData(productId)
        viewModel.setVariantStock("2", 5)
        viewModel.setVariantStock("1", 4)
        viewModel.saveVariants()

        val productVariants = listOf(
            createProductVariant(id = "1", stock = 4, isAllStockEmpty = false),
            createProductVariant(id = "2", stock = 5, isAllStockEmpty = false)
        )
        val expectedResult = EditVariantResult(
            productId,
            productName,
            productVariants,
            emptyList(),
            emptyList(),
            editStock = true
        )

        viewModel.onClickSaveButton
            .verifyValueEquals(expectedResult)
    }

    @Test
    fun `when set variant status should update variant status`() {
        val productId = "1"
        val productName = "Tokopedia"
        val variantList = listOf(
            createProductVariantResponse(productID = "1", status = ProductStatus.ACTIVE),
            createProductVariantResponse(productID = "2", status = ProductStatus.INACTIVE)
        )
        val response = createGetVariantResponse(productName, products = variantList)

        onGetProductVariant_thenReturn(response)

        viewModel.getData(productId)
        viewModel.setVariantStatus("2", ProductStatus.ACTIVE)
        viewModel.setVariantStatus("1", ProductStatus.INACTIVE)
        viewModel.saveVariants()

        val productVariants = listOf(
            createProductVariant(id = "1", status = ProductStatus.INACTIVE),
            createProductVariant(id = "2", status = ProductStatus.ACTIVE)
        )
        val expectedResult = EditVariantResult(
            productId,
            productName,
            productVariants,
            emptyList(),
            emptyList(),
            editStatus = true
        )

        viewModel.onClickSaveButton
            .verifyValueEquals(expectedResult)
    }

    @Test
    fun `given variant result is null when set variant price should NOT update variant`() {
        viewModel.setVariantPrice("1", 100)
        viewModel.saveVariants()

        viewModel.onClickSaveButton
            .verifyValueEquals(null)
    }

    @Test
    fun `when all variant stock is empty should set show stock ticker false`() {
        val productId = "1"
        val productName = "Tokopedia"
        val variantList = listOf(
            createProductVariantResponse(productID = "1", stock = 1),
            createProductVariantResponse(productID = "2", stock = 0)
        )
        val response = createGetVariantResponse(productName, products = variantList)

        onGetProductVariant_thenReturn(response)

        viewModel.getData("5")
        viewModel.setVariantStock(productId, 0)

        viewModel.showStockInfo
            .verifyValueEquals(false)
    }

    @Test
    fun `when variant stock is partially empty should set show stock ticker true`() {
        val productId = "1"
        val productName = "Tokopedia"
        val variantList = listOf(
            createProductVariantResponse(productID = "1", stock = 0),
            createProductVariantResponse(productID = "2", stock = 0)
        )
        val response = createGetVariantResponse(productName, products = variantList)

        onGetProductVariant_thenReturn(response)

        viewModel.getData("5")
        viewModel.setVariantStock(productId, 1)

        viewModel.showStockInfo
            .verifyValueEquals(true)
    }
}