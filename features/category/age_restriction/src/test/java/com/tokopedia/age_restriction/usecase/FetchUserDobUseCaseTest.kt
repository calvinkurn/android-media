package com.tokopedia.age_restriction.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.gson.reflect.TypeToken
import com.tokopedia.age_restriction.data.UserDOBResponse
import com.tokopedia.age_restriction.repository.ARRepository
import com.tokopedia.network.data.model.response.DataResponse
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class FetchUserDobUseCaseTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val repository: ARRepository = mockk()

    private val fetchUserDobUseCase: FetchUserDobUseCase by lazy {
        spyk(FetchUserDobUseCase(repository))
    }


    @Before
    fun setup() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(TestCoroutineDispatcher())
    }

    @Test(expected = NullPointerException::class)
    fun `getRestData throws exception`() {
        runBlockingTest {
            coEvery {
                (repository.getRestData("",
                        object : TypeToken<DataResponse<UserDOBResponse>>() {}.type,
                        any())
                        as DataResponse<UserDOBResponse>).data
            } returns  null

            fetchUserDobUseCase.getData("")

        }
    }


    @Test
    fun `check function invokation of getRestData`() {
        runBlockingTest {
            coEvery {
                (repository.getRestData("",
                        object : TypeToken<DataResponse<UserDOBResponse>>() {}.type,
                        any())
                        as DataResponse<UserDOBResponse>).data
            } returns mockk()

            fetchUserDobUseCase.getData("")
            coVerify(exactly = 1) {
                (repository.getRestData("",
                        object : TypeToken<DataResponse<UserDOBResponse>>() {}.type,
                        any()) as DataResponse<UserDOBResponse>).data
            }
        }
    }


    @After
    fun shutDown() {
        Dispatchers.resetMain()
    }

}