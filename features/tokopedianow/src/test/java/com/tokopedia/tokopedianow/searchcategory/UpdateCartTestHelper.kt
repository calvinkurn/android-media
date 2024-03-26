package com.tokopedia.tokopedianow.searchcategory

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.minicart.common.domain.data.MiniCartItem
import com.tokopedia.minicart.common.domain.data.MiniCartItemKey
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.minicart.common.domain.usecase.GetMiniCartListSimplifiedUseCase
import com.tokopedia.tokopedianow.search.presentation.viewmodel.TokoNowSearchViewModel
import com.tokopedia.tokopedianow.searchcategory.presentation.model.ProductItemDataView
import com.tokopedia.tokopedianow.searchcategory.utils.NO_VARIANT_PARENT_PRODUCT_ID
import com.tokopedia.tokopedianow.util.SearchCategoryDummyUtils
import com.tokopedia.tokopedianow.util.SearchCategoryDummyUtils.dummyChooseAddressData
import com.tokopedia.tokopedianow.util.SearchCategoryDummyUtils.miniCartItems
import com.tokopedia.tokopedianow.util.SearchCategoryDummyUtils.miniCartSimplifiedData
import com.tokopedia.unit.test.ext.verifyValueEquals
import io.mockk.every
import io.mockk.slot
import io.mockk.verify
import org.junit.Assert.assertThat
import org.hamcrest.CoreMatchers.`is` as shouldBe

