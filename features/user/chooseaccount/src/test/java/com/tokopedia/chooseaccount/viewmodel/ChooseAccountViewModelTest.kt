package com.tokopedia.chooseaccount.viewmodel

import FileUtil
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.chooseaccount.data.AccountListDataModel
import com.tokopedia.chooseaccount.data.AccountsDataModel
import com.tokopedia.chooseaccount.data.ErrorResponseDataModel
import com.tokopedia.chooseaccount.di.ChooseAccountQueryConstant
import com.tokopedia.chooseaccount.domain.usecase.GetAccountListUseCase
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.sessioncommon.data.Error
import com.tokopedia.sessioncommon.data.LoginToken
import com.tokopedia.sessioncommon.data.LoginTokenPojo
import com.tokopedia.sessioncommon.data.profile.ProfilePojo
import com.tokopedia.sessioncommon.domain.subscriber.LoginTokenSubscriber
import com.tokopedia.sessioncommon.domain.usecase.LoginTokenUseCase
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.unit.test.ext.getOrAwaitValue
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.lang.RuntimeException
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
class ChooseAccountViewModelTest {

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @RelaxedMockK
    lateinit var getAccountsListUseCase: GetAccountListUseCase

    @RelaxedMockK
    lateinit var loginTokenUseCase: LoginTokenUseCase

    @RelaxedMockK
    lateinit var userSession: UserSessionInterface

    @RelaxedMockK
    lateinit var getAccountListDataModelPhoneResponseObserver: Observer<Result<AccountListDataModel>>

    @RelaxedMockK
    lateinit var loginPhoneNumberResponseObserver: Observer<Result<LoginToken>>

    @RelaxedMockK
    lateinit var goToActivationPageObserver: Observer<Result<MessageErrorException>>

    @RelaxedMockK
    lateinit var goToSecurityQuestionObserver: Observer<Result<String>>

    private lateinit var viewmodel: ChooseAccountViewModel

    @Before
    fun before() {
        MockKAnnotations.init(this)
        viewmodel = ChooseAccountViewModel(
            getAccountsListUseCase,
            userSession,
            loginTokenUseCase,
            CoroutineTestDispatchersProvider,
        )
    }

    @Test
    fun `Success login token phone`() {
        viewmodel.loginPhoneNumberResponse.observeForever(loginPhoneNumberResponseObserver)

        coEvery { loginTokenUseCase.executeLoginPhoneNumber(any(), any()) } coAnswers {
            secondArg<LoginTokenSubscriber>().onSuccessLoginToken.invoke(
                successLoginTokenPhoneResponse
            )
        }

        viewmodel.loginTokenPhone("", "", "")

        verify { loginPhoneNumberResponseObserver.onChanged(any<Success<LoginToken>>()) }
        assert(viewmodel.loginPhoneNumberResponse.value is Success)

        val result = viewmodel.loginPhoneNumberResponse.value as Success<LoginToken>
        assert(result.data == successLoginTokenPhoneResponse.loginToken)
    }

    @Test
    fun `Success login token phone with error`() {
        val msg = "err message"
        val response = LoginTokenPojo(
            loginToken = LoginToken(errors = arrayListOf(Error(message = msg)))
        )
        coEvery { loginTokenUseCase.executeLoginPhoneNumber(any(), any()) } coAnswers {
            secondArg<LoginTokenSubscriber>().onSuccessLoginToken.invoke(
                response
            )
        }

        viewmodel.loginTokenPhone("", "", "")

        viewmodel.loginPhoneNumberResponse.getOrAwaitValue().let {
            assertThat(it, instanceOf(Fail::class.java))
            assertEquals((it as Fail).throwable.message, msg)
        }

    }

    @Test
    fun `Success login token phone with error else condition`() {
        val response = LoginTokenPojo()
        coEvery { loginTokenUseCase.executeLoginPhoneNumber(any(), any()) } coAnswers {
            secondArg<LoginTokenSubscriber>().onSuccessLoginToken.invoke(
                response
            )
        }

        viewmodel.loginTokenPhone("", "", "")

        viewmodel.loginPhoneNumberResponse.getOrAwaitValue().let {
            assertThat(it, instanceOf(Fail::class.java))
            assertThat((it as Fail).throwable, instanceOf(RuntimeException::class.java))
        }

    }

    @Test
    fun `Success login token phone with sq check`() {
        viewmodel.securityQuestion.observeForever(goToSecurityQuestionObserver)

        coEvery { loginTokenUseCase.executeLoginPhoneNumber(any(), any()) } coAnswers {
            secondArg<LoginTokenSubscriber>().onGoToSecurityQuestion.invoke()
        }

        viewmodel.loginTokenPhone("", "", "123")

        verify { goToSecurityQuestionObserver.onChanged(any()) }
        assert(viewmodel.securityQuestion.value is Success)

        val result = (viewmodel.securityQuestion.value as Success).data
        assert(result == "123")
    }

