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
import kotlinx.coroutines.delay
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

    private val dummyUsername = "dummy_username"

    private val dummyUsernameTwo = "dummy_other_username"

    private val dummyBio = "dummy_bio"

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

    @Test
    fun `success get profile feed`() {
        //Given
        val dummyResponse = ProfileFeedResponse()
        coEvery {
            profileFeedUsecase(Unit)
        } returns dummyResponse

        //When
        viewModel.getProfileFeed()

        //Then
        coVerify(exactly = 1) {
            profileFeedUsecase(Unit)
            profileFeedObserver.onChanged(viewModel.profileFeed.value)
        }
    }

    @Test
    fun `fail get profile feed`() {
        //Given
        val dummyError = Throwable("")
        coEvery {
            profileFeedUsecase(Unit)
        } throws dummyError

        //When
        viewModel.getProfileFeed()

        //Then
        coVerify(exactly = 1) {
            profileFeedUsecase(Unit)
            profileFeedObserver.onChanged(viewModel.profileFeed.value)

        }
    }

    @Test
    fun `validate username without cancel previous request`() {
        //Given
        val dummyResponse = UsernameValidationResponse(UsernameValidation(isValid = true))
        coEvery { validateUsecase(any()) } returns dummyResponse

        //When
        viewModel.validateUsername(dummyUsername)

        //Then
        coVerify(timeout = 3000) {
            validationUsernameObserver.onChanged(Success(dummyResponse.response))
        }
    }

    @Test
    fun `validate username and cancel previous request`() {
        //Given
        var dummyResponseOne = UsernameValidationResponse(UsernameValidation(isValid = true))
        val dummyResponseTwo = UsernameValidationResponse(UsernameValidation(isValid = false))
        coEvery { validateUsecase(dummyUsername) } returns dummyResponseOne
        coEvery { validateUsecase(dummyUsernameTwo) } returns dummyResponseTwo

        //When
        viewModel.validateUsername(dummyUsername)
        Thread.sleep(1000)
        viewModel.validateUsername(dummyUsernameTwo)

        //Then
        coVerify(timeout = 5000) {
            validationUsernameObserver.onChanged(Success(dummyResponseTwo.response))
        }

    }

    @Test
    fun `validate username and throw error`() {
        //Given
        val dummyError = Throwable("")
        coEvery { validateUsecase(any()) } throws dummyError

        //When
        viewModel.validateUsername(dummyUsername)

        //Then
        coVerify(timeout = 3000) {
            validationUsernameObserver.onChanged(Fail(dummyError))
        }
    }


    @Test
    fun `submit valid username and success`() {
        //Given
        val dummyResponseSubmit = SubmitBioUsernameResponse(SubmitBioUsername(status = true))
        val dummyResponse = UsernameValidationResponse(UsernameValidation(isValid = true))
        coEvery { validateUsecase(any()) } returns dummyResponse
        coEvery {
            submitProfileUsecase(any())
        } returns dummyResponseSubmit

        //When
        viewModel.submitUsername(dummyUsername)

        //Then
        coVerify(timeout = 3000) {
            submitProfileUsecase(any())
            submitUsernameObserver.onChanged(Success(dummyResponseSubmit.response))
        }
    }

    @Test
    fun `submit valid username and fail`() {
        //Given
        val dummyError = Throwable("")
        val dummyResponseSubmit = SubmitBioUsernameResponse(SubmitBioUsername(status = false))
        val dummyResponse = UsernameValidationResponse(UsernameValidation(isValid = true))
        coEvery { validateUsecase(any()) } returns dummyResponse
        coEvery {
            submitProfileUsecase(any())
        } throws dummyError

        //When
        viewModel.submitUsername(dummyUsername)

        //Then
        coVerify(timeout = 3000) {
            submitProfileUsecase(any())
            submitUsernameObserver.onChanged(Fail(dummyError))
        }
    }

    @Test
    fun `submit valid username and throw error`() {
        //Given
        val dummyResponse = UsernameValidationResponse(UsernameValidation(isValid = true))
        val dummyError = Throwable()
        coEvery { validateUsecase(any()) } returns dummyResponse
        coEvery {
            submitProfileUsecase(SubmitProfileParam(username = dummyUsername, isUpdateUsername = true))
        } throws dummyError

        //When
        viewModel.submitUsername(dummyUsername)

        //Then
        coVerify(timeout = 3000) {
            submitProfileUsecase(any())
            submitUsernameObserver.onChanged(Fail(dummyError))
        }
    }

    @Test
    fun `submit bio and success`() {
        //Given
        val dummyResponseSubmit = SubmitBioUsernameResponse(SubmitBioUsername(status = false))
        coEvery {
            submitProfileUsecase(any())
        } returns dummyResponseSubmit

        //When
        viewModel.submitBio(dummyBio)


        //Then
        coVerify {
            submitProfileUsecase(any())
            submitBioObserver.onChanged(Success(dummyResponseSubmit.response))
        }
    }

    @Test
    fun `submit bio and throw error`() {
        //Given
        val dummyError = Throwable("")
        coEvery {
            submitProfileUsecase(any())
        } throws dummyError

        //When
        viewModel.submitBio(dummyBio)


        //Then
        coVerify {
            submitProfileUsecase(any())
            submitBioObserver.onChanged(Fail(dummyError))
        }
    }

    @Test
    fun `submit invalid username`() {
        //Given
        val dummyResponse = UsernameValidationResponse(UsernameValidation(isValid = false))
        coEvery { validateUsecase(any()) } returns dummyResponse

        //When
        viewModel.submitUsername(dummyUsername)

        //Then
        coVerify(timeout = 3000) {
            submitUsernameObserver.onChanged(viewModel.resultSubmitUsername.value)
        }
        coVerify(exactly = 0) { submitProfileUsecase(any()) }
    }

    @Test
    fun `submit username and throw error when validate`() {
        //Given
        val dummyError = Throwable("")
        coEvery { validateUsecase(any()) } throws  dummyError

        //When
        viewModel.submitUsername(dummyUsername)

        //Then
        coVerify(timeout = 3000) {
            validateUsecase(any())
            submitUsernameObserver.onChanged(viewModel.resultSubmitUsername.value)
        }
        coVerify(exactly = 0) { submitProfileUsecase(any()) }
    }

    @Test
    fun `cancel validate username job`() {
        //Given
        val dummyResponse = UsernameValidationResponse(UsernameValidation(isValid = false))
        coEvery { validateUsecase(any()) } returns dummyResponse

        //When
        viewModel.validateUsername(dummyUsername)
        viewModel.cancelValidation()

        //Then
        coVerify(exactly = 0) {
            validateUsecase(any())
        }
    }
}