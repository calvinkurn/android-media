package com.tokopedia.homenav.mainnav.interactor

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartUseCase
import com.tokopedia.homenav.base.datamodel.HomeNavMenuDataModel
import com.tokopedia.homenav.base.datamodel.HomeNavTickerDataModel
import com.tokopedia.homenav.base.datamodel.HomeNavTitleDataModel
import com.tokopedia.homenav.common.util.ClientMenuGenerator
import com.tokopedia.homenav.mainnav.data.mapper.AccountHeaderMapper
import com.tokopedia.homenav.mainnav.data.pojo.membership.MembershipPojo
import com.tokopedia.homenav.mainnav.data.pojo.saldo.SaldoPojo
import com.tokopedia.homenav.mainnav.data.pojo.tokopoint.TokopointsStatusFilteredPojo
import com.tokopedia.homenav.mainnav.data.pojo.user.UserPojo
import com.tokopedia.homenav.mainnav.domain.model.AffiliateUserDetailData
import com.tokopedia.homenav.mainnav.domain.model.NavNotificationModel
import com.tokopedia.homenav.mainnav.domain.usecases.GetAffiliateUserUseCase
import com.tokopedia.homenav.mainnav.domain.usecases.GetNavNotification
import com.tokopedia.homenav.mainnav.domain.usecases.GetPaymentOrdersNavUseCase
import com.tokopedia.homenav.mainnav.domain.usecases.GetProfileDataUseCase
import com.tokopedia.homenav.mainnav.domain.usecases.GetReviewProductUseCase
import com.tokopedia.homenav.mainnav.domain.usecases.GetSaldoUseCase
import com.tokopedia.homenav.mainnav.domain.usecases.GetShopInfoUseCase
import com.tokopedia.homenav.mainnav.domain.usecases.GetTokopointStatusFiltered
import com.tokopedia.homenav.mainnav.domain.usecases.GetUohOrdersNavUseCase
import com.tokopedia.homenav.mainnav.domain.usecases.GetUserInfoUseCase
import com.tokopedia.homenav.mainnav.domain.usecases.GetUserMembershipUseCase
import com.tokopedia.homenav.mainnav.view.datamodel.account.AccountHeaderDataModel
import com.tokopedia.homenav.mainnav.view.presenter.MainNavViewModel
import com.tokopedia.navigation_common.model.wallet.WalletStatus
import com.tokopedia.navigation_common.usecase.GetWalletAppBalanceUseCase
import com.tokopedia.navigation_common.usecase.GetWalletEligibilityUseCase
import com.tokopedia.navigation_common.usecase.pojo.walletapp.WalletAppData
import com.tokopedia.recommendation_widget_common.domain.coroutines.GetRecommendationUseCase
import com.tokopedia.sessioncommon.data.admin.AdminDataResponse
import com.tokopedia.sessioncommon.data.profile.ShopData
import com.tokopedia.sessioncommon.domain.usecase.AccountAdminInfoUseCase
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.coroutines.UseCase
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.usercomponents.tokopediaplus.domain.TokopediaPlusResponseDataModel
import com.tokopedia.usercomponents.tokopediaplus.domain.TokopediaPlusUseCase
import dagger.Lazy
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk

