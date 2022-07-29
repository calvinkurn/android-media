package com.tokopedia.tokopedianow.recipebookmark.domain.mapper

import com.tokopedia.tokopedianow.recipebookmark.domain.model.GetRecipeBookmarksResponse
import com.tokopedia.tokopedianow.recipebookmark.persentation.uimodel.RecipeUiModel

object GetRecipeBookmarksMapper {
    private const val MAX_TAGS_DISPLAYED = 4
    private const val TAGS_DISPLAYED = 3

    private fun mapTag(tags: List<GetRecipeBookmarksResponse.Data.TokonowGetRecipeBookmarks.Data.Recipe.Tag>?): Pair<List<String>, Boolean> {
        if (tags.isNullOrEmpty()) return Pair(emptyList(), false)

        val newTags: MutableList<String> = mutableListOf()
        var isOtherTag = false
        for ((index, tag) in tags.withIndex()) {
            if (tags.size > MAX_TAGS_DISPLAYED) {
                if (index < TAGS_DISPLAYED) {
                    isOtherTag = false
                    newTags.add(tag.name)
                } else {
                    isOtherTag = true
                    newTags.add(String.format((tags.size - TAGS_DISPLAYED).toString()))
                    break
                }
            } else {
                isOtherTag = false
                newTags.add(tag.name)
            }
        }
        return Pair(newTags, isOtherTag)
    }

    fun List<GetRecipeBookmarksResponse.Data.TokonowGetRecipeBookmarks.Data.Recipe>.mapResponseToUiModelList(): List<RecipeUiModel> {
        return map { response ->
            val (tags, isOtherTag) = mapTag(response.tags)
            RecipeUiModel(
                id = response.id,
                title = response.title,
                portion = response.portion,
                duration = response.duration,
                tags = tags,
                isOtherTag = isOtherTag,
                picture = response.images?.firstOrNull()?.urlOriginal.orEmpty()
            )
        }
    }
}