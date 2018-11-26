package com.tokopedia.user_identification_common

import android.content.Context
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.graphql.CommonUtils
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.user_identification_common.pojo.GetApprovalStatusPojo
import com.tokopedia.user_identification_common.pojo.KycStatusDetailPojo
import com.tokopedia.user_identification_common.pojo.KycStatusPojo
import com.tokopedia.user_identification_common.subscriber.GetApprovalStatusSubscriber
import junit.framework.Assert
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.json.JSONObject
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith
import org.mockito.Mockito.`when`
import java.io.File
import java.lang.reflect.Type

/**
 * @author by nisie on 19/11/18.
 */
@RunWith(JUnitPlatform::class)
class GetApprovalStatusSubscriberTest : Spek({
    val DEFAULT_REQUEST_ERROR_UNKNOWN: String = "Terjadi kesalahan. Ulangi beberapa saat lagi"

    given("on getting response from server") {
        val context: Context = mock()
        val listener: GetApprovalStatusSubscriber.GetApprovalStatusListener = mock()
        val subscriber = GetApprovalStatusSubscriber(context, listener)


        `when`(ErrorHandler.getErrorMessage(context, RuntimeException("500")))
                .thenReturn(DEFAULT_REQUEST_ERROR_UNKNOWN)

        on("onError from server") {
            subscriber.onError(RuntimeException("500"))

            it("should check listener not null") {
                Assert.assertNotNull(listener)
            }

            it("should show error") {
                verify(listener).onErrorGetShopVerificationStatus(DEFAULT_REQUEST_ERROR_UNKNOWN)
            }

        }

        on("onSuccess with unverified json") {
            val graphqlResponse: GraphqlResponse = createMockGraphqlResponse("json/get_approval_status_success_unverified.json",GetApprovalStatusPojo::class.java )

            subscriber.onNext(graphqlResponse)

            it("should check listener not null") {
                Assert.assertNotNull(listener)
            }

            it("should display status when pojo is success") {
                verify(listener).onSuccessGetShopVerificationStatus(3)

            }

        }
    }
})

/**
 * @param path filepath of json response
 * @param type Pojo class
 * Example path : "json/{file_name}.json"
 * @return mock graphqlresponse.
 */
fun createMockGraphqlResponse(path: String, type: Type): GraphqlResponse {
    val results = HashMap<Type, Any>()
    val errors = HashMap<Type, List<GraphqlError>>()

    val responseObject = JSONObject(getJson(path).replace("\n","").trim())

//    Assert.assertNotNull(responseObject)
//    Assert.assertNotNull(responseObject.getJSONObject("data"))

//    results[type] = CommonUtils.fromJson<Any>(responseObject.get("data").toString(), type)

    return GraphqlResponse(results, errors, false)
}

/**
 * @param path filepath of json response
 * @return pojoString : pojoString from file at given path
 */
fun getJson(path: String): String {
    // Load the JSON response
    val uri = ClassLoader.getSystemClassLoader().getResource(path)
    val file = File(uri.path)
    return String(file.readBytes())
}
