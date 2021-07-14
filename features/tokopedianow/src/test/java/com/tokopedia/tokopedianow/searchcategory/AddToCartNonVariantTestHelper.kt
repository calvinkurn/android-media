package com.tokopedia.tokopedianow.searchcategory

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.atc_common.data.model.request.AddToCartRequestParams
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.model.response.DataModel
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartUseCase
import com.tokopedia.minicart.common.data.response.deletecart.RemoveFromCartData
import com.tokopedia.minicart.common.data.response.updatecart.Data
import com.tokopedia.minicart.common.data.response.updatecart.UpdateCartV2Data
import com.tokopedia.minicart.common.domain.data.MiniCartItem
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.minicart.common.domain.usecase.DeleteCartUseCase
import com.tokopedia.minicart.common.domain.usecase.GetMiniCartListSimplifiedUseCase
import com.tokopedia.minicart.common.domain.usecase.UpdateCartUseCase
import com.tokopedia.network.exception.ResponseErrorException
import com.tokopedia.tokopedianow.searchcategory.presentation.model.ProductItemDataView
import com.tokopedia.tokopedianow.searchcategory.presentation.viewmodel.BaseSearchCategoryViewModel
import com.tokopedia.tokopedianow.util.SearchCategoryDummyUtils.miniCartItems
import com.tokopedia.tokopedianow.util.SearchCategoryDummyUtils.miniCartSimplifiedData
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.every
import io.mockk.slot
import io.mockk.verify
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsNull.nullValue
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
    private val responseErrorException = ResponseErrorException(
            "Jumlah barang melebihi stok di toko. Kurangi pembelianmu, ya!"
    )

    fun `test add to cart success`() {
        val addToCartQty = 10
        val errorMessage = arrayListOf("Success nih", "1 barang berhasil ditambahkan ke keranjang!")
        val cartId = "12345"
        val addToCartSuccessModel = AddToCartDataModel(
                errorMessage = errorMessage,
                status = AddToCartDataModel.STATUS_OK,
                data = DataModel(
                        success = 1,
                        cartId = cartId,
                        message = arrayListOf(),
                        quantity = addToCartQty,
                ),
        )

        callback.`Given first page API will be successful`()
        `Given view already created`()
        `Given add to cart API will success`(addToCartSuccessModel)

        val productItemList = baseViewModel.visitableListLiveData.value!!.getProductItemList()
        val productItemDataViewToATC = productItemList[0]

        `When add to cart a product`(productItemDataViewToATC, addToCartQty)

        `Then assert add to cart request params`(productItemDataViewToATC, addToCartQty)
        `Then assert cart message event`(
                expectedSuccessMessage = errorMessage.joinToString(separator = ", ")
        )
        `Then assert product item quantity`(productItemDataViewToATC, addToCartQty)
        `Then verify mini cart is refreshed`()
        `Then verify add to cart tracking is called`(addToCartQty, cartId, productItemDataViewToATC)
        `Then assert route to login page event is null`()
    }

    private fun `Given view already created`() {
        baseViewModel.onViewCreated()
    }

    private fun `Given add to cart API will success`(addToCartSuccessModel: AddToCartDataModel) {
        every {
            addToCartUseCase.execute(any(), any())
        } answers {
            firstArg<(AddToCartDataModel) -> Unit>().invoke(addToCartSuccessModel)
        }
    }

    private fun List<Visitable<*>>.getProductItemList() = filterIsInstance<ProductItemDataView>()

    private fun `When add to cart a product`(productItemToATC: ProductItemDataView, addToCartQty: Int) {
        baseViewModel.onViewATCProductNonVariant(productItemToATC, addToCartQty)
    }

    private fun `Then assert add to cart request params`(
            productItem: ProductItemDataView,
            addToCartQty: Int,
    ) {
        verify {
            addToCartUseCase.setParams(capture(addToCartRequestParamsSlot))
        }

        assertThat(addToCartRequestParams.productId.toString(), shouldBe(productItem.id))
        assertThat(addToCartRequestParams.shopId.toString(), shouldBe(productItem.shop.id))
        assertThat(addToCartRequestParams.quantity, shouldBe(addToCartQty))
    }

    private fun `Then assert cart message event`(
            expectedSuccessMessage: String = "",
            expectedErrorMessage: String = "",
    ) {
        val successMessageLiveData = baseViewModel.successATCMessageLiveData.value ?: ""
        assertThat(successMessageLiveData, shouldBe(expectedSuccessMessage))

        val errorMessageLiveData = baseViewModel.errorATCMessageLiveData.value ?: ""
        assertThat(errorMessageLiveData, shouldBe(expectedErrorMessage))
    }

    private fun `Then assert product item quantity`(
            productItemDataViewToATC: ProductItemDataView,
            expectedQuantity: Int,
    ) {
        val actualQuantity = productItemDataViewToATC.nonVariantATC?.quantity ?: -1

        assertThat(actualQuantity, shouldBe(expectedQuantity))
    }

    private fun `Then verify mini cart is refreshed`(exactly: Int = 1) {
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

    private fun `Then assert route to login page event is null`() {
        assertThat(baseViewModel.routeApplinkLiveData.value, nullValue())
    }

    fun `test add to cart failed`() {
        callback.`Given first page API will be successful`()
        `Given view already created`()
        `Given add to cart API will fail`(responseErrorException)

        val productItemList = baseViewModel.visitableListLiveData.value!!.getProductItemList()
        val productItemDataViewToATC = productItemList[0]
        val addToCartQty = 15

        `When add to cart a product`(productItemDataViewToATC, addToCartQty)

        `Then assert add to cart request params`(productItemDataViewToATC, addToCartQty)
        `Then assert cart message event`(expectedErrorMessage = responseErrorException.message!!)
        `Then assert product item quantity`(productItemDataViewToATC, 0)
        `Then verify mini cart is refreshed`(0)
        `Then assert route to login page event is null`()
    }

    private fun `Given add to cart API will fail`(throwable: Throwable) {
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
        `When add to cart a product`(productInVisitable, currentQty)

        `Then assert add to cart use case is not called`()
        `Then assert route to login page event is null`()
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

    private fun `Given view resumed to update mini cart`() {
        baseViewModel.onViewResumed()
    }

    private fun getAvailableProductAndQuantity(): Pair<Int, ProductItemDataView> {
        val productItemList = baseViewModel.visitableListLiveData.value!!.getProductItemList()
        val productIdToATC = PRODUCT_ID_NON_VARIANT_ATC
        val productInMiniCart = miniCartItems.find { it.productId == productIdToATC }!!

        val productCurrentQuantity = productInMiniCart.quantity
        val productInVisitable = productItemList.find { it.id == productIdToATC }!!

        return Pair(productCurrentQuantity, productInVisitable)
    }

    private fun `Then assert add to cart use case is not called`() {
        verify(exactly = 0) {
            addToCartUseCase.execute(any(), any())
        }
    }

    fun `add to cart to decrease quantity success`() {
        val updateCartSuccessMessage = "Success nih"
        `Given view setup to update quantity`(updateCartSuccessMessage)

        val productItemList = baseViewModel.visitableListLiveData.value!!.getProductItemList()
        val productIdToATC = PRODUCT_ID_NON_VARIANT_ATC
        val productInMiniCart = miniCartItems.find { it.productId == productIdToATC }!!

        val productUpdatedQuantity = productInMiniCart.quantity - 3
        val productInVisitable = productItemList.find { it.id == productIdToATC }!!

        `When add to cart a product`(productInVisitable, productUpdatedQuantity)

        `Then assert update quantity`(
                productUpdatedQuantity,
                productInMiniCart,
                productInVisitable
        )
        `Then verify decrease cart quantity tracking is called`(productIdToATC)
        `Then assert route to login page event is null`()
    }

    private fun `Given view setup to update quantity`(updateCartSuccessMessage: String) {
        val successUpdateCartResponse = UpdateCartV2Data(
                data = Data(status = true, message = updateCartSuccessMessage)
        )

        callback.`Given first page API will be successful`()
        `Given get mini cart simplified use case will be successful`(miniCartSimplifiedData)
        `Given update cart use case will be successful`(successUpdateCartResponse)
        `Given view already created`()
        `Given view resumed to update mini cart`()
    }

    private fun `Given update cart use case will be successful`(
            successUpdateCartResponse: UpdateCartV2Data
    ) {
        every {
            updateCartUseCase.execute(any(), any())
        } answers {
            firstArg<(UpdateCartV2Data) -> Unit>().invoke(successUpdateCartResponse)
        }
    }

    private fun `Then assert update quantity`(
            productUpdatedQuantity: Int,
            productInMiniCart: MiniCartItem,
            productInVisitable: ProductItemDataView
    ) {
        `Then assert update cart params`(productUpdatedQuantity, productInMiniCart)
        `Then assert cart message event`()
        `Then assert product item quantity`(productInVisitable, productUpdatedQuantity)
        `Then assert add to cart use case is not called`()
        `Then verify mini cart is refreshed`(2)
    }

    private fun `Then assert update cart params`(
            productUpdatedQuantity: Int,
            productInMiniCart: MiniCartItem,
    ) {
        val updateCartParamSlot = slot<List<MiniCartItem>>()
        val updateCartParam by lazy { updateCartParamSlot.captured }

        verify {
            updateCartUseCase.setParams(
                    miniCartItemList = capture(updateCartParamSlot),
                    source = UpdateCartUseCase.VALUE_SOURCE_UPDATE_QTY_NOTES,
            )
        }
        val updatedMiniCartItem = updateCartParam[0]
        assertThat(updatedMiniCartItem.quantity, shouldBe(productUpdatedQuantity))
        assertThat(updatedMiniCartItem.cartId, shouldBe(productInMiniCart.cartId))
    }

    private fun `Then verify decrease cart quantity tracking is called`(productIdToATC: String) {
        val increaseQtyTracking = baseViewModel.increaseQtyTrackingLiveData.value
        assertThat(increaseQtyTracking, nullValue())

        val reduceQtyTracking = baseViewModel.decreaseQtyTrackingLiveData.value!!
        assertThat(reduceQtyTracking, shouldBe(productIdToATC))
    }

    fun `add to cart to increase quantity success`() {
        val updateCartSuccessMessage = "Success nih"
        `Given view setup to update quantity`(updateCartSuccessMessage)

        val productItemList = baseViewModel.visitableListLiveData.value!!.getProductItemList()
        val productIdToATC = PRODUCT_ID_NON_VARIANT_ATC
        val productInMiniCart = miniCartItems.find { it.productId == productIdToATC }!!

        val productUpdatedQuantity = productInMiniCart.quantity + 3
        val productInVisitable = productItemList.find { it.id == productIdToATC }!!

        `When add to cart a product`(productInVisitable, productUpdatedQuantity)

        `Then assert update quantity`(
                productUpdatedQuantity,
                productInMiniCart,
                productInVisitable,
        )
        `Then verify increase cart quantity tracking is called`(productIdToATC)
        `Then assert route to login page event is null`()
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
        val productInMiniCart = miniCartItems.find { it.productId == productIdToATC }!!

        val productUpdatedQuantity = productInMiniCart.quantity - 3
        val productInVisitable = productItemList.find { it.id == productIdToATC }!!

        `When add to cart a product`(productInVisitable, productUpdatedQuantity)

        `Then assert update cart params`(productUpdatedQuantity, productInMiniCart)
        `Then assert cart message event`(expectedErrorMessage = responseErrorException.message!!)
        `Then assert product item quantity`(productInVisitable, productInMiniCart.quantity)
        `Then assert add to cart use case is not called`()
        `Then verify mini cart is refreshed`(1)
        `Then assert route to login page event is null`()
    }

    private fun `Given update cart use case will fail`() {
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
        `When add to cart a product`(productItemDataViewToATC, 3)

        `Then assert route to login page event`()
        `Then assert add to cart and update cart not called`()

        val visitableIndex = visitableList.indexOf(productItemDataViewToATC)
        `Then assert product item visitable is updated to revert add to cart button`(visitableIndex)
    }

    private fun `Given user not logged in`() {
        every { userSession.isLoggedIn } returns false
    }

    private fun `Then assert route to login page event`() {
        assertThat(baseViewModel.routeApplinkLiveData.value, shouldBe(ApplinkConst.LOGIN))
    }

    private fun `Then assert add to cart and update cart not called`() {
        verify(exactly = 0) {
            addToCartUseCase.execute(any(), any())
            updateCartUseCase.execute(any(), any())
        }
    }

    private fun `Then assert product item visitable is updated to revert add to cart button`(
            visitableIndex: Int
    ) {
        assertThat(
                baseViewModel.updatedVisitableIndicesLiveData.value,
                shouldBe(listOf(visitableIndex))
        )
    }

    fun `test delete cart success`() {
        val updateCartSuccessMessage = "1 barang telah dihapus."
        `Given view setup to delete`(updateCartSuccessMessage)

        val productItemList = baseViewModel.visitableListLiveData.value!!.getProductItemList()
        val productIdToATC = PRODUCT_ID_NON_VARIANT_ATC
        val productInMiniCart = miniCartItems.find { it.productId == productIdToATC }!!

        val productUpdatedQuantity = 0
        val productInVisitable = productItemList.find { it.id == productIdToATC }!!

        `When add to cart a product`(productInVisitable, productUpdatedQuantity)

        `Then assert delete cart`(
                productUpdatedQuantity,
                productInMiniCart,
                productInVisitable,
        )
        `Then assert route to login page event is null`()
    }



    private fun `Given view setup to delete`(deleteCartMessage: String) {
        val deleteCartResponse = RemoveFromCartData(
            status = "OK",
            errorMessage = listOf(deleteCartMessage),
            data = com.tokopedia.minicart.common.data.response.deletecart.Data(success = 1, message = listOf(deleteCartMessage))
        )

        callback.`Given first page API will be successful`()
        `Given get mini cart simplified use case will be successful`(miniCartSimplifiedData)
        `Given delete cart use case will be successful`(deleteCartResponse)
        `Given view already created`()
        `Given view resumed to update mini cart`()
    }

    private fun `Given delete cart use case will be successful`(
        successUpdateCartResponse: RemoveFromCartData
    ) {
        every {
            deleteCartUseCase.execute(any(), any())
        } answers {
            firstArg<(RemoveFromCartData) -> Unit>().invoke(successUpdateCartResponse)
        }
    }

    private fun `Then assert delete cart`(
        productUpdatedQuantity: Int,
        productInMiniCart: MiniCartItem,
        productInVisitable: ProductItemDataView,
    ) {
        `Then assert delete cart params`(productUpdatedQuantity, productInMiniCart)
        `Then assert product item quantity`(productInVisitable, productUpdatedQuantity)
        `Then assert add to cart use case is not called`()
        `Then assert update cart use case is not called`()
        `Then verify mini cart is refreshed`(2)
    }

    private fun `Then assert delete cart params`(
            productUpdatedQuantity: Int,
            productInMiniCart: MiniCartItem,
    ) {
        val updateCartParamSlot = slot<List<MiniCartItem>>()
        val updateCartParam by lazy { updateCartParamSlot.captured }

        verify {
            deleteCartUseCase.setParams(
                    miniCartItems = capture(updateCartParamSlot),
            )
        }
        val deleteCartItem = updateCartParam[0]
        assertThat(deleteCartItem.quantity, shouldBe(productUpdatedQuantity))
        assertThat(deleteCartItem.cartId, shouldBe(productInMiniCart.cartId))
    }

    private fun `Then assert update cart use case is not called`() {
        verify(exactly = 0) {
            updateCartUseCase.execute(any(), any())
        }
    }

    companion object {
        private const val PRODUCT_ID_NON_VARIANT_ATC = "574261655"
    }

    interface Callback {
        fun `Given first page API will be successful`()
    }
}