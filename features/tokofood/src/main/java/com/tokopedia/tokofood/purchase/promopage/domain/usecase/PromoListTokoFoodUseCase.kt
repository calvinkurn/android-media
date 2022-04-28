package com.tokopedia.tokofood.purchase.promopage.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.requestAsFlow
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.flow.FlowUseCase
import com.tokopedia.localizationchooseaddress.common.ChosenAddressRequestHelper
import com.tokopedia.tokofood.common.domain.additionalattributes.CartAdditionalAttributesTokoFood
import com.tokopedia.tokofood.purchase.promopage.domain.model.PromoListTokoFoodCoupon
import com.tokopedia.tokofood.purchase.promopage.domain.model.PromoListTokoFoodData
import com.tokopedia.tokofood.purchase.promopage.domain.model.PromoListTokoFoodResponse
import com.tokopedia.tokofood.purchase.promopage.domain.model.PromoListTokoFoodSection
import com.tokopedia.tokofood.purchase.promopage.domain.model.PromoListTokoFoodSubSection
import com.tokopedia.tokofood.purchase.promopage.domain.model.PromoListTokoFoodSummary
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class PromoListTokoFoodUseCase @Inject constructor(
    private val repository: GraphqlRepository,
    private val chosenAddressRequestHelper: ChosenAddressRequestHelper,
    dispatcher: CoroutineDispatchers
): FlowUseCase<Unit, PromoListTokoFoodResponse>(dispatcher.io) {

    companion object {
        private const val ADDITIONAL_ATTRIBUTES_KEY = "additional_attributes"

        private fun generateParam(additionalAttributes: String): Map<String, Any> {
            return mapOf(ADDITIONAL_ATTRIBUTES_KEY to additionalAttributes)
        }
    }

    private val isDebug = true

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
        return if (isDebug) {
            flow {
                kotlinx.coroutines.delay(1000)
                emit(getDummyResponse())
            }
        } else {
            val additionalAttributes = CartAdditionalAttributesTokoFood(
                chosenAddressRequestHelper.getChosenAddress()
            )
            val param = generateParam(additionalAttributes.generateString())
            repository.requestAsFlow(graphqlQuery(), param)
        }
    }

    private fun getDummyResponse(): PromoListTokoFoodResponse {
        return PromoListTokoFoodResponse(
            status = 1,
            data = PromoListTokoFoodData(
                title = "Pakai Promo",
                changeRestrictionMessage = "Kupon dengan keuntungan terbaik otomatis terpasang dan tidak bisa diubah.",
                availableSection = PromoListTokoFoodSection(
                    isEnabled = true,
                    subSection = PromoListTokoFoodSubSection(
                        title = "Kupon otomatis",
                        iconUrl = "https://www.pinclipart.com/picdir/middle/107-1071110_coupon-clip-art.png",
                        isEnabled = true,
                        tickerMessage = "Kupon dengan keuntungan terbaik otomatis terpasang dan tidak bisa diubah.",
                        coupons = listOf(
                            PromoListTokoFoodCoupon(
                                title = "Diskon makanan Rp31.500 + Diskon ongkir Rp6.000",
                                expiryInfo = "Persediaan terbatas",
                                isSelected = true,
                                topBannerTitle = "Keuntungan Terbaik"
                            )
                        )
                    )
                ),
                unavailableSection = PromoListTokoFoodSection(
                    isEnabled = true,
                    title = "Promo yang belum bisa dipakai",
                    subSection = PromoListTokoFoodSubSection(
                        title = "Kupon otomatis",
                        iconUrl = "https://www.pinclipart.com/picdir/middle/107-1071110_coupon-clip-art.png",
                        isEnabled = false,
                        coupons = listOf(
                            PromoListTokoFoodCoupon(
                                title = "Diskon makanan Rp60.000 + Diskon ongkir Rp6.000",
                                expiryInfo = "Persediaan terbatas",
                                isSelected = false,
                                additionalInformation = "Belanja min. Rp60.000."
                            ),
                            PromoListTokoFoodCoupon(
                                title = "Diskon makanan Rp15.000 + Diskon ongkir Rp6.000",
                                expiryInfo = "Persediaan terbatas",
                                isSelected = false,
                                additionalInformation = "Kamu sudah pakai promo terbaik."
                            )
                        )
                    )
                ),
                promoSummary = PromoListTokoFoodSummary(
                    title = "Kamu bisa hemat",
                    totalFmt = "Rp37.500"
                )
            )
        )
    }

}