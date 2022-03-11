package com.tokopedia.profilecompletion.profileinfo

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.mediauploader.UploaderUseCase
import com.tokopedia.profilecompletion.profileinfo.data.ProfileFeedResponse
import com.tokopedia.profilecompletion.profileinfo.data.ProfileInfoResponse
import com.tokopedia.profilecompletion.profileinfo.data.ProfileInfoUiModel
import com.tokopedia.profilecompletion.profileinfo.data.ProfileRoleResponse
import com.tokopedia.profilecompletion.profileinfo.usecase.ProfileFeedInfoUseCase
import com.tokopedia.profilecompletion.profileinfo.usecase.ProfileInfoUseCase
import com.tokopedia.profilecompletion.profileinfo.usecase.ProfileRoleUseCase
import com.tokopedia.profilecompletion.profileinfo.usecase.SaveProfilePictureUseCase
import com.tokopedia.profilecompletion.profileinfo.viewmodel.ProfileViewModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ProfileViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @RelaxedMockK
    private lateinit var profileInfoUsecase: ProfileInfoUseCase

    @RelaxedMockK
    private lateinit var profileRoleUsecase: ProfileRoleUseCase

    @RelaxedMockK
    private lateinit var profileFeedInfoUsecase: ProfileFeedInfoUseCase

    @RelaxedMockK
    private lateinit var uploaderUsecase: UploaderUseCase

    @RelaxedMockK
    private lateinit var saveProfilePictureUsecase: SaveProfilePictureUseCase

    @RelaxedMockK
    private lateinit var userSession: UserSessionInterface

    @RelaxedMockK
    private lateinit var profileInfoObserver: Observer<ProfileInfoUiModel>

    @RelaxedMockK
    private lateinit var errorMessageObserver: Observer<String>

    @RelaxedMockK
    private lateinit var savePhotoObserver: Observer<String>

    private lateinit var viewModel: ProfileViewModel

    private val dummyUserId = "123"

    private val dummyProfileInfo = ProfileInfoResponse()
    private val dummyProfilerole = ProfileRoleResponse()
    private val dummyProfileFeed = ProfileFeedResponse()

        @Before
    fun before() {
        MockKAnnotations.init(this)
        viewModel = ProfileViewModel(profileInfoUsecase, profileRoleUsecase, profileFeedInfoUsecase,
            uploaderUsecase, saveProfilePictureUsecase, userSession, CoroutineTestDispatchersProvider)
        viewModel.profileInfoUiData.observeForever(profileInfoObserver)
        viewModel.errorMessage.observeForever(errorMessageObserver)
        viewModel.saveImageProfileResponse.observeForever(savePhotoObserver)
    }

    @Test
    fun `success get profile info`() {
        //Given
        coEvery { profileInfoUsecase(any()) } returns dummyProfileInfo
        coEvery { profileRoleUsecase(any()) } returns dummyProfilerole
        coEvery { profileFeedInfoUsecase(any()) } returns dummyProfileFeed

        //When
        viewModel.getProfileInfo()

        //Then
        coVerify(exactly = 1) {
            profileInfoUsecase(any())
            profileRoleUsecase(any())
            profileFeedInfoUsecase(any())
        }

    }
}