package com.tokopedia.age_restriction

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.age_restriction.data.UserDOBUpdateData
import com.tokopedia.age_restriction.data.UserDOBUpdateResponse
import com.tokopedia.age_restriction.usecase.UpdateUserDobUseCase
import com.tokopedia.age_restriction.viewmodel.VerifyDOBViewModel
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class VerifyDOBViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val updateUserDobUseCase: UpdateUserDobUseCase = mockk()

    private val viewModel: VerifyDOBViewModel by lazy {
        spyk(VerifyDOBViewModel(updateUserDobUseCase))
    }

    @RelaxedMockK
    lateinit var response: UserDOBUpdateResponse

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(TestCoroutineDispatcher())
    }


    @Test
    fun testDataMethodFails() {
        val exception = Exception("adfad")
        coEvery { updateUserDobUseCase.getData(any(), any(), any()) } throws exception

        viewModel.updateUserDoB("", "", "")

        assertEquals(viewModel.getWarningMessage().value, exception.localizedMessage)

    }


    @Test
    fun `check whether response error is not null`() {
        coEvery { updateUserDobUseCase.getData(any(), any(), any()) } returns response
        coEvery { response.userDobUpdateData.error } returns "error_message"
        viewModel.updateUserDoB("", "", "")

        assertFalse(viewModel.getProgBarVisibility().value ?: true)
        assertEquals(viewModel.getWarningMessage().value, "error_message")
        assertEquals(viewModel.userIsAdult.value, null)
        assertEquals(viewModel.userNotAdult.value, null)
    }

    @Test
    fun `check for adult`() {
        coEvery { updateUserDobUseCase.getData(any(), any(), any()) } returns response

        coEvery { response.userDobUpdateData.isDobVerified } returns true
        coEvery { response.userDobUpdateData.age } returns 22

        viewModel.updateUserDoB("", "", "")


        assertTrue(viewModel.getProgBarVisibility().value ?: false)
        assertEquals(viewModel.userIsAdult.value, true)

    }

    @Test
    fun `check for not adult`() {
        coEvery { updateUserDobUseCase.getData(any(), any(), any()) } returns response

        coEvery { response.userDobUpdateData.isDobVerified } returns true
        coEvery { response.userDobUpdateData.age } returns 12

        viewModel.updateUserDoB("", "", "")

        assertTrue(viewModel.getProgBarVisibility().value ?: false)
        assertEquals(viewModel.userNotAdult.value, true)

    }

    @After
    fun shutDown() {
        Dispatchers.resetMain()
    }

}