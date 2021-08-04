package com.tokopedia.talk.feature.sellersettings.template.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.talk.feature.sellersettings.template.data.EnableTemplateResponseWrapper
import com.tokopedia.talk.feature.sellersettings.template.util.TemplateConstants
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class EnableTemplateUseCase @Inject constructor(graphqlRepository: GraphqlRepository) : GraphqlUseCase<EnableTemplateResponseWrapper>(graphqlRepository) {

    companion object {
        const val ENABLE_TEMPLATE_QUERY_CLASS_NAME = "EnableTemplate"
        private const val query = """
            mutation chatToggleTemplate(${'$'}isEnable: Boolean!){
              chatToggleTemplate(isEnable: ${'$'}isEnable){
                success
              }
            }
        """
    }

    private val requestParams = RequestParams.create()

    init {
        setupUseCase()
    }

    @GqlQuery(ENABLE_TEMPLATE_QUERY_CLASS_NAME, query)
    private fun setupUseCase() {
        setGraphqlQuery(EnableTemplate.GQL_QUERY)
        setTypeClass(EnableTemplateResponseWrapper::class.java)
    }

    fun setParams(isEnable: Boolean) {
        setRequestParams(
                requestParams.apply {
                    putBoolean(TemplateConstants.PARAM_IS_ENABLE, isEnable)
                }.parameters
        )
    }
}