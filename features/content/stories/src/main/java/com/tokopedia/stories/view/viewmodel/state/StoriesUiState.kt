package com.tokopedia.stories.view.viewmodel.state

import com.tokopedia.content.common.report_content.model.PlayUserReportReasoningUiModel
import com.tokopedia.content.common.types.ResultState
import com.tokopedia.content.common.view.ContentTaggedProductUiModel
import com.tokopedia.stories.view.model.StoriesCampaignUiModel
import com.tokopedia.stories.view.model.StoriesDetailItem
import com.tokopedia.stories.view.model.StoriesUiModel
import com.tokopedia.usecase.coroutines.Result

data class StoriesUiState(
    val storiesMainData: StoriesUiModel,
    val productSheet: ProductBottomSheetUiState,
    val timerStatus: TimerStatusInfo,
    val reportState: StoryReportStatusInfo,
    val canShowGroup: Boolean
) {
    companion object {
        val Empty
            get() = StoriesUiState(
                storiesMainData = StoriesUiModel(),
                productSheet = ProductBottomSheetUiState.Empty,
                timerStatus = TimerStatusInfo.Empty,
                reportState = StoryReportStatusInfo.Empty,
                canShowGroup = true
            )
    }
}

enum class BottomSheetType {
    Kebab, Product, Sharing, Report, SubmitReport, Unknown;
}

data class ProductBottomSheetUiState(
    val products: List<ContentTaggedProductUiModel>,
    val campaign: StoriesCampaignUiModel,
    val resultState: ResultState
) {
    companion object {
        val Empty
            get() = ProductBottomSheetUiState(
                products = emptyList(),
                campaign = StoriesCampaignUiModel.Unknown,
                resultState = ResultState.Loading
            )
    }
}

val Map<BottomSheetType, Boolean>.isAnyShown: Boolean
    get() = values.any { it }
val BottomSheetStatusDefault: Map<BottomSheetType, Boolean>
    get() = mapOf(
        BottomSheetType.Sharing to false,
        BottomSheetType.Product to false,
        BottomSheetType.Kebab to false,
        BottomSheetType.Report to false,
        BottomSheetType.SubmitReport to false
    )

data class TimerStatusInfo(
    val event: StoriesDetailItem.StoriesDetailItemUiEvent,
    val story: StoryTimer
) {
    companion object {
        data class StoryTimer(
            val id: String,
            val itemCount: Int,
            val resetValue: Int,
            val duration: Int,
            val position: Int
        ) {
            companion object {
                val Empty
                    get() = StoryTimer(
                        id = "",
                        itemCount = 1,
                        resetValue = 0,
                        duration = 3000,
                        position = 0
                    )
            }
        }

        val Empty
            get() = TimerStatusInfo(
                StoriesDetailItem.StoriesDetailItemUiEvent.PAUSE,
                StoryTimer.Empty
            )
    }
}

data class StoryReportStatusInfo(
    val state: ReportState,
    val report: StoryReport
) {

    data class StoryReport(
        val reasonList: List<PlayUserReportReasoningUiModel.Reasoning>,
        val selectedReason: PlayUserReportReasoningUiModel.Reasoning?,
        val submitStatus: Result<Unit>?
    ) {
        companion object {
            val Empty = StoryReport(
                reasonList = emptyList(),
                selectedReason = null,
                submitStatus = null
            )
        }
    }

    enum class ReportState {
        None, OnSelectReason, OnSubmit, Submitted
    }

    companion object {
        val Empty = StoryReportStatusInfo(
            state = ReportState.None,
            report = StoryReport.Empty
        )
    }
}
