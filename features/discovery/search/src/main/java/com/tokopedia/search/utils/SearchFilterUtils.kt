package com.tokopedia.search.utils

import com.google.gson.Gson
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.filter.newdynamicfilter.helper.OptionHelper

internal val nonFilterParameterKeyList = setOf(
        SearchApiConst.Q,
        SearchApiConst.RF,
        SearchApiConst.ACTIVE_TAB,
        SearchApiConst.SOURCE,
        SearchApiConst.LANDING_PAGE,
        SearchApiConst.PREVIOUS_KEYWORD,
        SearchApiConst.ORIGIN_FILTER,
        SearchApiConst.SKIP_REWRITE,
        SearchApiConst.NAVSOURCE,
        SearchApiConst.SKIP_BROADMATCH,
        SearchApiConst.HINT,
        SearchApiConst.FIRST_INSTALL
)

internal fun getSortFilterCount(mapParameter: Map<String, Any>): Int {
    val filterParameter = mapParameter.minus(nonFilterParameterKeyList)

    var sortFilterCount = 0
    filterParameter.values.forEach {
        sortFilterCount += it.toString().split(OptionHelper.OPTION_SEPARATOR).size
    }

    if (mapParameter.hasMinAndMaxPriceFilter()) sortFilterCount -= 1
    if (mapParameter.isSortHasDefaultValue()) sortFilterCount -= 1

    return sortFilterCount
}

private fun Map<String, Any>.hasMinAndMaxPriceFilter(): Boolean {
    var hasMinPriceFilter = false
    var hasMaxPriceFilter = false

    for(entry in this) {
        if (entry.key == SearchApiConst.PMIN) hasMinPriceFilter = true
        if (entry.key == SearchApiConst.PMAX) hasMaxPriceFilter = true

        // Immediately return so it doesn't continue the loop
        if (hasMinPriceFilter && hasMaxPriceFilter) return true
    }

    return false
}

private fun Map<String, Any>.isSortHasDefaultValue(): Boolean {
    val sortValue = this[SearchApiConst.OB]

    return sortValue == SearchApiConst.DEFAULT_VALUE_OF_PARAMETER_SORT
}

internal fun getSortFilterParamsString(mapParameter: Map<String, Any>): String {
    val sortAndFilterParameter = mapParameter.minus(nonFilterParameterKeyList)

    return UrlParamUtils.generateUrlParamString(sortAndFilterParameter)
}

internal fun getFilterParams(mapParameter: Map<String, String>): Map<String, String> {
    return mapParameter.minus(nonFilterParameterKeyList).minus(SearchApiConst.OB)
}

internal fun createDefaultFilterProduct() = Gson().fromJson(createDefaultFilterProductJSON(), DynamicFilterModel::class.java)

private fun createDefaultFilterProductJSON() =
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
                        "icon": "https://ecs7.tokopedia.net/img/autocomplete/ic_pm.png",
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
                        "icon": "https://ecs7.tokopedia.net/img/autocomplete/ic_os.png",
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