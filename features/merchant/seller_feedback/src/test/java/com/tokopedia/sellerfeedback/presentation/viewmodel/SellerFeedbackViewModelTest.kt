package com.tokopedia.sellerfeedback.presentation.viewmodel

import com.tokopedia.sellerfeedback.presentation.uimodel.ImageFeedbackUiModel
import com.tokopedia.unit.test.ext.verifyValueEquals
import org.junit.Test

class SellerFeedbackViewModelTest : SellerFeedbackViewModelTestFixture() {

    @Test
    fun `when set images should update local feedbackimages and livedata feedbackImages`() {
        val images = listOf<ImageFeedbackUiModel>()
        viewModel.setImages(images)
        viewModel.getFeedbackImages().verifyValueEquals(images)
    }

}