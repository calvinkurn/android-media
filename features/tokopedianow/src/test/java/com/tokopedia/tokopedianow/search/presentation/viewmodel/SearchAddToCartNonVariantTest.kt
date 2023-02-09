package com.tokopedia.tokopedianow.search.presentation.viewmodel

import com.tokopedia.minicart.common.domain.data.getMiniCartItemProduct
import com.tokopedia.productcard_compact.productcardcarousel.presentation.uimodel.ProductCardCompactCarouselItemUiModel
import com.tokopedia.tokopedianow.search.domain.model.SearchModel
import com.tokopedia.tokopedianow.search.presentation.model.BroadMatchDataView
import com.tokopedia.tokopedianow.searchcategory.AddToCartNonVariantTestHelper
import com.tokopedia.tokopedianow.searchcategory.AddToCartNonVariantTestHelper.Callback
import com.tokopedia.tokopedianow.searchcategory.AddToCartNonVariantTestHelper.Companion.AddToCartTestObject.addToCartQty
import com.tokopedia.tokopedianow.searchcategory.AddToCartNonVariantTestHelper.Companion.AddToCartTestObject.addToCartSuccessModel
import com.tokopedia.tokopedianow.searchcategory.AddToCartNonVariantTestHelper.Companion.AddToCartTestObject.cartId
import com.tokopedia.tokopedianow.searchcategory.AddToCartNonVariantTestHelper.Companion.AddToCartTestObject.errorMessage
import com.tokopedia.tokopedianow.searchcategory.AddToCartNonVariantTestHelper.Companion.DeleteCartTestObject.deleteCartMessage
import com.tokopedia.tokopedianow.searchcategory.AddToCartNonVariantTestHelper.Companion.DeleteCartTestObject.deleteCartResponse
import com.tokopedia.tokopedianow.searchcategory.AddToCartNonVariantTestHelper.Companion.PRODUCT_ID_NON_VARIANT_ATC
import com.tokopedia.tokopedianow.searchcategory.jsonToObject
import com.tokopedia.tokopedianow.util.SearchCategoryDummyUtils.miniCartItems
import com.tokopedia.tokopedianow.util.SearchCategoryDummyUtils.miniCartSimplifiedData
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.hamcrest.CoreMatchers.`is` as shouldBe

class SearchAddToCartNonVariantTest: SearchTestFixtures(), Callback {

    private val searchModelJSON = "search/first-page-products-variant-and-non-variant.json"
    private val searchModel = searchModelJSON.jsonToObject<SearchModel>()

    private lateinit var addToCartTestHelper: AddToCartNonVariantTestHelper

    override fun setUp() {
        super.setUp()

        addToCartTestHelper = AddToCartNonVariantTestHelper(
                tokoNowSearchViewModel,
                addToCartUseCase,
                updateCartUseCase,
                deleteCartUseCase,
                getMiniCartListSimplifiedUseCase,
                userSession,
                this,
        )
    }

    override fun `Given first page API will be successful`() {
        `Given get search first page use case will be successful`(searchModel)
    }

    @Test
    fun `test add to cart success`() {
        addToCartTestHelper.`test add to cart success`()
    }

    @Test
    fun `test add to cart failed`() {
        addToCartTestHelper.`test add to cart failed`()
    }

    @Test
    fun `add to cart with current quantity should do nothing`() {
        addToCartTestHelper.`add to cart with current quantity should do nothing`()
    }

    @Test
    fun `add to cart to increase quantity success`() {
        addToCartTestHelper.`add to cart to increase quantity success`()
    }

    @Test
    fun `add to cart to decrease quantity success`() {
        addToCartTestHelper.`add to cart to decrease quantity success`()
    }

    @Test
    fun `add to cart to update quantity failed`() {
        addToCartTestHelper.`add to cart to update quantity failed`()
    }

    @Test
    fun `test ATC non login should redirect to cart page`() {
        addToCartTestHelper.`test ATC non login should redirect to login page`()
    }

    @Test
    fun `delete cart success`() {
        addToCartTestHelper.`delete cart success`()
    }

