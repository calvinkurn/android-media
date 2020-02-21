package com.tokopedia.home.play


import com.tokopedia.home.rules.InstantTaskExecutorRuleSpek
import com.tokopedia.home.beranda.data.model.PlayChannel
import com.tokopedia.home.beranda.domain.interactor.GetPlayLiveDynamicUseCase
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel
//import com.tokopedia.home.beranda.presentation.presenter.HomePresenter
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.PlayCardViewModel
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flow
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

class HomePresenterTest : Spek({
    InstantTaskExecutorRuleSpek(this)

    Feature("Get play data") {
        lateinit var homePresenter: HomePresenter
        val mockUserId = "12345"
        val getPlayLiveDynamicUseCase: GetPlayLiveDynamicUseCase = mockk(relaxed = true)
        Scenario("Get play data success and image valid") {

            Given("home presenter") {
                homePresenter = createPresenter()
            }

            Given("play data returns success"){
                every { homePresenter.playCardHomeUseCase } returns getPlayLiveDynamicUseCase

                every { getPlayLiveDynamicUseCase.execute() } returns flow {
                    emit(listOf(PlayChannel()))
                }
            }

            When("presenter load play data"){
                homePresenter.loadPlayBannerFromNetwork(PlayCardViewModel(DynamicHomeChannel.Channels()))
            }

            Then("Expect the function called"){
                verify{ getPlayLiveDynamicUseCase.execute() }
            }
        }
    }
})