package com.tokopedia.profilecompletion.profilecompletion

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.mediauploader.UploaderUseCase
import com.tokopedia.mediauploader.common.state.UploadResult
import com.tokopedia.profilecompletion.domain.UserProfileCompletionUseCase
import com.tokopedia.profilecompletion.profilecompletion.data.SaveProfilePictureData
import com.tokopedia.profilecompletion.profilecompletion.data.SaveProfilePictureInnerData
import com.tokopedia.profilecompletion.profilecompletion.data.SaveProfilePictureResponse
import com.tokopedia.profilecompletion.profilecompletion.data.UserProfileInfoData
import com.tokopedia.profilecompletion.profilecompletion.viewmodel.ProfileInfoViewModel
import com.tokopedia.profilecompletion.profileinfo.usecase.SaveProfilePictureUseCase
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.unit.test.ext.getOrAwaitValue
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import org.hamcrest.CoreMatchers
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.File
import kotlin.test.assertTrue

/**
 * Created by Yoris Prayogo on 29/06/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */
class ProfileInfoViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    val uploader = mockk<UploaderUseCase>(relaxed = true)
    val saveProfilePictureUseCase = mockk<SaveProfilePictureUseCase>(relaxed = true)
    private var userSessionInterface = mockk<UserSessionInterface>(relaxed = true)

    val context = mockk<Context>(relaxed = true)
    val mockFile = mockk<File>(relaxed = true)

    private val userProfileCompletionUseCase = mockk<UserProfileCompletionUseCase>(relaxed = true)

    lateinit var viewModel: ProfileInfoViewModel

    private var userProfileInfoData = UserProfileInfoData()
    private var mockThrowable = mockk<Throwable>(relaxed = true)
    private var mockException = mockk<Exception>(relaxed = true)

    private var profilePict = "https://tokopedia.com/test-profile-info.jpg"
    private var uploadId = "abc123"
    @Before
    fun setUp() {
        viewModel = ProfileInfoViewModel(
                userProfileCompletionUseCase,
                uploader,
                saveProfilePictureUseCase,
                userSessionInterface,
                CoroutineTestDispatchersProvider
        )
    }

    @Test
    fun `on getUserProfileInfo Success`() {
        every { userSessionInterface.profilePicture } returns profilePict
        coEvery { userProfileCompletionUseCase(Unit) } returns userProfileInfoData

        viewModel.getUserProfileInfo()

        /* Then */
        val result = viewModel.userProfileInfo.getOrAwaitValue()
        Assert.assertThat(result, CoreMatchers.instanceOf(Success::class.java))
        assertEquals(userSessionInterface.profilePicture, userProfileInfoData.profileCompletionData.profilePicture)
    }

    @Test
    fun `on getUserProfileInfo Error`() {

        coEvery { userProfileCompletionUseCase(Unit) } throws mockThrowable

        viewModel.getUserProfileInfo()

        /* Then */
        val result = viewModel.userProfileInfo.getOrAwaitValue()
        Assert.assertThat(result, CoreMatchers.instanceOf(Fail::class.java))
        Assert.assertThat((result as Fail).throwable, CoreMatchers.instanceOf(Throwable::class.java))
        coVerify (atLeast = 1){ userProfileCompletionUseCase(Unit) }
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

        val result = viewModel.saveImageProfileResponse.getOrAwaitValue()
        assertEquals(Success(profilePict), result)
    }

    @Test
    fun `on upload failed` () {
        val errMsg = "errors"
        val mockUploadError = mockk<UploadResult.Error>()

        every { mockUploadError.message } returns errMsg
        coEvery { uploader(any()) } returns mockUploadError

        viewModel.uploadPicture(mockFile)

        val result = viewModel.saveImageProfileResponse.getOrAwaitValue()
        assertTrue(result is Fail)
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

        val result = viewModel.saveImageProfileResponse.getOrAwaitValue()
        assertEquals(Success(profilePict), result)
    }

    @Test
    fun `on save profile picture throw error`() {

        coEvery { saveProfilePictureUseCase(any()) } throws mockException
        viewModel.saveProfilePicture(uploadId)

        val result = viewModel.saveImageProfileResponse.getOrAwaitValue()
        assertEquals(Fail(mockException), result)
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

        val result = viewModel.saveImageProfileResponse.getOrAwaitValue()
        assertTrue(result is Fail)
        assertEquals(mockSaveSuccess.data.errorMessage[0], result.throwable.message)
    }

}
