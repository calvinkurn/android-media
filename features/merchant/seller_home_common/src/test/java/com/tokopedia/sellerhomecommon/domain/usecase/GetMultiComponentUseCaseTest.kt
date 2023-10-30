package com.tokopedia.sellerhomecommon.domain.usecase

import com.google.gson.Gson
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.sellerhomecommon.common.const.DateFilterType
import com.tokopedia.sellerhomecommon.domain.mapper.MultiComponentMapper
import com.tokopedia.sellerhomecommon.domain.mapper.TooltipMapper
import com.tokopedia.sellerhomecommon.domain.model.FetchMultiComponentResponse
import com.tokopedia.sellerhomecommon.domain.model.ParamCommonWidgetModel
import com.tokopedia.sellerhomecommon.presentation.model.MultiLineGraphDataUiModel
import com.tokopedia.sellerhomecommon.presentation.model.MultiLineGraphWidgetUiModel
import com.tokopedia.sellerhomecommon.presentation.model.PieChartDataUiModel
import com.tokopedia.sellerhomecommon.presentation.model.PieChartWidgetUiModel
import com.tokopedia.sellerhomecommon.utils.TestHelper
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import org.mockito.ArgumentMatchers

class GetMultiComponentUseCaseTest {

    companion object {
        private const val SUCCESS_RESPONSE = "json/get_multi_component_data_success_response.json"
    }

    @get:Rule
    val expectedException = ExpectedException.none()

    @RelaxedMockK
    lateinit var gqlRepository: GraphqlRepository

    @RelaxedMockK
    lateinit var mapper: MultiComponentMapper

    @RelaxedMockK
    lateinit var multiLineUseCase: GetMultiLineGraphUseCase

    @RelaxedMockK
    lateinit var pieChartUseCase: GetPieChartDataUseCase

    private val getMultiComponentDetailDataUseCase by lazy {
        GetMultiComponentDetailDataUseCase(
            CoroutineTestDispatchersProvider,
            { multiLineUseCase },
            { pieChartUseCase }
        )
    }

    private fun getDynamicParameter(): ParamCommonWidgetModel {
        return ParamCommonWidgetModel(
            startDate = "15-07-20202",
            endDate = "21-07-20202",
            pageSource = "test",
            dateType = DateFilterType.DATE_TYPE_DAY
        )
    }

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    private val getMultiComponentDataUseCase by lazy {
        GetMultiComponentDataUseCase(
            gqlRepository, MultiComponentMapper(
                Gson(),
                TooltipMapper()
            ), CoroutineTestDispatchersProvider
        )
    }

    private val params = GetMultiComponentDataUseCase.createRequestParams(
        dataKey = ArgumentMatchers.anyList(),
        dynamicParameter = ParamCommonWidgetModel()
    )

    @Test
    fun `should success get multi component data`() = runBlocking {
        getMultiComponentDataUseCase.params = params
        val successResponse = TestHelper.createSuccessResponse<FetchMultiComponentResponse>(
            SUCCESS_RESPONSE
        )

        coEvery {
            gqlRepository.response(any(), any())
        } returns successResponse

        val multiComponentData = getMultiComponentDataUseCase.executeOnBackground()

        coVerify {
            gqlRepository.response(any(), any())
        }

        Assert.assertTrue(multiComponentData.size == 1)
        Assert.assertTrue(multiComponentData.first().tabs.size == 2)
        Assert.assertTrue(multiComponentData.first().tabs.first().components.size == 2)
        Assert.assertTrue(
            multiComponentData.first().tabs.first().components.first().data is PieChartWidgetUiModel
        )
        Assert.assertTrue(
            multiComponentData.first().tabs.first().components[1].data is PieChartWidgetUiModel
        )

        Assert.assertTrue(multiComponentData.first().tabs[1].components.size == 1)
        Assert.assertTrue(
            multiComponentData.first().tabs[1].components.first().data is MultiLineGraphWidgetUiModel
        )
    }

    @Test
    fun `should failed get multi component data`() = runBlocking {
        getMultiComponentDataUseCase.params = params

        val errorResponse = TestHelper.createErrorResponse<FetchMultiComponentResponse>()

        coEvery {
            gqlRepository.response(any(), any())
        } returns errorResponse

        expectedException.expect(com.tokopedia.network.exception.MessageErrorException::class.java)


        val multiComponentData = getMultiComponentDataUseCase.executeOnBackground()

        coVerify {
            gqlRepository.response(any(), any())
        }

        Assert.assertTrue(multiComponentData.isNullOrEmpty())
    }

    @Test
    fun `should success when execute 2 async pie chart get multi component data`() = runBlocking {
        getMultiComponentDataUseCase.params = params
        val successResponse = TestHelper.createSuccessResponse<FetchMultiComponentResponse>(
            SUCCESS_RESPONSE
        )

        coEvery {
            gqlRepository.response(any(), any())
        } returns successResponse

        coEvery {
            pieChartUseCase.executeOnBackground()
        } returns listOf(PieChartDataUiModel())

        val multiComponentData = getMultiComponentDataUseCase.executeOnBackground()

        val result = getMultiComponentDetailDataUseCase.executeOnBackground(
            multiComponentData.first().tabs.first(),
            getDynamicParameter()
        )

        coVerify(exactly = 2) {
            pieChartUseCase.executeOnBackground()
        }

        assert(result.size == 2)
        assert(result.first().data?.data is PieChartDataUiModel)
        assert(result[1].data?.data is PieChartDataUiModel)
    }

    @Test
    fun `should success when execute 1 async multi line graph get multi component data`() =
        runBlocking {
            getMultiComponentDataUseCase.params = params
            val successResponse = TestHelper.createSuccessResponse<FetchMultiComponentResponse>(
                SUCCESS_RESPONSE
            )

            coEvery {
                gqlRepository.response(any(), any())
            } returns successResponse

            coEvery {
                multiLineUseCase.executeOnBackground()
            } returns listOf(MultiLineGraphDataUiModel())

            val multiComponentData = getMultiComponentDataUseCase.executeOnBackground()

            //Second tabs containing 1 multiline graph
            val result = getMultiComponentDetailDataUseCase.executeOnBackground(
                multiComponentData.first().tabs[1],
                getDynamicParameter()
            )

            coVerify(exactly = 1) {
                multiLineUseCase.executeOnBackground()
            }

            assert(result.size == 1)
            assert(result.first().data?.data is MultiLineGraphDataUiModel)
        }

    @Test
    fun `should failed when execute 2 async pie chart get multi component data`() = runBlocking {
        getMultiComponentDataUseCase.params = params
        val successResponse = TestHelper.createSuccessResponse<FetchMultiComponentResponse>(
            SUCCESS_RESPONSE
        )

        coEvery {
            gqlRepository.response(any(), any())
        } returns successResponse

        coEvery {
            pieChartUseCase.executeOnBackground()
        } throws RuntimeException("error")

        val multiComponentData = getMultiComponentDataUseCase.executeOnBackground()

        val result = getMultiComponentDetailDataUseCase.executeOnBackground(
            multiComponentData.first().tabs.first(),
            getDynamicParameter()
        )

        coVerify(exactly = 2) {
            pieChartUseCase.executeOnBackground()
        }

        assert(result.size == 2)
        assert(result.first().data?.data == null)
        assert(result[1].data?.data == null)
    }
}