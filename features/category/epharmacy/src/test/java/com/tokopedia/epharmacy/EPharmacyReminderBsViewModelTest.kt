package com.tokopedia.epharmacy

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.epharmacy.network.request.EpharmacyUserReminderParam
import com.tokopedia.epharmacy.network.response.EPharmacyHeader
import com.tokopedia.epharmacy.network.response.EPharmacyReminderScreenResponse
import com.tokopedia.epharmacy.usecase.*
import com.tokopedia.epharmacy.viewmodel.EPharmacyReminderBsViewModel
import com.tokopedia.usecase.coroutines.Fail
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.lang.Exception

@ExperimentalCoroutinesApi
class EPharmacyReminderBsViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
    private val ePharmacyReminderScreenUseCase = mockk<EPharmacyReminderScreenUseCase>(relaxed = true)

    private val dispatcherBackground = TestCoroutineDispatcher()
    private lateinit var viewModel: EPharmacyReminderBsViewModel

    @Before
    fun setUp() {
        viewModel = EPharmacyReminderBsViewModel(
            ePharmacyReminderScreenUseCase,
            dispatcherBackground
        )
    }

    /**************************** test for getEPharmacyMiniConsultationDetail() *******************************************/
    @Test
    fun `test for getEPharmacyMiniConsultationDetail when response is not null`() {
        val params = EpharmacyUserReminderParam(
            input = EpharmacyUserReminderParam.Input(
                reminderType = 1,
                EpharmacyUserReminderParam.Input.EpharmacyConsultationInfoParams(
                    consultationSourceId = 9, ""
                )
            )
        )
        val headerData = mockk<EPharmacyHeader>()
        val screenData = EPharmacyReminderScreenResponse.SubmitEPharmacyUserReminderData.ReminderScreenData(true, "")
        val responseData = EPharmacyReminderScreenResponse.SubmitEPharmacyUserReminderData(headerData, screenData)
        val response = EPharmacyReminderScreenResponse(responseData)
        coEvery {
            ePharmacyReminderScreenUseCase(params)
        } returns response
        viewModel.setForReminder(params)

        TestCase.assertEquals(viewModel.reminderLiveData.value != null, true)
    }

    @Test
    fun `test for getEPharmacyMiniConsultationDetail when when response is null`() {
        val params = EpharmacyUserReminderParam(
            input = EpharmacyUserReminderParam.Input(
                reminderType = 1,
                EpharmacyUserReminderParam.Input.EpharmacyConsultationInfoParams(
                    consultationSourceId = 9 ,""
                )
            )
        )
        val responseData = EPharmacyReminderScreenResponse.SubmitEPharmacyUserReminderData(null, null)
        val response = EPharmacyReminderScreenResponse(responseData)
        coEvery {
            ePharmacyReminderScreenUseCase(params)
        } returns response

        viewModel.setForReminder(params)

        TestCase.assertEquals(viewModel.reminderLiveData.value is Fail, true)
    }

    @Test
    fun `test for getEPharmacyMiniConsultationDetail when when throws error`() {
        val params = EpharmacyUserReminderParam(
            input = EpharmacyUserReminderParam.Input(
                reminderType = 1,
                EpharmacyUserReminderParam.Input.EpharmacyConsultationInfoParams(
                    consultationSourceId = 9 , ""
                )
            )
        )
        coEvery {
            ePharmacyReminderScreenUseCase(params)
        } throws Exception()

        viewModel.setForReminder(params)

        TestCase.assertEquals(viewModel.reminderLiveData.value is Fail, true)
    }
}
