package com.tokopedia.buy_more_get_more.minicart.domain.mapper

import com.tokopedia.cachemanager.gson.GsonSingleton
import com.tokopedia.minicart.common.data.response.minicartlist.MiniCartGqlResponse

/**
 * Created by @ilhamsuaib on 18/09/23.
 */

open class BaseMiniCartDataMapper {

    protected fun getMockMiniCartResponseWithBmgmAndWholesalePrice(): MiniCartGqlResponse {
        val mockResponse = """
            {
              "mini_cart_v3": {
                "error_message": [],
                "status": "OK",
                "data": {
                  "shopping_summary": {
                    "total_original_value": 250000,
                    "total_value": 250000
                  },
                  "available_section": {
                    "available_group": [
                      {
                        "cart_details": [
                          {
                            "cart_detail_info": {
                              "cart_detail_type": "bmgm",
                              "bmgm": {
                                "offer_id": 910,
                                "offer_name": "[Test] Square BMSM",
                                "offer_message": [
                                  "Beli 10, <b>diskon 20%</b> di [Test] Square BMSM!",
                                  "Tambah 4 lagi diskon 20% di [Test] Square BMSM"
                                ],
                                "offer_json_data": "",
                                "offer_status": 2,
                                "is_tier_achieved": false,
                                "tier_product": [
                                  {
                                    "tier_id": 0,
                                    "tier_message": "Tambah 4 lagi diskon 20% di [Test] Square BMSM",
                                    "tier_discount_text": "",
                                    "tier_discount_amount": 0,
                                    "price_before_benefit": 250000,
                                    "price_after_benefit": 250000,
                                    "list_product": [
                                      {
                                        "product_id": 2150835655,
                                        "warehouse_id": 345977,
                                        "quantity": 2,
                                        "price_before_benefit": 50000,
                                        "price_after_benefit": 50000,
                                        "cart_id": "2166690162"
                                      }
                                    ]
                                  }
                                ]
                              }
                            },
                            "products": [
                              {
                                "cart_id": 2166690162,
                                "product_id": 2150835655,
                                "product_quantity": 2,
                                "product_name": "Jarum_Suntik",
                                "product_image": {
                                  "image_src_100_square": "https://images.tokopedia.net/img/cache/100-square/hDjmkQ/2022/11/16/5b32ada4-9b17-48b7-a557-97b4de8fe398.jpg"
                                },
                                "warehouse_id": 345977,
                                "wholesale_price": [
                                  {
                                    "qty_min": 2,
                                    "qty_max": 10000,
                                    "prd_prc": 35000
                                  }
                                ]
                              }
                            ]
                          }
                        ]
                      }
                    ]
                  }
                }
              }
            }
        """.trimIndent()
        return GsonSingleton.instance.fromJson(mockResponse, MiniCartGqlResponse::class.java)
    }

    protected fun getMockMiniCartResponseWithBmgmButNotEligibleWholesalePrice(): MiniCartGqlResponse {
        val mockResponse = """
            {
              "mini_cart_v3": {
                "error_message": [],
                "status": "OK",
                "data": {
                  "shopping_summary": {
                    "total_original_value": 250000,
                    "total_value": 250000
                  },
                  "available_section": {
                    "available_group": [
                      {
                        "cart_details": [
                          {
                            "cart_detail_info": {
                              "cart_detail_type": "bmgm",
                              "bmgm": {
                                "offer_id": 910,
                                "offer_type_id": 1,
                                "offer_name": "[Test] Square BMSM",
                                "offer_message": [
                                  "Beli 10, <b>diskon 20%</b> di [Test] Square BMSM!",
                                  "Tambah 4 lagi diskon 20% di [Test] Square BMSM"
                                ],
                                "offer_json_data": "",
                                "offer_status": 1,
                                "tier_product": [
                                  {
                                    "tier_id": 0,
                                    "tier_message": "Tambah 4 lagi diskon 20% di [Test] Square BMSM",
                                    "tier_discount_text": "",
                                    "tier_discount_amount": 0,
                                    "price_before_benefit": 250000,
                                    "price_after_benefit": 250000,
                                    "list_product": [
                                      {
                                        "product_id": 2150835655,
                                        "warehouse_id": 345977,
                                        "quantity": 1,
                                        "price_before_benefit": 50000,
                                        "price_after_benefit": 50000,
                                        "cart_id": "2166690162"
                                      }
                                    ]
                                  }
                                ]
                              }
                            },
                            "products": [
                              {
                                "cart_id": 2166690162,
                                "product_id": 2150835655,
                                "product_quantity": 1,
                                "product_name": "Jarum_Suntik",
                                "product_image": {
                                  "image_src_100_square": "https://images.tokopedia.net/img/cache/100-square/hDjmkQ/2022/11/16/5b32ada4-9b17-48b7-a557-97b4de8fe398.jpg"
                                },
                                "warehouse_id": 345977,
                                "wholesale_price": [
                                  {
                                    "qty_min": 2,
                                    "qty_max": 10000,
                                    "prd_prc": 35000
                                  }
                                ]
                              }
                            ]
                          }
                        ]
                      }
                    ]
                  }
                }
              }
            }
        """.trimIndent()
        return GsonSingleton.instance.fromJson(mockResponse, MiniCartGqlResponse::class.java)
    }

