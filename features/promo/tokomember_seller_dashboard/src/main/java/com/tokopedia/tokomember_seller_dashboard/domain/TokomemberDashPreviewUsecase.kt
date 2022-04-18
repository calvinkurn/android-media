package com.tokopedia.tokomember_seller_dashboard.domain

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.tokomember_seller_dashboard.domain.requestparam.TmCardPreviewRequestParams
import com.tokopedia.tokomember_seller_dashboard.model.*
import javax.inject.Inject

class TokomemberDashPreviewUsecase @Inject constructor(graphqlRepository: GraphqlRepository) :
    GraphqlUseCase<PreviewData>(graphqlRepository) {

    fun getPreviewData(
        success: (PreviewData) -> Unit,
        onFail: (Throwable) -> Unit,
        tmCardPreviewRequestParams: TmCardPreviewRequestParams ){
        this.setTypeClass(PreviewData::class.java)
        this.setRequestParams(getRequestParams(tmCardPreviewRequestParams))
        this.setGraphqlQuery(TM_PROGRAM_LIST)
        execute({
            success(it)
        }, {
            onFail(it)
        })
    }

    private fun getRequestParams(tmCardPreviewRequestParams: TmCardPreviewRequestParams): Map<String, Any> {
        return mapOf(INPUT to tmCardPreviewRequestParams)
    }
    companion object {
        const val INPUT = "input"
    }
}

const val TM_PREVIEW = """
     query membershipGetCardList(${'$'}input: MembershipGetListRequest!) {
    membershipGetCardList(input: ${'$'}input) {
    resultStatus {
      code
      message
      reason
    }
    intoolsCardList {
      id
      name
      status
      tierGroupID
    }
  }
}
"""