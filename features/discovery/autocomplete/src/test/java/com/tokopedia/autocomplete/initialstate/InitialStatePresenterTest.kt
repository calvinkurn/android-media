package com.tokopedia.autocomplete.initialstate

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.autocomplete.initialstate.newfiles.InitialStateData
import com.tokopedia.autocomplete.initialstate.recentsearch.RecentSearchTitleViewModel
import com.tokopedia.autocomplete.initialstate.testinstance.initialStateCommonResponse
import com.tokopedia.discovery.common.model.SearchParameter
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.Assert
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import rx.Subscriber

internal class InitialStatePresenterTest: Spek({

    Feature("Get Initial State Data") {
        val getInitialStateUseCase by memoized { mockk<UseCase<List<InitialStateData>>>(relaxed = true) }
        val initialStateView by memoized { mockk<InitialStateContract.View>(relaxed = true) }

        Scenario("Get initial state data success") {
            val getInitialStateUseCase by memoized<UseCase<List<InitialStateData>>>()
            val initialStateView by memoized<InitialStateContract.View>()
            val slotVisitableList = slot<List<Visitable<*>>>()

            lateinit var initialStatePresenter: InitialStatePresenter2

            Given("Initial State Presenter") {
                initialStatePresenter = InitialStatePresenter2(getInitialStateUseCase)
                initialStatePresenter.attachView(initialStateView)
            }

            Given("Get initial state API will return data") {
                every { getInitialStateUseCase.execute(any(), any()) }.answers {
                    secondArg<Subscriber<List<InitialStateData>>>().onStart()
                    secondArg<Subscriber<List<InitialStateData>>>().onNext(initialStateCommonResponse)
                }
            }

            When("presenter get initial state data") {
                initialStatePresenter.getInitialStateData(mapOf())
            }

            Then("verify initial state API is called") {
                verify { getInitialStateUseCase.execute(any(), any()) }
            }

            Then("verify initial state view behavior") {
                verify {
                    initialStateView.setData(capture(slotVisitableList))
                }
            }

            Then("verify visitable list") {
                val visitableList = slotVisitableList.captured

                Assert.assertTrue(visitableList[0] is RecentSearchTitleViewModel)
            }
        }
    }

})

class InitialStatePresenter2(
        private val getInitialStateUseCase: UseCase<List<InitialStateData>>
): BaseDaggerPresenter<InitialStateContract.View>(), InitialStateContract.Presenter {

    override fun search(searchParameter: SearchParameter) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteRecentSearchItem(keyword: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteAllRecentSearch() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun refreshPopularSearch(searchParameter: SearchParameter) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getInitialStateResult(list: MutableList<InitialStateData>, searchTerm: String): List<Visitable<*>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getInitialStateData(searchParameter: Map<String, Any>) {
        getInitialStateUseCase.execute(RequestParams.EMPTY, object: Subscriber<List<InitialStateData>>() {
            override fun onNext(t: List<InitialStateData>?) {
                val visitableList = mutableListOf<Visitable<*>>()

                visitableList.add(RecentSearchTitleViewModel())

                view.setData(visitableList)
            }

            override fun onCompleted() {

            }

            override fun onError(e: Throwable?) {

            }

        })
    }
}