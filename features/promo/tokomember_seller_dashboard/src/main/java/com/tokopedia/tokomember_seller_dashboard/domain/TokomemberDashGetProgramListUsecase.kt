package com.tokopedia.tokomember_seller_dashboard.domain

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.tokomember_seller_dashboard.model.ListCard
import com.tokopedia.tokomember_seller_dashboard.model.MembershipGetListRequest
import com.tokopedia.tokomember_seller_dashboard.model.PageInfo
import com.tokopedia.tokomember_seller_dashboard.model.Param
import com.tokopedia.tokomember_seller_dashboard.model.Program
import com.tokopedia.tokomember_seller_dashboard.model.ProgramList
import com.tokopedia.tokomember_seller_dashboard.model.Shop
import javax.inject.Inject

class TokomemberDashGetProgramListUsecase @Inject constructor(graphqlRepository: GraphqlRepository) :
    GraphqlUseCase<ProgramList>(graphqlRepository) {

    var gqlQuery = TM_PROGRAM_LIST

    fun getProgramList(
        success: (ProgramList) -> Unit,
        onFail: (Throwable) -> Unit,
        shopId: Int, cardID: Int, status: Int, page: Int, pageSize: Int
    ){
        this.setTypeClass(ProgramList::class.java)
        this.setRequestParams(getRequestParams(shopId, cardID, status, page, pageSize))
        this.setGraphqlQuery(gqlQuery)
        execute({
            success(it)
        }, {
            onFail(it)
        })
    }

    private fun getRequestParams(shopId: Int, cardId: Int, status: Int, page: Int, pageSize: Int): Map<String, Any> {
        val req = MembershipGetListRequest(
            Param(Shop(shopId), Program(status), ListCard(cardId)),
            PageInfo(pageSize, page)
        )
        return mapOf(INPUT to req)
    }

    companion object {
        const val ID = "id"
        const val STATUS = "status"
        const val PAGE = "page"
        const val PAGE_SIZE = "pageSize"
        const val SHOP = "shop"
        const val CARD = "card"
        const val PARAM = "param"
        const val PAGE_INFO = "pageInfo"
        const val INPUT = "input"

    }
}

const val TM_PROGRAM_LIST = """
     query membershipGetProgramList(${'$'}input: MembershipGetListRequest!) {
    membershipGetProgramList(input: ${'$'}input) {
    resultStatus {
      code
      message
      reason
    }
    programSellerList {
      id
      cardID
      name
      timeWindow {
        id
        startTime
        endTime
        status
      }
      status
      statusStr
      actions {
        buttons {
          text
          type
        }
        tripleDots {
          text
          type
        }
      }
      analytics {
        totalNewMember
        trxCount
        totalIncome
      }
    }
    isDisabledCreateProgram
    dropdownCardStatus {
      value
      text
    }
    dropdownProgramStatus {
      value
      text
    }
  }
}
"""