fun createViewModel(
    getProfileDataUseCase: GetProfileDataUseCase? = null,
    dispatchers: CoroutineDispatchers = CoroutineTestDispatchersProvider,
    userSession: UserSessionInterface? = null,
    clientMenuGenerator: ClientMenuGenerator? = null,
    getNavNotification: GetNavNotification? = null,
    getUohOrdersNavUseCase: GetUohOrdersNavUseCase? = null,
    getPaymentOrdersNavUseCase: GetPaymentOrdersNavUseCase? = null,
    getShopInfoUseCase: GetShopInfoUseCase? = null,
    accountAdminInfoUseCase: AccountAdminInfoUseCase? = null,
    getAffiliateUserUseCase: GetAffiliateUserUseCase? = null,
    getReviewProductUseCase: GetReviewProductUseCase? = null,
    getTokopediaPlusUseCase: TokopediaPlusUseCase? = null,
    getRecommendationUseCase: GetRecommendationUseCase? = null,
    addToCartUseCase: AddToCartUseCase? = null,
): MainNavViewModel {
    val userSessionMock = getOrUseDefault(userSession) {
        every { it.isLoggedIn } returns true
        every { it.hasShop() } returns true
    }
    val clientMenuGeneratorMock = getOrUseDefault(clientMenuGenerator) {
        every { it.getMenu(menuId = any(), notifCount = any(), sectionId = any(), showCta = any()) }
            .answers { HomeNavMenuDataModel(id = arg(0), notifCount = arg(1), sectionId = arg(2), showCta = arg(3)) }
        every { it.getTicker(menuId = any()) }
            .answers { HomeNavTickerDataModel() }
        every { it.getSectionTitle(identifier = any()) }
            .answers { HomeNavTitleDataModel(identifier = firstArg()) }
    }
    val getNavNotificationMock = getOrUseDefault(getNavNotification) {
        coEvery { it.executeOnBackground() }.answers { NavNotificationModel(0) }
    }
    val getUohOrdersNavUseCaseMock = getOrUseDefault(getUohOrdersNavUseCase) {
        coEvery { it.executeOnBackground() }.answers { listOf() }
    }
    val getPaymentOrdersNavUseCaseMock = getOrUseDefault(getPaymentOrdersNavUseCase) {
        coEvery { it.executeOnBackground() }.answers { listOf() }
    }
    val getProfileDataUseCaseMock = getOrUseDefault(getProfileDataUseCase) {
        coEvery { it.executeOnBackground() }.answers { AccountHeaderDataModel() }
    }
    val getShopInfoUseCaseMock = getOrUseDefault(getShopInfoUseCase) {
        coEvery { it.executeOnBackground() }.answers { Success(com.tokopedia.homenav.mainnav.data.pojo.shop.ShopData()) }
    }
    val accountAdminInfoUseCaseMock = getOrUseDefault(accountAdminInfoUseCase) {
        coEvery { it(any()) }.answers { Pair(AdminDataResponse(), ShopData()) }
    }

    val getAffiliateUserUseCaseMock = getOrUseDefault(getAffiliateUserUseCase) {
        coEvery { it.executeOnBackground() }.answers { Success(AffiliateUserDetailData()) }
    }

    val getTokopediaPlusUseCaseMock = getOrUseDefault(getTokopediaPlusUseCase) {
        coEvery { it.invoke(any()) }.answers { TokopediaPlusResponseDataModel() }
    }

    val getReviewProductUseCaseMock = getOrUseDefault(getReviewProductUseCase) {
        coEvery { it.executeOnBackground() }.answers { listOf() }
    }

    val getRecommendationUseCaseMock = getOrUseDefault(getRecommendationUseCase) {
        coEvery { it.getData(any()) }.answers { listOf() }
    }

    val addToCartUseCaseMock = getOrUseDefault(addToCartUseCase) {
        coEvery { it.executeOnBackground() }.answers { AddToCartDataModel() }
    }

    return spyk(
        MainNavViewModel(
            baseDispatcher = Lazy { dispatchers },
            clientMenuGenerator = clientMenuGeneratorMock,
            userSession = userSessionMock,
            getNavNotification = getNavNotificationMock,
            getUohOrdersNavUseCase = getUohOrdersNavUseCaseMock,
            getPaymentOrdersNavUseCase = getPaymentOrdersNavUseCaseMock,
            getProfileDataUseCase = getProfileDataUseCaseMock,
            getShopInfoUseCase = getShopInfoUseCaseMock,
            accountAdminInfoUseCase = accountAdminInfoUseCaseMock,
            getAffiliateUserUseCase = getAffiliateUserUseCaseMock,
            getTokopediaPlusUseCase = getTokopediaPlusUseCaseMock,
            getReviewProductUseCase = getReviewProductUseCaseMock,
            getRecommendationUseCase = getRecommendationUseCaseMock,
            addToCartUseCase = addToCartUseCaseMock
        ),
        recordPrivateCalls = true
    )
}

