package com.tokopedia.tokomart.searchcategory

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.discovery.common.constants.SearchApiConst.Companion.HARDCODED_SHOP_ID_PLEASE_DELETE
import com.tokopedia.minicart.common.domain.data.MiniCartItem
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.minicart.common.domain.data.MiniCartWidgetData
import com.tokopedia.minicart.common.domain.usecase.GetMiniCartListSimplifiedUseCase
import com.tokopedia.tokomart.searchcategory.presentation.model.ProductItemDataView
import com.tokopedia.tokomart.searchcategory.presentation.viewmodel.BaseSearchCategoryViewModel
import io.mockk.every
import io.mockk.slot
import io.mockk.verify
import org.junit.Assert.assertThat
import org.hamcrest.CoreMatchers.`is` as shouldBe

class UpdateCartTestHelper(
        private val baseViewModel: BaseSearchCategoryViewModel,
        private val getMiniCartListSimplifiedUseCase: GetMiniCartListSimplifiedUseCase,
        private val callback: Callback,
) {
    private val miniCartWidgetData = MiniCartWidgetData(
            totalProductCount = 7,
            totalProductPrice = 1000000,
    )

    private val miniCartItems = listOf(
            MiniCartItem(productId = "574261655", productParentId = "0", quantity = 10),

            MiniCartItem(productId = "574261652", productParentId = "0", quantity = 3),

            MiniCartItem(productId = "576261652", productParentId = "1682734974", quantity = 2),
            MiniCartItem(productId = "576261651", productParentId = "1682734974", quantity = 6),
            MiniCartItem(productId = "576261653", productParentId = "1682734974", quantity = 7),

            MiniCartItem(productId = "576861653", productParentId = "2682234972", quantity = 1),
            MiniCartItem(productId = "576861652", productParentId = "2682234972", quantity = 5),
    )

    private val miniCartSimplifiedData = MiniCartSimplifiedData(
            miniCartWidgetData = miniCartWidgetData,
            miniCartItems = miniCartItems,
            isShowMiniCartWidget = true,
    )

    fun `onViewResumed should update mini cart and quantity in product list`() {
        callback.`Given first page API will be successful`()
        `Given get mini cart simplified use case will be successful`(miniCartSimplifiedData)
        `Given view already created`()

        `When view resumed`()

        `Then assert get mini cart simplified use case params`()
        `Then assert mini cart widget live data is updated`(miniCartSimplifiedData.miniCartWidgetData)
        `Then assert product quantity is updated`()
        `Then assert mini cart widget visibility`()
    }

    private fun `Given get mini cart simplified use case will be successful`(
            miniCartSimplifiedData: MiniCartSimplifiedData
    ) {
        every {
            getMiniCartListSimplifiedUseCase.execute(any(), any())
        } answers {
            firstArg<(MiniCartSimplifiedData) -> Unit>().invoke(miniCartSimplifiedData)
        }
    }

    private fun `Given view already created`() {
        baseViewModel.onViewCreated()
    }

    private fun `When view resumed`() {
        baseViewModel.onViewResumed()
    }

    private fun `Then assert get mini cart simplified use case params`() {
        val shopIdListSlot = slot<List<String>>()

        verify {
            getMiniCartListSimplifiedUseCase.cancelJobs()
            getMiniCartListSimplifiedUseCase.setParams(capture(shopIdListSlot))
            getMiniCartListSimplifiedUseCase.execute(any(), any())
        }

        assertThat(shopIdListSlot.captured.first(), shouldBe(HARDCODED_SHOP_ID_PLEASE_DELETE))
    }

    private fun `Then assert mini cart widget live data is updated`(
            expectedMiniCartWidgetData: MiniCartWidgetData
    ) {
        assertThat(baseViewModel.miniCartWidgetLiveData.value, shouldBe(expectedMiniCartWidgetData))
    }

    private fun `Then assert product quantity is updated`() {
        val visitableList = baseViewModel.visitableListLiveData.value!!
        val productItems = visitableList.filterIsInstance<ProductItemDataView>()

        `Then assert product item non variant quantity`(miniCartItems, productItems)
        `Then assert product item variant quantity`(miniCartItems, productItems)
        `Then assert updated indices`(miniCartItems, visitableList)
    }

    private fun `Then assert product item non variant quantity`(
            miniCartItems: List<MiniCartItem>,
            productItems: List<ProductItemDataView>,
    ) {
        val miniCartItemsNonVariant = miniCartItems.filter { it.productParentId == "0" }

        miniCartItemsNonVariant.forEach { miniCartItem ->
            val productItemIndexed = productItems.withIndex().find {
                it.value.id == miniCartItem.productId
            }!!
            val productItem = productItemIndexed.value
            val reason = createInvalidNonVariantQtyReason(miniCartItem)
            assertThat(reason, productItem.nonVariantATC?.quantity, shouldBe(miniCartItem.quantity))
        }
    }

    private fun createInvalidNonVariantQtyReason(miniCartItem: MiniCartItem) =
            "Product \"${miniCartItem.productId}\" non variant quantity is invalid."

    private fun `Then assert product item variant quantity`(
            miniCartItems: List<MiniCartItem>,
            productItems: List<ProductItemDataView>,
    ) {
        val miniCartItemsVariant = miniCartItems.filter { it.productParentId != "0" }
        val miniCartItemsVariantGroup = miniCartItemsVariant.groupBy { it.productParentId }

        miniCartItemsVariantGroup.forEach { miniCartItemGroup ->
            val totalQuantity = miniCartItemGroup.value.sumBy { it.quantity }
            val productItemIndexed = productItems.withIndex()
                    .find { it.value.parentId == miniCartItemGroup.key }!!
            val productItem = productItemIndexed.value
            val reason = createInvalidVariantQtyReason(productItem.id, productItem.parentId)
            assertThat(reason, productItem.variantATC?.quantity, shouldBe(totalQuantity))
        }
    }

    private fun createInvalidVariantQtyReason(productId: String, parentProductId: String) =
            "Product \"$productId\" with parent \"$parentProductId\" variant quantity is invalid"

    private fun `Then assert updated indices`(
            miniCartItems: List<MiniCartItem>,
            visitableList: List<Visitable<*>>,
    ) {
        val expectedUpdatedIndices = createExpectedUpdatedIndices(miniCartItems, visitableList)
        val actualUpdatedIndices =
                baseViewModel.updatedVisitableIndicesLiveData.value?.getContentIfNotHandled()!!

        assertThat(actualUpdatedIndices, shouldBe(expectedUpdatedIndices.toList()))
    }

    private fun createExpectedUpdatedIndices(
            miniCartItems: List<MiniCartItem>,
            visitableList: List<Visitable<*>>,
    ): Set<Int> {
        val expectedUpdatedIndices = mutableSetOf<Int>()

        visitableList.forEachIndexed { index, visitable ->
            if (visitable is ProductItemDataView) {
                miniCartItems.forEach { miniCartItem ->
                    val isNonVariant = miniCartItem.productParentId == "0"
                            && visitable.id == miniCartItem.productId

                    val isVariant = miniCartItem.productParentId != "0"
                            && miniCartItem.productParentId == visitable.parentId

                    if (isNonVariant)
                        expectedUpdatedIndices.add(index)
                    else if (isVariant)
                        expectedUpdatedIndices.add(index)
                }
            }
        }

        return expectedUpdatedIndices
    }

    private fun `Then assert mini cart widget visibility`() {
        val isShowMiniCartWidget = miniCartSimplifiedData.isShowMiniCartWidget

        assertThat(baseViewModel.isShowMiniCartLiveData.value, shouldBe(isShowMiniCartWidget))
    }

    fun `onViewUpdateCartItems should update quantity in product list`() {
        callback.`Given first page API will be successful`()
        `Given get mini cart simplified use case will be successful`(miniCartSimplifiedData)
        `Given view already created`()

        `When view update cart items`()

        `Then assert product quantity is updated`()
    }

    private fun `When view update cart items`() {
        baseViewModel.onViewUpdateCartItems(miniCartSimplifiedData)
    }

    interface Callback {
        fun `Given first page API will be successful`()
    }
}