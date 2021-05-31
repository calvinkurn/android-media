package com.tokopedia.loginphone.chooseaccount.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.loginphone.chooseaccount.data.AccountList
import com.tokopedia.loginphone.chooseaccount.data.AccountListPojo
import com.tokopedia.loginphone.chooseaccount.di.ChooseAccountQueryConstant
import com.tokopedia.loginphone.chooseaccount.domain.subscriber.LoginFacebookSubscriber
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.sessioncommon.data.LoginToken
import com.tokopedia.sessioncommon.data.LoginTokenPojo
import com.tokopedia.sessioncommon.data.profile.ProfileInfo
import com.tokopedia.sessioncommon.data.profile.ProfilePojo
import com.tokopedia.sessioncommon.domain.subscriber.GetProfileSubscriber
import com.tokopedia.sessioncommon.domain.subscriber.LoginTokenSubscriber
import com.tokopedia.sessioncommon.domain.usecase.GetAdminTypeUseCase
import com.tokopedia.sessioncommon.domain.usecase.GetProfileUseCase
import com.tokopedia.sessioncommon.domain.usecase.LoginTokenUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.MockKAnnotations
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
class ChooseAccountViewModelTest {

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @RelaxedMockK
    lateinit var getAccountsListUseCase: GraphqlUseCase<AccountListPojo>
    @RelaxedMockK
    lateinit var loginTokenUseCase: LoginTokenUseCase
    @RelaxedMockK
    lateinit var getProfileUseCase: GetProfileUseCase
    @RelaxedMockK
    lateinit var getAdminTypeUseCase: GetAdminTypeUseCase
    @RelaxedMockK
    lateinit var userSession: UserSessionInterface
    @RelaxedMockK
    lateinit var getAccountListFBResponseObserver: Observer<Result<AccountList>>
    @RelaxedMockK
    lateinit var getAccountListPhoneResponseObserver: Observer<Result<AccountList>>
    @RelaxedMockK
    lateinit var loginPhoneNumberResponseObserver: Observer<Result<LoginToken>>
    @RelaxedMockK
    lateinit var getUserInfoResponseObserver: Observer<Result<ProfileInfo>>
    @RelaxedMockK
    lateinit var goToActivationPageObserver: Observer<MessageErrorException>
    @RelaxedMockK
    lateinit var goToSecurityQuestionObserver: Observer<String>
    @RelaxedMockK
    lateinit var showLocationAdminPopUpObserver: Observer<Result<Boolean>>

    private lateinit var viewmodel: ChooseAccountViewModel

    private val testDispatcher = TestCoroutineDispatcher()

    private val rawQueries = mapOf(
            ChooseAccountQueryConstant.QUERY_GET_ACCOUNT_LIST to ChooseAccountQueryConstant.QUERY_GET_ACCOUNT_LIST
    )

    @Before
    fun before() {
        MockKAnnotations.init(this)
        viewmodel = ChooseAccountViewModel(
                getAccountsListUseCase,
                userSession,
                loginTokenUseCase,
                getProfileUseCase,
                getAdminTypeUseCase,
                rawQueries,
                testDispatcher
        )
    }

    @Test
    fun `Success login token phone`() {
        viewmodel.loginPhoneNumberResponse.observeForever(loginPhoneNumberResponseObserver)

        coEvery { loginTokenUseCase.executeLoginPhoneNumber(any(), any()) } coAnswers {
            secondArg<LoginTokenSubscriber>().onSuccessLoginToken.invoke(successLoginTokenPhoneResponse)
        }

        viewmodel.loginTokenPhone("", "", "")

        verify { loginPhoneNumberResponseObserver.onChanged(any<Success<LoginToken>>()) }
        assert(viewmodel.loginPhoneNumberResponse.value is Success)

        val result = viewmodel.loginPhoneNumberResponse.value as Success<LoginToken>
        assert(result.data == successLoginTokenPhoneResponse.loginToken)
    }

    @Test
    fun `Success login token phone with sq check`() {
        viewmodel.goToSecurityQuestion.observeForever(goToSecurityQuestionObserver)

        coEvery { loginTokenUseCase.executeLoginPhoneNumber(any(), any()) } coAnswers {
            secondArg<LoginTokenSubscriber>().onGoToSecurityQuestion.invoke()
        }

        viewmodel.loginTokenPhone("", "", "123")

        verify { goToSecurityQuestionObserver.onChanged(any()) }
        assert(viewmodel.goToSecurityQuestion.value is String)

        val result = viewmodel.goToSecurityQuestion.value as String
        assert(result == "123")
    }

