package com.tokopedia.usercomponents.userconsent.common

import com.tokopedia.usercomponents.common.BaseUserComponentsInterceptor
import com.tokopedia.usercomponents.userconsent.common.UserConsentUiTestCons.TNC_MULTIPLE_SOME_ARE_OPTIONAL
import com.tokopedia.usercomponents.userconsent.common.UserConsentUiTestCons.TNC_POLICY_MULTIPLE_SOME_ARE_OPTIONAL
import com.tokopedia.usercomponents.userconsent.common.UserConsentUiTestCons.TNC_POLICY_SINGLE_MANDATORY
import com.tokopedia.usercomponents.userconsent.common.UserConsentUiTestCons.TNC_POLICY_SINGLE_OPTIONAL
import com.tokopedia.usercomponents.userconsent.common.UserConsentUiTestCons.TNC_SINGLE_MANDATORY
import com.tokopedia.usercomponents.userconsent.common.UserConsentUiTestCons.TNC_SINGLE_OPTIONAL
import com.tokopedia.usercomponents.userconsent.fakes.FakeGetCollectionResponse
import okhttp3.Interceptor
import okhttp3.Response

class UserConsentInterceptor : BaseUserComponentsInterceptor() {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder().build()
        val query = request.headers[HEADER_GQL_QUERY].orEmpty()
        val requestString = request.toString()

        return if(query.contains(Query.GET_CONSENT_COLLECTION)) {
            when {
                requestString.contains(TNC_SINGLE_OPTIONAL) -> {
                    mockResponse(request, FakeGetCollectionResponse.collectionTnCSingleOptional())
                }
                requestString.contains(TNC_SINGLE_MANDATORY) -> {
                    mockResponse(request, FakeGetCollectionResponse.collectionTnCSingleMandatory())
                }
                requestString.contains(TNC_POLICY_SINGLE_OPTIONAL) -> {
                    mockResponse(request, FakeGetCollectionResponse.collectionTnCPolicySingleOptional())
                }
                requestString.contains(TNC_POLICY_SINGLE_MANDATORY) -> {
                    mockResponse(request, FakeGetCollectionResponse.collectionTnCPolicySingleMandatory())
                }
                requestString.contains(TNC_MULTIPLE_SOME_ARE_OPTIONAL) -> {
                    mockResponse(request, FakeGetCollectionResponse.collectionTnCMultipleOptional())
                }
                requestString.contains(TNC_POLICY_MULTIPLE_SOME_ARE_OPTIONAL) -> {
                    mockResponse(request, FakeGetCollectionResponse.collectionTnCPolicyMultipleOptional())
                }
                else -> {
                    chain.proceed(request)
                }
            }
        }
        else {
            chain.proceed(request)
        }
    }

    companion object {
        object Query {
            const val GET_CONSENT_COLLECTION = "GetCollectionPoint"
        }
    }
}