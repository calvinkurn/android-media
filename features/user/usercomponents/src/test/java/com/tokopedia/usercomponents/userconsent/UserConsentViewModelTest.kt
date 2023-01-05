package com.tokopedia.usercomponents.userconsent

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.usercomponents.common.wrapper.UserComponentsStateResult
import com.tokopedia.usercomponents.userconsent.common.UserConsentCollectionDataModel
import com.tokopedia.usercomponents.userconsent.domain.collection.ConsentCollectionParam
import com.tokopedia.usercomponents.userconsent.domain.collection.ConsentCollectionResponse
import com.tokopedia.usercomponents.userconsent.domain.collection.GetConsentCollectionUseCase
import com.tokopedia.usercomponents.userconsent.domain.submission.ConsentSubmissionParam
import com.tokopedia.usercomponents.userconsent.domain.submission.SubmitConsentUseCase
import com.tokopedia.usercomponents.userconsent.ui.UserConsentViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class UserConsentViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val dispatcherProviderTest = CoroutineTestDispatchersProvider
    private var viewModel: UserConsentViewModel? = null

    private var getConsentCollectionUseCase = mockk<GetConsentCollectionUseCase>(relaxed = true)
    private var submitConsentUseCase = mockk<SubmitConsentUseCase>(relaxed = true)
    private var observerUserConsentCollection = mockk<Observer<UserComponentsStateResult<UserConsentCollectionDataModel>>>(relaxed = true)

    private var mockCollectionParam = ConsentCollectionParam(
        collectionId = "6d45e8ce-d46d-4f0e-bf0c-a93f82f75e36",
        version = "1"
    )
    private var mockErrorMessage = "Opps!"
    private val mockThrowable = Throwable(mockErrorMessage)

    @Before
    fun setup() {
        viewModel = UserConsentViewModel(getConsentCollectionUseCase, submitConsentUseCase, dispatcherProviderTest)
        viewModel?.consentCollection?.observeForever(observerUserConsentCollection)
    }

    @After
    fun tearDown() {
        viewModel?.consentCollection?.removeObserver(observerUserConsentCollection)
    }

    @Test
    fun `Get Consent Collection - Success`() {
        val mockCollectionPoints = mutableListOf(
            UserConsentCollectionDataModel.CollectionPointDataModel(
                id = "id",
                consentType = "type"
            )
        )

        val mockResponse = ConsentCollectionResponse(
            UserConsentCollectionDataModel(
                success = true,
                collectionPoints = mockCollectionPoints
            )
        )

        coEvery {
            getConsentCollectionUseCase(mockCollectionParam)
        } returns mockResponse

        viewModel?.getConsentCollection(mockCollectionParam)

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

        coEvery {
            getConsentCollectionUseCase(mockCollectionParam)
        } returns mockResponse

        viewModel?.getConsentCollection(mockCollectionParam)

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

        coEvery {
            getConsentCollectionUseCase(mockCollectionParam)
        } returns mockResponse

        viewModel?.getConsentCollection(mockCollectionParam)

        val result = viewModel?.consentCollection?.value
        assert(result is UserComponentsStateResult.Fail)
        assert((result as UserComponentsStateResult.Fail).error.message?.contains(UserConsentViewModel.GENERAL_ERROR) == true)
    }

    @Test
    fun `Get Consent Collection - Throw error`() {
        coEvery {
            getConsentCollectionUseCase(mockCollectionParam)
        } throws mockThrowable

        viewModel?.getConsentCollection(mockCollectionParam)

        val result = viewModel?.consentCollection?.value
        assert(result is UserComponentsStateResult.Fail)
    }

    @Test
    fun `submit consent then then make sure function only called once`() {
        val parameter = ConsentSubmissionParam()

        viewModel?.submitConsent(parameter)

        coVerify(exactly = 1) {
            submitConsentUseCase(parameter)
        }
    }

}
