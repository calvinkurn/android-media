package com.tokopedia.home_account.consentwithdrawal.fakes.data

import com.google.gson.Gson
import com.tokopedia.home_account.consentWithdrawal.data.GetConsentGroupListDataModel

object FakeGetConsentGroupListResponse {

    fun getResponse(): GetConsentGroupListDataModel {
        return Gson().fromJson(RAW_RESPONSE, GetConsentGroupListDataModel::class.java)
    }

    private const val RAW_RESPONSE = """
        {
            "GetConsentGroupList": {
                "success": true,
                "refId": "d3e7bad4-bd70-4ae4-b0a3-6977ad9da4fb",
                "errorMessages": [],
                "groups": [
                    {
                        "id": 9,
                        "groupTitle": "test12",
                        "groupSubtitle": "ini test 4",
                        "groupImg": "www.tokopedia.com/imgs",
                        "priority": 1
                    },
                    {
                        "id": 7,
                        "groupTitle": "test2",
                        "groupSubtitle": "test2",
                        "groupImg": "test2",
                        "priority": 2
                    },
                    {
                        "id": 1,
                        "groupTitle": "title",
                        "groupSubtitle": "test18",
                        "groupImg": "test18",
                        "priority": 2
                    },
                    {
                        "id": 10,
                        "groupTitle": "title2",
                        "groupSubtitle": "ini test 4",
                        "groupImg": "www.tokopedia.com/imgs",
                        "priority": 2
                    },
                    {
                        "id": 8,
                        "groupTitle": "test3",
                        "groupSubtitle": "test",
                        "groupImg": "test",
                        "priority": 3
                    },
                    {
                        "id": 6,
                        "groupTitle": "test1",
                        "groupSubtitle": "test1",
                        "groupImg": "test1",
                        "priority": 4
                    }
                ]
            }
        }
    """
}
