package com.tokopedia.profilecompletion.settingprofile

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.mediauploader.UploaderUseCase
import com.tokopedia.mediauploader.common.state.UploadResult
import com.tokopedia.profilecompletion.settingprofile.data.*
import com.tokopedia.profilecompletion.settingprofile.domain.SaveProfilePictureUseCase
import com.tokopedia.profilecompletion.settingprofile.viewmodel.ProfileInfoViewModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.hamcrest.CoreMatchers
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.File

/**
 * Created by Yoris Prayogo on 29/06/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */
class ProfileInfoViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    val userProfileInfoUseCase = mockk<GraphqlUseCase<UserProfileInfoData>>(relaxed = true)
    val uploader = mockk<UploaderUseCase>(relaxed = true)
    val saveProfilePictureUseCase = mockk<SaveProfilePictureUseCase>(relaxed = true)
    private var userSessionInterface = mockk<UserSessionInterface>(relaxed = true)

    val context = mockk<Context>(relaxed = true)
    val mockFile = mockk<File>(relaxed = true)

    private var userProfileObserver = mockk<Observer<Result<ProfileCompletionData>>>(relaxed = true)
    private var uploadProfileObserver = mockk<Observer<Result<UploadProfilePictureResult>>>(relaxed = true)
    private var saveImageProfileObserver = mockk<Observer<Result<String>>>(relaxed = true)

    lateinit var viewModel: ProfileInfoViewModel

    private var userProfileInfoData = UserProfileInfoData()
    private var mockThrowable = mockk<Throwable>(relaxed = true)
    private var mockException = mockk<Exception>(relaxed = true)

    private var profilePict = "https://tokopedia.com/test-profile-info.jpg"
    private var uploadId = "abc123"
    @Before
    fun setUp() {
        viewModel = ProfileInfoViewModel(
                userProfileInfoUseCase,
                uploader,
                saveProfilePictureUseCase,
                userSessionInterface,
                CoroutineTestDispatchersProvider
        )
        viewModel.userProfileInfo.observeForever(userProfileObserver)
        viewModel.uploadProfilePictureResponse.observeForever(uploadProfileObserver)
        viewModel.saveImageProfileResponse.observeForever(saveImageProfileObserver)
    }

    @Test
    fun `on getUserProfileInfo executed`() {

        viewModel.getUserProfileInfo(context)

        /* Then */
        verify {
            userProfileInfoUseCase.setGraphqlQuery(any<String>())
            userProfileInfoUseCase.execute(any(), any())
        }
    }

    @Test
    fun `on getUserProfileInfo Success`() {
        every { userSessionInterface.profilePicture } returns profilePict
        every { userProfileInfoUseCase.execute(any(), any()) } answers {
            firstArg<(UserProfileInfoData) -> Unit>().invoke(userProfileInfoData)
        }

        viewModel.getUserProfileInfo(context)

        /* Then */
        verify { userProfileObserver.onChanged(any()) }
        Assert.assertThat(viewModel.userProfileInfo.value, CoreMatchers.instanceOf(Success::class.java))
        assertEquals(userSessionInterface.profilePicture, userProfileInfoData.profileCompletionData.profilePicture)
    }

    @Test
    fun `on getUserProfileInfo Error`() {

        every { userProfileInfoUseCase.execute(any(), any()) } answers {
            secondArg<(Throwable) -> Unit>().invoke(mockThrowable)
        }

        viewModel.getUserProfileInfo(context)

        /* Then */
        Assert.assertThat(viewModel.userProfileInfo.value, CoreMatchers.instanceOf(Fail::class.java))
        Assert.assertThat((viewModel.userProfileInfo.value as Fail).throwable, CoreMatchers.instanceOf(Throwable::class.java))
        verify(atLeast = 1){ userProfileObserver.onChanged(any()) }
    }

    @Test
    fun `on upload success` () {
        val mockUploadSuccess = mockk<UploadResult.Success>()
        val innerData = SaveProfilePictureInnerData(imageUrl = profilePict, isSuccess = 1)
        val mockSaveSuccess = SaveProfilePictureResponse(
            data = SaveProfilePictureData(
                status = "OK",
                innerData = innerData
            )
        )

        every { mockUploadSuccess.uploadId } returns "abc123"
        coEvery { saveProfilePictureUseCase(any()) } returns mockSaveSuccess
        coEvery { uploader(any()) } returns mockUploadSuccess

        viewModel.uploadPicture(mockFile)

        verify {
            saveImageProfileObserver.onChanged(Success(profilePict))
        }
    }

    @Test
    fun `on upload failed` () {
        val errMsg = "errors"
        val mockUploadError = mockk<UploadResult.Error>()

        every { mockUploadError.message } returns errMsg
        coEvery { uploader(any()) } returns mockUploadError

        viewModel.uploadPicture(mockFile)

        verify {
            saveImageProfileObserver.onChanged(any<Fail>())
        }
    }

    @Test
    fun `on save profile picture success`() {
        val innerData = SaveProfilePictureInnerData(imageUrl = profilePict, isSuccess = 1)
        val mockSaveSuccess = SaveProfilePictureResponse(
            data = SaveProfilePictureData(
                status = "OK",
                innerData = innerData
            )
        )

        coEvery { saveProfilePictureUseCase(any()) } returns mockSaveSuccess

        viewModel.saveProfilePicture(uploadId)

        verify {
            saveImageProfileObserver.onChanged(Success(profilePict))
        }
    }

    @Test
    fun `on save profile picture throw error`() {

        coEvery { saveProfilePictureUseCase(any()) } throws mockException
        viewModel.saveProfilePicture(uploadId)

        verify {
            saveImageProfileObserver.onChanged(Fail(mockException))
        }
    }

    @Test
    fun `on save profile picture errors is not empty`() {
        val innerData = SaveProfilePictureInnerData(imageUrl = profilePict, isSuccess = 1)
        val mockSaveSuccess = SaveProfilePictureResponse(
            data = SaveProfilePictureData(
                status = "OK",
                innerData = innerData,
                errorMessage = listOf("Errors")
            )
        )

        coEvery { saveProfilePictureUseCase(any()) } returns mockSaveSuccess

        viewModel.saveProfilePicture(uploadId)

        verify {
            saveImageProfileObserver.onChanged(any<Fail>())
        }
        assertEquals(mockSaveSuccess.data.errorMessage[0], (viewModel.saveImageProfileResponse.value as Fail).throwable.message)
    }

}