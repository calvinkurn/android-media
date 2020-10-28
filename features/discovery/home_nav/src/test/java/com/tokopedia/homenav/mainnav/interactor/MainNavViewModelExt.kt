package com.tokopedia.homenav.mainnav.interactor

import com.tokopedia.homenav.mainnav.data.pojo.user.ProfilePojo
import com.tokopedia.homenav.mainnav.data.pojo.user.UserPojo
import com.tokopedia.homenav.mainnav.domain.interactor.*
import com.tokopedia.homenav.mainnav.view.presenter.MainNavViewModel
import com.tokopedia.homenav.mainnav.view.util.ClientMenuGenerator
import com.tokopedia.homenav.rule.TestDispatcherProvider
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Lazy
import io.mockk.coEvery
import io.mockk.mockk

fun createViewModel (
        getUserInfoUseCase: GetUserInfoUseCase = mockk(relaxed= true),
        getWalletBalanceUseCase: GetCoroutineWalletBalanceUseCase = mockk(relaxed = true),
        getUserMembershipUseCase: GetUserMembershipUseCase = mockk(relaxed = true),
        getShopInfoUseCase: GetShopInfoUseCase = mockk(relaxed = true),
        getMainNavDataUseCase: GetMainNavDataUseCase = mockk(relaxed = true),
        dispatchers: TestDispatcherProvider = TestDispatcherProvider(),
        userSession: UserSessionInterface = mockk(relaxed = true),
        clientMenuGenerator: ClientMenuGenerator = mockk(relaxed = true)
): MainNavViewModel {
    return MainNavViewModel(
            baseDispatcher = Lazy {dispatchers },
            getShopInfoUseCase = Lazy { getShopInfoUseCase },
            getUserMembershipUseCase = Lazy { getUserMembershipUseCase },
            getWalletUseCase = Lazy { getWalletBalanceUseCase },
            getUserInfoUseCase = Lazy { getUserInfoUseCase },
            clientMenuGenerator = Lazy { clientMenuGenerator },
            userSession = Lazy { userSession },
            getMainNavDataUseCase = Lazy { getMainNavDataUseCase }
    )
}

fun GetUserInfoUseCase.getBasicData() {
    coEvery {
        executeOnBackground()
    } returns UserPojo(ProfilePojo(name = "Joko", profilePicture = "Tingkir"))
}