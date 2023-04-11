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
              "code": "BOINTRAISLANDQA10",
              "success": true,
              "unique_id": "6370771-0-6595013-125529137",
              "cart_string_group": "6370771-0-6595013-174524131",
              "shipping_id": 23,
              "sp_id": 45,
              "cart_id": 0,
              "order_id": 0,
              "shop_id": 6370771,
              "is_po": 0,
              "duration": "0",
              "warehouse_id": 6595013,
              "address_id": 125529137,
              "type": "logistic",
              "cashback_wallet_amount": 0,
              "discount_amount": 11500,
              "invoice_description": "",
              "title_description": "Bebas Ongkir 50k Targeting",
              "message": {
                "state": "green",
                "color": "#ade3af",
                "text": "Kamu dapat potongan ongkir dari Tokopedia"
              },
              "benefit_details": [
                {
                  "code": "BOINTRAISLANDTST10",
                  "type": "",
                  "order_id": 1,
                  "unique_id": "6370771-0-6595013-125529137",
                  "discount_amount": 11500,
                  "discount_details": [
                    {
                      "amount": 11500,
                      "data_type": "total_shipping_price"
                    }
                  ],
                  "cashback_amount": 0,
                  "cashback_details": [],
                  "promo_type": {
                    "is_bebas_ongkir": true,
                    "is_exclusive_shipping": true
                  },
                  "benefit_product_details": [
                    {
                      "product_id": 688020593,
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
            "final_benefit_amount": 11500,
            "final_benefit_amount_str": "Rp11.500",
            "summaries": [
              {
                "description": "Total diskon: ",
                "type": "discount",
                "amount_str": "Rp11.500",
                "amount": 11500,
                "section_name": "Discount",
                "section_description": "",
                "details": [
                  {
                    "section_name": "",
                    "description": "Total Potongan Ongkos Kirim ",
                    "type": "shipping_discount",
                    "amount": 11500,
                    "amount_str": "Rp11.500",
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
              "product_id": 688020593,
              "promo_codes_tracking": "BOINTRAISLANDTST10",
              "promo_details_tracking": "L:3:11500:green"
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
            "unique_id": "6370771-0-6595013-125529137",
            "status_code": 700001,
            "message": "Yay, ongkir untuk pesanan dalam transaksi ini ditanggung Tokopedia!"
          },
          "additional_info": {
            "message_info": {
              "message": "Kamu bisa hemat Rp11.500",
              "detail": "1 promo dipakai"
            },
            "error_detail": {
              "message": ""
            },
            "empty_cart_info": {
              "image_url": "",
              "message": "",
              "detail": ""
            },
            "usage_summaries": [
              {
                "description": "Bebas Ongkir",
                "type": "bebas_ongkir",
                "amount_str": "Rp11.500",
                "amount": 11500,
                "currency_details_str": ""
              }
            ],
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
