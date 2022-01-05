//package com.tokopedia.home.viewModel.homepageRevamp
//
//import androidx.arch.core.executor.testing.InstantTaskExecutorRule
//import com.tokopedia.common_wallet.balance.view.WalletBalanceModel
//import com.tokopedia.home.beranda.domain.interactor.usecase.HomeDynamicChannelUseCase
//import com.tokopedia.home.beranda.domain.interactor.GetCoroutineWalletBalanceUseCase
//import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.HomeDynamicChannelModel
//import com.tokopedia.home.beranda.presentation.viewModel.HomeRevampViewModel
//import com.tokopedia.home.ext.observeOnce
//import com.tokopedia.user.session.UserSessionInterface
//import io.mockk.coEvery
//import io.mockk.every
//import io.mockk.mockk
//import kotlinx.coroutines.flow.flow
//import org.junit.Rule
//import org.junit.Test
//
//class HomeViewModelHomeUpdateNetworkTest {
//    @get:Rule
//    val instantTaskExecutorRule = InstantTaskExecutorRule()
//
//    private val getHomeUseCase = mockk<HomeDynamicChannelUseCase> (relaxed = true)
//    private val getCoroutineWalletBalanceUseCase = mockk<GetCoroutineWalletBalanceUseCase>(relaxed = true)
//    private val userSessionInterface = mockk<UserSessionInterface>(relaxed = true)
//    private lateinit var homeViewModel: HomeRevampViewModel
//
//    @Test
//    fun `Test home view model update state`(){
//        //TODO fix this for unit test
////        coEvery { getHomeUseCase.getHomeData() } returns flow{
////            throw Throwable()
////        }
//        getHomeUseCase.givenGetHomeDataReturn(
//                HomeDynamicChannelModel(
//                        list = listOf()
//                )
//        )
//        homeViewModel = createHomeViewModel(getHomeUseCase = getHomeUseCase)
//
//        homeViewModel.updateNetworkLiveData.observeOnce {
//            assert(it != null)
//        }
//    }
//
//    @Test
//    fun `Test channel closed`(){
//        //TODO fix this for unit test
////        getHomeUseCase.givenGetHomeDataReturn(
////                HomeDynamicChannelModel(
////                        list = listOf(HomeHeaderOvoDataModel()),
////                        isCache = false,
////                        isProcessingAtf = false
////                )
////        )
//        every { userSessionInterface.isLoggedIn } returns true
//        coEvery {
//            getCoroutineWalletBalanceUseCase.executeOnBackground()
//        } returns WalletBalanceModel(balance = "12000")
//        homeViewModel = createHomeViewModel(
//                getHomeUseCase = getHomeUseCase,
//                userSessionInterface = userSessionInterface,
//                getCoroutineWalletBalanceUseCase = getCoroutineWalletBalanceUseCase)
//        //TODO fix this for unit test
////        homeViewModel.setNewBalanceWidget(false)
////        homeViewModel.onRefreshTokoCash()
////        assert((homeViewModel.homeLiveDynamicChannel.value!!.list.find { it::class.java == HomeHeaderOvoDataModel::class.java} as? HomeHeaderOvoDataModel)?.headerDataModel?.homeHeaderWalletActionData?.balance == "12000")
//    }
//}