class UpdateCartTestHelper(
    private val baseViewModel: TokoNowSearchViewModel,
    private val getMiniCartListSimplifiedUseCase: GetMiniCartListSimplifiedUseCase,
    private val callback: Callback
) {

    fun `onViewResumed should update mini cart and quantity in product list`() {
        val miniCartSimplifiedData = SearchCategoryDummyUtils.miniCartSimplifiedData
        callback.`Given first page API will be successful`()
        `Given get mini cart simplified use case will be successful`(miniCartSimplifiedData)
        `Given view already created`()

        `When view resumed`()

        `Then assert get mini cart simplified use case params`()
        `Then assert mini cart widget live data is updated`(miniCartSimplifiedData)
        `Then assert product quantity is updated`()
        `Then assert mini cart widget visibility`(miniCartSimplifiedData.isShowMiniCartWidget)
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
            getMiniCartListSimplifiedUseCase.setParams(capture(shopIdListSlot), any())
            getMiniCartListSimplifiedUseCase.execute(any(), any())
        }

        assertThat(shopIdListSlot.captured.first(), shouldBe(dummyChooseAddressData.shop_id))
    }

    private fun `Then assert mini cart widget live data is updated`(
        expectedMiniCartSimplifiedData: MiniCartSimplifiedData
    ) {
        assertThat(
            baseViewModel.miniCartWidgetLiveData.value,
            shouldBe(expectedMiniCartSimplifiedData)
        )
    }

    private fun `Then assert product quantity is updated`() {
        val visitableList = baseViewModel.visitableListLiveData.value!!
        val productItems = visitableList.filterIsInstance<ProductItemDataView>()

        `Then assert product item non variant quantity`(miniCartItems, productItems)
        `Then assert product item variant quantity`(miniCartItems, productItems)
        `Then assert updated indices`(miniCartItems, visitableList)
    }

    private fun `Then assert update toolbar notification true`() {
        baseViewModel.updateToolbarNotification
            .verifyValueEquals(true)
    }

    private fun `Then assert product item non variant quantity`(
        miniCartItems: Map<MiniCartItemKey, MiniCartItem>,
        productItems: List<ProductItemDataView>
    ) {
        val miniCartItemsNonVariant = miniCartItems.values.mapNotNull {
            if (it is MiniCartItem.MiniCartItemProduct && it.productParentId == NO_VARIANT_PARENT_PRODUCT_ID) it else null
        }

        miniCartItemsNonVariant.forEach { miniCartItem ->
            val productItemIndexed = productItems.withIndex().find {
                it.value.productCardModel.productId == miniCartItem.productId
            }!!
            val productItem = productItemIndexed.value
            val reason = createInvalidNonVariantQtyReason(miniCartItem)
            assertThat(reason, productItem.productCardModel.orderQuantity, shouldBe(miniCartItem.quantity))
        }
    }

    private fun createInvalidNonVariantQtyReason(miniCartItem: MiniCartItem.MiniCartItemProduct) =
        "Product \"${miniCartItem.productId}\" non variant quantity is invalid."

    private fun `Then assert product item variant quantity`(
        miniCartItems: Map<MiniCartItemKey, MiniCartItem>,
        productItems: List<ProductItemDataView>
    ) {
        val miniCartItemsVariant = miniCartItems.values.mapNotNull {
            if (it is MiniCartItem.MiniCartItemParentProduct) it else null
        }

        miniCartItemsVariant.forEach { miniCartItemGroup ->
            val totalQuantity = miniCartItemGroup.totalQuantity
            val productItemIndexed = productItems.withIndex()
                .find { it.value.parentId == miniCartItemGroup.parentId }!!
            val productItem = productItemIndexed.value
            val reason = createInvalidVariantQtyReason(productItem.productCardModel.productId, productItem.parentId)
            assertThat(reason, productItem.productCardModel.orderQuantity, shouldBe(totalQuantity))
        }
    }

    private fun createInvalidVariantQtyReason(productId: String, parentProductId: String) =
        "Product \"$productId\" with parent \"$parentProductId\" variant quantity is invalid"

    private fun `Then assert updated indices`(
        miniCartItems: Map<MiniCartItemKey, MiniCartItem>,
        visitableList: List<Visitable<*>>
    ) {
        val expectedUpdatedIndices = createExpectedUpdatedIndices(miniCartItems, visitableList)
        val actualUpdatedIndices = baseViewModel.updatedVisitableIndicesLiveData.value!!

        assertThat(actualUpdatedIndices, shouldBe(expectedUpdatedIndices.toList()))
    }

    private fun createExpectedUpdatedIndices(
        miniCartItems: Map<MiniCartItemKey, MiniCartItem>,
        visitableList: List<Visitable<*>>
    ): Set<Int> {
        val expectedUpdatedIndices = mutableSetOf<Int>()

        visitableList.forEachIndexed { index, visitable ->
            if (visitable is ProductItemDataView) {
                miniCartItems.values.forEach { miniCartItem ->
                    if (miniCartItem is MiniCartItem.MiniCartItemProduct) {
                        val isNonVariant = miniCartItem.productParentId == "0" || miniCartItem.productParentId == "" && visitable.productCardModel.productId == miniCartItem.productId
                        val isVariant = !(miniCartItem.productParentId == "0" || miniCartItem.productParentId == "") && miniCartItem.productParentId == visitable.parentId

                        if (isNonVariant) {
                            expectedUpdatedIndices.add(index)
                        }

                        if (!isVariant) {
                            expectedUpdatedIndices.add(index)
                        }
                    }
                }
            }
        }

        return expectedUpdatedIndices
    }

    private fun `Then assert mini cart widget visibility`(isShowMiniCartWidget: Boolean) {
        assertThat(baseViewModel.isShowMiniCartLiveData.value, shouldBe(isShowMiniCartWidget))
    }

    fun `update mini cart fail should hide mini cart`() {
        callback.`Given first page API will be successful`()
        `Given get mini cart simplified use case will fail`()
        `Given view already created`()

        `When view resumed`()

        `Then assert mini cart widget visibility`(false)
    }

    private fun `Given get mini cart simplified use case will fail`() {
        every {
            getMiniCartListSimplifiedUseCase.execute(any(), any())
        } answers {
            secondArg<(Throwable) -> Unit>().invoke(Throwable(message = "error"))
        }
    }

    fun `onViewReloadPage should have product with quantity from mini cart`() {
        callback.`Given first page API will be successful`()
        `Given get mini cart simplified use case will be successful`(miniCartSimplifiedData)
        `Given view created and resumed`()

        `When view reload page`()

        `Then assert product quantity is updated`()
        `Then assert update toolbar notification true`()
    }

    private fun `Given view created and resumed`() {
        baseViewModel.onViewCreated()
        baseViewModel.onViewResumed()
    }

    private fun `When view reload page`() {
        baseViewModel.onViewReloadPage()
    }

    fun `onViewUpdateCartItems should update quantity in product list`() {
        val miniCartSimplifiedData = SearchCategoryDummyUtils.miniCartSimplifiedData
        callback.`Given first page API will be successful`()
        `Given view already created`()

        `When view update cart items`(miniCartSimplifiedData)

        `Then assert product quantity is updated`()
        `Then assert mini cart widget visibility`(miniCartSimplifiedData.isShowMiniCartWidget)
    }

    private fun `When view update cart items`(miniCartSimplifiedData: MiniCartSimplifiedData) {
        baseViewModel.onViewUpdateCartItems(miniCartSimplifiedData)
    }

    interface Callback {
        fun `Given first page API will be successful`()
    }
}
