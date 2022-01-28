package com.tokopedia.affiliate.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.affiliate.PAGE_ZERO
import com.tokopedia.affiliate.model.response.*
import com.tokopedia.affiliate.usecase.*
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class AffiliateLoginViewModelTest{
    private val userSessionInterface: UserSessionInterface = mockk()
    private var aff = spyk(AffiliateLoginViewModel(userSessionInterface))

    @get:Rule
    var rule = InstantTaskExecutorRule()

    @Before
    @Throws(Exception::class)
    fun setUp() {

        MockKAnnotations.init(this)
        Dispatchers.setMain(TestCoroutineDispatcher())
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun userSessionTest() {
        val name = "Testing"
        val profile = "Profile Testing"
        val email = "testing.profile@tokopedia.com"
        val isLoggedIn = false
        coEvery { userSessionInterface.name } returns name
        coEvery { userSessionInterface.profilePicture } returns profile
        coEvery { userSessionInterface.isLoggedIn } returns isLoggedIn
        coEvery { userSessionInterface.email } returns email


        assertEquals(aff.getUserName(), name)
        assertEquals(aff.getUserProfilePicture(), profile)
        assertEquals(aff.isUserLoggedIn(), isLoggedIn)
        assertEquals(aff.getUserEmail(),email)

    }
}