fun createProfileDataUseCase(
    userSession: UserSessionInterface? = null,
    getUserInfoUseCase: GetUserInfoUseCase? = null,
    getSaldoUseCase: GetSaldoUseCase? = null,
    getUserMembershipUseCase: GetUserMembershipUseCase? = null,
    getTokopointStatusFiltered: GetTokopointStatusFiltered? = null,
    getShopInfoUseCase: GetShopInfoUseCase? = null,
    getWalletEligibilityUseCase: GetWalletEligibilityUseCase? = null,
    getWalletAppBalanceUseCase: GetWalletAppBalanceUseCase? = null,
    getAffiliateUserUseCase: GetAffiliateUserUseCase? = null,
    getTokopediaPlusUseCase: TokopediaPlusUseCase? = null
): UseCase<AccountHeaderDataModel> {
    val userSessionMock = getOrUseDefault(userSession) {
        every { it.isLoggedIn } returns true
        every { it.hasShop() } returns true
    }
    val accountHeaderMapper = AccountHeaderMapper(userSessionMock.get())

    val getUserInfoUseCaseMock = getOrUseDefault(getUserInfoUseCase) {
        coEvery { it.executeOnBackground() }.answers { Success(UserPojo()) }
    }
    val getSaldoUseCaseMock = getOrUseDefault(getSaldoUseCase) {
        coEvery { it.executeOnBackground() }.answers { Success(SaldoPojo()) }
    }
    val getUserMembershipUseCaseMock = getOrUseDefault(getUserMembershipUseCase) {
        coEvery { it.executeOnBackground() }.answers { Success(MembershipPojo()) }
    }
    val getTokopointStatusFilteredMock = getOrUseDefault(getTokopointStatusFiltered) {
        coEvery { it.executeOnBackground() }.answers { Success(TokopointsStatusFilteredPojo()) }
    }
    val getShopInfoUseCaseMock = getOrUseDefault(getShopInfoUseCase) {
        coEvery { it.executeOnBackground() }.answers { Success(com.tokopedia.homenav.mainnav.data.pojo.shop.ShopData()) }
    }
    val getWalletEligibilityUseCaseMock = getOrUseDefault(getWalletEligibilityUseCase) {
        coEvery { it.executeOnBackground() }.answers { WalletStatus() }
    }
    val getWalletAppBalanceMock = getOrUseDefault(getWalletAppBalanceUseCase) {
        coEvery { it.executeOnBackground() }.answers { WalletAppData() }
    }
    val getAffiliateUserUseCaseMock = getOrUseDefault(getAffiliateUserUseCase) {
        coEvery { it.executeOnBackground() }.answers { Success(AffiliateUserDetailData()) }
    }
    val getTokopediaPlusUseCaseMock = getOrUseDefault(getTokopediaPlusUseCase) {
        coEvery { it.invoke(any()) }.answers { TokopediaPlusResponseDataModel() }
    }

    return GetProfileDataUseCase(
        accountHeaderMapper = accountHeaderMapper,
        getUserInfoUseCase = getUserInfoUseCaseMock.get(),
        getSaldoUseCase = getSaldoUseCaseMock.get(),
        getUserMembershipUseCase = getUserMembershipUseCaseMock.get(),
        getTokopointStatusFiltered = getTokopointStatusFilteredMock.get(),
        getShopInfoUseCase = getShopInfoUseCaseMock.get(),
        getWalletAppBalanceUseCase = getWalletAppBalanceMock.get(),
        getWalletEligibilityUseCase = getWalletEligibilityUseCaseMock.get(),
        getAffiliateUserUseCase = getAffiliateUserUseCaseMock.get(),
        getTokopediaPlusUseCase = getTokopediaPlusUseCaseMock.get()
    )
}

inline fun <reified T : Any> getOrUseDefault(any: T?, runObjectMockSetup: (obj: T) -> Unit): Lazy<T> {
    return if (any == null) {
        val mockObject = mockk<T>(relaxed = true)
        runObjectMockSetup.invoke(mockObject)
        Lazy { mockObject }
    } else {
        Lazy { any!! }
    }
}

fun getDefaultClientGeneratorMockValue(clientMenuGenerator: ClientMenuGenerator) {
    every { clientMenuGenerator.getMenu(menuId = any(), notifCount = any(), sectionId = any()) }
        .answers { HomeNavMenuDataModel(id = firstArg(), notifCount = secondArg(), sectionId = thirdArg()) }
    every { clientMenuGenerator.getTicker(menuId = any()) }
        .answers { HomeNavTickerDataModel() }
    every { clientMenuGenerator.getSectionTitle(identifier = any()) }
        .answers { (HomeNavTitleDataModel(identifier = firstArg())) }
}

internal fun getUserSession(login: Boolean): UserSessionInterface = mockk<UserSessionInterface>().apply {
    every { isLoggedIn } returns login
}
