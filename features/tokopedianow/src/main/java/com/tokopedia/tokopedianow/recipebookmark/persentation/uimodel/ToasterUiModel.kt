package com.tokopedia.tokopedianow.recipebookmark.persentation.uimodel

data class ToasterUiModel(
    val model: ToasterModel? = null,
    val position: Int? = null,
    val isRemoving: Boolean = false
)

data class ToasterModel(
    val title: String = "",
    val message: String = "",
    val recipeId: String = "",
    val isSuccess: Boolean = false
)