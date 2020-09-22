package com.tokopedia.statistic.presentation.domain.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.statistic.domain.model.GetUserRoleModel
import com.tokopedia.statistic.domain.usecase.GetUserRoleUseCase
import com.tokopedia.statistic.utils.TestHelper
import com.tokopedia.usecase.RequestParams
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException

/**
 * Created By @ilhamsuaib on 30/07/20
 */

@ExperimentalCoroutinesApi
class GetUserRoleUseCaseTest {

    companion object {
        private const val RESPONSE_SUCCESS = "json/get_user_role_success_response.json"
    }

    private lateinit var params: RequestParams
    private lateinit var getUserRoleUseCase: GetUserRoleUseCase

    @get:Rule
    val expectedException = ExpectedException.none()

    @RelaxedMockK
    lateinit var gqlRepository: GraphqlRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        params = GetUserRoleUseCase.createParam(12345)
        getUserRoleUseCase = GetUserRoleUseCase(gqlRepository)
    }

    @Test
    fun `should success get user role`() = runBlocking {
        getUserRoleUseCase.params = params
        val successResponse = TestHelper.createSuccessResponse<GetUserRoleModel>(RESPONSE_SUCCESS)

        coEvery {
            gqlRepository.getReseponse(any(), any())
        } returns successResponse

        val result = getUserRoleUseCase.executeOnBackground()

        coVerify {
            gqlRepository.getReseponse(any(), any())
        }

        assert(result.isNotEmpty())
    }

    @Test
    fun `should failed get user role`() = runBlocking {
        getUserRoleUseCase.params = params
        val errorResponse = TestHelper.createErrorResponse<GetUserRoleModel>()

        coEvery {
            gqlRepository.getReseponse(any(), any())
        } returns errorResponse

        expectedException.expect(RuntimeException::class.java)
        val result = getUserRoleUseCase.executeOnBackground()

        coVerify {
            gqlRepository.getReseponse(any(), any())
        }

        assert(result.isNullOrEmpty())
    }
}