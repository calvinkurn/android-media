package com.tokopedia.flight.cancellationV2.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.common.travel.utils.TravelTestDispatcherProvider
import com.tokopedia.flight.cancellationV2.data.FlightCancellationPassengerEntity
import com.tokopedia.flight.cancellationV2.data.FlightCancellationReasonDataCacheSource
import com.tokopedia.flight.dummy.DUMMY_REASON
import com.tokopedia.flight.dummy.DUMMY_REASON_LIST
import com.tokopedia.flight.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * @author by furqan on 22/07/2020
 */
class FlightCancellationChooseReasonViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()
    private val testDispatcherProvider = TravelTestDispatcherProvider()

    private val reasonDataCache: FlightCancellationReasonDataCacheSource = mockk()

    private lateinit var viewModel: FlightCancellationChooseReasonViewModel

    @Before
    fun setUp() {
        viewModel = FlightCancellationChooseReasonViewModel(reasonDataCache, testDispatcherProvider)
    }

    @Test
    fun isReasonChecked_nullSelectedReason_shouldBeFalse() {
        // given

        // when
        val isReasonChecked = viewModel.isReasonChecked(FlightCancellationPassengerEntity.Reason())

        // then
        isReasonChecked shouldBe false
    }

    @Test
    fun isReasonChecked_differentSelectedReason_shouldBeFalse() {
        // given
        viewModel.selectedReason = DUMMY_REASON

        // when
        val isReasonChecked = viewModel.isReasonChecked(FlightCancellationPassengerEntity.Reason())

        // then
        isReasonChecked shouldBe false
    }

    @Test
    fun isReasonChecked_sameSelectedReason_shouldBeTrue() {
        // given
        viewModel.selectedReason = DUMMY_REASON

        // when
        val isReasonChecked = viewModel.isReasonChecked(DUMMY_REASON)

        // then
        isReasonChecked shouldBe true
    }

    @Test
    fun loadReasonList() {
        // given
        coEvery { reasonDataCache.getCache() } returns DUMMY_REASON_LIST

        // when
        viewModel.loadReasonList()

        // then
        viewModel.reasonList.value!!.size shouldBe DUMMY_REASON_LIST.size

        for ((index, reason) in viewModel.reasonList.value!!.withIndex()) {
            reason.id shouldBe DUMMY_REASON_LIST[index].id
            reason.title shouldBe DUMMY_REASON_LIST[index].title
            reason.requiredDocs.size shouldBe DUMMY_REASON_LIST[index].requiredDocs.size
            reason.formattedRequiredDocs.size shouldBe DUMMY_REASON_LIST[index].formattedRequiredDocs.size

            for ((docIndex, doc) in reason.formattedRequiredDocs.withIndex()) {
                doc.id shouldBe reason.formattedRequiredDocs[docIndex].id
                doc.title shouldBe reason.formattedRequiredDocs[docIndex].title
            }
        }
    }
}