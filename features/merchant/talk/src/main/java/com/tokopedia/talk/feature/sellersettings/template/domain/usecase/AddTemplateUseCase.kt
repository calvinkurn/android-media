package com.tokopedia.talk.feature.sellersettings.template.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.talk.feature.sellersettings.template.data.AddTemplateResponseWrapper
import com.tokopedia.talk.feature.sellersettings.template.util.TemplateConstants
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class AddTemplateUseCase @Inject constructor(graphqlRepository: GraphqlRepository) : GraphqlUseCase<AddTemplateResponseWrapper>(graphqlRepository) {

    companion object {
        private const val ADD_TEMPLATE_QUERY_CLASS_NAME = "AddTemplate"
        private const val query = """
            mutation chatAddTemplate(${'$'}isSeller: Boolean!, ${'$'}value: String!){
              chatAddTemplate(isSeller: ${'$'}isSeller, value: ${'$'}value){
                success
              }
            }
        """
    }

    private val requestParams = RequestParams.create()

    init {
        setupUseCase()
    }

    @GqlQuery(ADD_TEMPLATE_QUERY_CLASS_NAME, query)
    private fun setupUseCase() {
        setGraphqlQuery(AddTemplate.GQL_QUERY)
        setTypeClass(AddTemplateResponseWrapper::class.java)
    }

    fun setParams(isSeller: Boolean, value: String) {
        setRequestParams(
                requestParams.apply {
                    putBoolean(TemplateConstants.PARAM_IS_SELLER, isSeller)
                    putString(TemplateConstants.PARAM_VALUE, value)
                }.parameters
        )
    }

}