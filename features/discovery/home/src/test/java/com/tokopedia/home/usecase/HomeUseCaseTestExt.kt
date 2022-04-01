package com.tokopedia.home.usecase

import com.tokopedia.home.beranda.domain.interactor.InjectCouponTimeBasedUseCase
import com.tokopedia.home.beranda.domain.interactor.repository.HomeFlagRepository
import com.tokopedia.home.beranda.domain.interactor.repository.HomeTokopointsListRepository
import com.tokopedia.home.beranda.domain.interactor.repository.HomeWalletAppRepository
import com.tokopedia.home.beranda.domain.interactor.usecase.HomeBalanceWidgetUseCase
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.coEvery
import io.mockk.mockk

fun createBalanceWidgetUseCase(
    homeWalletAppRepository: HomeWalletAppRepository = mockk(relaxed = true),
    homeTokopointsListRepository: HomeTokopointsListRepository = mockk(relaxed = true),
    homeFlagRepository: HomeFlagRepository = mockk(relaxed = true),
    userSessionInterface: UserSessionInterface = createDefaultLoggedInUserSession(),
    injectCouponTimeBasedUseCase: InjectCouponTimeBasedUseCase = mockk(relaxed = true)
    ): HomeBalanceWidgetUseCase {
    return HomeBalanceWidgetUseCase(
        homeWalletAppRepository = homeWalletAppRepository,
        homeTokopointsListRepository = homeTokopointsListRepository,
        homeFlagRepository = homeFlagRepository,
        userSession = userSessionInterface,
        injectCouponTimeBasedUseCase = injectCouponTimeBasedUseCase
    )
}

fun createDefaultLoggedInUserSession(): UserSessionInterface {
    val mockUserSession = mockk<UserSessionInterface>()
    coEvery {
        mockUserSession.isLoggedIn
    } returns true
    return mockUserSession
}
