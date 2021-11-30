package com.tokopedia.topchat.chatroom.viewmodel

import com.tokopedia.shop.common.domain.interactor.ToggleFavouriteShopUseCase
import com.tokopedia.topchat.chatroom.view.viewmodel.BroadcastSpamHandlerUiModel
import com.tokopedia.topchat.chatroom.viewmodel.base.BaseTopChatViewModelTest
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Assert
import org.junit.Test

class ToggleFollowUnfollowViewModelTest: BaseTopChatViewModelTest() {
    @Test
    fun should_get_boolean_result_when_success_follow_unfollow_shop() {
        //Given
        val booleanResult = true
        val expectedResult = Pair<BroadcastSpamHandlerUiModel?, Result<Boolean>>(
            null, Success(booleanResult)
        )
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