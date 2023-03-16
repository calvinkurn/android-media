package com.tokopedia.privacycenter.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.privacycenter.ui.main.PrivacyCenterViewModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

class PrivacyCenterViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val dispatcher = CoroutineTestDispatchersProvider
    private lateinit var viewModel: PrivacyCenterViewModel

    private val userSession = mockk<UserSessionInterface>(relaxed = true)

    @Before
    fun setup() {
        viewModel = PrivacyCenterViewModel(
            userSession,
            dispatcher
        )
    }

    @Test
    fun `when get name then return name`() {
        val name = "Habibi"

        coEvery { userSession.name } returns name

        val result = viewModel.getUserName()
        assertEquals(name, result)
    }

    @Test
    fun `when get status login then return user logged in`() {
        val isLoggedIn = true

        coEvery { userSession.isLoggedIn } returns isLoggedIn

        val result = viewModel.isLoggedIn()
        assertEquals(isLoggedIn, result)
    }

    @Test
    fun `when get status login then return user not logged in`() {
        val isLoggedIn = false

        coEvery { userSession.isLoggedIn } returns isLoggedIn

        val result = viewModel.isLoggedIn()
        assertEquals(isLoggedIn, result)
    }
}
