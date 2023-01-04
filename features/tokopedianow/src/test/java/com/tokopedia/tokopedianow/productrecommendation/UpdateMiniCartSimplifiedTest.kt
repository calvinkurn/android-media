package com.tokopedia.tokopedianow.productrecommendation

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.minicart.common.domain.data.MiniCartItem
import com.tokopedia.minicart.common.domain.data.MiniCartItemKey
import com.tokopedia.minicart.common.domain.data.MiniCartItemType
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.tokopedianow.common.domain.mapper.VisitableMapper.resetAllProductCardCarouselItemQuantities
import com.tokopedia.tokopedianow.common.model.TokoNowProductCardCarouselItemUiModel
import com.tokopedia.tokopedianow.util.TestUtils.getPrivateField
import com.tokopedia.unit.test.ext.verifyValueEquals
import org.junit.Assert
import org.junit.Test

class UpdateMiniCartSimplifiedTest: TokoNowProductRecommendationViewModelTestFixture() {

    private fun setNewQuantityAndVerifyAllProductQuantities(
        position: Int,
        product: TokoNowProductCardCarouselItemUiModel,
        newQuantity: Int
    ) {
        productModels[position] = product.copy(
            productCardModel = product.productCardModel.copy(
                orderQuantity = newQuantity
            )
        )
        viewModel.productModelsUpdate.verifyValueEquals(productModels)
    }

    private fun verifyProperProductQuantity(
        position: Int,
        newQuantity: Int
    ) {
        val actualProducts = viewModel.getPrivateField<MutableList<Visitable<*>>>(privateFieldProductModels)
        val actualProduct = actualProducts[position] as TokoNowProductCardCarouselItemUiModel
        Assert.assertEquals(newQuantity, actualProduct.productCardModel.orderQuantity)
    }

    @Test
    fun `while updating mMiniCartSimplifiedData, quantity of one product non variant type should be updated to the new one`() {
        val position = 2
        val expectedProduct = productModels[position] as TokoNowProductCardCarouselItemUiModel
        val productId = expectedProduct.getProductId()
        val parentId = "0"
        val newQuantity = 5

        mockProductModels()

        val miniCartSimplifiedData = MiniCartSimplifiedData(
            miniCartItems = mapOf(
                Pair(
                    MiniCartItemKey(
                        id = productId
                    ),
                    MiniCartItem.MiniCartItemProduct(
                        productId = productId,
                        quantity = newQuantity,
                        productParentId = parentId
                    )
                )
            )
        )

        viewModel.updateMiniCartSimplified(miniCartSimplifiedData)

        setNewQuantityAndVerifyAllProductQuantities(
            position = position,
            product = expectedProduct,
            newQuantity = newQuantity
        )

        verifyProperProductQuantity(
            position = position,
            newQuantity = newQuantity
        )
    }

    @Test
    fun `while updating mMiniCartSimplifiedData, quantity of one product variant type should be updated to the new one`() {
        val position = 4
        val expectedProduct = productModels[position] as TokoNowProductCardCarouselItemUiModel
        val parentId = expectedProduct.parentId
        val newQuantity = 6
        mockProductModels()

        val miniCartSimplifiedData = MiniCartSimplifiedData(
            miniCartItems = mapOf(
                Pair(
                    MiniCartItemKey(
                        id = parentId,
                        type = MiniCartItemType.PARENT
                    ),
                    MiniCartItem.MiniCartItemParentProduct(
                        parentId = parentId,
                        totalQuantity = newQuantity
                    )
                ),
                Pair(
                    MiniCartItemKey(
                        id = parentId,
                        type = MiniCartItemType.PARENT
                    ),
                    MiniCartItem.MiniCartItemParentProduct(
                        parentId = parentId,
                        totalQuantity = newQuantity
                    )
                )
            )
        )

        viewModel.updateMiniCartSimplified(miniCartSimplifiedData)

        setNewQuantityAndVerifyAllProductQuantities(
            position = position,
            product = expectedProduct,
            newQuantity = newQuantity
        )

        verifyProperProductQuantity(
            position = position,
            newQuantity = newQuantity
        )
    }

    @Test
    fun `while updating mMiniCartSimplifiedData, should reset all quantities because miniCartItems property has an empty value`() {
        mockProductModels()

        val miniCartSimplifiedData = MiniCartSimplifiedData(
            miniCartItems = mapOf()
        )

        viewModel.updateMiniCartSimplified(miniCartSimplifiedData)

        productModels.resetAllProductCardCarouselItemQuantities()

        viewModel.productModelsUpdate.verifyValueEquals(productModels)
    }

    @Test
    fun `while updating mMiniCartSimplifiedData, should not change the productModels because mMiniCartSimplifiedData is null`() {
        mockProductModels()

        viewModel.updateMiniCartSimplified(null)

        val actualProducts = viewModel.getPrivateField<MutableList<Visitable<*>>>(privateFieldProductModels)

        Assert.assertEquals(productModels, actualProducts)
    }

}