    @Test
    fun `delete cart failed`() {
        addToCartTestHelper.`delete cart failed`()
    }

    @Test
    fun `add to cart non login broad match item`() {
        addToCartTestHelper.run {
            val searchModel = "search/broadmatch/broadmatch-no-result.json".jsonToObject<SearchModel>()
            `Given get search first page use case will be successful`(searchModel)
            `Given user not logged in`()
            `Given view already created`()

            val (broadMatchItem, broadMatchIndex) = getBroadMatchItemForATC(PRODUCT_ID_NON_VARIANT_ATC)
            `When add to cart broad match item`(broadMatchItem, addToCartQty, broadMatchIndex)

            `Then assert route to login page event`()
            `Then assert cart use cases are not called`()
            `Then assert visitable list is updated to revert add to cart button`(broadMatchIndex)
        }
    }

    private fun getBroadMatchItemForATC(productId: String): Pair<ProductCardCompactCarouselItemUiModel, Int> {
        val visitableList = tokoNowSearchViewModel.visitableListLiveData.value!!
        val broadMatchDataView = visitableList.filterIsInstance<BroadMatchDataView>()
        val broadMatchItemList = broadMatchDataView.flatMap { it.broadMatchItemModelList }
        val broadMatchItem = broadMatchItemList.find { it.productCardModel.productId == productId }!!
        val broadMatchIndex = 3

        return Pair(broadMatchItem, broadMatchIndex)
    }

    private fun `When add to cart broad match item`(
        broadMatchItem: ProductCardCompactCarouselItemUiModel,
        quantity: Int,
        broadMatchIndex: Int,
    ) {
        tokoNowSearchViewModel.onViewATCBroadMatchItem(broadMatchItem, quantity, broadMatchIndex)
    }

    @Test
    fun `add to cart broad match item success`() {
        addToCartTestHelper.run {
            val searchModel = "search/broadmatch/broadmatch-no-result.json".jsonToObject<SearchModel>()
            `Given get search first page use case will be successful`(searchModel)
            `Given view already created`()
            `Given add to cart API will success`(addToCartSuccessModel)

            val (broadMatchItem, broadMatchIndex) = getBroadMatchItemForATC(PRODUCT_ID_NON_VARIANT_ATC)
            `When add to cart broad match item`(broadMatchItem, addToCartQty, broadMatchIndex)

            val productId = broadMatchItem.productCardModel.productId
            val shopId = broadMatchItem.shopId
            `Then assert add to cart request params`(productId, shopId, addToCartQty)
            `Then assert cart message event`(
                expectedSuccessMessage = errorMessage.joinToString(separator = ", ")
            )
            `Then verify mini cart is refreshed`()
            `Then assert add to cart tracking broad match item`(addToCartQty, cartId, broadMatchItem)
            `Then assert route to login page event is null`()
        }
    }

    private fun `Then assert broad match item quantity`(
        broadMatchItem: ProductCardCompactCarouselItemUiModel,
        expectedQuantity: Int
    ) {
        assertThat(broadMatchItem.productCardModel.orderQuantity, shouldBe(expectedQuantity))
    }

    private fun `Then assert add to cart tracking broad match item`(
        quantity: Int,
        cartId: String,
        item: ProductCardCompactCarouselItemUiModel,
    ) {
        val addToCartEvent = tokoNowSearchViewModel.addToCartBroadMatchTrackingLiveData.value!!

        val (actualQuantity, actualCartId, actualProductItem) = addToCartEvent
        assertThat(actualQuantity, shouldBe(quantity))
        assertThat(actualCartId, shouldBe(cartId))
        assertThat(actualProductItem, shouldBe(item))
    }

    @Test
    fun `add to cart broad match item failed`() {
        addToCartTestHelper.run {
            val searchModel = "search/broadmatch/broadmatch-no-result.json".jsonToObject<SearchModel>()
            `Given get search first page use case will be successful`(searchModel)
            `Given view already created`()
            `Given add to cart API will fail`(responseErrorException)

            val (broadMatchItem, broadMatchIndex) = getBroadMatchItemForATC(PRODUCT_ID_NON_VARIANT_ATC)
            `When add to cart broad match item`(broadMatchItem, addToCartQty, broadMatchIndex)

            val productId = broadMatchItem.productCardModel.productId
            val shopId = broadMatchItem.shopId
            `Then assert add to cart request params`(productId, shopId, addToCartQty)
            `Then assert cart failed message event`(responseErrorException.message!!)
            `Then verify mini cart is refreshed`(0)
            `Then assert route to login page event is null`()
        }
    }

