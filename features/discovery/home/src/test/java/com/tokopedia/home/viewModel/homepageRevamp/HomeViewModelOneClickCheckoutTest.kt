//package com.tokopedia.home.viewModel.homepageRevamp
//
//import androidx.arch.core.executor.testing.InstantTaskExecutorRule
//import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
//import com.tokopedia.atc_common.domain.model.response.DataModel
//import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartOccMultiUseCase
//import com.tokopedia.home.beranda.domain.interactor.usecase.HomeDynamicChannelUseCase
//import com.tokopedia.home.beranda.presentation.viewModel.HomeRevampViewModel
//import com.tokopedia.home_component.model.ChannelGrid
//import io.mockk.coEvery
//import io.mockk.mockk
//import org.junit.Rule
//import org.junit.Test
//
///**
// * Created by Lukas on 07/11/20.
// */
//class HomeViewModelOneClickCheckoutTest {
//    @get:Rule
//    val instantTaskExecutorRule = InstantTaskExecutorRule()
//
//    private val getHomeUseCase = mockk<HomeDynamicChannelUseCase> (relaxed = true)
//    private val addToCartOccUseCase = mockk<AddToCartOccMultiUseCase> (relaxed = true)
//    private lateinit var homeViewModel: HomeRevampViewModel
//
//    @Test
//    fun `one click checkout success`(){
//        val atc = AddToCartDataModel(status = AddToCartDataModel.STATUS_OK, data = DataModel(
//                success = 1
//        ))
//        coEvery { addToCartOccUseCase.setParams(any()).executeOnBackground().mapToAddToCartDataModel() } returns atc
//        homeViewModel = createHomeViewModel(getAtcUseCase = addToCartOccUseCase)
//        homeViewModel.getOneClickCheckoutHomeComponent(mockk(), ChannelGrid(), 1)
//        assert(homeViewModel.oneClickCheckoutHomeComponent.value?.peekContent() !is Throwable)
//        assert(homeViewModel.oneClickCheckout.value?.peekContent() !is Throwable)
//    }
//
//    @Test
//    fun `one click checkout error response`(){
//        val atc = AddToCartDataModel(status = AddToCartDataModel.STATUS_ERROR)
//        coEvery { addToCartOccUseCase.setParams(any()).executeOnBackground().mapToAddToCartDataModel() } returns atc
//        homeViewModel = createHomeViewModel(getAtcUseCase = addToCartOccUseCase)
//        homeViewModel.getOneClickCheckoutHomeComponent(mockk(), ChannelGrid(), 1)
//        assert(homeViewModel.oneClickCheckoutHomeComponent.value?.peekContent() != null)
//    }
//
//    @Test
//    fun `one click checkout error`(){
//        coEvery { addToCartOccUseCase.setParams(any()).executeOnBackground().mapToAddToCartDataModel() } throws Throwable()
//        homeViewModel = createHomeViewModel(getAtcUseCase = addToCartOccUseCase)
//        homeViewModel.getOneClickCheckoutHomeComponent(mockk(), ChannelGrid(), 1)
//        assert(homeViewModel.oneClickCheckoutHomeComponent.value?.peekContent() != null)
//    }
//}