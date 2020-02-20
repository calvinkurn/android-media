package com.tokopedia.home.viewModel


import androidx.lifecycle.Observer
import com.tokopedia.home.beranda.data.model.PlayChannel
import com.tokopedia.home.beranda.domain.interactor.GetPlayLiveDynamicUseCase
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.HomeDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.PlayCardViewModel
import com.tokopedia.home.beranda.presentation.viewModel.HomeViewModel
import com.tokopedia.home.ext.observeOnce
import com.tokopedia.home.rules.InstantTaskExecutorRuleSpek
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

@ExperimentalCoroutinesApi
class HomeViewModelTest : Spek({
    InstantTaskExecutorRuleSpek(this)

    Feature("Get play data") {
        lateinit var homeViewModel: HomeViewModel
        lateinit var observerHome: Observer<HomeDataModel>
        createHomeViewModelTestInstance()

        val getPlayLiveDynamicUseCase by memoized<GetPlayLiveDynamicUseCase>()
        Scenario("Get play data success and image valid") {
            val playDataModel = PlayCardViewModel(DynamicHomeChannel.Channels())
            Given("home viewmodel") {
                homeViewModel = createHomeViewModel()
            }

            Given("play data returns success"){
                getPlayLiveDynamicUseCase.givenGetPlayLiveDynamicUseCaseReturn(
                    channel = PlayChannel(
                            coverUrl = "cobacoba.com"
                    )
                )
            }

            When("viewModel load play data"){
                homeViewModel.loadPlayBannerFromNetwork(playDataModel)
            }

            Then("Expect the event on live data available"){
                homeViewModel.requestImageTestLiveData.observeOnce {
                    assert(it.peekContent() == playDataModel && it.peekContent().playCardHome != null)
                }
            }
        }
    }
})