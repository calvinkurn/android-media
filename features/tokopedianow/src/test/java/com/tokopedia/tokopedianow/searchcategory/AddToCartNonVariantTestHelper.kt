package com.tokopedia.tokopedianow.searchcategory

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.atc_common.data.model.request.AddToCartRequestParams
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.model.response.DataModel
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartUseCase
import com.tokopedia.cartcommon.data.request.updatecart.UpdateCartRequest
import com.tokopedia.cartcommon.data.response.deletecart.RemoveFromCartData
import com.tokopedia.cartcommon.data.response.updatecart.Data
import com.tokopedia.cartcommon.data.response.updatecart.UpdateCartV2Data
import com.tokopedia.cartcommon.domain.usecase.DeleteCartUseCase
import com.tokopedia.cartcommon.domain.usecase.UpdateCartUseCase
import com.tokopedia.minicart.common.domain.data.MiniCartItem
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.minicart.common.domain.data.getMiniCartItemProduct
import com.tokopedia.minicart.common.domain.usecase.GetMiniCartListSimplifiedUseCase
import com.tokopedia.network.exception.ResponseErrorException
import com.tokopedia.tokopedianow.searchcategory.AddToCartNonVariantTestHelper.Companion.AddToCartTestObject.addToCartQty
import com.tokopedia.tokopedianow.searchcategory.AddToCartNonVariantTestHelper.Companion.AddToCartTestObject.addToCartSuccessModel
import com.tokopedia.tokopedianow.searchcategory.AddToCartNonVariantTestHelper.Companion.AddToCartTestObject.cartId
import com.tokopedia.tokopedianow.searchcategory.AddToCartNonVariantTestHelper.Companion.AddToCartTestObject.errorMessage
import com.tokopedia.tokopedianow.searchcategory.AddToCartNonVariantTestHelper.Companion.DeleteCartTestObject.deleteCartMessage
import com.tokopedia.tokopedianow.searchcategory.AddToCartNonVariantTestHelper.Companion.DeleteCartTestObject.deleteCartResponse
import com.tokopedia.tokopedianow.searchcategory.AddToCartNonVariantTestHelper.Companion.UpdateCartTestObject.successUpdateCartResponse
import com.tokopedia.tokopedianow.searchcategory.presentation.model.ProductItemDataView
import com.tokopedia.tokopedianow.searchcategory.presentation.viewmodel.BaseSearchCategoryViewModel
import com.tokopedia.tokopedianow.util.SearchCategoryDummyUtils.miniCartItems
import com.tokopedia.tokopedianow.util.SearchCategoryDummyUtils.miniCartSimplifiedData
import com.tokopedia.unit.test.ext.verifyValueEquals
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.every
import io.mockk.slot
import io.mockk.verify
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsNull.nullValue
import com.tokopedia.cartcommon.data.response.deletecart.Data as DeleteCartData
import org.hamcrest.CoreMatchers.`is` as shouldBe

