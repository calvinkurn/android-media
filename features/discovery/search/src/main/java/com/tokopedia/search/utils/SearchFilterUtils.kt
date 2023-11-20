package com.tokopedia.search.utils

import android.content.Context
import android.os.Build
import android.view.View
import com.google.gson.Gson
import com.tokopedia.discovery.common.constants.SearchApiConst.Companion.DEFAULT_VALUE_OF_ORIGIN_FILTER_FROM_FILTER_PAGE
import com.tokopedia.discovery.common.constants.SearchApiConst.Companion.MANUAL_FILTER
import com.tokopedia.discovery.common.constants.SearchApiConst.Companion.ORIGIN_FILTER
import com.tokopedia.discovery.common.constants.SearchApiConst.Companion.SRP_COMPONENT_ID
import com.tokopedia.filter.common.data.DataValue
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.filter.common.data.Option
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.sortfilter.SortFilter

internal fun removeQuickFilterElevation(sortFilter: View?) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && sortFilter != null) {
        if (sortFilter.elevation > 0f) {
            sortFilter.elevation = 0f
        }
    }
}

internal fun applyQuickFilterElevation(context: Context?, sortFilter: View?) {
    if (context == null) return

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && sortFilter != null) {
        if (sortFilter.elevation == 0f) {
            val elevation = 5.dpToPx(context.resources.displayMetrics)
            sortFilter.elevation = elevation.toFloat()
        }
    }
}

internal fun manualFilterToggleMap(): Map<String, String> =
    mapOf(MANUAL_FILTER to true.toString())

internal fun originFilterMap(): Map<String, String> =
    mapOf(ORIGIN_FILTER to DEFAULT_VALUE_OF_ORIGIN_FILTER_FROM_FILTER_PAGE)

internal fun componentIdMap(componentId: String): Map<String, String> =
    mapOf(SRP_COMPONENT_ID to componentId)

fun List<Option>?.joinActiveOptionsToString(): String {
    if (this == null || this.isEmpty()) return ""

    return this.filter { it.inputState.toBoolean() }
        .groupBy { it.key }
        .map { "${it.key}=${it.value.joinToString(separator = ","){ option -> option.value} }" }
        .joinToString(separator = "&")
}

internal fun createSearchProductDefaultFilter() = Gson().fromJson(SEARCH_PRODUCT_DEFAULT_FILTER_JSON, DynamicFilterModel::class.java)
internal fun createSearchProductDefaultQuickFilter() = Gson().fromJson(SEARCH_PRODUCT_DEFAULT_QUICK_FILTER_JSON, DataValue::class.java)

internal fun createSearchShopDefaultQuickFilter() = Gson().fromJson(SEARCH_SHOP_DEFAULT_QUICK_FILTER_JSON, DataValue::class.java)

