package com.tokopedia.autocomplete.initialstate

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.autocomplete.initialstate.popularsearch.RefreshPopularSearchUseCase
import com.tokopedia.usecase.UseCase
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.mockk
import io.mockk.slot
import org.junit.Before

internal open class InitialStatePresenterTestFixtures {
    protected val initialStateView = mockk<InitialStateContract.View>(relaxed = true)

    protected val getInitialStateUseCase = mockk<UseCase<List<InitialStateData>>>(relaxed = true)
    protected val deleteRecentSearchUseCase = mockk<UseCase<Boolean>>(relaxed = true)
    protected val popularSearchUseCase = mockk<RefreshPopularSearchUseCase>(relaxed = true)

    protected val userSession = mockk<UserSessionInterface>(relaxed = true)

    protected val slotVisitableList = slot<List<Visitable<*>>>()
    protected val slotDeletedVisitableList = slot<List<Visitable<*>>>()
    protected val slotRefreshVisitableList = slot<List<Visitable<*>>>()

    protected val slotRecentViewItemList = slot<MutableList<Any>>()
    protected val slotRecentSearchItemList = slot<MutableList<Any>>()
    protected val slotPopularSearchItemList = slot<MutableList<Any>>()

    protected lateinit var initialStatePresenter: InitialStatePresenter

    protected val testException = TestException("Error")

    @Before
    fun setUp() {
        initialStatePresenter = InitialStatePresenter(
                getInitialStateUseCase,
                deleteRecentSearchUseCase,
                popularSearchUseCase,
                userSession)
        initialStatePresenter.attachView(initialStateView)
    }
}