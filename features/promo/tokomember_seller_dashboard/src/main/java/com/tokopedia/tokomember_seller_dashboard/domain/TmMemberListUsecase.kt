package com.tokopedia.tokomember_seller_dashboard.domain

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.tokomember_seller_dashboard.model.TmMemberListResponse
import javax.inject.Inject

class TmMemberListUsecase @Inject constructor(
    graphqlRepository: GraphqlRepository
) : GraphqlUseCase<TmMemberListResponse>(graphqlRepository) {

    fun getShopMemberList(
        shopId:Int,
        page:Int,
        pageSize:Int = 20,
        success:(TmMemberListResponse) -> Unit,
        failure:(Throwable) -> Unit
    ){
        setGraphqlQuery(TM_MEMBER_LIST_GQL)
        setTypeClass(TmMemberListResponse::class.java)
        setRequestParams(getRequestParams(shopId, page, pageSize))
        execute({
            success(it)
        },{
           failure(it)
        })
    }

    private fun getRequestParams(shopId: Int,page: Int,pageSize: Int) : Map<String,Any?>{
        return mapOf(
            SHOP_ID to shopId,
            PAGE to page,
            PAGE_SIZE to pageSize
        )
    }

    companion object{
       private const val SHOP_ID = "shopID"
       private const val PAGE="page"
       private const val PAGE_SIZE="pageSize"
    }
}

const val TM_MEMBER_LIST_GQL = """
query membershipGetUserCardMemberList(${'$'}shopID:Int!,${'$'}page:Int!,${'$'}pageSize:Int!){
      membershipGetUserCardMemberList(shopID:${'$'}shopID,page:${'$'}page,pageSize:${'$'}pageSize) {
        resultStatus {
          code
          message
          reason
        }
        userCardMemberList {
          sumUserCardMember {
            card {
              id
              shopID
              name
              status
              tierGroupID
            }
            sumUserCardMember
            sumUserCardMemberStr
          }
          userCardMember {
            id
            userID
            referenceID
            userInfo {
              name
              email
              phoneNumber
              profilePicture
            }
          }
        }
        paging{
          hasNext
        }
      }
}
"""