private const val SEARCH_PRODUCT_DEFAULT_FILTER_JSON =
        """
            {
              "data": {
                "filter": [
                  {
                    "title": "Jenis toko",
                    "search": {
                      "searchable": 0,
                      "placeholder": ""
                    },
                    "template_name": "",
                    "options": [
                      {
                        "name": "Power Merchant",
                        "key": "goldmerchant",
                        "icon": "https://images.tokopedia.net/img/autocomplete/ic_pm.png",
                        "Description": "",
                        "value": "true",
                        "inputType": "checkbox",
                        "totalData": "",
                        "valMax": "",
                        "valMin": "",
                        "isPopular": true,
                        "isNew": false,
                        "hexColor": "",
                        "child": []
                      },
                      {
                        "name": "Official Store",
                        "key": "official",
                        "icon": "https://images.tokopedia.net/img/autocomplete/ic_os.png",
                        "Description": "",
                        "value": "true",
                        "inputType": "checkbox",
                        "totalData": "",
                        "valMax": "",
                        "valMin": "",
                        "isPopular": true,
                        "isNew": false,
                        "hexColor": "",
                        "child": []
                      }
                    ]
                  },
                  {
                    "title": "Harga",
                    "search": {
                      "searchable": 0,
                      "placeholder": ""
                    },
                    "template_name": "template_price",
                    "options": [
                      {
                        "name": "Harga Minimum",
                        "key": "pmin",
                        "icon": "",
                        "Description": "",
                        "value": "0",
                        "inputType": "textbox",
                        "totalData": "",
                        "valMax": "",
                        "valMin": "",
                        "isPopular": false,
                        "isNew": false,
                        "hexColor": "",
                        "child": []
                      },
                      {
                        "name": "Harga Maksimum",
                        "key": "pmax",
                        "icon": "",
                        "Description": "",
                        "value": "",
                        "inputType": "textbox",
                        "totalData": "",
                        "valMax": "",
                        "valMin": "",
                        "isPopular": false,
                        "isNew": false,
                        "hexColor": "",
                        "child": []
                      },
                      {
                        "name": "",
                        "key": "pmin-pmax",
                        "icon": "",
                        "Description": "",
                        "value": "",
                        "inputType": "slider",
                        "totalData": "",
                        "valMax": "40000000",
                        "valMin": "100",
                        "isPopular": false,
                        "isNew": false,
                        "hexColor": "",
                        "child": []
                      }
                    ]
                  },
                  {
                    "title": "Kondisi",
                    "search": {
                      "searchable": 0,
                      "placeholder": ""
                    },
                    "template_name": "",
                    "options": [
                      {
                        "name": "Baru",
                        "key": "condition",
                        "icon": "",
                        "Description": "",
                        "value": "1",
                        "inputType": "checkbox",
                        "totalData": "",
                        "valMax": "",
                        "valMin": "",
                        "isPopular": true,
                        "isNew": false,
                        "hexColor": "",
                        "child": []
                      },
                      {
                        "name": "Bekas",
                        "key": "condition",
                        "icon": "",
                        "Description": "",
                        "value": "2",
                        "inputType": "checkbox",
                        "totalData": "",
                        "valMax": "",
                        "valMin": "",
                        "isPopular": true,
                        "isNew": false,
                        "hexColor": "",
                        "child": []
                      }
                    ]
                  },
                  {
                    "title": "Rating",
                    "search": {
                      "searchable": 0,
                      "placeholder": ""
                    },
                    "template_name": "template_rating",
                    "options": [
                      {
                        "name": "4 Keatas",
                        "key": "rt",
                        "icon": "",
                        "Description": "",
                        "value": "4,5",
                        "inputType": "checkbox",
                        "totalData": "",
                        "valMax": "",
                        "valMin": "",
                        "isPopular": true,
                        "isNew": false,
                        "hexColor": "",
                        "child": []
                      }
                    ]
                  },
                  {
                    "title": "Lainnya",
                    "search": {
                      "searchable": 0,
                      "placeholder": ""
                    },
                    "template_name": "",
                    "options": [
                      {
                        "name": "Pre Order",
                        "key": "preorder",
                        "icon": "",
                        "Description": "",
                        "value": "true",
                        "inputType": "checkbox",
                        "totalData": "",
                        "valMax": "",
                        "valMin": "",
                        "isPopular": true,
                        "isNew": false,
                        "hexColor": "",
                        "child": []
                      },
                      {
                        "name": "Ready Stock",
                        "key": "preorder",
                        "icon": "",
                        "Description": "",
                        "value": "false",
                        "inputType": "checkbox",
                        "totalData": "",
                        "valMax": "",
                        "valMin": "",
                        "isPopular": true,
                        "isNew": false,
                        "hexColor": "",
                        "child": []
                      }
                    ]
                  }
                ],
                "sort": [
                  {
                    "name": "Paling Sesuai",
                    "key": "ob",
                    "value": "23",
                    "inputType": "checkbox",
                    "applyFilter": ""
                  },
                  {
                    "name": "Ulasan",
                    "key": "ob",
                    "value": "5",
                    "inputType": "checkbox",
                    "applyFilter": ""
                  },
                  {
                    "name": "Terbaru",
                    "key": "ob",
                    "value": "9",
                    "inputType": "checkbox",
                    "applyFilter": ""
                  },
                  {
                    "name": "Harga Tertinggi",
                    "key": "ob",
                    "value": "4",
                    "inputType": "checkbox",
                    "applyFilter": ""
                  },
                  {
                    "name": "Harga Terendah",
                    "key": "ob",
                    "value": "3",
                    "inputType": "checkbox",
                    "applyFilter": ""
                  }
                ]
              }
            }
        """

