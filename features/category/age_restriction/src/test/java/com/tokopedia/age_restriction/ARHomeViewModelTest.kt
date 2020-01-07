package com.tokopedia.age_restriction

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.gson.reflect.TypeToken
import com.tokopedia.abstraction.common.data.model.response.DataResponse
import com.tokopedia.age_restriction.data.UserDOBResponse
import com.tokopedia.age_restriction.viewmodel.ARHomeViewModel
import com.tokopedia.common.network.data.model.RequestType
import com.tokopedia.common.network.data.model.RestResponse
import com.tokopedia.tradein_common.repository.BaseRepository
import com.tokopedia.usecase.RequestParams
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Assert.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ARHomeViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private var application: Application = mockk()
    private var repository: BaseRepository = mockk()
    private val USER_DOB_PATH = "https://accounts.tokopedia.com/userapp/api/v1/profile/get-dob"

    @RelaxedMockK
    lateinit var response: RestResponse


    private val viewModel: ARHomeViewModel by lazy {
        spyk(ARHomeViewModel(application))
    }

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(TestCoroutineDispatcher())
        every { viewModel.getRepo() } returns repository
    }


    @Test
    fun `dob verified and isAdult case`() {
        coEvery {
            repository.getRestData(USER_DOB_PATH,
                    object : TypeToken<DataResponse<UserDOBResponse>>() {}.type,
                    RequestType.GET,
                    RequestParams.EMPTY.parameters)
        } returns response

        coEvery { response.getData<DataResponse<UserDOBResponse>>().data.isDobVerified } returns true
        coEvery { response.getData<DataResponse<UserDOBResponse>>().data.isAdult } returns true

        viewModel.fetchUserDOB()

        coVerify {
            repository.getRestData(USER_DOB_PATH,
                    object : TypeToken<DataResponse<UserDOBResponse>>() {}.type,
                    RequestType.GET,
                    RequestParams.EMPTY.parameters)
        }
        assertEquals(viewModel.userAdult.value, 1)
        assertEquals(viewModel.progBarVisibility.value, false)
    }


    @Test
    fun `dob verified and not Adult case`() {
        coEvery {
            repository.getRestData(USER_DOB_PATH,
                    object : TypeToken<DataResponse<UserDOBResponse>>() {}.type,
                    RequestType.GET,
                    RequestParams.EMPTY.parameters)
        } returns response

        coEvery { response.getData<DataResponse<UserDOBResponse>>().data.isDobVerified } returns true
        coEvery { response.getData<DataResponse<UserDOBResponse>>().data.isAdult } returns false

        viewModel.fetchUserDOB()

        coVerify {
            repository.getRestData(USER_DOB_PATH,
                    object : TypeToken<DataResponse<UserDOBResponse>>() {}.type,
                    RequestType.GET,
                    RequestParams.EMPTY.parameters)
        }
        assertEquals(viewModel.notAdult.value, 1)
        assertEquals(viewModel.progBarVisibility.value, false)
    }

    @Test
    fun `dob not verified but DobExist`() {
        coEvery {
            repository.getRestData(USER_DOB_PATH,
                    object : TypeToken<DataResponse<UserDOBResponse>>() {}.type,
                    RequestType.GET,
                    RequestParams.EMPTY.parameters)
        } returns response

        coEvery { response.getData<DataResponse<UserDOBResponse>>().data.isDobVerified } returns false
        coEvery { response.getData<DataResponse<UserDOBResponse>>().data.isDobExist } returns true
        coEvery { response.getData<DataResponse<UserDOBResponse>>().data.bday } returns "1995"


        viewModel.fetchUserDOB()

        coVerify {
            repository.getRestData(USER_DOB_PATH,
                    object : TypeToken<DataResponse<UserDOBResponse>>() {}.type,
                    RequestType.GET,
                    RequestParams.EMPTY.parameters)
        }
        assertEquals(viewModel.notVerified.value, "1995")
        assertEquals(viewModel.progBarVisibility.value, false)
    }


    @Test
    fun `dob not verified also Dob not Exist`() {
        coEvery {
            repository.getRestData(USER_DOB_PATH,
                    object : TypeToken<DataResponse<UserDOBResponse>>() {}.type,
                    RequestType.GET,
                    RequestParams.EMPTY.parameters)
        } returns response

        coEvery { response.getData<DataResponse<UserDOBResponse>>().data.isDobVerified } returns false
        coEvery { response.getData<DataResponse<UserDOBResponse>>().data.isDobExist } returns false

        viewModel.fetchUserDOB()

        coVerify {
            repository.getRestData(USER_DOB_PATH,
                    object : TypeToken<DataResponse<UserDOBResponse>>() {}.type,
                    RequestType.GET,
                    RequestParams.EMPTY.parameters)
        }
        assertEquals(viewModel.notFilled.value, 1)
        assertEquals(viewModel.progBarVisibility.value, false)
    }

    @After
    fun shutDown() {
        Dispatchers.resetMain()
    }
}