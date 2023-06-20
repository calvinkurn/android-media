package com.tokopedia.loginHelper

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.encryption.security.AESEncryptorCBC
import com.tokopedia.encryption.security.RsaUtils
import com.tokopedia.loginHelper.domain.usecase.DeleteUserRestUseCase
import com.tokopedia.loginHelper.domain.usecase.GetUserDetailsRestUseCase
import com.tokopedia.loginHelper.presentation.searchAccount.viewmodel.LoginHelperSearchAccountViewModel
import com.tokopedia.network.refreshtoken.EncoderDecoder
import com.tokopedia.unit.test.rule.CoroutineTestRule
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.MockKAnnotations
import io.mockk.mockk
import io.mockk.mockkObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.resetMain
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import javax.crypto.SecretKey

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class LoginHelperSearchAccountViewModelTest {

    @get:Rule
    var rule = InstantTaskExecutorRule()

    @get:Rule
    var testRule = CoroutineTestRule()

    private lateinit var getUserDetailsRestCase: GetUserDetailsRestUseCase
    private lateinit var deleteUserRestUseCase: DeleteUserRestUseCase
    private lateinit var aesEncryptorCBC: AESEncryptorCBC
    private lateinit var secretKey: SecretKey

    private lateinit var viewModel: LoginHelperSearchAccountViewModel
    private lateinit var userSession: UserSessionInterface

    private val throwable = Throwable("Error")
    private val email = "sourav.saikia@tokopedia.com"
    private val password = "abc123456"

    @Before
    @Throws(Exception::class)
    fun setUp() {
        MockKAnnotations.init(this)
        mockkObject(RsaUtils)
        mockkObject(EncoderDecoder())
        getUserDetailsRestCase = mockk(relaxed = true)
        deleteUserRestUseCase = mockk(relaxed = true)
        userSession = mockk(relaxed = true)
        aesEncryptorCBC = mockk(relaxed = true)
        secretKey = mockk(relaxed = true)
        viewModel =
            LoginHelperSearchAccountViewModel(
                getUserDetailsRestCase,
                deleteUserRestUseCase,
                aesEncryptorCBC,
                secretKey,
                testRule.dispatchers
            )
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test`() {
        assertTrue(true)
    }
}