    @Test
    fun `Failed login token phone`() {
        viewmodel.loginPhoneNumberResponse.observeForever(loginPhoneNumberResponseObserver)
        viewmodel.goToActivationPage.observeForever(goToActivationPageObserver)
        viewmodel.goToSecurityQuestion.observeForever(goToSecurityQuestionObserver)

        coEvery { loginTokenUseCase.executeLoginPhoneNumber(any(), any()) } coAnswers {
            secondArg<LoginTokenSubscriber>().onErrorLoginToken.invoke(throwable)
        }

        viewmodel.loginTokenPhone("", "", "")

        verify { loginPhoneNumberResponseObserver.onChanged(any<Fail>()) }
        assert(viewmodel.loginPhoneNumberResponse.value is Fail)

        val result = viewmodel.loginPhoneNumberResponse.value as Fail
        assertEquals(throwable, result.throwable)
    }

    @Test
    fun `Failed login token phone and go to activation page`() {
        viewmodel.goToActivationPage.observeForever(goToActivationPageObserver)

        coEvery { loginTokenUseCase.executeLoginPhoneNumber(any(), any()) } coAnswers {
            secondArg<LoginTokenSubscriber>().onGoToActivationPage.invoke(messageErrorException)
        }

        viewmodel.loginTokenPhone("", "", "")

        verify { goToActivationPageObserver.onChanged(any()) }
        assert(viewmodel.goToActivationPage.value is MessageErrorException)

        val result = viewmodel.goToActivationPage.value as MessageErrorException
        assertEquals(messageErrorException, result)
    }

    @Test
    fun `Success login token fb`() {
        viewmodel.loginPhoneNumberResponse.observeForever(loginPhoneNumberResponseObserver)

        coEvery { loginTokenUseCase.executeLoginSocialMediaPhone(any(), any()) } coAnswers {
            secondArg<LoginFacebookSubscriber>().onSuccessLoginToken.invoke(successLoginTokenPhoneResponse)
        }

        viewmodel.loginTokenFacebook("", "", "")

        verify { loginPhoneNumberResponseObserver.onChanged(any<Success<LoginToken>>()) }
        assert(viewmodel.loginPhoneNumberResponse.value is Success)

        val result = viewmodel.loginPhoneNumberResponse.value as Success<LoginToken>
        assert(result.data == successLoginTokenPhoneResponse.loginToken)
    }

    @Test
    fun `Success login token fb with sq check`() {
        viewmodel.goToSecurityQuestion.observeForever(goToSecurityQuestionObserver)

        coEvery { loginTokenUseCase.executeLoginSocialMediaPhone(any(), any()) } coAnswers {
            secondArg<LoginFacebookSubscriber>().onGoToSecurityQuestion.invoke()
        }

        viewmodel.loginTokenFacebook("", "", "123")

        verify { goToSecurityQuestionObserver.onChanged(any()) }
        assert(viewmodel.goToSecurityQuestion.value is String)

        val result = viewmodel.goToSecurityQuestion.value as String
        assert(result == "123")
    }

    @Test
    fun `Failed login token fb`() {
        viewmodel.loginPhoneNumberResponse.observeForever(loginPhoneNumberResponseObserver)
        viewmodel.goToActivationPage.observeForever(goToActivationPageObserver)
        viewmodel.goToSecurityQuestion.observeForever(goToSecurityQuestionObserver)

        coEvery { loginTokenUseCase.executeLoginSocialMediaPhone(any(), any()) } coAnswers {
            secondArg<LoginFacebookSubscriber>().onErrorLoginToken.invoke(throwable)
        }

        viewmodel.loginTokenFacebook("", "", "")

        verify { loginPhoneNumberResponseObserver.onChanged(any<Fail>()) }
        assert(viewmodel.loginPhoneNumberResponse.value is Fail)

        val result = viewmodel.loginPhoneNumberResponse.value as Fail
        assertEquals(throwable, result.throwable)
    }

    @Test
    fun `Success get account list phone`() {
        viewmodel.getAccountListPhoneResponse.observeForever(getAccountListPhoneResponseObserver)

        coEvery { getAccountsListUseCase.execute(any(), any()) } coAnswers {
            firstArg<(AccountListPojo) -> Unit>().invoke(successGetAccountsListResponse)
        }

        viewmodel.getAccountListPhoneNumber("", "")

        verify { getAccountListPhoneResponseObserver.onChanged(any<Success<AccountList>>()) }
        assert(viewmodel.getAccountListPhoneResponse.value is Success)

        val result = viewmodel.getAccountListPhoneResponse.value as Success<AccountList>
        assert(result.data == successGetAccountsListResponse.accountList)
    }

    @Test
    fun `Failed get account list phone`() {
        viewmodel.getAccountListPhoneResponse.observeForever(getAccountListPhoneResponseObserver)

        coEvery { getAccountsListUseCase.execute(any(), any()) } coAnswers {
            secondArg<(Throwable) -> Unit>().invoke(throwable)
        }

        viewmodel.getAccountListPhoneNumber("", "")

        verify { getAccountListPhoneResponseObserver.onChanged(any<Fail>()) }
        assert(viewmodel.getAccountListPhoneResponse.value is Fail)

        val result = viewmodel.getAccountListPhoneResponse.value as Fail
        assertEquals(throwable, result.throwable)
    }

