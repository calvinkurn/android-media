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

    fun getProgramList(
        success: (ProgramList) -> Unit,
        onFail: (Throwable) -> Unit,
        shopId: Int, cardID: Int, status: Int, page: Int, pageSize: Int
    ){
        this.setTypeClass(ProgramList::class.java)
        this.setRequestParams(getRequestParams(shopId, cardID, status, page, pageSize))
        this.setGraphqlQuery(TM_PROGRAM_LIST)
        execute({
            success(it)
        }, {
            onFail(it)
        })
    }

    private fun getRequestParams(shopId: Int, cardId: Int, status: Int, page: Int, pageSize: Int): Map<String, Any> {
//        val shopMap = mapOf(ID to shopId)
//        val cardMap = mapOf(ID to cardId)
//        val statusMap = mapOf(STATUS to status)
//        val pageInfoMap = mapOf(PAGE to page, PAGE_SIZE to pageSize)
//        val paramMap = mapOf(SHOP to shopMap, CARD to cardMap, STATUS to statusMap)
//        val inputMap = mapOf(PARAM to paramMap, PAGE_INFO to pageInfoMap)
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

//TODO change Int! to Map data type

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