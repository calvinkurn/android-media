package com.tokopedia.home_account.consentwithdrawal.fakes.data

import com.google.gson.Gson
import com.tokopedia.home_account.consentWithdrawal.data.GetConsentPurposeDataModel

object FakeGetPurposeListByGroupResponse {

    fun getResponse(): GetConsentPurposeDataModel {
        return Gson().fromJson(RAW_RESPONSE, GetConsentPurposeDataModel::class.java)
    }

    fun getResponseWithOptOutList(): GetConsentPurposeDataModel {
        return getResponse().apply {
            consentGroup.consents.optional.first().apply {
                consentStatus = "OPT_OUT"
            }
        }
    }

    private const val RAW_RESPONSE = """
        {
            "GetPurposesByGroup": {
                "isSuccess": true,
                "refId": "914ad8cd-c3ea-48ac-b716-c19b6440d898",
                "errorMessages": [],
                "groupId": 1,
                "consents": {
                    "mandatory": [
                        {
                            "consentTitle": "test",
                            "consentSubtitle": "test",
                            "consentStatus": "OPT_IN",
                            "purposeId": "fa0cddf1-e480-43bf-9921-9a948fe99750",
                            "optInUrl": "test",
                            "optOutUrl": "test",
                            "optIntAppLink": "test",
                            "optOutAppLink": "test",
                            "priority": 1
                        }
                    ],
                    "optional": [
                        {
                            "consentTitle": "test",
                            "consentSubtitle": "test",
                            "consentStatus": "OPT_IN",
                            "purposeId": "9c9ad309-aad4-4ed0-9da6-508f5044c088",
                            "optInUrl": "test",
                            "optOutUrl": "test",
                            "optIntAppLink": "test",
                            "optOutAppLink": "test",
                            "priority": 2
                        }
                    ]
                }
            }
        }
    """
}
