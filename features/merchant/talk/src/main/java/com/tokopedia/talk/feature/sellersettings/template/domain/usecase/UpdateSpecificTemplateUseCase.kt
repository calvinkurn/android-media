package com.tokopedia.talk.feature.sellersettings.template.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.talk.feature.sellersettings.template.data.UpdateSpecificTemplateResponseWrapper
import com.tokopedia.talk.feature.sellersettings.template.util.TemplateConstants
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class UpdateSpecificTemplateUseCase @Inject constructor(graphqlRepository: GraphqlRepository) : GraphqlUseCase<UpdateSpecificTemplateResponseWrapper>(graphqlRepository) {

    companion object {
        const val INDEX_ADJUSTMENT = 1
        const val CHAT_UPDATE_TEMPLATE_QUERY_CLASS_NAME = "UpdateSpecificTemplate"
        private const val query = """
            mutation chatUpdateTemplate(${'$'}isSeller: Boolean!, ${'$'}value: String!, ${'$'}index: Int!){
              chatUpdateTemplate(isSeller: ${'$'}isSeller, value: ${'$'}value, index: ${'$'}index){
                success
              }
            }
        """
    }

    private val requestParams = RequestParams.EMPTY

    init {
        setupUseCase()
    }

    @GqlQuery(CHAT_UPDATE_TEMPLATE_QUERY_CLASS_NAME, query)
    private fun setupUseCase() {
        setGraphqlQuery(UpdateSpecificTemplate.GQL_QUERY)
        setTypeClass(UpdateSpecificTemplateResponseWrapper::class.java)
    }

    fun setParams(isSeller: Boolean, value: String, index: Int) {
        setRequestParams(
                requestParams.apply {
                    putBoolean(TemplateConstants.PARAM_IS_SELLER, isSeller)
                    putString(TemplateConstants.PARAM_VALUE, value)
                    putInt(TemplateConstants.PARAM_INDEX, index + INDEX_ADJUSTMENT)
                }.parameters
        )
    }
}