    @Test
    fun `Success get account list fb`() {
        viewmodel.getAccountListFBResponse.observeForever(getAccountListFBResponseObserver)

        coEvery { getAccountsListUseCase.execute(any(), any()) } coAnswers {
            firstArg<(AccountListPojo) -> Unit>().invoke(successGetAccountsListResponse)
        }

        viewmodel.getAccountListFacebook("")

        verify { getAccountListFBResponseObserver.onChanged(any<Success<AccountList>>()) }
        assert(viewmodel.getAccountListFBResponse.value is Success)

        val result = viewmodel.getAccountListFBResponse.value as Success<AccountList>
        assert(result.data == successGetAccountsListResponse.accountList)
    }

    @Test
    fun `Failed get account list fb`() {
        viewmodel.getAccountListFBResponse.observeForever(getAccountListFBResponseObserver)

        coEvery { getAccountsListUseCase.execute(any(), any()) } coAnswers {
            secondArg<(Throwable) -> Unit>().invoke(throwable)
        }

        viewmodel.getAccountListFacebook("")

        verify { getAccountListFBResponseObserver.onChanged(any<Fail>()) }
        assert(viewmodel.getAccountListFBResponse.value is Fail)

        val result = viewmodel.getAccountListFBResponse.value as Fail
        assertEquals(throwable, result.throwable)
    }

    @Test
    fun `Success get user info`() {
        viewmodel.getUserInfoResponse.observeForever(getUserInfoResponseObserver)

        coEvery { getProfileUseCase.execute(any()) } coAnswers {
            firstArg<GetProfileSubscriber>().onSuccessGetProfile.invoke(successGetUserInfoResponse)
        }

        viewmodel.getUserInfo()

        verify { getUserInfoResponseObserver.onChanged(any<Success<ProfileInfo>>()) }
        assert(viewmodel.getUserInfoResponse.value is Success)

        val result = viewmodel.getUserInfoResponse.value as Success<ProfileInfo>
        assert(result.data == successGetUserInfoResponse.profileInfo)
    }

    @Test
    fun `Failed get user info`() {
        viewmodel.getUserInfoResponse.observeForever(getUserInfoResponseObserver)

        coEvery { getProfileUseCase.execute(any()) } coAnswers {
            firstArg<GetProfileSubscriber>().onErrorGetProfile.invoke(throwable)
        }

        viewmodel.getUserInfo()

        verify { getUserInfoResponseObserver.onChanged(any<Fail>()) }
        assert(viewmodel.getUserInfoResponse.value is Fail)

        val result = viewmodel.getUserInfoResponse.value as Fail
        assertEquals(throwable, result.throwable)
    }

    @Test
    fun `when getUserInfo success should set show location admin pop up true`() {
        viewmodel.showAdminLocationPopUp.observeForever(showLocationAdminPopUpObserver)

        coEvery { getProfileUseCase.execute(any()) } coAnswers {
            firstArg<GetProfileSubscriber>().getAdminTypeUseCase?.let { getAdminTypeUseCase = it }
            firstArg<GetProfileSubscriber>().showLocationAdminPopUp?.invoke()
        }

        viewmodel.getUserInfo()

        val expectedLocationAdmin = true
        val actualLocationAdmin = (viewmodel.showAdminLocationPopUp.value as? Success)?.data

        verify { showLocationAdminPopUpObserver.onChanged(any<Success<Boolean>>()) }
        assertEquals(expectedLocationAdmin, actualLocationAdmin)
    }

    @Test
    fun `when getUserInfo error should set show location admin pop up fail`() {
        viewmodel.showAdminLocationPopUp.observeForever(showLocationAdminPopUpObserver)

        coEvery { getProfileUseCase.execute(any()) } coAnswers {
            firstArg<GetProfileSubscriber>().showErrorGetAdminType?.invoke(throwable)
        }

        viewmodel.getUserInfo()

        val expectedError = throwable
        val actualError = (viewmodel.showAdminLocationPopUp.value as? Fail)?.throwable

        verify { showLocationAdminPopUpObserver.onChanged(any<Success<Boolean>>()) }
        assertEquals(expectedError, actualError)
    }

    companion object {
        private val successLoginTokenPhoneResponse: LoginTokenPojo = FileUtil.parse(
                "/success_login_token_sq_check_false.json",
                LoginTokenPojo::class.java
        )
        private val successGetAccountsListResponse: AccountListPojo = FileUtil.parse(
                "/success_get_account_list.json",
                AccountListPojo::class.java
        )
        private val successGetUserInfoResponse: ProfilePojo = FileUtil.parse(
                "/success_get_user_info.json",
                ProfilePojo::class.java
        )
        private val throwable = Throwable()
        private val messageErrorException = MessageErrorException()
    }
}