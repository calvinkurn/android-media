package com.tokopedia.home.viewModel.homepageRevamp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.atc_common.domain.usecase.AddToCartOccUseCase
import com.tokopedia.home.beranda.data.usecase.HomeRevampUseCase
import com.tokopedia.home.beranda.domain.gql.CloseChannel
import com.tokopedia.home.beranda.domain.interactor.CloseChannelUseCase
import com.tokopedia.home.beranda.domain.interactor.GetDynamicChannelsUseCase
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.HomeDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.DynamicChannelDataModel
import com.tokopedia.home.beranda.presentation.viewModel.HomeRevampViewModel
import com.tokopedia.home.ext.observeOnce
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.visitable.RecommendationListCarouselDataModel
import io.mockk.*
import org.junit.Rule
import org.junit.Test
import rx.Observable

class HomeViewModelListCarouselUnitTest{
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val getDynamicChannelsUseCase = mockk<GetDynamicChannelsUseCase>(relaxed = true)
    private val getHomeUseCase = mockk<HomeRevampUseCase>(relaxed = true)
    private val getAtcUseCase = mockk<AddToCartOccUseCase>(relaxed = true)
    private val closeChannelUseCase = mockk<CloseChannelUseCase>(relaxed = true)
    private lateinit var homeViewModel: HomeRevampViewModel

    @Test
    fun `Get dynamic channel data success with single data and try close widget`() {
        val dataModel = DynamicChannelDataModel()
        dataModel.channel = DynamicHomeChannel.Channels(id = "1")
        val observerHome: Observer<HomeDataModel> = mockk(relaxed = true)

        getHomeUseCase.givenGetHomeDataReturn(
                HomeDataModel(
                        list = listOf(dataModel)
                )
        )

        coEvery { closeChannelUseCase.executeOnBackground() } returns CloseChannel(success = true)

        // Success Express Checkout
        every{ getAtcUseCase.createObservable(any()) } returns Observable.just(mockk())

        homeViewModel = createHomeViewModel(closeChannelUseCase = closeChannelUseCase, getHomeUseCase = getHomeUseCase, getAtcUseCase = getAtcUseCase)
        homeViewModel.homeLiveData.observeForever(observerHome)

        // Express checkout clicked
        homeViewModel.onCloseBuyAgain(dataModel.channel!!.id, 0)

        // Expect channel updated
        assert(homeViewModel.homeDataModel.list.none { it::class.java == DynamicChannelDataModel::class.java })
    }

    @Test
    fun `Get RecommendationListCarouselDataModel channel data success with single data and try close widget`() {
        val dataModel = RecommendationListCarouselDataModel(ChannelModel(id = "1", groupId = "1"))
        val observerHome: Observer<HomeDataModel> = mockk(relaxed = true)

        getHomeUseCase.givenGetHomeDataReturn(
                HomeDataModel(
                        list = listOf(dataModel)
                )
        )

        coEvery { closeChannelUseCase.executeOnBackground() } returns CloseChannel(success = true)

        // Success Express Checkout
        every{ getAtcUseCase.createObservable(any()) } returns Observable.just(mockk())

        homeViewModel = createHomeViewModel(closeChannelUseCase = closeChannelUseCase, getHomeUseCase = getHomeUseCase, getAtcUseCase = getAtcUseCase)
        homeViewModel.homeLiveData.observeForever(observerHome)

        // Express checkout clicked
        homeViewModel.onCloseBuyAgain(dataModel.channelModel.id, 0)


        assert(homeViewModel.homeDataModel.list.none { it::class.java == DynamicChannelDataModel::class.java })
    }

    @Test
    fun `Get dynamic channel data success with single data and try close widget fail`() {
        val dataModel = DynamicChannelDataModel()
        dataModel.channel = DynamicHomeChannel.Channels(id = "1")
        val observerHome: Observer<HomeDataModel> = mockk(relaxed = true)

        getHomeUseCase.givenGetHomeDataReturn(
                HomeDataModel(
                        list = listOf(dataModel)
                )
        )

        coEvery { closeChannelUseCase.executeOnBackground() } returns CloseChannel(success = false)

        // Success Express Checkout
        every{ getAtcUseCase.createObservable(any()) } returns Observable.just(mockk())

        homeViewModel = createHomeViewModel(closeChannelUseCase = closeChannelUseCase, getHomeUseCase = getHomeUseCase, getAtcUseCase = getAtcUseCase)
        homeViewModel.homeLiveData.observeForever(observerHome)

        // Express checkout clicked
        homeViewModel.onCloseBuyAgain(dataModel.channel!!.id, 0)

        homeViewModel.errorEventLiveData.observeOnce { assert(it != null) }

        homeViewModel.isViewModelInitialized.observeOnce { assert(it != null) }

        // Expect channel updated
        verifyOrder {
            // check on home data initial first channel is dynamic channel
            observerHome.onChanged(match {
                it.list.isNotEmpty() && it.list.first() is DynamicChannelDataModel &&
                        (it.list.first() as DynamicChannelDataModel).channel?.id == "1"
            })
        }
        confirmVerified(observerHome)

    }


//    @Test
//    fun `Get dynamic channel data success with single data and try express checkout home component`() {
//        val dataModel = DynamicChannelDataModel()
//        dataModel.channel = DynamicHomeChannel.Channels(id = "1", grids = arrayOf(DynamicHomeChannel.Grid()))
//        val observerHome: Observer<HomeDataModel> = mockk(relaxed = true)
//        val observerExpressCheckout: Observer<Event<Any>> = mockk(relaxed = true)
//
//        getHomeUseCase.givenGetHomeDataReturn(
//                HomeDataModel(
//                        list = listOf(dataModel)
//                )
//        )
//
//        // Success Express Checkout"){
//        every{ getAtcUseCase.createObservable(any()) } returns
//                Observable.just(mockk())
//
//        homeViewModel = createHomeViewModel(getDynamicChannelsUseCase = getDynamicChannelsUseCase, getHomeUseCase = getHomeUseCase, getAtcUseCase = getAtcUseCase)
//        homeViewModel.homeLiveData.observeForever(observerHome)
//        homeViewModel.oneClickCheckoutHomeComponent.observeForever(observerExpressCheckout)
//
//        // dynamic data returns success
//        getDynamicChannelsUseCase.givenGetDynamicChannelsUseCase(
//                dynamicChannelDataModels = listOf(dataModel)
//        )
//
//        // Express checkout clicked
//        homeViewModel.getOneClickCheckoutHomeComponent(ChannelModel(id="1", groupId = "1"), ChannelGrid(), 0)
//
//        // Expect channel updated
//        verifyOrder {
//            // check on home data initial first channel is dynamic channel
//            observerHome.onChanged(match {
//                it.list.isNotEmpty() && it.list.first() is DynamicChannelDataModel &&
//                        (it.list.first() as DynamicChannelDataModel).channel?.id == "1"
//            })
//        }
//        confirmVerified(observerHome)
//
//        // Event express checkout should be triggered
//        Thread.sleep(100)
//        verifyOrder {
//            observerExpressCheckout.onChanged(match { it != null })
//        }
//        confirmVerified(observerExpressCheckout)
//    }

}