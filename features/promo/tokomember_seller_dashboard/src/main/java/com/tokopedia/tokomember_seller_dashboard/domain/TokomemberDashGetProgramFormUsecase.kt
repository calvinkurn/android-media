package com.tokopedia.tokomember_seller_dashboard.domain

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.tokomember_seller_dashboard.model.ProgramFormData
import javax.inject.Inject

class TokomemberDashGetProgramFormUsecase @Inject constructor(graphqlRepository: GraphqlRepository) :
    GraphqlUseCase<ProgramFormData>(graphqlRepository) {

    @GqlQuery("TmProgramForm", TM_PROGRAM_FORM)
    fun getProgramInfo(
        success: (ProgramFormData) -> Unit,
        onFail: (Throwable) -> Unit,
        programID: Int ,shopId: Int ,actionType: String
    ) {
        this.setTypeClass(ProgramFormData::class.java)
        this.setRequestParams(getRequestParams(programID,shopId,actionType))
        this.setGraphqlQuery(TmProgramForm.GQL_QUERY)
        execute({
            success(it)
        }, {
            onFail(it)
        })
    }

    private fun getRequestParams(programID: Int ,shopId: Int ,actionType: String ): Map<String, Any> {
        return mapOf(PROGRAM_ID to shopId , SHOP_ID to shopId, ACTION_TYPE to actionType )
    }

    companion object {
        const val PROGRAM_ID = "programID"
        const val SHOP_ID = "shopID"
        const val ACTION_TYPE = "actionType"

    }
}

const val TM_PROGRAM_FORM = """
     query membershipGetProgramForm(${'$'}programID: Int! , ${'$'}shopID: Int! , ${'$'}actionType: String!) {
    membershipGetProgramForm(programID: ${'$'}programID, shopID: ${'$'}shopID , actionType: ${'$'}actionType) {
    resultStatus {
      code
      message
      reason
    }
    programForm {
      tierLevels {
        tierGroupID
      }
    }
    timePeriodList {
      name
      months
    }
  }
}
"""