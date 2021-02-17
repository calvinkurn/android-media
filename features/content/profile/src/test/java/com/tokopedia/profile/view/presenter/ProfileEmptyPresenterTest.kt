package com.tokopedia.profile.view.presenter

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.feedcomponent.data.pojo.profileheader.BymeProfileHeader
import com.tokopedia.feedcomponent.data.pojo.profileheader.ProfileHeaderData
import com.tokopedia.feedcomponent.data.pojo.profileheader.ProfileHeaderError
import com.tokopedia.feedcomponent.domain.usecase.GetProfileHeaderUseCase
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.profile.view.listener.ProfileEmptyContract
import com.tokopedia.unit.test.rule.CoroutineTestRule
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.slot
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import rx.Subscriber
import java.lang.reflect.Type

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class ProfileEmptyPresenterTest {

    @get:Rule
    val testRule = CoroutineTestRule()

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @RelaxedMockK
    lateinit var getProfileHeaderUseCase: GetProfileHeaderUseCase

    @RelaxedMockK
    lateinit var view: ProfileEmptyContract.View

    private val presenter: ProfileEmptyPresenter by lazy {
        ProfileEmptyPresenter(getProfileHeaderUseCase)
    }

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        presenter.attachView(view)
    }

    @Test
    fun `test detachView method`() {
        presenter.detachView()
        verify { getProfileHeaderUseCase.unsubscribe() }
    }

    @Test
    fun `test getProfileHeader method for exception`() {
        val slot = slot<Subscriber<GraphqlResponse>>()
        every {
            getProfileHeaderUseCase.execute(any(), capture(slot))
        } answers {
            val ans = slot.captured
            ans.onError(Throwable())
        }
        presenter.getProfileHeader(0)
        verify { view.showGetListError(any()) }
    }

    @Test
    fun `test getProfileHeader method for success with null data`() {
        val dummyResponse: ProfileHeaderData? = null
        val result = HashMap<Type, Any?>()
        val errors = HashMap<Type, List<GraphqlError>>()
        val objectType = ProfileHeaderData::class.java
        result[objectType] = dummyResponse
        val slot = slot<Subscriber<GraphqlResponse>>()

        every {
            getProfileHeaderUseCase.execute(any(), capture(slot))
        } answers {
            val ans = slot.captured
            ans.onNext(GraphqlResponse(result, errors, false))
        }
        presenter.getProfileHeader(0)
        verify { view.showGetListError(any()) }
    }

    @Test
    fun `test getProfileHeader method for success with error in data`() {
        val dummyResponse = ProfileHeaderData(
                bymeProfileHeader = BymeProfileHeader(
                        profileHeaderError = ProfileHeaderError(
                                message = "dummyMessage")))
        val result = HashMap<Type, Any?>()
        val errors = HashMap<Type, List<GraphqlError>>()
        val objectType = ProfileHeaderData::class.java
        result[objectType] = dummyResponse
        val slot = slot<Subscriber<GraphqlResponse>>()

        every {
            getProfileHeaderUseCase.execute(any(), capture(slot))
        } answers {
            val ans = slot.captured
            ans.onNext(GraphqlResponse(result, errors, false))
        }
        presenter.getProfileHeader(0)
        verify { view.showGetListError(any()) }
    }

    @Test
    fun `test getProfileHeader method for success`() {
        val dummyResponse = ProfileHeaderData()
        val result = HashMap<Type, Any?>()
        val errors = HashMap<Type, List<GraphqlError>>()
        val objectType = ProfileHeaderData::class.java
        result[objectType] = dummyResponse
        val slot = slot<Subscriber<GraphqlResponse>>()

        every {
            getProfileHeaderUseCase.execute(any(), capture(slot))
        } answers {
            val ans = slot.captured
            ans.onNext(GraphqlResponse(result, errors, false))
        }
        presenter.getProfileHeader(0)
        verify { view.renderList(any()) }
    }


}