package com.tokopedia.checkout.domain.usecase

import com.google.gson.Gson
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.checkout.data.model.request.saf.ShipmentAddressFormRequest
import com.tokopedia.checkout.data.model.response.shipmentaddressform.ShipmentAddressFormGqlResponse
import com.tokopedia.checkout.domain.mapper.ShipmentMapper
import com.tokopedia.checkout.domain.model.cartshipmentform.CartShipmentAddressFormData
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.localizationchooseaddress.common.ChosenAddressRequestHelper
import com.tokopedia.purchase_platform.common.constant.CartConstant
import com.tokopedia.purchase_platform.common.exception.CartResponseErrorException
import timber.log.Timber
import javax.inject.Inject

class GetShipmentAddressFormV4UseCase @Inject constructor(
    @ApplicationContext private val graphqlRepository: GraphqlRepository,
    private val shipmentMapper: ShipmentMapper,
    private val chosenAddressRequestHelper: ChosenAddressRequestHelper,
    dispatchers: CoroutineDispatchers
) : CoroutineUseCase<ShipmentAddressFormRequest, CartShipmentAddressFormData>(dispatchers.io) {

    @GqlQuery(QUERY_SHIPMENT_ADDRESS_FORM, SHIPMENT_ADDRESS_FORM_V4_QUERY)
    override fun graphqlQuery(): String {
        return SHIPMENT_ADDRESS_FORM_V4_QUERY
    }

    override suspend fun execute(params: ShipmentAddressFormRequest): CartShipmentAddressFormData {
        val paramMap: MutableMap<String, Any> = HashMap()
        paramMap[ChosenAddressRequestHelper.KEY_CHOSEN_ADDRESS] =
            chosenAddressRequestHelper.getChosenAddress()
        paramMap[PARAM_KEY_LANG] = "id"
        paramMap[PARAM_KEY_IS_ONE_CLICK_SHIPMENT] = params.isOneClickShipment
        paramMap[PARAM_KEY_SKIP_ONBOARDING_UPDATE_STATE] = if (params.isSkipUpdateOnboardingState) 1 else 0
        if (params.cornerId != null) {
            try {
                val tmpCornerId = params.cornerId.toLongOrZero()
                paramMap[PARAM_KEY_CORNER_ID] = tmpCornerId
            } catch (e: NumberFormatException) {
                Timber.d(e)
            }
        }
        if (params.leasingId != null && params.leasingId.isNotEmpty()) {
            try {
                val tmpLeasingId = params.leasingId.toLongOrZero()
                paramMap[PARAM_KEY_VEHICLE_LEASING_ID] = tmpLeasingId
            } catch (e: NumberFormatException) {
                Timber.d(e)
            }
        }
        if (params.isTradeIn) {
            paramMap[PARAM_KEY_IS_TRADEIN] = true
            paramMap[PARAM_KEY_DEVICE_ID] = params.deviceId ?: ""
        }
        paramMap[PARAM_KEY_IS_PLUS_SELECTED] = params.isPlusSelected

        val request = GraphqlRequest(
            graphqlQuery(),
            ShipmentAddressFormGqlResponse::class.java,
            mapOf(
                "params" to paramMap
            )
        )
//        val response = graphqlRepository.response(listOf(request))
//            .getSuccessData<ShipmentAddressFormGqlResponse>()
        val response = Gson().fromJson(
            """
{
  "shipment_address_form_v3": {
    "status": "OK",
    "error_message": [],
    "data": {
      "coachmark": {
        "Plus": {
          "is_shown": false,
          "title": "",
          "content": ""
        }
      },
      "errors": [],
      "error_code": 0,
      "kero_token": "Tokopedia+Kero:JmFJN6byhsE0ylxeC1i+arSsCz8\u003d",
      "kero_discom_token": "Tokopedia+Kero:KIkj+8N3MBl4SbH5xBaAG8lL07w\u003d",
      "kero_unix_time": 1679970698,
      "is_robinhood": 1,
      "is_hide_courier_name": false,
      "is_show_onboarding": false,
      "is_ineligible_promo_dialog_enabled": true,
      "disabled_features": [],
      "donation_checkbox_status": false,
      "image_upload": {
        "show_image_upload": false,
        "text": "Lampirkan Resep Dokter",
        "left_icon_url": "https://images.tokopedia.net/img/cartapp/icons/doctor_receipt.png",
        "right_icon_url": "https://images.tokopedia.net/img/cartapp/icons/chevron_right_grey.png",
        "checkout_id": "d41d8cd98f00b204e9800998ecf8427e",
        "front_end_validation": false,
        "consultation_flow": false,
        "rejected_wording": "Produk berikut tidak bisa diproses karena pembeliannya tidak disetujui dokter."
      },
      "open_prerequisite_site": false,
      "eligible_new_shipping_experience": true,
      "pop_up_message": "",
      "pop_up": {
        "title": "",
        "description": "",
        "button": {
          "text": ""
        }
      },
      "error_ticker": "",
      "group_address": [
        {
          "errors": [],
          "user_address": {
            "address_id": 174524131,
            "address_name": "now",
            "address": "Kembangan Utara, Kembangan Utara, Kec. Kembangan, Kota Jakarta Barat, Daerah Khusus Ibukota Jakarta [Tokopedia Note: kembangan utara]",
            "postal_code": "11610",
            "phone": "6287889005019",
            "receiver_name": "Hansen Prod",
            "status": 1,
            "country": "Indonesia",
            "province_id": 13,
            "province_name": "DKI Jakarta",
            "city_id": 174,
            "city_name": "Jakarta Barat",
            "district_id": 2257,
            "district_name": "Kembangan",
            "address_2": "-6.1713568,106.7441048",
            "latitude": "-6.1713568",
            "longitude": "106.7441048",
            "corner_id": 0,
            "is_corner": false,
            "state": 101,
            "state_detail": "chosen_address_match",
            "tokonow": {
              "is_modified": false,
              "shop_id": 11515028,
              "warehouse_id": 11528221,
              "warehouses": [
                {
                  "warehouse_id": 11528221,
                  "service_type": "2h"
                },
                {
                  "warehouse_id": 0,
                  "service_type": "15m"
                }
              ],
              "service_type": "2h"
            }
          },
          "group_shop_v2": [
            {
              "group_type": 0,
              "ui_group_type": 0,
              "group_information": {
                "name": "Dilayani sama Tokopedia oeei",
                "badge_url": "https://images.tokopedia.net/img/restriction-engine/bebas-ongkir/DilayaniTokopedia_Icon.png"
              },
              "add_ons": {
                "status": 0,
                "add_on_data": [],
                "add_on_button": {
                  "title": "",
                  "description": "",
                  "left_icon_url": "",
                  "right_icon_url": "",
                  "action": 0
                },
                "add_on_bottomsheet": {
                  "header_title": "",
                  "description": "",
                  "products": [],
                  "ticker": {
                    "text": ""
                  }
                }
              },
              "errors": [],
              "errors_unblocking": [],
              "shipping_id": 29,
              "sp_id": 58,
              "scheduled_delivery": {
                "timeslot_id": 0,
                "schedule_date": "",
                "validation_metadata": ""
              },
              "rates_validation_flow": true,
              "bo_code": "",
              "is_insurance": false,
              "is_fulfillment_service": false,
              "toko_cabang": {
                "message": "Dilayani Tokopedia",
                "badge_url": "https://images.tokopedia.net/img/restriction-engine/bebas-ongkir/DilayaniTokopedia_Icon.png"
              },
              "cart_string": "11530573-0-12941756-61465344",
              "has_promo_list": false,
              "shipment_information": {
                "shop_location": "Jakarta Selatan",
                "estimation": "",
                "free_shipping": {
                  "eligible": true,
                  "badge_url": "https://images.tokopedia.net/img/img/promo/targeting/red_bo_20k.png"
                },
                "free_shipping_extra": {
                  "eligible": false,
                  "badge_url": ""
                },
                "free_shipping_general": {
                  "bo_name": "bebas ongkir",
                  "bo_type": 1,
                  "badge_url": "https://images.tokopedia.net/img/img/promo/targeting/red_bo_20k.png"
                },
                "preorder": {
                  "is_preorder": false,
                  "duration": ""
                }
              },
              "is_disable_change_courier": true,
              "auto_courier_selection": false,
              "courier_selection_error": {
                "title": "",
                "description": ""
              },
              "bo_metadata": {
                "bo_type": 1,
                "bo_eligibilities": [
                  {
                    "key": "is_bo_reg",
                    "value": "true"
                  },
                  {
                    "key": "bo_type",
                    "value": "1"
                  },
                  {
                    "key": "campaign_ids",
                    "value": "27,24,25,26"
                  }
                ]
              },
              "save_state_flag": true,
              "promo_codes": [
                "BOINTRAISLANDQA10"
              ],
              "vehicle_leasing": {
                "booking_fee": 0,
                "is_leasing_product": false
              },
              "group_shop_v2_saf": [
                {
                  "cart_string_order": "123",
                  "shop": {
                    "shop_id": 6370771,
                    "shop_name": "TX Shop PM",
                    "postal_code": "12930",
                    "latitude": "-6.221197900000001",
                    "longitude": "106.81941699999993",
                    "district_id": 2270,
                    "shop_alert_message": "",
                    "is_tokonow": false,
                    "is_gold": 1,
                    "is_official": 0,
                    "shop_ticker": "",
                    "shop_ticker_title": "",
                    "shop_type_info": {
                      "shop_tier": 1,
                      "shop_grade": 12,
                      "badge": "https://images.tokopedia.net/img/goldmerchant/pm_activation/badge/ic-powermerchant-130px.png",
                      "badge_svg": "https://assets.tokopedia.net/asts/goldmerchant/pm_activation/badge/ic-powermerchant.svg",
                      "title": "Power Merchant"
                    },
                    "enabler_data": {
                      "label_name": "",
                      "show_label": false
                    }
                  },
                  "cart_details": [
                    {
                      "errors": [],
                      "bundle_detail": {
                        "bundle_description": "",
                        "bundle_group_id": "",
                        "bundle_id": 0,
                        "bundle_max_order": 0,
                        "bundle_min_order": 0,
                        "bundle_name": "",
                        "bundle_original_price": 0,
                        "bundle_original_price_fmt": "",
                        "bundle_price": 0,
                        "bundle_price_fmt": "",
                        "bundle_qty": 0,
                        "bundle_quota": 0,
                        "bundle_status": "",
                        "bundle_type": "",
                        "slash_price_label": "",
                        "bundle_icon_url": ""
                      },
                      "products": [
                        {
                          "add_ons": {
                            "status": 0,
                            "add_on_data": [],
                            "add_on_button": {
                              "title": "",
                              "description": "",
                              "left_icon_url": "",
                              "right_icon_url": "",
                              "action": 0
                            },
                            "add_on_bottomsheet": {
                              "header_title": "",
                              "description": "",
                              "products": [],
                              "ticker": {
                                "text": ""
                              }
                            }
                          },
                          "errors": [],
                          "cart_id": "6282742320",
                          "product_id": 2770799122,
                          "product_name": "Bawang Merah 250 gram",
                          "product_price_fmt": "Rp13.700",
                          "product_price": 13700,
                          "product_original_price": 0,
                          "product_wholesale_price": 13700,
                          "product_wholesale_price_fmt": "Rp13.700",
                          "product_weight_fmt": "250gr",
                          "product_weight": 250,
                          "product_weight_actual": 250,
                          "product_condition": 1,
                          "product_url": "https://www.tokopedia.com/now/bawang-merah-250-gram",
                          "product_returnable": 0,
                          "product_is_free_returns": 0,
                          "product_is_preorder": 0,
                          "product_cashback": "",
                          "product_min_order": 1,
                          "product_invenage_value": 113,
                          "product_switch_invenage": 1,
                          "product_price_currency": 1,
                          "product_image_src_200_square": "https://images.tokopedia.net/img/cache/200-square/VqbcmM/2022/10/28/c7bb2d37-f9a0-4f72-bbae-8e82447fb788.jpg",
                          "product_notes": "",
                          "product_quantity": 1,
                          "product_menu_id": 0,
                          "product_finsurance": 1,
                          "product_fcancel_partial": 0,
                          "product_cat_id": 4848,
                          "product_catalog_id": 0,
                          "product_category": "",
                          "purchase_protection_plan_data": {
                            "protection_available": false,
                            "protection_type_id": 0,
                            "protection_price_per_product": 0,
                            "protection_price": 0,
                            "protection_title": "",
                            "protection_subtitle": "",
                            "protection_link_text": "",
                            "protection_link_url": "",
                            "protection_opt_in": false,
                            "protection_checkbox_disabled": false
                          },
                          "product_variants": {
                            "parent_id": 0
                          },
                          "product_tracker_data": {
                            "attribution": "none/other",
                            "tracker_list_name": "none/other"
                          },
                          "product_preorder": {
                            "duration_text": "",
                            "duration_day": "0"
                          },
                          "trade_in_info": {
                            "is_valid_trade_in": false,
                            "new_device_price": 0,
                            "new_device_price_fmt": "",
                            "old_device_price": 0,
                            "old_device_price_fmt": "",
                            "drop_off_enable": false,
                            "device_model": "",
                            "diagnostic_id": 0
                          },
                          "free_shipping": {
                            "eligible": false,
                            "badge_url": ""
                          },
                          "free_shipping_extra": {
                            "eligible": false,
                            "badge_url": ""
                          },
                          "free_shipping_general": {
                            "bo_name": "tokonow",
                            "bo_type": 3,
                            "badge_url": ""
                          },
                          "product_ticker": {
                            "show_ticker": false,
                            "message": ""
                          },
                          "variant_description_detail": {
                            "variant_name": [],
                            "variant_description": ""
                          },
                          "product_alert_message": "",
                          "product_information": [],
                          "campaign_id": 0,
                          "ethical_drug": {
                            "need_prescription": false,
                            "icon_url": "https://images.tokopedia.net/img/cartapp/icons/ethical_drug.png",
                            "text": "Butuh Resep"
                          }
                        },
                        {
                          "add_ons": {
                            "status": 0,
                            "add_on_data": [],
                            "add_on_button": {
                              "title": "",
                              "description": "",
                              "left_icon_url": "",
                              "right_icon_url": "",
                              "action": 0
                            },
                            "add_on_bottomsheet": {
                              "header_title": "",
                              "description": "",
                              "products": [],
                              "ticker": {
                                "text": ""
                              }
                            }
                          },
                          "errors": [],
                          "cart_id": "6282737769",
                          "product_id": 1928749260,
                          "product_name": "Dada Ayam Boneless Fillet Frozen 500 gram",
                          "product_price_fmt": "Rp30.000",
                          "product_price": 30000,
                          "product_original_price": 0,
                          "product_wholesale_price": 30000,
                          "product_wholesale_price_fmt": "Rp30.000",
                          "product_weight_fmt": "500gr",
                          "product_weight": 500,
                          "product_weight_actual": 500,
                          "product_condition": 1,
                          "product_url": "https://www.tokopedia.com/now/dada-ayam-boneless-fillet-frozen-500-gram",
                          "product_returnable": 0,
                          "product_is_free_returns": 0,
                          "product_is_preorder": 0,
                          "product_cashback": "",
                          "product_min_order": 1,
                          "product_invenage_value": 161,
                          "product_switch_invenage": 1,
                          "product_price_currency": 1,
                          "product_image_src_200_square": "https://images.tokopedia.net/img/cache/200-square/hDjmkQ/2021/6/17/8f95fba6-08a8-4a49-9e05-952918f60e98.jpg",
                          "product_notes": "",
                          "product_quantity": 1,
                          "product_menu_id": 0,
                          "product_finsurance": 1,
                          "product_fcancel_partial": 0,
                          "product_cat_id": 5083,
                          "product_catalog_id": 0,
                          "product_category": "",
                          "purchase_protection_plan_data": {
                            "protection_available": false,
                            "protection_type_id": 0,
                            "protection_price_per_product": 0,
                            "protection_price": 0,
                            "protection_title": "",
                            "protection_subtitle": "",
                            "protection_link_text": "",
                            "protection_link_url": "",
                            "protection_opt_in": false,
                            "protection_checkbox_disabled": false
                          },
                          "product_variants": {
                            "parent_id": 0
                          },
                          "product_tracker_data": {
                            "attribution": "none/other",
                            "tracker_list_name": "none/other"
                          },
                          "product_preorder": {
                            "duration_text": "",
                            "duration_day": "0"
                          },
                          "trade_in_info": {
                            "is_valid_trade_in": false,
                            "new_device_price": 0,
                            "new_device_price_fmt": "",
                            "old_device_price": 0,
                            "old_device_price_fmt": "",
                            "drop_off_enable": false,
                            "device_model": "",
                            "diagnostic_id": 0
                          },
                          "free_shipping": {
                            "eligible": false,
                            "badge_url": ""
                          },
                          "free_shipping_extra": {
                            "eligible": false,
                            "badge_url": ""
                          },
                          "free_shipping_general": {
                            "bo_name": "tokonow",
                            "bo_type": 3,
                            "badge_url": ""
                          },
                          "product_ticker": {
                            "show_ticker": false,
                            "message": ""
                          },
                          "variant_description_detail": {
                            "variant_name": [],
                            "variant_description": ""
                          },
                          "product_alert_message": "",
                          "product_information": [],
                          "campaign_id": 0,
                          "ethical_drug": {
                            "need_prescription": false,
                            "icon_url": "https://images.tokopedia.net/img/cartapp/icons/ethical_drug.png",
                            "text": "Butuh Resep"
                          }
                        }
                      ]
                    },
                    {
                      "errors": [],
                      "bundle_detail": {
                        "bundle_description": "",
                        "bundle_group_id": "bid:1134642-pid:0-pid1:1928767021-pid2:1928767512",
                        "bundle_id": 1134642,
                        "bundle_max_order": 0,
                        "bundle_min_order": 1,
                        "bundle_name": "Bundle Sawi Cabai",
                        "bundle_original_price": 17900,
                        "bundle_original_price_fmt": "Rp17.900",
                        "bundle_price": 15910,
                        "bundle_price_fmt": "Rp15.910",
                        "bundle_qty": 1,
                        "bundle_quota": 0,
                        "bundle_status": "ACTIVE",
                        "bundle_type": "MULTIPLE",
                        "slash_price_label": "12%",
                        "bundle_icon_url": "https://images.tokopedia.net/img/cartapp/bundling_icon.png"
                      },
                      "products": [
                        {
                          "add_ons": {
                            "status": 0,
                            "add_on_data": [],
                            "add_on_button": {
                              "title": "",
                              "description": "",
                              "left_icon_url": "",
                              "right_icon_url": "",
                              "action": 0
                            },
                            "add_on_bottomsheet": {
                              "header_title": "",
                              "description": "",
                              "products": [],
                              "ticker": {
                                "text": ""
                              }
                            }
                          },
                          "errors": [],
                          "cart_id": "6320981092",
                          "product_id": 1928767512,
                          "product_name": "Cabai Rawit Hijau 150 gram",
                          "product_price_fmt": "Rp11.610",
                          "product_price": 11610,
                          "product_original_price": 12900,
                          "product_wholesale_price": 11610,
                          "product_wholesale_price_fmt": "Rp11.610",
                          "product_weight_fmt": "150gr",
                          "product_weight": 150,
                          "product_weight_actual": 150,
                          "product_condition": 1,
                          "product_url": "https://www.tokopedia.com/now/cabai-rawit-hijau-150-gram",
                          "product_returnable": 0,
                          "product_is_free_returns": 0,
                          "product_is_preorder": 0,
                          "product_cashback": "",
                          "product_min_order": 1,
                          "product_invenage_value": 13,
                          "product_switch_invenage": 1,
                          "product_price_currency": 1,
                          "product_image_src_200_square": "https://images.tokopedia.net/img/cache/200-square/VqbcmM/2022/11/4/12f50545-c14c-4d43-806f-83d3738f9145.png",
                          "product_notes": "",
                          "product_quantity": 1,
                          "product_menu_id": 0,
                          "product_finsurance": 1,
                          "product_fcancel_partial": 0,
                          "product_cat_id": 4877,
                          "product_catalog_id": 0,
                          "product_category": "",
                          "purchase_protection_plan_data": {
                            "protection_available": false,
                            "protection_type_id": 0,
                            "protection_price_per_product": 0,
                            "protection_price": 0,
                            "protection_title": "",
                            "protection_subtitle": "",
                            "protection_link_text": "",
                            "protection_link_url": "",
                            "protection_opt_in": false,
                            "protection_checkbox_disabled": false
                          },
                          "product_variants": {
                            "parent_id": 0
                          },
                          "product_tracker_data": {
                            "attribution": "none/other",
                            "tracker_list_name": "none/other"
                          },
                          "product_preorder": {
                            "duration_text": "",
                            "duration_day": "0"
                          },
                          "trade_in_info": {
                            "is_valid_trade_in": false,
                            "new_device_price": 0,
                            "new_device_price_fmt": "",
                            "old_device_price": 0,
                            "old_device_price_fmt": "",
                            "drop_off_enable": false,
                            "device_model": "",
                            "diagnostic_id": 0
                          },
                          "free_shipping": {
                            "eligible": false,
                            "badge_url": ""
                          },
                          "free_shipping_extra": {
                            "eligible": false,
                            "badge_url": ""
                          },
                          "free_shipping_general": {
                            "bo_name": "tokonow",
                            "bo_type": 3,
                            "badge_url": ""
                          },
                          "product_ticker": {
                            "show_ticker": false,
                            "message": ""
                          },
                          "variant_description_detail": {
                            "variant_name": [],
                            "variant_description": ""
                          },
                          "product_alert_message": "",
                          "product_information": [],
                          "campaign_id": 0,
                          "ethical_drug": {
                            "need_prescription": false,
                            "icon_url": "https://images.tokopedia.net/img/cartapp/icons/ethical_drug.png",
                            "text": "Butuh Resep"
                          }
                        },
                        {
                          "add_ons": {
                            "status": 0,
                            "add_on_data": [],
                            "add_on_button": {
                              "title": "",
                              "description": "",
                              "left_icon_url": "",
                              "right_icon_url": "",
                              "action": 0
                            },
                            "add_on_bottomsheet": {
                              "header_title": "",
                              "description": "",
                              "products": [],
                              "ticker": {
                                "text": ""
                              }
                            }
                          },
                          "errors": [],
                          "cart_id": "6320981090",
                          "product_id": 1928767021,
                          "product_name": "Sawi Hijau 250 gram",
                          "product_price_fmt": "Rp4.300",
                          "product_price": 4300,
                          "product_original_price": 5000,
                          "product_wholesale_price": 4300,
                          "product_wholesale_price_fmt": "Rp4.300",
                          "product_weight_fmt": "250gr",
                          "product_weight": 250,
                          "product_weight_actual": 250,
                          "product_condition": 1,
                          "product_url": "https://www.tokopedia.com/now/sawi-hijau-250-gram",
                          "product_returnable": 0,
                          "product_is_free_returns": 0,
                          "product_is_preorder": 0,
                          "product_cashback": "",
                          "product_min_order": 1,
                          "product_invenage_value": 165,
                          "product_switch_invenage": 1,
                          "product_price_currency": 1,
                          "product_image_src_200_square": "https://images.tokopedia.net/img/cache/200-square/VqbcmM/2021/10/15/440f4760-fa62-4f82-a92c-b29c193b8f41.jpg",
                          "product_notes": "",
                          "product_quantity": 1,
                          "product_menu_id": 0,
                          "product_finsurance": 1,
                          "product_fcancel_partial": 0,
                          "product_cat_id": 4897,
                          "product_catalog_id": 0,
                          "product_category": "",
                          "purchase_protection_plan_data": {
                            "protection_available": false,
                            "protection_type_id": 0,
                            "protection_price_per_product": 0,
                            "protection_price": 0,
                            "protection_title": "",
                            "protection_subtitle": "",
                            "protection_link_text": "",
                            "protection_link_url": "",
                            "protection_opt_in": false,
                            "protection_checkbox_disabled": false
                          },
                          "product_variants": {
                            "parent_id": 0
                          },
                          "product_tracker_data": {
                            "attribution": "none/other",
                            "tracker_list_name": "none/other"
                          },
                          "product_preorder": {
                            "duration_text": "",
                            "duration_day": "0"
                          },
                          "trade_in_info": {
                            "is_valid_trade_in": false,
                            "new_device_price": 0,
                            "new_device_price_fmt": "",
                            "old_device_price": 0,
                            "old_device_price_fmt": "",
                            "drop_off_enable": false,
                            "device_model": "",
                            "diagnostic_id": 0
                          },
                          "free_shipping": {
                            "eligible": false,
                            "badge_url": ""
                          },
                          "free_shipping_extra": {
                            "eligible": false,
                            "badge_url": ""
                          },
                          "free_shipping_general": {
                            "bo_name": "tokonow",
                            "bo_type": 3,
                            "badge_url": ""
                          },
                          "product_ticker": {
                            "show_ticker": false,
                            "message": ""
                          },
                          "variant_description_detail": {
                            "variant_name": [],
                            "variant_description": ""
                          },
                          "product_alert_message": "",
                          "product_information": [],
                          "campaign_id": 0,
                          "ethical_drug": {
                            "need_prescription": false,
                            "icon_url": "https://images.tokopedia.net/img/cartapp/icons/ethical_drug.png",
                            "text": "Butuh Resep"
                          }
                        }
                      ]
                    },
                    {
                      "errors": [],
                      "bundle_detail": {
                        "bundle_description": "",
                        "bundle_group_id": "bid:1134703-pid:0-pid1:2399808496",
                        "bundle_id": 1134703,
                        "bundle_max_order": 0,
                        "bundle_min_order": 4,
                        "bundle_name": "Paket Hydro Coco",
                        "bundle_original_price": 31600,
                        "bundle_original_price_fmt": "Rp31.600",
                        "bundle_price": 28756,
                        "bundle_price_fmt": "Rp28.756",
                        "bundle_qty": 1,
                        "bundle_quota": 0,
                        "bundle_status": "ACTIVE",
                        "bundle_type": "SINGLE",
                        "slash_price_label": "9%",
                        "bundle_icon_url": "https://images.tokopedia.net/img/cartapp/bundling_icon.png"
                      },
                      "products": [
                        {
                          "add_ons": {
                            "status": 0,
                            "add_on_data": [],
                            "add_on_button": {
                              "title": "",
                              "description": "",
                              "left_icon_url": "",
                              "right_icon_url": "",
                              "action": 0
                            },
                            "add_on_bottomsheet": {
                              "header_title": "",
                              "description": "",
                              "products": [],
                              "ticker": {
                                "text": ""
                              }
                            }
                          },
                          "errors": [],
                          "cart_id": "6320975798",
                          "product_id": 2399808496,
                          "product_name": "Hydro Coco Minuman Air Kelapa Original 250 ml",
                          "product_price_fmt": "Rp7.189",
                          "product_price": 7189,
                          "product_original_price": 7900,
                          "product_wholesale_price": 7189,
                          "product_wholesale_price_fmt": "Rp7.189",
                          "product_weight_fmt": "250gr",
                          "product_weight": 250,
                          "product_weight_actual": 250,
                          "product_condition": 1,
                          "product_url": "https://www.tokopedia.com/now/hydro-coco-minuman-air-kelapa-original-250-ml",
                          "product_returnable": 0,
                          "product_is_free_returns": 0,
                          "product_is_preorder": 0,
                          "product_cashback": "",
                          "product_min_order": 4,
                          "product_invenage_value": 31,
                          "product_switch_invenage": 1,
                          "product_price_currency": 1,
                          "product_image_src_200_square": "https://images.tokopedia.net/img/cache/200-square/hDjmkQ/2021/11/10/b3870059-b017-4fe1-9fe2-c7ebe5a635de.jpg",
                          "product_notes": "",
                          "product_quantity": 4,
                          "product_menu_id": 0,
                          "product_finsurance": 1,
                          "product_fcancel_partial": 0,
                          "product_cat_id": 5140,
                          "product_catalog_id": 0,
                          "product_category": "",
                          "purchase_protection_plan_data": {
                            "protection_available": false,
                            "protection_type_id": 0,
                            "protection_price_per_product": 0,
                            "protection_price": 0,
                            "protection_title": "",
                            "protection_subtitle": "",
                            "protection_link_text": "",
                            "protection_link_url": "",
                            "protection_opt_in": false,
                            "protection_checkbox_disabled": false
                          },
                          "product_variants": {
                            "parent_id": 0
                          },
                          "product_tracker_data": {
                            "attribution": "none/other",
                            "tracker_list_name": "none/other"
                          },
                          "product_preorder": {
                            "duration_text": "",
                            "duration_day": "0"
                          },
                          "trade_in_info": {
                            "is_valid_trade_in": false,
                            "new_device_price": 0,
                            "new_device_price_fmt": "",
                            "old_device_price": 0,
                            "old_device_price_fmt": "",
                            "drop_off_enable": false,
                            "device_model": "",
                            "diagnostic_id": 0
                          },
                          "free_shipping": {
                            "eligible": false,
                            "badge_url": ""
                          },
                          "free_shipping_extra": {
                            "eligible": false,
                            "badge_url": ""
                          },
                          "free_shipping_general": {
                            "bo_name": "tokonow",
                            "bo_type": 3,
                            "badge_url": ""
                          },
                          "product_ticker": {
                            "show_ticker": false,
                            "message": ""
                          },
                          "variant_description_detail": {
                            "variant_name": [],
                            "variant_description": ""
                          },
                          "product_alert_message": "",
                          "product_information": [],
                          "campaign_id": 0,
                          "ethical_drug": {
                            "need_prescription": false,
                            "icon_url": "https://images.tokopedia.net/img/cartapp/icons/ethical_drug.png",
                            "text": "Butuh Resep"
                          }
                        }
                      ]
                    }
                  ]
                }
              ],
              "shop_shipments": [
                {
                  "ship_id": 1,
                  "ship_name": "JNE",
                  "ship_code": "jne",
                  "ship_logo": "https://images.tokopedia.net/img/kurir-jne.png",
                  "is_dropship_enabled": 1,
                  "ship_prods": [
                    {
                      "ship_prod_id": 1,
                      "ship_prod_name": "Reguler",
                      "ship_group_name": "regular",
                      "ship_group_id": 1004,
                      "additional_fee": 0,
                      "minimum_weight": 0
                    },
                    {
                      "ship_prod_id": 2,
                      "ship_prod_name": "OKE",
                      "ship_group_name": "regular",
                      "ship_group_id": 1004,
                      "additional_fee": 0,
                      "minimum_weight": 0
                    },
                    {
                      "ship_prod_id": 6,
                      "ship_prod_name": "YES",
                      "ship_group_name": "nextday",
                      "ship_group_id": 1003,
                      "additional_fee": 0,
                      "minimum_weight": 0
                    }
                  ]
                },
                {
                  "ship_id": 2,
                  "ship_name": "TIKI",
                  "ship_code": "tiki",
                  "ship_logo": "https://images.tokopedia.net/img/kurir-tiki.png",
                  "is_dropship_enabled": 1,
                  "ship_prods": [
                    {
                      "ship_prod_id": 3,
                      "ship_prod_name": "Reguler",
                      "ship_group_name": "regular",
                      "ship_group_id": 1004,
                      "additional_fee": 0,
                      "minimum_weight": 0
                    },
                    {
                      "ship_prod_id": 16,
                      "ship_prod_name": "Over Night Service",
                      "ship_group_name": "nextday",
                      "ship_group_id": 1003,
                      "additional_fee": 0,
                      "minimum_weight": 0
                    }
                  ]
                },
                {
                  "ship_id": 4,
                  "ship_name": "Pos Indonesia",
                  "ship_code": "pos",
                  "ship_logo": "https://images.tokopedia.net/img/kurir-pos-aja.png",
                  "is_dropship_enabled": 1,
                  "ship_prods": [
                    {
                      "ship_prod_id": 10,
                      "ship_prod_name": "Pos Reguler",
                      "ship_group_name": "regular",
                      "ship_group_id": 1004,
                      "additional_fee": 0,
                      "minimum_weight": 0
                    }
                  ]
                },
                {
                  "ship_id": 6,
                  "ship_name": "Wahana",
                  "ship_code": "wahana",
                  "ship_logo": "https://images.tokopedia.net/img/kurir-wahana.png",
                  "is_dropship_enabled": 1,
                  "ship_prods": [
                    {
                      "ship_prod_id": 8,
                      "ship_prod_name": "Service Normal",
                      "ship_group_name": "regular",
                      "ship_group_id": 1004,
                      "additional_fee": 0,
                      "minimum_weight": 0
                    }
                  ]
                },
                {
                  "ship_id": 10,
                  "ship_name": "GoSend",
                  "ship_code": "gojek",
                  "ship_logo": "https://images.tokopedia.net/img/kurir-gosend.png",
                  "is_dropship_enabled": 0,
                  "ship_prods": [
                    {
                      "ship_prod_id": 28,
                      "ship_prod_name": "Instant Courier",
                      "ship_group_name": "instant",
                      "ship_group_id": 1000,
                      "additional_fee": 0,
                      "minimum_weight": 0
                    },
                    {
                      "ship_prod_id": 59,
                      "ship_prod_name": "Instant Car",
                      "ship_group_name": "regular",
                      "ship_group_id": 1004,
                      "additional_fee": 0,
                      "minimum_weight": 0
                    },
                    {
                      "ship_prod_id": 20,
                      "ship_prod_name": "Same Day",
                      "ship_group_name": "sameday",
                      "ship_group_id": 1002,
                      "additional_fee": 0,
                      "minimum_weight": 0
                    }
                  ]
                },
                {
                  "ship_id": 11,
                  "ship_name": "SiCepat",
                  "ship_code": "sicepat",
                  "ship_logo": "https://images.tokopedia.net/img/kurir-sicepat.png",
                  "is_dropship_enabled": 1,
                  "ship_prods": [
                    {
                      "ship_prod_id": 18,
                      "ship_prod_name": "Regular Package",
                      "ship_group_name": "regular",
                      "ship_group_id": 1004,
                      "additional_fee": 0,
                      "minimum_weight": 0
                    },
                    {
                      "ship_prod_id": 33,
                      "ship_prod_name": "BEST",
                      "ship_group_name": "nextday",
                      "ship_group_id": 1003,
                      "additional_fee": 0,
                      "minimum_weight": 0
                    },
                    {
                      "ship_prod_id": 43,
                      "ship_prod_name": "GOKIL",
                      "ship_group_name": "regular",
                      "ship_group_id": 1004,
                      "additional_fee": 0,
                      "minimum_weight": 0
                    },
                    {
                      "ship_prod_id": 44,
                      "ship_prod_name": "Regular Package",
                      "ship_group_name": "regular",
                      "ship_group_id": 1004,
                      "additional_fee": 0,
                      "minimum_weight": 0
                    }
                  ]
                },
                {
                  "ship_id": 12,
                  "ship_name": "Ninja Xpress",
                  "ship_code": "ninja",
                  "ship_logo": "https://images.tokopedia.net/img/kurir-ninja.png",
                  "is_dropship_enabled": 0,
                  "ship_prods": [
                    {
                      "ship_prod_id": 25,
                      "ship_prod_name": "Reguler",
                      "ship_group_name": "regular",
                      "ship_group_id": 1004,
                      "additional_fee": 0,
                      "minimum_weight": 0
                    }
                  ]
                },
                {
                  "ship_id": 14,
                  "ship_name": "J\u0026T",
                  "ship_code": "jnt",
                  "ship_logo": "https://images.tokopedia.net/img/kurir-jnt.png",
                  "is_dropship_enabled": 1,
                  "ship_prods": [
                    {
                      "ship_prod_id": 27,
                      "ship_prod_name": "Reguler",
                      "ship_group_name": "regular",
                      "ship_group_id": 1004,
                      "additional_fee": 0,
                      "minimum_weight": 0
                    }
                  ]
                },
                {
                  "ship_id": 16,
                  "ship_name": "REX",
                  "ship_code": "rex",
                  "ship_logo": "https://images.tokopedia.net/img/kurir-rex.png",
                  "is_dropship_enabled": 0,
                  "ship_prods": [
                    {
                      "ship_prod_id": 32,
                      "ship_prod_name": "REX-10",
                      "ship_group_name": "regular",
                      "ship_group_id": 1004,
                      "additional_fee": 0,
                      "minimum_weight": 0
                    }
                  ]
                },
                {
                  "ship_id": 23,
                  "ship_name": "AnterAja",
                  "ship_code": "anteraja",
                  "ship_logo": "https://images.tokopedia.net/img/kurir-anteraja.png",
                  "is_dropship_enabled": 1,
                  "ship_prods": [
                    {
                      "ship_prod_id": 46,
                      "ship_prod_name": "Next Day",
                      "ship_group_name": "regular",
                      "ship_group_id": 1004,
                      "additional_fee": 0,
                      "minimum_weight": 0
                    },
                    {
                      "ship_prod_id": 45,
                      "ship_prod_name": "Reguler",
                      "ship_group_name": "regular",
                      "ship_group_id": 1004,
                      "additional_fee": 0,
                      "minimum_weight": 0
                    }
                  ]
                },
                {
                  "ship_id": 24,
                  "ship_name": "Lion Parcel",
                  "ship_code": "lion",
                  "ship_logo": "https://images.tokopedia.net/img/kurir-lionparcel.png",
                  "is_dropship_enabled": 1,
                  "ship_prods": [
                    {
                      "ship_prod_id": 47,
                      "ship_prod_name": "Reguler",
                      "ship_group_name": "regular",
                      "ship_group_id": 1004,
                      "additional_fee": 0,
                      "minimum_weight": 0
                    }
                  ]
                }
              ],
              "dropshipper": {
                "name": "",
                "telp_no": ""
              },
              "warehouse": {
                "warehouse_id": 6595013,
                "city_name": "Jakarta Selatan"
              }
            }
          ]
        }
      ],
      "donation": {
        "Title": "Donasi Rp5.000 untuk paket buka puasa masyarakat prasejahtera",
        "Nominal": 5000,
        "Description": "Donasi akan disalurkan dalam bentuk paket hidangan buka puasa untuk masyarakat prasejahtera di wilayah Jawa bersama BenihBaik.com "
      },
      "cod": {
        "is_cod": false,
        "counter_cod": -2
      },
      "message": {
        "message_info": "",
        "message_link": "",
        "message_logo": ""
      },
      "egold_attributes": {
        "eligible": false,
        "is_tiering": false,
        "is_opt_in": false,
        "range": {
          "min": 0,
          "max": 0
        },
        "message": {
          "title_text": "",
          "sub_text": "",
          "ticker_text": "",
          "tooltip_text": ""
        },
        "tier_data": [],
        "hyperlink_text": {
          "text": "",
          "url": "",
          "is_show": false
        }
      },
      "tickers": [],
      "campaign_timer": {
        "description": "",
        "show_timer": false,
        "expired_timer_message": {
          "Button": "",
          "description": "",
          "title": ""
        },
        "timer_detail": {
          "deduct_time": "",
          "expire_duration": 0,
          "expired_time": "",
          "server_time": ""
        }
      },
      "addresses": {
        "active": "",
        "disable_tabs": [],
        "data": []
      },
      "promo": {
        "error_default": {
          "title": "promo tidak dapat digunakan",
          "description": "silahkan coba beberapa saat lagi"
        },
        "last_apply": {
          "code": "200000",
          "data": {
            "codes": [],
            "promo_code_id": 0,
            "success": true,
            "voucher_orders": [],
            "tracking_details": [],
            "message": {
              "color": "",
              "state": "",
              "text": ""
            },
            "additional_info": {
              "sp_ids": [],
              "usage_summaries": [],
              "message_info": {
                "message": "Makin hemat pakai promo",
                "detail": ""
              },
              "error_detail": {
                "message": ""
              },
              "empty_cart_info": {
                "image_url": "",
                "detail": "",
                "message": ""
              },
              "promo_sp_ids": [],
              "poml_auto_applied": false,
              "bebas_ongkir_info": {
                "is_bo_unstack_enabled": true
              }
            }
          }
        }
      },
      "cross_sell": [],
      "add_on_wording": {
        "packaging_and_greeting_card": "{{qty}} barang akan dibungkus dalam 1 kemasan dan hanya dapat 1 kartu ucapan",
        "only_greeting_card": "{{qty}} barang hanya dapat 1 kartu ucapan",
        "invoice_not_sent_to_recipient": "Invoice tidak dikirim ke penerima pesanan"
      },
      "upsell": {
        "is_show": false,
        "title": "Nikmatin Bebas Ongkir tanpa batas",
        "description": "Langganan 6 bulan \u003cs\u003eRp300.000\u003c/s\u003e Rp150.000",
        "app_link": "tokopedia://webview?url\u003dhttps%3A%2F%2Fwww.tokopedia.com%2Fgotoplus%3Fatc_source%3Dpg_checkout%26source%3Dpg_checkout",
        "image": "https://images.tokopedia.net/img/plus/logo/account/globalmenu/checkout/Logo%20Area%20Entrypoints@4x.png"
      },
      "cart_data": "{\"data\":{\"codes\":[],\"grand_total\":60000,\"book\":false,\"service_id\":731644293353960,\"secret_key\":\"9136a5d48e5883ffbe731e1b322cd5b7dfb4f6a6\",\"user_data\":{\"user_id\":117045551,\"email\":\"hansen.wijaya+occ@tokopedia.com\",\"msisdn\":\"6287889005019\",\"msisdn_verified\":true,\"is_qc_acc\":true,\"app_version\":\"4.01\",\"ip_address\":\"202.179.187.226, 34.98.125.32\",\"user_agent\":\"TkpdConsumer/4.01 (Android 13;)\",\"advertisement_id\":\"68293182-fc55-461f-aae1-55409e5cbd3b\",\"device_type\":\"android\",\"device_id\":\"dmn13QCeQAyRDAjoAAMtnn:APA91bGp_r69HYqMq_ycTgLaZFeKVYeyWEUZtfd-mr6whPdJRH_2ToVeQ4GgI1Da-j87iPKbt8sSj5VMskEom1dyTlzRJjSYqep1qfVbezUOcII8XPry_zJGdp5dx2TG7fDcZrQpFB_V\"},\"meta_data\":{\"orders\":[{\"shop_id\":14005189,\"shop_name\":\"epharmos\",\"codes\":[],\"unique_id\":\"14005189-0-13888027-125529137\",\"is_po\":false,\"duration\":\"0\",\"warehouse_id\":13888027,\"address_id\":125529137,\"fulfill_by\":0}]},\"state\":\"checkout\"}}",
      "upsell_v2": {
        "is_show": false,
        "is_selected": false,
        "price": 0,
        "price_fmt": "",
        "duration": "",
        "description": "",
        "summary_info": "",
        "image": "",
        "app_link": "",
        "button": {
          "text": ""
        },
        "id": 0,
        "additional_vertical_id": 0,
        "transaction_type": ""
      },
      "dynamic_data_passing": {
        "is_ddp": false,
        "dynamic_data": "{\"payment_level\":{\"donation\":{\"validation\":false,\"predefined\":null,\"data\":{\"is_donation\":false,\"id\":146,\"description\":\"Donasi akan disalurkan dalam bentuk paket hidangan buka puasa untuk masyarakat prasejahtera di wilayah Jawa bersama BenihBaik.com \",\"title\":\"Donasi Rp5.000 untuk paket buka puasa masyarakat prasejahtera\",\"amount\":5000}},\"cross_sell\":{\"validation\":false,\"predefined\":null,\"data\":{\"main_vertical_id\":1,\"items\":[]}}},\"order_level\":[]}"
      }
    }
  }
}
            """,
            ShipmentAddressFormGqlResponse::class.java
        )

        if (response.shipmentAddressFormResponse.status == "OK") {
            return shipmentMapper.convertToShipmentAddressFormData(response.shipmentAddressFormResponse.data)
        } else {
            if (response.shipmentAddressFormResponse.errorMessages.isNotEmpty()) {
                throw CartResponseErrorException(response.shipmentAddressFormResponse.errorMessages.joinToString())
            } else {
                throw CartResponseErrorException(CartConstant.CART_ERROR_GLOBAL)
            }
        }
    }

    companion object {
        private const val PARAM_KEY_LANG = "lang"
        private const val PARAM_KEY_IS_ONE_CLICK_SHIPMENT = "is_ocs"
        private const val PARAM_KEY_CORNER_ID = "corner_id"
        private const val PARAM_KEY_SKIP_ONBOARDING_UPDATE_STATE = "skip_onboarding"
        private const val PARAM_KEY_IS_TRADEIN = "is_trade_in"
        private const val PARAM_KEY_DEVICE_ID = "dev_id"
        private const val PARAM_KEY_VEHICLE_LEASING_ID = "vehicle_leasing_id"
        private const val PARAM_KEY_IS_PLUS_SELECTED = "is_plus_selected"

        private const val QUERY_SHIPMENT_ADDRESS_FORM = "ShipmentAddressFormQuery"
    }
}
