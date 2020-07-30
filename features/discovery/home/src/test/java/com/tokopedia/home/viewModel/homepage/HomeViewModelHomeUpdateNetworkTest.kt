package com.tokopedia.home.viewModel.homepage

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.common_wallet.balance.view.WalletBalanceModel
import com.tokopedia.home.beranda.data.usecase.HomeUseCase
import com.tokopedia.home.beranda.domain.interactor.GetCoroutineWalletBalanceUseCase
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.HomeDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.HomepageBannerDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.HeaderDataModel
import com.tokopedia.home.beranda.presentation.view.viewmodel.HomeHeaderWalletAction
import com.tokopedia.home.beranda.presentation.viewModel.HomeViewModel
import com.tokopedia.home.ext.observeOnce
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import org.junit.Rule
import org.junit.Test

class HomeViewModelHomeUpdateNetworkTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val getHomeUseCase = mockk<HomeUseCase> (relaxed = true)
    private val getCoroutineWalletBalanceUseCase = mockk<GetCoroutineWalletBalanceUseCase>(relaxed = true)
    private val userSessionInterface = mockk<UserSessionInterface>(relaxed = true)
    private lateinit var homeViewModel: HomeViewModel

    @Test
    fun `Test home view model update state`(){
        coEvery { getHomeUseCase.getHomeData() } returns flow{
            throw Throwable()
        }
        getHomeUseCase.givenGetHomeDataReturn(
                HomeDataModel(
                        list = listOf()
                )
        )
        homeViewModel = createHomeViewModel(getHomeUseCase = getHomeUseCase)

        homeViewModel.updateNetworkLiveData.observeOnce {
            assert(it != null)
        }
    }

    @Test
    fun `Test channel closed`(){
        getHomeUseCase.givenGetHomeDataReturn(
                HomeDataModel(
                        list = listOf(HeaderDataModel())
                )
        )
        every { userSessionInterface.isLoggedIn } returns true
        coEvery {
            getCoroutineWalletBalanceUseCase.executeOnBackground()
        } returns WalletBalanceModel(balance = "12000")
        homeViewModel = createHomeViewModel(
                getHomeUseCase = getHomeUseCase,
                userSessionInterface = userSessionInterface,
                getCoroutineWalletBalanceUseCase = getCoroutineWalletBalanceUseCase)
        homeViewModel.onCleared()
        homeViewModel.onRefreshTokoCash()
        homeViewModel.homeLiveData.observeOnce { homeDataModel ->
            assert((homeDataModel.list.find { it::class.java == HeaderDataModel::class.java} as? HeaderDataModel)?.homeHeaderWalletActionData?.balance == "12000")
        }
    }
}