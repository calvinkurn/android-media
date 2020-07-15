package com.tokopedia.instantloan.test.domain.subscriber

import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.instantloan.data.model.response.GqlLendingDataResponse
import com.tokopedia.instantloan.domain.subscriber.GetLendingDataSubscriber
import com.tokopedia.instantloan.view.contractor.InstantLoanLendingDataContractor
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test

class GetLendingDataSubscriberTest {

    lateinit var subscriber: GetLendingDataSubscriber
    val throwable: java.lang.Exception = mockk()
    var view: InstantLoanLendingDataContractor.View = mockk()
    var response: GraphqlResponse = mockk()
    private val gqlLendingDataResponse: GqlLendingDataResponse = mockk()


    @Before
    @Throws(Exception::class)
    fun setUp() {
        subscriber = GetLendingDataSubscriber(view)
    }

    @Test
    fun `test subscriber on next with view attached`() {

        every {
            view.isViewAttached()
        } returns true

        every {
            response.getData<GqlLendingDataResponse>(GqlLendingDataResponse::class.java)
        } returns gqlLendingDataResponse

        every {
            view.renderLendingData(gqlLendingDataResponse)
        } returns mockk()

        subscriber.onNext(response)

        verify(exactly = 1) {
            view.renderLendingData(gqlLendingDataResponse)
        }
    }

    @Test
    fun `test subscriber on next with view not attached`() {

        every {
            view.isViewAttached()
        } returns false

        subscriber.onNext(response)
        verify(exactly = 0) {
            view.renderLendingData(gqlLendingDataResponse)
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
