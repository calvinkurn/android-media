package com.tokopedia.shop.home.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.shop.common.data.source.cloud.model.followshop.FollowShop
import com.tokopedia.shop.common.data.source.cloud.model.followshop.FollowShopResponse
import com.tokopedia.shop.common.data.source.cloud.model.followstatus.FollowStatus
import com.tokopedia.shop.common.data.source.cloud.model.followstatus.FollowStatusResponse
import com.tokopedia.shop.common.domain.interactor.GQLGetShopFavoriteStatusUseCase
import com.tokopedia.shop.common.domain.interactor.GetFollowStatusUseCase
import com.tokopedia.shop.common.domain.interactor.ToggleFavouriteShopUseCase
import com.tokopedia.shop.common.domain.interactor.UpdateFollowStatusUseCase
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo
import com.tokopedia.shop.home.data.model.ShopHomeCampaignNplTncModel
import com.tokopedia.shop.home.domain.GetShopHomeCampaignNplTncUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.util.LiveDataUtil.observeAwaitValue
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import rx.Observable

@ExperimentalCoroutinesApi
class ShopHomeNplCampaignTncBottomSheetViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @RelaxedMockK
    lateinit var userSession: UserSessionInterface
    @RelaxedMockK
    lateinit var getCampaignNplTncUseCase: GetShopHomeCampaignNplTncUseCase
    @RelaxedMockK
    lateinit var updateFollowStatusUseCase: UpdateFollowStatusUseCase
    @RelaxedMockK
    lateinit var getFollowStatusUseCase: GetFollowStatusUseCase

    private val testCoroutineDispatcherProvider by lazy {
        CoroutineTestDispatchersProvider
    }
    private lateinit var viewModel: ShopHomeNplCampaignTncBottomSheetViewModel


    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = ShopHomeNplCampaignTncBottomSheetViewModel(
                testCoroutineDispatcherProvider,
                userSession,
                getCampaignNplTncUseCase,
                updateFollowStatusUseCase,
                getFollowStatusUseCase
        )
    }

    @Test
    fun `check whether userSessionShopId return same value as mocked value`() {
        val sampleShopId = "1234"
        every { userSession.shopId } returns sampleShopId
        assert(viewModel.userSessionShopId == sampleShopId)
    }

    @Test
    fun `check whether userSessionShopId return empty string when userSession shopId return null`() {
        every { userSession.shopId } returns null
        assert(viewModel.userSessionShopId == "")
    }

    @Test
    fun `check whether _campaignTncLiveData and _campaignFollowStatusLiveData post success data`() {
        val sampleCampaignId = "1234"
        val shopId = "12345"
        coEvery { getCampaignNplTncUseCase.executeOnBackground() } returns ShopHomeCampaignNplTncModel()
        coEvery { getFollowStatusUseCase.executeOnBackground().followStatus } returns FollowStatus(null, null, null)
        viewModel.getTncBottomSheetData(sampleCampaignId, shopId, false)

        viewModel.campaignTncLiveData.observeAwaitValue()
        viewModel.campaignFollowStatusLiveData.observeAwaitValue()

        coVerify { getCampaignNplTncUseCase.executeOnBackground() }
        coVerify { getFollowStatusUseCase.executeOnBackground() }

        assert(viewModel.campaignTncLiveData.value is Success)
        assert(viewModel.campaignFollowStatusLiveData.value is Success)
    }

    @Test
    fun `check whether _campaignTncLiveData post fail data`() {
        val sampleCampaignId = "1234"
        val shopId = "0"
        coEvery { getCampaignNplTncUseCase.executeOnBackground() } throws Exception()
        viewModel.getTncBottomSheetData(sampleCampaignId, shopId, false)
        coVerify { getCampaignNplTncUseCase.executeOnBackground() }
        assert(viewModel.campaignTncLiveData.value is Fail)
    }

    @Test
    fun `check whether userId return same value as mocked userSession userId value`() {
        val mockUserId = "123"
        every { userSession.userId } returns mockUserId
        assert(viewModel.userId == mockUserId)
    }

    @Test
    fun `check whether userId return empty string when userSession userId return null`() {
        every { userSession.userId } returns null
        assert(viewModel.userId == "")
    }

    @Test
    fun `check whether isUserLoggedIn return same value as mocked userSession isLoggedIn value`() {
        every { userSession.isLoggedIn } returns true
        assert(viewModel.isUserLoggedIn)
    }

    @Test
    fun `check whether _followUnfollowShopLiveData post success data`() {
        val shopId = "123"
        every { userSession.isLoggedIn } returns true
        coEvery { updateFollowStatusUseCase.executeOnBackground() } returns FollowShopResponse(FollowShop(true,null,null,null, null, null))
        viewModel.updateFollowStatus(shopId, UpdateFollowStatusUseCase.ACTION_FOLLOW)
        coVerify { updateFollowStatusUseCase.executeOnBackground() }
        assert(viewModel.followUnfollowShopLiveData.value is Success)
    }

    @Test
    fun `check whether _followUnfollowShopLiveData post fail data when usecase throw error`() {
        val shopId = "123"
        every { userSession.isLoggedIn } returns true
        coEvery { updateFollowStatusUseCase.executeOnBackground() } throws Throwable()
        viewModel.updateFollowStatus(shopId, UpdateFollowStatusUseCase.ACTION_FOLLOW)
        coVerify { updateFollowStatusUseCase.executeOnBackground() }
        assert(viewModel.followUnfollowShopLiveData.value is Fail)
    }

    @Test
    fun `check whether _followUnfollowShopLiveData post fail data when user not logged in`() {
        val shopId = "123"
        every { userSession.isLoggedIn } returns false
        viewModel.updateFollowStatus(shopId, UpdateFollowStatusUseCase.ACTION_FOLLOW)
        verify { userSession.isLoggedIn }
        assert(viewModel.followUnfollowShopLiveData.value is Fail)
    }


}