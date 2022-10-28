package com.tokopedia.shopadmin.feature.invitationaccepted.domain.mapper

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.shopadmin.feature.invitationaccepted.domain.model.ArticleDetailResponse
import com.tokopedia.shopadmin.feature.invitationaccepted.presentation.model.ArticleDetailUiModel
import javax.inject.Inject

class ArticleDetailMapper @Inject constructor() {

    fun mapToArticleDetailUiModel(articleDetailResponse: ArticleDetailResponse): ArticleDetailUiModel {
        val type = object : TypeToken<HashMap<String, Any>>() {}.type
        val convertToMap: Map<String, Any> = Gson().fromJson(articleDetailResponse.articleDetail.data.blog.htmlContent, type)
        val htmlString = if (convertToMap[HTML_STRING] == null) {
            String.EMPTY
        } else {
            convertToMap[HTML_STRING].toString()
        }
        return ArticleDetailUiModel(htmlString)
    }

    companion object {
        const val HTML_STRING = "htmlString"
    }
}