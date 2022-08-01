package com.tokopedia.topchat.chatroom.domain.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.topchat.chatroom.domain.pojo.GetExistingMessageIdPojo
import com.tokopedia.topchat.stubRepository
import com.tokopedia.topchat.stubRepositoryAsThrow
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.assertThrows

class GetExistingMessageIdUseCaseTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @RelaxedMockK
    lateinit var repository: GraphqlRepository
    private val dispatchers: CoroutineDispatchers = CoroutineTestDispatchersProvider

    private lateinit var useCase: GetExistingMessageIdUseCase
    private val testShopId: String = "123"
    private val testUserId: String = "345"
    private val source = "testSource"

    @Before
    fun before() {
        MockKAnnotations.init(this)
        useCase = GetExistingMessageIdUseCase(repository, dispatchers)
    }

    @Test
    fun should_get_message_id_when_successfully_get_data() {
        //Given
        val expectedMessageId = "567"
        val expectedResult = GetExistingMessageIdPojo().apply {
            this.chatExistingChat.messageId = expectedMessageId
        }
        val params = GetExistingMessageIdUseCase.Param(testShopId, testUserId, source)

        //Then
        runBlocking {
            repository.stubRepository(expectedResult, onError = mapOf())
            val result = useCase(params)
            Assert.assertEquals(result, expectedResult)
        }
    }

    @Test
    fun should_get_throwable_when_failed_to_get_data() {
        //Given
        val params = GetExistingMessageIdUseCase.Param(testShopId, testUserId, source)
        val expectedResult = Throwable("Oops!")
        repository.stubRepositoryAsThrow(expectedResult)

        //Then
        assertThrows<Throwable> {
            runBlocking {
                useCase.invoke(params)
            }
        }
    }

}