package com.tokopedia.instantloan.test.domain.subscriber

import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.instantloan.data.model.response.GqlFilterDataResponse
import com.tokopedia.instantloan.domain.subscriber.GetFilterDataSubscriber
import com.tokopedia.instantloan.view.contractor.OnlineLoanContractor
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test

class GetFilterDataSubscriberTest {

    var onlineLoanView: OnlineLoanContractor.View = mockk()
    var response: GraphqlResponse = mockk()
    val gqlFilterDataResponse: GqlFilterDataResponse = mockk()
    lateinit var subscriber: GetFilterDataSubscriber
    val throwable: java.lang.Exception = mockk()

    @Before
    @Throws(Exception::class)
    fun setUp() {
        subscriber = GetFilterDataSubscriber(onlineLoanView)
    }

    @Test
    fun `test subscriber on next with view attached`() {

        every {
            onlineLoanView.isViewAttached()
        } returns true

        every {
            response.getData<GqlFilterDataResponse>(GqlFilterDataResponse::class.java)
        } returns gqlFilterDataResponse

        every {
            gqlFilterDataResponse.gqlFilterData
        } returns mockk()

        every {
            onlineLoanView.setFilterDataForOnlineLoan(gqlFilterDataResponse.gqlFilterData)
        } returns mockk()

        subscriber.onNext(response)

        verify(exactly = 1) {
            onlineLoanView.
                    setFilterDataForOnlineLoan(gqlFilterDataResponse.gqlFilterData)
        }
    }

    @Test
    fun `test subscriber on next with view not attached`() {

        every {
            onlineLoanView.isViewAttached()
        } returns false

        subscriber.onNext(response)
        verify(exactly = 0) {
            onlineLoanView.
                    setFilterDataForOnlineLoan(gqlFilterDataResponse.gqlFilterData)
        }
    }

    @Test
    fun `test subscriber on error`() {

        every {
            throwable.printStackTrace()
        } returns mockk()

        subscriber.onError(throwable)

        verify(exactly = 1) {
            throwable.printStackTrace()
        }
    }

}
