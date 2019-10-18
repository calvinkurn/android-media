package com.tokopedia.purchase_platform.features.cart.domain.usecase

import com.google.gson.Gson
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.network.exception.ResponseErrorException
import com.tokopedia.purchase_platform.features.cart.data.model.response.ShopGroupSimplifiedGqlResponse
import com.tokopedia.purchase_platform.features.cart.domain.mapper.CartMapperV3
import com.tokopedia.purchase_platform.features.cart.domain.model.cartlist.CartListData
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by Irfan Khoirul on 2019-10-18.
 */

class GetCartListSimplifiedUseCase @Inject constructor(@Named("shopGroupSimplifiedQuery") private val queryString: String,
                                                       private val graphqlUseCase: GraphqlUseCase,
                                                       private val cartMapperV3: CartMapperV3) : UseCase<CartListData>() {

    override fun createObservable(p0: RequestParams?): Observable<CartListData> {
        val graphqlRequest = GraphqlRequest(queryString, ShopGroupSimplifiedGqlResponse::class.java)
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)
        return graphqlUseCase.createObservable(RequestParams.EMPTY).map {
//            val shopGroupSimplifiedGqlResponse = it.getData<ShopGroupSimplifiedGqlResponse>(ShopGroupSimplifiedGqlResponse::class.java)
            val shopGroupSimplifiedGqlResponse = Gson().fromJson(mockResponse, ShopGroupSimplifiedGqlResponse::class.java)
            if (shopGroupSimplifiedGqlResponse.shopGroupSimplifiedResponse.status == "OK") {
                cartMapperV3.convertToCartItemDataList(shopGroupSimplifiedGqlResponse.shopGroupSimplifiedResponse.data)
            } else {
                if (shopGroupSimplifiedGqlResponse.shopGroupSimplifiedResponse.errorMessages.isNotEmpty()) {
                    throw ResponseErrorException(shopGroupSimplifiedGqlResponse.shopGroupSimplifiedResponse.errorMessages.joinToString())
                } else {
                    throw ResponseErrorException()
                }
            }
        }

    }

    val mockResponse =
            """
  {
    "shop_group_simplified": {
      "status": "OK",
      "error_message": [],
      "data": {
        "errors": [
          "Ada barang atau toko yang gagal diproses. Refresh halaman dan perbaiki keranjangmu dulu ya."
        ],
        "is_coupon_active": 0,
        "is_one_tab_promo": false,
        "max_quantity": 0,
        "max_char_note": 0,
        "messages": {
          "ErrorFieldBetween": "",
          "ErrorFieldMaxChar": "",
          "ErrorFieldRequired": "",
          "ErrorProductAvailableStock": "",
          "ErrorProductAvailableStockDetail": "",
          "ErrorProductMaxQuantity": "",
          "ErrorProductMinQuantity": ""
        },
        "donation": {
          "Title": "",
          "Nominal": 0,
          "Description": ""
        },
        "autoapply_stack": {
          "global_success": false,
          "success": false,
          "message": {
            "state": "",
            "color": "",
            "text": ""
          },
          "codes": [],
          "promo_code_id": 0,
          "title_description": "",
          "discount_amount": 0,
          "cashback_wallet_amount": 0,
          "cashback_advocate_referral_amount": 0,
          "cashback_voucher_description": "",
          "invoice_description": "",
          "gateway_id": "",
          "is_coupon": 0,
          "coupon_description": "",
          "voucher_orders": [],
          "benefit_summary_info": {
            "final_benefit_text": "",
            "final_benefit_amount": 0,
            "final_benefit_amount_str": "",
            "summaries": []
          },
          "clashing_info_detail": {
            "clash_message": "",
            "clash_reason": "",
            "is_clashed_promos": false,
            "options": []
          },
          "tracking_details": [],
          "ticker_info": {
            "unique_id": "",
            "status_code": 0,
            "message": ""
          },
          "benefit_details": []
        },
        "global_coupon_attr": {
          "description": "Gunakan promo Tokopedia",
          "quantity_label": ""
        },
        "tickers": [],
        "default_promo_dialog_tab": "",
        "total_product_price": "10000",
        "total_product_count": 1,
        "total_product_error": 1,
        "global_checkbox_state": false,
        "hashed_email": "c630e8053412031c4ab875cb68ad182d",
        "shop_group_available": [
          {
            "errors": [],
            "user_address_id": 0,
            "cart_string": "480282-0-548",
            "total_cart_details_error": 0,
            "total_cart_price": "10000",
            "sort_key": 33375057,
            "is_fulfillment_service": false,
            "has_promo_list": true,
            "shop": {
              "shop_id": 480282,
              "user_id": 5514029,
              "shop_name": "marcelina store",
              "shop_image": "https://ecs7.tokopedia.net/img/default_v3-shopnophoto.png",
              "shop_url": "https://staging.tokopedia.com/marcelinastore",
              "shop_status": 1,
              "is_gold": 0,
              "is_gold_badge": false,
              "is_official": 0,
              "is_free_returns": 0,
              "gold_merchant": {
                "is_gold": 0,
                "is_gold_badge": false,
                "gold_merchant_logo_url": ""
              },
              "official_store": {
                "is_official": 0,
                "os_logo_url": ""
              },
              "address_id": 2270,
              "postal_code": "12910",
              "latitude": "-999",
              "longitude": "-999",
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
              "shop_domain": "marcelinastore"
            },
            "warehouse": {
              "warehouse_id": 548,
              "partner_id": 0,
              "shop_id": 480282,
              "warehouse_name": "Shop location",
              "district_id": 2270,
              "district_name": "Setiabudi",
              "city_id": 175,
              "city_name": "Jakarta Selatan",
              "province_id": 13,
              "province_name": "DKI Jakarta",
              "status": 1,
              "postal_code": "12910",
              "is_default": 1,
              "latlon": "-999,-999",
              "latitude": "-999",
              "longitude": "-999",
              "email": "",
              "address_detail": "karet setiabudi",
              "country_name": "Indonesia",
              "is_fulfillment": false
            },
            "checkbox_state": true,
            "cart_details": [
              {
                "errors": [],
                "cart_id": "33375057",
                "checkbox_state": true,
                "similar_product_url": "",
                "similar_product": {
                  "text": "",
                  "url": ""
                },
                "messages": [],
                "product": {
                  "product_tracker_data": {
                    "attribution": "none/other",
                    "tracker_list_name": "none/other"
                  },
                  "isWishlist": false,
                  "product_id": 15161678,
                  "product_name": "celana bahan",
                  "product_price_fmt": "Rp10.000",
                  "product_price": 10000,
                  "parent_id": 0,
                  "category_id": 1826,
                  "category": "Fashion Pria / Celana / Celana Panjang",
                  "catalog_id": 0,
                  "wholesale_price": [],
                  "product_weight_fmt": "1kg",
                  "product_weight": 1,
                  "product_condition": 1,
                  "product_status": 1,
                  "product_url": "https://staging.tokopedia.com//celana-bahan",
                  "product_returnable": 0,
                  "is_freereturns": 0,
                  "is_preorder": 0,
                  "product_cashback": "",
                  "product_min_order": 1,
                  "product_max_order": 10000,
                  "product_rating": "0.000000",
                  "product_invenage_value": 10000,
                  "product_switch_invenage": 1,
                  "product_price_currency": 1,
                  "product_invenage_total": {
                    "is_counted_by_user": false,
                    "is_counted_by_product": false,
                    "by_user": {
                      "in_cart": 0,
                      "last_stock_less_than": 0
                    },
                    "by_user_text": {
                      "in_cart": "",
                      "last_stock_less_than": "",
                      "complete": ""
                    },
                    "by_product": {
                      "in_cart": 0,
                      "last_stock_less_than": 0
                    },
                    "by_product_text": {
                      "in_cart": "",
                      "last_stock_less_than": "",
                      "complete": ""
                    }
                  },
                  "price_changes": {
                    "changes_state": 0,
                    "amount_difference": 0,
                    "original_amount": 0,
                    "description": ""
                  },
                  "product_image": {
                    "image_src": "https://cdn-staging.tokopedia.com/img/cache/700/product-1/2018/1/18/5514029/5514029_146653fe-9a56-459a-bbdc-d9435541883d_960_720.jpg",
                    "image_src_200_square": "https://cdn-staging.tokopedia.com/img/cache/200-square/product-1/2018/1/18/5514029/5514029_146653fe-9a56-459a-bbdc-d9435541883d_960_720.jpg",
                    "image_src_300": "https://cdn-staging.tokopedia.com/img/cache/300/product-1/2018/1/18/5514029/5514029_146653fe-9a56-459a-bbdc-d9435541883d_960_720.jpg",
                    "image_src_square": "https://cdn-staging.tokopedia.com/img/cache/500-square/product-1/2018/1/18/5514029/5514029_146653fe-9a56-459a-bbdc-d9435541883d_960_720.jpg"
                  },
                  "product_all_images": "[{\"file_path\":\"product-1/2018/1/18/5514029\",\"file_name\":\"5514029_146653fe-9a56-459a-bbdc-d9435541883d_960_720.jpg\",\"status\":2}]",
                  "product_notes": "",
                  "product_quantity": 1,
                  "product_weight_unit_code": 2,
                  "product_weight_unit_text": "kg",
                  "last_update_price": "1516246265",
                  "is_update_price": false,
                  "product_preorder": {
                    "duration_text": "",
                    "duration_day": "0",
                    "duration_unit_code": "0",
                    "duration_unit_text": "",
                    "duration_value": "0"
                  },
                  "product_showcase": {
                    "id": 1403336
                  },
                  "product_alias": "celana-bahan",
                  "sku": "",
                  "campaign_id": 0,
                  "product_original_price": 0,
                  "product_price_original_fmt": "",
                  "is_slash_price": false,
                  "free_returns": {
                    "free_returns_logo": "https://ecs7.tokopedia.net/img/icon-frs.png"
                  },
                  "product_finsurance": 0,
                  "is_wishlisted": true,
                  "is_ppp": false,
                  "is_cod": false,
                  "warehouse_id": 548,
                  "is_parent": false,
                  "is_campaign_error": false
                }
              }
            ]
          }
        ],
        "shop_group_with_errors": [
          {
            "errors": [],
            "user_address_id": 0,
            "cart_string": "480579-0-2240",
            "total_cart_details_error": 0,
            "total_cart_price": "246",
            "sort_key": 33377691,
            "is_fulfillment_service": false,
            "has_promo_list": true,
            "shop": {
              "shop_id": 480579,
              "user_id": 8940774,
              "shop_name": "tahutomotahuto",
              "shop_image": "https://ecs7.tokopedia.net/img/cache/215-square/shops-1/2019/6/14/480579/480579_44669a71-3e7c-4148-ab82-94eb4d7e0075.png",
              "shop_url": "https://staging.tokopedia.com/fujiwara-tofu",
              "shop_status": 1,
              "is_gold": 1,
              "is_gold_badge": true,
              "is_official": 1,
              "is_free_returns": 0,
              "gold_merchant": {
                "is_gold": 0,
                "is_gold_badge": false,
                "gold_merchant_logo_url": ""
              },
              "official_store": {
                "is_official": 1,
                "os_logo_url": "https://ecs7.tokopedia.net/img/official_store/badge_os128.png"
              },
              "address_id": 2253,
              "postal_code": "11710",
              "latitude": "-6.148665099999985",
              "longitude": "106.73525840000002",
              "district_id": 2253,
              "district_name": "",
              "origin": 0,
              "address_street": "",
              "province_id": 0,
              "city_id": 174,
              "city_name": "Jakarta Barat",
              "province_name": "",
              "country_name": "",
              "is_allow_manage": false,
              "shop_domain": "fujiwara-tofu"
            },
            "warehouse": {
              "warehouse_id": 2240,
              "partner_id": 0,
              "shop_id": 480579,
              "warehouse_name": "Shop location",
              "district_id": 2253,
              "district_name": "Cengkareng",
              "city_id": 174,
              "city_name": "Jakarta Barat",
              "province_id": 13,
              "province_name": "DKI Jakarta",
              "status": 1,
              "postal_code": "11710",
              "is_default": 1,
              "latlon": "-6.148665099999985,106.73525840000002",
              "latitude": "-6.148665099999985",
              "longitude": "106.73525840000002",
              "email": "",
              "address_detail": "Jalan puspa 2, cengkareng, 11720",
              "country_name": "Indonesia",
              "is_fulfillment": false
            },
            "checkbox_state": true,
            "cart_details": []
          }
        ]
      }
    }
  }""".trimIndent()

}