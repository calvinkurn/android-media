package com.tokopedia.explore

import com.tokopedia.home.explore.domain.GetExploreDataUseCase
import com.tokopedia.home.explore.domain.GetExploreLocalDataUseCase
import com.tokopedia.home.explore.view.adapter.datamodel.ExploreSectionDataModel
import com.tokopedia.home.explore.view.presentation.ExploreContract
import com.tokopedia.home.explore.view.presentation.ExplorePresenter
import com.tokopedia.user.session.UserSession
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import org.junit.Test
import rx.Observable
import rx.Subscriber

/**
 * Created by Lukas on 12/18/20.
 */
class ExplorePresenterTest {
    private val dataUseCase = mockk<GetExploreDataUseCase>(relaxed = true)
    private val localDataUseCase = mockk<GetExploreLocalDataUseCase>(relaxed = true)
    private val userSession = mockk<UserSession>(relaxed = true)
    private val presenter = ExplorePresenter(
            dataUseCase, localDataUseCase, userSession
    )
    private val viewListener = mockk<ExploreContract.View>(relaxed = true)

    @Test
    fun `Test detach view`(){
        presenter.detachView()
        assert(presenter.compositeSubscription.isUnsubscribed)
    }

    @Test
    fun `Test get data`(){
        presenter.attachView(viewListener)
        val subscriber = slot<Subscriber<List<ExploreSectionDataModel>>>()
        val mockData = mockk<Observable<List<ExploreSectionDataModel>>>(relaxed = true)
        every { localDataUseCase.getExecuteObservable(any()) } returns mockData
        every { dataUseCase.getExecuteObservable(any()) } returns mockData
        every { mockData.subscribe(capture(subscriber)) } answers  {
            subscriber.captured.onNext(listOf())
            subscriber.captured
        }
        presenter.getData()
        assert(presenter.compositeSubscription.hasSubscriptions())
    }
}