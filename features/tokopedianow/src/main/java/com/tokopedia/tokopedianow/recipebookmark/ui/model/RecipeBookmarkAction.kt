package com.tokopedia.tokopedianow.recipebookmark.ui.model

import com.tokopedia.tokopedianow.recipebookmark.persentation.uimodel.ToasterModel

sealed class RecipeBookmarkAction {

    data class ShowToaster(
        val model: ToasterModel? = null,
        val position: Int? = null,
        val isRemoving: Boolean = false
    ) : RecipeBookmarkAction()

    object PressBackButton : RecipeBookmarkAction()
    object UnregisteredAction : RecipeBookmarkAction()
}
