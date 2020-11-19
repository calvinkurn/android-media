package com.tokopedia.product.manage.feature.etalase.view.viewmodel

import com.tokopedia.unit.test.ext.verifyErrorEquals
import com.tokopedia.unit.test.ext.verifySuccessEquals
import com.tokopedia.product.manage.feature.etalase.data.model.EtalaseViewModel
import com.tokopedia.shop.common.graphql.data.shopetalase.ShopEtalaseModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.coVerify
import junit.framework.Assert.assertEquals
import org.junit.Test
import rx.Observable

class EtalasePickerViewModelTest: EtalasePickerViewModelTestFixture() {

    @Test
    fun when_get_etalase_list_success__should_set_result_success() {
        val index = 0
        val etalaseId = "1"
        val etalaseName = "Indomie Rebus"
        val etalaseList = arrayListOf(ShopEtalaseModel(id = etalaseId, name = etalaseName))

        onGetEtalaseList_thenReturn(etalaseList)

        viewModel.getEtalaseList("1")

        val expectedResult = Success(listOf(EtalaseViewModel(etalaseId, etalaseName, index)))

        viewModel.getEtalaseResult
            .verifySuccessEquals(expectedResult)
    }

    @Test
    fun when_get_etalase_list_error__should_set_result_fail() {
        val error = NullPointerException()
        onGetEtalaseList_thenError(error)

        viewModel.getEtalaseList("1")

        val expectedResult = Fail(error)

        viewModel.getEtalaseResult
            .verifyErrorEquals(expectedResult)
    }

    @Test
    fun when_clear_etalase_cache__should_call_get_etalase_use_case_clearCache() {
        viewModel.clearGetEtalaseCache()

        verifyClearEtalaseCacheCalled()
    }

    @Test
    fun when_set_selected_etalase__should_update_selected_etalase() {
        viewModel.selectedEtalase = EtalaseViewModel("1", "Mie Sedap", 0)

        val expectedSelectedEtalase = EtalaseViewModel("1", "Mie Sedap", 0)
        val actualSelectedEtalase = viewModel.selectedEtalase

        assertEquals(expectedSelectedEtalase, actualSelectedEtalase)
    }

    private fun onGetEtalaseList_thenReturn(etalaseList: ArrayList<ShopEtalaseModel>) {
        coEvery { getShopEtalaseByShopUseCase.createObservable(any()) } returns Observable.just(etalaseList)
    }

    private fun onGetEtalaseList_thenError(error: Throwable) {
        coEvery { getShopEtalaseByShopUseCase.createObservable(any()) } throws error
    }

    private fun verifyClearEtalaseCacheCalled() {
        coVerify { getShopEtalaseByShopUseCase.clearCache() }
    }
}