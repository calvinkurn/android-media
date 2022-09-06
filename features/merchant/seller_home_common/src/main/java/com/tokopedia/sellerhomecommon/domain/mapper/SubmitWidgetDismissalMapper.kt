package com.tokopedia.sellerhomecommon.domain.mapper

import com.tokopedia.sellerhomecommon.domain.model.WidgetDismissWithFeedbackResponse
import com.tokopedia.sellerhomecommon.presentation.model.SubmitWidgetDismissUiModel
import com.tokopedia.sellerhomecommon.presentation.model.WidgetDismissalResultUiModel
import javax.inject.Inject

/**
 * Created by @ilhamsuaib on 05/09/22.
 */

class SubmitWidgetDismissalMapper @Inject constructor() {

    fun mapRemoteModelToUiModel(
        param: SubmitWidgetDismissUiModel,
        model: WidgetDismissWithFeedbackResponse
    ): WidgetDismissalResultUiModel {
        return WidgetDismissalResultUiModel(
            widgetId = param.feedbackWidgetIDParent,
            itemIds = param.dismissObjectIDs,
            dismissToken = model.data.dismissToken,
            action = param.action
        )
    }
}