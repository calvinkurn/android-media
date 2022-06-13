package com.tokopedia.tokomember_seller_dashboard.domain

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.tokomember_seller_dashboard.domain.requestparam.TmCardModifyInput
import com.tokopedia.tokomember_seller_dashboard.model.*
import javax.inject.Inject

class TokomemberDashEditCardUsecase @Inject constructor(graphqlRepository: GraphqlRepository) :
    GraphqlUseCase<MembershipCreateEditCardResponse>(graphqlRepository) {

    fun modifyShopCard(
        success: (MembershipCreateEditCardResponse) -> Unit,
        onFail: (Throwable) -> Unit,
        cardModifyInput: TmCardModifyInput
    ){
        this.setTypeClass(MembershipCreateEditCardResponse::class.java)
        this.setRequestParams(getRequestParams(cardModifyInput))
        this.setGraphqlQuery(TM_CARD_MODIFY)
        execute({
            success(it)
        }, {
            onFail(it)
        })
    }

    private fun getRequestParams(cardModifyInput: TmCardModifyInput): Map<String, Any> {
        return mapOf(INPUT to cardModifyInput)
    }

    companion object {
        const val ID = "id"
        const val IS_MERCHANT_CARD = "isMerchantCard"
        const val FONT_COLOR = "fontColor"
        const val BACKGROUND_URL = "backgroundImgUrl"
        const val SHOP_ID = "shopID"
        const val CARD_ID = "cardID"
        const val CARD = "card"
        const val PARAM = "param"
        const val PAGE_INFO = "pageInfo"
        const val INPUT = "input"
        const val NAME = "name"
        const val TIER_GROUP_ID = "tierGroupID"
        const val NUMBER_OF_LEVEL = "numberOfLevel"
        const val API_VERSION = "3.0"

    }
}

const val TM_CARD_MODIFY = """
     mutation membershipCreateEditCard(${'$'}input: MembershipCardSellerParam!) {
    membershipCreateEditCard(input: ${'$'}input) {
    resultStatus {
      code
      message
      reason
    }
    intoolsCard {
      id
      shopID
      name
      status
      tierGroupID
    }
  }
}
"""