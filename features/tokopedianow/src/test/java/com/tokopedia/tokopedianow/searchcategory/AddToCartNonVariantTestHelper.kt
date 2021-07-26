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
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.minicart.common.domain.usecase.GetMiniCartListSimplifiedUseCase
import com.tokopedia.network.exception.ResponseErrorException
import com.tokopedia.recommendation_widget_common.data.RecommendationEntity
import com.tokopedia.recommendation_widget_common.domain.coroutines.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.extension.mappingToRecommendationModel
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.tokopedianow.searchcategory.AddToCartNonVariantTestHelper.Companion.AddToCartTestObject.addToCartQty
import com.tokopedia.tokopedianow.searchcategory.AddToCartNonVariantTestHelper.Companion.AddToCartTestObject.addToCartSuccessModel
import com.tokopedia.tokopedianow.searchcategory.AddToCartNonVariantTestHelper.Companion.AddToCartTestObject.cartId
import com.tokopedia.tokopedianow.searchcategory.AddToCartNonVariantTestHelper.Companion.AddToCartTestObject.errorMessage
import com.tokopedia.tokopedianow.searchcategory.AddToCartNonVariantTestHelper.Companion.RecommendationATCTestObject.recommendationWidgetList
import com.tokopedia.tokopedianow.searchcategory.presentation.model.ProductItemDataView
import com.tokopedia.tokopedianow.searchcategory.presentation.model.RecommendationCarouselDataView
import com.tokopedia.tokopedianow.searchcategory.presentation.viewmodel.BaseSearchCategoryViewModel
import com.tokopedia.tokopedianow.util.SearchCategoryDummyUtils.miniCartItems
import com.tokopedia.tokopedianow.util.SearchCategoryDummyUtils.miniCartSimplifiedData
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.coEvery
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
        private val getRecommendationUseCase: GetRecommendationUseCase,
        private val userSession: UserSessionInterface,
        private val callback: Callback,
) {

    private val addToCartRequestParamsSlot = slot<AddToCartRequestParams>()
    private val addToCartRequestParams by lazy { addToCartRequestParamsSlot.captured }
    private val responseErrorException = ResponseErrorException(
            "Jumlah barang melebihi stok di toko. Kurangi pembelianmu, ya!"
    )

    fun `test add to cart success`() {
        callback.`Given first page API will be successful`()
        `Given view already created`()
        `Given add to cart API will success`(addToCartSuccessModel)

        val productItemList = baseViewModel.visitableListLiveData.value!!.getProductItemList()
        val productItemDataViewToATC = productItemList[0]

        `When handle cart event product non variant`(productItemDataViewToATC, addToCartQty)

        val productId = productItemDataViewToATC.id
        val shopId = productItemDataViewToATC.shop.id
        `Then assert add to cart request params`(productId, shopId, addToCartQty)
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

    private fun `When handle cart event product non variant`(
            productItemToATC: ProductItemDataView,
            addToCartQty: Int,
    ) {
        baseViewModel.onViewATCProductNonVariant(productItemToATC, addToCartQty)
    }

    private fun `Then assert add to cart request params`(
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

        `When handle cart event product non variant`(productItemDataViewToATC, addToCartQty)

        val productId = productItemDataViewToATC.id
        val shopId = productItemDataViewToATC.shop.id
        `Then assert add to cart request params`(productId, shopId, addToCartQty)
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
        `When handle cart event product non variant`(productInVisitable, currentQty)

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

        `When handle cart event product non variant`(productInVisitable, productUpdatedQuantity)

        `Then assert update quantity`(
                productUpdatedQuantity,
                productInMiniCart.cartId,
                productInVisitable
        )
        `Then verify decrease cart quantity tracking is called`(productIdToATC)
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
            updateQuantityParam: Int,
            cartIdParam: String,
            productInVisitable: ProductItemDataView,
            expectedCartErrorMessage: String = "",
            expectedProductQuantity: Int = updateQuantityParam,
            expectedRefreshMiniCartCount: Int = 2,
    ) {
        `Then assert update cart params`(updateQuantityParam, cartIdParam)
        `Then assert cart message event`("", expectedCartErrorMessage)
        `Then assert product item quantity`(productInVisitable, expectedProductQuantity)
        `Then assert add to cart use case is not called`()
        `Then verify mini cart is refreshed`(expectedRefreshMiniCartCount)
        `Then assert route to login page event is null`()
    }

    private fun `Then assert update cart params`(
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
        val updateCartSuccessMessage = "Success nih"
        `Given view setup to update quantity`(updateCartSuccessMessage)

        val productItemList = baseViewModel.visitableListLiveData.value!!.getProductItemList()
        val productIdToATC = PRODUCT_ID_NON_VARIANT_ATC
        val productInMiniCart = miniCartItems.find { it.productId == productIdToATC }!!

        val productUpdatedQuantity = productInMiniCart.quantity + 3
        val productInVisitable = productItemList.find { it.id == productIdToATC }!!

        `When handle cart event product non variant`(productInVisitable, productUpdatedQuantity)

        `Then assert update quantity`(
                productUpdatedQuantity,
                productInMiniCart.cartId,
                productInVisitable,
        )
        `Then verify increase cart quantity tracking is called`(productIdToATC)
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
        `When handle cart event product non variant`(productItemDataViewToATC, 3)

        `Then assert route to login page event`()
        `Then assert cart use cases are not called`()

        val visitableIndex = visitableList.indexOf(productItemDataViewToATC)
        `Then assert visitable list is updated to revert add to cart button`(visitableIndex)
    }

    private fun `Given user not logged in`() {
        every { userSession.isLoggedIn } returns false
    }

    private fun `Then assert route to login page event`() {
        assertThat(baseViewModel.routeApplinkLiveData.value, shouldBe(ApplinkConst.LOGIN))
    }

    private fun `Then assert cart use cases are not called`() {
        verify(exactly = 0) {
            addToCartUseCase.execute(any(), any())
            updateCartUseCase.execute(any(), any())
            deleteCartUseCase.execute(any(), any())
        }
    }

    private fun `Then assert visitable list is updated to revert add to cart button`(
            visitableIndex: Int
    ) {
        assertThat(
                baseViewModel.updatedVisitableIndicesLiveData.value,
                shouldBe(listOf(visitableIndex))
        )
    }

    fun `test delete cart success`() {
        val deleteCartMessage = "1 barang telah dihapus."
        val deleteCartResponse = RemoveFromCartData(
                status = "OK",
                errorMessage = listOf(deleteCartMessage),
                data = DeleteCartData(success = 1, message = listOf(deleteCartMessage))
        )
        `Given delete cart use case will be successful`(deleteCartResponse)
        `Given view setup to delete`()

        val productItemList = baseViewModel.visitableListLiveData.value!!.getProductItemList()
        val productId = PRODUCT_ID_NON_VARIANT_ATC
        val productInMiniCart = miniCartItems.find { it.productId == productId }!!

        val productUpdatedQuantity = 0
        val productInVisitable = productItemList.find { it.id == productId }!!

        `When handle cart event product non variant`(productInVisitable, productUpdatedQuantity)

        `Then assert delete cart behavior`(
            productInMiniCart.cartId,
            productInVisitable,
            expectedSuccessDeleteCartMessage = deleteCartMessage,
        )
        `Then verify delete cart tracking is called`(productId)
    }

    private fun `Given view setup to delete`() {
        callback.`Given first page API will be successful`()
        `Given get mini cart simplified use case will be successful`(miniCartSimplifiedData)
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

    private fun `Then assert delete cart behavior`(
            cartIdParam: String,
            productInVisitable: ProductItemDataView,
            expectedQuantity: Int = 0,
            expectedSuccessDeleteCartMessage: String = "",
            expectedFailedDeleteCartMessage: String = "",
            expectedRefreshMiniCartCount: Int = 2,
    ) {
        `Then assert delete cart params`(cartIdParam)
        `Then assert cart message event`(expectedSuccessDeleteCartMessage, expectedFailedDeleteCartMessage)
        `Then assert product item quantity`(productInVisitable, expectedQuantity)
        `Then assert add to cart use case is not called`()
        `Then assert update cart use case is not called`()
        `Then verify mini cart is refreshed`(expectedRefreshMiniCartCount)
        `Then assert route to login page event is null`()
    }

    private fun `Then assert delete cart params`(
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

    private fun `Then assert update cart use case is not called`() {
        verify(exactly = 0) {
            updateCartUseCase.execute(any(), any())
        }
    }

    private fun `Then verify delete cart tracking is called`(productId: String) {
        val increaseQtyTracking = baseViewModel.deleteCartTrackingLiveData.value!!
        assertThat(increaseQtyTracking, shouldBe(productId))
    }

    fun `delete cart failed`() {
        `Given delete cart use case will fail`()
        `Given view setup to delete`()

        val productItemList = baseViewModel.visitableListLiveData.value!!.getProductItemList()
        val productIdToATC = PRODUCT_ID_NON_VARIANT_ATC
        val productInMiniCart = miniCartItems.find { it.productId == productIdToATC }!!

        val productUpdatedQuantity = 0
        val productInVisitable = productItemList.find { it.id == productIdToATC }!!

        `When handle cart event product non variant`(productInVisitable, productUpdatedQuantity)

        `Then assert delete cart behavior`(
                productInMiniCart.cartId,
                productInVisitable,
                productInMiniCart.quantity,
                expectedFailedDeleteCartMessage = responseErrorException.message!!,
                expectedRefreshMiniCartCount = 1
        )
    }

    private fun `Given delete cart use case will fail`() {
        every {
            deleteCartUseCase.execute(any(), any())
        } answers {
            secondArg<(Throwable) -> Unit>().invoke(responseErrorException)
        }
    }

    fun `test ATC recom non login should redirect to login page`() {
        val recommendationWidgets = recommendationWidgetList
        `Given user not logged in`()
        `Given view setup to show recommendation widget`(recommendationWidgets)

        val recommendationItemForATC =
                recommendationWidgets.getRecommendationItem(PRODUCT_ID_NON_VARIANT_ATC)
        val recommendationDataViewDummyIndex = 0
        `When add to cart recommendation item`(
                recommendationItemForATC,
                recommendationDataViewDummyIndex,
                10,
        )

        `Then assert route to login page event`()
        `Then assert cart use cases are not called`()
        `Then assert visitable list is updated to revert add to cart button`(
                recommendationDataViewDummyIndex
        )
    }

    private fun `Given view setup to show recommendation widget`(
            recommendationWidgetList: List<RecommendationWidget>
    ) {
        callback.`Given first page API can show recommendation`()
        `Given get recommendation use case will be successful`(recommendationWidgetList)
        `Given view already created`()
        `Given view resumed to update mini cart`()

        val visitableList = baseViewModel.visitableListLiveData.value!!
        val (recommendationDataViewIndex, recommendationDataView) =
                visitableList.findRecommendationDataViewIndexed()

        `Given view already bind recommendation widget`(
                recommendationDataView, recommendationDataViewIndex
        )
    }

    private fun `Given get recommendation use case will be successful`(
            recommendationWidgetList: List<RecommendationWidget>
    ) {
        coEvery {
            getRecommendationUseCase.getData(any())
        } returns recommendationWidgetList
    }

    @Suppress("UNCHECKED_CAST")
    private fun List<Visitable<*>>.findRecommendationDataViewIndexed() =
            withIndex().find { it.value is RecommendationCarouselDataView }
                as? IndexedValue<RecommendationCarouselDataView>
                ?: throw Throwable("Cannot find recom widget")

    private fun `Given view already bind recommendation widget`(
            recommendationDataView: RecommendationCarouselDataView,
            recommendationDataViewIndex: Int,
    ) {
        baseViewModel.onBindRecommendationCarousel(
                recommendationDataView, recommendationDataViewIndex,
        )
    }

    private fun List<RecommendationWidget>.getRecommendationItem(productId: String) =
            flatMap { it.recommendationItemList }.find {
                it.productId.toString() == productId
            }!!

    private fun `When add to cart recommendation item`(
            recomItem: RecommendationItem,
            recomPosition: Int,
            quantity: Int,
    ) {
        baseViewModel.onViewATCRecommendationItemNonVariant(
                recomItem,
                recomPosition,
                quantity,
        )
    }

    fun `test add to cart recom item success`() {
        val recommendationWidgets = recommendationWidgetList
        `Given add to cart API will success`(addToCartSuccessModel)
        `Given view setup to show recommendation widget`(recommendationWidgets)

        val recommendationItemForATC =
                recommendationWidgets.getRecommendationItem(PRODUCT_ID_NON_VARIANT_ATC)
        val recommendationDataViewDummyIndex = 0
        `When add to cart recommendation item`(
                recommendationItemForATC,
                recommendationDataViewDummyIndex,
                addToCartQty,
        )

        val productId = recommendationItemForATC.productId.toString()
        val shopId = recommendationItemForATC.shopId.toString()
        `Then assert add to cart request params`(productId, shopId, addToCartQty)
        `Then assert cart message event`(
                expectedSuccessMessage = errorMessage.joinToString(separator = ", ")
        )
        `Then assert recommendation item quantity`(recommendationItemForATC, addToCartQty)
        `Then verify mini cart is refreshed`(exactly = 2)
        `Then assert route to login page event is null`()
        // TODO::Verify Tracking Add to cart
//        `Then verify add to cart tracking is called`(addToCartQty, cartId, productItemDataViewToATC)
    }

    private fun `Then assert recommendation item quantity`(recommendationItemForATC: RecommendationItem, addToCartQty: Int) {
        assertThat(recommendationItemForATC.quantity, shouldBe(addToCartQty))
    }

    fun `test add to cart recom item failed`() {
        val recommendationWidgets = recommendationWidgetList
        `Given add to cart API will fail`(responseErrorException)
        `Given view setup to show recommendation widget`(recommendationWidgets)

        val recommendationItemForATC =
                recommendationWidgets.getRecommendationItem(PRODUCT_ID_NON_VARIANT_ATC)
        val recommendationDataViewDummyIndex = 0
        `When add to cart recommendation item`(
                recommendationItemForATC,
                recommendationDataViewDummyIndex,
                addToCartQty,
        )

        val productId = recommendationItemForATC.productId.toString()
        val shopId = recommendationItemForATC.shopId.toString()
        `Then assert add to cart request params`(productId, shopId, addToCartQty)
        `Then assert cart message event`(expectedErrorMessage = responseErrorException.message!!)
        `Then assert recommendation item quantity`(recommendationItemForATC, 0)
        `Then verify mini cart is refreshed`(1)
        `Then assert route to login page event is null`()
    }

    fun `add to cart recom item with current quantity should do nothing`() {
        val recommendationWidgets = recommendationWidgetList
        `Given get mini cart simplified use case will be successful`(miniCartSimplifiedData)
        `Given view setup to show recommendation widget`(recommendationWidgets)

        val recommendationItemForATC =
                recommendationWidgets.getRecommendationItem(PRODUCT_ID_NON_VARIANT_ATC)
        val recommendationDataViewDummyIndex = 0
        val currentQty = getRecommendationItemCurrentQty(
                miniCartSimplifiedData, recommendationItemForATC
        )

        `When add to cart recommendation item`(
                recommendationItemForATC,
                recommendationDataViewDummyIndex,
                currentQty,
        )

        `Then assert add to cart use case is not called`()
        `Then assert route to login page event is null`()
    }

    private fun getRecommendationItemCurrentQty(
            miniCartSimplifiedData: MiniCartSimplifiedData,
            recommendationItemForATC: RecommendationItem,
    ) = miniCartSimplifiedData
            .miniCartItems
            .find { it.productId == recommendationItemForATC.productId.toString() }
            ?.quantity
            ?: 0

    fun `add to cart recom item to decrease quantity success`() {
        val recommendationWidgets = recommendationWidgetList
        val miniCartData = miniCartSimplifiedData
        `Given view setup to update quantity recom item`(recommendationWidgets, miniCartData)

        val recommendationItemForATC =
                recommendationWidgets.getRecommendationItem(PRODUCT_ID_NON_VARIANT_ATC)
        val recommendationDataViewDummyIndex = 0
        val currentQty = getRecommendationItemCurrentQty(
                miniCartData, recommendationItemForATC
        )
        val updateQty = currentQty - 3

        `When add to cart recommendation item`(
                recommendationItemForATC,
                recommendationDataViewDummyIndex,
                updateQty,
        )

        val productInMiniCart = miniCartData.miniCartItems.find {
            it.productId == recommendationItemForATC.productId.toString()
        }!!
        `Then verify behavior update quantity recom item`(
                recommendationItemForATC,
                updateQty,
                productInMiniCart.cartId
        )
        //TODO:: Tracking decrease quantity
//        `Then verify decrease cart quantity tracking is called`(productIdToATC)
    }

    private fun `Given view setup to update quantity recom item`(
            recommendationWidgets: List<RecommendationWidget>,
            miniCartData: MiniCartSimplifiedData,
    ) {
        val updateCartSuccessMessage = "Success nih"
        val successUpdateCartResponse = UpdateCartV2Data(
                data = Data(status = true, message = updateCartSuccessMessage)
        )

        `Given get mini cart simplified use case will be successful`(miniCartData)
        `Given update cart use case will be successful`(successUpdateCartResponse)
        `Given view setup to show recommendation widget`(recommendationWidgets)
    }

    private fun `Then verify behavior update quantity recom item`(
            recommendationItemForATC: RecommendationItem,
            updateQuantityParams: Int,
            cartIdParam: String,
            expectedCartErrorMessage: String = "",
            expectedProductQuantity: Int = updateQuantityParams,
            expectedRefreshMiniCartCount: Int = 2,
    ) {
        `Then assert update cart params`(updateQuantityParams, cartIdParam)
        `Then assert cart message event`("", expectedCartErrorMessage)
        `Then assert recommendation item quantity`(recommendationItemForATC, expectedProductQuantity)
        `Then assert add to cart use case is not called`()
        `Then verify mini cart is refreshed`(expectedRefreshMiniCartCount)
        `Then assert route to login page event is null`()
    }

    fun `add to cart recom item to increase quantity success`() {
        val recommendationWidgets = recommendationWidgetList
        val miniCartData = miniCartSimplifiedData
        `Given view setup to update quantity recom item`(recommendationWidgets, miniCartData)

        val recommendationItemForATC =
                recommendationWidgets.getRecommendationItem(PRODUCT_ID_NON_VARIANT_ATC)
        val recommendationDataViewDummyIndex = 0
        val currentQty = getRecommendationItemCurrentQty(
                miniCartData, recommendationItemForATC
        )
        val updateQty = currentQty + 3

        `When add to cart recommendation item`(
                recommendationItemForATC,
                recommendationDataViewDummyIndex,
                updateQty,
        )

        val productInMiniCart = miniCartData.miniCartItems.find {
            it.productId == recommendationItemForATC.productId.toString()
        }!!
        `Then verify behavior update quantity recom item`(
                recommendationItemForATC,
                updateQty,
                productInMiniCart.cartId,
        )
        //TODO:: Tracking increase quantity
//        `Then verify increase cart quantity tracking is called`(productIdToATC)
    }

    fun `add to cart recom item to update quantity failed`() {
        val recommendationWidgets = recommendationWidgetList
        val miniCartData = miniCartSimplifiedData
        `Given get mini cart simplified use case will be successful`(miniCartData)
        `Given update cart use case will fail`()
        `Given view setup to show recommendation widget`(recommendationWidgets)

        val recommendationItemForATC =
                recommendationWidgets.getRecommendationItem(PRODUCT_ID_NON_VARIANT_ATC)
        val recommendationDataViewDummyIndex = 0
        val currentQty = getRecommendationItemCurrentQty(
                miniCartData, recommendationItemForATC
        )
        val updateQty = currentQty + 3

        `When add to cart recommendation item`(
                recommendationItemForATC,
                recommendationDataViewDummyIndex,
                updateQty,
        )

        val productInMiniCart = miniCartData.miniCartItems.find {
            it.productId == recommendationItemForATC.productId.toString()
        }!!
        val expectedErrorMessage = responseErrorException.message!!
        val expectedRefreshMiniCartCount = 1
        `Then verify behavior update quantity recom item`(
                recommendationItemForATC,
                updateQty,
                productInMiniCart.cartId,
                expectedErrorMessage,
                currentQty,
                expectedRefreshMiniCartCount,
        )
    }

    companion object {
        private const val PRODUCT_ID_NON_VARIANT_ATC = "574261655"

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

        object RecommendationATCTestObject {
            private val recommendationEntity get() =
                    "recom/recom-carousel.json".jsonToObject<RecommendationEntity>()

            val recommendationWidgetList get() = recommendationEntity
                    .productRecommendationWidget
                    .data
                    .mappingToRecommendationModel()
        }
    }

    interface Callback {
        fun `Given first page API will be successful`()

        fun `Given first page API can show recommendation`()
    }
}