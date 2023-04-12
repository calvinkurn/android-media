package com.tokopedia.purchase_platform.common.feature.promo.domain.usecase

import com.google.gson.Gson
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.localizationchooseaddress.common.ChosenAddressRequestHelper
import com.tokopedia.localizationchooseaddress.common.ChosenAddressRequestHelper.Companion.KEY_CHOSEN_ADDRESS
import com.tokopedia.purchase_platform.common.feature.promo.data.request.validateuse.ValidateUsePromoRequest
import com.tokopedia.purchase_platform.common.feature.promo.data.response.validateuse.ValidateUseResponse
import com.tokopedia.purchase_platform.common.feature.promo.view.mapper.ValidateUsePromoCheckoutMapper
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.ValidateUsePromoRevampUiModel
import com.tokopedia.usecase.coroutines.UseCase
import kotlinx.coroutines.delay
import javax.inject.Inject

class ValidateUsePromoRevampUseCase @Inject constructor(
    @ApplicationContext private val graphqlRepository: GraphqlRepository,
    private val chosenAddressRequestHelper: ChosenAddressRequestHelper
) : UseCase<ValidateUsePromoRevampUiModel>() {

    private var paramValidateUse: ValidateUsePromoRequest? = null

    companion object {
        private const val PARAM_PROMO = "promo"
        private const val PARAM_PARAMS = "params"

        private const val QUERY_VALIDATE_USE = "ValidateUseQuery"
    }

    fun setParam(param: ValidateUsePromoRequest): ValidateUsePromoRevampUseCase {
        paramValidateUse = param
        return this
    }

    private fun getParams(validateUsePromoRequest: ValidateUsePromoRequest): Map<String, Any?> {
        return mapOf(
            PARAM_PARAMS to mapOf(
                PARAM_PROMO to validateUsePromoRequest
            ),
            KEY_CHOSEN_ADDRESS to chosenAddressRequestHelper.getChosenAddress()
        )
    }

    @GqlQuery(QUERY_VALIDATE_USE, VALIDATE_USE_QUERY)
    override suspend fun executeOnBackground(): ValidateUsePromoRevampUiModel {
        val param = paramValidateUse?.copy() ?: throw RuntimeException("Param has not been initialized")
        delay(5_000)
//        val request = GraphqlRequest(ValidateUseQuery(), ValidateUseResponse::class.java, getParams(param))
//        val validateUseGqlResponse = graphqlRepository.response(listOf(request)).getSuccessData<ValidateUseResponse>()
        val validateUseGqlResponse = Gson().fromJson(
            """
{
      "validate_use_promo_revamp": {
        "status": "OK",
        "message": [],
        "error_code": "200",
        "code": "200000",
        "promo": {
          "global_success": true,
          "message": {
            "state": "",
            "color": "",
            "text": ""
          },
          "success": true,
          "codes": [],
          "promo_code_id": 0,
          "title_description": "Promo ",
          "discount_amount": 0,
          "cashback_wallet_amount": 0,
          "cashback_advocate_referral_amount": 0,
          "cashback_voucher_description": "",
          "invoice_description": "",
          "gateway_id": "53860745131894406905855",
          "is_coupon": 0,
          "coupon_description": "",
          "is_tokopedia_gerai": false,
          "voucher_orders": [
            {
              "code": "BONOWSELLY",
              "success": true,
              "unique_id": "11530573-0-12941756-61465344",
              "cart_string_group": "11530573-0-12941756-61465344",
              "shipping_id": 29,
              "sp_id": 58,
              "cart_id": 0,
              "order_id": 0,
              "shop_id": 11530573,
              "is_po": 0,
              "duration": "0",
              "warehouse_id": 12941756,
              "address_id": 61465344,
              "type": "logistic",
              "cashback_wallet_amount": 0,
              "discount_amount": 17000,
              "invoice_description": "",
              "title_description": "NOW! BEBAS ONGKIR hingga Rp75.000",
              "message": {
                "state": "green",
                "color": "#ade3af",
                "text": "Kamu dapat potongan ongkir dari Tokopedia"
              },
              "benefit_details": [
                {
                  "code": "BONOWSELLY",
                  "type": "",
                  "order_id": 1,
                  "unique_id": "11530573-0-12941756-61465344",
                  "discount_amount": 17000,
                  "discount_details": [
                    {
                      "amount": 17000,
                      "data_type": "total_shipping_price"
                    }
                  ],
                  "cashback_amount": 0,
                  "cashback_details": [],
                  "promo_type": {
                    "is_bebas_ongkir": true,
                    "is_exclusive_shipping": false
                  },
                  "benefit_product_details": [
                    {
                      "product_id": 2770799122,
                      "cashback_amount": 0,
                      "cashback_amount_idr": 0,
                      "discount_amount": 0,
                      "is_bebas_ongkir": true
                    },
                    {
                      "product_id": 1928749260,
                      "cashback_amount": 0,
                      "cashback_amount_idr": 0,
                      "discount_amount": 0,
                      "is_bebas_ongkir": true
                    },
                    {
                      "product_id": 1928767512,
                      "cashback_amount": 0,
                      "cashback_amount_idr": 0,
                      "discount_amount": 0,
                      "is_bebas_ongkir": true
                    },
                    {
                      "product_id": 1928767021,
                      "cashback_amount": 0,
                      "cashback_amount_idr": 0,
                      "discount_amount": 0,
                      "is_bebas_ongkir": true
                    },
                    {
                      "product_id": 2399808496,
                      "cashback_amount": 0,
                      "cashback_amount_idr": 0,
                      "discount_amount": 0,
                      "is_bebas_ongkir": true
                    }
                  ]
                }
              ]
            }
          ],
          "benefit_details": [],
          "benefit_summary_info": {
            "final_benefit_text": "Total benefit anda: ",
            "final_benefit_amount": 17000,
            "final_benefit_amount_str": "Rp17.000",
            "summaries": [
              {
                "description": "Total diskon: ",
                "type": "discount",
                "amount_str": "Rp17.000",
                "amount": 17000,
                "section_name": "Discount",
                "section_description": "",
                "details": [
                  {
                    "section_name": "",
                    "description": "Total Potongan Ongkos Kirim ",
                    "type": "shipping_discount",
                    "amount": 17000,
                    "amount_str": "Rp17.000",
                    "points": 0,
                    "points_str": ""
                  }
                ]
              }
            ]
          },
          "clashing_info_detail": {
            "clash_message": "",
            "clash_reason": "",
            "is_clashed_promos": false,
            "options": []
          },
          "tracking_details": [
            {
              "product_id": 1928767021,
              "promo_codes_tracking": "BONOWSELLY",
              "promo_details_tracking": "L:3:3400:green"
            },
            {
              "product_id": 2399808496,
              "promo_codes_tracking": "BONOWSELLY",
              "promo_details_tracking": "L:3:3400:green"
            },
            {
              "product_id": 2770799122,
              "promo_codes_tracking": "BONOWSELLY",
              "promo_details_tracking": "L:3:3400:green"
            },
            {
              "product_id": 1928749260,
              "promo_codes_tracking": "BONOWSELLY",
              "promo_details_tracking": "L:3:3400:green"
            },
            {
              "product_id": 1928767512,
              "promo_codes_tracking": "BONOWSELLY",
              "promo_details_tracking": "L:3:3400:green"
            }
          ],
          "tokopoints_detail": {
            "conversion_rate": {
              "rate": 0,
              "points_coefficient": 0,
              "external_currency_coefficient": 0
            }
          },
          "ticker_info": {
            "unique_id": "",
            "status_code": 0,
            "message": ""
          },
          "additional_info": {
            "message_info": {
              "message": "Makin hemat pakai promo",
              "detail": ""
            },
            "error_detail": {
              "message": ""
            },
            "empty_cart_info": {
              "image_url": "",
              "message": "",
              "detail": ""
            },
            "usage_summaries": [],
            "sp_ids": [],
            "promo_sp_ids": [],
            "poml_auto_applied": false
          }
        }
      }
    }
            """,
            ValidateUseResponse::class.java
        )

        return ValidateUsePromoCheckoutMapper.mapToValidateUseRevampPromoUiModel(validateUseGqlResponse.validateUsePromoRevamp)
    }
}
