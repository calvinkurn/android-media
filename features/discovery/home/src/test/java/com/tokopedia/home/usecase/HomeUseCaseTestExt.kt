package com.tokopedia.home.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.home.beranda.data.newatf.DynamicPositionRepository
import com.tokopedia.home.beranda.data.newatf.HomeAtfUseCase
import com.tokopedia.home.beranda.data.newatf.balance.BalanceWidgetUseCase
import com.tokopedia.home.beranda.data.newatf.banner.HomepageBannerRepository
import com.tokopedia.home.beranda.data.newatf.channel.AtfChannelRepository
import com.tokopedia.home.beranda.data.newatf.icon.DynamicIconRepository
import com.tokopedia.home.beranda.data.newatf.mission.MissionWidgetRepository
import com.tokopedia.home.beranda.data.newatf.ticker.TickerRepository
import com.tokopedia.home.beranda.data.newatf.todo.TodoWidgetRepository
import com.tokopedia.home.beranda.domain.interactor.InjectCouponTimeBasedUseCase
import com.tokopedia.home.beranda.domain.interactor.repository.GetHomeBalanceWidgetRepository
import com.tokopedia.home.beranda.domain.interactor.repository.HomeTokopointsListRepository
import com.tokopedia.home.beranda.domain.interactor.repository.HomeWalletAppRepository
import com.tokopedia.home.beranda.domain.interactor.usecase.HomeBalanceWidgetUseCase
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
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
        getHomeBalanceWidgetRepository = getHomeBalanceWidgetRepository
    )
}

fun createHomeAtfUseCase(
    homeDispatcher: CoroutineDispatchers = CoroutineTestDispatchersProvider,
    dynamicPositionRepository: DynamicPositionRepository = mockk(relaxed = true),
    homepageBannerRepository: HomepageBannerRepository = mockk(relaxed = true),
    dynamicIconRepository: DynamicIconRepository = mockk(relaxed = true),
    tickerRepository: TickerRepository = mockk(relaxed = true),
    atfChannelRepository: AtfChannelRepository = mockk(relaxed = true),
    missionWidgetRepository: MissionWidgetRepository = mockk(relaxed = true),
    todoWidgetRepository: TodoWidgetRepository = mockk(relaxed = true),
    balanceWidgetUseCase: BalanceWidgetUseCase = mockk(relaxed = true),
): HomeAtfUseCase {
    return HomeAtfUseCase(
        homeDispatcher = homeDispatcher,
        dynamicPositionRepository = dynamicPositionRepository,
        homepageBannerRepository = homepageBannerRepository,
        dynamicIconRepository = dynamicIconRepository,
        tickerRepository = tickerRepository,
        atfChannelRepository = atfChannelRepository,
        missionWidgetRepository = missionWidgetRepository,
        todoWidgetRepository = todoWidgetRepository,
        balanceWidgetUseCase = balanceWidgetUseCase
    )
}

fun createDefaultLoggedInUserSession(): UserSessionInterface {
    val mockUserSession = mockk<UserSessionInterface>()
    coEvery {
        mockUserSession.isLoggedIn
    } returns true
    return mockUserSession
}
