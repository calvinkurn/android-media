package com.tokopedia.seller.menu.domain.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.unit.test.rule.CoroutineTestRule
import com.tokopedia.seller.menu.common.domain.entity.OthersBalance
import com.tokopedia.seller.menu.common.domain.usecase.BalanceInfoUseCase
import com.tokopedia.seller.menu.common.domain.usecase.GetShopBadgeUseCase
import com.tokopedia.seller.menu.common.domain.usecase.GetShopTotalFollowersUseCase
import com.tokopedia.seller.menu.common.domain.usecase.GetUserShopInfoUseCase
import com.tokopedia.seller.menu.common.view.uimodel.UserShopInfoWrapper
import com.tokopedia.seller.menu.common.view.uimodel.base.partialresponse.PartialSettingSuccessInfoType
import com.tokopedia.seller.menu.common.view.uimodel.base.PowerMerchantStatus
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Matchers.*

@ExperimentalCoroutinesApi
class GetAllShopInfoUseCaseTest {

    @RelaxedMockK
    lateinit var userSession : UserSessionInterface

    @RelaxedMockK
    lateinit var balanceInfoUseCase: BalanceInfoUseCase

    @RelaxedMockK
    lateinit var getShopBadgeUseCase: GetShopBadgeUseCase

    @RelaxedMockK
    lateinit var getShopTotalFollowersUseCase: GetShopTotalFollowersUseCase

    @RelaxedMockK
    lateinit var getUserShopInfoUseCase: GetUserShopInfoUseCase

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    private val mGetAllShopInfoUseCase by lazy {
        GetAllShopInfoUseCase(
            userSession,
            balanceInfoUseCase,
            getShopBadgeUseCase,
            getShopTotalFollowersUseCase,
            getUserShopInfoUseCase,
            coroutineTestRule.dispatchers
        )
    }

    @Test
    fun `success get all shop info`() = runBlocking {
        val balanceSuccess = OthersBalance()
        val shopTypeSuccess = PowerMerchantStatus.Active
        val userShopInfoWrapper = UserShopInfoWrapper(shopType = shopTypeSuccess)
        val totalFollowersSuccess = anyLong()
        val shopBadgeUrlSuccess = anyString()
        val topAdsDepositSuccess = anyFloat()

        val partialTopAdsInfo = PartialSettingSuccessInfoType.PartialTopAdsSettingSuccessInfo(
                balanceSuccess
        )

        val partialShopInfo = PartialSettingSuccessInfoType.PartialShopSettingSuccessInfo(
                userShopInfoWrapper,
                totalFollowersSuccess,
                shopBadgeUrlSuccess
        )

        val allShopInfoSuccess = Pair(partialShopInfo, partialTopAdsInfo)

        coEvery {
            userSession.shopId
        } returns "0"

        coEvery {
            balanceInfoUseCase.executeOnBackground()
        } returns balanceSuccess

        coEvery {
            getShopTotalFollowersUseCase.executeOnBackground()
        } returns totalFollowersSuccess

        coEvery {
            getShopBadgeUseCase.executeOnBackground()
        } returns shopBadgeUrlSuccess

        coEvery {
            getUserShopInfoUseCase.executeOnBackground()
        } returns userShopInfoWrapper

        val allShopInfo = mGetAllShopInfoUseCase.executeOnBackground()

        coVerify {
            balanceInfoUseCase.executeOnBackground()
            getShopTotalFollowersUseCase.executeOnBackground()
            getShopBadgeUseCase.executeOnBackground()
            getUserShopInfoUseCase.executeOnBackground()
        }

        assertEquals(allShopInfo, allShopInfoSuccess)
    }

}