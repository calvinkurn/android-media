package com.tokopedia.tokomember_seller_dashboard.domain

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.tokomember_seller_dashboard.model.ProgramList
import javax.inject.Inject

class TokomemberDashHomeUsecase @Inject constructor(graphqlRepository: GraphqlRepository) :
    GraphqlUseCase<ProgramList>(graphqlRepository) {

    fun getHomeData(
        success: (ProgramList) -> Unit,
        onFail: (Throwable) -> Unit,
        shopId: Int, cardID: Int, status: Int, page: Int, pageSize: Int
    ){
        this.setTypeClass(ProgramList::class.java)
        this.setRequestParams(getRequestParams(shopId, cardID, status, page, pageSize))
        this.setGraphqlQuery(TM_HOME)
        execute({
            success(it)
        }, {
            onFail(it)
        })
    }

    private fun getRequestParams(shopId: Int, cardID: Int, status: Int, page: Int, pageSize: Int): Map<String, Any> {

//        return mapOf(PROGRAM_ID to programID , SHOP_ID to shopId, ACTION_TYPE to actionType )
        return mapOf()
    }

    companion object {
        const val PROGRAM_ID = "programID"
        const val SHOP_ID = "shopID"
        const val ACTION_TYPE = "actionType"

    }
}

const val TM_HOME = """
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