package com.tokopedia.flight.cancellation_navigation.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.flight.cancellation.data.FlightCancellationPassengerEntity
import com.tokopedia.flight.cancellation.data.FlightCancellationReasonDataCacheSource
import com.tokopedia.flight.dummy.DUMMY_REASON
import com.tokopedia.flight.dummy.DUMMY_REASON_LIST
import com.tokopedia.flight.shouldBe
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * @author by furqan on 27/05/2021
 */
class FlightCancellationChooseReasonViewModelTest {


    @get:Rule
    val rule = InstantTaskExecutorRule()
    private val testDispatcherProvider = CoroutineTestDispatchersProvider

    private val reasonDataCache: FlightCancellationReasonDataCacheSource = mockk()

    private lateinit var viewModel: FlightCancellationBottomSheetChooseReasonViewModel

    @Before
    fun setUp() {
        viewModel = FlightCancellationBottomSheetChooseReasonViewModel(reasonDataCache, testDispatcherProvider)
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
        viewModel.selectedReason shouldBe DUMMY_REASON
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
    }}