package com.tokopedia.tokopedianow.recipebookmark.ui.model

sealed class RecipeBookmarkEvent {

    object LoadRecipeBookmarkList : RecipeBookmarkEvent()
    object PressBackButton : RecipeBookmarkEvent()

    data class AddRecipeBookmark(
        val recipeId: String,
        val position: Int,
        val isRemoving: Boolean
    ) : RecipeBookmarkEvent()

    data class RemoveRecipeBookmark(
        val title: String,
        val position: Int,
        val recipeId: String,
        val isRemoving: Boolean
    ) : RecipeBookmarkEvent()

    data class LoadMoreRecipeBookmarkList(val scrolledToBottom: Boolean) : RecipeBookmarkEvent()

    data class ClickEmptyStateActionButton(val errorCode: String?) : RecipeBookmarkEvent()

    fun toAction(): RecipeBookmarkAction {
        return when (this) {
            is PressBackButton -> RecipeBookmarkAction.PressBackButton
            else -> RecipeBookmarkAction.UnregisteredAction
        }
    }
}
