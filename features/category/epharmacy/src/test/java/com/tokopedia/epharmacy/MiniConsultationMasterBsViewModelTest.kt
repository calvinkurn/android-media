package com.tokopedia.epharmacy

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.epharmacy.network.params.GetMiniConsultationBottomSheetParams
import com.tokopedia.epharmacy.network.response.EPharmacyMiniConsultationMasterResponse
import com.tokopedia.epharmacy.network.response.GetEpharmacyMiniConsultationStaticData
import com.tokopedia.epharmacy.usecase.*
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.epharmacy.viewmodel.MiniConsultationMasterBsViewModel
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class MiniConsultationMasterBsViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
    private val getEPharmacyMiniConsultationMasterUseCase = mockk<GetEPharmacyMiniConsultationMasterUseCase>(relaxed = true)

    private val dispatcherBackground = TestCoroutineDispatcher()
    private lateinit var viewModel: MiniConsultationMasterBsViewModel


    @Before
    fun setUp() {
        viewModel = MiniConsultationMasterBsViewModel(
            getEPharmacyMiniConsultationMasterUseCase,
            dispatcherBackground)
    }

    /**************************** test for getEPharmacyMiniConsultationDetail() *******************************************/
    @Test
    fun `test for getEPharmacyMiniConsultationDetail when response is not null`() {
        val enablerParam = GetMiniConsultationBottomSheetParams.EpharmacyStaticInfoParams(enablerName = "obat_keras_tnc")
        val params = GetMiniConsultationBottomSheetParams(dataType = "halodoc", params = enablerParam)
        val ePharmacyMiniConsultationData = EPharmacyMiniConsultationMasterResponse.EPharmacyMiniConsultationData(infoText = "xyz", infoTitle = "abc")
        val epharmacyStaticData = GetEpharmacyMiniConsultationStaticData(
            getEpharmacyStaticData = EPharmacyMiniConsultationMasterResponse(data = ePharmacyMiniConsultationData, error = ""))
        coEvery {
            getEPharmacyMiniConsultationMasterUseCase(params)
        } returns epharmacyStaticData

        viewModel.getEPharmacyMiniConsultationDetail(params)

        TestCase.assertEquals(viewModel.miniConsultationLiveData.value != null, true)
    }

    @Test
    fun `test for getEPharmacyMiniConsultationDetail when when response is null`() {
        val enablerParam = GetMiniConsultationBottomSheetParams.EpharmacyStaticInfoParams(enablerName = "obat_keras_tnc")
        val params = GetMiniConsultationBottomSheetParams(dataType = "halodoc", params = enablerParam)
        val epharmacyStaticData = GetEpharmacyMiniConsultationStaticData(
            getEpharmacyStaticData = null)
        coEvery {
            getEPharmacyMiniConsultationMasterUseCase(params)
        } returns epharmacyStaticData

        viewModel.getEPharmacyMiniConsultationDetail(params)

        TestCase.assertEquals(viewModel.miniConsultationLiveData.value is Fail, true)
    }

    @Test
    fun `test for getEPharmacyMiniConsultationDetail when when throws error`() {
        val enablerParam = GetMiniConsultationBottomSheetParams.EpharmacyStaticInfoParams(enablerName = "obat_keras_tnc")
        val params = GetMiniConsultationBottomSheetParams(dataType = "halodoc", params = enablerParam)
        coEvery {
            getEPharmacyMiniConsultationMasterUseCase(params)
        } throws Exception()

        viewModel.getEPharmacyMiniConsultationDetail(params)

        TestCase.assertEquals(viewModel.miniConsultationLiveData.value is Fail, true)
    }

}
