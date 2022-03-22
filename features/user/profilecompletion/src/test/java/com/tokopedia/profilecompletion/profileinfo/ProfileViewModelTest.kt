package com.tokopedia.profilecompletion.profileinfo

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.mediauploader.UploaderUseCase
import com.tokopedia.mediauploader.common.state.UploadResult
import com.tokopedia.profilecompletion.profileinfo.data.*
import com.tokopedia.profilecompletion.profileinfo.usecase.ProfileFeedInfoUseCase
import com.tokopedia.profilecompletion.profileinfo.usecase.ProfileInfoUseCase
import com.tokopedia.profilecompletion.profileinfo.usecase.ProfileRoleUseCase
import com.tokopedia.profilecompletion.profileinfo.usecase.SaveProfilePictureUseCase
import com.tokopedia.profilecompletion.profileinfo.viewmodel.ProfileViewModel
import com.tokopedia.profilecompletion.settingprofile.data.SaveProfilePictureData
import com.tokopedia.profilecompletion.settingprofile.data.SaveProfilePictureInnerData
import com.tokopedia.profilecompletion.settingprofile.data.SaveProfilePictureResponse
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import net.bytebuddy.implementation.bytecode.Throw
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.File

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
    private lateinit var errorMessageObserver: Observer<ProfileInfoError>

    @RelaxedMockK
    private lateinit var savePhotoObserver: Observer<String>

    private lateinit var viewModel: ProfileViewModel


    private val dummyError = Exception("")
    private val dummyImageUrl = "dummy_image_url"
    private val dummyErrorMsg = "failed_save_picture"
    private val dummyProfileInfo = ProfileInfoResponse()
    private val dummyProfilerole = ProfileRoleResponse()
    private val dummyProfileFeed = ProfileFeedResponse()
    private val dummyUploadResultSuccess = UploadResult.Success("123", "")
    private val dummyUploadResultFailed = UploadResult.Error("")
    private val dummySaveProfilePictureSuccess = SaveProfilePictureResponse(SaveProfilePictureData(
        "", emptyList(), SaveProfilePictureInnerData(imageUrl = dummyImageUrl, isSuccess = 1)))
    private val dummySaveProfilePictureFailed = SaveProfilePictureResponse(SaveProfilePictureData(
        "", listOf(dummyErrorMsg), SaveProfilePictureInnerData(imageUrl = "", isSuccess = 0)))
    private val dummyProfileUIModel = ProfileInfoUiModel(
        profileInfoData = dummyProfileInfo.profileInfoData,
        profileRoleData = dummyProfilerole.profileRole,
        profileFeedData = dummyProfileFeed.profileFeedData
    )

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
        coEvery { profileInfoUsecase(Unit) } returns dummyProfileInfo
        coEvery { profileRoleUsecase(Unit) } returns dummyProfilerole
        coEvery { profileFeedInfoUsecase(any()) } returns dummyProfileFeed

        //When
        viewModel.getProfileInfo()

        //Then
        coVerify(exactly = 1) {
            profileInfoUsecase(Unit)
            profileRoleUsecase(Unit)
            profileFeedInfoUsecase(any())
            profileInfoObserver.onChanged(dummyProfileUIModel)
        }
    }

    @Test
    fun `failed get profile info`() {
        //Given
        coEvery { profileInfoUsecase(Unit) } throws dummyError
        coEvery { profileRoleUsecase(Unit) } returns dummyProfilerole
        coEvery { profileFeedInfoUsecase(any()) } returns dummyProfileFeed

        //When
        viewModel.getProfileInfo()

        //Then
        coVerify(exactly = 1) {
            profileInfoUsecase(Unit)
            errorMessageObserver.onChanged(ProfileInfoError.GeneralError(dummyError))
        }
    }

    @Test
    fun `failed get profile role`() {
        //Given
        coEvery { profileInfoUsecase(Unit) } returns dummyProfileInfo
        coEvery { profileRoleUsecase(Unit) } throws dummyError
        coEvery { profileFeedInfoUsecase(any()) } returns dummyProfileFeed

        //When
        viewModel.getProfileInfo()

        //Then
        coVerify(exactly = 1) {
            profileRoleUsecase(Unit)
            errorMessageObserver.onChanged(viewModel.errorMessage.value)
        }
    }

    @Test
    fun `failed get profile feed`() {
        //Given
        coEvery { profileInfoUsecase(Unit) } returns dummyProfileInfo
        coEvery { profileRoleUsecase(Unit) } returns dummyProfilerole
        coEvery { profileFeedInfoUsecase(any()) } throws dummyError

        //When
        viewModel.getProfileInfo()

        //Then
        coVerify(exactly = 1) {
            profileFeedInfoUsecase(any())
            errorMessageObserver.onChanged(viewModel.errorMessage.value)
        }
    }

    @Test
    fun `upload picture success and success save picture `() {
        //Given
        coEvery { uploaderUsecase(any()) } returns dummyUploadResultSuccess
        coEvery { saveProfilePictureUsecase(any()) } returns dummySaveProfilePictureSuccess

        //When
        viewModel.uploadPicture(File(""))

        //Then
        coVerify {
            uploaderUsecase(any())
            saveProfilePictureUsecase(any())
            savePhotoObserver.onChanged(dummyImageUrl)
        }
    }

    @Test
    fun `upload picture success and failed save picture`() {
        //Given
        coEvery { uploaderUsecase(any()) } returns dummyUploadResultSuccess
        coEvery { saveProfilePictureUsecase(any()) } returns dummySaveProfilePictureFailed

        //When
        viewModel.uploadPicture(File(""))

        //Then
        coVerify {
            uploaderUsecase(any())
            saveProfilePictureUsecase(any())
            errorMessageObserver.onChanged(ProfileInfoError.ErrorSavePhoto(dummyErrorMsg))
        }
    }

    @Test
    fun `upload picture success and throws error`() {
        //Given
        coEvery { uploaderUsecase(any()) } returns dummyUploadResultSuccess
        coEvery { saveProfilePictureUsecase(any()) } throws dummyError

        //When
        viewModel.uploadPicture(File(""))

        //Then
        coVerify {
            uploaderUsecase(any())
            saveProfilePictureUsecase(any())
            errorMessageObserver.onChanged(ProfileInfoError.ErrorSavePhoto(dummyError.message))
        }
    }


    @Test
    fun `upload picture failed`() {
        coEvery { uploaderUsecase(any()) } returns dummyUploadResultFailed

        //When
        viewModel.uploadPicture(File(""))

        //Then
        coVerify {
            uploaderUsecase(any())
            errorMessageObserver.onChanged(ProfileInfoError.ErrorSavePhoto(dummyUploadResultFailed.message))
        }
    }

    @Test
    fun `upload picture throws error`() {
        val dummyErrors = Exception("")

        coEvery { uploaderUsecase(any()) } throws dummyErrors

        //When
        viewModel.uploadPicture(File(""))

        //Then
        coVerify {
            uploaderUsecase(any())
            errorMessageObserver.onChanged(ProfileInfoError.ErrorSavePhoto(dummyErrors.message))
        }
    }
}