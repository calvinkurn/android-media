package com.tokopedia.libra.domain.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.libra.data.entity.LibraParameter
import com.tokopedia.libra.data.entity.LibraResponse
import com.tokopedia.libra.data.repository.CacheRepository
import com.tokopedia.libra.domain.robot.createSetLibraUseCaseRobot
import com.tokopedia.libra.utils.MockUtil
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchers
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class SetGetLibraUseCaseTest {

    private val gqlRepository = mockk<GraphqlRepository>(relaxed = true)
    private val cacheRepository = mockk<CacheRepository>()

    private val dispatchers = CoroutineTestDispatchers

    @Test
    fun `should able to execute the libra's gql and store the cache locally`() =
        dispatchers.coroutineDispatcher.runBlockingTest {
            // Given
            shouldMockGqlResponse()
            shouldMockInvokeSaveCache()

            // When
            val robot = createSetLibraUseCaseRobot(
                gqlRepository = gqlRepository,
                cacheRepository = cacheRepository
            )

            // Then
            assertEquals(robot.getResult(), robot.libraUiModel)
        }

    private fun shouldMockInvokeSaveCache() {
        every { cacheRepository.save(any(), any()) } just Runs
    }

    private fun shouldMockGqlResponse() {
        coEvery { gqlRepository.response(any(), any()) } returns MockUtil.createSuccessResponse(
            LibraResponse(
                listOf(
                    LibraParameter("foo", "bar")
                )
            )
        )
    }
}
