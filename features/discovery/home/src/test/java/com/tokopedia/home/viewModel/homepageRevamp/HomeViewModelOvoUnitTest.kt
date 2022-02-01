//package com.tokopedia.home.viewModel.homepageRevamp
//
//import androidx.arch.core.executor.testing.InstantTaskExecutorRule
//import androidx.lifecycle.Observer
//import com.tokopedia.common_wallet.balance.view.WalletBalanceModel
//import com.tokopedia.home.beranda.domain.interactor.usecase.HomeDynamicChannelUseCase
//import com.tokopedia.home.beranda.domain.interactor.GetCoroutineWalletBalanceUseCase
//import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.HomeDynamicChannelModel
//import com.tokopedia.home.beranda.presentation.viewModel.HomeRevampViewModel
//import com.tokopedia.user.session.UserSessionInterface
//import io.mockk.*
//import org.junit.Rule
//import org.junit.Test
//
///**
// * Created by Lukas on 14/05/20.
// */
//
//class HomeViewModelOvoUnitTest{
//    @get:Rule
//    val instantTaskExecutorRule = InstantTaskExecutorRule()
//
//    private val userSessionInterface = mockk<UserSessionInterface>(relaxed = true)
//    private val getHomeUseCase = mockk<HomeDynamicChannelUseCase>(relaxed = true)
//    private lateinit var homeViewModel: HomeRevampViewModel
//
//    private val getCoroutineWalletBalanceUseCase = mockk<GetCoroutineWalletBalanceUseCase>(relaxed = true)
//    //TODO fix this for unit test
////    private val headerDataModel = HomeHeaderOvoDataModel()
//
//    @Test
//    fun `Test Tokocash Only Get success data Tokocash`(){
//        val observerHome: Observer<HomeDynamicChannelModel> = mockk(relaxed = true)
//        every { userSessionInterface.isLoggedIn } returns true
//        coEvery{ getCoroutineWalletBalanceUseCase.executeOnBackground() } returns WalletBalanceModel()
//
//        getHomeUseCase.givenGetHomeDataReturn(
//            //TODO fix this for unit test
////                HomeDynamicChannelModel(
////                        list = listOf(headerDataModel)
////                )
//        )
//        homeViewModel = createHomeViewModel(
//                userSessionInterface = userSessionInterface,
//                getHomeUseCase = getHomeUseCase,
//                getCoroutineWalletBalanceUseCase = getCoroutineWalletBalanceUseCase
//        )
//        //TODO fix this for unit test
////        homeViewModel.setNewBalanceWidget(false)
//        homeViewModel.homeLiveDynamicChannel.observeForever(observerHome)
//        homeViewModel.refresh(true)
//
//        //TODO fix this for unit test
////        assert(
////            homeViewModel.homeDataModel.list.isNotEmpty() &&
////                    homeViewModel.homeDataModel.list.filterIsInstance<HomeHeaderOvoDataModel>().firstOrNull()?.headerDataModel?.homeHeaderWalletActionData != null
////        )
//    }
//}
//