    protected fun getMockMiniCartResponseWithNoBmgmWithOfferId(): MiniCartGqlResponse {
//        "cart_detail_type": ""
//        "offer_id": 910
        val mockResponse = """
            {
              "mini_cart_v3": {
                "error_message": [],
                "status": "OK",
                "data": {
                  "shopping_summary": {
                    "total_original_value": 250000,
                    "total_value": 250000
                  },
                  "available_section": {
                    "available_group": [
                      {
                        "cart_details": [
                          {
                            "cart_detail_info": {
                              "cart_detail_type": "",
                              "bmgm": {
                                "offer_id": 910,
                                "offer_type_id": 1,
                                "offer_name": "[Test] Square BMSM",
                                "offer_message": [
                                  "Beli 10, <b>diskon 20%</b> di [Test] Square BMSM!",
                                  "Tambah 4 lagi diskon 20% di [Test] Square BMSM"
                                ],
                                "offer_json_data": "",
                                "offer_status": 1,
                                "tier_product": [
                                  {
                                    "tier_id": 0,
                                    "tier_message": "Tambah 4 lagi diskon 20% di [Test] Square BMSM",
                                    "tier_discount_text": "",
                                    "tier_discount_amount": 0,
                                    "price_before_benefit": 250000,
                                    "price_after_benefit": 250000,
                                    "list_product": [
                                      {
                                        "product_id": 2150835655,
                                        "warehouse_id": 345977,
                                        "quantity": 1,
                                        "price_before_benefit": 50000,
                                        "price_after_benefit": 50000,
                                        "cart_id": "2166690162"
                                      }
                                    ]
                                  }
                                ]
                              }
                            },
                            "products": [
                              {
                                "cart_id": 2166690162,
                                "product_id": 2150835655,
                                "product_quantity": 1,
                                "product_name": "Jarum_Suntik",
                                "product_image": {
                                  "image_src_100_square": "https://images.tokopedia.net/img/cache/100-square/hDjmkQ/2022/11/16/5b32ada4-9b17-48b7-a557-97b4de8fe398.jpg"
                                },
                                "warehouse_id": 345977,
                                "wholesale_price": [
                                  {
                                    "qty_min": 2,
                                    "qty_max": 10000,
                                    "prd_prc": 35000
                                  }
                                ]
                              }
                            ]
                          }
                        ]
                      }
                    ]
                  }
                }
              }
            }
        """.trimIndent()
        return GsonSingleton.instance.fromJson(mockResponse, MiniCartGqlResponse::class.java)
    }

    protected fun getMockMiniCartResponseWithBmgmAndZeroOfferId(): MiniCartGqlResponse {
//        "cart_detail_type": "bmgm"
//        "offer_id": 0
        val mockResponse = """
            {
              "mini_cart_v3": {
                "error_message": [],
                "status": "OK",
                "data": {
                  "shopping_summary": {
                    "total_original_value": 250000,
                    "total_value": 250000
                  },
                  "available_section": {
                    "available_group": [
                      {
                        "cart_details": [
                          {
                            "cart_detail_info": {
                              "cart_detail_type": "bmgm",
                              "bmgm": {
                                "offer_id": 0,
                                "offer_name": "[Test] Square BMSM",
                                "offer_message": [
                                  "Beli 10, <b>diskon 20%</b> di [Test] Square BMSM!",
                                  "Tambah 4 lagi diskon 20% di [Test] Square BMSM"
                                ],
                                "offer_json_data": "",
                                "offer_status": 1,
                                "tier_product": [
                                  {
                                    "tier_id": 0,
                                    "tier_message": "Tambah 4 lagi diskon 20% di [Test] Square BMSM",
                                    "tier_discount_text": "",
                                    "tier_discount_amount": 0,
                                    "price_before_benefit": 250000,
                                    "price_after_benefit": 250000,
                                    "list_product": [
                                      {
                                        "product_id": 2150835655,
                                        "warehouse_id": 345977,
                                        "quantity": 1,
                                        "price_before_benefit": 50000,
                                        "price_after_benefit": 50000,
                                        "cart_id": "2166690162"
                                      }
                                    ]
                                  }
                                ]
                              }
                            },
                            "products": [
                              {
                                "cart_id": 2166690162,
                                "product_id": 2150835655,
                                "product_quantity": 1,
                                "product_name": "Jarum_Suntik",
                                "product_image": {
                                  "image_src_100_square": "https://images.tokopedia.net/img/cache/100-square/hDjmkQ/2022/11/16/5b32ada4-9b17-48b7-a557-97b4de8fe398.jpg"
                                },
                                "warehouse_id": 345977,
                                "wholesale_price": [
                                  {
                                    "qty_min": 2,
                                    "qty_max": 10000,
                                    "prd_prc": 35000
                                  }
                                ]
                              }
                            ]
                          }
                        ]
                      }
                    ]
                  }
                }
              }
            }
        """.trimIndent()
        return GsonSingleton.instance.fromJson(mockResponse, MiniCartGqlResponse::class.java)
    }
}