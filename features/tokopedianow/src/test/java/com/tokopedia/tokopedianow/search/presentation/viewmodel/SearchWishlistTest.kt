package com.tokopedia.tokopedianow.search.presentation.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokopedianow.common.model.TokoNowProductCardViewUiModel
import com.tokopedia.tokopedianow.searchcategory.presentation.model.ProductItemDataView
import com.tokopedia.tokopedianow.util.TestUtils.getParentPrivateField
import com.tokopedia.tokopedianow.util.TestUtils.mockSuperClassField
import org.junit.Assert
import org.junit.Test

class SearchWishlistTest: SearchTestFixtures() {
    @Test
    fun `when updating wishlist status but the product not found should not do nothing`() {
        /**
         * create test data
         */
        val productId = "1000"
        val fieldName = "visitableList"
        val fieldValue = mutableListOf<Visitable<*>>()

        /**
         * mock private field from viewModel
         */
        tokoNowSearchViewModel.mockSuperClassField(
            name = fieldName,
            value = fieldValue
        )

        /**
         * update wishlist status
         */
        tokoNowSearchViewModel.updateWishlistStatus(
            productId = productId,
            hasBeenWishlist = true
        )

        /**
         * verify the data test
         */
        Assert.assertTrue(tokoNowSearchViewModel.updatedVisitableIndicesLiveData.value == null)
    }

    @Test
    fun `when updating wishlist status success and the status turns out as we expected`() {
        /**
         * create test data
         */
        val productId = "1000"
        val fieldName = "visitableList"
        val fieldValue = mutableListOf<Visitable<*>>(
            ProductItemDataView(
                productCardModel = TokoNowProductCardViewUiModel(
                    productId = productId,
                    isWishlistShown = true,
                    hasBeenWishlist = false
                )
            )
        )
        val expectedValue = mutableListOf<Visitable<*>>(
            ProductItemDataView(
                productCardModel = TokoNowProductCardViewUiModel(
                    productId = productId,
                    isWishlistShown = true,
                    hasBeenWishlist = true
                )
            )
        )

        /**
         * mock private field from viewModel
         */
        tokoNowSearchViewModel.mockSuperClassField(
            name = fieldName,
            value = fieldValue
        )

        /**
         * update wishlist status
         */
        tokoNowSearchViewModel.updateWishlistStatus(
            productId = productId,
            hasBeenWishlist = true
        )

        /**
         * verify the data test
         */
        val actualValue = tokoNowSearchViewModel.getParentPrivateField<MutableList<Visitable<*>>>(fieldName)
        Assert.assertEquals(expectedValue, actualValue)
        Assert.assertTrue(tokoNowSearchViewModel.updatedVisitableIndicesLiveData.value != null)
    }
}
