package com.tokopedia.tokopedianow.recipebookmark.domain.mapper

import com.tokopedia.tokopedianow.recipebookmark.domain.model.GetRecipeBookmarksResponse
import com.tokopedia.tokopedianow.recipebookmark.persentation.uimodel.RecipeUiModel

object GetRecipeBookmarksMapper {
    private const val MAX_TAGS_DISPLAYED = 4
    private const val TAGS_DISPLAYED = 3

    private fun mapTag(tags: List<GetRecipeBookmarksResponse.Data.TokonowGetRecipeBookmarks.Data.Recipe.Tag>?): List<String> {
        if (tags.isNullOrEmpty()) return emptyList()

        val newTags: MutableList<String> = mutableListOf()
        for ((index, tag) in tags.withIndex()) {
            if (tags.size > MAX_TAGS_DISPLAYED) {
                if (index < TAGS_DISPLAYED) {
                    newTags.add(tag.name)
                } else {
                    newTags.add(String.format("+ ${tags.size - TAGS_DISPLAYED} lainnya"))
                    break
                }
            } else {
                newTags.add(tag.name)
            }
        }
        return newTags
    }

    fun List<GetRecipeBookmarksResponse.Data.TokonowGetRecipeBookmarks.Data.Recipe>.mapResponseToUiModelList(): List<RecipeUiModel> {
        return map { response ->
            RecipeUiModel(
                id = response.id,
                title = response.title,
                portion = response.portion,
                duration = response.duration,
                tags = mapTag(response.tags),
                picture = response.images?.firstOrNull()?.urlOriginal.orEmpty()
            )
        }
    }
}