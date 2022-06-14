package com.tokopedia.power_merchant.subscribe.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.power_merchant.subscribe.domain.usecase.GetMembershipDetailUseCase
import com.tokopedia.power_merchant.subscribe.view.model.MembershipDetailUiModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.unit.test.ext.verifyErrorEquals
import com.tokopedia.unit.test.ext.verifySuccessEquals
import com.tokopedia.unit.test.rule.CoroutineTestRule
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created by @ilhamsuaib on 09/06/22.
 */

@ExperimentalCoroutinesApi
class MembershipDetailViewModelTest {

    @get:Rule
    val ruleForLivaData = InstantTaskExecutorRule()

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    @RelaxedMockK
    lateinit var getMembershipDetailUseCase: GetMembershipDetailUseCase

    @RelaxedMockK
    lateinit var userSession: UserSessionInterface

    private lateinit var viewModel: MembershipDetailViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        viewModel = MembershipDetailViewModel(
            { getMembershipDetailUseCase },
            { userSession },
            coroutineTestRule.dispatchers
        )
    }

    @Test
    fun `when get membership data should return success result`() {
        coroutineTestRule.runBlockingTest {
            val result = MembershipDetailUiModel()

            coEvery {
                getMembershipDetailUseCase.executeOnBackground()
            } returns result

            viewModel.getMembershipBasicInfo()

            coVerify {
                getMembershipDetailUseCase.executeOnBackground()
            }

            val expected = Success(result)
            viewModel.membershipBasicInfo.verifySuccessEquals(expected)
        }
    }

    @Test
    fun `when get membership data should return error result`() {
        coroutineTestRule.runBlockingTest {
            val exception = Throwable()

            coEvery {
                getMembershipDetailUseCase.executeOnBackground()
            } throws exception

            viewModel.getMembershipBasicInfo()

            coVerify {
                getMembershipDetailUseCase.executeOnBackground()
            }

            val expected = Fail(exception)
            viewModel.membershipBasicInfo.verifyErrorEquals(expected)
        }
    }
}