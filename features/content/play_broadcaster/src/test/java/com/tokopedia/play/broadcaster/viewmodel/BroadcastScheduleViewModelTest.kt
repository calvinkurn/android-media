package com.tokopedia.play.broadcaster.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.play.broadcaster.data.config.HydraConfigStore
import com.tokopedia.play.broadcaster.data.datastore.BroadcastScheduleDataStore
import com.tokopedia.play.broadcaster.robot.andThen
import com.tokopedia.play.broadcaster.robot.andWhen
import com.tokopedia.play.broadcaster.robot.givenBroadcastScheduleViewModel
import com.tokopedia.play.broadcaster.robot.thenVerify
import com.tokopedia.play.broadcaster.ui.model.BroadcastScheduleUiModel
import com.tokopedia.play.broadcaster.util.*
import com.tokopedia.play_common.model.result.NetworkResult
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchers
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.*

/**
 * Created by jegul on 21/05/21
 */
class BroadcastScheduleViewModelTest {

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    val dispatcher = CoroutineTestDispatchers

    private val responseBuilder = PlayBroadcasterResponseBuilder()
    private val testModelBuilder = TestDoubleModelBuilder()

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher.coroutineDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when set schedule succeed, it should return success`() {
        val mockScheduleDataStore: BroadcastScheduleDataStore = mockk(relaxed = true)
        val mockDataStore = testModelBuilder.buildSetupDataStore(
                dispatcher = dispatcher,
                scheduleDataStore = mockScheduleDataStore
        )

        val successResult = NetworkResult.Success(Unit)

        givenBroadcastScheduleViewModel(
            setupDataStore = mockDataStore
        ) {
            coEvery { mockScheduleDataStore.updateBroadcastSchedule(any(), any()) } returns successResult
        }.andThen {
            setSchedule(Calendar.getInstance())
        }.andWhen {
            getUpdateScheduleResult()
        }.thenVerify {
            it.assertSuccess()
            it.isEqualTo(successResult)
        }
    }

    @Test
    fun `when set schedule failed, it should return error`() {
        val mockScheduleDataStore: BroadcastScheduleDataStore = mockk(relaxed = true)
        val mockDataStore = testModelBuilder.buildSetupDataStore(
                dispatcher = dispatcher,
                scheduleDataStore = mockScheduleDataStore
        )

        val failedResult = NetworkResult.Fail(IllegalArgumentException("Set Schedule Error"))

        givenBroadcastScheduleViewModel(
                setupDataStore = mockDataStore
        ) {
            coEvery { mockScheduleDataStore.updateBroadcastSchedule(any(), any()) } returns failedResult
        }.andThen {
            setSchedule(Calendar.getInstance())
        }.andWhen {
            getUpdateScheduleResult()
        }.thenVerify {
            it.assertWhenFailed { error ->
                error.isEqualToComparingFieldByField(failedResult.error)
            }
        }
    }

    @Test
    fun `when delete schedule succeed, it should return success`() {
        val mockScheduleDataStore: BroadcastScheduleDataStore = mockk(relaxed = true)
        val mockDataStore = testModelBuilder.buildSetupDataStore(
                dispatcher = dispatcher,
                scheduleDataStore = mockScheduleDataStore
        )

        val successResult = NetworkResult.Success(Unit)

        givenBroadcastScheduleViewModel(
                setupDataStore = mockDataStore
        ) {
            coEvery { mockScheduleDataStore.deleteBroadcastSchedule(any()) } returns successResult
        }.andThen {
            deleteSchedule()
        }.andWhen {
            getDeleteScheduleResult()
        }.thenVerify {
            it.assertSuccess()
            it.isEqualTo(successResult)
        }
    }

    @Test
    fun `when delete schedule failed, it should return error`() {
        val mockScheduleDataStore: BroadcastScheduleDataStore = mockk(relaxed = true)
        val mockDataStore = testModelBuilder.buildSetupDataStore(
                dispatcher = dispatcher,
                scheduleDataStore = mockScheduleDataStore
        )

        val failedResult = NetworkResult.Fail(IllegalArgumentException("Delete Schedule Error"))

        givenBroadcastScheduleViewModel(
                setupDataStore = mockDataStore
        ) {
            coEvery { mockScheduleDataStore.deleteBroadcastSchedule(any()) } returns failedResult
        }.andThen {
            deleteSchedule()
        }.andWhen {
            getDeleteScheduleResult()
        }.thenVerify {
            it.assertWhenFailed { error ->
                error.isEqualToComparingFieldByField(failedResult.error)
            }
        }
    }

    @Test
    fun `given schedule has been set, when retrieved the date, it should return the correct date`() {
        val mockScheduleDataStore: BroadcastScheduleDataStore = mockk(relaxed = true)
        val mockDataStore = testModelBuilder.buildSetupDataStore(
                dispatcher = dispatcher,
                scheduleDataStore = mockScheduleDataStore
        )

        val scheduleModel = BroadcastScheduleUiModel.Scheduled(Date(), "22 Feb 2022")

        givenBroadcastScheduleViewModel(
                setupDataStore = mockDataStore
        ) {
            every { mockScheduleDataStore.getSchedule() } returns scheduleModel
        }.andWhen {
            getDefaultScheduleDate()
        }.thenVerify {
            it.isEqualTo(scheduleModel.time)
        }
    }

    @Test
    fun `given schedule has not been set, when retrieved the date, it should return default date`() {
        val mockScheduleDataStore: BroadcastScheduleDataStore = mockk(relaxed = true)
        val mockDataStore = testModelBuilder.buildSetupDataStore(
                dispatcher = dispatcher,
                scheduleDataStore = mockScheduleDataStore
        )

        val mockConfigStore: HydraConfigStore = mockk(relaxed = true)

        val defaultDate = Date()

        givenBroadcastScheduleViewModel(
                setupDataStore = mockDataStore,
                hydraConfigStore = mockConfigStore
        ) {
            every { mockConfigStore.getDefaultScheduleDate() } returns defaultDate
            every { mockScheduleDataStore.getSchedule() } returns null
        }.andWhen {
            getDefaultScheduleDate()
        }.thenVerify {
            it.isEqualTo(defaultDate)
        }
    }

    @Test
    fun `given minimum schedule date has been set, when retrieved, it should return the correct date`(){
        val mockConfigStore: HydraConfigStore = mockk(relaxed = true)

        val minDate = Date()

        givenBroadcastScheduleViewModel(
                hydraConfigStore = mockConfigStore
        ) {
            every { mockConfigStore.getMinScheduleDate() } returns minDate
        }.andWhen {
            getMinimumScheduleDate()
        }.thenVerify {
            it.isEqualTo(minDate)
        }
    }

    @Test
    fun `given maximum schedule date has been set, when retrieved, it should return the correct date`(){
        val mockConfigStore: HydraConfigStore = mockk(relaxed = true)

        val maxDate = Date()

        givenBroadcastScheduleViewModel(
                hydraConfigStore = mockConfigStore
        ) {
            every { mockConfigStore.getMaxScheduleDate() } returns maxDate
        }.andWhen {
            getMaximumScheduleDate()
        }.thenVerify {
            it.isEqualTo(maxDate)
        }
    }

    @Test
    fun `given schedule has been set, when retrieved, it should return the correct schedule`() {
        val mockScheduleDataStore: BroadcastScheduleDataStore = mockk(relaxed = true)
        val mockDataStore = testModelBuilder.buildSetupDataStore(
                dispatcher = dispatcher,
                scheduleDataStore = mockScheduleDataStore
        )

        val scheduleModel = BroadcastScheduleUiModel.Scheduled(Date(), "22 Feb 2022")

        givenBroadcastScheduleViewModel(
                setupDataStore = mockDataStore
        ) {
            every { mockScheduleDataStore.getSchedule() } returns scheduleModel
        }.andWhen {
            getSchedule()
        }.thenVerify {
            it.isEqualTo(scheduleModel)
        }
    }

    @Test
    fun `given schedule has not been set, when retrieved, it should return no schedule`() {
        val mockScheduleDataStore: BroadcastScheduleDataStore = mockk(relaxed = true)
        val mockDataStore = testModelBuilder.buildSetupDataStore(
                dispatcher = dispatcher,
                scheduleDataStore = mockScheduleDataStore
        )

        givenBroadcastScheduleViewModel(
                setupDataStore = mockDataStore
        ) {
            every { mockScheduleDataStore.getSchedule() } returns null
        }.andWhen {
            getSchedule()
        }.thenVerify {
            it.isEqualTo(BroadcastScheduleUiModel.NoSchedule)
        }
    }
}