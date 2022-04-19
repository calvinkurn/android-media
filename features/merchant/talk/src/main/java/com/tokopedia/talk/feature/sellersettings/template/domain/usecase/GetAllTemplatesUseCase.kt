package com.tokopedia.talk.feature.sellersettings.template.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.talk.feature.sellersettings.template.data.GetAllTemplateResponseWrapper
import com.tokopedia.talk.feature.sellersettings.template.util.TemplateConstants
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class GetAllTemplatesUseCase @Inject constructor(graphqlRepository: GraphqlRepository) : GraphqlUseCase<GetAllTemplateResponseWrapper>(graphqlRepository) {

    companion object {
        const val GET_ALL_TEMPLATE_QUERY_CLASS_NAME = "GetAllTemplate"
        private const val query = """
            query chatTemplatesAll(${'$'}isSeller: Boolean!) {
              chatTemplatesAll(isSeller: ${'$'}isSeller) {
                sellerTemplate {
                  isEnable
                  IsEnableSmartReply
                  IsSeller
                  templates
                }
                buyerTemplate {
                  isEnable
                  IsEnableSmartReply
                  IsSeller
                  templates
                }
              }
            }
        """
    }

    private val requestParams = RequestParams.create()

    init {
        setupUseCase()
    }

    @GqlQuery(GET_ALL_TEMPLATE_QUERY_CLASS_NAME, query)
    private fun setupUseCase() {
        setGraphqlQuery(GetAllTemplate.GQL_QUERY)
        setTypeClass(GetAllTemplateResponseWrapper::class.java)
    }

    fun setParams(isSeller: Boolean) {
        setRequestParams(
                requestParams.apply {
                    putBoolean(TemplateConstants.PARAM_IS_SELLER, isSeller)
                }.parameters
        )
    }
}