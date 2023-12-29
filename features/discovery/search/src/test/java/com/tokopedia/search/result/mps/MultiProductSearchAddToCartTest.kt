package com.tokopedia.search.result.mps

import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel.Companion.STATUS_OK
import com.tokopedia.atc_common.domain.model.response.DataModel
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartUseCase
import com.tokopedia.discovery.common.State
import com.tokopedia.search.TestException
import com.tokopedia.search.jsonToObject
import com.tokopedia.search.result.mps.shopwidget.MPSShopWidgetDataView
import com.tokopedia.search.result.mps.shopwidget.MPSShopWidgetProductDataView
import io.mockk.coEvery
import io.mockk.verify
import org.hamcrest.core.Is.`is`
import org.hamcrest.core.IsInstanceOf.instanceOf
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertThat
import org.junit.Test

class MultiProductSearchAddToCartTest: MultiProductSearchTestFixtures() {

    @Test
    fun `add to cart shop widget product`() {
        val mpsState = MPSState().success(MPSSuccessJSON.jsonToObject())
        val mpsViewModel = mpsViewModel(mpsState)

        val (mpsShopWidgetDataView, mpsShopWidgetProduct) = mpsState.getAddToCartTestProduct()

        `When add to cart`(mpsViewModel, mpsShopWidgetDataView, mpsShopWidgetProduct)

        verify {
            addToCartUseCase.setParams(AddToCartUseCase.getMinimumParams(
                mpsShopWidgetProduct.id,
                mpsShopWidgetDataView.id,
                mpsShopWidgetProduct.minOrder))

            addToCartUseCase.execute(any(), any())
        }
    }

    private fun MPSState.getAddToCartTestProduct(
    ): Pair<MPSShopWidgetDataView, MPSShopWidgetProductDataView> {

        val mpsShopWidgetDataView = visitableList
            .filterIsInstance<MPSShopWidgetDataView>()
            .first()

        val mpsShopWidgetProduct = mpsShopWidgetDataView.productList.first()

        return Pair(mpsShopWidgetDataView, mpsShopWidgetProduct)
    }

    private fun `When add to cart`(
        mpsViewModel: MPSViewModel,
        mpsShopWidgetDataView: MPSShopWidgetDataView,
        mpsShopWidgetProduct: MPSShopWidgetProductDataView
    ) {
        mpsViewModel.onAddToCart(mpsShopWidgetDataView, mpsShopWidgetProduct)
    }

    @Test
    fun `add to cart shop widget product success`() {
        val mpsState = MPSState().success(MPSSuccessJSON.jsonToObject())
        val mpsViewModel = mpsViewModel(mpsState)
        val (mpsShopWidgetDataView, mpsShopWidgetProduct) = mpsState.getAddToCartTestProduct()

        `Given add to cart model will be successful`()

        `When add to cart`(mpsViewModel, mpsShopWidgetDataView, mpsShopWidgetProduct)

        `Then assert add to cart success state`(mpsViewModel)
    }

    private fun `Given add to cart model will be successful`() {
        coEvery { addToCartUseCase.execute(any(), any()) } answers {
            firstArg<(AddToCartDataModel) -> Unit>().invoke(successAddToCartModel)
        }
    }

    private fun `Then assert add to cart success state`(mpsViewModel: MPSViewModel) {
        assertThat(
            mpsViewModel.stateValue.addToCartState.state,
            `is`(instanceOf(State.Success::class.java))
        )

        assertEquals(
            mpsViewModel.stateValue.addToCartState.state?.data,
            successAddToCartModel,
        )
    }

    @Test
    fun `add to cart shop widget product failed`() {
        val mpsState = MPSState().success(MPSSuccessJSON.jsonToObject())
        val mpsViewModel = mpsViewModel(mpsState)
        val (mpsShopWidgetDataView, mpsShopWidgetProduct) = mpsState.getAddToCartTestProduct()
        val testException = TestException("error atc")

        `Given add to cart model will throw exception`(testException)

        `When add to cart`(mpsViewModel, mpsShopWidgetDataView, mpsShopWidgetProduct)

        `Then assert add to cart error state`(mpsViewModel, testException)
    }

    private fun `Given add to cart model will throw exception`(testException: TestException) {
        coEvery { addToCartUseCase.execute(any(), any()) } answers {
            secondArg<(Throwable) -> Unit>().invoke(testException)
        }
    }

    private fun `Then assert add to cart error state`(
        mpsViewModel: MPSViewModel,
        testException: TestException
    ) {
        assertThat(
            mpsViewModel.stateValue.addToCartState.state,
            `is`(instanceOf(State.Error::class.java))
        )

        val addToCartErrorState =
            mpsViewModel.stateValue.addToCartState.state as State.Error<AddToCartDataModel>

        assertEquals(addToCartErrorState.throwable, testException)
        assertEquals(addToCartErrorState.message, testException.message)
    }

    @Test
    fun `add to cart widget message dismissed`() {
        val mpsState = MPSState().successAddToCart(successAddToCartModel)
        val mpsViewModel = mpsViewModel(mpsState)

        mpsViewModel.onAddToCartMessageDismissed()

        assertNull(mpsViewModel.stateValue.addToCartState.state)
    }

    companion object {

        private val successAddToCartModel = AddToCartDataModel(
            status = STATUS_OK,
            data = DataModel(success = 1),
        )
    }
}
