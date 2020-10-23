package com.tokopedia.homenav.mainnav.interactor

import com.tokopedia.homenav.mainnav.data.pojo.user.ProfilePojo
import com.tokopedia.homenav.mainnav.data.pojo.user.UserPojo
import com.tokopedia.homenav.mainnav.domain.interactor.GetCoroutineWalletBalanceUseCase
import com.tokopedia.homenav.mainnav.domain.interactor.GetShopInfoUseCase
import com.tokopedia.homenav.mainnav.domain.interactor.GetUserInfoUseCase
import com.tokopedia.homenav.mainnav.domain.interactor.GetUserMembershipUseCase
import com.tokopedia.homenav.mainnav.view.presenter.MainNavViewModel
import com.tokopedia.homenav.rule.TestDispatcherProvider
import dagger.Lazy
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk

fun createViewModel (
        getUserInfoUseCase: GetUserInfoUseCase = mockk(relaxed= true),
        getWalletBalanceUseCase: GetCoroutineWalletBalanceUseCase = mockk(relaxed = true),
        getUserMembershipUseCase: GetUserMembershipUseCase = mockk(relaxed = true),
        getShopInfoUseCase: GetShopInfoUseCase = mockk(relaxed = true),
        dispatchers: TestDispatcherProvider = TestDispatcherProvider()
): MainNavViewModel {
    return MainNavViewModel(
            baseDispatcher = Lazy {dispatchers },
            getShopInfoUseCase = Lazy { getShopInfoUseCase },
            getUserMembershipUseCase = Lazy { getUserMembershipUseCase },
            getWalletUseCase = Lazy { getWalletBalanceUseCase },
            getUserInfoUseCase = Lazy { getUserInfoUseCase }
    )
}

fun GetUserInfoUseCase.getBasicData() {
    coEvery {
        executeOnBackground()
    } returns UserPojo(ProfilePojo(name = "Joko", profilePicture = "Tingkir"))
}