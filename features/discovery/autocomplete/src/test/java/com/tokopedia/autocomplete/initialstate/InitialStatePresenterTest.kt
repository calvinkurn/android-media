package com.tokopedia.autocomplete.initialstate

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.autocomplete.initialstate.popularsearch.PopularSearchTitleViewModel
import com.tokopedia.autocomplete.initialstate.popularsearch.RefreshPopularSearchUseCase
import com.tokopedia.autocomplete.initialstate.popularsearch.PopularSearchViewModel
import com.tokopedia.autocomplete.initialstate.recentsearch.DeleteRecentSearchUseCase
import com.tokopedia.autocomplete.initialstate.recentsearch.RecentSearchTitleViewModel
import com.tokopedia.autocomplete.initialstate.recentsearch.RecentSearchViewModel
import com.tokopedia.autocomplete.initialstate.testinstance.createTestInstance
import com.tokopedia.autocomplete.initialstate.testinstance.initialStateCommonResponse
import com.tokopedia.autocomplete.initialstate.testinstance.popularSearchCommonResponse
import com.tokopedia.discovery.common.model.SearchParameter
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.*
import org.junit.Assert
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import rx.Subscriber

internal class InitialStatePresenterTest: Spek({

    Feature("Get Initial State Data") {
        createTestInstance()

        Scenario("Get initial state data success") {
            val getInitialStateUseCase by memoized<InitialStateUseCase>()
            val deleteRecentSearchUseCase by memoized<DeleteRecentSearchUseCase>()
            val popularSearchUseCase by memoized<RefreshPopularSearchUseCase>()
            val userSession by memoized<UserSessionInterface>()
            val searchParameter by memoized<SearchParameter>()
            val initialStateView by memoized<InitialStateContract.View>()

            val slotVisitableList = slot<List<Visitable<*>>>()

            lateinit var initialStatePresenter: InitialStatePresenter

            Given("Initial State Presenter") {
                initialStatePresenter = InitialStatePresenter(
                        getInitialStateUseCase,
                        deleteRecentSearchUseCase,
                        popularSearchUseCase,
                        userSession
                )
                initialStatePresenter.attachView(initialStateView)
            }

            Given("Get initial state API will return data") {
                every { getInitialStateUseCase.execute(any(), any()) }.answers {
                    secondArg<Subscriber<List<InitialStateData>>>().onStart()
                    secondArg<Subscriber<List<InitialStateData>>>().onNext(initialStateCommonResponse)
                }
            }

            When("Presenter get initial state data") {
                initialStatePresenter.getInitialStateData(searchParameter)
            }

            Then("Verify initial state API is called") {
                verify { getInitialStateUseCase.execute(any(), any()) }
            }

            Then("Verify initial state view behavior") {
                verify {
                    initialStateView.showInitialStateResult(capture(slotVisitableList))
                }
            }

            Then("Verify visitable list") {
                val visitableList = slotVisitableList.captured

                Assert.assertTrue(visitableList[0] is RecentSearchTitleViewModel)
                Assert.assertTrue(visitableList[1] is RecentSearchViewModel)
                Assert.assertTrue(visitableList[2] is PopularSearchTitleViewModel)
                Assert.assertTrue(visitableList[3] is PopularSearchViewModel)
                Assert.assertTrue(visitableList.size == 4)
            }
        }
    }

    Feature("Failed to Get Initial State Data") {
        createTestInstance()

        Scenario("Get initial state data success") {
            val getInitialStateUseCase by memoized<InitialStateUseCase>()
            val deleteRecentSearchUseCase by memoized<DeleteRecentSearchUseCase>()
            val popularSearchUseCase by memoized<RefreshPopularSearchUseCase>()
            val userSession by memoized<UserSessionInterface>()
            val searchParameter by memoized<SearchParameter>()
            val initialStateView by memoized<InitialStateContract.View>()

            lateinit var initialStatePresenter: InitialStatePresenter

            Given("Initial State Presenter") {
                initialStatePresenter = InitialStatePresenter(
                        getInitialStateUseCase,
                        deleteRecentSearchUseCase,
                        popularSearchUseCase,
                        userSession
                )
                initialStatePresenter.attachView(initialStateView)
            }

            Given("Get initial state API will return error") {
                every { getInitialStateUseCase.execute(any(), any()) }.answers {
                    secondArg<Subscriber<List<InitialStateData>>>().onStart()
                    secondArg<Subscriber<List<InitialStateData>>>().onError(Throwable())
                }
            }

            When("Presenter get initial state data") {
                initialStatePresenter.getInitialStateData(searchParameter)
            }

            Then("Verify initial state API is called") {
                verify { getInitialStateUseCase.execute(any(), any()) }
            }

            Then("Verify initial state view behavior") {
                confirmVerified(initialStateView)
            }
        }
    }

    Feature("Refresh Popular Search Data") {
        createTestInstance()

        Scenario("Get initial state data success") {
            val getInitialStateUseCase by memoized<InitialStateUseCase>()
            val deleteRecentSearchUseCase by memoized<DeleteRecentSearchUseCase>()
            val popularSearchUseCase by memoized<RefreshPopularSearchUseCase>()
            val userSession by memoized<UserSessionInterface>()
            val searchParameter by memoized<SearchParameter>()
            val initialStateView by memoized<InitialStateContract.View>()

            val slotVisitableList = slot<List<Visitable<*>>>()
            val slotRefreshVisitableList = slot<List<Visitable<*>>>()

            lateinit var initialStatePresenter: InitialStatePresenter

            Given("Initial State Presenter") {
                initialStatePresenter = InitialStatePresenter(
                        getInitialStateUseCase,
                        deleteRecentSearchUseCase,
                        popularSearchUseCase,
                        userSession
                )
                initialStatePresenter.attachView(initialStateView)
            }

            Given("Get initial state API will return data") {
                every { getInitialStateUseCase.execute(any(), any()) }.answers {
                    secondArg<Subscriber<List<InitialStateData>>>().onStart()
                    secondArg<Subscriber<List<InitialStateData>>>().onNext(initialStateCommonResponse)
                }
            }

            Given("Presenter get initial state data") {
                initialStatePresenter.getInitialStateData(searchParameter)
            }

            Given("Verify initial state API is called") {
                verify { getInitialStateUseCase.execute(any(), any()) }
            }

            Given("Verify initial state view behavior") {
                verify {
                    initialStateView.showInitialStateResult(capture(slotVisitableList))
                }
            }

            Given("Refresh popular search API will return data") {
                every { popularSearchUseCase.execute(any(), any()) }.answers {
                    secondArg<Subscriber<List<InitialStateData>>>().onStart()
                    secondArg<Subscriber<List<InitialStateData>>>().onNext(popularSearchCommonResponse)
                }
            }

            When("Presenter refresh popular search") {
                initialStatePresenter.refreshPopularSearch(searchParameter)
            }

            Then("Verify popularSearch API is called") {
                verify { popularSearchUseCase.execute(any(), any()) }
            }

            Then("Verify initial state view behavior") {
                verify {
                    initialStateView.refreshPopularSearch(capture(slotRefreshVisitableList))
                }
            }

            Then("Verify visitable list") {
                val refreshVisitableList = slotRefreshVisitableList.captured
                val visitableList = slotVisitableList.captured

                Assert.assertTrue(
                        (refreshVisitableList[3] as PopularSearchViewModel).list.size == (visitableList[3] as PopularSearchViewModel).list.size
                )
            }
        }
    }

    Feature("Failed to Refresh Popular Search Data") {
        createTestInstance()

        Scenario("Get initial state data success") {
            val getInitialStateUseCase by memoized<InitialStateUseCase>()
            val deleteRecentSearchUseCase by memoized<DeleteRecentSearchUseCase>()
            val popularSearchUseCase by memoized<RefreshPopularSearchUseCase>()
            val userSession by memoized<UserSessionInterface>()
            val searchParameter by memoized<SearchParameter>()
            val initialStateView by memoized<InitialStateContract.View>()

            val slotVisitableList = slot<List<Visitable<*>>>()

            lateinit var initialStatePresenter: InitialStatePresenter

            Given("Initial State Presenter") {
                initialStatePresenter = InitialStatePresenter(
                        getInitialStateUseCase,
                        deleteRecentSearchUseCase,
                        popularSearchUseCase,
                        userSession
                )
                initialStatePresenter.attachView(initialStateView)
            }

            Given("Get initial state API will return data") {
                every { getInitialStateUseCase.execute(any(), any()) }.answers {
                    secondArg<Subscriber<List<InitialStateData>>>().onStart()
                    secondArg<Subscriber<List<InitialStateData>>>().onNext(initialStateCommonResponse)
                }
            }

            Given("Presenter get initial state data") {
                initialStatePresenter.getInitialStateData(searchParameter)
            }

            Given("Verify initial state API is called") {
                verify { getInitialStateUseCase.execute(any(), any()) }
            }

            Given("Verify initial state view behavior") {
                verify {
                    initialStateView.showInitialStateResult(capture(slotVisitableList))
                }
            }

            Given("Get Refresh popular API will return error") {
                every { popularSearchUseCase.execute(any(), any()) }.answers {
                    secondArg<Subscriber<List<InitialStateItem>>>().onStart()
                    secondArg<Subscriber<List<InitialStateItem>>>().onError(Throwable())
                }
            }

            When("Presenter call refresh popular search") {
                initialStatePresenter.refreshPopularSearch(searchParameter)
            }

            Then("Verify popularSearch API is called") {
                verify { popularSearchUseCase.execute(any(), any()) }
            }

            Then("Verify initial state view behavior") {
                confirmVerified(initialStateView)
            }
        }
    }

})