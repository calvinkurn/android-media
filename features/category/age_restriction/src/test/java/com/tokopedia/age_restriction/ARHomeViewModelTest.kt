package com.tokopedia.age_restriction

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.age_restriction.data.UserDOBResponse
import com.tokopedia.age_restriction.usecase.FetchUserDobUseCase
import com.tokopedia.age_restriction.viewmodel.ARHomeViewModel
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ARHomeViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private var fetchUserDobUseCase: FetchUserDobUseCase = mockk()
    private var userSessionInterface: UserSessionInterface = mockk(relaxed = true)
    private val USER_DOB_PATH = "https://accounts.tokopedia.com/userapp/api/v1/profile/get-dob"

    @RelaxedMockK
    lateinit var response: UserDOBResponse


    private val viewModel: ARHomeViewModel by lazy {
        spyk(ARHomeViewModel(fetchUserDobUseCase, userSessionInterface))
    }

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(TestCoroutineDispatcher())
    }

    /****************************************** doOnCreate() ****************************************/

    @Test
    fun `is logged in`() {

        coEvery { userSessionInterface.isLoggedIn } returns true
        viewModel.doOnCreate()

        coVerify { viewModel.fetchUserDOB() }
    }

    @Test
    fun `not logged in`() {

        coEvery { userSessionInterface.isLoggedIn } returns false
        viewModel.doOnCreate()

        assertEquals(viewModel.getAskUserLogin().value, 1)
    }

    /****************************************** fetchUserDOB() ****************************************/

    @Test
    fun `dob verified and isAdult case`() {
        coEvery {
            fetchUserDobUseCase.getData(USER_DOB_PATH)
        } returns response

        coEvery { response.isDobVerified } returns true
        coEvery { response.isAdult } returns true

        viewModel.fetchUserDOB()

        assertEquals(viewModel.userAdult.value, 1)
        assertEquals(viewModel.getProgBarVisibility().value, false)
    }

    @Test
    fun `dob verified and age_is_greater_than_18`() {
        coEvery {
            fetchUserDobUseCase.getData(any())
        } returns response

        coEvery { response.isDobVerified } returns true
        coEvery { response.age } returns 22

        viewModel.fetchUserDOB()

        assertEquals(viewModel.userAdult.value, 1)
        assertEquals(viewModel.getProgBarVisibility().value, false)
    }


    @Test
    fun `dob verified and not Adult case`() {

        coEvery {
            fetchUserDobUseCase.getData(USER_DOB_PATH)
        } returns response

        coEvery { response.isDobVerified } returns true
        coEvery { response.isAdult } returns false

        viewModel.fetchUserDOB()

        assertEquals(viewModel.notAdult.value, 1)
        assertEquals(viewModel.getProgBarVisibility().value, false)
    }

    @Test
    fun `dob verified and age_is_less_than_18`() {
        coEvery {
            fetchUserDobUseCase.getData(USER_DOB_PATH)
        } returns response

        coEvery { response.isDobVerified } returns true
        coEvery { response.age } returns 12

        viewModel.fetchUserDOB()

        assertEquals(viewModel.notAdult.value, 1)
        assertEquals(viewModel.getProgBarVisibility().value, false)
    }


    @Test
    fun `dob not verified but DobExist`() {
        coEvery {
            fetchUserDobUseCase.getData(USER_DOB_PATH)
        } returns response

        coEvery { response.isDobVerified } returns false
        coEvery { response.isDobExist } returns true
        coEvery { response.bday } returns "1995"


        viewModel.fetchUserDOB()

        assertEquals(viewModel.notVerified.value, "1995")
        assertEquals(viewModel.getProgBarVisibility().value, false)
    }


    @Test
    fun `dob not verified also Dob not Exist`() {
        coEvery {
            fetchUserDobUseCase.getData(USER_DOB_PATH)
        } returns response

        coEvery { response.isDobVerified } returns false
        coEvery { response.isDobExist } returns false

        viewModel.fetchUserDOB()

        assertEquals(viewModel.notFilled.value, 1)
        assertEquals(viewModel.getProgBarVisibility().value, false)
    }


    @After
    fun shutDown() {
        Dispatchers.resetMain()
    }
}