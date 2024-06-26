package com.tokopedia.usercomponents.userconsent

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.unit.test.ext.getOrAwaitValue
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.usercomponents.common.wrapper.UserComponentsStateResult
import com.tokopedia.usercomponents.userconsent.common.CollectionPointDataModel
import com.tokopedia.usercomponents.userconsent.common.ConsentCollectionResponse
import com.tokopedia.usercomponents.userconsent.common.UserConsentCollectionDataModel
import com.tokopedia.usercomponents.userconsent.domain.collection.ConsentCollectionParam
import com.tokopedia.usercomponents.userconsent.domain.collection.GetCollectionPointWithConsentUseCase
import com.tokopedia.usercomponents.userconsent.domain.collection.GetConsentCollectionUseCase
import com.tokopedia.usercomponents.userconsent.domain.submission.ConsentSubmissionParam
import com.tokopedia.usercomponents.userconsent.domain.submission.ConsentSubmissionResponse
import com.tokopedia.usercomponents.userconsent.domain.submission.SubmitConsentDataModel
import com.tokopedia.usercomponents.userconsent.domain.submission.SubmitConsentUseCase
import com.tokopedia.usercomponents.userconsent.ui.UserConsentViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertTrue

class UserConsentViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val dispatcherProviderTest = CoroutineTestDispatchersProvider
    private var viewModel: UserConsentViewModel? = null

    private var getConsentCollectionUseCase = mockk<GetConsentCollectionUseCase>(relaxed = true)
    private var getCollectionPointWithConsentUseCase = mockk<GetCollectionPointWithConsentUseCase>(relaxed = true)
    private var submitConsentUseCase = mockk<SubmitConsentUseCase>(relaxed = true)
    private val userSession = mockk<UserSessionInterface>(relaxed = true)
    private var observerUserConsentCollection = mockk<Observer<UserComponentsStateResult<UserConsentCollectionDataModel>>>(relaxed = true)

    private var mockCollectionParam = ConsentCollectionParam(
        collectionId = "6d45e8ce-d46d-4f0e-bf0c-a93f82f75e36",
        version = "1"
    )
    private var mockErrorMessage = "Opps!"
    private val mockThrowable = Throwable(mockErrorMessage)

    @Before
    fun setup() {
        viewModel = UserConsentViewModel(
            getConsentCollectionUseCase,
            getCollectionPointWithConsentUseCase,
            submitConsentUseCase,
            userSession,
            dispatcherProviderTest
        )
        viewModel?.consentCollection?.observeForever(observerUserConsentCollection)
    }

    @After
    fun tearDown() {
        viewModel?.consentCollection?.removeObserver(observerUserConsentCollection)
    }

    @Test
    fun `Get Consent Collection - Success`() {
        val mockCollectionPoints = mutableListOf(
            CollectionPointDataModel(
                id = "id",
                consentType = "type"
            )
        )
        val hideWhenAlreadyHaveConsent = false

        val mockResponse = ConsentCollectionResponse(
            UserConsentCollectionDataModel(
                success = true,
                collectionPoints = mockCollectionPoints
            )
        )

        coEvery { userSession.isLoggedIn } returns true
        coEvery {
            getConsentCollectionUseCase(mockCollectionParam)
        } returns mockResponse

        viewModel?.getConsentCollection(mockCollectionParam, hideWhenAlreadyHaveConsent)

        coVerify {
            observerUserConsentCollection.onChanged(
                UserComponentsStateResult.Success(mockResponse.data)
            )
        }

        val result = viewModel?.consentCollection?.value
        assert(result is UserComponentsStateResult.Success)

        (result as UserComponentsStateResult.Success).apply {
            assert(result.data?.success == true)
            assert(result.data?.collectionPoints?.isNotEmpty() == true)
        }
    }

    @Test
    fun `Get Consent Collection - Failed with message`() {
        val mockResponse = ConsentCollectionResponse(
            UserConsentCollectionDataModel(
                success = false,
                errorMessages = listOf(
                    mockErrorMessage
                )
            )
        )
        val hideWhenAlreadyHaveConsent = false

        coEvery { userSession.isLoggedIn } returns true
        coEvery {
            getConsentCollectionUseCase(mockCollectionParam)
        } returns mockResponse

        viewModel?.getConsentCollection(mockCollectionParam, hideWhenAlreadyHaveConsent)

        val result = viewModel?.consentCollection?.value
        assert(result is UserComponentsStateResult.Fail)
    }

    @Test
    fun `Get Consent Collection - General Error`() {
        val mockResponse = ConsentCollectionResponse(
            UserConsentCollectionDataModel(
                success = false,
                errorMessages = listOf()
            )
        )
        val hideWhenAlreadyHaveConsent = false

        coEvery { userSession.isLoggedIn } returns true
        coEvery {
            getConsentCollectionUseCase(mockCollectionParam)
        } returns mockResponse

        viewModel?.getConsentCollection(mockCollectionParam, hideWhenAlreadyHaveConsent)

        val result = viewModel?.consentCollection?.value
        assert(result is UserComponentsStateResult.Fail)
        assert((result as UserComponentsStateResult.Fail).error.message?.contains(UserConsentViewModel.GENERAL_ERROR) == true)
    }

    @Test
    fun `Get Consent Collection - Throw error`() {
        coEvery {
            getConsentCollectionUseCase(mockCollectionParam)
        } throws mockThrowable
        val hideWhenAlreadyHaveConsent = false

        coEvery { userSession.isLoggedIn } returns true

        viewModel?.getConsentCollection(mockCollectionParam, hideWhenAlreadyHaveConsent)

        val result = viewModel?.consentCollection?.value
        assert(result is UserComponentsStateResult.Fail)
    }

    @Test
    fun `Get Consent Collection - Throw error - Not Login`() {
        coEvery {
            getConsentCollectionUseCase(mockCollectionParam)
        } throws mockThrowable
        val hideWhenAlreadyHaveConsent = false

        coEvery { userSession.isLoggedIn } returns false

        viewModel?.getConsentCollection(mockCollectionParam, hideWhenAlreadyHaveConsent)

        val result = viewModel?.consentCollection?.value
        assert(result is UserComponentsStateResult.Fail)
    }

    @Test
    fun `Get Consent Collection With Consent - Success`() {
        val mockCollectionPoints = mutableListOf(
            CollectionPointDataModel(
                id = "id",
                consentType = "type",
                needConsent = true
            )
        )
        val hideWhenAlreadyHaveConsent = true

        val mockResponse = ConsentCollectionResponse(
            UserConsentCollectionDataModel(
                success = true,
                collectionPoints = mockCollectionPoints
            )
        )

        coEvery { userSession.isLoggedIn } returns true
        coEvery {
            getCollectionPointWithConsentUseCase(mockCollectionParam)
        } returns mockResponse

        viewModel?.getConsentCollection(mockCollectionParam, hideWhenAlreadyHaveConsent)

        coVerify {
            observerUserConsentCollection.onChanged(
                UserComponentsStateResult.Success(mockResponse.data)
            )
        }

        val result = viewModel?.consentCollection?.getOrAwaitValue()
        assert(result is UserComponentsStateResult.Success)

        (result as UserComponentsStateResult.Success).apply {
            assert(result.data?.success == true)
            assert(result.data?.collectionPoints?.isNotEmpty() == true)
        }
    }

    @Test
    fun `Get Consent Collection With Consent - Failed with message`() {
        val mockResponse = ConsentCollectionResponse(
            UserConsentCollectionDataModel(
                success = false,
                errorMessages = listOf(
                    mockErrorMessage
                )
            )
        )
        val hideWhenAlreadyHaveConsent = true

        coEvery { userSession.isLoggedIn } returns true
        coEvery {
            getCollectionPointWithConsentUseCase(mockCollectionParam)
        } returns mockResponse

        viewModel?.getConsentCollection(mockCollectionParam, hideWhenAlreadyHaveConsent)

        val result = viewModel?.consentCollection?.value
        assert(result is UserComponentsStateResult.Fail)
    }

    @Test
    fun `Get Consent Collection With Consent - General Error`() {
        val mockResponse = ConsentCollectionResponse(
            UserConsentCollectionDataModel(
                success = false,
                errorMessages = listOf()
            )
        )
        val hideWhenAlreadyHaveConsent = true

        coEvery { userSession.isLoggedIn } returns true
        coEvery {
            getCollectionPointWithConsentUseCase(mockCollectionParam)
        } returns mockResponse

        viewModel?.getConsentCollection(mockCollectionParam, hideWhenAlreadyHaveConsent)

        val result = viewModel?.consentCollection?.value
        assert(result is UserComponentsStateResult.Fail)
        assert((result as UserComponentsStateResult.Fail).error.message?.contains(UserConsentViewModel.GENERAL_ERROR) == true)
    }

    @Test
    fun `Get Consent Collection With Consent - Throw error`() {
        coEvery {
            getConsentCollectionUseCase(mockCollectionParam)
        } throws mockThrowable
        val hideWhenAlreadyHaveConsent = true

        coEvery { userSession.isLoggedIn } returns true

        viewModel?.getConsentCollection(mockCollectionParam, hideWhenAlreadyHaveConsent)

        val result = viewModel?.consentCollection?.value
        assert(result is UserComponentsStateResult.Fail)
    }

    @Test
    fun `submit consent then make sure function only called once`() {
        val parameter = ConsentSubmissionParam()

        viewModel?.submitConsent(parameter)

        coVerify(exactly = 1) {
            submitConsentUseCase(parameter)
        }
    }

    @Test
    fun `submit consent then failed`() {
        val parameter = ConsentSubmissionParam()

        coEvery {
            submitConsentUseCase(parameter)
        } throws mockThrowable
        viewModel?.submitConsent(parameter)

        coVerify(exactly = 1) {
            submitConsentUseCase(parameter)
        }
    }

    @Test
    fun `submit consent then success submit`() {
        val parameter = ConsentSubmissionParam()
        val response = ConsentSubmissionResponse(
            SubmitConsentDataModel(
                isSuccess = true
            )
        )

        coEvery {
            submitConsentUseCase(parameter)
        } returns response
        viewModel?.submitConsent(parameter)

        val result = viewModel?.submitResult?.getOrAwaitValue()
        assertTrue(result is UserComponentsStateResult.Success)
    }

    @Test
    fun `submit consent then failed submit`() {
        val parameter = ConsentSubmissionParam()
        val response = ConsentSubmissionResponse(
            SubmitConsentDataModel(
                isSuccess = false
            )
        )

        coEvery {
            submitConsentUseCase(parameter)
        } returns response
        viewModel?.submitConsent(parameter)

        val result = viewModel?.submitResult?.getOrAwaitValue()
        assertTrue(result is UserComponentsStateResult.Fail)
    }

}
