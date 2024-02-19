package com.tokopedia.tokopedianow.recipebookmark.presentation.model

import com.tokopedia.tokopedianow.recipebookmark.presentation.uimodel.ToasterModel

sealed class RecipeBookmarkAction {

    data class ShowToaster(
        val model: ToasterModel? = null,
        val position: Int? = null,
        val isRemoving: Boolean = false
    ) : RecipeBookmarkAction()

    object PressBackButton : RecipeBookmarkAction()
    object UnregisteredAction : RecipeBookmarkAction()
}
