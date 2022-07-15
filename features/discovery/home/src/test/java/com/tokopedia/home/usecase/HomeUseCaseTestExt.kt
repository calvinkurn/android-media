package com.tokopedia.home.usecase

import com.tokopedia.home.beranda.data.model.GetHomeBalanceItem
import com.tokopedia.home.beranda.data.model.GetHomeBalanceList
import com.tokopedia.home.beranda.data.model.GetHomeBalanceWidgetData
import com.tokopedia.home.beranda.domain.interactor.InjectCouponTimeBasedUseCase
import com.tokopedia.home.beranda.domain.interactor.repository.GetHomeBalanceWidgetRepository
import com.tokopedia.home.beranda.domain.interactor.repository.HomeTokopointsListRepository
import com.tokopedia.home.beranda.domain.interactor.repository.HomeWalletAppRepository
import com.tokopedia.home.beranda.domain.interactor.usecase.HomeBalanceWidgetUseCase
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.coEvery
import io.mockk.mockk

fun createBalanceWidgetUseCase(
    homeWalletAppRepository: HomeWalletAppRepository = mockk(relaxed = true),
    homeTokopointsListRepository: HomeTokopointsListRepository = mockk(relaxed = true),
    userSessionInterface: UserSessionInterface = createDefaultLoggedInUserSession(),
    injectCouponTimeBasedUseCase: InjectCouponTimeBasedUseCase = mockk(relaxed = true),
    getHomeBalanceWidgetRepository: GetHomeBalanceWidgetRepository = mockk(relaxed = true)
    ): HomeBalanceWidgetUseCase {
    return HomeBalanceWidgetUseCase(
        homeWalletAppRepository = homeWalletAppRepository,
        homeTokopointsListRepository = homeTokopointsListRepository,
        userSession = userSessionInterface,
        injectCouponTimeBasedUseCase = injectCouponTimeBasedUseCase,
        getHomeBalanceWidgetRepository = createDummyGetHomeBalanceWidgetRepository()
    )
}

fun createDefaultLoggedInUserSession(): UserSessionInterface {
    val mockUserSession = mockk<UserSessionInterface>()
    coEvery {
        mockUserSession.isLoggedIn
    } returns true
    return mockUserSession
}

fun createDummyGetHomeBalanceWidgetRepository(): GetHomeBalanceWidgetRepository {
    val getHomeBalanceWidgetRepository: GetHomeBalanceWidgetRepository = mockk(relaxUnitFun = true)
    val mockBalanceWidgetData = GetHomeBalanceWidgetData(
            getHomeBalanceList = GetHomeBalanceList(
                balancesList = mutableListOf(GetHomeBalanceItem(title = "Gopay", type = "gopay"))
            )
        )
    coEvery { getHomeBalanceWidgetRepository.getRemoteData() } returns mockBalanceWidgetData
    return getHomeBalanceWidgetRepository
}
