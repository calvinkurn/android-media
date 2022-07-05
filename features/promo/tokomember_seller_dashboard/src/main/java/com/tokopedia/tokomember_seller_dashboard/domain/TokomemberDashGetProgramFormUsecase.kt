package com.tokopedia.tokomember_seller_dashboard.domain

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.tokomember_seller_dashboard.model.ProgramDetailData
import javax.inject.Inject

class TokomemberDashGetProgramFormUsecase @Inject constructor(graphqlRepository: GraphqlRepository) :
    GraphqlUseCase<ProgramDetailData>(graphqlRepository) {

    var gqlQuery = TM_PROGRAM_FORM

    fun getProgramInfo(
        success: (ProgramDetailData) -> Unit,
        onFail: (Throwable) -> Unit,
        programID: Int ,shopId: Int ,actionType: String, query: String = ""
    ) {
        if(query.isNotEmpty()){
            this.gqlQuery = query
        }
        this.setTypeClass(ProgramDetailData::class.java)
        this.setRequestParams(getRequestParams(programID,shopId,actionType))
        this.setGraphqlQuery(gqlQuery)
        execute({
            success(it)
        }, {
            onFail(it)
        })
    }

    private fun getRequestParams(programID: Int, shopId: Int, actionType: String): Map<String, Any> {
        return mapOf(PROGRAM_ID to programID , SHOP_ID to shopId, ACTION_TYPE to actionType )
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
      id
      cardID
      name
      timeWindow {
        id
        startTime
        endTime
        status
      }
      tierLevels {
        id
        tierGroupID
        activeTime
        name
        level
        threshold
        metadata
      }
      programAttributes {
        id
        programID
        tierLevelID
        minimumTransaction
        isUseMultiplier
        multiplierRates
      }
      status
      statusStr
      analytics {
        totalNewMember
        trxCount
        totalIncome
      }
    }
    timePeriodList {
      name
      months
      isSelected
    }
     programThreshold {
      minThresholdLevel1
      maxThresholdLevel1
      minThresholdLevel2
      maxThresholdLevel2
    }
  }
}
"""