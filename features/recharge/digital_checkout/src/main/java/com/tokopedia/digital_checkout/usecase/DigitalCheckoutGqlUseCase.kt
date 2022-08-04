package com.tokopedia.digital_checkout.usecase

import com.tokopedia.digital_checkout.data.response.checkout.RechargeCheckoutResponse
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import javax.inject.Inject

/**
 * @author Created By : Muhammad Furqan on Aug 5, 2022
 */
@GqlQuery(
    DigitalCheckoutGqlUseCase.QUERY_NAME_RECHARGE_CHECKOUT,
    DigitalCheckoutGqlUseCase.QUERY_RECHARGE_CHECKOUT
)
class DigitalCheckoutGqlUseCase @Inject constructor(graphqlRepository: GraphqlRepository) :
    GraphqlUseCase<RechargeCheckoutResponse>(graphqlRepository) {

    init {
        setGraphqlQuery(RechargeCheckoutQuery())
        setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        setTypeClass(RechargeCheckoutResponse::class.java)
    }

    companion object {
        const val QUERY_NAME_RECHARGE_CHECKOUT = "RechargeCheckoutQuery"
        const val QUERY_RECHARGE_CHECKOUT = """
        mutation RechargeCheckout(${'$'}request: RechargeCheckoutRequestV3!) {
          rechargeCheckoutV3(body: ${'$'}request) {
            meta {
              order_id
            }
            data {
              type
              id
              attributes {
                redirect_url
                callback_url_success
                callback_url_failed
                thanks_url
                query_string
                parameter {
                  merchant_code
                  profile_code
                  transaction_id
                  transaction_code
                  transaction_date
                  customer_name
                  customer_email
                  customer_msisdn
                  amount
                  currency
                  items_name
                  items_quantity
                  items_price
                  signature
                  language
                  user_defined_value
                  nid
                  state
                  fee
                  payments_amount
                  payments_name
                  pid
                }
              }
            }
            errors {
              status
              title
            }
          }
        }
    """
    }

}