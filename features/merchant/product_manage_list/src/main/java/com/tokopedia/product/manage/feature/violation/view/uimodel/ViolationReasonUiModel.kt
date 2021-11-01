package com.tokopedia.product.manage.feature.violation.view.uimodel

data class ViolationReasonUiModel(
    val title: String,
    val reason: String,
    val stepTitle: String,
    val stepList: List<String>,
    val buttonText: String,
    val buttonApplink: String,
)