package com.tokopedia.otp.notif.domain.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.otp.notif.domain.pojo.DeviceStatusPushNotifPojo
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import junit.framework.Assert
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.lang.reflect.Type
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse

class DeviceStatusPushNotifUseCaseTest {

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @RelaxedMockK
    lateinit var graphqlRepository: GraphqlRepository

    private val dispatcherProviderTest = CoroutineTestDispatchersProvider

    private lateinit var useCase: DeviceStatusPushNotifUseCase

    @Before
    fun before() {
        MockKAnnotations.init(this)
        useCase = DeviceStatusPushNotifUseCase(graphqlRepository, dispatcherProviderTest)
    }

    @Test
    fun `on success get device status push notif`() {
        val result = HashMap<Type, Any>()
        result[DeviceStatusPushNotifPojo::class.java] = successResponse
        val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)

        coEvery { graphqlRepository.response(any(), any()) } returns gqlResponse

        runBlocking(dispatcherProviderTest.io) {
            val data = useCase.getData(mapOf())
            Assert.assertNotNull(data)
            assertEquals(successResponse, data)
        }
    }

    @Test
    fun `on success get device status push notif and result data is success`() {
        val result = HashMap<Type, Any>()
        result[DeviceStatusPushNotifPojo::class.java] = successResponse
        val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)

        coEvery { graphqlRepository.response(any(), any()) } returns gqlResponse

        runBlocking(dispatcherProviderTest.io) {
            val data = useCase.getData(mapOf())
            Assert.assertNotNull(data)
            assertEquals(successResponse, data)
            assert(data.data.success)
        }
    }

    @Test
    fun `on success get device status push notif and result data is failed`() {
        val result = HashMap<Type, Any>()
        result[DeviceStatusPushNotifPojo::class.java] = failedResponse
        val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)

        coEvery { graphqlRepository.response(any(), any()) } returns gqlResponse

        runBlocking(dispatcherProviderTest.io) {
            val data = useCase.getData(mapOf())
            Assert.assertNotNull(data)
            assertEquals(failedResponse, data)
            assertFalse(data.data.success)
        }
    }

    @Test
    fun `on failed get device status push notif`() {
        coEvery { graphqlRepository.response(any(), any()) } coAnswers { throw Throwable() }

        runBlocking(dispatcherProviderTest.io) {
            assertFails { useCase.getData(mapOf()) }
        }
    }

    @Test
    fun `on failed runtime exception get device status push notif`() {
        coEvery { graphqlRepository.response(any(), any()) } coAnswers { throw RuntimeException() }

        runBlocking(dispatcherProviderTest.io) {
            assertFailsWith<RuntimeException> { useCase.getData(mapOf()) }
        }
    }

    @Test
    fun `on failed null pointer exception get device status push notif`() {
        coEvery { graphqlRepository.response(any(), any()) } coAnswers { throw NullPointerException() }

        runBlocking(dispatcherProviderTest.io) {
            assertFailsWith<NullPointerException> { useCase.getData(mapOf()) }
        }
    }

    companion object {
        private val successResponse: DeviceStatusPushNotifPojo = FileUtil.parse(
                "/success_get_device_status_push_notif.json",
                DeviceStatusPushNotifPojo::class.java
        )
        private val failedResponse: DeviceStatusPushNotifPojo = FileUtil.parse(
                "/success_get_device_status_push_notif_result_failed.json",
                DeviceStatusPushNotifPojo::class.java
        )
    }
}