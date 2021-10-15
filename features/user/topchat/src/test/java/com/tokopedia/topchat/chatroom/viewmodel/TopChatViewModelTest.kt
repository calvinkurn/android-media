package com.tokopedia.topchat.chatroom.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.shop.common.domain.interactor.ToggleFavouriteShopUseCase
import com.tokopedia.topchat.chatroom.domain.pojo.*
import com.tokopedia.topchat.chatroom.domain.usecase.GetExistingMessageIdUseCase
import com.tokopedia.topchat.chatroom.domain.usecase.GetShopFollowingUseCase
import com.tokopedia.topchat.chatroom.view.viewmodel.BroadcastSpamHandlerUiModel
import com.tokopedia.topchat.chatroom.view.viewmodel.TopChatViewModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
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
    lateinit var getShopFollowingUseCase: GetShopFollowingUseCase

    @RelaxedMockK
    lateinit var toggleFavouriteShopUseCase: ToggleFavouriteShopUseCase

    @RelaxedMockK
    lateinit var remoteConfig: RemoteConfig
    private val dispatchers: CoroutineDispatchers = CoroutineTestDispatchersProvider

    private lateinit var viewModel: TopChatViewModel
    private val testShopId = "123"
    private val testUserId = "345"
    private val source = "testSource"
    private val expectedThrowable = Throwable("Oops!")

    @Before
    fun before() {
        MockKAnnotations.init(this)
        viewModel = TopChatViewModel(
            getExistingMessageIdUseCase,
            getShopFollowingUseCase,
            toggleFavouriteShopUseCase,
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
    fun should_not_error_when_the_userid_and_shopid_is_not_number () {
        //Given
        val testShopIdWrong = "test"
        val testUserIdWrong = "test"
        val expectedMessageId = "567"
        val expectedResult = GetExistingMessageIdPojo().apply {
            this.chatExistingChat.messageId = expectedMessageId
        }
        coEvery {
            getExistingMessageIdUseCase.invoke(any())
        } returns expectedResult

        //When
        viewModel.getMessageId(testShopIdWrong, testUserIdWrong, source)

        //Then
        Assert.assertEquals(viewModel.messageId.value,
            Success(expectedResult.chatExistingChat.messageId))
    }

    @Test
    fun should_get_throwable_when_failed_get_message_id() {
        //Given
        coEvery {
            getExistingMessageIdUseCase.invoke(any())
        } throws expectedThrowable

        //When
        viewModel.getMessageId(testShopId, testUserId, source)

        //Then
        Assert.assertEquals(viewModel.messageId.value, Fail(expectedThrowable))
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
        coEvery {
            getShopFollowingUseCase.invoke(any())
        } throws expectedThrowable

        //When
        viewModel.getShopFollowingStatus(testShopId.toLong())

        //Then
        Assert.assertEquals(
            viewModel.shopFollowing.value,
            Fail(expectedThrowable)
        )
    }

    @Test
    fun should_get_boolean_result_when_success_follow_unfollow_shop() {
        //Given
        val booleanResult = true
        val expectedResult = Pair<BroadcastSpamHandlerUiModel?, Result<Boolean>>(
            null, Success(booleanResult))
        coEvery {
            toggleFavouriteShopUseCase.createObservable(requestParams = any()).toBlocking().first()
        } returns booleanResult

        //When
        viewModel.followUnfollowShop(testShopId)

        //Then
        Assert.assertEquals(
            viewModel.followUnfollowShop.value,
            expectedResult
        )
    }

    @Test
    fun should_get_throwable_when_failed_follow_unfollow_shop() {
        //Given
        coEvery {
            toggleFavouriteShopUseCase.createObservable(requestParams = any()).toBlocking().first()
        } throws expectedThrowable

        //When
        viewModel.followUnfollowShop(testShopId)

        //Then
        Assert.assertEquals(
            (viewModel.followUnfollowShop.value?.second as Fail).throwable.message,
            expectedThrowable.message
        )
    }

    @Test
    fun should_get_boolean_result_when_success_request_follow_shop() {
        //Given
        val booleanResult = true
        val broadcastSpamHandlerUiModelTest = mockk<BroadcastSpamHandlerUiModel>(relaxed = true)
        val expectedResult = Pair(broadcastSpamHandlerUiModelTest, Success(booleanResult))
        coEvery {
            toggleFavouriteShopUseCase.createObservable(requestParams = any()).toBlocking().first()
        } returns booleanResult

        //When
        viewModel.followUnfollowShop(testShopId, element = broadcastSpamHandlerUiModelTest)

        //Then
        Assert.assertEquals(
            viewModel.followUnfollowShop.value,
            expectedResult
        )
    }

    @Test
    fun should_get_throwable_when_failed_request_follow_shop() {
        //Given
        val broadcastSpamHandlerUiModelTest = mockk<BroadcastSpamHandlerUiModel>(relaxed = true)
        coEvery {
            toggleFavouriteShopUseCase.createObservable(requestParams = any()).toBlocking().first()
        } throws expectedThrowable

        //When
        viewModel.followUnfollowShop(testShopId, element = broadcastSpamHandlerUiModelTest)

        //Then
        Assert.assertEquals(
            (viewModel.followUnfollowShop.value?.second as Fail).throwable.message,
            expectedThrowable.message
        )
    }

    @Test
    fun should_get_boolean_result_when_success_request_follow_shop_with_action() {
        //Given
        val booleanResult = true
        val broadcastSpamHandlerUiModelTest = mockk<BroadcastSpamHandlerUiModel>(relaxed = true)
        val expectedResult = Pair(broadcastSpamHandlerUiModelTest, Success(booleanResult))
        coEvery {
            toggleFavouriteShopUseCase.createObservable(requestParams = any()).toBlocking().first()
        } returns booleanResult

        //When
        viewModel.followUnfollowShop(
            action = ToggleFavouriteShopUseCase.Action.FOLLOW,
            shopId = testShopId,
            element = broadcastSpamHandlerUiModelTest)

        //Then
        Assert.assertEquals(
            viewModel.followUnfollowShop.value,
            expectedResult
        )
    }

    @Test
    fun should_get_throwable_when_failed_request_follow_shop_with_action() {
        //Given
        val broadcastSpamHandlerUiModelTest = mockk<BroadcastSpamHandlerUiModel>(relaxed = true)
        coEvery {
            toggleFavouriteShopUseCase.createObservable(requestParams = any()).toBlocking().first()
        } throws expectedThrowable

        //When
        viewModel.followUnfollowShop(
            action = ToggleFavouriteShopUseCase.Action.FOLLOW,
            shopId = testShopId,
            element = broadcastSpamHandlerUiModelTest)

        //Then
        Assert.assertEquals(
            (viewModel.followUnfollowShop.value?.second as Fail).throwable.message,
            expectedThrowable.message
        )
    }
}