package com.tokopedia.shopadmin.feature.invitationaccepted.domain.mapper

import com.tokopedia.shopadmin.feature.invitationaccepted.domain.model.ArticleDetailResponse
import com.tokopedia.shopadmin.feature.invitationaccepted.presentation.model.ArticleDetailUiModel
import javax.inject.Inject

class ArticleDetailMapper @Inject constructor() {

    fun mapToArticleDetailUiModel(articleDetailResponse: ArticleDetailResponse) =
        ArticleDetailUiModel(articleDetailResponse.articleDetail.data.blog.htmlContent)

}