    @Test
    fun `Failed login token phone`() {
        viewmodel.loginPhoneNumberResponse.observeForever(loginPhoneNumberResponseObserver)
        viewmodel.activationPage.observeForever(goToActivationPageObserver)
        viewmodel.securityQuestion.observeForever(goToSecurityQuestionObserver)

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
        viewmodel.activationPage.observeForever(goToActivationPageObserver)

        coEvery { loginTokenUseCase.executeLoginPhoneNumber(any(), any()) } coAnswers {
            secondArg<LoginTokenSubscriber>().onGoToActivationPage.invoke(messageErrorException)
        }

        viewmodel.loginTokenPhone("", "", "")

        verify { goToActivationPageObserver.onChanged(any()) }
        assert(viewmodel.activationPage.value is Success)

        val result = (viewmodel.activationPage.value as Success).data
        assertEquals(messageErrorException, result)
    }

    @Test
    fun `loginTokenPhone onHasPopupError`() {
        viewmodel.activationPage.observeForever(goToActivationPageObserver)
        val pojo = LoginTokenPojo()

        coEvery { loginTokenUseCase.executeLoginPhoneNumber(any(), any()) } coAnswers {
            secondArg<LoginTokenSubscriber>().onShowPopupError.invoke(pojo)
        }

        viewmodel.loginTokenPhone("", "", "")

        viewmodel.popupError.getOrAwaitValue().let {
            assertThat(it, instanceOf(Success::class.java))
        }
    }

    @Test
    fun `Success get account list phone`() {
        viewmodel.getAccountListDataModelPhoneResponse.observeForever(
            getAccountListDataModelPhoneResponseObserver
        )

        coEvery { getAccountsListUseCase.invoke(any()) } returns SUCCESS_GET_ACCOUNTS_LIST_RESPONSE

        viewmodel.getAccountListPhoneNumber("", "")

        verify { getAccountListDataModelPhoneResponseObserver.onChanged(any<Success<AccountListDataModel>>()) }
        assert(viewmodel.getAccountListDataModelPhoneResponse.value is Success)

        val result =
            viewmodel.getAccountListDataModelPhoneResponse.value as Success<AccountListDataModel>
        assert(result.data == SUCCESS_GET_ACCOUNTS_LIST_RESPONSE.accountListDataModel)
    }

    @Test
    fun `Success get account list phone error message`() {
        val errorMsg = "error"
        coEvery { getAccountsListUseCase.invoke(any()) } returns AccountsDataModel(
            accountListDataModel = AccountListDataModel(
                errorResponseDataModels = listOf(
                    ErrorResponseDataModel(message = errorMsg)
                )
            )
        )

        viewmodel.getAccountListPhoneNumber("", "")

        assertThat(
            viewmodel.getAccountListDataModelPhoneResponse.getOrAwaitValue(),
            instanceOf(Fail::class.java)
        )
    }

    @Test
    fun `Success get account list phone empty error message`() {
        coEvery { getAccountsListUseCase.invoke(any()) } returns AccountsDataModel(
            accountListDataModel = AccountListDataModel(
                errorResponseDataModels = listOf(
                    ErrorResponseDataModel()
                )
            )
        )

        viewmodel.getAccountListPhoneNumber("", "")

        assertThat(
            viewmodel.getAccountListDataModelPhoneResponse.getOrAwaitValue(),
            instanceOf(Fail::class.java)
        )
    }

    @Test
    fun `Failed get account list phone`() {
        viewmodel.getAccountListDataModelPhoneResponse.observeForever(
            getAccountListDataModelPhoneResponseObserver
        )

        coEvery { getAccountsListUseCase.invoke(any()) } throws throwable

        viewmodel.getAccountListPhoneNumber("", "")

        verify { getAccountListDataModelPhoneResponseObserver.onChanged(any<Fail>()) }
        assert(viewmodel.getAccountListDataModelPhoneResponse.value is Fail)

        val result = viewmodel.getAccountListDataModelPhoneResponse.value as Fail
        assertEquals(throwable, result.throwable)
    }

    companion object {
        private val successLoginTokenPhoneResponse: LoginTokenPojo = FileUtil.parse(
            "/success_login_token_sq_check_false.json",
            LoginTokenPojo::class.java
        )
        private val SUCCESS_GET_ACCOUNTS_LIST_RESPONSE: AccountsDataModel = FileUtil.parse(
            "/success_get_account_list.json",
            AccountsDataModel::class.java
        )
        private val successGetUserInfoResponse: ProfilePojo = FileUtil.parse(
            "/success_get_user_info.json",
            ProfilePojo::class.java
        )
        private val throwable = Throwable()
        private val messageErrorException = MessageErrorException()
    }
}