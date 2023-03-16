package com.tokopedia.tokopedianow.searchcategory.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.tokopedianow.searchcategory.domain.model.AddProductFeedbackModel
import com.tokopedia.usecase.coroutines.UseCase

class AddProductFeedbackUseCase(
   private val graphqlCase : GraphqlUseCase<AddProductFeedbackModel>
) : UseCase<AddProductFeedbackModel>() {
    override suspend fun executeOnBackground(): AddProductFeedbackModel {
       val params = useCaseRequestParams.parameters
        graphqlCase.setTypeClass(AddProductFeedbackModel::class.java)
        graphqlCase.setGraphqlQuery(GQL_QUERY)
        graphqlCase.setRequestParams(params)
        return graphqlCase.executeOnBackground()
    }

    companion object{
        private const val GQL_QUERY = """
            mutation TokonowAddFeedback(${'$'}RequestProducts:String!) {
              TokonowAddFeedback(RequestProducts: ${'$'}RequestProducts){
                header {
                  process_time
                  reason
                  error_code
                  messages
                }
              }
            }
        """
        const val FEEDBACK_QUERY_PARAM = "RequestProducts"
    }
}
