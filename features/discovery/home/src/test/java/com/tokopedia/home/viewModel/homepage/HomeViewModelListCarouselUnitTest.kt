package com.tokopedia.home.viewModel.homepage

import androidx.lifecycle.Observer
import com.tokopedia.atc_common.domain.usecase.AddToCartOccUseCase
import com.tokopedia.home.beranda.data.usecase.HomeUseCase
import com.tokopedia.home.beranda.domain.interactor.GetDynamicChannelsUseCase
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel
import com.tokopedia.home.beranda.helper.Event
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.HomeDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.DynamicChannelDataModel
import com.tokopedia.home.beranda.presentation.viewModel.HomeViewModel
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verifyOrder
import org.junit.Test
import rx.Observable

class HomeViewModelListCarouselUnitTest{

    private val getDynamicChannelsUseCase = mockk<GetDynamicChannelsUseCase>(relaxed = true)
    private val getHomeUseCase = mockk<HomeUseCase>(relaxed = true)
    private val getAtcUseCase = mockk<AddToCartOccUseCase>(relaxed = true)
    private val homeViewModel: HomeViewModel = createHomeViewModel(getDynamicChannelsUseCase = getDynamicChannelsUseCase, getHomeUseCase = getHomeUseCase, getAtcUseCase = getAtcUseCase)

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

        // Success Express Checkout
        every{ getAtcUseCase.createObservable(any()) } returns Observable.just(mockk())


        homeViewModel.homeLiveData.observeForever(observerHome)

        // Express checkout clicked
        homeViewModel.onCloseBuyAgain(dataModel.channel!!.id, 0)


        // Expect channel updated
        verifyOrder {
            // check on home data initial first channel is dynamic channel
            observerHome.onChanged(match {
                it.list.isNotEmpty() && it.list.first() is DynamicChannelDataModel &&
                        (it.list.first() as DynamicChannelDataModel).channel?.id == "1"
            })
            observerHome.onChanged(match {
                it.list.isNotEmpty() && it.list.first() !is DynamicChannelDataModel
            })
        }
        confirmVerified(observerHome)

    }

    @Test
    fun `Get dynamic channel data success with single data and try express checkout`() {
        val dataModel = DynamicChannelDataModel()
        dataModel.channel = DynamicHomeChannel.Channels(id = "1", grids = arrayOf(DynamicHomeChannel.Grid()))
        val observerHome: Observer<HomeDataModel> = mockk(relaxed = true)
        val observerExpressCheckout: Observer<Event<Any>> = mockk(relaxed = true)

        getHomeUseCase.givenGetHomeDataReturn(
                HomeDataModel(
                        list = listOf(dataModel)
                )
        )

        // Success Express Checkout"){
        every{ getAtcUseCase.createObservable(any()) } returns
                Observable.just(mockk())

        homeViewModel.homeLiveData.observeForever(observerHome)
        homeViewModel.oneClickCheckout.observeForever(observerExpressCheckout)


        // dynamic data returns success
        getDynamicChannelsUseCase.givenGetDynamicChannelsUseCase(
                dynamicChannelDataModels = listOf(dataModel)
        )

        // Express checkout clicked
        homeViewModel.getOneClickCheckout(dataModel.channel!!, dataModel.channel!!.grids.first(), 0)

        // Expect channel updated
        verifyOrder {
            // check on home data initial first channel is dynamic channel
            observerHome.onChanged(match {
                it.list.isNotEmpty() && it.list.first() is DynamicChannelDataModel &&
                        (it.list.first() as DynamicChannelDataModel).channel?.id == "1"
            })
        }
        confirmVerified(observerHome)

        // Event express checkout should be triggered
        Thread.sleep(100)
        verifyOrder {
            observerExpressCheckout.onChanged(match { it != null })
        }
        confirmVerified(observerExpressCheckout)
    }

    @Test
    fun `Get dynamic channel data success with single data and fail express checkout`() {
        val dataModel = DynamicChannelDataModel()
        dataModel.channel = DynamicHomeChannel.Channels(id = "1", grids = arrayOf(DynamicHomeChannel.Grid()))
        val observerHome: Observer<HomeDataModel> = mockk(relaxed = true)
        val observerExpressCheckout: Observer<Event<Any>> = mockk(relaxed = true)

        getHomeUseCase.givenGetHomeDataReturn(
                HomeDataModel(
                        list = listOf(dataModel)
                )
        )

        // Success Express Checkout
        every{ getAtcUseCase.createObservable(any()) } returns Observable.error(mockk())

        homeViewModel.homeLiveData.observeForever(observerHome)
        homeViewModel.oneClickCheckout.observeForever(observerExpressCheckout)

        // dynamic data returns success
        getDynamicChannelsUseCase.givenGetDynamicChannelsUseCase(
                dynamicChannelDataModels = listOf(dataModel)
        )

        // Express checkout clicked
        homeViewModel.getOneClickCheckout(dataModel.channel!!, dataModel.channel!!.grids.first(), 0)

        // Expect channel updated
        verifyOrder {
            // check on home data initial first channel is dynamic channel
            observerHome.onChanged(match {
                it.list.isNotEmpty() && it.list.first() is DynamicChannelDataModel &&
                        (it.list.first() as DynamicChannelDataModel).channel?.id == "1"
            })
        }
        confirmVerified(observerHome)

        // Event express checkout should be triggered
        Thread.sleep(100)
        verifyOrder {
            observerExpressCheckout.onChanged(match { it != null })
        }
        confirmVerified(observerExpressCheckout)
    }
}