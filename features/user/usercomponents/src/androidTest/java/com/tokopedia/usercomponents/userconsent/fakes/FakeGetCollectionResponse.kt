package com.tokopedia.usercomponents.userconsent.fakes

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.tokopedia.usercomponents.userconsent.common.UserConsentConst
import com.tokopedia.usercomponents.userconsent.domain.ConsentCollectionResponse

object FakeGetCollectionResponse {

    fun collectionTnCSingleMandatory(): String {
        val gson = Gson().fromJson(GET_COLLECTION_JSON, FakeGetCollectionResponseDataModel::class.java)
        with(gson.data.data.collectionPoints.first()) {
            attributes.policyNoticeType = UserConsentConst.TERM_CONDITION
            attributes.collectionPointPurposeRequirement = UserConsentConst.MANDATORY
            attributes.collectionPointStatementOnlyFlag = UserConsentConst.CHECKLIST
        }
        return "[${Gson().toJson(gson)}]"
    }

    fun collectionTnCSingleOptional(): String {
        val gson = Gson().fromJson(GET_COLLECTION_JSON, FakeGetCollectionResponseDataModel::class.java)
        with(gson.data.data.collectionPoints.first()) {
            attributes.policyNoticeType = UserConsentConst.TERM_CONDITION
            attributes.collectionPointPurposeRequirement = UserConsentConst.MANDATORY
            attributes.collectionPointStatementOnlyFlag = UserConsentConst.NO_CHECKLIST
        }
        return "[${Gson().toJson(gson)}]"
    }

    fun collectionTnCPolicySingleMandatory(): String {
        val gson = Gson().fromJson(GET_COLLECTION_JSON, FakeGetCollectionResponseDataModel::class.java)
        with(gson.data.data.collectionPoints.first()) {
            attributes.policyNoticeType = UserConsentConst.TERM_CONDITION_POLICY
            attributes.collectionPointPurposeRequirement = UserConsentConst.MANDATORY
            attributes.collectionPointStatementOnlyFlag = UserConsentConst.CHECKLIST
        }
        return "[${Gson().toJson(gson)}]"
    }

    fun collectionTnCPolicySingleOptional(): String {
        val gson = Gson().fromJson(GET_COLLECTION_JSON, FakeGetCollectionResponseDataModel::class.java)
        with(gson.data.data.collectionPoints.first()) {
            attributes.policyNoticeType = UserConsentConst.TERM_CONDITION_POLICY
            attributes.collectionPointPurposeRequirement = UserConsentConst.MANDATORY
            attributes.collectionPointStatementOnlyFlag = UserConsentConst.NO_CHECKLIST
        }
        return "[${Gson().toJson(gson)}]"
    }

    fun collectionTnCMultipleOptional(): String {
        val gson = Gson().fromJson(GET_COLLECTION_JSON, FakeGetCollectionResponseDataModel::class.java)
        with(gson.data.data.collectionPoints.first()) {
            attributes.policyNoticeType = UserConsentConst.TERM_CONDITION
            attributes.collectionPointPurposeRequirement = UserConsentConst.OPTIONAL
            attributes.collectionPointStatementOnlyFlag = UserConsentConst.CHECKLIST
        }
        return "[${Gson().toJson(gson)}]"
    }

    fun collectionTnCPolicyMultipleOptional(): String {
        val gson = Gson().fromJson(GET_COLLECTION_JSON, FakeGetCollectionResponseDataModel::class.java)
        with(gson.data.data.collectionPoints.first()) {
            attributes.policyNoticeType = UserConsentConst.TERM_CONDITION_POLICY
            attributes.collectionPointPurposeRequirement = UserConsentConst.OPTIONAL
            attributes.collectionPointStatementOnlyFlag = UserConsentConst.CHECKLIST
        }
        return "[${Gson().toJson(gson)}]"
    }
}

data class FakeGetCollectionResponseDataModel(
    @SerializedName("data")
    var data: ConsentCollectionResponse = ConsentCollectionResponse()
)

const val GET_COLLECTION_JSON = """
    {
        "data": {
          "GetCollectionPoint": {
            "success": true,
            "refId": "85d69566-41f8-4cc9-8dee-ca0cbbc60caf",
            "errorMessages": [],
            "collectionPoints": [
              {
                "id": "6d45e8ce-d46d-4f0e-bf0c-a93f82f75e36",
                "version": "1",
                "consentType": "CONDITIONALTRIGGER",
                "purposes": [
                  {
                    "id": "c47c1cf4-fdb1-4c6a-a2c2-c0dab23e9255",
                    "label": "fintech_lending_kyc",
                    "description": "fintech_lending_kyc",
                    "version": "1",
                    "purposeType": "STANDARD",
                    "attributes": {
                      "uiName": "fintech_lending_kyc",
                      "uiDescription": "",
                      "alwaysMandatory": "optional",
                      "personalDataType": [
                        "Email Address"
                      ]
                    }
                  },
                  {
                    "id": "04e42343-e7cd-485d-85cf-4494765fd5e1",
                    "label": "fintech_lending_sendto3rdparty",
                    "description": "fintech_lending_sendto3rdparty",
                    "version": "1",
                    "purposeType": "STANDARD",
                    "attributes": {
                      "uiName": "fintech_lending_sendto3rdparty",
                      "uiDescription": "",
                      "alwaysMandatory": "mandatory",
                      "personalDataType": [
                        "Email Address"
                      ]
                    }
                  },
                  {
                    "id": "46294a4e-fecf-4ad1-a0bb-3f76295b01af",
                    "label": "fintech_lending_registration",
                    "description": "fintech_registration",
                    "version": "2",
                    "purposeType": "STANDARD",
                    "attributes": {
                      "uiName": "fintech_lending_registration",
                      "uiDescription": "",
                      "alwaysMandatory": "optional",
                      "personalDataType": [
                        "Full Address"
                      ]
                    }
                  }
                ],
                "notices": [],
                "attributes": {
                  "collectionPointPurposeRequirement": "optional",
                  "collectionPointStatementOnlyFlag": "checklist",
                  "policyNoticeType": "termconditionpolicy",
                  "PolicyNoticeTnCPageID": "a-0209",
                  "PolicyNoticePolicyPageID": "a-0209"
                }
              }
            ]
          }
        }
    }
"""