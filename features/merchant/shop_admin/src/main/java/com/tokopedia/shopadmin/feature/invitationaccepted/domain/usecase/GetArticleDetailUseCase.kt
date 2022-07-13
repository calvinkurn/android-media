package com.tokopedia.shopadmin.feature.invitationaccepted.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.shopadmin.feature.invitationaccepted.domain.mapper.ArticleDetailMapper
import com.tokopedia.shopadmin.feature.invitationaccepted.domain.model.ArticleDetailResponse
import com.tokopedia.shopadmin.feature.invitationaccepted.domain.query.ARTICLE_DETAIL_QUERY
import com.tokopedia.shopadmin.feature.invitationaccepted.presentation.model.ArticleDetailUiModel
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

@GqlQuery("GetArticleDetailQuery", ARTICLE_DETAIL_QUERY)
class GetArticleDetailUseCase @Inject constructor(
    private val useCase: GraphqlUseCase<ArticleDetailResponse>,
    private val mapper: ArticleDetailMapper
) {

    init {
        useCase.setGraphqlQuery(GetArticleDetailQuery())
        useCase.setTypeClass(ArticleDetailResponse::class.java)
    }

    suspend fun execute(): ArticleDetailUiModel {
        useCase.setRequestParams(createRequestParams())
        return mapper.mapToArticleDetailUiModel(useCase.executeOnBackground())
    }

    fun createRequestParams(): Map<String, Any> {
        return RequestParams.create().apply {
            putString(SLUG_KEY, SLUG_VALUE)
        }.parameters
    }

    companion object {
        private const val SLUG_KEY = "slug"
        private const val SLUG_VALUE = "syarat-dan-ketentuan-admin"
    }
}