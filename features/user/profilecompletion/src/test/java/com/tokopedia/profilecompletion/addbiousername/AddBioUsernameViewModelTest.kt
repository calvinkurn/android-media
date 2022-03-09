package com.tokopedia.profilecompletion.addbiousername

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.profilecompletion.changebiousername.data.*
import com.tokopedia.profilecompletion.changebiousername.domain.usecase.SubmitBioUsernameUseCase
import com.tokopedia.profilecompletion.changebiousername.domain.usecase.ValidateUsernameUseCase
import com.tokopedia.profilecompletion.changebiousername.viewmodel.ChangeBioUsernameViewModel
import com.tokopedia.profilecompletion.profileinfo.data.ProfileFeedData
import com.tokopedia.profilecompletion.profileinfo.data.ProfileFeedResponse
import com.tokopedia.profilecompletion.profileinfo.usecase.ProfileFeedInfoUseCase
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertTrue

class AddBioUsernameViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @RelaxedMockK
    private lateinit var profileFeedUsecase: ProfileFeedInfoUseCase

    @RelaxedMockK
    private lateinit var validateUsecase: ValidateUsernameUseCase

    @RelaxedMockK
    private lateinit var submitProfileUsecase: SubmitBioUsernameUseCase

    @RelaxedMockK
    private lateinit var userSession: UserSessionInterface

    @RelaxedMockK
    private lateinit var profileFeedObserver: Observer<Result<ProfileFeedData>>

    @RelaxedMockK
    private lateinit var validationUsernameObserver: Observer<Result<UsernameValidation>>

    @RelaxedMockK
    private lateinit var submitUsernameObserver: Observer<Result<SubmitBioUsername>>

    @RelaxedMockK
    private lateinit var submitBioObserver: Observer<Result<SubmitBioUsername>>

    @RelaxedMockK
    private lateinit var loadingStateObserver: Observer<Boolean>

    private lateinit var viewModel: ChangeBioUsernameViewModel

    private val dummyUserId = "123"

    private val dummyUsername = "dummy_username"

    @Before
    fun before() {
        MockKAnnotations.init(this)
        viewModel = ChangeBioUsernameViewModel(profileFeedUsecase,
            validateUsecase, submitProfileUsecase,
            userSession,  CoroutineTestDispatchersProvider)
        viewModel.profileFeed.observeForever(profileFeedObserver)
        viewModel.resultSubmitBio.observeForever(submitBioObserver)
        viewModel.resultSubmitUsername.observeForever(submitUsernameObserver)
        viewModel.resultValidationUsername.observeForever(validationUsernameObserver)
        viewModel.loadingState.observeForever(loadingStateObserver)
    }

//    @Test
//    fun `success get profile feed`() {
//        //Given
//        val dummyResponse = ProfileFeedResponse()
//        coEvery {
//            profileFeedUsecase(any())
//        } returns dummyResponse
//
//        //When
//        viewModel.getProfileFeed()
//
//        //Then
//        coVerify(exactly = 1) {
//            profileFeedUsecase(any())
//            profileFeedObserver.onChanged(viewModel.profileFeed.value)
//        }
//    }
//
//    @Test
//    fun `fail get profile feed`() {
//        //Given
//        val dummyError = Throwable("")
//        coEvery {
//            profileFeedUsecase(any())
//        } throws dummyError
//
//        //When
//        viewModel.getProfileFeed()
//
//        //Then
//        coVerify(exactly = 1) {
//            profileFeedUsecase(any())
//            profileFeedObserver.onChanged(viewModel.profileFeed.value)
//
//        }
//    }
//
//    @Test
//    fun `validate username without cancel previous request`() {
//        //Given
//        val dummyResponse = UsernameValidationResponse()
//        coEvery { validateUsecase(any()) } returns dummyResponse
//
//        //When
//        viewModel.validateUsername(dummyUsername)
//
//        //Then
//        coVerify(exactly = 1) {
//            validationUsernameObserver.onChanged(viewModel.resultValidationUsername.value)
//        }
//    }
//
//    @Test
//    fun `validate username and cancel previous request`() {
//        //Given
//        val dummyResponse = UsernameValidationResponse()
//        coEvery { validateUsecase(any()) } returns dummyResponse
//
//        //When
//        viewModel.validateUsername(dummyUsername)
//        viewModel.validateUsername(dummyUsername)
//
//        //Then
//        coVerify() {
//            viewModel.cancelValidation()
//            validationUsernameObserver.onChanged(viewModel.resultValidationUsername.value)
//        }
//
//    }
//
//    @Test
//    fun `validate username and throw error`() {
//        //Given
//        val dummyError = Throwable("")
//        coEvery { validateUsecase(any()) } throws dummyError
//
//        //When
//        viewModel.validateUsername(dummyUsername)
//
//        //Then
//        coVerify {
//            validationUsernameObserver.onChanged(viewModel.resultValidationUsername.value)
//        }
//    }
//
//
//    @Test
//    fun `submit valid username and success`() {
//        //Given
//        val dummyResponseSubmit = SubmitBioUsernameResponse()
//        val dummyResponse = UsernameValidationResponse(UsernameValidation(isValid = true))
//        coEvery { validateUsecase(any()) } returns dummyResponse
//        coEvery {
//            submitProfileUsecase(SubmitProfileParam(username = dummyUsername, isUpdateUsername = true))
//        } returns dummyResponseSubmit
//
//        //When
//        viewModel.submitUsername(dummyUsername)
//
//        //Then
//        coVerify {
//            submitProfileUsecase(any())
//            submitUsernameObserver.onChanged(viewModel.resultSubmitUsername.value)
//        }
//        assertTrue(viewModel.resultSubmitUsername.value is Success)
//
//    }
//
//    @Test
//    fun `submit valid username and throw error`() {
//        //Given
//        val dummyResponse = UsernameValidationResponse(UsernameValidation(isValid = true))
//        val dummyError = Throwable()
//        coEvery { validateUsecase(any()) } returns dummyResponse
//        coEvery {
//            submitProfileUsecase(SubmitProfileParam(username = dummyUsername, isUpdateUsername = true))
//        } throws dummyError
//
//        //When
//        viewModel.submitUsername(dummyUsername)
//
//        //Then
//        coVerify {
//            submitProfileUsecase(any())
//            submitUsernameObserver.onChanged(viewModel.resultSubmitUsername.value)
//        }
//        assertTrue(viewModel.resultSubmitUsername.value is Fail)
//    }
//
//    @Test
//    fun `submit bio and success`() {
//
//    }
//
//    @Test
//    fun `submit bio and throw error`() {
//
//    }
//
//    @Test
//    fun `submit invalid username`() {
//
//    }
}