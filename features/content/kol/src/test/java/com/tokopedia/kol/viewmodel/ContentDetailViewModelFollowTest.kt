package com.tokopedia.kol.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.kol.common.util.ContentDetailResult
import com.tokopedia.kol.feature.postdetail.domain.ContentDetailRepository
import com.tokopedia.kol.feature.postdetail.view.datamodel.type.ShopFollowAction
import com.tokopedia.kol.feature.postdetail.view.viewmodel.ContentDetailViewModel
import com.tokopedia.kol.model.ContentDetailModelBuilder
import com.tokopedia.unit.test.ext.getOrAwaitValue
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

    @Test
    fun `when user follow user, given response success, then it should emit updated data`() {
        val rowNumber = 0
        val followAction = false
        val mutationSuccess = builder.buildMutationSuccess()
        val expectedResultFollow = builder.getUserFollowUnfollowModel(followAction, rowNumber)

        coEvery { mockRepo.followUnfollowUser(followAction, any()) } returns mutationSuccess

        viewModel.followUnFollowUser(followAction, "123", rowNumber)

        val resultMutation = viewModel.followUserObservable.value

        Assertions
            .assertThat(resultMutation)
            .isEqualTo(ContentDetailResult.Success(expectedResultFollow))
    }

    @Test
    fun `when user follow user, given response fail, then it should emit error`() {
        val rowNumber = 0
        val followAction = false
        val mutationError = Throwable("fail follow user")

        coEvery { mockRepo.followUnfollowUser(followAction, any()) } throws mutationError

        viewModel.followUnFollowUser(followAction, "123", rowNumber)

        val resultMutation = viewModel.followUserObservable.value

        Assertions
            .assertThat(resultMutation)
            .isInstanceOf(ContentDetailResult.Failure::class.java)
    }

    @Test
    fun `when user unfollow user, given response success, then it should emit updated data`() {
        val rowNumber = 0
        val followAction = true
        val mutationSuccess = builder.buildMutationSuccess()
        val expectedResult = builder.getUserFollowUnfollowModel(followAction, rowNumber)

        coEvery { mockRepo.followUnfollowUser(followAction, any()) } returns mutationSuccess

        viewModel.followUnFollowUser(followAction, "123", rowNumber)

        val resultMutation = viewModel.followUserObservable.value

        Assertions
            .assertThat(resultMutation)
            .isEqualTo(ContentDetailResult.Success(expectedResult))
    }

    @Test
    fun `when user unfollow user, given response fail, then it should emit error`() {
        val rowNumber = 0
        val followAction = true
        val mutationError = Throwable("fail follow user")

        coEvery { mockRepo.followUnfollowUser(followAction, any()) } throws mutationError

        viewModel.followUnFollowUser(followAction, "123", rowNumber)

        val resultMutation = viewModel.followUserObservable.value

        Assertions
            .assertThat(resultMutation)
            .isInstanceOf(ContentDetailResult.Failure::class.java)
    }

}
