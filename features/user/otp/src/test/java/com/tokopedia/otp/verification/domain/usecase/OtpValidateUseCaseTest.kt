package com.tokopedia.otp.verification.domain.usecase

import FileUtil
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.otp.verification.domain.data.OtpValidatePojo
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import junit.framework.Assert.assertNotNull
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.lang.reflect.Type
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse

class OtpValidateUseCaseTest {

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @RelaxedMockK
    lateinit var graphqlRepository: GraphqlRepository

    private val dispatcherProviderTest = CoroutineTestDispatchersProvider

    private lateinit var useCase: OtpValidateUseCase

    @Before
    fun before() {
        MockKAnnotations.init(this)
        useCase = OtpValidateUseCase(graphqlRepository, dispatcherProviderTest)
    }

    @Test
    fun `on success validate otp`() {
        val result = HashMap<Type, Any>()
        result[OtpValidatePojo::class.java] = successResponse
        val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)

        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponse

        runBlocking(dispatcherProviderTest.io) {
            val data = useCase.getData(mapOf())
            assertNotNull(data)
            assertEquals(successResponse, data)
        }
    }

    @Test
    fun `on success validate otp and result data is success`() {
        val result = HashMap<Type, Any>()
        result[OtpValidatePojo::class.java] = successResponse
        val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)

        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponse

        runBlocking(dispatcherProviderTest.io) {
            val data = useCase.getData(mapOf())
            assertNotNull(data)
            assertEquals(successResponse, data)
            assert(data.data.success)
        }
    }

    @Test
    fun `on success validate otp and result data is failed`() {
        val result = HashMap<Type, Any>()
        result[OtpValidatePojo::class.java] = failedResponse
        val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)

        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponse

        runBlocking(dispatcherProviderTest.io) {
            val data = useCase.getData(mapOf())
            assertNotNull(data)
            assertEquals(failedResponse, data)
            assertFalse(data.data.success)
        }
    }

    @Test
    fun `on failed validate otp`() {
        coEvery { graphqlRepository.getReseponse(any(), any()) } coAnswers { throw Throwable() }

        runBlocking(dispatcherProviderTest.io) {
            assertFails { useCase.getData(mapOf()) }
        }
    }

    @Test
    fun `on failed runtime exception validate otp`() {
        coEvery { graphqlRepository.getReseponse(any(), any()) } coAnswers { throw RuntimeException() }

        runBlocking(dispatcherProviderTest.io) {
            assertFailsWith<RuntimeException> { useCase.getData(mapOf()) }
        }
    }

    @Test
    fun `on failed null pointer exception validate otp`() {
        coEvery { graphqlRepository.getReseponse(any(), any()) } coAnswers { throw NullPointerException() }

        runBlocking(dispatcherProviderTest.io) {
            assertFailsWith<NullPointerException> { useCase.getData(mapOf()) }
        }
    }

    companion object {
        private val successResponse: OtpValidatePojo = FileUtil.parse(
                "/success_otp_validate.json",
                OtpValidatePojo::class.java
        )
        private val failedResponse: OtpValidatePojo = FileUtil.parse(
                "/success_otp_validate_result_failed.json",
                OtpValidatePojo::class.java
        )
    }
}