package com.tokopedia.search.utils

import com.google.gson.Gson
import com.tokopedia.filter.common.data.DataValue

internal fun createDefaultQuickFilter() = Gson().fromJson(createDefaultQuickFilterJSON(), DataValue::class.java)

private fun createDefaultQuickFilterJSON() =
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
                    "icon": "https://ecs7.tokopedia.net/img/autocomplete/ic_os.png",
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
                    "icon": "https://ecs7.tokopedia.net/img/autocomplete/ic_pm.png",
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

internal fun createSearchShopDefaultQuickFilter() = Gson().fromJson(createSearchShopDefaultQuickFilterJSON(), DataValue::class.java)

private fun createSearchShopDefaultQuickFilterJSON() =
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
                "icon": "https://ecs7.tokopedia.net/img/autocomplete/ic_os.png",
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
                "icon": "https://ecs7.tokopedia.net/img/autocomplete/ic_pm.png",
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