package com.tokopedia.chatbot.view.viewmodel

import com.tokopedia.chatbot.chatbot2.csat.domain.model.CsatModel
import com.tokopedia.chatbot.chatbot2.csat.domain.model.PointModel
import com.tokopedia.chatbot.chatbot2.csat.view.CsatEvent
import com.tokopedia.chatbot.chatbot2.csat.view.CsatUserAction
import com.tokopedia.chatbot.chatbot2.csat.view.CsatViewModel
import com.tokopedia.unit.test.rule.StandardTestRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class CsatViewModelTest {

    private lateinit var viewModel: CsatViewModel

    @Before
    fun setup() {
        viewModel = CsatViewModel(StandardTestRule().dispatchers)
    }

    @Test
    fun `WHEN initialize data with valid score THEN score should reflected correctly`() {
        // GIVEN
        val selectedScore = 3

        // WHEN
        viewModel.initializeData(selectedScore, csatModel)

        // THEN
        assert(viewModel.csatDataStateFlow.value.selectedPoint.score == selectedScore)
    }

    @Test
    fun `WHEN initialize data with invalid data THEN should call fallback`() {
        // GIVEN
        val selectedScore = 7

        // WHEN
        viewModel.initializeData(selectedScore, csatModel)

        // THEN
        assert(viewModel.csatEventFlow.replayCache[0] is CsatEvent.FallbackDismissBottomSheet)
    }

    @Test
    fun `WHEN user select score THEN data state should be updated correctly`() {
        // GIVEN
        val pointModel = PointModel(
            score = 3,
            caption = "Caption 3",
            reasonTitle = "Reason title",
            reasons = listOf("Reason 1", "Reason 2"),
            otherReasonTitle = "Other reason title"
        )

        // WHEN
        viewModel.processAction(CsatUserAction.SelectScore(pointModel))

        // THEN
        assert(viewModel.csatDataStateFlow.value.selectedPoint == pointModel)
    }

    @Test
    fun `WHEN user select reason THEN data state should be updated correctly`() {
        // GIVEN
        val reason = "Reason"

        // WHEN
        viewModel.processAction(CsatUserAction.SelectReason(reason))

        // THEN
        assert(viewModel.csatDataStateFlow.value.selectedReasons.contains(reason))
    }

    @Test
    fun `WHEN user unselect reason THEN data state should be updated correctly`() {
        // GIVEN
        val reason = "Reason"
        viewModel.csatDataStateFlow.value.selectedReasons = mutableListOf(reason)

        // WHEN
        viewModel.processAction(CsatUserAction.UnselectReason(reason))

        // THEN
        assert(!viewModel.csatDataStateFlow.value.selectedReasons.contains(reason))
    }

    @Test
    fun `WHEN user set other reason THEN data state should be updated correctly`() {
        // GIVEN
        val otherReason = "Other reason"

        // WHEN
        viewModel.processAction(CsatUserAction.SetOtherReason(otherReason))

        // THEN
        assert(viewModel.csatDataStateFlow.value.otherReason == otherReason)
    }

    @Test
    fun `WHEN user submit csat THEN data state should be updated correctly`() {
        // WHEN
        viewModel.processAction(CsatUserAction.SubmitCsat)

        // THEN
        assert(viewModel.csatEventFlow.replayCache.isNotEmpty())
    }

    @Test
    fun `WHEN reason and other reason valid THEN submit button should enabled`() {
        // GIVEN
        viewModel.csatDataStateFlow.value.minimumOtherReasonChar = 30
        val reason = "Reason"
        viewModel.csatDataStateFlow.value.selectedReasons = mutableListOf(reason)
        val otherReason = "Other reason reason reason reason reason reason"
        viewModel.processAction(CsatUserAction.SetOtherReason(otherReason))

        // WHEN
        viewModel.updateButton()

        // THEN
        assert((viewModel.csatEventFlow.replayCache[0] as CsatEvent.UpdateButton).isEnabled)
    }

    @Test
    fun `WHEN reason valid and other reason invalid THEN submit button should disabled`() {
        // GIVEN
        viewModel.csatDataStateFlow.value.minimumOtherReasonChar = 30
        val reason = "Reason"
        viewModel.csatDataStateFlow.value.selectedReasons = mutableListOf(reason)
        val otherReason = "Other reason"
        viewModel.processAction(CsatUserAction.SetOtherReason(otherReason))

        // WHEN
        viewModel.updateButton()

        // THEN
        assert(!(viewModel.csatEventFlow.replayCache[0] as CsatEvent.UpdateButton).isEnabled)
    }

    @Test
    fun `WHEN reason invalid and other reason valid THEN submit button should disabled`() {
        // GIVEN
        viewModel.csatDataStateFlow.value.minimumOtherReasonChar = 30
        val otherReason = "Other reason reason reason reason reason reason"
        viewModel.processAction(CsatUserAction.SetOtherReason(otherReason))

        // WHEN
        viewModel.updateButton()

        // THEN
        assert(!(viewModel.csatEventFlow.replayCache[0] as CsatEvent.UpdateButton).isEnabled)
    }

    @Test
    fun `WHEN reason invalid and other reason invalid THEN submit button should disabled`() {
        // GIVEN
        viewModel.csatDataStateFlow.value.minimumOtherReasonChar = 30
        val otherReason = ""
        viewModel.processAction(CsatUserAction.SetOtherReason(otherReason))

        // WHEN
        viewModel.updateButton()

        // THEN
        assert(!(viewModel.csatEventFlow.replayCache[0] as CsatEvent.UpdateButton).isEnabled)
    }

    @Test
    fun `WHEN reason and other reason invalid THEN submit button should enabled`() {
    }

    companion object {
        val csatModel = CsatModel(
            title = "Gimana pengalamanmu dengan Tokopedia Care?",
            minimumOtherReasonChar = 30,
            points = mutableListOf(
                PointModel(
                    score = 1,
                    caption = "Sangat Tidak Puas",
                    reasonTitle = "Apa yang menurut kamu kurang?",
                    reasons = listOf(
                        "Reason 1"
                    ),
                    otherReasonTitle = "Ada kendala waktu kamu pakai layanan Tokopedia Care?"
                ),
                PointModel(
                    score = 2,
                    caption = "Tidak Puas",
                    reasonTitle = "Apa yang menurut kamu kurang?",
                    reasons = listOf(
                        "Reason 2",
                        "Reason 22"
                    ),
                    otherReasonTitle = "Ada kendala waktu kamu pakai layanan Tokopedia Care?"
                ),
                PointModel(
                    score = 3,
                    caption = "Kurang Puas",
                    reasonTitle = "Apa yang menurut kamu kurang?",
                    reasons = listOf(
                        "Reason 3",
                        "Reason 33",
                        "Reason 333"
                    ),
                    otherReasonTitle = "Ada kendala waktu kamu pakai layanan Tokopedia Care?"
                ),
                PointModel(
                    score = 4,
                    caption = "Puas",
                    reasonTitle = "Apa yang menurut kamu kurang?",
                    reasons = listOf(
                        "Reason 4",
                        "Reason 44",
                        "Reason 444",
                        "Reason 4444"
                    ),
                    otherReasonTitle = "Ada kendala waktu kamu pakai layanan Tokopedia Care?"
                ),
                PointModel(
                    score = 5,
                    caption = "Sangat Puas",
                    reasonTitle = "Apa yang menurut kamu kurang?",
                    reasons = listOf(
                        "Reason 5",
                        "Reason 55",
                        "Reason 555",
                        "Reason 5555",
                        "Reason 55555"
                    ),
                    otherReasonTitle = "Ada kendala waktu kamu pakai layanan Tokopedia Care?"
                )
            )
        )
    }
}
