package com.tokopedia.topchat.chatroom.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.topchat.chatroom.domain.pojo.*
import com.tokopedia.topchat.chatroom.domain.usecase.GetExistingMessageIdUseCase
import com.tokopedia.topchat.chatroom.domain.usecase.GetShopFollowingUseCaseNew
import com.tokopedia.topchat.chatroom.view.viewmodel.TopChatViewModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class TopChatViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    @RelaxedMockK
    lateinit var getExistingMessageIdUseCase: GetExistingMessageIdUseCase

    @RelaxedMockK
    lateinit var getShopFollowingUseCase: GetShopFollowingUseCaseNew

    @RelaxedMockK
    lateinit var remoteConfig: RemoteConfig
    private val dispatchers: CoroutineDispatchers = CoroutineTestDispatchersProvider

    private lateinit var viewModel: TopChatViewModel
    private val testShopId = "123"
    private val testUserId = "345"
    private val source = "testSource"

    @Before
    fun before() {
        MockKAnnotations.init(this)
        viewModel = TopChatViewModel(
            getExistingMessageIdUseCase,
            getShopFollowingUseCase,
            dispatchers,
            remoteConfig
        )
    }

    @Test
    fun should_get_message_id_when_successfull() {
        //Given
        val expectedMessageId = "567"
        val expectedResult = GetExistingMessageIdPojo().apply {
            this.chatExistingChat.messageId = expectedMessageId
        }
        coEvery {
            getExistingMessageIdUseCase.invoke(any())
        } returns expectedResult

        //When
        viewModel.getMessageId(testShopId, testUserId, source)

        //Then
        Assert.assertEquals(
            viewModel.messageId.value,
            Success(expectedResult.chatExistingChat.messageId)
        )
    }

    @Test
    fun should_get_throwable_when_failed_get_message_id() {
        //Given
        val expectedResult = Throwable("Oops!")
        coEvery {
            getExistingMessageIdUseCase.invoke(any())
        } throws expectedResult

        //When
        viewModel.getMessageId(testShopId, testUserId, source)

        //Then
        Assert.assertEquals(viewModel.messageId.value, Fail(expectedResult))
    }

    @Test
    fun should_get_shop_following_data_when_successfull() {
        //Given
        val resultData = ResultItem(FavoriteData(alreadyFavorited = 1))
        val expectedResult = ShopFollowingPojo(
            shopInfoById = ShopInfoById(arrayListOf(resultData))
        )
        coEvery {
            getShopFollowingUseCase.invoke(any())
        } returns expectedResult

        //When
        viewModel.getShopFollowingStatus(testShopId.toLong())

        //Then
        Assert.assertEquals(
            (viewModel.shopFollowing.value as Success).data.isFollow,
            expectedResult.isFollow
        )
    }

    @Test
    fun should_get_throwable_when_failed_get_shop_following() {
        //Given
        val expectedResult = Throwable("Oops!")
        coEvery {
            getShopFollowingUseCase.invoke(any())
        } throws expectedResult

        //When
        viewModel.getShopFollowingStatus(testShopId.toLong())

        //Then
        Assert.assertEquals(
            viewModel.shopFollowing.value,
            Fail(expectedResult)
        )
    }
}