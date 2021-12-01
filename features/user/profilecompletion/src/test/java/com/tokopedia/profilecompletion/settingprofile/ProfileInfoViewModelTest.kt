package com.tokopedia.profilecompletion.settingprofile

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.profilecompletion.data.UploadProfileImageModel
import com.tokopedia.profilecompletion.settingprofile.data.ProfileCompletionData
import com.tokopedia.profilecompletion.settingprofile.data.SubmitProfilePictureData
import com.tokopedia.profilecompletion.settingprofile.data.UploadProfilePictureResult
import com.tokopedia.profilecompletion.settingprofile.data.UserProfileInfoData
import com.tokopedia.profilecompletion.settingprofile.domain.UploadProfilePictureUseCase
import com.tokopedia.profilecompletion.settingprofile.viewmodel.ProfileInfoViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.hamcrest.CoreMatchers
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import rx.Subscriber

/**
 * Created by Yoris Prayogo on 29/06/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */
class ProfileInfoViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    val userProfileInfoUseCase = mockk<GraphqlUseCase<UserProfileInfoData>>(relaxed = true)
    val uploadProfilePictureUseCase = mockk<UploadProfilePictureUseCase>(relaxed = true)
    val submitProfilePictureUseCase = mockk<GraphqlUseCase<SubmitProfilePictureData>>(relaxed = true)
    private var userSessionInterface = mockk<UserSessionInterface>(relaxed = true)

    val context = mockk<Context>(relaxed = true)

    private val testDispatcher = TestCoroutineDispatcher()

    private var userProfileObserver = mockk<Observer<Result<ProfileCompletionData>>>(relaxed = true)
    private var uploadProfileObserver = mockk<Observer<Result<UploadProfilePictureResult>>>(relaxed = true)

    lateinit var viewModel: ProfileInfoViewModel

    private var userProfileInfoData = UserProfileInfoData()
    private var profilePict = "http://tokopedia.com/test.jpg"
    private var mockThrowable = mockk<Throwable>(relaxed = true)
    val uploadProfileImageModel = UploadProfileImageModel()
    val submitProfilePictureData = SubmitProfilePictureData()
    val pictObj = "aabbcc"

    @Before
    fun setUp() {
        viewModel = ProfileInfoViewModel(
                userProfileInfoUseCase,
                uploadProfilePictureUseCase,
                submitProfilePictureUseCase,
                userSessionInterface,
                testDispatcher
        )
        viewModel.userProfileInfo.observeForever(userProfileObserver)
        viewModel.uploadProfilePictureResponse.observeForever(uploadProfileObserver)
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
    fun `on uploadProfilePicture executed`() {

        viewModel.uploadProfilePicture(context, profilePict)

        /* Then */
        verify {
            uploadProfilePictureUseCase.execute(any(), any())
        }
    }

    @Test
    fun `on uploadProfilePicture Success`() {

        uploadProfileImageModel.data.picObj = "aabbcc"

        every { uploadProfilePictureUseCase.execute(any(), any()) } answers {
            (secondArg() as Subscriber<UploadProfileImageModel>).onNext(uploadProfileImageModel)
        }

        viewModel.uploadProfilePicture(context, profilePict)

        /* Then */
        verify {
            submitProfilePictureUseCase.setTypeClass(SubmitProfilePictureData::class.java)
            submitProfilePictureUseCase.setRequestParams(any())
            submitProfilePictureUseCase.setGraphqlQuery(any<String>())
            submitProfilePictureUseCase.execute(any(), any())
        }
    }

    @Test
    fun `on uploadProfilePicture Error`() {

        uploadProfileImageModel.data.picObj = ""

        every { uploadProfilePictureUseCase.execute(any(), any()) } answers {
            (secondArg() as Subscriber<UploadProfileImageModel>).onError(mockThrowable)
        }

        viewModel.uploadProfilePicture(context, profilePict)

        /* Then */
        verify(atLeast = 1){ uploadProfileObserver.onChanged(any()) }
        Assert.assertThat(viewModel.uploadProfilePictureResponse.value, CoreMatchers.instanceOf(Fail::class.java))
        Assert.assertThat((viewModel.uploadProfilePictureResponse.value as Fail).throwable, CoreMatchers.instanceOf(Throwable::class.java))
    }

    @Test
    fun `on Change Picture success`() {
        submitProfilePictureData.changePictureData.isSuccess = 1
        uploadProfileImageModel.data.picObj = pictObj

        every { uploadProfilePictureUseCase.execute(any(), any()) } answers {
            (secondArg() as Subscriber<UploadProfileImageModel>).onNext(uploadProfileImageModel)
        }

        every { submitProfilePictureUseCase.execute(any(), any()) } answers {
            firstArg<(SubmitProfilePictureData) -> Unit>().invoke(submitProfilePictureData)
        }

        viewModel.uploadProfilePicture(context, profilePict)

        /* Then */
        verify { uploadProfileObserver.onChanged(any()) }
        Assert.assertThat(viewModel.uploadProfilePictureResponse.value, CoreMatchers.instanceOf(Success::class.java))
        Assert.assertThat((viewModel.uploadProfilePictureResponse.value as Success<UploadProfilePictureResult>).data, CoreMatchers.instanceOf(UploadProfilePictureResult::class.java))
        assertEquals(pictObj, (viewModel.uploadProfilePictureResponse.value as Success<UploadProfilePictureResult>).data.uploadProfileImageModel.data.picObj)
    }

    @Test
    fun `on Change Picture Error message not empty`() {
        val error = "Error"
        submitProfilePictureData.changePictureData.isSuccess = 0
        submitProfilePictureData.changePictureData.error = error

        uploadProfileImageModel.data.picObj = pictObj

        every { uploadProfilePictureUseCase.execute(any(), any()) } answers {
            (secondArg() as Subscriber<UploadProfileImageModel>).onNext(uploadProfileImageModel)
        }

        every { submitProfilePictureUseCase.execute(any(), any()) } answers {
            firstArg<(SubmitProfilePictureData) -> Unit>().invoke(submitProfilePictureData)
        }

        viewModel.uploadProfilePicture(context, profilePict)

        /* Then */
        verify { uploadProfileObserver.onChanged(any()) }
        Assert.assertThat(viewModel.uploadProfilePictureResponse.value, CoreMatchers.instanceOf(Fail::class.java))
        Assert.assertThat((viewModel.uploadProfilePictureResponse.value as Fail).throwable, CoreMatchers.instanceOf(com.tokopedia.abstraction.common.network.exception.MessageErrorException::class.java))
        assertEquals(error, (viewModel.uploadProfilePictureResponse.value as Fail).throwable.message)
    }

    @Test
    fun `on another error Change Picture`() {
        submitProfilePictureData.changePictureData.isSuccess = 0
        submitProfilePictureData.changePictureData.error = ""

        uploadProfileImageModel.data.picObj = pictObj

        every { uploadProfilePictureUseCase.execute(any(), any()) } answers {
            (secondArg() as Subscriber<UploadProfileImageModel>).onNext(uploadProfileImageModel)
        }

        every { submitProfilePictureUseCase.execute(any(), any()) } answers {
            firstArg<(SubmitProfilePictureData) -> Unit>().invoke(submitProfilePictureData)
        }

        viewModel.uploadProfilePicture(context, profilePict)

        /* Then */
        verify { uploadProfileObserver.onChanged(any()) }
        Assert.assertThat(viewModel.uploadProfilePictureResponse.value, CoreMatchers.instanceOf(Fail::class.java))
        Assert.assertThat((viewModel.uploadProfilePictureResponse.value as Fail).throwable, CoreMatchers.instanceOf(RuntimeException::class.java))
    }

}