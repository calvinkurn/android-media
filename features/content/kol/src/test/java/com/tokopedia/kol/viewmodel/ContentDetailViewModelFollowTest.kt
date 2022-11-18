package com.tokopedia.kol.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.kol.common.util.ContentDetailResult
import com.tokopedia.kol.feature.postdetail.domain.ContentDetailRepository
import com.tokopedia.kol.feature.postdetail.view.datamodel.type.ShopFollowAction
import com.tokopedia.kol.feature.postdetail.view.viewmodel.ContentDetailViewModel
import com.tokopedia.kol.model.ContentDetailModelBuilder
import com.tokopedia.unit.test.rule.CoroutineTestRule
import io.mockk.coEvery
import io.mockk.mockk
import org.assertj.core.api.Assertions
import org.junit.Rule
import org.junit.Test

/**
 * Created by meyta.taliti on 08/08/22.
 */
class ContentDetailViewModelFollowTest {

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val rule: CoroutineTestRule = CoroutineTestRule()

    private val testDispatcher = rule.dispatchers

    private val mockRepo: ContentDetailRepository = mockk(relaxed = true)
    private val viewModel = ContentDetailViewModel(
        testDispatcher,
        mockRepo,
    )

    private val shopId = "1234"
    private val builder = ContentDetailModelBuilder()

    @Test
    fun `when user follow show, given response success, then it should emit ShopFollowModel`() {
        val rowNumber = 0
        val followAction = ShopFollowAction.Follow
        val expectedResult = builder.getShopFollowModel(followAction, rowNumber)

        coEvery { mockRepo.followShop(shopId, followAction, rowNumber) } returns expectedResult

        viewModel.followShop(shopId, followAction, rowNumber)

        val result = viewModel.followShopObservable.value

        Assertions
            .assertThat(result)
            .isEqualTo(ContentDetailResult.Success(expectedResult))
    }

    @Test
    fun `when user unfollow shop, given response success, then it should emit ShopFollowModel`() {
        val rowNumber = 0
        val followAction = ShopFollowAction.UnFollow
        val expectedResult = builder.getShopFollowModel(followAction, rowNumber)

        coEvery { mockRepo.followShop(shopId, followAction, rowNumber) } returns expectedResult

        viewModel.followShop(shopId, followAction, rowNumber)

        val result = viewModel.followShopObservable.value

        Assertions
            .assertThat(result)
            .isEqualTo(ContentDetailResult.Success(expectedResult))
    }

    @Test
    fun `when user follow shop, given response error, then it should emit error`() {
        val rowNumber = 0
        val followAction = ShopFollowAction.Follow

        coEvery { mockRepo.followShop(shopId, followAction, rowNumber) } throws Throwable()

        viewModel.followShop(shopId, followAction, rowNumber)

        val result = viewModel.followShopObservable.value

        Assertions
            .assertThat(result)
            .isInstanceOf(ContentDetailResult.Failure::class.java)
    }

    @Test
    fun `when user unfollow shop, given response error, then it should emit error`() {
        val rowNumber = 0
        val followAction = ShopFollowAction.UnFollow

        coEvery { mockRepo.followShop(shopId, followAction, rowNumber) } throws Throwable()

        viewModel.followShop(shopId, followAction, rowNumber)

        val result = viewModel.followShopObservable.value

        Assertions
            .assertThat(result)
            .isInstanceOf(ContentDetailResult.Failure::class.java)
    }
}