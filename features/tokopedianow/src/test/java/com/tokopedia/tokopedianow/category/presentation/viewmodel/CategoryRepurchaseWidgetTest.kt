package com.tokopedia.tokopedianow.category.presentation.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.minicart.common.domain.data.MiniCartItem
import com.tokopedia.minicart.common.domain.data.MiniCartItemKey
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.minicart.common.domain.data.getMiniCartItemProduct
import com.tokopedia.tokopedianow.category.domain.model.CategoryModel
import com.tokopedia.tokopedianow.common.model.TokoNowProductCardUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowRepurchaseUiModel
import com.tokopedia.tokopedianow.searchcategory.AddToCartNonVariantTestHelper
import com.tokopedia.tokopedianow.searchcategory.AddToCartNonVariantTestHelper.Companion.AddToCartTestObject.addToCartQty
import com.tokopedia.tokopedianow.searchcategory.AddToCartNonVariantTestHelper.Companion.AddToCartTestObject.addToCartSuccessModel
import com.tokopedia.tokopedianow.searchcategory.AddToCartNonVariantTestHelper.Companion.AddToCartTestObject.cartId
import com.tokopedia.tokopedianow.searchcategory.AddToCartNonVariantTestHelper.Companion.AddToCartTestObject.errorMessage
import com.tokopedia.tokopedianow.searchcategory.AddToCartNonVariantTestHelper.Companion.DeleteCartTestObject.deleteCartMessage
import com.tokopedia.tokopedianow.searchcategory.AddToCartNonVariantTestHelper.Companion.DeleteCartTestObject.deleteCartResponse
import com.tokopedia.tokopedianow.searchcategory.AddToCartNonVariantTestHelper.Companion.PRODUCT_ID_NON_VARIANT_ATC
import com.tokopedia.tokopedianow.searchcategory.jsonToObject
import com.tokopedia.tokopedianow.searchcategory.presentation.model.ProductItemDataView
import com.tokopedia.tokopedianow.searchcategory.utils.NO_VARIANT_PARENT_PRODUCT_ID
import com.tokopedia.tokopedianow.searchcategory.utils.REPURCHASE_WIDGET_POSITION
import com.tokopedia.tokopedianow.util.SearchCategoryDummyUtils
import io.mockk.every
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
            instanceOf(TokoNowRepurchaseUiModel::class.java)
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
            everyItem(not(instanceOf(TokoNowRepurchaseUiModel::class.java)))
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
        `Then assert repurchase widget visitable is updated`(visitableList)
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
        find { it is TokoNowRepurchaseUiModel } as TokoNowRepurchaseUiModel

    private fun `Then verify non variant product quantity`(
            miniCartItems: Map<MiniCartItemKey, MiniCartItem>,
            repurchaseProductList: List<TokoNowProductCardUiModel>,
    ) {
        val miniCartItemsNonVariant = miniCartItems.filter {
            val value = it.value
            value is MiniCartItem.MiniCartItemProduct && value.productParentId == NO_VARIANT_PARENT_PRODUCT_ID
        }.map { it.value as MiniCartItem.MiniCartItemProduct }

        miniCartItemsNonVariant.forEach { miniCartItem ->
            val productItem = repurchaseProductList.find {
                it.productId == miniCartItem.productId
            } ?: return@forEach
            val reason = createInvalidNonVariantQtyReason(miniCartItem)
            assertThat(
                reason,
                productItem.product.nonVariant!!.quantity,
                shouldBe(miniCartItem.quantity)
            )
        }
    }

    private fun createInvalidNonVariantQtyReason(miniCartItem: MiniCartItem.MiniCartItemProduct) =
        "Product \"${miniCartItem.productId}\" non variant quantity is invalid."

    private fun `Then verify product variant quantity`(
            miniCartItems: Map<MiniCartItemKey, MiniCartItem>,
            repurchaseProductList: List<TokoNowProductCardUiModel>,
    ) {
        val miniCartItemsVariant = miniCartItems.mapNotNull {
            if (it.value is MiniCartItem.MiniCartItemParentProduct) it.value as MiniCartItem.MiniCartItemParentProduct else null
        }
//        val miniCartItemsVariantGroup = miniCartItemsVariant.groupBy { it.productParentId }

        miniCartItemsVariant.forEach { miniCartItemGroup ->
            val totalQuantity = miniCartItemGroup.totalQuantity
            val productItem = repurchaseProductList.find {
                it.parentId == miniCartItemGroup.parentId
            } ?: return@forEach
            val parentProductId = productItem.productId
            val productId = productItem.productId

            val reason = createInvalidVariantQtyReason(productId, parentProductId)
            assertThat(reason, productItem.product.variant!!.quantity, shouldBe(totalQuantity))
        }
    }

    private fun createInvalidVariantQtyReason(productId: String, parentProductId: String) =
        "Product \"$productId\" with parent \"$parentProductId\" variant quantity is invalid"

    private fun `Then assert repurchase widget visitable is updated`(
        visitableList: List<Visitable<*>>,
    ) {
        val updatedVisitableList = tokoNowCategoryViewModel.updatedVisitableIndicesLiveData.value!!
        val repurchaseWidgetIndex = visitableList.indexOfFirst {
            it is TokoNowRepurchaseUiModel
        }

        assertThat(updatedVisitableList, hasItem(repurchaseWidgetIndex))
    }

    @Test
    fun `add to cart repurchase widget product`() {
        val addToCartTestHelper = AddToCartNonVariantTestHelper(
            tokoNowCategoryViewModel,
            addToCartUseCase,
            updateCartUseCase,
            deleteCartUseCase,
            getMiniCartListSimplifiedUseCase,
            userSession,
            object : AddToCartNonVariantTestHelper.Callback {
                override fun `Given first page API will be successful`() { }
            },
        )

        addToCartTestHelper.`add to cart repurchase widget product`()
    }

    private fun AddToCartNonVariantTestHelper.`add to cart repurchase widget product`() {
        val categoryModel = "category/repurchasewidget/repurchase-widget.json"
            .jsonToObject<CategoryModel>()

        `Given get category first page use case will be successful`(categoryModel)
        `Given view already created`()
        `Given add to cart API will success`(addToCartSuccessModel)

        val visitableList = tokoNowCategoryViewModel.visitableListLiveData.value!!
        val repurchaseWidget = visitableList.getRepurchaseWidgetUiModel()
        val repurchaseWidgetProduct = repurchaseWidget.productList.find {
            it.productId == PRODUCT_ID_NON_VARIANT_ATC
        }!!

        `When add to cart repurchase product`(repurchaseWidgetProduct, addToCartQty)

        `Then assert cart message event`(
            expectedSuccessMessage = errorMessage.joinToString(separator = ", ")
        )
        `Then assert repurchase product quantity`(repurchaseWidgetProduct, addToCartQty)
        `Then verify mini cart is refreshed`()
        `Then verify add to cart tracking repurchase product`(
            repurchaseWidgetProduct,
            addToCartQty,
            cartId,
        )
        `Then assert update toolbar notification true`()
    }

    private fun `When add to cart repurchase product`(
        repurchaseWidgetProduct: TokoNowProductCardUiModel,
        quantity: Int,
    ) {
        tokoNowCategoryViewModel.onViewATCRepurchaseWidget(repurchaseWidgetProduct, quantity)
    }

    private fun `Then assert repurchase product quantity`(
        repurchaseProductCardUiModel: TokoNowProductCardUiModel,
        expectedQuantity: Int,
    ) {
        assertThat(
            repurchaseProductCardUiModel.product.nonVariant?.quantity,
            shouldBe(expectedQuantity)
        )
    }

    private fun `Then verify add to cart tracking repurchase product`(
        repurchaseWidgetProduct: TokoNowProductCardUiModel,
        addToCartQty: Int,
        cartId: String,
    ) {
        val addToCartEvent = tokoNowCategoryViewModel.addToCartRepurchaseWidgetTrackingLiveData.value!!

        val (actualQuantity, actualCartId, actualRepurchaseProduct) = addToCartEvent
        assertThat(actualQuantity, shouldBe(addToCartQty))
        assertThat(actualCartId, shouldBe(cartId))
        assertThat(actualRepurchaseProduct, shouldBe(repurchaseWidgetProduct))
    }

    @Test
    fun `add to cart repurchase widget product failed`() {
        val addToCartTestHelper = AddToCartNonVariantTestHelper(
            tokoNowCategoryViewModel,
            addToCartUseCase,
            updateCartUseCase,
            deleteCartUseCase,
            getMiniCartListSimplifiedUseCase,
            userSession,
            object : AddToCartNonVariantTestHelper.Callback {
                override fun `Given first page API will be successful`() { }
            },
        )

        addToCartTestHelper.`add to cart repurchase widget product failed`()
    }

    private fun AddToCartNonVariantTestHelper.`add to cart repurchase widget product failed`() {
        val categoryModel = "category/repurchasewidget/repurchase-widget.json"
            .jsonToObject<CategoryModel>()

        println("CategoryModel: $categoryModel")
        `Given get category first page use case will be successful`(categoryModel)
        `Given view already created`()
        `Given add to cart API will fail`(responseErrorException)

        val visitableList = tokoNowCategoryViewModel.visitableListLiveData.value!!
        println("VISITABLES: $visitableList")
        val repurchaseWidget = visitableList.getRepurchaseWidgetUiModel()
        val repurchaseWidgetProduct = repurchaseWidget.productList.find {
            it.productId == PRODUCT_ID_NON_VARIANT_ATC
        }!!

        `When add to cart repurchase product`(repurchaseWidgetProduct, addToCartQty)

        `Then assert cart failed message event`(expectedErrorMessage = responseErrorException.message!!)
        `Then assert repurchase product quantity`(repurchaseWidgetProduct, 0)
    }

    @Test
    fun `update repurchase widget product`() {
        val addToCartTestHelper = AddToCartNonVariantTestHelper(
            tokoNowCategoryViewModel,
            addToCartUseCase,
            updateCartUseCase,
            deleteCartUseCase,
            getMiniCartListSimplifiedUseCase,
            userSession,
            object : AddToCartNonVariantTestHelper.Callback {
                override fun `Given first page API will be successful`() {
                    val categoryModel = "category/repurchasewidget/repurchase-widget.json"
                        .jsonToObject<CategoryModel>()

                    `Given get category first page use case will be successful`(categoryModel)
                }
            },
        )

        addToCartTestHelper.`update repurchase widget product`()
    }

    private fun AddToCartNonVariantTestHelper.`update repurchase widget product`() {
        val productIdToATC = PRODUCT_ID_NON_VARIANT_ATC
        val productInMiniCart = SearchCategoryDummyUtils.miniCartItems.getMiniCartItemProduct(productIdToATC)!!
        val productUpdatedQuantity = productInMiniCart.quantity - 3

        `Given view setup to update quantity`(productIdToATC, productUpdatedQuantity)

        val visitableList = tokoNowCategoryViewModel.visitableListLiveData.value!!
        val repurchaseWidget = visitableList.getRepurchaseWidgetUiModel()
        val repurchaseWidgetProduct = repurchaseWidget.productList.find {
            it.productId == PRODUCT_ID_NON_VARIANT_ATC
        }!!

        `When add to cart repurchase product`(repurchaseWidgetProduct, productUpdatedQuantity)

        `Then assert repurchase product quantity`(repurchaseWidgetProduct, productUpdatedQuantity)
        `Then verify mini cart is refreshed`(2)
        `Then assert update toolbar notification true`()
    }

    @Test
    fun `delete cart repurchase widget product`() {
        val addToCartTestHelper = AddToCartNonVariantTestHelper(
            tokoNowCategoryViewModel,
            addToCartUseCase,
            updateCartUseCase,
            deleteCartUseCase,
            getMiniCartListSimplifiedUseCase,
            userSession,
            object : AddToCartNonVariantTestHelper.Callback {
                override fun `Given first page API will be successful`() {
                    val categoryModel = "category/repurchasewidget/repurchase-widget.json"
                        .jsonToObject<CategoryModel>()

                    `Given get category first page use case will be successful`(categoryModel)
                }
            },
        )

        addToCartTestHelper.`delete cart repurchase widget product`()
    }

    private fun AddToCartNonVariantTestHelper.`delete cart repurchase widget product`() {
        val productId = PRODUCT_ID_NON_VARIANT_ATC
        `Given delete cart use case will be successful`(deleteCartResponse)
        `Given view setup to delete`(productId)

        val visitableList = tokoNowCategoryViewModel.visitableListLiveData.value!!
        val repurchaseWidget = visitableList.getRepurchaseWidgetUiModel()
        val repurchaseWidgetProduct = repurchaseWidget.productList.find {
            it.productId == PRODUCT_ID_NON_VARIANT_ATC
        }!!

        `When add to cart repurchase product`(repurchaseWidgetProduct, 0)

        `Then assert repurchase product quantity`(repurchaseWidgetProduct, 0)
        `Then assert remove from cart message event`(expectedSuccessMessage = deleteCartMessage)
        `Then verify mini cart is refreshed`(2)
        `Then assert update toolbar notification true`()
    }

    @Test
    fun `add to cart repurchase widget product non login`() {
        val categoryModel = "category/repurchasewidget/repurchase-widget.json"
            .jsonToObject<CategoryModel>()

        `Given user not logged in`()
        `Given get category first page use case will be successful`(categoryModel)
        `Given view already created`()

        val visitableList = tokoNowCategoryViewModel.visitableListLiveData.value!!
        val repurchaseWidget = visitableList.getRepurchaseWidgetUiModel()
        val repurchaseWidgetProduct = repurchaseWidget.productList.find {
            it.productId == PRODUCT_ID_NON_VARIANT_ATC
        }!!

        `When add to cart repurchase product`(repurchaseWidgetProduct, addToCartQty)

        `Then assert route to login page`()
        `Then assert repurchase widget visitable is updated`(visitableList)
    }

    private fun `Given user not logged in`() {
        every { userSession.isLoggedIn } returns false
    }

    private fun `Then assert route to login page`() {
        assertThat(
            tokoNowCategoryViewModel.routeApplinkLiveData.value!!,
            shouldBe(ApplinkConst.LOGIN)
        )
    }
}
