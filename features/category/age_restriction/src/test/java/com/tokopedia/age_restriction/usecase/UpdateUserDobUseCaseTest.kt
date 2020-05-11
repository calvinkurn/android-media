package com.tokopedia.age_restriction.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.age_restriction.data.UserDOBUpdateResponse
import com.tokopedia.age_restriction.repository.ARRepository
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


class UpdateUserDobUseCaseTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val repository: ARRepository = mockk()

    private val updateUserDobUseCase: UpdateUserDobUseCase by lazy {
        spyk(UpdateUserDobUseCase("", repository))
    }


    @Before
    fun setup() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(TestCoroutineDispatcher())
    }

    @Test
    fun `check function invokation of getData`() {
        runBlockingTest {
            coEvery {
                (repository.getGQLData("",
                        UserDOBUpdateResponse::class.java,
                        any()))
            } returns mockk()

            updateUserDobUseCase.getData("", "", "")
            coVerify(exactly = 1) {
                repository.getGQLData("",
                        UserDOBUpdateResponse::class.java,
                        any())
            }
        }
    }

    @Test
    fun `check function invokation of getDobMap on invokation of getData`() {
        runBlockingTest {
            coEvery {
                (repository.getGQLData("",
                        UserDOBUpdateResponse::class.java,
                        any()))
            } returns mockk()

            updateUserDobUseCase.getData("", "", "")
            coVerify(exactly = 1) {
                updateUserDobUseCase.getDobMap("","","")
            }
        }
    }


    @After
    fun shutDown() {
        Dispatchers.resetMain()
    }

}