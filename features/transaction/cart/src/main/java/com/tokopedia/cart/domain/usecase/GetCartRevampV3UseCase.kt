package com.tokopedia.cart.domain.usecase

import com.google.gson.Gson
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.cart.data.model.response.shopgroupsimplified.CartData
import com.tokopedia.cart.data.model.response.shopgroupsimplified.ShopGroupSimplifiedGqlResponse
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.localizationchooseaddress.common.ChosenAddressRequestHelper
import com.tokopedia.network.exception.ResponseErrorException
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class GetCartRevampV3UseCase @Inject constructor(@ApplicationContext private val graphqlRepository: GraphqlRepository,
                                                 private val chosenAddressRequestHelper: ChosenAddressRequestHelper) : UseCase<CartData>() {

    private var params: Map<String, Any>? = null

    fun setParams(cartId: String, state: Int) {
        params = mapOf(
                PARAM_KEY_LANG to PARAM_VALUE_ID,
                PARAM_KEY_SELECTED_CART_ID to cartId,
                PARAM_KEY_ADDITIONAL to mapOf(
                        ChosenAddressRequestHelper.KEY_CHOSEN_ADDRESS to chosenAddressRequestHelper.getChosenAddress(),
                        PARAM_KEY_STATE to state
                )
        )
    }

    override suspend fun executeOnBackground(): CartData {
        if (params == null) {
            throw RuntimeException("Parameter is null!")
        }

        val request = GraphqlRequest(getQueryCartRevampV3(), ShopGroupSimplifiedGqlResponse::class.java, params)
//        val response = graphqlRepository.getReseponse(listOf(request)).getSuccessData<ShopGroupSimplifiedGqlResponse>()
        val response = Gson().fromJson(RESPONSE, ShopGroupSimplifiedGqlResponse::class.java)

        if (response.shopGroupSimplifiedResponse.status == "OK") {
            return response.shopGroupSimplifiedResponse.data
        } else {
            throw ResponseErrorException(response.shopGroupSimplifiedResponse.errorMessages.joinToString(", "))
        }
    }

    val RESPONSE = """
        {
          "status": "OK",
          "cart_revamp_v3": {
            "error_message": [],
            "status": "OK",
            "data": {
              "errors": [],
              "popup_error_message": "",
              "empty_cart": {
                "title": "Wah, keranjang belanjamu kosong",
                "image": "https://ecs7.tokopedia.net/assets-tokopedia-lite/v2/zeus/kratos/103cf4bc.jpg",
                "description": "Daripada dianggurin, mending isi dengan barang-barang impianmu. Yuk, cek sekarang!",
                "buttons": [
                  {
                    "id": 1,
                    "code": "STARTSHOPPING",
                    "message": "Mulai Belanja",
                    "color": "green"
                  }
                ]
              },
              "out_of_service": {
                "id": 0,
                "code": "",
                "image": "",
                "title": "",
                "description": "",
                "buttons": []
              },
              "shopping_summary": {
                "total_wording": "Total Harga (3 Barang)",
                "total_value": 52050013,
                "discount_total_wording": "Total Diskon Barang",
                "discount_value": 0,
                "payment_total_wording": "Total Bayar",
                "payment_total_value": 52050013,
                "promo_wording": "Hemat pakai promo",
                "promo_value": 0,
                "seller_cashback_wording": "Cashback Penjual",
                "seller_cashback_value": 0
              },
              "max_quantity": 30000,
              "max_char_note": 144,
              "messages": {
                "ErrorFieldBetween": "Jumlah harus diisi antara 1 - {{value}}",
                "ErrorFieldMaxChar": "Catatan terlalu panjang, maks. {{value}} karakter.",
                "ErrorFieldRequired": "Jumlah harus diisi",
                "ErrorProductAvailableStock": "Stok tersedia:{{value}}",
                "ErrorProductAvailableStockDetail": "Harap kurangi jumlah barang",
                "ErrorProductMaxQuantity": "Maks. beli {{value}} barang",
                "ErrorProductMinQuantity": "Min. beli {{value}} barang"
              },
              "fulfillment_message": "Dilayani Tokopedia",
              "toko_cabang": {
                "message": "TokoCabang",
                "badge_url": "https://images.tokopedia.net/img/cart/toko-cabang/tc-icon.png"
              },
              "available_section": {
                "action": [
                  {
                    "id": 1,
                    "code": "WISHLIST",
                    "message": "Pindahkan ke Wishlist"
                  },
                  {
                    "id": 2,
                    "code": "DELETE",
                    "message": ""
                  },
                  {
                    "id": 3,
                    "code": "NOTES",
                    "message": "Tulis catatan untuk barang ini"
                  },
                  {
                    "id": 4,
                    "code": "VALIDATENOTES",
                    "message": "Maks. 144 character"
                  },
                  {
                    "id": 10,
                    "code": "WISHLISTED",
                    "message": "Sudah ada di wishlist"
                  }
                ],
                "available_group": [
                  {
                    "user_address_id": 0,
                    "bo_metadata": {
                      "bo_type": 0,
                      "bo_eligibilities": [],
                      "additional_attributes": []
                    },
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
                      "free_shipping_tokonow": {
                        "eligible": false,
                        "badge_url": ""
                      },
                      "preorder": {
                        "is_preorder": false,
                        "duration": ""
                      }
                    },
                    "shop": {
                      "shop_id": 6370771,
                      "user_id": 70588522,
                      "admin_ids": [],
                      "shop_name": "TX Shop PM",
                      "shop_image": "https://images.tokopedia.net/img/seller_no_logo_3.png",
                      "shop_url": "https://www.tokopedia.com/txshoppm",
                      "shop_status": 1,
                      "is_gold": 1,
                      "is_gold_badge": true,
                      "is_official": 0,
                      "is_free_returns": 0,
                      "gold_merchant": {
                        "is_gold": 1,
                        "is_gold_badge": true,
                        "gold_merchant_logo_url": "https://images.tokopedia.net/img/goldmerchant/pm_activation/badge/ic-powermerchant-130px.png",
                        "gold_expired": "",
                        "badge_svg": "https://assets.tokopedia.net/asts/goldmerchant/pm_activation/badge/ic-powermerchant.svg",
                        "is_idle": false,
                        "shop_tier": 1,
                        "shop_tier_wording": "Power Merchant",
                        "shop_grade": 0,
                        "shop_grade_wording": ""
                      },
                      "shop_type_info": {
                        "shop_tier": 1,
                        "shop_grade": 0,
                        "badge": "https://images.tokopedia.net/img/goldmerchant/pm_activation/badge/ic-powermerchant-130px.png",
                        "badge_svg": "https://assets.tokopedia.net/asts/goldmerchant/pm_activation/badge/ic-powermerchant.svg",
                        "title": "Power Merchant",
                        "title_fmt": "gold_merchant"
                      },
                      "official_store": {
                        "is_official": 0,
                        "os_logo_url": ""
                      },
                      "address_id": 2270,
                      "postal_code": "12930",
                      "latitude": "-6.221197900000001",
                      "longitude": "106.81941699999993",
                      "district_id": 2270,
                      "district_name": "",
                      "origin": 0,
                      "address_street": "",
                      "province_id": 0,
                      "city_id": 175,
                      "city_name": "Jakarta Selatan",
                      "province_name": "",
                      "country_name": "",
                      "is_allow_manage": false,
                      "shop_domain": "txshoppm",
                      "is_bridestory": false,
                      "is_cod": false,
                      "IsBOSellerApproval": false,
                      "shop_alert_message": "",
                      "is_tokonow": false,
                      "shop_ticker": "",
                      "maximum_weight_wording": "",
                      "maximum_shipping_weight": 0
                    },
                    "promo_codes": [],
                    "cart_string": "6370771-0-6595013",
                    "cart_details": [
                      {
                        "bundle_detail": {
                          "bundle_id": 123,
                          "bundle_group_id": "123-911324892-508261332",
                          "bundle_name": "Paket bundle",
                          "bundle_type": "MULTIPLE",
                          "bundle_status": "ACTIVE",
                          "bundle_start_time_unix": "1622746378",
                          "bundle_end_time_unix": "1623349675",
                          "bundle_description": "Lebih hemat xx%",
                          "bundle_price": 3000,
                          "bundle_price_fmt": "Rp3.000",
                          "bundle_original_price": 6000,
                          "bundle_original_price_fmt": "Rp6.000",
                          "bundle_min_order": 1,
                          "bundle_max_order": 4,
                          "bundle_quota": 10,
                          "edit_bundle_applink": "",
                          "bundle_qty": 1,
                          "slash_price_label": "50%"
                        },
                        "products": [
                          {
                            "checkbox_state": true,
                            "checkbox_show": true,
                            "errors": [],
                            "cart_id": 2838636113,
                            "product_id": 911324892,
                            "product_alias": "cyp-tetra",
                            "parent_id": 0,
                            "sku": "",
                            "campaign_id": 0,
                            "event_id": 0,
                            "is_big_campaign": false,
                            "product_name": "cyp - tetra",
                            "product_description": "",
                            "product_price_fmt": "Rp50.000.001",
                            "trade_in_info": {
                              "is_valid_trade_in": false,
                              "new_device_price": 0,
                              "new_device_price_fmt": "",
                              "old_device_price": 0,
                              "old_device_price_fmt": "",
                              "drop_off_enable": false,
                              "device_model": "",
                              "device_model_id": 0,
                              "diagnostic_id": 0
                            },
                            "product_price": 1000,
                            "product_original_price": 0,
                            "product_price_original_fmt": "",
                            "is_slash_price": false,
                            "is_slash_price_removal_quota": false,
                            "wholesale_price": [],
                            "product_wholesale_price": 1000,
                            "product_wholesale_price_fmt": "Rp1.000",
                            "product_weight_fmt": "100gr",
                            "product_weight": 100,
                            "product_weight_actual": 0,
                            "product_weight_volume": 0,
                            "is_product_volume_weight": false,
                            "product_condition": 1,
                            "product_url": "https://www.tokopedia.com/txshoppm/cyp-tetra",
                            "product_returnable": 0,
                            "product_is_free_returns": 0,
                            "free_returns": {
                              "is_freereturns": 0,
                              "free_returns_logo": ""
                            },
                            "product_is_preorder": 0,
                            "product_preorder": {},
                            "product_cashback": "",
                            "product_min_order": 1,
                            "product_invenage_value": 999999,
                            "product_switch_invenage": 1,
                            "currency_rate": 1,
                            "product_price_currency": 1,
                            "product_image_src_100_square": "https://ecs7.tokopedia.net/img/cache/100-square/product-1/2020/3/5/70588522/70588522_c5cf67db-92a6-4d3d-8c99-de0d2a79bbd7_760_760",
                            "product_image_src_200_square": "https://ecs7.tokopedia.net/img/cache/200-square/product-1/2020/3/5/70588522/70588522_c5cf67db-92a6-4d3d-8c99-de0d2a79bbd7_760_760",
                            "product_all_images": "[{\"file_name\":\"70588522_c5cf67db-92a6-4d3d-8c99-de0d2a79bbd7_760_760\",\"file_path\":\"product-1/2020/3/5/70588522\",\"status\":2}]",
                            "product_notes": "",
                            "product_quantity": 1,
                            "IsOutOfCoverage": false,
                            "product_menu_id": 20382881,
                            "product_finsurance": 1,
                            "product_fcancel_partial": 0,
                            "product_shipment": [],
                            "product_shipment_mapping": [],
                            "product_cat_id": 1659,
                            "product_catalog_id": 0,
                            "product_status": 1,
                            "product_tracker_data": {
                              "attribution": "none/other",
                              "tracker_list_name": "none/other"
                            },
                            "product_category": "Rumah Tangga / Taman / Hiasan Taman",
                            "product_total_weight": 100,
                            "product_total_weight_fmt": "100 gr",
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
                              "protection_checkbox_disabled": false,
                              "tokopedia_protection_price": 0,
                              "unit": "",
                              "protection_price_per_product_fmt": "",
                              "protection_price_fmt": "",
                              "source": "",
                              "protection_config": ""
                            },
                            "product_variants": {
                              "parent_id": 0,
                              "default_child": 0,
                              "variant": [],
                              "children": [],
                              "is_enabled": false,
                              "stock": 0
                            },
                            "warehouse_id": 6595013,
                            "cashback_percentage": 0,
                            "cashback_amount": 0,
                            "is_blacklisted": false,
                            "free_shipping": {
                              "eligible": false,
                              "badge_url": ""
                            },
                            "free_shipping_extra": {
                              "eligible": false,
                              "badge_url": ""
                            },
                            "free_shipping_tokonow": {
                              "eligible": false,
                              "badge_url": ""
                            },
                            "atc_time": 1623754221729544000,
                            "campaign_error_code": 0,
                            "categories": [
                              {
                                "category_id": 984,
                                "category_name": "Rumah Tangga",
                                "category_level": 1
                              },
                              {
                                "category_id": 1654,
                                "category_name": "Taman",
                                "category_level": 2
                              },
                              {
                                "category_id": 1659,
                                "category_name": "Hiasan Taman",
                                "category_level": 3
                              }
                            ],
                            "product_ticker": {
                              "show_ticker": false,
                              "message": ""
                            },
                            "additional_cart_info": {
                              "is_using_ovo": false,
                              "is_atc_booking_stock": false,
                              "show_ticker": false,
                              "default_courier_duration": false,
                              "timer_detail": {
                                "expired_time": "",
                                "deduct_time": "",
                                "expire_duration": 0
                              }
                            },
                            "is_available_cod": false,
                            "last_update_category_id": 1591917214,
                            "variant_description_detail": {
                              "variant_name": [],
                              "variant_description": ""
                            },
                            "product_information": [],
                            "product_alert_message": "",
                            "campagin_type_id": 0,
                            "ErrorCode": 0,
                            "similar_product_url": "",
                            "similar_product": {
                              "text": "",
                              "url": ""
                            },
                            "nicotine_lite_message": {
                              "text": "",
                              "url": ""
                            },
                            "member_label": "",
                            "selected_unavailable_action_link": ""
                          },
                          {
                            "errors": [],
                            "checkbox_state": true,
                            "checkbox_show": false,
                            "cart_id": 2492518130,
                            "product_id": 508261332,
                            "product_alias": "cyp-kotak-kosong",
                            "parent_id": 0,
                            "sku": "",
                            "campaign_id": 0,
                            "event_id": 0,
                            "is_big_campaign": false,
                            "product_name": "cyp - kotak kosong",
                            "product_description": "produk dummy. jangan dibeli!!!",
                            "product_price_fmt": "Rp50.000",
                            "trade_in_info": {
                              "is_valid_trade_in": false,
                              "new_device_price": 0,
                              "new_device_price_fmt": "",
                              "old_device_price": 0,
                              "old_device_price_fmt": "",
                              "drop_off_enable": false,
                              "device_model": "",
                              "device_model_id": 0,
                              "diagnostic_id": 0
                            },
                            "product_price": 2000,
                            "product_original_price": 0,
                            "product_price_original_fmt": "",
                            "is_slash_price": false,
                            "is_slash_price_removal_quota": false,
                            "wholesale_price": [],
                            "product_wholesale_price": 2000,
                            "product_wholesale_price_fmt": "Rp2.000",
                            "product_weight_fmt": "1gr",
                            "product_weight": 1,
                            "product_weight_actual": 0,
                            "product_weight_volume": 0,
                            "is_product_volume_weight": false,
                            "product_condition": 1,
                            "product_url": "https://www.tokopedia.com/txshoppm/cyp-kotak-kosong",
                            "product_returnable": 0,
                            "product_is_free_returns": 0,
                            "free_returns": {
                              "is_freereturns": 0,
                              "free_returns_logo": ""
                            },
                            "product_is_preorder": 0,
                            "product_preorder": {},
                            "product_cashback": "",
                            "product_min_order": 1,
                            "product_invenage_value": 999999,
                            "product_switch_invenage": 1,
                            "currency_rate": 1,
                            "product_price_currency": 1,
                            "product_image_src_100_square": "https://ecs7.tokopedia.net/img/cache/100-square/product-1/2019/7/12/70588522/70588522_35c0cf04-4c5e-43ba-a275-3585d852c155_800_800",
                            "product_image_src_200_square": "https://ecs7.tokopedia.net/img/cache/200-square/product-1/2019/7/12/70588522/70588522_35c0cf04-4c5e-43ba-a275-3585d852c155_800_800",
                            "product_all_images": "[{\"file_name\":\"70588522_35c0cf04-4c5e-43ba-a275-3585d852c155_800_800\",\"file_path\":\"product-1/2019/7/12/70588522\",\"status\":2}]",
                            "product_notes": "",
                            "product_quantity": 1,
                            "IsOutOfCoverage": false,
                            "product_menu_id": 20382881,
                            "product_finsurance": 0,
                            "product_fcancel_partial": 0,
                            "product_shipment": [],
                            "product_shipment_mapping": [],
                            "product_cat_id": 1889,
                            "product_catalog_id": 0,
                            "product_status": 1,
                            "product_tracker_data": {
                              "attribution": "none/other",
                              "tracker_list_name": "none/other"
                            },
                            "product_category": "Fashion Muslim / Dress Muslim Wanita / Gamis Wanita",
                            "product_total_weight": 1,
                            "product_total_weight_fmt": "1 gr",
                            "purchase_protection_plan_data": {
                              "protection_available": true,
                              "protection_type_id": 1873,
                              "protection_price_per_product": 2600,
                              "protection_price": 2600,
                              "protection_title": "Proteksi Fashion",
                              "protection_subtitle": "Ganti rugi 70% jika produk rusak total dalam 90 hari",
                              "protection_link_text": "Pelajari",
                              "protection_link_url": "https://www.tokopedia.com/asuransi/proteksi/kerusakan-total/",
                              "protection_opt_in": false,
                              "protection_checkbox_disabled": false,
                              "tokopedia_protection_price": 0,
                              "unit": "",
                              "protection_price_per_product_fmt": "",
                              "protection_price_fmt": "",
                              "source": "",
                              "protection_config": ""
                            },
                            "product_variants": {
                              "parent_id": 0,
                              "default_child": 0,
                              "variant": [],
                              "children": [],
                              "is_enabled": false,
                              "stock": 0
                            },
                            "warehouse_id": 6595013,
                            "cashback_percentage": 0,
                            "cashback_amount": 0,
                            "is_blacklisted": false,
                            "free_shipping": {
                              "eligible": false,
                              "badge_url": ""
                            },
                            "free_shipping_extra": {
                              "eligible": false,
                              "badge_url": ""
                            },
                            "free_shipping_tokonow": {
                              "eligible": false,
                              "badge_url": ""
                            },
                            "atc_time": 1616177617725231000,
                            "campaign_error_code": 0,
                            "categories": [
                              {
                                "category_id": 1760,
                                "category_name": "Fashion Muslim",
                                "category_level": 1
                              },
                              {
                                "category_id": 1878,
                                "category_name": "Dress Muslim Wanita",
                                "category_level": 2
                              },
                              {
                                "category_id": 1889,
                                "category_name": "Gamis Wanita",
                                "category_level": 3
                              }
                            ],
                            "product_ticker": {
                              "show_ticker": false,
                              "message": ""
                            },
                            "additional_cart_info": {
                              "is_using_ovo": false,
                              "is_atc_booking_stock": false,
                              "show_ticker": false,
                              "default_courier_duration": false,
                              "timer_detail": {
                                "expired_time": "",
                                "deduct_time": "",
                                "expire_duration": 0
                              }
                            },
                            "is_available_cod": false,
                            "last_update_category_id": 1587992369,
                            "variant_description_detail": {
                              "variant_name": [],
                              "variant_description": ""
                            },
                            "product_information": [],
                            "product_alert_message": "",
                            "campagin_type_id": 0,
                            "ErrorCode": 0,
                            "similar_product_url": "",
                            "similar_product": {
                              "text": "",
                              "url": ""
                            },
                            "nicotine_lite_message": {
                              "text": "",
                              "url": ""
                            },
                            "member_label": "",
                            "selected_unavailable_action_link": ""
                          }
                        ],
                        "errors": [],
                        "messages": []
                      },
                      {
                        "cart_id": 2492518130,
                        "products": [
                          {
                            "checkbox_state": true,
                            "checkbox_show": true,
                            "product_id": 508261332,
                            "product_name": "cyp - kotak kosong",
                            "product_alias": "cyp-kotak-kosong",
                            "parent_id": 0,
                            "sku": "",
                            "campaign_id": 0,
                            "event_id": 0,
                            "is_big_campaign": false,
                            "product_price_fmt": "Rp3.000",
                            "product_price": 3000,
                            "product_original_price": 0,
                            "product_price_original_fmt": "",
                            "product_wholesale_price": 0,
                            "is_slash_price": false,
                            "is_slash_price_removal_quota": false,
                            "category_id": 1889,
                            "category": "Fashion Muslim / Dress Muslim Wanita / Gamis Wanita",
                            "catalog_id": 0,
                            "wholesale_price": [],
                            "product_weight_fmt": "1gr",
                            "product_condition": 1,
                            "product_status": 1,
                            "product_url": "https://www.tokopedia.com/txshoppm/cyp-kotak-kosong",
                            "product_returnable": 0,
                            "is_freereturns": 0,
                            "free_returns": {
                              "is_freereturns": 0,
                              "free_returns_logo": ""
                            },
                            "is_preorder": 0,
                            "product_cashback": "",
                            "product_cashback_value": 0,
                            "product_min_order": 1,
                            "product_max_order": 30000,
                            "product_rating": 0,
                            "product_invenage_value": 999999,
                            "product_switch_invenage": 1,
                            "product_invenage_total": {
                              "by_user": {
                                "in_cart": 0,
                                "last_stock_less_than": 0
                              },
                              "by_user_text": {
                                "in_cart": "",
                                "last_stock_less_than": "",
                                "complete": ""
                              },
                              "is_counted_by_user": false,
                              "by_product": {
                                "in_cart": 0,
                                "last_stock_less_than": 0
                              },
                              "by_product_text": {
                                "in_cart": "",
                                "last_stock_less_than": "",
                                "complete": ""
                              },
                              "is_counted_by_product": false
                            },
                            "currency_rate": 1,
                            "product_price_currency": 1,
                            "product_image": {
                              "image_src_200_square": "https://ecs7.tokopedia.net/img/cache/200-square/product-1/2019/7/12/70588522/70588522_35c0cf04-4c5e-43ba-a275-3585d852c155_800_800"
                            },
                            "product_all_images": "[{\"file_name\":\"70588522_35c0cf04-4c5e-43ba-a275-3585d852c155_800_800\",\"file_path\":\"product-1/2019/7/12/70588522\",\"status\":2}]",
                            "product_notes": "",
                            "product_quantity": 1,
                            "price_changes": {
                              "changes_state": 0,
                              "amount_difference": 0,
                              "original_amount": 50000,
                              "description": "Harga Normal"
                            },
                            "product_weight": 1,
                            "product_weight_actual": 0,
                            "product_weight_volume": 0,
                            "product_weight_unit_code": 1,
                            "product_weight_unit_text": "gr",
                            "is_product_volume_weight": false,
                            "last_update_price": 1562902535,
                            "is_update_price": false,
                            "product_preorder": {},
                            "product_showcase": {
                              "name": "produk",
                              "id": 20382881
                            },
                            "product_finsurance": 0,
                            "product_shop_id": 6370771,
                            "is_wishlisted": true,
                            "product_tracker_data": {
                              "attribution": "",
                              "tracker_list_name": ""
                            },
                            "is_ppp": false,
                            "is_cod": false,
                            "warehouse_id": 6595013,
                            "IsOutOfCoverage": false,
                            "warehouses": {},
                            "is_parent": false,
                            "is_campaign_error": false,
                            "campaign_type_id": 0,
                            "campaign_type_name": "",
                            "hide_gimmick": false,
                            "is_blacklisted": false,
                            "categories": [
                              {
                                "category_id": 1760,
                                "category_name": "Fashion Muslim",
                                "category_level": 1
                              },
                              {
                                "category_id": 1878,
                                "category_name": "Dress Muslim Wanita",
                                "category_level": 2
                              },
                              {
                                "category_id": 1889,
                                "category_name": "Gamis Wanita",
                                "category_level": 3
                              }
                            ],
                            "free_shipping": {
                              "eligible": false,
                              "badge_url": ""
                            },
                            "free_shipping_extra": {
                              "eligible": false,
                              "badge_url": ""
                            },
                            "free_shipping_tokonow": {
                              "eligible": false,
                              "badge_url": ""
                            },
                            "campaign_error_code": 0,
                            "additional_cart_info": {
                              "is_using_ovo": false,
                              "is_atc_booking_stock": false,
                              "show_ticker": false,
                              "default_courier_duration": false,
                              "timer_detail": {
                                "expired_time": "",
                                "deduct_time": "",
                                "expire_duration": 0
                              }
                            },
                            "brand": {
                              "id": 0,
                              "name": "",
                              "is_active": false,
                              "brand_status": 0
                            },
                            "last_update_category_id": 1587992369,
                            "payment_profile": "",
                            "variant_description_detail": {
                              "variant_name": [],
                              "variant_description": ""
                            },
                            "initial_price": 50000,
                            "initial_price_fmt": "Rp50.000",
                            "slash_price_label": "",
                            "product_warning_message": "",
                            "product_alert_message": "",
                            "product_information": [],
                            "IsCampaignMaxOrderAvailable": false,
                            "campaign_short_name": ""
                          }
                        ],
                        "errors": [],
                        "messages": [],
                        "checkbox_state": true,
                        "similar_product_url": "",
                        "similar_product": {
                          "text": "",
                          "url": ""
                        },
                        "nicotine_lite_message": {
                          "text": "",
                          "url": ""
                        },
                        "member_label": "",
                        "selected_unavailable_action_link": "",
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
                          "protection_checkbox_disabled": false,
                          "tokopedia_protection_price": 0,
                          "unit": "",
                          "protection_price_per_product_fmt": "",
                          "protection_price_fmt": "",
                          "source": "",
                          "protection_config": ""
                        }
                      }
                    ],
                    "total_cart_details_error": 0,
                    "total_cart_price": 50050001,
                    "errors": [],
                    "sort_key": 2838636113,
                    "is_fulfillment_service": false,
                    "warehouse": {
                      "warehouse_id": 6595013,
                      "partner_id": 0,
                      "shop_id": 6370771,
                      "warehouse_name": "Shop Location",
                      "district_id": 2270,
                      "district_name": "Setiabudi",
                      "city_id": 175,
                      "city_name": "Jakarta Selatan",
                      "province_id": 13,
                      "province_name": "DKI Jakarta",
                      "status": 1,
                      "postal_code": "12930",
                      "is_default": 1,
                      "latlon": "-6.221197900000001,106.81941699999993",
                      "latitude": "-6.221197900000001",
                      "longitude": "106.81941699999993",
                      "email": "",
                      "address_detail": "Jalan Karet Sawah, Kecamatan Setiabudi, 12930",
                      "country_name": "Indonesia",
                      "is_fulfillment": false,
                      "tkpd_preferred_logistic_spid": [],
                      "red_zone": false,
                      "message": ""
                    },
                    "has_promo_list": false,
                    "checkbox_state": true
                  },
                  {
                    "user_address_id": 0,
                    "bo_metadata": {
                      "bo_type": 0,
                      "bo_eligibilities": [],
                      "additional_attributes": []
                    },
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
                      "free_shipping_tokonow": {
                        "eligible": false,
                        "badge_url": ""
                      },
                      "preorder": {
                        "is_preorder": true,
                        "duration": "Pre Order 45 hari"
                      }
                    },
                    "shop": {
                      "shop_id": 6778692,
                      "user_id": 70399913,
                      "admin_ids": [],
                      "shop_name": "elizabethsatu",
                      "shop_image": "https://images.tokopedia.net/img/seller_no_logo_0.png",
                      "shop_url": "https://www.tokopedia.com/elizabethsatu",
                      "shop_status": 1,
                      "is_gold": 1,
                      "is_gold_badge": true,
                      "is_official": 0,
                      "is_free_returns": 0,
                      "gold_merchant": {
                        "is_gold": 1,
                        "is_gold_badge": true,
                        "gold_merchant_logo_url": "https://images.tokopedia.net/img/goldmerchant/pm_activation/badge/ic-powermerchant-130px.png",
                        "gold_expired": "",
                        "badge_svg": "https://assets.tokopedia.net/asts/goldmerchant/pm_activation/badge/ic-powermerchant.svg",
                        "is_idle": false,
                        "shop_tier": 1,
                        "shop_tier_wording": "Power Merchant",
                        "shop_grade": 0,
                        "shop_grade_wording": ""
                      },
                      "shop_type_info": {
                        "shop_tier": 1,
                        "shop_grade": 0,
                        "badge": "https://images.tokopedia.net/img/goldmerchant/pm_activation/badge/ic-powermerchant-130px.png",
                        "badge_svg": "https://assets.tokopedia.net/asts/goldmerchant/pm_activation/badge/ic-powermerchant.svg",
                        "title": "Power Merchant",
                        "title_fmt": "gold_merchant"
                      },
                      "official_store": {
                        "is_official": 0,
                        "os_logo_url": ""
                      },
                      "address_id": 2271,
                      "postal_code": "12870",
                      "latitude": "-6.22810891137005",
                      "longitude": "106.84549670666458",
                      "district_id": 2271,
                      "district_name": "",
                      "origin": 0,
                      "address_street": "",
                      "province_id": 0,
                      "city_id": 175,
                      "city_name": "Jakarta Selatan",
                      "province_name": "",
                      "country_name": "",
                      "is_allow_manage": false,
                      "shop_domain": "elizabethsatu",
                      "is_bridestory": false,
                      "is_cod": false,
                      "IsBOSellerApproval": false,
                      "shop_alert_message": "",
                      "is_tokonow": false,
                      "shop_ticker": "",
                      "maximum_weight_wording": "",
                      "maximum_shipping_weight": 0
                    },
                    "promo_codes": [],
                    "cart_string": "6778692-45-6987195",
                    "cart_details": [
                      {
                        "bundle_detail": {
                          "bundle_id": 0,
                          "bundle_group_id": 0,
                          "bundle_name": "",
                          "bundle_type": "",
                          "bundle_status": "",
                          "bundle_start_time_unix": "",
                          "bundle_end_time_unix": "",
                          "bundle_description": "",
                          "bundle_price": 0,
                          "bundle_price_fmt": "",
                          "bundle_min_order": 0,
                          "bundle_max_order": 0,
                          "bundle_quota": 0,
                          "edit_bundle_applink": "",
                          "bundle_qty": 1
                        },
                        "products": [
                          {
                            "errors": [],
                            "checkbox_state": true,
                            "checkbox_show": true,
                            "cart_id": 2838636113,
                            "product_id": 911324892,
                            "product_alias": "cyp-tetra",
                            "parent_id": 0,
                            "sku": "",
                            "campaign_id": 0,
                            "event_id": 0,
                            "is_big_campaign": false,
                            "product_name": "cyp - tetra",
                            "product_description": "",
                            "product_price_fmt": "Rp50.000.001",
                            "trade_in_info": {
                              "is_valid_trade_in": false,
                              "new_device_price": 0,
                              "new_device_price_fmt": "",
                              "old_device_price": 0,
                              "old_device_price_fmt": "",
                              "drop_off_enable": false,
                              "device_model": "",
                              "device_model_id": 0,
                              "diagnostic_id": 0
                            },
                            "product_price": 4000,
                            "product_original_price": 0,
                            "product_price_original_fmt": "",
                            "is_slash_price": false,
                            "is_slash_price_removal_quota": false,
                            "wholesale_price": [],
                            "product_wholesale_price": 4000,
                            "product_wholesale_price_fmt": "Rp4.000.001",
                            "product_weight_fmt": "100gr",
                            "product_weight": 100,
                            "product_weight_actual": 0,
                            "product_weight_volume": 0,
                            "is_product_volume_weight": false,
                            "product_condition": 1,
                            "product_url": "https://www.tokopedia.com/txshoppm/cyp-tetra",
                            "product_returnable": 0,
                            "product_is_free_returns": 0,
                            "free_returns": {
                              "is_freereturns": 0,
                              "free_returns_logo": ""
                            },
                            "product_is_preorder": 0,
                            "product_preorder": {},
                            "product_cashback": "",
                            "product_min_order": 1,
                            "product_invenage_value": 999999,
                            "product_switch_invenage": 1,
                            "currency_rate": 1,
                            "product_price_currency": 1,
                            "product_image_src_100_square": "https://ecs7.tokopedia.net/img/cache/100-square/product-1/2020/3/5/70588522/70588522_c5cf67db-92a6-4d3d-8c99-de0d2a79bbd7_760_760",
                            "product_image_src_200_square": "https://ecs7.tokopedia.net/img/cache/200-square/product-1/2020/3/5/70588522/70588522_c5cf67db-92a6-4d3d-8c99-de0d2a79bbd7_760_760",
                            "product_all_images": "[{\"file_name\":\"70588522_c5cf67db-92a6-4d3d-8c99-de0d2a79bbd7_760_760\",\"file_path\":\"product-1/2020/3/5/70588522\",\"status\":2}]",
                            "product_notes": "",
                            "product_quantity": 1,
                            "IsOutOfCoverage": false,
                            "product_menu_id": 20382881,
                            "product_finsurance": 1,
                            "product_fcancel_partial": 0,
                            "product_shipment": [],
                            "product_shipment_mapping": [],
                            "product_cat_id": 1659,
                            "product_catalog_id": 0,
                            "product_status": 1,
                            "product_tracker_data": {
                              "attribution": "none/other",
                              "tracker_list_name": "none/other"
                            },
                            "product_category": "Rumah Tangga / Taman / Hiasan Taman",
                            "product_total_weight": 100,
                            "product_total_weight_fmt": "100 gr",
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
                              "protection_checkbox_disabled": false,
                              "tokopedia_protection_price": 0,
                              "unit": "",
                              "protection_price_per_product_fmt": "",
                              "protection_price_fmt": "",
                              "source": "",
                              "protection_config": ""
                            },
                            "product_variants": {
                              "parent_id": 0,
                              "default_child": 0,
                              "variant": [],
                              "children": [],
                              "is_enabled": false,
                              "stock": 0
                            },
                            "warehouse_id": 6595013,
                            "cashback_percentage": 0,
                            "cashback_amount": 0,
                            "is_blacklisted": false,
                            "free_shipping": {
                              "eligible": false,
                              "badge_url": ""
                            },
                            "free_shipping_extra": {
                              "eligible": false,
                              "badge_url": ""
                            },
                            "free_shipping_tokonow": {
                              "eligible": false,
                              "badge_url": ""
                            },
                            "atc_time": 1623754221729544000,
                            "campaign_error_code": 0,
                            "categories": [
                              {
                                "category_id": 984,
                                "category_name": "Rumah Tangga",
                                "category_level": 1
                              },
                              {
                                "category_id": 1654,
                                "category_name": "Taman",
                                "category_level": 2
                              },
                              {
                                "category_id": 1659,
                                "category_name": "Hiasan Taman",
                                "category_level": 3
                              }
                            ],
                            "product_ticker": {
                              "show_ticker": false,
                              "message": ""
                            },
                            "additional_cart_info": {
                              "is_using_ovo": false,
                              "is_atc_booking_stock": false,
                              "show_ticker": false,
                              "default_courier_duration": false,
                              "timer_detail": {
                                "expired_time": "",
                                "deduct_time": "",
                                "expire_duration": 0
                              }
                            },
                            "is_available_cod": false,
                            "last_update_category_id": 1591917214,
                            "variant_description_detail": {
                              "variant_name": [],
                              "variant_description": ""
                            },
                            "product_information": [],
                            "product_alert_message": "",
                            "campagin_type_id": 0,
                            "ErrorCode": 0,
                            "similar_product_url": "",
                            "similar_product": {
                              "text": "",
                              "url": ""
                            },
                            "nicotine_lite_message": {
                              "text": "",
                              "url": ""
                            },
                            "member_label": "",
                            "selected_unavailable_action_link": ""
                          }
                        ],
                        "errors": [],
                        "messages": []
                      }
                    ],
                    "total_cart_details_error": 0,
                    "total_cart_price": 8000048,
                    "errors": [],
                    "sort_key": 2165436100,
                    "is_fulfillment_service": false,
                    "warehouse": {
                      "warehouse_id": 6987195,
                      "partner_id": 0,
                      "shop_id": 6778692,
                      "warehouse_name": "Shop Location",
                      "district_id": 2271,
                      "district_name": "Tebet",
                      "city_id": 175,
                      "city_name": "Jakarta Selatan",
                      "province_id": 13,
                      "province_name": "DKI Jakarta",
                      "status": 1,
                      "postal_code": "12870",
                      "is_default": 1,
                      "latlon": "-6.22810891137005,106.84549670666458",
                      "latitude": "-6.22810891137005",
                      "longitude": "106.84549670666458",
                      "email": "",
                      "address_detail": "Jalan Pal Batu 1, Kecamatan Tebet, 12870",
                      "country_name": "Indonesia",
                      "is_fulfillment": false,
                      "tkpd_preferred_logistic_spid": [],
                      "red_zone": false,
                      "message": ""
                    },
                    "has_promo_list": false,
                    "checkbox_state": true
                  }
                ]
              },
              "unavailable_ticker": "",
              "unavailable_section_action": [
                {
                  "id": 7,
                  "code": "SHOWLESS",
                  "message": "Tampilkan lebih sedikit"
                },
                {
                  "id": 8,
                  "code": "SHOWMORE",
                  "message": "Tampilkan semua"
                }
              ],
              "unavailable_section": [],
              "total_product_price": 58050049,
              "total_product_count": 3,
              "total_product_error": 0,
              "global_coupon_attr": {
                "description": "Gunakan promo Tokopedia",
                "quantity_label": ""
              },
              "global_checkbox_state": true,
              "tickers": [],
              "hashed_email": "984b8b5cb46134207c1808c6a04e29e8",
              "promo": {
                "last_apply": {
                  "data": {
                    "global_success": true,
                    "success": true,
                    "message": {
                      "state": "",
                      "color": "",
                      "text": ""
                    },
                    "codes": [],
                    "promo_code_id": 0,
                    "title_description": "",
                    "discount_amount": 0,
                    "cashback_amount": 0,
                    "cashback_wallet_amount": 0,
                    "cashback_advocate_referral_amount": 0,
                    "cashback_voucher_description": "",
                    "invoice_description": "",
                    "gateway_id": "",
                    "is_tokopedia_gerai": false,
                    "is_coupon": 0,
                    "coupon_description": "",
                    "voucher_orders": [],
                    "benefit_summary_info": {
                      "final_benefit_text": "",
                      "final_benefit_amount_str": "",
                      "final_benefit_amount": 0,
                      "summaries": []
                    },
                    "clashing_info_detail": {
                      "clash_message": "",
                      "clash_reason": "",
                      "is_clashed_promos": false,
                      "options": []
                    },
                    "tracking_details": [],
                    "benefit_details": [],
                    "ticker_info": {
                      "unique_id": "",
                      "status_code": 0,
                      "message": ""
                    },
                    "tokopoints_detail": {
                      "conversion_rate": {
                        "rate": 0,
                        "points_coefficient": 0,
                        "external_currency_coefficient": 0
                      }
                    },
                    "additional_info": {
                      "message_info": {
                        "message": "",
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
                      "promo_sp_ids": []
                    }
                  },
                  "code": "200000"
                },
                "error_default": {
                  "title": "promo tidak dapat digunakan",
                  "description": "silahkan coba beberapa saat lagi"
                }
              },
              "customer_data": {
                "id": 0,
                "name": "",
                "email": "",
                "phone": "",
                "msisdn": ""
              },
              "ab_test_button": {
                "enable": true,
                "color": "primary_green"
              },
              "localization_choose_address": {
                "address_id": 0,
                "address_name": "",
                "address": "",
                "postal_code": "",
                "phone": "",
                "receiver_name": "",
                "status": 0,
                "country": "",
                "province_id": 0,
                "province_name": "",
                "city_id": 0,
                "city_name": "",
                "district_id": 0,
                "district_name": "",
                "corner_id": 0,
                "is_corner": false,
                "is_primary": false,
                "buyer_store_code": "",
                "type": 0,
                "state": 0,
                "state_detail": "",
                "tokonow": {
                  "shop_id": 0,
                  "warehouse_id": 0
                }
              },
              "promo_summary": {
                "title": "Bisa hemat pakai promo",
                "detail": []
              },
              "pop_up_message": ""
            }
          }
        }
    """.trimIndent()

    companion object {
        const val PARAM_KEY_LANG = "lang"
        const val PARAM_KEY_SELECTED_CART_ID = "selected_cart_id"
        const val PARAM_KEY_ADDITIONAL = "additional_params"
        const val PARAM_KEY_STATE = "state"

        const val PARAM_VALUE_ID = "id"
    }

}