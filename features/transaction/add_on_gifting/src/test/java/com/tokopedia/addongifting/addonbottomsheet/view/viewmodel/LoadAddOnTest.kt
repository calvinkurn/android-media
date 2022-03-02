package com.tokopedia.addongifting.addonbottomsheet.view.viewmodel

import com.tokopedia.abstraction.common.network.exception.ResponseErrorException
import com.tokopedia.addongifting.addonbottomsheet.data.getaddonbyproduct.GetAddOnByProductResponse
import com.tokopedia.addongifting.addonbottomsheet.view.DataProvider
import com.tokopedia.addongifting.addonbottomsheet.view.UiEvent
import com.tokopedia.purchase_platform.common.feature.gifting.domain.model.AddOnProductData
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.just
import org.junit.Test

class LoadAddOnTest: BaseAddOnTest() {

    @Test
    fun `WHEN load add on data error THEN should ui state should be STATE_FAILED_LOAD_ADD_ON_DATA`() {
        // GIVEN
        val addOnProductData = AddOnProductData()
        coEvery { getAddOnByProductUseCase.setParams(any()) } just Runs
        coEvery { getAddOnByProductUseCase.execute(any(), any()) } answers {
            secondArg<(Throwable) -> Unit>().invoke(ResponseErrorException())
        }

        // WHEN
        viewModel.loadAddOnData(addOnProductData)

        // THEN
        assert(viewModel.uiEvent.replayCache.firstOrNull()?.state == UiEvent.STATE_FAILED_LOAD_ADD_ON_DATA)
    }

}