    @Test
    fun `add to cart broad match item with current quantity should do nothing`() {
        addToCartTestHelper.run {
            val searchModel = "search/broadmatch/broadmatch-no-result.json".jsonToObject<SearchModel>()
            `Given get search first page use case will be successful`(searchModel)
            `Given get mini cart simplified use case will be successful`(miniCartSimplifiedData)
            `Given view already created`()
            `Given view resumed to update mini cart`()

            val (broadMatchItem, broadMatchIndex) = getBroadMatchItemForATC(PRODUCT_ID_NON_VARIANT_ATC)
            val quantity = miniCartItems.getMiniCartItemProduct(broadMatchItem.productCardModel.productId)!!.quantity
            `When add to cart broad match item`(broadMatchItem, quantity, broadMatchIndex)

            `Then assert add to cart use case is not called`()
            `Then assert route to login page event is null`()
        }
    }

    @Test
    fun `update quantity broad match item success`() {
        addToCartTestHelper.run {
            val productIdToATC = PRODUCT_ID_NON_VARIANT_ATC
            val broadMatchItemInMiniCart = miniCartItems.getMiniCartItemProduct(productIdToATC)!!
            val updatedQuantity = broadMatchItemInMiniCart.quantity - 3

            `Given view setup to update quantity broad match item`(productIdToATC, updatedQuantity)

            val (broadMatchItem, broadMatchIndex) = getBroadMatchItemForATC(productIdToATC)
            `When add to cart broad match item`(broadMatchItem, updatedQuantity, broadMatchIndex)

            `Then assert update quantity broad match item`(
                updatedQuantity,
                broadMatchItemInMiniCart.cartId,
                broadMatchItem
            )
        }
    }

    private fun AddToCartNonVariantTestHelper.`Given view setup to update quantity broad match item`(
        productId: String,
        updatedQuantity: Int,
    ) {
        val searchModel = "search/broadmatch/broadmatch-no-result.json".jsonToObject<SearchModel>()
        `Given get search first page use case will be successful`(searchModel)
        `Given get mini cart simplified use case will be successful`(miniCartSimplifiedData)
        `Given update cart use case will be successful`(AddToCartNonVariantTestHelper.Companion.UpdateCartTestObject.successUpdateCartResponse)
        `Given view already created`()
        `Given view resumed to update mini cart`()

        val updatedMiniCartData = miniCartSimplifiedData
        updateMiniCartData(updatedMiniCartData, productId, updatedQuantity)
        `Given get mini cart simplified use case will be successful`(updatedMiniCartData)
    }

    private fun AddToCartNonVariantTestHelper.`Then assert update quantity broad match item`(
        updateQuantityParam: Int,
        cartIdParam: String,
        broadMatchItem: ProductCardCompactCarouselItemUiModel,
        expectedErrorMessage: String = "",
        expectedProductQuantity: Int = updateQuantityParam,
        expectedRefreshMiniCartCount: Int = 2,
    ) {
        `Then assert update cart params`(updateQuantityParam, cartIdParam)
        `Then assert cart message event`("")
        `Then assert cart failed message event`(expectedErrorMessage)
        `Then assert broad match item quantity`(broadMatchItem, expectedProductQuantity)
        `Then assert add to cart use case is not called`()
        `Then verify mini cart is refreshed`(expectedRefreshMiniCartCount)
        `Then assert route to login page event is null`()
    }

