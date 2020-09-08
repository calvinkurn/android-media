package com.tokopedia.productcard.options

import com.tokopedia.atc_common.data.model.request.AddToCartRequestParams
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.model.response.DataModel
import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase.Companion.REQUEST_PARAM_KEY_ADD_TO_CART_REQUEST
import com.tokopedia.discovery.common.model.ProductCardOptionsModel
import com.tokopedia.productcard.options.testutils.complete
import com.tokopedia.productcard.options.testutils.error
import com.tokopedia.usecase.RequestParams
import io.mockk.every
import io.mockk.slot
import io.mockk.verify
import org.junit.Test
import rx.Subscriber
import java.lang.Exception

internal class ClickAddToCartOptionsTest: ProductCardOptionsViewModelTestFixtures() {

    private val productCardOptionsModelATC = ProductCardOptionsModel(
            hasAddToCart = true,
            productId = "12345",
            productName = "Product Name",
            shopId = "123456",
            categoryName = "Handphone",
            formattedPrice = "Rp32.900",
            addToCartParams = ProductCardOptionsModel.AddToCartParams(quantity = 1)
    )

    private val addToCartSuccessModel = AddToCartDataModel(
            status = AddToCartDataModel.STATUS_OK,
            data = DataModel(
                    success = 1,
                    cartId = "12345",
                    message = arrayListOf()
            )
    )

    private val addToCartFailedModel = AddToCartDataModel(
            status = AddToCartDataModel.STATUS_ERROR,
            errorMessage = arrayListOf<String>().also {
                it.add("Jumlah barang melebihi stok di toko. Kurangi pembelianmu, ya!")
            },
            data = DataModel(
                    success = 0
            )
    )

    private val addToCartRequestParamsSlot = slot<RequestParams>()

    @Test
    fun `Click add to cart for non login user should redirect to login page`() {
        `Given Product Card Options View Model with ATC enabled`()
        `Given User is not logged in`()

        `When click add to cart`()

        `Then should redirect to login page`()
        `Then should not execute add to cart use case`()
    }

    private fun `Given Product Card Options View Model with ATC enabled`() {
        createProductCardOptionsViewModel(productCardOptionsModelATC)
    }

    private fun `Given User is not logged in`() {
        every { userSession.isLoggedIn }.returns(false)
    }

    private fun `When click add to cart`() {
        productCardOptionsViewModel.getOption(ADD_TO_CART).onClick()
    }

    private fun `Then should redirect to login page`() {
        val isRouteToLoginPage = productCardOptionsViewModel.routeToLoginPageEventLiveData().value?.getContentIfNotHandled()

        isRouteToLoginPage.shouldBe(true, "Should route to login page.")
    }

    private fun `Then should not execute add to cart use case`() {
        verify (exactly = 0) { addToCartUseCase.execute(any(), any()) }
    }

    @Test
    fun `Click add to cart success should return success result`() {
        `Given Product Card Options View Model with ATC enabled`()
        `Given user is logged in`()
        `Given add to cart API will successs`()

        `When click add to cart`()

        `Then should not redirect to login page`()
        `Then verify add to cart use case is executed with correct input`()
        `Then should post add to cart event`()
        `Then verify add to cart result success`()
    }

    private fun `Given user is logged in`() {
        every { userSession.isLoggedIn }.returns(true)
        every { userSession.userId } returns "12345"
    }

    private fun `Given add to cart API will successs`() {
        every { addToCartUseCase.execute(capture(addToCartRequestParamsSlot), any()) } answers {
            secondArg<Subscriber<AddToCartDataModel>>().complete(addToCartSuccessModel)
        }
    }

    private fun `Then should not redirect to login page`() {
        productCardOptionsViewModel.routeToLoginPageEventLiveData().value shouldBe null
    }

    private fun `Then verify add to cart use case is executed with correct input`() {
        val requestParams = addToCartRequestParamsSlot.captured
        val addToCartRequestParams = requestParams.parameters[REQUEST_PARAM_KEY_ADD_TO_CART_REQUEST] as AddToCartRequestParams

        addToCartRequestParams.productId.toString() shouldBe productCardOptionsModelATC.productId
        addToCartRequestParams.shopId.toString() shouldBe productCardOptionsModelATC.shopId
        addToCartRequestParams.quantity shouldBe productCardOptionsModelATC.addToCartParams!!.quantity
        addToCartRequestParams.productName shouldBe productCardOptionsModelATC.productName
        addToCartRequestParams.category shouldBe productCardOptionsModelATC.categoryName
        addToCartRequestParams.price shouldBe productCardOptionsModelATC.formattedPrice
    }

    private fun `Then should post add to cart event`() {
        val addToCartEvent = productCardOptionsViewModel.getAddToCartEventLiveData().value

        addToCartEvent?.getContentIfNotHandled() shouldBe true
    }

    private fun `Then verify add to cart result success`() {
        val addToCartResult = productCardOptionsViewModel.productCardOptionsModel!!.addToCartResult

        addToCartResult.isSuccess shouldBe true
        addToCartResult.errorMessage shouldBe ""
    }

    @Test
    fun `Click add to cart with status fail should return error result with given error message`() {
        `Given Product Card Options View Model with ATC enabled`()
        `Given user is logged in`()
        `Given add to cart API will return failed status`()

        `When click add to cart`()

        `Then should not redirect to login page`()
        `Then verify add to cart use case is executed with correct input`()
        `Then should post add to cart event`()
        `Then verify add to cart result failed with error message`(addToCartFailedModel.getAtcErrorMessage())
    }

    private fun `Given add to cart API will return failed status`() {
        every { addToCartUseCase.execute(capture(addToCartRequestParamsSlot), any()) } answers {
            secondArg<Subscriber<AddToCartDataModel>>().complete(addToCartFailedModel)
        }
    }

    private fun `Then verify add to cart result failed with error message`(errorMessage: String?) {
        val addToCartResult = productCardOptionsViewModel.productCardOptionsModel!!.addToCartResult

        addToCartResult.isSuccess shouldBe false
        addToCartResult.errorMessage shouldBe (errorMessage ?: "")
    }

    @Test
    fun `Click add to cart error should return error result with default error message`() {
        `Given Product Card Options View Model with ATC enabled`()
        `Given user is logged in`()
        `Given add to cart API will give error`()

        `When click add to cart`()

        `Then should not redirect to login page`()
        `Then verify add to cart use case is executed with correct input`()
        `Then should post add to cart event`()
        `Then verify add to cart result failed with error message`(ATC_DEFAULT_ERROR_MESSAGE)
    }

    private fun `Given add to cart API will give error`() {
        every { addToCartUseCase.execute(capture(addToCartRequestParamsSlot), any()) } answers {
            secondArg<Subscriber<AddToCartDataModel>>().error(Exception("ATC Error"))
        }
    }
}