package com.tokopedia.homenav.mainnav.interactor

import com.tokopedia.homenav.base.viewmodel.HomeNavMenuViewModel
import com.tokopedia.homenav.base.viewmodel.HomeNavTickerViewModel
import com.tokopedia.homenav.mainnav.domain.model.NavNotificationModel
import com.tokopedia.homenav.mainnav.view.presenter.MainNavViewModel
import com.tokopedia.homenav.common.util.ClientMenuGenerator
import com.tokopedia.homenav.mainnav.data.pojo.shop.ShopInfoPojo
import com.tokopedia.homenav.mainnav.domain.usecases.*
import com.tokopedia.homenav.mainnav.view.viewmodel.AccountHeaderViewModel
import com.tokopedia.homenav.rule.TestDispatcherProvider
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import dagger.Lazy
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk

fun createViewModel (
        getProfileDataUseCase: GetProfileDataUseCase? = null,
        getProfileDataCacheUseCase: GetProfileDataCacheUseCase? = null,
        getBuListUseCase: GetCategoryGroupUseCase? = null,
        dispatchers: TestDispatcherProvider = TestDispatcherProvider(),
        userSession: UserSessionInterface? = null,
        clientMenuGenerator: ClientMenuGenerator? = null,
        getNavNotification: GetNavNotification? = null,
        getUohOrdersNavUseCase: GetUohOrdersNavUseCase? = null,
        getPaymentOrdersNavUseCase: GetPaymentOrdersNavUseCase? = null,
        getShopInfoUseCase: GetShopInfoUseCase? = null
): MainNavViewModel {
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
        coEvery { it.executeOnBackground() }.answers { AccountHeaderViewModel() }
    }
    val getProfileDataCacheUseCaseMock = getOrUseDefault(getProfileDataCacheUseCase) {
        coEvery { it.executeOnBackground() }.answers { AccountHeaderViewModel() }
    }
    val getBuListDataUseCaseMock = getOrUseDefault(getBuListUseCase) {
        coEvery { it.executeOnBackground() }.answers { listOf() }
    }
    val getShopInfoUseCaseMock = getOrUseDefault(getShopInfoUseCase) {
        coEvery { it.executeOnBackground() }.answers { Success((ShopInfoPojo.Response()).userShopInfo) }
    }

    return MainNavViewModel(
            baseDispatcher = Lazy {dispatchers },
            clientMenuGenerator = clientMenuGeneratorMock,
            userSession = userSessionMock,
            getNavNotification = getNavNotificationMock,
            getUohOrdersNavUseCase = getUohOrdersNavUseCaseMock,
            getPaymentOrdersNavUseCase = getPaymentOrdersNavUseCaseMock,
            getProfileDataUseCase = getProfileDataUseCaseMock,
            getCategoryGroupUseCase = getBuListDataUseCaseMock,
            getProfileDataCacheUseCase = getProfileDataCacheUseCaseMock,
            getShopInfoUseCase = getShopInfoUseCaseMock
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