private const val SEARCH_PRODUCT_DEFAULT_QUICK_FILTER_JSON =
        """
        {
            "filter": [
            {
                "title": "Official Store",
                "template_name": "",
                "search": {
                    "searchable": 1,
                    "placeholder": ""
                },
                "options": [
                {
                    "name": "Official Store",
                    "key": "official",
                    "icon": "https://images.tokopedia.net/img/autocomplete/ic_os.png",
                    "value": "true",
                    "inputType": "checkbox",
                    "totalData": "",
                    "valMax": "",
                    "valMin": "",
                    "hexColor": "",
                    "child": [],
                    "isPopular": true,
                    "isNew": false,
                    "Description": ""
                }
                ]
            },
            {
                "title": "Kurir Instan",
                "template_name": "",
                "search": {
                    "searchable": 1,
                    "placeholder": ""
                },
                "options": [
                {
                    "name": "Kurir Instan",
                    "key": "shipping",
                    "icon": "",
                    "value": "10,12,13",
                    "inputType": "checkbox",
                    "totalData": "",
                    "valMax": "",
                    "valMin": "",
                    "hexColor": "",
                    "child": [],
                    "isPopular": true,
                    "isNew": false,
                    "Description": ""
                }
                ]
            },
            {
                "title": "Power Merchant",
                "template_name": "",
                "search": {
                    "searchable": 1,
                    "placeholder": ""
                },
                "options": [
                {
                    "name": "Power Merchant",
                    "key": "goldmerchant",
                    "icon": "https://images.tokopedia.net/img/autocomplete/ic_pm.png",
                    "value": "true",
                    "inputType": "checkbox",
                    "totalData": "",
                    "valMax": "",
                    "valMin": "",
                    "hexColor": "",
                    "child": [],
                    "isPopular": true,
                    "isNew": false,
                    "Description": ""
                }
                ]
            }
            ],
            "sort": []
        }
        """

private const val SEARCH_SHOP_DEFAULT_QUICK_FILTER_JSON =
        """
    {
        "filter": [
        {
            "title": "Official Store",
            "template_name": "",
            "search": {
                "searchable": 1,
                "placeholder": ""
            },
            "options": [
            {
                "name": "Official Store",
                "key": "official",
                "icon": "https://images.tokopedia.net/img/autocomplete/ic_os.png",
                "value": "true",
                "inputType": "checkbox",
                "totalData": "",
                "valMax": "",
                "valMin": "",
                "hexColor": "",
                "child": [],
                "isPopular": true,
                "isNew": false,
                "Description": ""
            }
            ]
        },
        {
            "title": "Power Merchant",
            "template_name": "",
            "search": {
                "searchable": 1,
                "placeholder": ""
            },
            "options": [
            {
                "name": "Power Merchant",
                "key": "goldmerchant",
                "icon": "https://images.tokopedia.net/img/autocomplete/ic_pm.png",
                "value": "true",
                "inputType": "checkbox",
                "totalData": "",
                "valMax": "",
                "valMin": "",
                "hexColor": "",
                "child": [],
                "isPopular": true,
                "isNew": false,
                "Description": ""
            }
            ]
        }
        ],
        "sort": []
    }
    """
