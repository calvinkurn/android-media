package com.tokopedia.user_identification_common

import android.content.Context
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.user_identification_common.subscriber.GetApprovalStatusSubscriber
import junit.framework.Assert
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith

/**
 * @author by nisie on 19/11/18.
 */
@RunWith(JUnitPlatform::class)
class GetApprovalStatusSubscriberTest : Spek({
    given("on getting response from server"){
        val context: Context = mock()
        val listener : GetApprovalStatusSubscriber.GetApprovalStatusListener = mock()
        val subscriber = GetApprovalStatusSubscriber(context, listener)

        on("onError from server 500"){
            subscriber.onError(RuntimeException("500"))

            it("should show error"){
                verify(listener).onErrorGetShopVerificationStatus("Terjadi kesalahan pada server. Ulangi beberapa saat lagi")
            }

        }

        on("onSuccess"){
            val graphqlResponse : GraphqlResponse = mock()
            subscriber.onNext(graphqlResponse)

            it("should check for null listener"){
                Assert.assertNotNull(listener)
            }

        }
    }
})