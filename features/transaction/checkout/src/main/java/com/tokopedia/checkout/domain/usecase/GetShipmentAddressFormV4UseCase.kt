package com.tokopedia.checkout.domain.usecase

import com.google.gson.Gson
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.checkout.data.model.request.saf.ShipmentAddressFormRequest
import com.tokopedia.checkout.data.model.response.shipmentaddressform.ShipmentAddressFormGqlResponse
import com.tokopedia.checkout.domain.mapper.ShipmentMapper
import com.tokopedia.checkout.domain.model.cartshipmentform.CartShipmentAddressFormData
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
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

    val jsonRaw = """
        {
          "shipment_address_form_v4": {
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
              "kero_token": "Tokopedia+Kero:9j5Br4wexNPtEi0shJcn6B2i1x8\u003d",
              "kero_discom_token": "Tokopedia+Kero:5eL60gPEag8CsVWreGP99eDZN60\u003d",
              "kero_unix_time": 1690768745,
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
                "front_end_validation": true,
                "consultation_flow": false,
                "rejected_wording": "Produk berikut tidak bisa diproses karena pembeliannya tidak disetujui dokter."
              },
              "open_prerequisite_site": false,
              "eligible_new_shipping_experience": true,
              "pop_up_message": "asdfasdfasdf",
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
                    "address_id": 151959883,
                    "address_name": "panjang",
                    "address": "Jl. Panjang, Kec. Kb. Jeruk, Kota Jakarta Barat, Daerah Khusus Ibukota Jakarta [Tokopedia Note: panjang]",
                    "postal_code": "11530",
                    "phone": "6287889005019",
                    "receiver_name": "Hansen Prod",
                    "status": 1,
                    "country": "Indonesia",
                    "province_id": 13,
                    "province_name": "DKI Jakarta",
                    "city_id": 174,
                    "city_name": "Jakarta Barat",
                    "district_id": 2256,
                    "district_name": "Kebon Jeruk",
                    "address_2": "-6.194531200000001,106.7688255",
                    "latitude": "-6.194531200000001",
                    "longitude": "106.7688255",
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
                        }
                      ],
                      "service_type": "2h"
                    }
                  },
                  "group_shop": [
                    {
                      "subtotal_add_ons": [
                        {
                          "wording": "Jasa Pasang ({{qty}} Barang)",
                          "type": 3
                        },
                        {
                          "wording": "Biaya Proteksi ({{qty}} Barang)",
                          "type": 4
                        }
                      ],
                      "group_type": 0,
                      "ui_group_type": 0,
                      "group_information": {
                        "name": "11515028tknw",
                        "badge_url": "https://images.tokopedia.net/img/now/now-clock-badge.png",
                        "description": "Jakarta Barat",
                        "description_badge_url": ""
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
                      "shipping_id": 10,
                      "sp_id": 28,
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
                      "cart_string": "11515028-0-11528221-151959883",
                      "has_promo_list": false,
                      "shipment_information": {
                        "shop_location": "Jakarta Barat",
                        "estimation": "",
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
                        "bo_type": 3,
                        "bo_eligibilities": [
                          {
                            "key": "is_tokonow",
                            "value": "true"
                          },
                          {
                            "key": "bo_type",
                            "value": "3"
                          },
                          {
                            "key": "campaign_ids",
                            "value": "35,176"
                          }
                        ]
                      },
                      "save_state_flag": false,
                      "promo_codes": [],
                      "vehicle_leasing": {
                        "booking_fee": 0,
                        "is_leasing_product": false
                      },
                      "group_shop_v2_saf": [
                        {
                          "cart_string_order": "11515028-0-11528221-151959883",
                          "shop": {
                            "shop_id": 11515028,
                            "shop_name": "11515028tknw",
                            "postal_code": "11610",
                            "latitude": "-6.17383580000002",
                            "longitude": "106.73374999999999",
                            "district_id": 2257,
                            "shop_alert_message": "",
                            "is_tokonow": true,
                            "is_gold": 1,
                            "is_official": 1,
                            "shop_ticker": "",
                            "shop_ticker_title": "",
                            "shop_type_info": {
                              "shop_tier": 2,
                              "shop_grade": 0,
                              "badge": "https://images.tokopedia.net/img/now/now-clock-badge.png",
                              "badge_svg": "https://assets.tokopedia.net/asts/NOW-Badge-Icon.svg",
                              "title": "Official Store"
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
                                "bundle_group_id": "",
                                "bundle_id": 0,
                                "bundle_name": "",
                                "bundle_original_price": 0,
                                "bundle_price": 0,
                                "bundle_qty": 0,
                                "bundle_type": "",
                                "slash_price_label": "",
                                "bundle_icon_url": ""
                              },
                              "products": [
                                {
                                  "origin_warehouse_ids": [],
                                  "add_ons_product": {
                                    "icon_url": "",
                                    "title": "",
                                    "bottomsheet": {
                                      "title": "",
                                      "applink": "",
                                      "is_shown": false
                                    },
                                    "data": []
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
                                  "cart_id": "6580201872",
                                  "product_id": 5320822952,
                                  "product_name": "multiple_kecap_manis - 200 ml",
                                  "product_price": 8000,
                                  "product_original_price": 0,
                                  "product_wholesale_price": 8000,
                                  "product_weight_fmt": "200gr",
                                  "product_weight": 200,
                                  "product_weight_actual": 200,
                                  "product_is_free_returns": 0,
                                  "product_is_preorder": 0,
                                  "product_cashback": "",
                                  "product_price_currency": 1,
                                  "product_image_src_200_square": "https://images.tokopedia.net/img/cache/200-square/hDjmkQ/2022/8/9/b43d16d7-e943-4d6e-a539-ff676d9d7743.jpg",
                                  "product_notes": "",
                                  "product_quantity": 1,
                                  "product_menu_id": 31808111,
                                  "product_finsurance": 0,
                                  "product_fcancel_partial": 0,
                                  "product_cat_id": 4810,
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
                                    "parent_id": 5320793986
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
                                  "origin_warehouse_ids": [],
                                  "add_ons_product": {
                                    "icon_url": "",
                                    "title": "",
                                    "bottomsheet": {
                                      "title": "",
                                      "applink": "",
                                      "is_shown": false
                                    },
                                    "data": []
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
                                  "cart_id": "6580201690",
                                  "product_id": 7029764088,
                                  "product_name": "flash_sale_salak",
                                  "product_price": 90000,
                                  "product_original_price": 100000,
                                  "product_wholesale_price": 90000,
                                  "product_weight_fmt": "100gr",
                                  "product_weight": 100,
                                  "product_weight_actual": 100,
                                  "product_is_free_returns": 0,
                                  "product_is_preorder": 0,
                                  "product_cashback": "",
                                  "product_price_currency": 1,
                                  "product_image_src_200_square": "https://images.tokopedia.net/img/cache/200-square/hDjmkQ/2022/11/22/c79df332-0cef-458a-add7-2a3d989c3a3a.jpg",
                                  "product_notes": "",
                                  "product_quantity": 2,
                                  "product_menu_id": 33364940,
                                  "product_finsurance": 0,
                                  "product_fcancel_partial": 0,
                                  "product_cat_id": 4923,
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
                                    "variant_description": ""
                                  },
                                  "product_alert_message": "",
                                  "product_information": [],
                                  "campaign_id": 2216680,
                                  "ethical_drug": {
                                    "need_prescription": false,
                                    "icon_url": "https://images.tokopedia.net/img/cartapp/icons/ethical_drug.png",
                                    "text": "Butuh Resep"
                                  }
                                },
                                {
                                  "origin_warehouse_ids": [],
                                  "add_ons_product": {
                                    "icon_url": "",
                                    "title": "",
                                    "bottomsheet": {
                                      "title": "",
                                      "applink": "",
                                      "is_shown": false
                                    },
                                    "data": []
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
                                  "cart_id": "6580201283",
                                  "product_id": 5194296612,
                                  "product_name": "single_beras_merah",
                                  "product_price": 1000,
                                  "product_original_price": 0,
                                  "product_wholesale_price": 1000,
                                  "product_weight_fmt": "100gr",
                                  "product_weight": 100,
                                  "product_weight_actual": 100,
                                  "product_is_free_returns": 0,
                                  "product_is_preorder": 0,
                                  "product_cashback": "",
                                  "product_price_currency": 1,
                                  "product_image_src_200_square": "https://images.tokopedia.net/img/cache/200-square/hDjmkQ/2022/8/3/1fb67f2a-33df-43a0-88fd-cb633139729c.jpg",
                                  "product_notes": "",
                                  "product_quantity": 1,
                                  "product_menu_id": 31808113,
                                  "product_finsurance": 0,
                                  "product_fcancel_partial": 0,
                                  "product_cat_id": 5010,
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
                                "bundle_group_id": "bid:643387-pid:3525110198-pid1:3525110198-pid2:3537981175",
                                "bundle_id": 643387,
                                "bundle_name": "auto multi kuota",
                                "bundle_original_price": 8000,
                                "bundle_price": 7000,
                                "bundle_qty": 1,
                                "bundle_type": "MULTIPLE",
                                "slash_price_label": "13%",
                                "bundle_icon_url": "https://images.tokopedia.net/img/cartapp/bundling_icon.png"
                              },
                              "products": [
                                {
                                  "origin_warehouse_ids": [],
                                  "add_ons_product": {
                                    "icon_url": "",
                                    "title": "",
                                    "bottomsheet": {
                                      "title": "",
                                      "applink": "",
                                      "is_shown": false
                                    },
                                    "data": []
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
                                  "cart_id": "6562018768",
                                  "product_id": 3537981175,
                                  "product_name": "multiple_biji_kopi - 1 kg",
                                  "product_price": 3500,
                                  "product_original_price": 4000,
                                  "product_wholesale_price": 3500,
                                  "product_weight_fmt": "100gr",
                                  "product_weight": 100,
                                  "product_weight_actual": 100,
                                  "product_is_free_returns": 0,
                                  "product_is_preorder": 0,
                                  "product_cashback": "",
                                  "product_price_currency": 1,
                                  "product_image_src_200_square": "https://images.tokopedia.net/img/cache/200-square/hDjmkQ/2022/4/13/016be363-43e6-412c-83c2-98e9fddd6b69.jpg",
                                  "product_notes": "",
                                  "product_quantity": 1,
                                  "product_menu_id": 31808111,
                                  "product_finsurance": 0,
                                  "product_fcancel_partial": 0,
                                  "product_cat_id": 5062,
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
                                    "parent_id": 3537981143
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
                                  "origin_warehouse_ids": [],
                                  "add_ons_product": {
                                    "icon_url": "",
                                    "title": "",
                                    "bottomsheet": {
                                      "title": "",
                                      "applink": "",
                                      "is_shown": false
                                    },
                                    "data": []
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
                                  "cart_id": "6562018767",
                                  "product_id": 3525110198,
                                  "product_name": "multiple_minyak_telon",
                                  "product_price": 3500,
                                  "product_original_price": 4000,
                                  "product_wholesale_price": 3500,
                                  "product_weight_fmt": "100gr",
                                  "product_weight": 100,
                                  "product_weight_actual": 100,
                                  "product_is_free_returns": 0,
                                  "product_is_preorder": 0,
                                  "product_cashback": "",
                                  "product_price_currency": 1,
                                  "product_image_src_200_square": "https://images.tokopedia.net/img/cache/200-square/hDjmkQ/2022/4/12/5feda6db-2380-4c9b-a654-0a730a3e16c1.jpg",
                                  "product_notes": "",
                                  "product_quantity": 1,
                                  "product_menu_id": 31808111,
                                  "product_finsurance": 0,
                                  "product_fcancel_partial": 0,
                                  "product_cat_id": 5025,
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
                              "cart_detail_info": {
                                "cart_detail_type": "BMGM",
                                "bmgm": {
                                  "offer_id": 1,
                                  "offer_name": "Buy More Get More",
                                  "offer_icon": "left icon",
                                  "offer_message": "Yay, kamu dapat potongan Rp120 rb",
                                  "offer_landing_page_link": "/offer-landing-page/1?warehouseID=12",
                                  "total_discount": 50000,
                                  "offer_json_data": "{.....}",
                                  "tiers_applied": [
                                    {
                                      "tier_id": 5,
                                      "tier_message": "",
                                      "tier_discount_text": "diskon 25%",
                                      "tier_discount_amount": 65000,
                                      "price_before_benefit": 1100000,
                                      "price_after_benefit": 1035000,
                                      "list_product": [
                                        {
                                          "product_id": 3537981175,
                                          "warehouse_id": 12,
                                          "qty": 1,
                                          "final_price": 400000,
                                          "price_after_bmgm": 300000,
                                          "cart_id": "123"
                                        },
                                        {
                                          "product_id": 112,
                                          "warehouse_id": 12,
                                          "qty": 1,
                                          "final_price": 200000,
                                          "cart_id": "1235"
                                        },
                                        {
                                          "product_id": 113,
                                          "warehouse_id": 12,
                                          "qty": 1,
                                          "final_price": 300000,
                                          "cart_id": "1234"
                                        }
                                      ]
                                    },
                                    {
                                      "tier_id": 3,
                                      "tier_message": "",
                                      "tier_discount_text": "65rb",
                                      "tier_discount_amount": 65000,
                                      "price_before_benefit": 1100000,
                                      "price_after_benefit": 1035000,
                                      "list_product": [
                                        {
                                          "product_id": 3525110198,
                                          "warehouse_id": 12,
                                          "qty": 1,
                                          "final_price": 400000,
                                          "cart_id": "123"
                                        },
                                        {
                                          "product_id": 112,
                                          "warehouse_id": 12,
                                          "qty": 1,
                                          "final_price": 200000,
                                          "cart_id": "1235"
                                        },
                                        {
                                          "product_id": 113,
                                          "warehouse_id": 12,
                                          "qty": 1,
                                          "final_price": 300000,
                                          "cart_id": "1234"
                                        }
                                      ]
                                    }
                                  ]
                                }
                              },
                              "products": [
                                {
                                  "origin_warehouse_ids": [],
                                  "add_ons_product": {
                                    "icon_url": "",
                                    "title": "",
                                    "bottomsheet": {
                                      "title": "",
                                      "applink": "",
                                      "is_shown": false
                                    },
                                    "data": []
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
                                  "cart_id": "6562018768",
                                  "product_id": 3537981175,
                                  "product_name": "BMGM multiple_biji_kopi - 1 kg",
                                  "product_price": 3500,
                                  "product_original_price": 4000,
                                  "product_wholesale_price": 3500,
                                  "product_weight_fmt": "100gr",
                                  "product_weight": 100,
                                  "product_weight_actual": 100,
                                  "product_is_free_returns": 0,
                                  "product_is_preorder": 0,
                                  "product_cashback": "",
                                  "product_price_currency": 1,
                                  "product_image_src_200_square": "https://images.tokopedia.net/img/cache/200-square/hDjmkQ/2022/4/13/016be363-43e6-412c-83c2-98e9fddd6b69.jpg",
                                  "product_notes": "",
                                  "product_quantity": 1,
                                  "product_menu_id": 31808111,
                                  "product_finsurance": 0,
                                  "product_fcancel_partial": 0,
                                  "product_cat_id": 5062,
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
                                    "parent_id": 3537981143
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
                                  "origin_warehouse_ids": [],
                                  "add_ons_product": {
                                    "icon_url": "",
                                    "title": "",
                                    "bottomsheet": {
                                      "title": "",
                                      "applink": "",
                                      "is_shown": false
                                    },
                                    "data": []
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
                                  "cart_id": "6562018767",
                                  "product_id": 3525110198,
                                  "product_name": "BMGM multiple_minyak_telon",
                                  "product_price": 3500,
                                  "product_original_price": 4000,
                                  "product_wholesale_price": 3500,
                                  "product_weight_fmt": "100gr",
                                  "product_weight": 100,
                                  "product_weight_actual": 100,
                                  "product_is_free_returns": 0,
                                  "product_is_preorder": 0,
                                  "product_cashback": "",
                                  "product_price_currency": 1,
                                  "product_image_src_200_square": "https://images.tokopedia.net/img/cache/200-square/hDjmkQ/2022/4/12/5feda6db-2380-4c9b-a654-0a730a3e16c1.jpg",
                                  "product_notes": "",
                                  "product_quantity": 1,
                                  "product_menu_id": 31808111,
                                  "product_finsurance": 0,
                                  "product_fcancel_partial": 0,
                                  "product_cat_id": 5025,
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
                            }
                          ]
                        }
                      ],
                      "dropshipper": {
                        "name": "",
                        "telp_no": ""
                      },
                      "warehouse": {
                        "warehouse_id": 11528221,
                        "city_name": "Jakarta Barat"
                      }
                    }
                  ]
                }
              ],
              "donation": {
                "Title": "Donasi testing",
                "Nominal": 3500,
                "Description": "Donasi ke testing wallet"
              },
              "cod": {
                "is_cod": false,
                "counter_cod": -1
              },
              "message": {
                "message_info": "",
                "message_link": "",
                "message_logo": ""
              },
              "egold_attributes": {
                "eligible": true,
                "is_tiering": false,
                "is_opt_in": false,
                "range": {
                  "min": 1000,
                  "max": 50000
                },
                "message": {
                  "title_text": "Egold testing",
                  "sub_text": "Egold testing subtext",
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
              "tickers": [
                {
                  "id": 1002,
                  "title": "",
                  "message": "",
                  "page": "checkout"
                }
              ],
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
                "title": "",
                "description": "",
                "app_link": "",
                "image": ""
              },
              "cart_data": "{\"data\":{\"codes\":[],\"grand_total\":196000,\"book\":false,\"service_id\":731644293353960,\"secret_key\":\"9136a5d48e5883ffbe731e1b322cd5b7dfb4f6a6\",\"user_data\":{\"user_id\":117045551,\"email\":\"hansen.wijaya+occ@tokopedia.com\",\"msisdn\":\"6287889005019\",\"msisdn_verified\":true,\"is_qc_acc\":true,\"app_version\":\"4.100\",\"ip_address\":\"117.54.140.18, 34.98.125.32\",\"user_agent\":\"TkpdConsumer/4.100 (Android 12;)\",\"advertisement_id\":\"07c528f8-6d0b-4b4d-9243-799b3a61988e\",\"device_type\":\"android\",\"device_id\":\"cDlsjYXoTA-uF6TUqY3jHw:APA91bG_BQYV84yUpm5qahUpE-qisabfv39C2vCd4A7czek7E6XfRX2pdqf8nV4aLqgsfKjXGGmd8elzqotGeGoD_KpzLjEh6b17FTF2mmYgXsVnKNAZXU8SYHKvWZGyy9Sd7QNrhuie\"},\"meta_data\":{\"orders\":[{\"shop_id\":11515028,\"shop_name\":\"11515028tknw\",\"codes\":[],\"unique_id\":\"11515028-0-11528221-151959883\",\"is_po\":false,\"duration\":\"0\",\"warehouse_id\":11528221,\"address_id\":151959883,\"fulfill_by\":0}]},\"state\":\"checkout\"}}",
              "add_ons_summary": [
                {
                  "wording": "Total Jasa Pasang ({{qty}} Jasa)",
                  "type": 3
                },
                {
                  "wording": "Total Biaya Proteksi ({{qty}} Polis)",
                  "type": 4
                }
              ],
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
                "dynamic_data": "{\"payment_level\":{\"donation\":{\"validation\":false,\"predefined\":null,\"data\":{\"is_donation\":false,\"id\":0,\"description\":\"\",\"title\":\"\",\"amount\":0}},\"cross_sell\":{\"validation\":false,\"predefined\":null,\"data\":{\"main_vertical_id\":1,\"items\":[{\"id\":24159,\"price\":50000,\"transaction_type\":\"PLUS\",\"additional_vertical_id\":6,\"info\":{\"client_number\":\"117045551\",\"quota_id\":642,\"pct_tokopedia_subsidy\":100,\"pct_external_subsidy\":0},\"metadata\":\"{\\\"operator_name\\\":\\\"Langganan PLUS\\\",\\\"fintech_partner_amount\\\":0,\\\"client_number_type\\\":\\\"registered\\\",\\\"integration_data\\\":{\\\"is_temporary\\\":\\\"false\\\",\\\"period\\\":\\\"bulan\\\",\\\"plus_background\\\":\\\"https://images.tokopedia.net/img/plus/upsell-checkout/background/Upsell_Plus_Supergraphic.png\\\",\\\"plus_button\\\":\\\"{\\\\\\\"text\\\\\\\":\\\\\\\"Langganan\\\\\\\",\\\\\\\"url\\\\\\\":\\\\\\\"\\\\\\\",\\\\\\\"url_mobile\\\\\\\":\\\\\\\"https://www.tokopedia.com/gotoplus?source\u003dpg_checkout_v2\\\\\\\",\\\\\\\"app_link\\\\\\\":\\\\\\\"tokopedia://webview?url\u003dhttps%3A%2F%2Fwww.tokopedia.com%2Fgotoplus%3Fsource%3Dpg_checkout_v2\\\\\\\",\\\\\\\"type\\\\\\\":\\\\\\\"Button\\\\\\\",\\\\\\\"is_shown\\\\\\\":true,\\\\\\\"is_disabled\\\\\\\":false,\\\\\\\"position\\\\\\\":\\\\\\\"\\\\\\\",\\\\\\\"json_metadata\\\\\\\":\\\\\\\"\\\\\\\"}\\\",\\\"plus_icon\\\":\\\"https://images.tokopedia.net/img/plus/upsell-checkout/icon/Plus_Logo_for_Upsells.png\\\",\\\"price\\\":\\\"50.000\\\",\\\"subtitle\\\":\\\"Cobain PLUS 1 bulan buat belanja pakai \\\\u003cb\\\\u003eBebas Ongkir lebih banyak!\\\\u003c/b\\\\u003e\\\",\\\"title\\\":\\\"Langganan GoTo Plus\\\"},\\\"enquiry_log_id\\\":6213947091}\",\"disable\":false,\"error_blocking\":false}]}}},\"order_level\":[]}"
              },
              "platform_fee": {
                "enable": true,
                "profile_code": "TKPD_DEFAULT",
                "additional_data": "{\"is_plus\":false}",
                "error_wording": "Biaya jasa aplikasi gagal dimuat. Cek Total Tagihan di Pembayaran atau \u003ca href\u003d\"refresh\"\u003eMuat Ulang\u003c/a\u003e."
              }
            }
          }
        }
    """.trimIndent()
    override suspend fun execute(params: ShipmentAddressFormRequest): CartShipmentAddressFormData {
        val gson = Gson()
        val obj: ShipmentAddressFormGqlResponse = gson.fromJson(
            jsonRaw,
            ShipmentAddressFormGqlResponse::class.java
        )

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
        if (!params.leasingId.isNullOrEmpty()) {
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
            ShipmentAddressFormQuery(),
            ShipmentAddressFormGqlResponse::class.java,
            mapOf(
                "params" to paramMap
            )
        )
        val response = graphqlRepository.response(listOf(request))
            .getSuccessData<ShipmentAddressFormGqlResponse>()

        if (response.shipmentAddressFormResponse.status == "OK") {
            return shipmentMapper.convertToShipmentAddressFormData(obj.shipmentAddressFormResponse.data)
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
