package com.tokopedia.developer_options.presentation.feedbackpage.domain.model

import com.tokopedia.feedback_form.feedbackpage.domain.model.ImageFeedbackUiModel
import com.tokopedia.screenshot_observer.ScreenshotData
import javax.inject.Inject

class ScreenshotResultMapper @Inject constructor(){

    fun mapData(data: ScreenshotData) : ImageFeedbackUiModel {
        return ImageFeedbackUiModel().apply {
            imageUrl = data.path
        }
    }
}