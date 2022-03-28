package com.tokopedia.tokomember_seller_dashboard.domain

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.tokomember_seller_dashboard.model.TmMembershipCardResponse
import javax.inject.Inject

class TokomemberDashCardUsecase @Inject constructor(graphqlRepository: GraphqlRepository) :
    GraphqlUseCase<TmMembershipCardResponse>(graphqlRepository) {

    @GqlQuery("tmCardForm", TM_CARD_FORM)
    fun getMembershipCardInfo(
        success: (TmMembershipCardResponse) -> Unit,
        onFail: (Throwable) -> Unit,
        cardId: String
    ) {
        this.setTypeClass(TmMembershipCardResponse::class.java)
        this.setRequestParams(getRequestParams(cardId))
        this.setGraphqlQuery(TmCardForm.GQL_QUERY)
        execute({
            success(it)
        }, {
            onFail(it)
        })
    }

    private fun getRequestParams(cardId: String): Map<String, Any> {
        return mapOf(CARD_ID to cardId)
    }

    companion object {
        const val CARD_ID = "cardID"
    }
}

const val TM_CARD_FORM = """
    {
     query membershipGetCardForm(${'$'}cardID: String!) {
    membershipGetCardForm(cardID: ${'$'}cardID) {
    resultStatus {
      code
      message
      reason
    }
    card {
      id
      name
      shopID
      status
      tierGroupID
      numberOfLevel
    }
    cardTemplate {
      id
      cardID
      fontColor
      backgroundColor
      backgroundImgUrl
    }
    cardTemplateImageList {
      name
      imageURL
      isChoosen
    }
    patternList
    colorTemplateList {
      id
      colorCode
    }
    shopAvatar
  }
  }
}
"""