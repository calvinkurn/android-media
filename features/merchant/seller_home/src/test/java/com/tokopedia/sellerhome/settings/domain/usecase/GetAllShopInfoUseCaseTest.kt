package com.tokopedia.sellerhome.settings.domain.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.kotlin.extensions.view.getCurrencyFormatted
import com.tokopedia.kotlin.extensions.view.toEmptyStringIfNull
import com.tokopedia.sellerhome.settings.domain.entity.ShopInfo
import com.tokopedia.sellerhome.settings.view.uimodel.base.PowerMerchantStatus
import com.tokopedia.sellerhome.settings.view.uimodel.base.ShopType
import com.tokopedia.sellerhome.settings.view.uimodel.shopinfo.SettingShopInfoUiModel
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
                topAdsDashboardDepositUseCase
        )
    }

    @Test
    fun `success get all shop info`() = runBlocking {
        val settingShopInfoSuccess = ShopInfo()
        val shopTypeSuccess = PowerMerchantStatus.Active
        val totalFollowersSuccess = anyInt()
        val shopBadgeUrlSuccess = anyString()
        val topAdsDepositSuccess = anyFloat()
        val isTopAdsAutoTopupSuccess = anyBoolean()

        val allShopInfoSuccess =
                SettingShopInfoUiModel(
                        settingShopInfoSuccess.shopInfoMoengage?.info?.shopName.toEmptyStringIfNull(),
                        settingShopInfoSuccess.shopInfoMoengage?.info?.shopAvatar.toEmptyStringIfNull(),
                        shopTypeSuccess,
                        settingShopInfoSuccess.balance?.totalBalance ?: "",
                        topAdsDepositSuccess.getCurrencyFormatted(),
                        isTopAdsAutoTopupSuccess,
                        shopBadgeUrlSuccess,
                        totalFollowersSuccess,
                        userSession
                )

        coEvery {
            balanceInfoUseCase.executeOnBackground()
        } returns settingShopInfoSuccess

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
            mapToSettingShopInfo(
                    settingShopInfoSuccess,
                    shopTypeSuccess,
                    topAdsDepositSuccess,
                    isTopAdsAutoTopupSuccess,
                    totalFollowersSuccess,
                    shopBadgeUrlSuccess)
        }

        assertEquals(allShopInfo.shopName, allShopInfoSuccess.shopName)
        assertEquals(allShopInfo.saldoBalanceUiModel.balanceValue, allShopInfoSuccess.saldoBalanceUiModel.balanceValue)
        assertEquals(allShopInfo.shopBadgeUiModel.shopBadgeUrl, allShopInfoSuccess.shopBadgeUiModel.shopBadgeUrl)
        assertEquals(allShopInfo.shopFollowersUiModel.shopFollowers, allShopInfoSuccess.shopFollowersUiModel.shopFollowers)
        assertEquals(allShopInfo.shopStatusUiModel.shopType, allShopInfoSuccess.shopStatusUiModel.shopType)
        assertEquals(allShopInfo.topadsBalanceUiModel.isTopAdsUser, allShopInfoSuccess.topadsBalanceUiModel.isTopAdsUser)
        assertEquals(allShopInfo.shopAvatarUiModel.shopAvatarUrl, allShopInfoSuccess.shopAvatarUiModel.shopAvatarUrl)
    }

    private fun mapToSettingShopInfo(shopInfo: ShopInfo,
                             shopStatusType: ShopType,
                             topAdsBalance: Float,
                             isTopAdsAutoTopup: Boolean,
                             totalFollowers: Int,
                             shopBadge: String): SettingShopInfoUiModel {
        shopInfo.shopInfoMoengage?.run {
            return SettingShopInfoUiModel(
                info?.shopName.toEmptyStringIfNull(),
                info?.shopAvatar.toEmptyStringIfNull(),
                shopStatusType,
                shopInfo.balance?.totalBalance ?: "",
                topAdsBalance.getCurrencyFormatted(),
                isTopAdsAutoTopup,
                shopBadge,
                totalFollowers,
                userSession)
        }
        return SettingShopInfoUiModel(user = userSession)
    }

}