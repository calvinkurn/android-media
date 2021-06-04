package com.tokopedia.tokomart.searchcategory

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.atc_common.data.model.request.AddToCartRequestParams
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.model.response.DataModel
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartUseCase
import com.tokopedia.network.exception.ResponseErrorException
import com.tokopedia.tokomart.searchcategory.presentation.model.ProductItemDataView
import com.tokopedia.tokomart.searchcategory.presentation.viewmodel.BaseSearchCategoryViewModel
import io.mockk.every
import io.mockk.slot
import io.mockk.verify
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.CoreMatchers.`is` as shouldBe

class AddToCartNonVariantTestHelper(
        private val baseViewModel: BaseSearchCategoryViewModel,
        private val addToCartUseCase: AddToCartUseCase,
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
                        message = arrayListOf()
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
        val addToCartErrorMessage = baseViewModel.addToCartErrorMessageLiveData.value

        assertThat(addToCartErrorMessage, shouldBe(""))
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
        val addToCartErrorMessage = baseViewModel.addToCartErrorMessageLiveData.value

        assertThat(addToCartErrorMessage, shouldBe(responseErrorMessage.message))
    }

    interface Callback {
        fun `Given first page API will be successful`()
    }
}