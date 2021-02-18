package com.tokopedia.affiliate.feature.onboarding.view.presenter

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.affiliate.feature.onboarding.data.pojo.registerusername.BymeRegisterAffiliateName
import com.tokopedia.affiliate.feature.onboarding.data.pojo.registerusername.Error
import com.tokopedia.affiliate.feature.onboarding.data.pojo.registerusername.RegisterUsernameData
import com.tokopedia.affiliate.feature.onboarding.data.pojo.usernamesuggestion.BymeGetRecommendedAffiliateName
import com.tokopedia.affiliate.feature.onboarding.data.pojo.usernamesuggestion.GetUsernameSuggestionData
import com.tokopedia.affiliate.feature.onboarding.domain.usecase.GetUsernameSuggestionUseCase
import com.tokopedia.affiliate.feature.onboarding.domain.usecase.RegisterUsernameUseCase
import com.tokopedia.affiliate.feature.onboarding.view.listener.UsernameInputContract
import com.tokopedia.affiliate.feature.onboarding.view.subscriber.RegisterUsernameSubscriber
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlResponse
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
class UsernameInputPresenterTest {

    @get:Rule
    val testRule = CoroutineTestRule()

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @RelaxedMockK
    lateinit var getUsernameSuggestionUseCase: GetUsernameSuggestionUseCase

    @RelaxedMockK
    lateinit var registerUsernameUseCase: RegisterUsernameUseCase

    @RelaxedMockK
    lateinit var view: UsernameInputContract.View

    private val presenter: UsernameInputPresenter by lazy {
        UsernameInputPresenter(getUsernameSuggestionUseCase, registerUsernameUseCase)
    }

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        presenter.attachView(view)
    }

    @Test
    fun `test detachView method`() {
        presenter.detachView()
        verify { getUsernameSuggestionUseCase.unsubscribe() }
        verify { registerUsernameUseCase.unsubscribe() }
    }

    @Test
    fun `test getUsernameSuggestion method for error`() {
        val slot = slot<Subscriber<GraphqlResponse>>()
        every {
            getUsernameSuggestionUseCase.execute(capture(slot))
        } answers {
            val ans = slot.captured
            ans.onError(Throwable())
        }
        presenter.getUsernameSuggestion()

        verify { view.showLoading() }
        verify { view.hideLoading() }
    }

    @Test
    fun `test getUsernameSuggestion method for success`() {
        val dummyResponse = GetUsernameSuggestionData().apply {
            suggestion = BymeGetRecommendedAffiliateName()
        }
        val result = HashMap<Type, Any?>()
        val errors = HashMap<Type, List<GraphqlError>>()
        val objectType = GetUsernameSuggestionData::class.java
        result[objectType] = dummyResponse
        val slot = slot<Subscriber<GraphqlResponse>>()

        every {
            getUsernameSuggestionUseCase.execute(capture(slot))
        } answers {
            val ans = slot.captured
            ans.onNext(GraphqlResponse(result, errors, false))
        }
        presenter.getUsernameSuggestion()
        verify { view.showLoading() }
        verify { view.hideLoading() }
        verify { view.onSuccessGetUsernameSuggestion(any()) }
    }

    @Test
    fun `test registerUsername method for error`() {
        val slot = slot<Subscriber<GraphqlResponse>>()
        every {
            registerUsernameUseCase.execute(any(), capture(slot))
        } answers {
            val ans = slot.captured
            ans.onError(Throwable())
        }
        presenter.registerUsername("")

        verify { view.showLoading() }
        verify { view.hideLoading() }
        verify { view.onErrorRegisterUsername(any()) }
    }

    @Test
    fun `test registerUsername method for success having error with validation`() {
        //test for error in success response having error with validation
        val dummyErrorResponseWithValidation = RegisterUsernameData().apply {
            bymeRegisterAffiliateName = BymeRegisterAffiliateName().apply {
                error = Error("Dummy Error", type = RegisterUsernameSubscriber.ERROR_VALIDATION)
            }
        }
        val result = HashMap<Type, Any?>()
        val errors = HashMap<Type, List<GraphqlError>>()
        val objectType = RegisterUsernameData::class.java
        result[objectType] = dummyErrorResponseWithValidation
        val slot = slot<Subscriber<GraphqlResponse>>()
        every {
            registerUsernameUseCase.execute(any(), capture(slot))
        } answers {
            val ans = slot.captured
            ans.onNext(GraphqlResponse(result, errors, false))
        }
        presenter.registerUsername("")
        verify { view.showLoading() }
        verify { view.hideLoading() }
        verify { view.onErrorInputRegisterUsername(dummyErrorResponseWithValidation.bymeRegisterAffiliateName.error.message) }
    }

    @Test
    fun `test registerUsername method for success having error without validation`() {
        //test for error in success response having error without validation
        val dummyErrorResponseWithoutValidation = RegisterUsernameData().apply {
            bymeRegisterAffiliateName = BymeRegisterAffiliateName().apply {
                error = Error("Dummy Error")
            }
        }
        val result = HashMap<Type, Any?>()
        val errors = HashMap<Type, List<GraphqlError>>()
        val objectType = RegisterUsernameData::class.java
        result[objectType] = dummyErrorResponseWithoutValidation
        val slot = slot<Subscriber<GraphqlResponse>>()
        every {
            registerUsernameUseCase.execute(any(), capture(slot))
        } answers {
            val ans = slot.captured
            ans.onNext(GraphqlResponse(result, errors, false))
        }
        presenter.registerUsername("")
        verify { view.showLoading() }
        verify { view.hideLoading() }
        verify { view.onErrorRegisterUsername(dummyErrorResponseWithoutValidation.bymeRegisterAffiliateName.error.message) }
    }

    @Test(expected = RuntimeException::class)
    fun `test registerUsername method for success having failure in response`() {
        //test for error in success response having error with validation
        val dummyError = RegisterUsernameData().apply {
            bymeRegisterAffiliateName = BymeRegisterAffiliateName().apply {
                isSuccess = false
            }
        }
        val result = HashMap<Type, Any?>()
        val errors = HashMap<Type, List<GraphqlError>>()
        val objectType = RegisterUsernameData::class.java
        result[objectType] = dummyError
        val slot = slot<Subscriber<GraphqlResponse>>()
        every {
            registerUsernameUseCase.execute(any(), capture(slot))
        } answers {
            val ans = slot.captured
            ans.onNext(GraphqlResponse(result, errors, false))
        }
        presenter.registerUsername("")
        verify { view.showLoading() }
        verify { view.hideLoading() }
    }

    @Test
    fun `test registerUsername method for success`() {
        val dummyResponse = RegisterUsernameData().apply {
            bymeRegisterAffiliateName = BymeRegisterAffiliateName().apply {
                isSuccess = true
            }
        }
        val result = HashMap<Type, Any?>()
        val errors = HashMap<Type, List<GraphqlError>>()
        val objectType = RegisterUsernameData::class.java
        result[objectType] = dummyResponse
        val slot = slot<Subscriber<GraphqlResponse>>()
        every {
            registerUsernameUseCase.execute(any(), capture(slot))
        } answers {
            val ans = slot.captured
            ans.onNext(GraphqlResponse(result, errors, false))
        }
        presenter.registerUsername("")
        verify { view.showLoading() }
        verify { view.hideLoading() }
        verify { view.onSuccessRegisterUsername() }
    }

}