package com.tokopedia.topchat.chatroom.viewmodel

import com.tokopedia.topchat.chatroom.domain.pojo.FavoriteData
import com.tokopedia.topchat.chatroom.domain.pojo.ResultItem
import com.tokopedia.topchat.chatroom.domain.pojo.ShopFollowingPojo
import com.tokopedia.topchat.chatroom.domain.pojo.ShopInfoById
import com.tokopedia.topchat.chatroom.viewmodel.base.BaseTopChatViewModelTest
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import org.junit.Assert
import org.junit.Test

class GetShopFollowingStatusViewModelTest: BaseTopChatViewModelTest() {

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
}