package com.tokopedia.tokofood.feature.purchase.promopage.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.requestAsFlow
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.flow.FlowUseCase
import com.tokopedia.tokofood.common.address.TokoFoodChosenAddressRequestHelper
import com.tokopedia.tokofood.common.domain.additionalattributes.CartAdditionalAttributesTokoFood
import com.tokopedia.tokofood.feature.purchase.promopage.domain.model.PromoListTokoFoodResponse
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PromoListTokoFoodUseCase @Inject constructor(
    private val repository: GraphqlRepository,
    private val chosenAddressRequestHelper: TokoFoodChosenAddressRequestHelper,
    dispatcher: CoroutineDispatchers
): FlowUseCase<Unit, PromoListTokoFoodResponse>(dispatcher.io) {

    companion object {
        private const val ADDITIONAL_ATTRIBUTES_KEY = "additional_attributes"

        private fun generateParam(additionalAttributes: String): Map<String, Any> {
            return mapOf(ADDITIONAL_ATTRIBUTES_KEY to additionalAttributes)
        }
    }

    override fun graphqlQuery(): String = """
        query PromoListTokofood($$ADDITIONAL_ATTRIBUTES_KEY: String) {
          promo_list_tokofood(params: {"additional_attributes": $ADDITIONAL_ATTRIBUTES_KEY}) {
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
                sub_sections{
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
                sub_sections{
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
    """.trimIndent()

    override suspend fun execute(params: Unit): Flow<PromoListTokoFoodResponse> {
        val additionalAttributes = CartAdditionalAttributesTokoFood(
            chosenAddressRequestHelper.getChosenAddress()
        )
        val param = generateParam(additionalAttributes.generateString())
        return repository.requestAsFlow(graphqlQuery(), param)
    }

}