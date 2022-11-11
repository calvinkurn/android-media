package com.tokopedia.tokopedianow.recipebookmark.domain.mapper

import com.tokopedia.tokopedianow.recipebookmark.domain.model.GetRecipeBookmarksResponse
import com.tokopedia.tokopedianow.recipebookmark.persentation.uimodel.RecipeUiModel
import com.tokopedia.tokopedianow.recipebookmark.persentation.uimodel.TagUiModel

object RecipeBookmarksMapper {
    private const val MAX_TAGS_DISPLAYED = 4
    private const val TAGS_DISPLAYED = 3

    private fun mapTag(tags: List<GetRecipeBookmarksResponse.TokonowGetRecipeBookmarks.Data.Recipe.Tag>?): List<TagUiModel> {
        if (tags.isNullOrEmpty()) return emptyList()

        val tagList: MutableList<TagUiModel> = mutableListOf()
        for ((index, tag) in tags.withIndex()) {
            if (tags.size > MAX_TAGS_DISPLAYED) {
                if (index < TAGS_DISPLAYED) {
                    tagList.add(
                        TagUiModel(
                            tag = tag.name,
                            shouldFormatTag = false
                        )
                    )
                } else {
                    tagList.add(
                        TagUiModel(
                            tag = (tags.size - TAGS_DISPLAYED).toString(),
                            shouldFormatTag = true
                        )
                    )
                    break
                }
            } else {
                tagList.add(
                    TagUiModel(
                        tag = tag.name,
                        shouldFormatTag = false
                    )
                )
            }
        }
        return tagList
    }

    fun List<GetRecipeBookmarksResponse.TokonowGetRecipeBookmarks.Data.Recipe>.mapResponseToUiModelList(): List<RecipeUiModel> {
        return map { response ->
            RecipeUiModel(
                id = response.id,
                title = response.title,
                portion = response.portion,
                duration = response.duration,
                tags =  mapTag(response.tags),
                picture = response.images?.firstOrNull()?.urlOriginal.orEmpty(),
                appUrl = response.appUrl
            )
        }
    }
}