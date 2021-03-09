package com.tokopedia.talk.feature.sellersettings.template.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.talk.feature.sellersettings.template.data.ArrangeTemplateResponseWrapper
import com.tokopedia.talk.feature.sellersettings.template.util.TemplateConstants
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class ArrangeTemplateUseCase @Inject constructor(graphqlRepository: GraphqlRepository) : GraphqlUseCase<ArrangeTemplateResponseWrapper>(graphqlRepository) {

    companion object {
        const val INDEX_ADJUSTMENT = 1
        const val ARRANGE_TEMPLATE_QUERY_CLASS_NAME = "ArrangeTemplate"
        private const val query = """
            mutation chatMoveTemplate(${'$'}index: Int!, ${'$'}moveTo: Int!, ${'$'}isSeller: Boolean!) {
              chatMoveTemplate(index: ${'$'}index, moveTo: ${'$'}moveTo, isSeller: ${'$'}isSeller) {
                success
              }
            }
        """
    }

    private val requestParams = RequestParams.EMPTY

    init {
        setupUseCase()
    }

    @GqlQuery(ARRANGE_TEMPLATE_QUERY_CLASS_NAME, query)
    private fun setupUseCase() {
        setGraphqlQuery(ArrangeTemplate.GQL_QUERY)
        setTypeClass(ArrangeTemplateResponseWrapper::class.java)
    }

    fun setParams(index: Int, moveTo: Int, isSeller: Boolean) {
        setRequestParams(
                requestParams.apply {
                    putInt(TemplateConstants.PARAM_INDEX, index + INDEX_ADJUSTMENT)
                    putInt(TemplateConstants.PARAM_MOVE_TO, moveTo + INDEX_ADJUSTMENT)
                    putBoolean(TemplateConstants.PARAM_IS_SELLER, isSeller)
                }.parameters
        )
    }
}