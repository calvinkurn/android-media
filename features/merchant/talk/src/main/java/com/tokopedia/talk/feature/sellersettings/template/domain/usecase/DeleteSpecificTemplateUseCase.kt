package com.tokopedia.talk.feature.sellersettings.template.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.talk.feature.sellersettings.template.data.DeleteSpecificTemplateResponseWrapper
import com.tokopedia.talk.feature.sellersettings.template.util.TemplateConstants
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class DeleteSpecificTemplateUseCase @Inject constructor(graphqlRepository: GraphqlRepository) : GraphqlUseCase<DeleteSpecificTemplateResponseWrapper>(graphqlRepository) {

    companion object {
        const val INDEX_ADJUSTMENT = 1
        const val DELETE_SPECIFIC_TEMPLATE_QUERY_CLASS_NAME = "DeleteSpecificTemplate"
        private const val query = """
            mutation chatDeleteTemplate(${'$'}templateIndex: Int!, ${'$'}isSeller: Boolean!){
              chatDeleteTemplate(templateIndex: ${'$'}templateIndex, isSeller: ${'$'}isSeller){
                success
              }
            }
        """
    }

    private val requestParams = RequestParams.EMPTY

    init {
        setupUseCase()
    }

    @GqlQuery(DELETE_SPECIFIC_TEMPLATE_QUERY_CLASS_NAME, query)
    private fun setupUseCase() {
        setGraphqlQuery(DeleteSpecificTemplate.GQL_QUERY)
        setTypeClass(DeleteSpecificTemplateResponseWrapper::class.java)
    }

    fun setParams(index: Int, isSeller: Boolean) {
        setRequestParams(
                requestParams.apply {
                    putInt(TemplateConstants.PARAM_TEMPLATE_INDEX, index + INDEX_ADJUSTMENT)
                    putBoolean(TemplateConstants.PARAM_IS_SELLER, isSeller)
                }.parameters
        )
    }
}