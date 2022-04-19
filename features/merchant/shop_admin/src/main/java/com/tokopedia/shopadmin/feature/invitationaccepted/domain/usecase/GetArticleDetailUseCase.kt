package com.tokopedia.shopadmin.feature.invitationaccepted.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.shopadmin.feature.invitationaccepted.domain.mapper.ArticleDetailMapper
import com.tokopedia.shopadmin.feature.invitationaccepted.domain.model.ArticleDetailResponse
import com.tokopedia.shopadmin.feature.invitationaccepted.domain.query.GetArticleDetailQuery
import com.tokopedia.shopadmin.feature.invitationaccepted.presentation.model.ArticleDetailUiModel
import javax.inject.Inject

class GetArticleDetailUseCase @Inject constructor(
    private val useCase: GraphqlUseCase<ArticleDetailResponse>,
    private val mapper: ArticleDetailMapper
) {

    init {
        useCase.setGraphqlQuery(GetArticleDetailQuery)
        useCase.setTypeClass(ArticleDetailResponse::class.java)
    }

    suspend fun execute(): ArticleDetailUiModel {
        useCase.setRequestParams(GetArticleDetailQuery.createRequestParams())
        return mapper.mapToArticleDetailUiModel(useCase.executeOnBackground())
    }
}