class AddToCartNonVariantTestHelper(
        private val baseViewModel: BaseSearchCategoryViewModel,
        private val addToCartUseCase: AddToCartUseCase,
        private val updateCartUseCase: UpdateCartUseCase,
        private val deleteCartUseCase: DeleteCartUseCase,
        private val getMiniCartListSimplifiedUseCase: GetMiniCartListSimplifiedUseCase,
        private val userSession: UserSessionInterface,
        private val callback: Callback,
) {

    private val addToCartRequestParamsSlot = slot<AddToCartRequestParams>()
    private val addToCartRequestParams by lazy { addToCartRequestParamsSlot.captured }
    val responseErrorException = ResponseErrorException(
            "Jumlah barang melebihi stok di toko. Kurangi pembelianmu, ya!"
    )

    fun `test add to cart success`() {
        callback.`Given first page API will be successful`()
        `Given view already created`()
        `Given add to cart API will success`(addToCartSuccessModel)

        val productItemList = baseViewModel.visitableListLiveData.value!!.getProductItemList()
        val productItemDataViewToATC = productItemList[0]

        `When handle cart event product non variant`(productItemDataViewToATC, addToCartQty)

        val productId = productItemDataViewToATC.productCardModel.productId
        val shopId = productItemDataViewToATC.shop.id
        `Then assert add to cart request params`(productId, shopId, addToCartQty)
        `Then assert cart message event`(
                expectedSuccessMessage = errorMessage.joinToString(separator = ", ")
        )
        `Then assert product item quantity`(productItemDataViewToATC, addToCartQty)
        `Then verify mini cart is refreshed`()
        `Then verify add to cart tracking is called`(addToCartQty, cartId, productItemDataViewToATC)
        `Then assert route to login page event is null`()
        `Then assert update toolbar notification true`()
    }

    fun `Given view already created`() {
        baseViewModel.onViewCreated()
    }

    fun `Given add to cart API will success`(addToCartSuccessModel: AddToCartDataModel) {
        every {
            addToCartUseCase.execute(any(), any())
        } answers {
            firstArg<(AddToCartDataModel) -> Unit>().invoke(addToCartSuccessModel)
        }
    }

    private fun List<Visitable<*>>.getProductItemList() = filterIsInstance<ProductItemDataView>()

    private fun `When handle cart event product non variant`(
            productItemToATC: ProductItemDataView,
            addToCartQty: Int,
    ) {
        baseViewModel.onViewATCProductNonVariant(productItemToATC, addToCartQty)
    }

    fun `Then assert add to cart request params`(
            productId: String,
            shopId: String,
            addToCartQty: Int,
    ) {
        verify {
            addToCartUseCase.setParams(capture(addToCartRequestParamsSlot))
        }

        assertThat(addToCartRequestParams.productId.toString(), shouldBe(productId))
        assertThat(addToCartRequestParams.shopId.toString(), shouldBe(shopId))
        assertThat(addToCartRequestParams.quantity, shouldBe(addToCartQty))
    }

    fun `Then assert cart message event`(
        expectedSuccessMessage: String = ""
    ) {
        val successMessageLiveData = baseViewModel.successAddToCartMessageLiveData.value ?: ""
        assertThat(successMessageLiveData, shouldBe(expectedSuccessMessage))
    }

    fun `Then assert cart failed message event`(
        expectedErrorMessage: String = ""
    ) {
        val errorMessageLiveData = baseViewModel.errorATCMessageLiveData.value ?: ""
        assertThat(errorMessageLiveData, shouldBe(expectedErrorMessage))
    }

    fun `Then assert remove from cart message event`(
        expectedSuccessMessage: String = ""
    ) {
        val successMessageLiveData = baseViewModel.successRemoveFromCartMessageLiveData.value ?: ""
        assertThat(successMessageLiveData, shouldBe(expectedSuccessMessage))
    }

    fun `Then assert update toolbar notification true`() {
        baseViewModel.updateToolbarNotification
            .verifyValueEquals(true)
    }

    private fun `Then assert product item quantity`(
            productItemDataViewToATC: ProductItemDataView,
            expectedQuantity: Int,
    ) {
        val actualQuantity = productItemDataViewToATC.productCardModel.orderQuantity

        assertThat(actualQuantity, shouldBe(expectedQuantity))
    }

    fun `Then verify mini cart is refreshed`(exactly: Int = 1) {
        verify(exactly = exactly) {
            getMiniCartListSimplifiedUseCase.execute(any(), any())
        }
    }

    private fun `Then verify add to cart tracking is called`(
            quantity: Int,
            cartId: String,
            productItem: ProductItemDataView,
    ) {
        val addToCartEvent = baseViewModel.addToCartTrackingLiveData.value!!

        val (actualQuantity, actualCartId, actualProductItem) = addToCartEvent
        assertThat(actualQuantity, shouldBe(quantity))
        assertThat(actualCartId, shouldBe(cartId))
        assertThat(actualProductItem, shouldBe(productItem))
    }

    fun `Then assert route to login page event is null`() {
        assertThat(baseViewModel.routeApplinkLiveData.value, nullValue())
    }

    fun `test add to cart failed`() {
        callback.`Given first page API will be successful`()
        `Given view already created`()
        `Given add to cart API will fail`(responseErrorException)

        val productItemList = baseViewModel.visitableListLiveData.value!!.getProductItemList()
        val productItemDataViewToATC = productItemList[0]
        val addToCartQty = 15

        `When handle cart event product non variant`(productItemDataViewToATC, addToCartQty)

        val productId = productItemDataViewToATC.productCardModel.productId
        val shopId = productItemDataViewToATC.shop.id
        `Then assert add to cart request params`(productId, shopId, addToCartQty)
        `Then assert cart failed message event`(responseErrorException.message!!)
        `Then assert product item quantity`(productItemDataViewToATC, 0)
        `Then verify mini cart is refreshed`(0)
        `Then assert route to login page event is null`()
    }

    fun `Given add to cart API will fail`(throwable: Throwable) {
        every {
            addToCartUseCase.execute(any(), any())
        } answers {
            secondArg<(Throwable) -> Unit>().invoke(throwable)
        }
    }

    fun `add to cart with current quantity should do nothing`() {
        callback.`Given first page API will be successful`()
        `Given get mini cart simplified use case will be successful`(miniCartSimplifiedData)
        `Given view already created`()
        `Given view resumed to update mini cart`()

        val (currentQty, productInVisitable) = getAvailableProductAndQuantity()
        `When handle cart event product non variant`(productInVisitable, currentQty)

        `Then assert add to cart use case is not called`()
        `Then assert route to login page event is null`()
    }

    fun `Given get mini cart simplified use case will be successful`(
            miniCartSimplifiedData: MiniCartSimplifiedData
    ) {
        every {
            getMiniCartListSimplifiedUseCase.execute(any(), any())
        } answers {
            firstArg<(MiniCartSimplifiedData) -> Unit>().invoke(miniCartSimplifiedData)
        }
    }

    fun `Given view resumed to update mini cart`() {
        baseViewModel.onViewResumed()
    }

    private fun getAvailableProductAndQuantity(): Pair<Int, ProductItemDataView> {
        val productItemList = baseViewModel.visitableListLiveData.value!!.getProductItemList()
        val productIdToATC = PRODUCT_ID_NON_VARIANT_ATC
        val productInMiniCart = miniCartItems.getMiniCartItemProduct(productIdToATC)!!

        val productCurrentQuantity = productInMiniCart.quantity
        val productInVisitable = productItemList.find { it.productCardModel.productId == productIdToATC }!!

        return Pair(productCurrentQuantity, productInVisitable)
    }

    fun `Then assert add to cart use case is not called`() {
        verify(exactly = 0) {
            addToCartUseCase.execute(any(), any())
        }
    }

    fun `add to cart to decrease quantity success`() {
        val productIdToATC = PRODUCT_ID_NON_VARIANT_ATC
        val productInMiniCart = miniCartItems.getMiniCartItemProduct(productIdToATC)!!
        val productUpdatedQuantity = productInMiniCart.quantity - 3

        `Given view setup to update quantity`(productIdToATC, productUpdatedQuantity)

        val productItemList = baseViewModel.visitableListLiveData.value!!.getProductItemList()
        val productInVisitable = productItemList.find { it.productCardModel.productId == productIdToATC }!!

        `When handle cart event product non variant`(productInVisitable, productUpdatedQuantity)

        `Then assert update quantity`(
                productUpdatedQuantity,
                productInMiniCart.cartId,
                productInVisitable
        )
        `Then verify decrease cart quantity tracking is called`(productIdToATC)
    }

    fun `Given view setup to update quantity`(
            productId: String,
            updatedQuantity: Int,
    ) {
        callback.`Given first page API will be successful`()
        `Given get mini cart simplified use case will be successful`(miniCartSimplifiedData)
        `Given update cart use case will be successful`(successUpdateCartResponse)
        `Given view already created`()
        `Given view resumed to update mini cart`()

        val updatedMiniCartData = miniCartSimplifiedData
        updateMiniCartData(updatedMiniCartData, productId, updatedQuantity)
        `Given get mini cart simplified use case will be successful`(updatedMiniCartData)
    }

    fun updateMiniCartData(
            updatedMiniCartData: MiniCartSimplifiedData,
            productId: String,
            updatedQuantity: Int
    ) {
        updatedMiniCartData.miniCartItems.values.forEach {
            if (it is MiniCartItem.MiniCartItemProduct && it.productId == productId)
                it.quantity = updatedQuantity
        }
    }

    fun `Given update cart use case will be successful`(
            successUpdateCartResponse: UpdateCartV2Data
    ) {
        every {
            updateCartUseCase.execute(any(), any())
        } answers {
            firstArg<(UpdateCartV2Data) -> Unit>().invoke(successUpdateCartResponse)
        }
    }

    private fun `Then assert update quantity`(
            updateQuantityParam: Int,
            cartIdParam: String,
            productInVisitable: ProductItemDataView,
            expectedCartErrorMessage: String = "",
            expectedProductQuantity: Int = updateQuantityParam,
            expectedRefreshMiniCartCount: Int = 2,
    ) {
        `Then assert update cart params`(updateQuantityParam, cartIdParam)
        `Then assert cart failed message event`(expectedCartErrorMessage)
        `Then assert cart message event`("")
        `Then assert product item quantity`(productInVisitable, expectedProductQuantity)
        `Then assert add to cart use case is not called`()
        `Then verify mini cart is refreshed`(expectedRefreshMiniCartCount)
        `Then assert route to login page event is null`()
    }

    fun `Then assert update cart params`(
            productUpdatedQuantity: Int,
            cartId: String,
    ) {
        val updateCartParamSlot = slot<List<UpdateCartRequest>>()
        val updateCartParam by lazy { updateCartParamSlot.captured }

        verify {
            updateCartUseCase.setParams(
                    updateCartRequestList = capture(updateCartParamSlot),
                    source = UpdateCartUseCase.VALUE_SOURCE_UPDATE_QTY_NOTES,
            )
        }
        val updatedMiniCartItem = updateCartParam[0]
        assertThat(updatedMiniCartItem.quantity, shouldBe(productUpdatedQuantity))
        assertThat(updatedMiniCartItem.cartId, shouldBe(cartId))
    }

    private fun `Then verify decrease cart quantity tracking is called`(productIdToATC: String) {
        val increaseQtyTracking = baseViewModel.increaseQtyTrackingLiveData.value
        assertThat(increaseQtyTracking, nullValue())

        val reduceQtyTracking = baseViewModel.decreaseQtyTrackingLiveData.value!!
        assertThat(reduceQtyTracking, shouldBe(productIdToATC))
    }

    fun `add to cart to increase quantity success`() {
        val productIdToATC = PRODUCT_ID_NON_VARIANT_ATC
        val productInMiniCart = miniCartItems.getMiniCartItemProduct(productIdToATC)!!
        val productUpdatedQuantity = productInMiniCart.quantity + 3

        `Given view setup to update quantity`(productIdToATC, productUpdatedQuantity)

        val productItemList = baseViewModel.visitableListLiveData.value!!.getProductItemList()
        val productInVisitable = productItemList.find { it.productCardModel.productId == productIdToATC }!!

        `When handle cart event product non variant`(productInVisitable, productUpdatedQuantity)

        `Then assert update quantity`(
                productUpdatedQuantity,
                productInMiniCart.cartId,
                productInVisitable,
        )
        `Then verify increase cart quantity tracking is called`(productIdToATC)
        `Then assert update toolbar notification true`()
    }

    private fun `Then verify increase cart quantity tracking is called`(productIdToATC: String) {
        val increaseQtyTracking = baseViewModel.increaseQtyTrackingLiveData.value!!
        assertThat(increaseQtyTracking, shouldBe(productIdToATC))

        val reduceQtyTracking = baseViewModel.decreaseQtyTrackingLiveData.value
        assertThat(reduceQtyTracking, nullValue())
    }

    fun `add to cart to update quantity failed`() {
        callback.`Given first page API will be successful`()
        `Given get mini cart simplified use case will be successful`(miniCartSimplifiedData)
        `Given update cart use case will fail`()
        `Given view already created`()
        `Given view resumed to update mini cart`()

        val productItemList = baseViewModel.visitableListLiveData.value!!.getProductItemList()
        val productIdToATC = PRODUCT_ID_NON_VARIANT_ATC
        val productInMiniCart = miniCartItems.getMiniCartItemProduct(productIdToATC)!!

        val productUpdatedQuantity = productInMiniCart.quantity - 3
        val productInVisitable = productItemList.find { it.productCardModel.productId == productIdToATC }!!

        `When handle cart event product non variant`(productInVisitable, productUpdatedQuantity)

        val expectedErrorMessage = responseErrorException.message!!
        val expectedProductQuantity = productInMiniCart.quantity
        val expectedRefreshMiniCartCount = 1
        `Then assert update quantity`(
                productUpdatedQuantity,
                productInMiniCart.cartId,
                productInVisitable,
                expectedErrorMessage,
                expectedProductQuantity,
                expectedRefreshMiniCartCount,
        )
    }

    fun `Given update cart use case will fail`() {
        every {
            updateCartUseCase.execute(any(), any())
        } answers {
            secondArg<(Throwable) -> Unit>().invoke(responseErrorException)
        }
    }

    fun `test ATC non login should redirect to login page`() {
        callback.`Given first page API will be successful`()
        `Given view already created`()
        `Given user not logged in`()

        val visitableList = baseViewModel.visitableListLiveData.value!!
        val productItemList = visitableList.getProductItemList()
        val productItemDataViewToATC = productItemList[0]
        `When handle cart event product non variant`(productItemDataViewToATC, 3)

        `Then assert route to login page event`()
        `Then assert cart use cases are not called`()

        val visitableIndex = visitableList.indexOf(productItemDataViewToATC)
        `Then assert visitable list is updated to revert add to cart button`(visitableIndex)
    }

    fun `Given user not logged in`() {
        every { userSession.isLoggedIn } returns false
    }

    fun `Then assert route to login page event`() {
        assertThat(baseViewModel.routeApplinkLiveData.value, shouldBe(ApplinkConst.LOGIN))
    }

    fun `Then assert cart use cases are not called`() {
        verify(exactly = 0) {
            addToCartUseCase.execute(any(), any())
            updateCartUseCase.execute(any(), any())
            deleteCartUseCase.execute(any(), any())
        }
    }

    fun `Then assert visitable list is updated to revert add to cart button`(
            visitableIndex: Int
    ) {
        assertThat(
                baseViewModel.updatedVisitableIndicesLiveData.value,
                shouldBe(listOf(visitableIndex))
        )
    }

    fun `delete cart success`() {
        val productId = PRODUCT_ID_NON_VARIANT_ATC
        `Given delete cart use case will be successful`(deleteCartResponse)
        `Given view setup to delete`(productId)

        val productItemList = baseViewModel.visitableListLiveData.value!!.getProductItemList()
        val productInMiniCart = miniCartItems.getMiniCartItemProduct(productId)!!
        val productInVisitable = productItemList.find { it.productCardModel.productId == productId }!!

        `When handle cart event product non variant`(productInVisitable, 0)

        `Then assert delete cart behavior`(
            productInMiniCart.cartId,
            productInVisitable,
            expectedSuccessDeleteCartMessage = deleteCartMessage,
        )
        `Then verify delete cart tracking is called`(productId)
        `Then assert update toolbar notification true`()
    }

    fun `Given view setup to delete`(productIdToDelete: String) {
        callback.`Given first page API will be successful`()
        `Given get mini cart simplified use case will be successful`(miniCartSimplifiedData)
        `Given view already created`()
        `Given view resumed to update mini cart`()

        val updatedMiniCartData = miniCartSimplifiedData
        updateMiniCartData(updatedMiniCartData, productIdToDelete, 0)
        `Given get mini cart simplified use case will be successful`(updatedMiniCartData)
    }

    fun `Given delete cart use case will be successful`(
        successUpdateCartResponse: RemoveFromCartData
    ) {
        every {
            deleteCartUseCase.execute(any(), any())
        } answers {
            firstArg<(RemoveFromCartData) -> Unit>().invoke(successUpdateCartResponse)
        }
    }

    private fun `Then assert delete cart behavior`(
            cartIdParam: String,
            productInVisitable: ProductItemDataView,
            expectedQuantity: Int = 0,
            expectedSuccessDeleteCartMessage: String = "",
            expectedFailedDeleteCartMessage: String = "",
            expectedRefreshMiniCartCount: Int = 2,
    ) {
        `Then assert delete cart params`(cartIdParam)
        `Then assert remove from cart message event`(expectedSuccessDeleteCartMessage)
        `Then assert cart failed message event`(expectedFailedDeleteCartMessage)
        `Then assert product item quantity`(productInVisitable, expectedQuantity)
        `Then assert add to cart use case is not called`()
        `Then assert update cart use case is not called`()
        `Then verify mini cart is refreshed`(expectedRefreshMiniCartCount)
        `Then assert route to login page event is null`()
    }

    fun `Then assert delete cart params`(
            cartIdParam: String,
    ) {
        val deleteCartIdParamSlot = slot<List<String>>()
        val listDeleteCartId by lazy { deleteCartIdParamSlot.captured }

        verify {
            deleteCartUseCase.setParams(
                    cartIdList = capture(deleteCartIdParamSlot)
            )
        }
        val deleteCartId = listDeleteCartId[0]
        assertThat(deleteCartId, shouldBe(cartIdParam))
    }

    fun `Then assert update cart use case is not called`() {
        verify(exactly = 0) {
            updateCartUseCase.execute(any(), any())
        }
    }

    fun `Then verify delete cart tracking is called`(productId: String) {
        val increaseQtyTracking = baseViewModel.deleteCartTrackingLiveData.value!!
        assertThat(increaseQtyTracking, shouldBe(productId))
    }

    fun `delete cart failed`() {
        val productIdToATC = PRODUCT_ID_NON_VARIANT_ATC

        `Given delete cart use case will fail`()
        `Given view setup to delete`(productIdToATC)

        val productItemList = baseViewModel.visitableListLiveData.value!!.getProductItemList()
        val productInMiniCart = miniCartItems.getMiniCartItemProduct(productIdToATC)!!
        val productInVisitable = productItemList.find { it.productCardModel.productId == productIdToATC }!!
        val currentQuantity = productInVisitable.productCardModel.orderQuantity

        `When handle cart event product non variant`(productInVisitable, 0)

        `Then assert delete cart behavior`(
                productInMiniCart.cartId,
                productInVisitable,
                currentQuantity,
                expectedFailedDeleteCartMessage = responseErrorException.message!!,
                expectedRefreshMiniCartCount = 1
        )
    }

    fun `Given delete cart use case will fail`() {
        every {
            deleteCartUseCase.execute(any(), any())
        } answers {
            secondArg<(Throwable) -> Unit>().invoke(responseErrorException)
        }
    }

    companion object {
        const val PRODUCT_ID_NON_VARIANT_ATC = "574261655"

        object AddToCartTestObject {
            val addToCartQty get() = 10
            val errorMessage get() =
                arrayListOf("Success nih", "1 barang berhasil ditambahkan ke keranjang!")
            val cartId get() = "12345"
            val addToCartSuccessModel get() = AddToCartDataModel(
                    errorMessage = errorMessage,
                    status = AddToCartDataModel.STATUS_OK,
                    data = DataModel(
                            success = 1,
                            cartId = cartId,
                            message = arrayListOf(),
                            quantity = addToCartQty,
                    ),
            )
        }

        object UpdateCartTestObject {
            val updateCartSuccessMessage get() = "Success nih"
            val successUpdateCartResponse get() = UpdateCartV2Data(
                    data = Data(status = true, message = updateCartSuccessMessage)
            )
        }

        object DeleteCartTestObject {
            val deleteCartMessage get() = "1 barang telah dihapus."
            val deleteCartResponse get() = RemoveFromCartData(
                    status = "OK",
                    errorMessage = listOf(deleteCartMessage),
                    data = DeleteCartData(success = 1, message = listOf(deleteCartMessage))
            )
        }
    }

    interface Callback {
        fun `Given first page API will be successful`()
    }
}
