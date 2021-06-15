package com.tokopedia.homenav.mainnav.interactor

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.homenav.base.datamodel.HomeNavMenuDataModel
import com.tokopedia.homenav.base.datamodel.HomeNavTickerDataModel
import com.tokopedia.homenav.base.datamodel.HomeNavTitleDataModel
import com.tokopedia.homenav.mainnav.domain.model.NavNotificationModel
import com.tokopedia.homenav.mainnav.view.presenter.MainNavViewModel
import com.tokopedia.homenav.common.util.ClientMenuGenerator
import com.tokopedia.homenav.mainnav.domain.usecases.*
import com.tokopedia.homenav.mainnav.view.datamodel.AccountHeaderDataModel
import com.tokopedia.sessioncommon.data.admin.AdminDataResponse
import com.tokopedia.sessioncommon.data.profile.ShopData
import com.tokopedia.sessioncommon.domain.usecase.AccountAdminInfoUseCase
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
        dispatchers: CoroutineDispatchers = CoroutineTestDispatchersProvider,
        userSession: UserSessionInterface? = null,
        clientMenuGenerator: ClientMenuGenerator? = null,
        getNavNotification: GetNavNotification? = null,
        getUohOrdersNavUseCase: GetUohOrdersNavUseCase? = null,
        getPaymentOrdersNavUseCase: GetPaymentOrdersNavUseCase? = null,
        getShopInfoUseCase: GetShopInfoUseCase? = null,
        accountAdminInfoUseCase: AccountAdminInfoUseCase? = null
): MainNavViewModel {
    val userSessionMock = getOrUseDefault(userSession) {
        every { it.isLoggedIn } returns true
        every { it.hasShop() } returns true
    }
    val clientMenuGeneratorMock = getOrUseDefault(clientMenuGenerator) {
        every { it.getMenu(menuId = any(), notifCount = any(), sectionId = any()) }
                .answers { HomeNavMenuDataModel(id = firstArg(), notifCount = secondArg(), sectionId = thirdArg()) }
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
    val getProfileDataCacheUseCaseMock = getOrUseDefault(getProfileDataCacheUseCase) {
        coEvery { it.executeOnBackground() }.answers { AccountHeaderDataModel() }
    }
    val getBuListDataUseCaseMock = getOrUseDefault(getBuListUseCase) {
        coEvery { it.executeOnBackground() }.answers { listOf() }
    }
    val getShopInfoUseCaseMock = getOrUseDefault(getShopInfoUseCase) {
        coEvery { it.executeOnBackground() }.answers { Success(com.tokopedia.homenav.mainnav.data.pojo.shop.ShopData()) }
    }
    val accountAdminInfoUseCaseMock = getOrUseDefault(accountAdminInfoUseCase) {
        coEvery { it.executeOnBackground() }.answers { Pair(AdminDataResponse(), ShopData()) }
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
            getShopInfoUseCase = getShopInfoUseCaseMock,
            accountAdminInfoUseCase = accountAdminInfoUseCaseMock
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

fun getDefaultClientGeneratorMockValue(clientMenuGenerator: ClientMenuGenerator) {
    every { clientMenuGenerator.getMenu(menuId = any(), notifCount = any(), sectionId = any()) }
            .answers { HomeNavMenuDataModel(id = firstArg(), notifCount = secondArg(), sectionId = thirdArg()) }
    every { clientMenuGenerator.getTicker(menuId = any()) }
            .answers { HomeNavTickerDataModel() }
    every { clientMenuGenerator.getSectionTitle(identifier = any()) }
            .answers {(HomeNavTitleDataModel(identifier = firstArg()))}
}