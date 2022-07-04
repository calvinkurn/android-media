package com.tokopedia.tokopedianow.recipebookmark.domain.mapper

import com.tokopedia.tokopedianow.recipebookmark.domain.model.GetRecipeBookmarksResponse
import com.tokopedia.tokopedianow.recipebookmark.persentation.uimodel.RecipeUiModel

fun GetRecipeBookmarksResponse.mapResponseToUiModelList(): List<RecipeUiModel> {
    return this.data.tokonowGetRecipeBookmarks.data.recipes.map { response ->
        RecipeUiModel(
            id = response.id,
            title = response.title,
            portion = response.portion,
            duration = response.duration,
            tags = response.tags.map { tag ->
                tag.name
            },
            picture = response.images.getOrNull(0)?.urlOriginal.orEmpty()
        )
    }
}