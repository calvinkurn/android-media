package com.tokopedia.feedback_form.feedbackpage.domain.mapper

import com.tokopedia.feedback_form.feedbackpage.domain.model.CategoriesItem
import com.tokopedia.feedback_form.feedbackpage.domain.model.FeedbackModel
import com.tokopedia.feedback_form.feedbackpage.domain.model.LabelsItem
import com.tokopedia.feedback_form.feedbackpage.domain.response.Category
import com.tokopedia.feedback_form.feedbackpage.domain.response.FeedbackDataResponse
import com.tokopedia.feedback_form.feedbackpage.domain.response.Labels
import javax.inject.Inject

class FeedbackDataMapper @Inject constructor() {

    fun updateData(data: FeedbackDataResponse, pageModel: FeedbackModel) : FeedbackModel {
        return pageModel.apply {
            if (data.categories.isNotEmpty()) {
                categories = data.categories.map(categoriesItemMapper)
            }
            if (data.labels.isNotEmpty()) {
                labels = data.labels.map(labelsItemMapper)
            }
        }
    }

    private val categoriesItemMapper: (Category) -> CategoriesItem = {
        CategoriesItem().apply {
            label = it.label
            value = it.value
        }
    }

    private val labelsItemMapper: (Labels) -> LabelsItem = {
        LabelsItem().apply {
            id = it.idLabel
            name = it.name
            weight = it.weight
        }
    }
}
