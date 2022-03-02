package com.tokopedia.addongifting.addonbottomsheet.view.viewmodel

import com.tokopedia.addongifting.addonbottomsheet.data.getaddonbyproduct.GetAddOnByProductResponse
import com.tokopedia.addongifting.addonbottomsheet.view.DataProvider
import com.tokopedia.purchase_platform.common.feature.gifting.domain.model.AddOnProductData
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.just
import org.junit.Test

class LoadAddOnTest: BaseAddOnTest() {

    @Test
    fun `WHEN load add on data success THEN should `() {
        // GIVEN
        val addOnProductData = AddOnProductData()
        val mockResponse = DataProvider.provideLoadAddOnDataSuccess()
        coEvery { getAddOnByProductUseCase.setParams(any()) } just Runs
        coEvery { getAddOnByProductUseCase.execute(any(), any()) } answers {
            firstArg<(GetAddOnByProductResponse) -> Unit>().invoke(mockResponse)
        }

        // WHEN
        viewModel.loadAddOnData(addOnProductData)

        // THEN
        assert(viewModel.uiEvent.replayCache.firstOrNull()?.)
    }

}