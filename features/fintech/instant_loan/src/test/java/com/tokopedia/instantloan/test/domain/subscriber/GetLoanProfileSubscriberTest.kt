package com.tokopedia.instantloan.test.domain.subscriber

import com.tokopedia.common.network.data.model.RestResponse
import com.tokopedia.instantloan.data.model.response.ResponseUserProfileStatus
import com.tokopedia.instantloan.data.model.response.UserProfileLoanEntity
import com.tokopedia.instantloan.domain.subscriber.GetLoanProfileSubscriber
import com.tokopedia.instantloan.view.contractor.InstantLoanLendingDataContractor
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test
import java.lang.reflect.Type

class GetLoanProfileSubscriberTest {

    lateinit var subscriber: GetLoanProfileSubscriber
    var view: InstantLoanLendingDataContractor.View = mockk()
    var restResponse: RestResponse= mockk()
    var loanStatusResponse: ResponseUserProfileStatus = mockk()
    var userEntity: UserProfileLoanEntity = mockk()

    var typeRestResponseMap: Map<Type, RestResponse>? = mockk()
    @Before
    @Throws(Exception::class)
    fun setUp() {
        subscriber = GetLoanProfileSubscriber(view)
    }

    @Test
    fun `test subscriber on next with view attached`() {

        every {
            view.isViewAttached()
        } returns true

        every {
            typeRestResponseMap?.get(ResponseUserProfileStatus::class.java)
        } returns restResponse

        every {
            restResponse.getData<ResponseUserProfileStatus>()
        } returns loanStatusResponse

        every {
            loanStatusResponse.userProfileLoanEntity
        } returns userEntity

        every {
            userEntity.onGoingLoanId
        } returns 0

        every {
            view.setUserOnGoingLoanStatus(0)
        } returns mockk()

        subscriber.onNext(typeRestResponseMap)

        verify(exactly = 1) {
            view.setUserOnGoingLoanStatus(userEntity.onGoingLoanId)
        }
    }

    @Test
    fun `test subscriber on next with view not attached`() {

        every {
            view.isViewAttached()
        } returns false

        subscriber.onNext(typeRestResponseMap)
        verify(exactly = 0) {
            view.setUserOnGoingLoanStatus(0)
        }
    }
}
