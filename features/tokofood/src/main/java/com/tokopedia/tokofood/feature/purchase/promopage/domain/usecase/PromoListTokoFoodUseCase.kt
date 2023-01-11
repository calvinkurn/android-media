package com.tokopedia.tokofood.feature.purchase.promopage.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.tokofood.common.address.TokoFoodChosenAddressRequestHelper
import com.tokopedia.tokofood.common.domain.additionalattributes.CartAdditionalAttributesTokoFood
import com.tokopedia.tokofood.common.domain.param.CheckoutTokoFoodParam
import com.tokopedia.tokofood.feature.purchase.promopage.domain.model.PromoListTokoFood
import com.tokopedia.tokofood.feature.purchase.promopage.domain.model.PromoListTokoFoodResponse
import com.tokopedia.tokofood.feature.purchase.promopage.domain.param.PromoTokoFoodParam
import javax.inject.Inject

private const val QUERY = """
        query PromoListTokofood(${'$'}params: cartTokofoodParams!) {
          promo_list_tokofood(params: ${'$'}params) {
            message
            status
            data {
              title
              change_restriction_message
              error_page{
                is_show_error_page
                image
                title
                description
                button{
                   text
                   color
                   action
                   link
                  }
                }
              empty_state{
                title
                description
                image_url
              }
              available_section{
                title
                sub_title
                icon_url
                is_enabled
                sub_section {
                  title
                  icon_url
                  is_enabled
                  ticker_message
                  coupons{
                    title
                    expiry_info
                    is_selected
                    top_banner_title
                    additional_information
                  }
                }
              }
              unavailable_section{
                title
                sub_title
                icon_url
                is_enabled
                sub_section {
                  title
                  icon_url
                  is_enabled
                  ticker_message
                  coupons{
                    title
                    expiry_info
                    is_selected
                    top_banner_title
                    additional_information
                  }
                }
              }
              promo_summary{
                title
                total
                total_fmt
              }
            }
          }
        }
    """

@GqlQuery("PromoListTokofood", QUERY)
class PromoListTokoFoodUseCase @Inject constructor(
    repository: GraphqlRepository,
    private val chosenAddressRequestHelper: TokoFoodChosenAddressRequestHelper
): GraphqlUseCase<PromoListTokoFoodResponse>(repository) {

    init {
        setTypeClass(PromoListTokoFoodResponse::class.java)
        setGraphqlQuery(PromoListTokofood())
    }

    suspend fun execute(source: String,
                        merchantId: String): PromoListTokoFood {
        val additionalAttributes = CartAdditionalAttributesTokoFood(
            chosenAddressRequestHelper.getChosenAddress()
        )
        val param = generateParams(additionalAttributes.generateString(), source, merchantId)
        setRequestParams(param)
        val response = executeOnBackground()
        if (response.promoListTokoFood.isSuccess()) {
            return response.promoListTokoFood
        } else {
            throw MessageErrorException(response.promoListTokoFood.message)
        }
    }

    companion object {
        private const val PARAMS_KEY = "params"

        private fun generateParams(additionalAttributes: String,
                                   source: String,
                                   merchantId: String): Map<String, Any> {
            val params = PromoTokoFoodParam(
                additionalAttributes = additionalAttributes,
                source = source,
                merchantId = merchantId
            )
            return mapOf(PARAMS_KEY to params)
        }
    }

}
