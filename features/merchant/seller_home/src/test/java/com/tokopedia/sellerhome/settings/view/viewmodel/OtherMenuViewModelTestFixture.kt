package com.tokopedia.sellerhome.settings.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.gm.common.domain.interactor.GetShopCreatedInfoUseCase
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.seller.menu.common.domain.usecase.*
import com.tokopedia.sellerhome.domain.usecase.GetShopOperationalUseCase
import com.tokopedia.sellerhome.domain.usecase.ShareInfoOtherUseCase
import com.tokopedia.sellerhome.domain.usecase.TopAdsAutoTopupUseCase
import com.tokopedia.sellerhome.domain.usecase.TopAdsDashboardDepositUseCase
import com.tokopedia.shop.common.graphql.domain.usecase.GetTokoPlusBadgeUseCase
import com.tokopedia.sellerhome.domain.usecase.*
import com.tokopedia.sellerhomecommon.domain.usecase.GetNewPromotionUseCase
import com.tokopedia.sellerhomecommon.domain.usecase.GetTopAdsAutoAdsUseCase
import com.tokopedia.sellerhomecommon.domain.usecase.GetTopAdsShopInfoUseCase
import com.tokopedia.unit.test.rule.CoroutineTestRule
import com.tokopedia.unit.test.rule.StandardTestRule
import com.tokopedia.unit.test.rule.UnconfinedTestRule
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
    lateinit var getTokoPlusBadgeUseCase: GetTokoPlusBadgeUseCase

    @RelaxedMockK
    lateinit var getShopOperationalUseCase: GetShopOperationalUseCase

    @RelaxedMockK
    lateinit var getShopCreatedInfoUseCase: GetShopCreatedInfoUseCase

    @RelaxedMockK
    lateinit var balanceInfoUseCase: BalanceInfoUseCase

    @RelaxedMockK
    lateinit var getShopBadgeUseCase: GetShopBadgeUseCase

    @RelaxedMockK
    lateinit var getTotalTokoMemberUseCase: GetTotalTokoMemberUseCase

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

    @RelaxedMockK
    lateinit var getNewPromotionUseCase: GetNewPromotionUseCase

    @RelaxedMockK
    lateinit var getTopAdsShopInfoUseCase: GetTopAdsShopInfoUseCase

    @RelaxedMockK
    lateinit var getTopAdsAutoAdsUseCase: GetTopAdsAutoAdsUseCase

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineTestRule = UnconfinedTestRule()

    protected lateinit var mViewModel: OtherMenuViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        mViewModel =
            OtherMenuViewModel(
                coroutineTestRule.dispatchers,
                getTokoPlusBadgeUseCase,
                getShopOperationalUseCase,
                getShopCreatedInfoUseCase,
                balanceInfoUseCase,
                getShopBadgeUseCase,
                getShopTotalFollowersUseCase,
                getTotalTokoMemberUseCase,
                getUserShopInfoUseCase,
                topAdsAutoTopupUseCase,
                topAdsDashboardDepositUseCase,
                shopShareInfoUseCase,
                getNewPromotionUseCase,
                getTopAdsShopInfoUseCase,
                getTopAdsAutoAdsUseCase,
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
