package com.tokopedia.tokopedianow.recipebookmark.persentation.uimodel

data class ToasterUiModel(
    val message: String,
    val recipeId: String,
    val isSuccess: Boolean,
    val cta: String? = null
)