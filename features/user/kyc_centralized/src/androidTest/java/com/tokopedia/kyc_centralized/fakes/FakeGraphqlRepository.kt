package com.tokopedia.kyc_centralized.fakes

import androidx.test.core.app.ApplicationProvider
import com.google.gson.Gson
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.kyc_centralized.data.model.KycProjectInfo
import com.tokopedia.kyc_centralized.data.model.KycUserProjectInfoPojo
import com.tokopedia.kyc_centralized.test.R
import com.tokopedia.test.application.graphql.GqlMockUtil
import com.tokopedia.test.application.graphql.GqlQueryParser
import com.tokopedia.test.application.util.InstrumentationMockHelper
import com.tokopedia.usercomponents.userconsent.common.ConsentCollectionResponse
import timber.log.Timber

class FakeGraphqlRepository : GraphqlRepository {

    var infoCount = 0
    override suspend fun response(
        requests: List<GraphqlRequest>,
        cacheStrategy: GraphqlCacheStrategy
    ): GraphqlResponse {
        val q = GqlQueryParser.parse(requests).first()
        Timber.d("Processing query from fakes: $q")
        return when (q) {
            "GetCollectionPoint" -> {
                GqlMockUtil.createSuccessResponse(
                    Gson().fromJson(
                        GET_COLLECTION_JSON,
                        ConsentCollectionResponse::class.java
                    )
                )
            }
            "kycProjectInfo" -> {
                val obj = when (infoCount) {
                    0 -> MockProvider.notRegisteredUser()
                    else -> MockProvider.pending()
                }
                infoCount += 1
                GqlMockUtil.createSuccessResponse(obj)
            }
            else -> throw IllegalArgumentException("")
        }
    }

    private object MockProvider {
        fun notRegisteredUser(): KycUserProjectInfoPojo {
            val content = InstrumentationMockHelper.getRawString(
                ApplicationProvider.getApplicationContext(),
                R.raw.get_project_info_not_verified
            )
            return Gson().fromJson(content, KycUserProjectInfoPojo::class.java)
        }

        fun pending(): KycUserProjectInfoPojo {
            return KycUserProjectInfoPojo().apply {
                kycProjectInfo = KycProjectInfo(
                    isSelfie = false,
                    status = 0,
                    reasonList = arrayListOf()
                )
            }
        }
    }
}

const val GET_COLLECTION_JSON = """
  {
      "GetCollectionPoint": {
        "success": true,
        "refId": "232c0819-f644-445e-a24c-65f43811a05e",
        "errorMessages": [],
        "collectionPoints": [
          {
            "id": "bf7c9ba1-a4a9-447e-bbee-974c905a95ac",
            "version": "1",
            "consentType": "CONDITIONALTRIGGER",
            "purposes": [
              {
                "id": "74a324c7-45ac-49f7-926e-d692e9a8a552",
                "label": "Centralized KYC ( User verification and validation)",
                "description": "A consent showing that users are willing to share KTP (photo and data) and their face photo to Tokopedia for validation and verification use, including sending the data to Tokopedia partners",
                "version": "1",
                "purposeType": "STANDARD",
                "attributes": {
                  "uiName": "Verifikasi data KTP dan foto wajah",
                  "uiDescription": "",
                  "alwaysMandatory": "mandatory",
                  "personalDataType": [
                    "Full Address",
                    "Full Name",
                    "Gender",
                    "Date of Birth",
                    "Marital Status",
                    "National ID Number/NIK (Numerical Identifier)",
                    "Nationality",
                    "Occupation",
                    "Religion",
                    "Place of Birth",
                    "National ID/KTP (Scanned/Photo/Content)",
                    "Selfie Photo"
                  ],
                  "dataElementType": ""
                }
              }
            ],
            "notices": [],
            "attributes": {
              "collectionPointPurposeRequirement": "mandatory",
              "collectionPointStatementOnlyFlag": "checklist",
              "policyNoticeType": "termconditionpolicy",
              "PolicyNoticeTnCPageID": "a-3235",
              "PolicyNoticePolicyPageID": "",
              "statementWording": {
                "template": "Saya menyetujui #tnc serta #privacy #purpose.",
                "attributes": [
                  {
                    "key": "#tnc",
                    "type": "hyperlink",
                    "text": "Syarat \u0026 Ketentuan",
                    "link": "https://www.tokopedia.com/consent?pages\u003dW3sicGFnZSI6ImEtMzIzNSIsInVpX25hbWUiOiJWZXJpZmlrYXNpIGRhdGEgS1RQIGRhbiBmb3RvIHdhamFoIn1d\u0026type\u003d2\u0026tab\u003dtnc"
                  },
                  {
                    "key": "#privacy",
                    "type": "hyperlink",
                    "text": "Kebijakan Privasi",
                    "link": "https://www.tokopedia.com/consent?pages\u003dW3sicGFnZSI6ImEtMzIzNSIsInVpX25hbWUiOiJWZXJpZmlrYXNpIGRhdGEgS1RQIGRhbiBmb3RvIHdhamFoIn1d\u0026type\u003d2\u0026tab\u003dprivacy"
                  },
                  {
                    "key": "#purpose",
                    "type": "plain",
                    "text": "Verifikasi data KTP dan foto wajah",
                    "link": ""
                  }
                ]
              }
            }
          }
        ]
      }
    
  }
"""
