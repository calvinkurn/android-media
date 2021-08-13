package com.tokopedia.sellerhomecommon.domain.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.sellerhomecommon.domain.mapper.TableMapper
import com.tokopedia.sellerhomecommon.domain.model.GetTableDataResponse
import com.tokopedia.sellerhomecommon.domain.model.DynamicParameterModel
import com.tokopedia.sellerhomecommon.utils.TestHelper
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import org.mockito.ArgumentMatchers

/**
 * Created By @ilhamsuaib on 22/07/20
 */

@ExperimentalCoroutinesApi
class GetTableDataUseCaseTest {

    companion object {
        private const val SUCCESS_RESPONSE = "json/get_table_data_success_response.json"
    }

    @get:Rule
    val expectedException = ExpectedException.none()

    @RelaxedMockK
    lateinit var gqlRepository: GraphqlRepository

    @RelaxedMockK
    lateinit var mapper: TableMapper

    private lateinit var getTableDataUseCase: GetTableDataUseCase

    private val params = GetTableDataUseCase.getRequestParams(
            dataKey = ArgumentMatchers.anyList(),
            dynamicParameter = DynamicParameterModel()
    )

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        getTableDataUseCase = GetTableDataUseCase(gqlRepository, mapper, CoroutineTestDispatchersProvider)
    }

    @Test
    fun `should success when get table data`() = runBlocking {
        getTableDataUseCase.params = params

        val successResponse = TestHelper.createSuccessResponse<GetTableDataResponse>(SUCCESS_RESPONSE)

        coEvery {
            gqlRepository.getReseponse(any(), any())
        } returns successResponse

        val result = getTableDataUseCase.executeOnBackground()

        coEvery {
            gqlRepository.getReseponse(any(), any())
        }

        Assert.assertTrue(!result.isNullOrEmpty())
    }

    @Test
    fun `should failed when get table data`() = runBlocking {
        getTableDataUseCase.params = params
        val errorResponse = TestHelper.createErrorResponse<GetTableDataResponse>()

        coEvery {
            gqlRepository.getReseponse(any(), any())
        } returns errorResponse

        expectedException.expect(RuntimeException::class.java)
        val result = getTableDataUseCase.executeOnBackground()

        coEvery {
            gqlRepository.getReseponse(any(), any())
        }

        Assert.assertTrue(result.isNullOrEmpty())
    }
}