package com.tokopedia.homenav.mainnav.interactor

import com.tokopedia.homenav.base.viewmodel.HomeNavMenuViewModel
import com.tokopedia.homenav.base.viewmodel.HomeNavTickerViewModel
import com.tokopedia.homenav.mainnav.data.pojo.user.ProfilePojo
import com.tokopedia.homenav.mainnav.data.pojo.user.UserPojo
import com.tokopedia.homenav.mainnav.domain.model.NavNotificationModel
import com.tokopedia.homenav.mainnav.view.presenter.MainNavViewModel
import com.tokopedia.homenav.common.util.ClientMenuGenerator
import com.tokopedia.homenav.mainnav.domain.usecases.*
import com.tokopedia.homenav.mainnav.view.viewmodel.AccountHeaderViewModel
import com.tokopedia.homenav.mainnav.view.viewmodel.MainNavigationDataModel
import com.tokopedia.homenav.rule.TestDispatcherProvider
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import dagger.Lazy
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk

fun createViewModel (
        getWalletBalanceUseCase: GetCoroutineWalletBalanceUseCase? = null,
        getUserMembershipUseCase: GetUserMembershipUseCase? = null,
        getShopInfoUseCase: GetShopInfoUseCase? = null,
        getMainNavDataUseCase: GetMainNavDataUseCase? = null,
        dispatchers: TestDispatcherProvider = TestDispatcherProvider(),
        userSession: UserSessionInterface? = null,
        clientMenuGenerator: ClientMenuGenerator? = null,
        getSaldoUseCase: GetSaldoUseCase? = null,
        getNavNotification: GetNavNotification? = null,
        getUohOrdersNavUseCase: GetUohOrdersNavUseCase? = null,
        getPaymentOrdersNavUseCase: GetPaymentOrdersNavUseCase? = null,
        getUserInfoUseCase: GetUserInfoUseCase? = null
): MainNavViewModel {
    val getUserInfoUseCaseMock = getOrUseDefault(getUserInfoUseCase) {}
    val getWalletBalanceUseCaseMock = getOrUseDefault(getWalletBalanceUseCase) {}
    val getUserMembershipUseCaseMock = getOrUseDefault(getUserMembershipUseCase) {}
    val getShopInfoUseCaseMock = getOrUseDefault(getShopInfoUseCase) {}
    val getMainNavDataUseCaseMock = getOrUseDefault(getMainNavDataUseCase) {
        coEvery { it.executeOnBackground() }.answers { MainNavigationDataModel() }
    }
    val userSessionMock = getOrUseDefault(userSession) {
        every { it.isLoggedIn } returns true
        every { it.hasShop() } returns true
    }
    val clientMenuGeneratorMock = getOrUseDefault(clientMenuGenerator) {
        every { it.getMenu(menuId = any(), notifCount = any(), sectionId = any()) }
                .answers { HomeNavMenuViewModel(id = firstArg(), notifCount = secondArg(), sectionId = thirdArg()) }
        every { it.getTicker(menuId = any()) }
                .answers { HomeNavTickerViewModel() }
    }
    val getSaldoUseCaseMock = getOrUseDefault(getSaldoUseCase) {}
    val getNavNotificationMock = getOrUseDefault(getNavNotification) {
        coEvery { it.executeOnBackground() }.answers { NavNotificationModel(0) }
    }
    val getUohOrdersNavUseCaseMock = getOrUseDefault(getUohOrdersNavUseCase) {
        coEvery { it.executeOnBackground() }.answers { listOf() }
    }
    val getPaymentOrdersNavUseCaseMock = getOrUseDefault(getPaymentOrdersNavUseCase) {
        coEvery { it.executeOnBackground() }.answers { listOf() }
    }

    return MainNavViewModel(
            baseDispatcher = Lazy {dispatchers },
            getShopInfoUseCase = getShopInfoUseCaseMock,
            getUserMembershipUseCase = getUserMembershipUseCaseMock,
            getWalletUseCase = getWalletBalanceUseCaseMock,
            getSaldoUseCase = getSaldoUseCaseMock,
            clientMenuGenerator = clientMenuGeneratorMock,
            userSession = userSessionMock,
            getMainNavDataUseCase = getMainNavDataUseCaseMock,
            getNavNotification = getNavNotificationMock,
            getUohOrdersNavUseCase = getUohOrdersNavUseCaseMock,
            getPaymentOrdersNavUseCase = getPaymentOrdersNavUseCaseMock,
            getUserInfoUseCase = getUserInfoUseCaseMock
    )
}

inline fun <reified T : Any> getOrUseDefault(any: T?, runObjectMockSetup: (obj: T)->Unit): Lazy<T> {
    return if (any == null) {
        val mockObject = mockk<T>(relaxed = true)
        runObjectMockSetup.invoke(mockObject)
        Lazy { mockObject }
    } else {
        Lazy { any!! }
    }
}

fun GetUserInfoUseCase.getBasicData() {
    coEvery {
        executeOnBackground()
    } returns Success(UserPojo(ProfilePojo(name = "Joko", profilePicture = "Tingkir")))
}

fun GetMainNavDataUseCase.getBasicData() {
    coEvery {
        executeOnBackground()
    } returns MainNavigationDataModel(listOf(AccountHeaderViewModel(userName = "Joko", userImage = "Tingkir")))
}