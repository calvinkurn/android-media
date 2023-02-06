package com.tokopedia.usercomponents.userconsent.fakes

import com.google.gson.Gson
import com.tokopedia.usercomponents.userconsent.common.UserConsentConst
import com.tokopedia.usercomponents.userconsent.domain.collection.ConsentCollectionResponse

object FakeGetCollectionResponse {

    private fun getDefaultResponse(): ConsentCollectionResponse {
        return Gson().fromJson(GET_COLLECTION_JSON, ConsentCollectionResponse::class.java)
    }

    fun collectionTnCSingleMandatory(): ConsentCollectionResponse {
        val gson = getDefaultResponse()
        with(gson.data.collectionPoints.first()) {
            attributes.policyNoticeType = UserConsentConst.TERM_CONDITION
            attributes.collectionPointPurposeRequirement = UserConsentConst.MANDATORY
            attributes.collectionPointStatementOnlyFlag = UserConsentConst.CHECKLIST
        }
        return gson
    }

    fun collectionTnCSingleMandatoryNotNeedConsent(): ConsentCollectionResponse {
        val gson = getDefaultResponse()
        with(gson.data.collectionPoints.first()) {
            needConsent = false
            attributes.policyNoticeType = UserConsentConst.TERM_CONDITION
            attributes.collectionPointPurposeRequirement = UserConsentConst.MANDATORY
            attributes.collectionPointStatementOnlyFlag = UserConsentConst.CHECKLIST
        }
        return gson
    }

    fun collectionTnCSingleOptional(): ConsentCollectionResponse {
        val gson = getDefaultResponse()
        with(gson.data.collectionPoints.first()) {
            attributes.policyNoticeType = UserConsentConst.TERM_CONDITION
            attributes.collectionPointPurposeRequirement = UserConsentConst.MANDATORY
            attributes.collectionPointStatementOnlyFlag = UserConsentConst.NO_CHECKLIST
        }
        return gson
    }

    fun collectionTnCPolicySingleMandatory(): ConsentCollectionResponse {
        val gson = getDefaultResponse()
        with(gson.data.collectionPoints.first()) {
            attributes.policyNoticeType = UserConsentConst.TERM_CONDITION_POLICY
            attributes.collectionPointPurposeRequirement = UserConsentConst.MANDATORY
            attributes.collectionPointStatementOnlyFlag = UserConsentConst.CHECKLIST
        }
        return gson
    }

    fun collectionTnCPolicySingleOptional(): ConsentCollectionResponse {
        val gson = getDefaultResponse()
        with(gson.data.collectionPoints.first()) {
            attributes.policyNoticeType = UserConsentConst.TERM_CONDITION_POLICY
            attributes.collectionPointPurposeRequirement = UserConsentConst.MANDATORY
            attributes.collectionPointStatementOnlyFlag = UserConsentConst.NO_CHECKLIST
        }
        return gson
    }

    fun collectionTnCMultipleOptional(): ConsentCollectionResponse {
        val gson = getDefaultResponse()
        with(gson.data.collectionPoints.first()) {
            attributes.policyNoticeType = UserConsentConst.TERM_CONDITION
            attributes.collectionPointPurposeRequirement = UserConsentConst.OPTIONAL
            attributes.collectionPointStatementOnlyFlag = UserConsentConst.CHECKLIST
        }
        return gson
    }

    fun collectionTnCPolicyMultipleOptional(): ConsentCollectionResponse {
        val gson = getDefaultResponse()
        with(gson.data.collectionPoints.first()) {
            attributes.policyNoticeType = UserConsentConst.TERM_CONDITION_POLICY
            attributes.collectionPointPurposeRequirement = UserConsentConst.OPTIONAL
            attributes.collectionPointStatementOnlyFlag = UserConsentConst.CHECKLIST
        }
        return gson
    }
}

const val GET_COLLECTION_JSON = """
    {
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
"""
