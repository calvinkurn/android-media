package com.tokopedia.tokofood.feature.purchase.promopage.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.flow.FlowUseCase
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.tokofood.common.address.TokoFoodChosenAddressRequestHelper
import com.tokopedia.tokofood.common.domain.additionalattributes.CartAdditionalAttributesTokoFood
import com.tokopedia.tokofood.common.domain.param.CheckoutTokoFoodParam
import com.tokopedia.tokofood.feature.purchase.promopage.domain.model.PromoListTokoFood
import com.tokopedia.tokofood.feature.purchase.promopage.domain.model.PromoListTokoFoodResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class PromoListTokoFoodUseCase @Inject constructor(
    private val repository: GraphqlRepository,
    private val chosenAddressRequestHelper: TokoFoodChosenAddressRequestHelper,
    dispatcher: CoroutineDispatchers
): FlowUseCase<String, PromoListTokoFood>(dispatcher.io) {

    companion object {
        private const val PARAMS_KEY = "params"

        private fun generateParams(additionalAttributes: String,
                                   source: String): Map<String, Any> {
            val params = CheckoutTokoFoodParam(
                additionalAttributes = additionalAttributes,
                source = source
            )
            return mapOf(PARAMS_KEY to params)
        }
    }

    override fun graphqlQuery(): String = """
        query PromoListTokofood($$PARAMS_KEY: cartTokofoodParams!) {
          promo_list_tokofood(params: $$PARAMS_KEY) {
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
              unavailable_section{
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
              promo_summary{
                title
                total
                total_fmt
              }
            }
          }
        }
    """.trimIndent()

    override suspend fun execute(params: String): Flow<PromoListTokoFood> = flow {
        val additionalAttributes = CartAdditionalAttributesTokoFood(
            chosenAddressRequestHelper.getChosenAddress()
        )
        val param = generateParams(additionalAttributes.generateString(), params)
        val response =
            repository.request<Map<String, Any>, PromoListTokoFoodResponse>(graphqlQuery(), param)
        if (response.promoListTokoFood.isSuccess()) {
            emit(response.promoListTokoFood)
        } else {
            throw MessageErrorException(response.promoListTokoFood.message)
        }
    }

}