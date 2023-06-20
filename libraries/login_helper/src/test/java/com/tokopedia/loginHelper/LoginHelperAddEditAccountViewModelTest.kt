package com.tokopedia.loginHelper

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.gson.Gson
import com.tokopedia.encryption.security.AESEncryptorCBC
import com.tokopedia.encryption.security.RsaUtils
import com.tokopedia.loginHelper.domain.usecase.AddUserRestUseCase
import com.tokopedia.loginHelper.domain.usecase.EditUserRestUseCase
import com.tokopedia.loginHelper.presentation.addEditAccount.viewmodel.LoginHelperAddEditAccountViewModel
import com.tokopedia.network.refreshtoken.EncoderDecoder
import com.tokopedia.unit.test.rule.CoroutineTestRule
import io.mockk.MockKAnnotations
import io.mockk.mockk
import io.mockk.mockkObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.resetMain
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class LoginHelperAddEditAccountViewModelTest {

    @get:Rule
    var rule = InstantTaskExecutorRule()

    @get:Rule
    var testRule = CoroutineTestRule()

    private lateinit var addUserRestUseCase: AddUserRestUseCase
    private lateinit var editUserRestUseCase: EditUserRestUseCase
    private lateinit var aesEncryptorCBC: AESEncryptorCBC
    private lateinit var gson: Gson

    private lateinit var viewModel: LoginHelperAddEditAccountViewModel

    private val throwable = Throwable("Error")
    private val email = "sourav.saikia@tokopedia.com"
    private val password = "abc123456"

    @Before
    @Throws(Exception::class)
    fun setUp() {
        MockKAnnotations.init(this)
        mockkObject(RsaUtils)
        mockkObject(EncoderDecoder())
        addUserRestUseCase = mockk(relaxed = true)
        editUserRestUseCase = mockk(relaxed = true)
        aesEncryptorCBC = mockk(relaxed = true)
        gson = mockk(relaxed = true)
        viewModel =
            LoginHelperAddEditAccountViewModel(
                addUserRestUseCase,
                editUserRestUseCase,
                aesEncryptorCBC,
                gson,
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
        Assert.assertTrue(true)
    }
}
