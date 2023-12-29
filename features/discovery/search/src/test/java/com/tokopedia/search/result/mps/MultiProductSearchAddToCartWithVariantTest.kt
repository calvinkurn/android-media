package com.tokopedia.search.result.mps

import com.tokopedia.search.jsonToObject
import com.tokopedia.search.result.mps.shopwidget.MPSShopWidgetDataView
import com.tokopedia.search.result.mps.shopwidget.MPSShopWidgetProductDataView
import org.junit.Assert.assertEquals
import org.junit.Test

const val MPSSuccessWithVariantJSON = "mps/mps-with-variant.json"
const val MPSSuccessWithNoVariantJSON = "mps/mps-with-empty-variant.json"
class MultiProductSearchAddToCartVariantTest: MultiProductSearchTestFixtures() {

    @Test
    fun `add to cart shop widget product with variant`() {
        val mpsState = MPSState().success(MPSSuccessWithVariantJSON.jsonToObject())
        val mpsViewModel = mpsViewModel(mpsState)

        val (mpsShopWidgetDataView, mpsShopWidgetProduct) = mpsState.getAddToCartWithVariantTestProduct(true)

        `When add to cart`(mpsViewModel, mpsShopWidgetDataView, mpsShopWidgetProduct)
        mpsViewModel.`assert bottom sheet state is showing`()
    }

    @Test
    fun `add to cart shop widget product with no variant`() {
        val mpsState = MPSState().success(MPSSuccessWithNoVariantJSON.jsonToObject())
        val mpsViewModel = mpsViewModel(mpsState)

        val (mpsShopWidgetDataView, mpsShopWidgetProduct) = mpsState.getAddToCartWithVariantTestProduct(false)

        `When add to cart`(mpsViewModel, mpsShopWidgetDataView, mpsShopWidgetProduct)
        mpsViewModel.`assert bottom sheet state is close`()
    }

    @Test
    fun `close bottom sheet variant`() {
        val mpsState = MPSState().success(MPSSuccessWithNoVariantJSON.jsonToObject())
        val mpsViewModel = mpsViewModel(mpsState)
        `Given Product Variant Already Add To Cart`(mpsState, mpsViewModel)
        mpsViewModel.`assert bottom sheet state is close`()
    }

    private fun MPSState.getAddToCartWithVariantTestProduct(isWithVariant: Boolean
    ): Pair<MPSShopWidgetDataView, MPSShopWidgetProductDataView> {

        val mpsShopWidgetDataView = visitableList
            .filterIsInstance<MPSShopWidgetDataView>()
            .first()

        val mpsShopWidgetProduct = mpsShopWidgetDataView.productList.first {
            it.parentId.isNotEmpty() == isWithVariant
        }

        return Pair(mpsShopWidgetDataView, mpsShopWidgetProduct)
    }

    private fun `When add to cart`(
        mpsViewModel: MPSViewModel,
        mpsShopWidgetDataView: MPSShopWidgetDataView,
        mpsShopWidgetProduct: MPSShopWidgetProductDataView
    ) {
        mpsViewModel.onAddToCart(mpsShopWidgetDataView, mpsShopWidgetProduct)
    }

    private fun MPSViewModel.`assert bottom sheet state is showing`() {
        assertEquals(
            true,
            this.stateValue.bottomSheetVariantState.isOpen,
        )
    }

    private fun MPSViewModel.`assert bottom sheet state is close`() {
        assertEquals(
            false,
            this.stateValue.bottomSheetVariantState.isOpen,
        )
    }

    private fun `Dismiss bottom sheet variant`(mpsViewModel: MPSViewModel) {
        mpsViewModel.onBottomSheetVariantDismissed()
    }

    private fun `Given Product Variant Already Add To Cart`(mpsState: MPSState, mpsViewModel: MPSViewModel) {
        val (mpsShopWidgetDataView, mpsShopWidgetProduct) = mpsState.getAddToCartWithVariantTestProduct(true)
        `When add to cart`(mpsViewModel, mpsShopWidgetDataView, mpsShopWidgetProduct)
        `Dismiss bottom sheet variant`(mpsViewModel)
    }
}
