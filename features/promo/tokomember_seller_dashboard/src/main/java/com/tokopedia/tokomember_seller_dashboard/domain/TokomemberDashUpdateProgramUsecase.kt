package com.tokopedia.tokomember_seller_dashboard.domain

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.tokomember_seller_dashboard.domain.requestparam.ProgramUpdateDataInput
import com.tokopedia.tokomember_seller_dashboard.model.*
import javax.inject.Inject

class TokomemberDashUpdateProgramUsecase @Inject constructor(graphqlRepository: GraphqlRepository) :
    GraphqlUseCase<ProgramUpdateResponse>(graphqlRepository) {

    fun updateProgram(
        success: (ProgramUpdateResponse) -> Unit,
        onFail: (Throwable) -> Unit,
        programUpdateDataInput: ProgramUpdateDataInput
    ){
        this.setTypeClass(ProgramUpdateResponse::class.java)
        this.setRequestParams(getRequestParams(programUpdateDataInput))
        this.setGraphqlQuery(TM_PROGRAM_UPDATE)
        execute({
            success(it)
        }, {
            onFail(it)
        })
    }

    private fun getRequestParams(programUpdateDataInput: ProgramUpdateDataInput): Map<String, Any> {
        return mapOf(INPUT to programUpdateDataInput)
    }

    companion object {
        const val INPUT = "input"
        const val NAME = "name"
    }
}

const val TM_PROGRAM_UPDATE = """
     mutation membershipCreateEditProgram(${'$'}input: MembershipProgramSellerParam!) {
    membershipCreateEditProgram(input: ${'$'}input) {
    resultStatus {
      code
      message
      reason
    }
    programSeller {
      id
      cardID
      name
      timeWindow {
        id
        referenceID
        startTime
        endTime
      }
      tierLevels {
        id
        tierGroupID
        name
        level
        threshold
        metadata
        activeTime
      }
      programAttributes {
        id
        programID
        tierLevelID
        minimumTransaction
        isUseMultiplier
        multiplierRates
      }
    }
  }
}
"""