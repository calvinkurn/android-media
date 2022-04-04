package com.tokopedia.review.feature.createreputation.model

import java.io.Serializable

data class CreateReviewTemplate(
    val text: String,
    val selected: Boolean
) : Serializable