package com.tokopedia.tokopedianow.oldcategory.presentation.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokopedianow.common.domain.mapper.TickerMapper
import com.tokopedia.tokopedianow.common.model.TokoNowProductCardUiModel
import com.tokopedia.tokopedianow.common.model.oldrepurchase.TokoNowRepurchaseUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowTickerUiModel
import com.tokopedia.tokopedianow.oldcategory.domain.model.CategoryModel
import com.tokopedia.tokopedianow.searchcategory.AddToCartNonVariantTestHelper
import com.tokopedia.tokopedianow.searchcategory.jsonToObject
import com.tokopedia.tokopedianow.util.TestUtils.getParentPrivateField
import org.hamcrest.CoreMatchers
import org.junit.Assert
import org.junit.Test

class CategoryTickerTest: CategoryTestFixtures() {

    private lateinit var addToCartTestHelper: AddToCartNonVariantTestHelper

    override fun setUp() {
        super.setUp()

        addToCartTestHelper = AddToCartNonVariantTestHelper(
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
    }

    @Test
    fun `add to cart product of repurchase widget should be failed because hasBlockedAddToCart is true`() {
        addToCartTestHelper.`add to cart repurchase widget when hasBlockedAddToCart is true`()
    }

    @Test
    fun `add to cart product of repurchase widget should be success because hasBlockedAddToCart is false`() {
        addToCartTestHelper.`add to cart repurchase widget when hasBlockedAddToCart is false`()
    }

    private fun AddToCartNonVariantTestHelper.`add to cart repurchase widget when hasBlockedAddToCart is true`() {
        val categoryModel = "oldcategory/repurchasewidget/repurchase-widget-block-add-to-cart.json"
            .jsonToObject<CategoryModel>()

        `Given get category first page use case will be successful`(categoryModel)
        `Given view already created`()
        `Given add to cart API will success`(AddToCartNonVariantTestHelper.Companion.AddToCartTestObject.addToCartSuccessModel)

        val visitableList = tokoNowCategoryViewModel.visitableListLiveData.value!!

        val repurchaseWidget = visitableList.getRepurchaseWidgetUiModel()
        val tickerWidget = visitableList.getTicketWidgetUiModel()

        val repurchaseWidgetProduct = repurchaseWidget.productList.find {
            it.productId == AddToCartNonVariantTestHelper.PRODUCT_ID_NON_VARIANT_ATC
        }!!

        `When add to cart repurchase product`(repurchaseWidgetProduct, 3)

        `Then assert ticker data`(categoryModel, tickerWidget)
        `Then assert hasBlockedAddToCart`(true)
    }

    private fun AddToCartNonVariantTestHelper.`add to cart repurchase widget when hasBlockedAddToCart is false`() {
        val categoryModel = "oldcategory/repurchasewidget/repurchase-widget-not-block-add-to-cart.json"
            .jsonToObject<CategoryModel>()

        `Given get category first page use case will be successful`(categoryModel)
        `Given view already created`()
        `Given add to cart API will success`(AddToCartNonVariantTestHelper.Companion.AddToCartTestObject.addToCartSuccessModel)

        val visitableList = tokoNowCategoryViewModel.visitableListLiveData.value!!
        val repurchaseWidget = visitableList.getRepurchaseWidgetUiModel()
        val tickerWidget = visitableList.getTicketWidgetUiModel()

        val repurchaseWidgetProduct = repurchaseWidget.productList.find {
            it.productId == AddToCartNonVariantTestHelper.PRODUCT_ID_NON_VARIANT_ATC
        }!!

        `When add to cart repurchase product`(repurchaseWidgetProduct,
            AddToCartNonVariantTestHelper.Companion.AddToCartTestObject.addToCartQty
        )

        `Then assert cart message event`(
            expectedSuccessMessage = AddToCartNonVariantTestHelper.Companion.AddToCartTestObject.errorMessage.joinToString(separator = ", ")
        )
        `Then assert repurchase product quantity`(repurchaseWidgetProduct,
            AddToCartNonVariantTestHelper.Companion.AddToCartTestObject.addToCartQty
        )
        `Then verify mini cart is refreshed`()
        `Then verify add to cart tracking repurchase product`(
            repurchaseWidgetProduct,
            AddToCartNonVariantTestHelper.Companion.AddToCartTestObject.addToCartQty,
            AddToCartNonVariantTestHelper.Companion.AddToCartTestObject.cartId,
        )
        `Then assert update toolbar notification true`()
        `Then assert ticker data`(categoryModel, tickerWidget)
        `Then assert hasBlockedAddToCart`(false)
    }

    private fun List<Visitable<*>>.getRepurchaseWidgetUiModel() = find { it is TokoNowRepurchaseUiModel } as TokoNowRepurchaseUiModel

    private fun List<Visitable<*>>.getTicketWidgetUiModel() = find { it is TokoNowTickerUiModel } as TokoNowTickerUiModel

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
        Assert.assertThat(
            repurchaseProductCardUiModel.product.nonVariant?.quantity,
            CoreMatchers.`is`(expectedQuantity)
        )
    }

    private fun `Then verify add to cart tracking repurchase product`(
        repurchaseWidgetProduct: TokoNowProductCardUiModel,
        addToCartQty: Int,
        cartId: String,
    ) {
        val addToCartEvent = tokoNowCategoryViewModel.addToCartRepurchaseWidgetTrackingLiveData.value!!

        val (actualQuantity, actualCartId, actualRepurchaseProduct) = addToCartEvent
        Assert.assertThat(actualQuantity, CoreMatchers.`is`(addToCartQty))
        Assert.assertThat(actualCartId, CoreMatchers.`is`(cartId))
        Assert.assertThat(actualRepurchaseProduct, CoreMatchers.`is`(repurchaseWidgetProduct))
    }

    private fun `Then assert ticker data`(categoryModel: CategoryModel, tickerUiModel: TokoNowTickerUiModel) {
        Assert.assertEquals(TickerMapper.mapTickerData(categoryModel.targetedTicker).second, tickerUiModel.tickers)
    }

    private fun `Then assert hasBlockedAddToCart`(expected: Boolean) {
        val hasBlockedAddToCart = tokoNowCategoryViewModel.getParentPrivateField<Boolean>("hasBlockedAddToCart")
        Assert.assertEquals(expected, hasBlockedAddToCart)
        if (expected) {
            Assert.assertTrue(tokoNowCategoryViewModel.blockAddToCartLiveData.value != null)
        } else {
            Assert.assertTrue(tokoNowCategoryViewModel.blockAddToCartLiveData.value == null)
        }
    }
}
