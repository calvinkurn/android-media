package com.tokopedia.cart.bundle.junit

import com.tokopedia.cartcommon.data.response.undodeletecart.Data
import com.tokopedia.cartcommon.data.response.undodeletecart.UndoDeleteCartDataResponse
import com.tokopedia.network.exception.ResponseErrorException
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.just
import io.mockk.verify
import org.junit.Test

class UndoDeleteTest : BaseCartTest() {

    @Test
    fun `WHEN undo delete success THEN should render success`() {
        // GIVEN
        val response = UndoDeleteCartDataResponse(
                status = "OK",
                data = Data(success = 1, message = listOf("success message"))
        )

        coEvery { undoDeleteCartUseCase.setParams(any()) } just Runs
        coEvery { undoDeleteCartUseCase.execute(any(), any()) } answers {
            firstArg<(UndoDeleteCartDataResponse) -> Unit>().invoke(response)
        }

        // WHEN
        cartListPresenter?.processUndoDeleteCartItem(listOf("123"))

        // THEN
        verify {
            view.onUndoDeleteCartDataSuccess()
        }
    }

    @Test
    fun `WHEN undo delete failed THEN should render error`() {
        // GIVEM
        val errorMessage = "Error Message"
        val throwable = ResponseErrorException(errorMessage)

        coEvery { undoDeleteCartUseCase.setParams(any()) } just Runs
        coEvery { undoDeleteCartUseCase.execute(any(), any()) } answers {
            secondArg<(Throwable) -> Unit>().invoke(throwable)
        }

        // WHEN
        cartListPresenter?.processUndoDeleteCartItem(listOf("123"))

        // THEN
        verify {
            view.showToastMessageRed(throwable)
        }
    }
}