    @Test
    fun `update quantity broad match item failed`() {
        addToCartTestHelper.run {
            val productIdToATC = PRODUCT_ID_NON_VARIANT_ATC
            val broadMatchItemInMiniCart = miniCartItems.getMiniCartItemProduct(productIdToATC)!!
            val updatedQuantity = broadMatchItemInMiniCart.quantity - 3

            val searchModel = "search/broadmatch/broadmatch-no-result.json".jsonToObject<SearchModel>()
            `Given get search first page use case will be successful`(searchModel)
            `Given get mini cart simplified use case will be successful`(miniCartSimplifiedData)
            `Given update cart use case will fail`()
            `Given view already created`()
            `Given view resumed to update mini cart`()

            val (broadMatchItem, broadMatchIndex) = getBroadMatchItemForATC(productIdToATC)
            `When add to cart broad match item`(broadMatchItem, updatedQuantity, broadMatchIndex)

            val expectedErrorMessage = responseErrorException.message!!
            val expectedProductQuantity = broadMatchItemInMiniCart.quantity
            val expectedRefreshMiniCartCount = 1
            `Then assert update quantity broad match item`(
                updatedQuantity,
                broadMatchItemInMiniCart.cartId,
                broadMatchItem,
                expectedErrorMessage,
                expectedProductQuantity,
                expectedRefreshMiniCartCount,
            )
        }
    }

    @Test
    fun `delete cart broad match item success`() {
        addToCartTestHelper.run {
            val productIdToDelete = PRODUCT_ID_NON_VARIANT_ATC
            val broadMatchItemInMiniCart = miniCartItems.getMiniCartItemProduct(productIdToDelete)!!
            `Given delete cart use case will be successful`(deleteCartResponse)
            `Given view setup to delete broad match item`(productIdToDelete)

            val (broadMatchItem, broadMatchIndex) = getBroadMatchItemForATC(productIdToDelete)
            `When add to cart broad match item`(broadMatchItem, 0, broadMatchIndex)

            `Then assert delete cart broad match item behavior`(
                broadMatchItemInMiniCart.cartId,
                expectedSuccessDeleteCartMessage = deleteCartMessage,
            )
        }
    }

    private fun AddToCartNonVariantTestHelper.`Given view setup to delete broad match item`(
        productIdToDelete: String,
    ) {
        val searchModel = "search/broadmatch/broadmatch-no-result.json".jsonToObject<SearchModel>()
        `Given get search first page use case will be successful`(searchModel)
        `Given get mini cart simplified use case will be successful`(miniCartSimplifiedData)
        `Given view already created`()
        `Given view resumed to update mini cart`()

        val updatedMiniCartData = miniCartSimplifiedData
        updateMiniCartData(updatedMiniCartData, productIdToDelete, 0)
        `Given get mini cart simplified use case will be successful`(updatedMiniCartData)
    }

    private fun AddToCartNonVariantTestHelper.`Then assert delete cart broad match item behavior`(
        cartIdParam: String,
        expectedSuccessDeleteCartMessage: String = "",
        expectedFailedDeleteCartMessage: String = "",
        expectedRefreshMiniCartCount: Int = 2,
    ) {
        `Then assert delete cart params`(cartIdParam)
        `Then assert remove from cart message event`(expectedSuccessDeleteCartMessage)
        `Then assert cart failed message event`(expectedFailedDeleteCartMessage)
        `Then assert add to cart use case is not called`()
        `Then assert update cart use case is not called`()
        `Then verify mini cart is refreshed`(expectedRefreshMiniCartCount)
        `Then assert route to login page event is null`()
    }

    @Test
    fun `delete cart broad match item failed`() {
        addToCartTestHelper.run {
            val productIdToDelete = PRODUCT_ID_NON_VARIANT_ATC
            val broadMatchItemInMiniCart = miniCartItems.getMiniCartItemProduct(productIdToDelete)!!
            `Given delete cart use case will fail`()
            `Given view setup to delete broad match item`(productIdToDelete)

            val (broadMatchItem, broadMatchIndex) = getBroadMatchItemForATC(productIdToDelete)
            `When add to cart broad match item`(broadMatchItem, 0, broadMatchIndex)

            `Then assert delete cart broad match item behavior`(
                broadMatchItemInMiniCart.cartId,
                expectedFailedDeleteCartMessage = responseErrorException.message!!,
                expectedRefreshMiniCartCount = 1
            )
        }
    }
}
