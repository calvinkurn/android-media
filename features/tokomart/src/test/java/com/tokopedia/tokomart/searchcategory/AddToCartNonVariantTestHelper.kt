package com.tokopedia.tokomart.searchcategory

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.atc_common.data.model.request.AddToCartRequestParams
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.model.response.DataModel
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartUseCase
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.minicart.common.domain.usecase.GetMiniCartListSimplifiedUseCase
import com.tokopedia.network.exception.ResponseErrorException
import com.tokopedia.tokomart.searchcategory.presentation.model.ProductItemDataView
import com.tokopedia.tokomart.searchcategory.presentation.viewmodel.BaseSearchCategoryViewModel
import com.tokopedia.tokomart.util.SearchCategoryDummyUtils.miniCartItems
import com.tokopedia.tokomart.util.SearchCategoryDummyUtils.miniCartSimplifiedData
import io.mockk.every
import io.mockk.slot
import io.mockk.verify
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.CoreMatchers.`is` as shouldBe

class AddToCartNonVariantTestHelper(
        private val baseViewModel: BaseSearchCategoryViewModel,
        private val addToCartUseCase: AddToCartUseCase,
        private val getMiniCartListSimplifiedUseCase: GetMiniCartListSimplifiedUseCase,
        private val callback: Callback,
) {

    private val addToCartRequestParamsSlot = slot<AddToCartRequestParams>()
    private val addToCartRequestParams by lazy { addToCartRequestParamsSlot.captured }

    fun `test add to cart success`() {
        val addToCartSuccessModel = AddToCartDataModel(
                status = AddToCartDataModel.STATUS_OK,
                data = DataModel(
                        success = 1,
                        cartId = "12345",
                        message = arrayListOf(),
                        quantity = 10,
                ),
        )

        callback.`Given first page API will be successful`()
        `Given view already created`()
        `Given add to cart API will success`(addToCartSuccessModel)

        val productItemList = baseViewModel.visitableListLiveData.value!!.getProductItemList()
        val productItemDataViewToATC = productItemList[0]
        val addToCartQty = 10

        `When add to cart a product`(productItemDataViewToATC, addToCartQty)

        `Then assert add to cart request params`(productItemDataViewToATC, addToCartQty)
        `Then assert add to cart error message is empty`()
        `Then assert product item quantity`(productItemDataViewToATC, addToCartSuccessModel)
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

    private fun `Then assert add to cart error message is empty`() {
        val addToCartEvent = baseViewModel.addToCartEventMessageLiveData.value!!
        val addToCartErrorMessage = addToCartEvent.getContentIfNotHandled()

        assertThat(addToCartErrorMessage, shouldBe(""))
    }

    private fun `Then assert product item quantity`(
            productItemDataViewToATC: ProductItemDataView,
            addToCartSuccessModel: AddToCartDataModel,
    ) {
        val actualQuantity = productItemDataViewToATC.nonVariantATC?.quantity ?: -1
        val expectedQuantity = addToCartSuccessModel.data.quantity

        assertThat(actualQuantity, shouldBe(expectedQuantity))
    }

    fun `test add to cart failed`() {
        val responseErrorException = ResponseErrorException(
                "Jumlah barang melebihi stok di toko. Kurangi pembelianmu, ya!"
        )

        callback.`Given first page API will be successful`()
        `Given view already created`()
        `Given add to cart API will failed`(responseErrorException)

        val productItemList = baseViewModel.visitableListLiveData.value!!.getProductItemList()
        val productItemDataViewToATC = productItemList[0]
        val addToCartQty = 10

        `When add to cart a product`(productItemDataViewToATC, addToCartQty)

        `Then assert add to cart request params`(productItemDataViewToATC, addToCartQty)
        `Then assert add to cart error message from exception`(responseErrorException)
    }

    private fun `Given add to cart API will failed`(throwable: Throwable) {
        every {
            addToCartUseCase.execute(any(), any())
        } answers {
            secondArg<(Throwable) -> Unit>().invoke(throwable)
        }
    }

    private fun `Then assert add to cart error message from exception`(
            responseErrorMessage: ResponseErrorException
    ) {
        val addToCartEvent = baseViewModel.addToCartEventMessageLiveData.value!!
        val addToCartErrorMessage = addToCartEvent.getContentIfNotHandled()

        assertThat(addToCartErrorMessage, shouldBe(responseErrorMessage.message))
    }

    fun `add to cart with current quantity should do nothing`() {
        callback.`Given first page API will be successful`()
        `Given get mini cart simplified use case will be successful`(miniCartSimplifiedData)
        `Given view already created`()
        `Given view resumed to update mini cart`()

        val productItemList = baseViewModel.visitableListLiveData.value!!.getProductItemList()
        val productIdToATC = "574261655"
        val productInMiniCart = miniCartItems.find { it.productId == productIdToATC }!!
        val productCurrentQuantity = productInMiniCart.quantity
        val productInVisitable = productItemList.find { it.id == productIdToATC }!!

        `When add to cart a product`(productInVisitable, productCurrentQuantity)

        `Then verify add to cart use case is not called`()
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

    private fun `Then verify add to cart use case is not called`() {
        verify(exactly = 0) {
            addToCartUseCase.execute(any(), any())
        }
    }

    interface Callback {
        fun `Given first page API will be successful`()
    }
}