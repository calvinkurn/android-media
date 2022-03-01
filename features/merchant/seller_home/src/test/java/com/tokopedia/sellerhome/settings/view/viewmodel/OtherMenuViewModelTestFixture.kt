package com.tokopedia.sellerhome.settings.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.gm.common.domain.interactor.GetShopCreatedInfoUseCase
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.seller.menu.common.domain.usecase.*
import com.tokopedia.sellerhome.domain.usecase.GetShopOperationalUseCase
import com.tokopedia.sellerhome.domain.usecase.ShareInfoOtherUseCase
import com.tokopedia.shop.common.domain.interactor.GetShopFreeShippingInfoUseCase
import com.tokopedia.unit.test.rule.CoroutineTestRule
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Before
import org.junit.Rule

@ExperimentalCoroutinesApi
abstract class OtherMenuViewModelTestFixture {

    @RelaxedMockK
    lateinit var getShopFreeShippingInfoUseCase: GetShopFreeShippingInfoUseCase

    @RelaxedMockK
    lateinit var getShopOperationalUseCase: GetShopOperationalUseCase

    @RelaxedMockK
    lateinit var getShopCreatedInfoUseCase: GetShopCreatedInfoUseCase

    @RelaxedMockK
    lateinit var balanceInfoUseCase: BalanceInfoUseCase

    @RelaxedMockK
    lateinit var getShopBadgeUseCase: GetShopBadgeUseCase

    @RelaxedMockK
    lateinit var getShopTotalFollowersUseCase: GetShopTotalFollowersUseCase

    @RelaxedMockK
    lateinit var getUserShopInfoUseCase: GetUserShopInfoUseCase

    @RelaxedMockK
    lateinit var topAdsAutoTopupUseCase: TopAdsAutoTopupUseCase

    @RelaxedMockK
    lateinit var topAdsDashboardDepositUseCase: TopAdsDashboardDepositUseCase

    @RelaxedMockK
    lateinit var shopShareInfoUseCase: ShareInfoOtherUseCase

    @RelaxedMockK
    lateinit var userSession: UserSessionInterface

    @RelaxedMockK
    lateinit var remoteConfig: FirebaseRemoteConfigImpl

    @RelaxedMockK
    lateinit var shouldShowMultipleErrorObserver: Observer<in Boolean>

    @RelaxedMockK
    lateinit var shouldSwipeSecondaryInfoObserver: Observer<in Boolean>

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    protected lateinit var mViewModel: OtherMenuViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        mViewModel =
            OtherMenuViewModel(
                coroutineTestRule.dispatchers,
                getShopFreeShippingInfoUseCase,
                getShopOperationalUseCase,
                getShopCreatedInfoUseCase,
                balanceInfoUseCase,
                getShopBadgeUseCase,
                getShopTotalFollowersUseCase,
                getUserShopInfoUseCase,
                topAdsAutoTopupUseCase,
                topAdsDashboardDepositUseCase,
                shopShareInfoUseCase,
                userSession,
                remoteConfig
            )

        mViewModel.run {
            shouldShowMultipleErrorToaster.observeForever(shouldShowMultipleErrorObserver)
            shouldSwipeSecondaryInfo.observeForever(shouldSwipeSecondaryInfoObserver)
        }
    }

    @After
    fun cleanUp() {
        mViewModel.run {
            shouldShowMultipleErrorToaster.removeObserver(shouldShowMultipleErrorObserver)
            shouldSwipeSecondaryInfo.removeObserver(shouldSwipeSecondaryInfoObserver)
        }
    }

}