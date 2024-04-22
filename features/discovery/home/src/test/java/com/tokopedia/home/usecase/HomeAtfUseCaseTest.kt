package com.tokopedia.home.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.home.beranda.data.newatf.AtfDataList
import com.tokopedia.home.beranda.data.newatf.DynamicPositionRepository
import com.tokopedia.home.beranda.data.newatf.banner.HomepageBannerRepository
import com.tokopedia.home.beranda.data.newatf.channel.AtfChannelRepository
import com.tokopedia.home.beranda.data.newatf.icon.DynamicIconRepository
import com.tokopedia.home.beranda.data.newatf.mission.MissionWidgetRepository
import com.tokopedia.home.beranda.data.newatf.ticker.TickerRepository
import com.tokopedia.home.beranda.data.newatf.todo.TodoWidgetRepository
import com.tokopedia.unit.test.rule.UnconfinedTestRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

/**
 * Created by frenzel
 */
@FlowPreview
@ExperimentalCoroutinesApi
class HomeAtfUseCaseTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val coroutineTestRule = UnconfinedTestRule()

    private val dispatcher = coroutineTestRule.dispatchers
    private val testDispatcher = coroutineTestRule.coroutineDispatcher

    private val mockDynamicPositionRepository = mockk<DynamicPositionRepository>(relaxed = true)
    private val mockBannerRepository = mockk<HomepageBannerRepository>(relaxed = true)
    private val mockIconRepository = mockk<DynamicIconRepository>(relaxed = true)
    private val mockTickerRepository = mockk<TickerRepository>(relaxed = true)
    private val mockMissionRepository = mockk<MissionWidgetRepository>(relaxed = true)
    private val mockTodoRepository = mockk<TodoWidgetRepository>(relaxed = true)
    private val mockChannelRepository = mockk<AtfChannelRepository>(relaxed = true)

    @Test
    fun `given empty cache and success remote when getting dynamic position then only emit remote data`() = runTest {
        coEvery { mockDynamicPositionRepository.flow } returns flow {
            emit(mockDynamicPositionCacheEmpty())
            emit(mockDynamicPositionSuccess(isCache = false))
        }

        val homeAtfUseCase = createHomeAtfUseCase(
            homeDispatcher = dispatcher,
            dynamicPositionRepository = mockDynamicPositionRepository
        )
        val result = mutableListOf<AtfDataList?>()
        backgroundScope.launch(testDispatcher) {
            homeAtfUseCase.flow.toList(result)
        }

        homeAtfUseCase.fetchAtfDataList()

        Assert.assertNull(result[0])
        assert(result[1] != null && result[1]?.status == AtfDataList.STATUS_SUCCESS && !result[1]!!.isCache)
    }

    @Test
    fun `given cache exists and success remote when getting dynamic position then emit cache first then remote`() = runTest {
        val result: MutableList<AtfDataList?> = mutableListOf()
        coEvery { mockDynamicPositionRepository.flow } returns flow {
            delay(500)
            emit(mockDynamicPositionSuccess(isCache = true))
            delay(500)
            emit(mockDynamicPositionSuccess(isCache = false))
        }

        val homeAtfUseCase = createHomeAtfUseCase(
            homeDispatcher = dispatcher,
            dynamicPositionRepository = mockDynamicPositionRepository
        )

        backgroundScope.launch(testDispatcher) {
            homeAtfUseCase.flow.toList(result)
        }

        homeAtfUseCase.fetchAtfDataList()
        delay(2000)

        Assert.assertNull(result[0])
        val result1 = result[1]
        assert(result1 != null && result1.status == AtfDataList.STATUS_SUCCESS && result1.isCache)
        val result2 = result[2]
        assert(result2 != null && result2.status == AtfDataList.STATUS_SUCCESS && !result2.isCache)
    }

    @Test
    fun `given cache exists and remote failed when getting dynamic position then keep cache data with status error`() = runTest {
        val result: MutableList<AtfDataList?> = mutableListOf()

        coEvery { mockDynamicPositionRepository.flow } returns flow {
            delay(500)
            emit(mockDynamicPositionSuccess(isCache = true))
            delay(500)
            emit(mockDynamicPositionRemoteFailedCacheExists())
        }

        val homeAtfUseCase = createHomeAtfUseCase(
            homeDispatcher = dispatcher,
            dynamicPositionRepository = mockDynamicPositionRepository
        )

        backgroundScope.launch(testDispatcher) {
            homeAtfUseCase.flow.toList(result)
        }

        homeAtfUseCase.fetchAtfDataList()
        delay(2000)

        Assert.assertNull(result[0])
        val result1 = result[1]
        assert(result1 != null && result1.status == AtfDataList.STATUS_SUCCESS && result1.isCache && result1.listAtfData.isNotEmpty())
        val result2 = result[2]
        assert(result2 != null && result2.status == AtfDataList.STATUS_ERROR && result2.isCache && result2.listAtfData.isNotEmpty())
    }

    @Test
    fun `given needToFetchComponents flag true then fetch remote data for each ATF components`() = runTest {
        val result: MutableList<AtfDataList?> = mutableListOf()

        coEvery { mockDynamicPositionRepository.flow } returns flow {
            emit(mockDynamicPositionSuccess(isCache = true))
        }

        val homeAtfUseCase = createHomeAtfUseCase(
            dynamicPositionRepository = mockDynamicPositionRepository,
            homepageBannerRepository = mockBannerRepository,
            dynamicIconRepository = mockIconRepository,
            tickerRepository = mockTickerRepository,
            missionWidgetRepository = mockMissionRepository,
            todoWidgetRepository = mockTodoRepository,
            atfChannelRepository = mockChannelRepository
        )

        backgroundScope.launch(testDispatcher) {
            homeAtfUseCase.flow.toList(result)
        }

        homeAtfUseCase.fetchAtfDataList()

        coVerify { mockBannerRepository.getData(any()) }
        coVerify { mockIconRepository.getData(any()) }
        coVerify { mockTickerRepository.getData(any()) }
        coVerify { mockMissionRepository.getData(any()) }
        coVerify { mockTodoRepository.getData(any()) }
        coVerify { mockChannelRepository.getData(any()) }
    }
}
