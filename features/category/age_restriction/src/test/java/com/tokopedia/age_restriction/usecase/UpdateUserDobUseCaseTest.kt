package com.tokopedia.age_restriction.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.gson.reflect.TypeToken
import com.tokopedia.abstraction.common.data.model.response.DataResponse
import com.tokopedia.age_restriction.data.UserDOBResponse
import com.tokopedia.age_restriction.data.UserDOBUpdateResponse
import com.tokopedia.age_restriction.repository.ARRepository
import com.tokopedia.age_restriction.viewmodel.VerifyDOBViewModel
import com.tokopedia.common.network.data.model.RequestType
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.*


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