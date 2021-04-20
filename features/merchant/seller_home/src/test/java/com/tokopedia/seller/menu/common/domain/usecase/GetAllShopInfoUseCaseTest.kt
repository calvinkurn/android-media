package com.tokopedia.seller.menu.common.domain.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.unit.test.rule.CoroutineTestRule
import com.tokopedia.seller.menu.common.domain.entity.OthersBalance
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
    lateinit var shopStatusTypeUseCase: ShopStatusTypeUseCase

    @RelaxedMockK
    lateinit var topAdsAutoTypeUseCase: TopAdsAutoTopupUseCase

    @RelaxedMockK
    lateinit var topAdsDashboardDepositUseCase: TopAdsDashboardDepositUseCase

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
                shopStatusTypeUseCase,
                topAdsAutoTypeUseCase,
                topAdsDashboardDepositUseCase,
                coroutineTestRule.dispatchers
        )
    }

    @Test
    fun `success get all shop info`() = runBlocking {
        val balanceSuccess = OthersBalance()
        val shopTypeSuccess = PowerMerchantStatus.Active
        val totalFollowersSuccess = anyLong()
        val shopBadgeUrlSuccess = anyString()
        val topAdsDepositSuccess = anyFloat()
        val isTopAdsAutoTopupSuccess = anyBoolean()

        val partialTopAdsInfo = PartialSettingSuccessInfoType.PartialTopAdsSettingSuccessInfo(
                balanceSuccess,
                topAdsDepositSuccess,
                isTopAdsAutoTopupSuccess
        )

        val partialShopInfo = PartialSettingSuccessInfoType.PartialShopSettingSuccessInfo(
                shopTypeSuccess,
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
            shopStatusTypeUseCase.executeOnBackground()
        } returns shopTypeSuccess

        coEvery {
            getShopTotalFollowersUseCase.executeOnBackground()
        } returns totalFollowersSuccess

        coEvery {
            getShopBadgeUseCase.executeOnBackground()
        } returns shopBadgeUrlSuccess

        coEvery {
            topAdsDashboardDepositUseCase.executeOnBackground()
        } returns topAdsDepositSuccess

        coEvery {
            topAdsAutoTypeUseCase.executeOnBackground()
        } returns isTopAdsAutoTopupSuccess

        val allShopInfo = mGetAllShopInfoUseCase.executeOnBackground()

        coVerify {
            balanceInfoUseCase.executeOnBackground()
            shopStatusTypeUseCase.executeOnBackground()
            getShopTotalFollowersUseCase.executeOnBackground()
            getShopBadgeUseCase.executeOnBackground()
            topAdsDashboardDepositUseCase.executeOnBackground()
            topAdsAutoTypeUseCase.executeOnBackground()
        }

        assertEquals(allShopInfo, allShopInfoSuccess)
    }

}