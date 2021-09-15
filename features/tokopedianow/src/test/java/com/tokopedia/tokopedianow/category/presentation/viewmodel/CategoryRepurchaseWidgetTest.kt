package com.tokopedia.tokopedianow.category.presentation.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.minicart.common.domain.data.MiniCartItem
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.tokopedianow.category.domain.model.CategoryModel
import com.tokopedia.tokopedianow.common.model.TokoNowProductCardUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowRecentPurchaseUiModel
import com.tokopedia.tokopedianow.searchcategory.jsonToObject
import com.tokopedia.tokopedianow.searchcategory.presentation.model.ProductItemDataView
import com.tokopedia.tokopedianow.searchcategory.utils.NO_VARIANT_PARENT_PRODUCT_ID
import com.tokopedia.tokopedianow.searchcategory.utils.REPURCHASE_WIDGET_POSITION
import com.tokopedia.tokopedianow.util.SearchCategoryDummyUtils
import io.mockk.every
import org.hamcrest.CoreMatchers.containsString
import org.hamcrest.CoreMatchers.everyItem
import org.hamcrest.CoreMatchers.hasItem
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.CoreMatchers.not
import org.junit.Assert.assertThat
import org.junit.Test
import org.hamcrest.CoreMatchers.`is` as shouldBe

class CategoryRepurchaseWidgetTest: CategoryTestFixtures() {

    @Test
    fun `show repurchase widget after 4th product`() {
        val categoryModel = "category/repurchasewidget/repurchase-widget.json"
            .jsonToObject<CategoryModel>()
        `Given get category first page use case will be successful`(categoryModel)

        `When view created`()

        val visitableList = tokoNowCategoryViewModel.visitableListLiveData.value!!
        `Then assert repurchase widget in visitable list`(visitableList)
    }

    private fun `Then assert repurchase widget in visitable list`(
        visitableList: List<Visitable<*>>,
    ) {
        val indexOfFirstProductCard = visitableList.indexOfFirst { it is ProductItemDataView }
        val repurchaseWidgetIndex = indexOfFirstProductCard + REPURCHASE_WIDGET_POSITION

        assertThat(
            visitableList[repurchaseWidgetIndex],
            instanceOf(TokoNowRecentPurchaseUiModel::class.java)
        )
    }

    @Test
    fun `do not show repurchase if less than 4 products`() {
        val categoryModel = "category/repurchasewidget/repurchase-widget-less-than-4-products.json"
            .jsonToObject<CategoryModel>()
        `Given get category first page use case will be successful`(categoryModel)

        `When view created`()

        val visitableList = tokoNowCategoryViewModel.visitableListLiveData.value!!
        `Then assert no repurchase widget in visitable list`(visitableList)
    }

    private fun `Then assert no repurchase widget in visitable list`(
        visitableList: List<Visitable<*>>,
    ) {
        assertThat(
            visitableList,
            everyItem(not(instanceOf(TokoNowRecentPurchaseUiModel::class.java)))
        )
    }

    @Test
    fun `update repurchase widget quantity from mini cart`() {
        val categoryModel = "category/repurchasewidget/repurchase-widget.json"
            .jsonToObject<CategoryModel>()

        val miniCartSimplifiedData = SearchCategoryDummyUtils.miniCartSimplifiedData

        `Given get category first page use case will be successful`(categoryModel)
        `Given view already created`()
        `Given get mini cart simplified use case will be successful`(miniCartSimplifiedData)

        `When view is resumed`()

        val visitableList = tokoNowCategoryViewModel.visitableListLiveData.value!!
        `Then assert repurchase widget product quantity`(visitableList, miniCartSimplifiedData)
        `Then assert recent purchase widget visitable is updated`(visitableList)
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

    private fun `When view is resumed`() {
        tokoNowCategoryViewModel.onViewResumed()
    }

    private fun `Then assert repurchase widget product quantity`(
        visitableList: List<Visitable<*>>,
        miniCartSimplifiedData: MiniCartSimplifiedData,
    ) {
        val repurchaseWidgetUiModel = visitableList.getRepurchaseWidgetUiModel()
        val repurchaseProductList = repurchaseWidgetUiModel.productList
        val miniCartItems = miniCartSimplifiedData.miniCartItems

        `Then verify non variant product quantity`(miniCartItems, repurchaseProductList)
        `Then verify product variant quantity`(miniCartItems, repurchaseProductList)
    }

    private fun List<Visitable<*>>.getRepurchaseWidgetUiModel() =
        find { it is TokoNowRecentPurchaseUiModel } as TokoNowRecentPurchaseUiModel

    private fun `Then verify non variant product quantity`(miniCartItems: List<MiniCartItem>, repurchaseProductList: List<TokoNowProductCardUiModel>) {
        val miniCartItemsNonVariant = miniCartItems.filter {
            it.productParentId == NO_VARIANT_PARENT_PRODUCT_ID
        }

        miniCartItemsNonVariant.forEach { miniCartItem ->
            val productItem = repurchaseProductList.find {
                it.productId == miniCartItem.productId
            } ?: return@forEach
            val reason = createInvalidNonVariantQtyReason(miniCartItem)
            assertThat(reason, productItem.product.nonVariant!!.quantity, shouldBe(miniCartItem.quantity))
        }
    }

    private fun createInvalidNonVariantQtyReason(miniCartItem: MiniCartItem) =
        "Product \"${miniCartItem.productId}\" non variant quantity is invalid."

    private fun `Then verify product variant quantity`(miniCartItems: List<MiniCartItem>, repurchaseProductList: List<TokoNowProductCardUiModel>) {
        val miniCartItemsVariant = miniCartItems.filter {
            it.productParentId != NO_VARIANT_PARENT_PRODUCT_ID
        }
        val miniCartItemsVariantGroup = miniCartItemsVariant.groupBy { it.productParentId }

        miniCartItemsVariantGroup.forEach { miniCartItemGroup ->
            val totalQuantity = miniCartItemGroup.value.sumOf { it.quantity }
            val productItem = repurchaseProductList.find {
                it.parentId == miniCartItemGroup.key
            } ?: return@forEach
            val parentProductId = productItem.productId
            val productId = productItem.productId

            val reason = createInvalidVariantQtyReason(productId, parentProductId)
            assertThat(reason, productItem.product.variant!!.quantity, shouldBe(totalQuantity))
        }
    }

    private fun createInvalidVariantQtyReason(productId: String, parentProductId: String) =
        "Product \"$productId\" with parent \"$parentProductId\" variant quantity is invalid"

    private fun `Then assert recent purchase widget visitable is updated`(
        visitableList: List<Visitable<*>>,
    ) {
        val updatedVisitableList = tokoNowCategoryViewModel.updatedVisitableIndicesLiveData.value!!
        val repurchaseWidgetIndex = visitableList.indexOfFirst {
            it is TokoNowRecentPurchaseUiModel
        }

        assertThat(updatedVisitableList, hasItem(repurchaseWidgetIndex))
    }
}