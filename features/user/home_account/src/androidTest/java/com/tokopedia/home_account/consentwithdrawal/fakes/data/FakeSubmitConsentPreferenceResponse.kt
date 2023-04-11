package com.tokopedia.home_account.consentwithdrawal.fakes.data

import com.google.gson.Gson
import com.tokopedia.home_account.consentWithdrawal.data.SubmitConsentPreferenceDataModel

object FakeSubmitConsentPreferenceResponse {

    fun getResponse(): SubmitConsentPreferenceDataModel {
        return Gson().fromJson(RAW_RESPONSE, SubmitConsentPreferenceDataModel::class.java)
    }

    private const val RAW_RESPONSE = """
        {
		    "SubmitConsentPreference": {
                "refId": "bfa14493-5a98-4f3f-a4bc-c2825b8e8092",
                "isSuccess": true,
                "errorMessages": [],
                "receipts": [
                    {
                        "receiptId": 409,
                        "identifier": "9088919",
                        "identifierType": "User ID Tokopedia",
                        "purposeId": "64e6a777-2458-47b2-b1b0-5fd835f49b1d",
                        "transactionType": "OPT_IN",
                        "version": ""
                    },
                    {
                        "receiptId": 410,
                        "identifier": "9088919",
                        "identifierType": "User ID Tokopedia",
                        "purposeId": "8f629680-a492-490a-a7ec-533a6e0eae47",
                        "transactionType": "OPT_IN",
                        "version": "1"
                    }
			    ]
		    }
        }
    """
}
