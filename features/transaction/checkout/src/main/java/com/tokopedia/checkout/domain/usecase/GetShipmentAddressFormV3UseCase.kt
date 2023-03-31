package com.tokopedia.checkout.domain.usecase

import com.google.gson.Gson
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.checkout.data.model.response.shipmentaddressform.ShipmentAddressFormGqlResponse
import com.tokopedia.checkout.domain.mapper.ShipmentMapper
import com.tokopedia.checkout.domain.model.cartshipmentform.CartShipmentAddressFormData
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.localizationchooseaddress.common.ChosenAddressRequestHelper
import com.tokopedia.purchase_platform.common.constant.CartConstant
import com.tokopedia.purchase_platform.common.exception.CartResponseErrorException
import com.tokopedia.usecase.coroutines.UseCase
import timber.log.Timber
import javax.inject.Inject

class GetShipmentAddressFormV3UseCase @Inject constructor(
    @ApplicationContext private val graphqlRepository: GraphqlRepository,
    private val shipmentMapper: ShipmentMapper,
    private val chosenAddressRequestHelper: ChosenAddressRequestHelper
) : UseCase<CartShipmentAddressFormData>() {

    private var params: Map<String, Any?>? = null

    fun setParams(
        isOneClickShipment: Boolean,
        isTradeIn: Boolean,
        isSkipUpdateOnboardingState: Boolean,
        cornerId: String?,
        deviceId: String?,
        leasingId: String?,
        isPlusSelected: Boolean
    ) {
        val params: MutableMap<String, Any?> = HashMap()
        params[ChosenAddressRequestHelper.KEY_CHOSEN_ADDRESS] =
            chosenAddressRequestHelper.getChosenAddress()
        params[PARAM_KEY_LANG] = "id"
        params[PARAM_KEY_IS_ONE_CLICK_SHIPMENT] = isOneClickShipment
        params[PARAM_KEY_SKIP_ONBOARDING_UPDATE_STATE] = if (isSkipUpdateOnboardingState) 1 else 0
        if (cornerId != null) {
            try {
                val tmpCornerId = cornerId.toIntOrZero()
                params[PARAM_KEY_CORNER_ID] = tmpCornerId
            } catch (e: NumberFormatException) {
                Timber.d(e)
            }
        }
        if (leasingId != null && !leasingId.isEmpty()) {
            try {
                val tmpLeasingId = leasingId.toIntOrZero()
                params[PARAM_KEY_VEHICLE_LEASING_ID] = tmpLeasingId
            } catch (e: NumberFormatException) {
                Timber.d(e)
            }
        }
        if (isTradeIn) {
            params[PARAM_KEY_IS_TRADEIN] = true
            params[PARAM_KEY_DEVICE_ID] = deviceId ?: ""
        }
        params[PARAM_KEY_IS_PLUS_SELECTED] = isPlusSelected

        this.params = mapOf(
            "params" to params
        )
    }

    @GqlQuery(QUERY_SHIPMENT_ADDRESS_FORM, SHIPMENT_ADDRESS_FORM_V3_QUERY)
    override suspend fun executeOnBackground(): CartShipmentAddressFormData {
        if (params == null) {
            throw RuntimeException("Parameter is null!")
        }

        /*val request = GraphqlRequest(
            ShipmentAddressFormQuery(),
            ShipmentAddressFormGqlResponse::class.java,
            params
        )
        val response = graphqlRepository.response(listOf(request))
            .getSuccessData<ShipmentAddressFormGqlResponse>()*/

        val jsonRaw = """
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
                  "kero_token": "Tokopedia+Kero:kRpKR8O1k2kgBkkjGMgBWDQIs9w\u003d",
                  "kero_discom_token": "Tokopedia+Kero:v/b7/DMFssbCNGuV6TZUCTZ3xJk\u003d",
                  "kero_unix_time": 1680054497,
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
                        "address_id": 202185473,
                        "address_name": "Rumah 2",
                        "address": "test alamat lengkap",
                        "postal_code": "10110",
                        "phone": "6287767899273",
                        "receiver_name": "Warga Lokal",
                        "status": 2,
                        "country": "Indonesia",
                        "province_id": 13,
                        "province_name": "DKI Jakarta",
                        "city_id": 176,
                        "city_name": "Jakarta Pusat",
                        "district_id": 2274,
                        "district_name": "Gambir",
                        "address_2": "-6.1753926,106.827156",
                        "latitude": "-6.1753926",
                        "longitude": "106.827156",
                        "corner_id": 0,
                        "is_corner": false,
                        "state": 101,
                        "state_detail": "chosen_address_match",
                        "tokonow": {
                          "is_modified": false,
                          "shop_id": 11515028,
                          "warehouse_id": 11692055,
                          "warehouses": [
                            {
                              "warehouse_id": 11692055,
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
                      "group_shop": [
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
                          "errors_unblocking": [],
                          "shipping_id": 10,
                          "sp_id": 28,
                          "scheduled_delivery": {
                            "timeslot_id": 0,
                            "schedule_date": "",
                            "validation_metadata": ""
                          },
                          "rates_validation_flow": false,
                          "bo_code": "",
                          "is_insurance": true,
                          "is_fulfillment_service": false,
                          "toko_cabang": {
                            "message": "Dilayani Tokopedia",
                            "badge_url": "https://images.tokopedia.net/img/restriction-engine/bebas-ongkir/DilayaniTokopedia_Icon.png"
                          },
                          "cart_string": "1479278-2-740525-202185473",
                          "has_promo_list": false,
                          "shipment_information": {
                            "shop_location": "Jakarta Selatan",
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
                              "bo_name": "none / other",
                              "bo_type": 0,
                              "badge_url": ""
                            },
                            "preorder": {
                              "is_preorder": true,
                              "duration": "Pre Order 2 hari"
                            }
                          },
                          "is_disable_change_courier": false,
                          "auto_courier_selection": false,
                          "courier_selection_error": {
                            "title": "",
                            "description": ""
                          },
                          "bo_metadata": {
                            "bo_type": 0,
                            "bo_eligibilities": []
                          },
                          "save_state_flag": true,
                          "promo_codes": [],
                          "vehicle_leasing": {
                            "booking_fee": 0,
                            "is_leasing_product": false
                          },
                          "shop": {
                            "shop_id": 1479278,
                            "shop_name": "Tumbler Starbucks 123",
                            "shop_image": "https://images.tokopedia.net/img/cache/215-square/GAnVPX/2022/8/12/3400d291-c1d0-4966-a3f2-5101f90018fe.jpg",
                            "shop_url": "https://www.tokopedia.com/tumblersbux",
                            "shop_status": 1,
                            "postal_code": "12940",
                            "latitude": "-6.210079885624856",
                            "longitude": "106.82311870157719",
                            "district_id": 2270,
                            "district_name": "",
                            "origin": 0,
                            "address_street": "",
                            "province_id": 0,
                            "city_id": 175,
                            "city_name": "Jakarta Selatan",
                            "shop_alert_message": "",
                            "is_tokonow": false,
                            "is_gold": 0,
                            "is_official": 0,
                            "shop_ticker": "",
                            "shop_ticker_title": "",
                            "shop_type_info": {
                              "shop_tier": 0,
                              "shop_grade": 0,
                              "badge": "",
                              "badge_svg": "",
                              "title": "Regular Merchant"
                            },
                            "enabler_data": {
                              "label_name": "",
                              "show_label": false
                            }
                          },
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
                                },
                                {
                                  "ship_prod_id": 22,
                                  "ship_prod_name": "JNE Trucking",
                                  "ship_group_name": "regular",
                                  "ship_group_id": 1004,
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
                                  "ship_prod_id": 20,
                                  "ship_prod_name": "Same Day",
                                  "ship_group_name": "sameday",
                                  "ship_group_id": 1002,
                                  "additional_fee": 0,
                                  "minimum_weight": 0
                                },
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
                            },
                            {
                              "ship_id": 11,
                              "ship_name": "SiCepat",
                              "ship_code": "sicepat",
                              "ship_logo": "https://images.tokopedia.net/img/kurir-sicepat.png",
                              "is_dropship_enabled": 1,
                              "ship_prods": [
                                {
                                  "ship_prod_id": 51,
                                  "ship_prod_name": "HALU",
                                  "ship_group_name": "regular",
                                  "ship_group_id": 1004,
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
                              "ship_id": 13,
                              "ship_name": "GrabExpress",
                              "ship_code": "grab",
                              "ship_logo": "https://images.tokopedia.net/img/kurir-grab.png",
                              "is_dropship_enabled": 0,
                              "ship_prods": [
                                {
                                  "ship_prod_id": 24,
                                  "ship_prod_name": "Same Day",
                                  "ship_group_name": "sameday",
                                  "ship_group_id": 1002,
                                  "additional_fee": 0,
                                  "minimum_weight": 0
                                },
                                {
                                  "ship_prod_id": 37,
                                  "ship_prod_name": "Instant",
                                  "ship_group_name": "instant",
                                  "ship_group_id": 1000,
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
                                  "ship_prod_id": 45,
                                  "ship_prod_name": "Reguler",
                                  "ship_group_name": "regular",
                                  "ship_group_id": 1004,
                                  "additional_fee": 0,
                                  "minimum_weight": 0
                                },
                                {
                                  "ship_prod_id": 46,
                                  "ship_prod_name": "Next Day",
                                  "ship_group_name": "regular",
                                  "ship_group_id": 1004,
                                  "additional_fee": 0,
                                  "minimum_weight": 0
                                },
                                {
                                  "ship_prod_id": 49,
                                  "ship_prod_name": "Same Day",
                                  "ship_group_name": "regular",
                                  "ship_group_id": 1004,
                                  "additional_fee": 0,
                                  "minimum_weight": 0
                                },
                                {
                                  "ship_prod_id": 55,
                                  "ship_prod_name": "Economy",
                                  "ship_group_name": "regular",
                                  "ship_group_id": 1004,
                                  "additional_fee": 0,
                                  "minimum_weight": 0
                                },
                                {
                                  "ship_prod_id": 56,
                                  "ship_prod_name": "Cargo",
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
                            },
                            {
                              "ship_id": 26,
                              "ship_name": "Kurir Rekomendasi",
                              "ship_code": "tkpdexp",
                              "ship_logo": "https://images.tokopedia.net/img/kurir-rekomendasi.png",
                              "is_dropship_enabled": 0,
                              "ship_prods": [
                                {
                                  "ship_prod_id": 50,
                                  "ship_prod_name": "Reguler",
                                  "ship_group_name": "regular",
                                  "ship_group_id": 1004,
                                  "additional_fee": 0,
                                  "minimum_weight": 0
                                }
                              ]
                            },
                            {
                              "ship_id": 28,
                              "ship_name": "Paxel",
                              "ship_code": "paxel",
                              "ship_logo": "https://images.tokopedia.net/img/kurir-paxel.png",
                              "is_dropship_enabled": 1,
                              "ship_prods": [
                                {
                                  "ship_prod_id": 54,
                                  "ship_prod_name": "Next Day",
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
                                  "cart_id": "6036597212",
                                  "product_id": 7088814955,
                                  "product_name": "Product 48 PO",
                                  "product_price_fmt": "Rp10.000",
                                  "product_price": 10000,
                                  "product_original_price": 0,
                                  "product_wholesale_price": 10000,
                                  "product_wholesale_price_fmt": "Rp10.000",
                                  "product_weight_fmt": "1000gr",
                                  "product_weight": 1000,
                                  "product_weight_actual": 1000,
                                  "product_condition": 1,
                                  "product_url": "https://www.tokopedia.com/tumblersbux/product-48-po",
                                  "product_returnable": 0,
                                  "product_is_free_returns": 0,
                                  "product_is_preorder": 1,
                                  "product_cashback": "",
                                  "product_min_order": 4,
                                  "product_invenage_value": 8992,
                                  "product_switch_invenage": 1,
                                  "product_price_currency": 1,
                                  "product_image_src_200_square": "https://images.tokopedia.net/img/cache/200-square/VqbcmM/2022/11/29/6bd6ec90-eb7c-49d1-8a27-b64c97fd1cf6.jpg",
                                  "product_notes": "",
                                  "product_quantity": 8,
                                  "product_menu_id": 0,
                                  "product_finsurance": 0,
                                  "product_fcancel_partial": 0,
                                  "product_cat_id": 3965,
                                  "product_catalog_id": 0,
                                  "product_category": "Komputer \u0026 Laptop / Software / Multimedia",
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
                                    "duration_text": "2 Hari",
                                    "duration_day": "2"
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
                                    "bo_name": "none / other",
                                    "bo_type": 0,
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
                          ],
                          "warehouse": {
                            "warehouse_id": 740525,
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
                    "counter_cod": -1
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
                  "disabled_features_detail": {
                    "disabled_multi_address_message": ""
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
                        "cashback_advocate_referral_amount": 0,
                        "cashback_wallet_amount": 0,
                        "discount_amount": 0,
                        "title_description": "",
                        "is_tokopedia_gerai": false,
                        "global_success": true,
                        "gateway_id": "",
                        "is_coupon": 0,
                        "success": true,
                        "invoice_description": "",
                        "voucher_orders": [],
                        "clashing_info_detail": {
                          "is_clashed_promos": false,
                          "clash_reason": "",
                          "clash_message": ""
                        },
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
                            "is_bo_unstack_enabled": false
                          }
                        },
                        "benefit_summary_info": {
                          "final_benefit_amount_str": "",
                          "final_benefit_amount": 0,
                          "final_benefit_text": "",
                          "summaries": []
                        },
                        "ticker_info": {
                          "unique_id": "",
                          "status_code": 0,
                          "message": ""
                        }
                      }
                    }
                  },
                  "cross_sell": [],
                  "add_on_wording": {
                    "packaging_and_greeting_card": "",
                    "only_greeting_card": "",
                    "invoice_not_sent_to_recipient": ""
                  },
                  "upsell": {
                    "is_show": false,
                    "title": "",
                    "description": "",
                    "app_link": "",
                    "image": ""
                  },
                  "cart_data": "{\"data\":{\"codes\":[],\"grand_total\":80000,\"book\":false,\"service_id\":731644293353960,\"secret_key\":\"9136a5d48e5883ffbe731e1b322cd5b7dfb4f6a6\",\"user_data\":{\"user_id\":12299592,\"email\":\"try.sugiharto+01@tokopedia.com\",\"msisdn\":\"6287767899273\",\"msisdn_verified\":true,\"is_qc_acc\":true,\"app_version\":\"3.150\",\"ip_address\":\"202.179.187.226, 34.98.125.32\",\"user_agent\":\"TkpdConsumer/3.150 (Android 12;)\",\"advertisement_id\":\"dd49a6c2-8b6b-454b-833d-e1302d571124\",\"device_type\":\"android\",\"device_id\":\"cX61G9mfT4eO2QuXv1lfHU:APA91bFtEXHds8C17N5g6p25ZnHZMmg_xHwhlRkXVsF-0KdtprX2TjlfvSVxD4VeZgdFuN0OaTIGdHpPZIxGuo7ZILGjrO4h4p1YN1z29U9prosxlrKFUvt4cnB0skua4uEL1iIap4dC\"},\"meta_data\":{\"orders\":[{\"shop_id\":1479278,\"shop_name\":\"Tumbler Starbucks 123\",\"codes\":[],\"unique_id\":\"1479278-2-740525-202185473\",\"is_po\":true,\"duration\":\"2\",\"warehouse_id\":740525,\"address_id\":202185473,\"fulfill_by\":0}]},\"state\":\"checkout\"}}",
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
                    "dynamic_data": "{\"payment_level\":{\"donation\":{\"validation\":false,\"predefined\":null,\"data\":{\"is_donation\":false,\"id\":146,\"description\":\"Donasi akan disalurkan dalam bentuk paket hidangan buka puasa untuk masyarakat prasejahtera di wilayah Jawa bersama BenihBaik.com \",\"title\":\"Donasi Rp5.000 untuk paket buka puasa masyarakat prasejahtera\",\"amount\":5000}},\"cross_sell\":{\"validation\":false,\"predefined\":null,\"data\":{\"main_vertical_id\":0,\"items\":null}}},\"order_level\":[]}"
                  },
                  "platform_fee": {
                    "enable": true,
                    "profile_code": "TKPD_DEFAULT",
                    "additional_data": "{\"is_plus\":true}",
                    "error_wording":"Biaya jasa aplikasi gagal dimuat. Cek Total Tagihan di Pembayaran atau <a href=refresh>Muat Ulang</a>."
                  }
                }
              }
            }
        """.trimIndent()
        val gson = Gson()
        val response = gson.fromJson(jsonRaw, ShipmentAddressFormGqlResponse::class.java)

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
