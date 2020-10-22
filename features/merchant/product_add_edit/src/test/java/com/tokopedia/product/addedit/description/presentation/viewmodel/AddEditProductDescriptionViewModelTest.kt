package com.tokopedia.product.addedit.description.presentation.viewmodel

import android.net.Uri
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.common.network.data.model.RestResponse
import com.tokopedia.product.addedit.common.constant.AddEditProductConstants
import com.tokopedia.product.addedit.common.util.ResourceProvider
import com.tokopedia.product.addedit.description.presentation.model.VideoLinkModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.youtube_common.data.model.YoutubeVideoDetailModel
import com.tokopedia.youtube_common.domain.usecase.GetYoutubeVideoDetailUseCase
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.lang.reflect.Type

@ExperimentalCoroutinesApi
class AddEditProductDescriptionViewModelTest {
    @RelaxedMockK
    lateinit var resourceProvider: ResourceProvider

    @RelaxedMockK
    lateinit var getYoutubeVideoUseCase: GetYoutubeVideoDetailUseCase

    @RelaxedMockK
    lateinit var videoUri: Uri

    @RelaxedMockK
    lateinit var videoYoutubeObserver: Observer<in Pair<Int, Result<YoutubeVideoDetailModel>>>

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        mockkStatic(Uri::class)

        every {
            Uri.parse(any())
        } answers {
            if (usedYoutubeVideoUrl == youtubeVideoUrlFromApp ||
                    usedYoutubeVideoUrl == youtubeVideoUrlFromWebsite ||
                    usedYoutubeVideoUrl == unknownYoutubeUrl) videoUri
            else throw NullPointerException()
        }

        every {
            videoUri.lastPathSegment
        } returns videoId

        every {
            videoUri.host
        } answers {
            when (usedYoutubeVideoUrl) {
                youtubeVideoUrlFromApp -> youtubeAppHost
                youtubeVideoUrlFromWebsite -> youtubeWebsiteHost
                unknownYoutubeUrl -> unknownYoutubeHost
                else -> null
            }
        }

        every {
            videoUri.getQueryParameter(AddEditProductConstants.KEY_YOUTUBE_VIDEO_ID)
        } returns videoId

        viewModel.videoYoutube.observeForever(videoYoutubeObserver)
    }

    @After
    fun cleanup() {
        viewModel.videoYoutube.removeObserver(videoYoutubeObserver)
    }

    private val testCoroutineDispatcher = TestCoroutineDispatcher()

    private val viewModel: AddEditProductDescriptionViewModel by lazy {
        AddEditProductDescriptionViewModel(testCoroutineDispatcher, resourceProvider, getYoutubeVideoUseCase)
    }

    private val youtubeAppHost = "youtu.be"
    private val youtubeWebsiteHost = "www.youtube.com"
    private val unknownYoutubeHost = "google.com"
    private val videoId = "8UzbKepncNk"
    private val youtubeVideoUrlFromApp = "https://$youtubeAppHost/$videoId"
    private val youtubeVideoUrlFromWebsite = "https://$youtubeWebsiteHost/watch?v=$videoId"
    private val unknownYoutubeUrl = "https://$unknownYoutubeHost/$videoId"
    private var usedYoutubeVideoUrl = ""

    private val youtubeSuccessData = YoutubeVideoDetailModel()
    private val youtubeRestResponse = RestResponse(youtubeSuccessData, 200, false)
    private val youtubeSuccessRestResponseMap = mapOf<Type, RestResponse>(
            YoutubeVideoDetailModel::class.java to youtubeRestResponse
    )

    @Test
    fun `When user insert url from youtube app and usecase is success expect youtube video data`() = runBlocking {
        usedYoutubeVideoUrl = youtubeVideoUrlFromApp

        coEvery {
            getYoutubeVideoUseCase.executeOnBackground()
        } returns youtubeSuccessRestResponseMap

        viewModel.getVideoYoutube(usedYoutubeVideoUrl, 0)

        coVerify {
            getYoutubeVideoUseCase.executeOnBackground()
        }

        viewModel.coroutineContext[Job]?.children?.forEach { it.join() }

        val result = viewModel.videoYoutube.value
        assert(result != null && result == Pair(0, Success(youtubeSuccessData)))
    }

    @Test
    fun `When user insert url from youtube web and usecase is success expect youtube video data`() = runBlocking {
        usedYoutubeVideoUrl = youtubeVideoUrlFromWebsite

        coEvery {
            getYoutubeVideoUseCase.executeOnBackground()
        } returns youtubeSuccessRestResponseMap

        viewModel.getVideoYoutube(usedYoutubeVideoUrl, 0)

        coVerify {
            getYoutubeVideoUseCase.executeOnBackground()
        }

        viewModel.coroutineContext[Job]?.children?.forEach { it.join() }

        val result = viewModel.videoYoutube.value
        assert(result != null && result == Pair(0, Success(youtubeSuccessData)))
    }

    @Test
    fun `When user insert url with unknown host expect failed get youtube video data`() {
        usedYoutubeVideoUrl = unknownYoutubeUrl

        viewModel.getVideoYoutube(usedYoutubeVideoUrl, 0)

        val result = viewModel.videoYoutube.value
        assert(result != null && result.second is Fail)
    }

    @Test
    fun `When the url is null expect failed get youtube video data`() {
        usedYoutubeVideoUrl = unknownYoutubeUrl

        viewModel.getVideoYoutube(usedYoutubeVideoUrl, 0)

        val result = viewModel.videoYoutube.value
        assert(result != null && result.second is Fail)
    }

    @Test
    fun `When get youtube video usecase is throwing an error expect failed get youtube video data`() {
        val throwable = Throwable("")

        coEvery {
            getYoutubeVideoUseCase.executeOnBackground()
        } throws throwable

        viewModel.getVideoYoutube(usedYoutubeVideoUrl, 0)

        Thread.sleep(100)
        val result = viewModel.videoYoutube.value
        assert(result != null && result.second is Fail)
    }

    @Test
    fun `When there are 2 or more same video url expect produce duplicate video error message`() {
        val addedVideoUrls = mutableListOf(
                VideoLinkModel("https://youtu.be/$videoId"),
                VideoLinkModel("https://youtu.be/$videoId")
        )
        val newVideoUrl = "https://youtu.be/$videoId"

        every { resourceProvider.getDuplicateProductVideoErrorMessage() }  returns "Link video tidak boleh sama"

        val result = viewModel.validateDuplicateVideo(addedVideoUrls, newVideoUrl)
        assert(result == "Link video tidak boleh sama")
    }

    @Test
    fun `When every video url is unique expect produce empty error message`() {
        val addedVideoUrls = mutableListOf(
                VideoLinkModel("https://youtu.be/d1kf1887aKl")
        )
        val newVideoUrl = "https://youtu.be/$videoId"

        assert(viewModel.validateDuplicateVideo(addedVideoUrls, newVideoUrl).isEmpty())
    }

    @Test
    fun `When there are one or more video link model with error message expect return false on validate video input`() {
        val videoUrls = mutableListOf(
                VideoLinkModel(errorMessage = "Pastikan link Youtube kamu benar")
        )

        assert(!viewModel.validateInputVideo(videoUrls))
    }

    @Test
    fun `When every video link model is not containing an error message expect return true on validate video input`() {
        val videoUrls = mutableListOf(
                VideoLinkModel("https://youtu.be/$videoId")
        )

        assert(viewModel.validateInputVideo(videoUrls))
    }
}