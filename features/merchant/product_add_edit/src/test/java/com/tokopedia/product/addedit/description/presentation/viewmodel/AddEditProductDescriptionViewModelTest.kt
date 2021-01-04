package com.tokopedia.product.addedit.description.presentation.viewmodel

import android.net.Uri
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.tokopedia.common.network.data.model.RestResponse
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.product.addedit.common.constant.AddEditProductConstants
import com.tokopedia.product.addedit.common.util.ResourceProvider
import com.tokopedia.product.addedit.description.domain.usecase.ValidateProductDescriptionUseCase
import com.tokopedia.product.addedit.description.presentation.model.DescriptionInputModel
import com.tokopedia.product.addedit.description.presentation.model.VideoLinkModel
import com.tokopedia.product.addedit.detail.presentation.model.DetailInputModel
import com.tokopedia.product.addedit.preview.presentation.model.ProductInputModel
import com.tokopedia.product.addedit.util.setPrivateProperty
import com.tokopedia.product.addedit.variant.presentation.model.*
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
import org.junit.*
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

    @RelaxedMockK
    lateinit var validateProductDescriptionUseCase: ValidateProductDescriptionUseCase

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
                    usedYoutubeVideoUrl == youtubeVideoUrlFromWebsiteWithoutWww ||
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
                youtubeVideoUrlFromWebsiteWithoutWww -> youtubeWebsiteHostWithoutWww
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

    private val viewModel: AddEditProductDescriptionViewModel by lazy {
        AddEditProductDescriptionViewModel(CoroutineTestDispatchersProvider, resourceProvider, getYoutubeVideoUseCase, validateProductDescriptionUseCase)
    }

    private val youtubeAppHost = "youtu.be"
    private val youtubeWebsiteHost = "www.youtube.com"
    private val youtubeWebsiteHostWithoutWww = "youtube.com"
    private val unknownYoutubeHost = "google.com"
    private val videoId = "8UzbKepncNk"
    private val youtubeVideoUrlFromApp = "https://$youtubeAppHost/$videoId"
    private val youtubeVideoUrlFromWebsite = "https://$youtubeWebsiteHost/watch?v=$videoId"
    private val youtubeVideoUrlFromWebsiteWithoutWww = "https://$youtubeWebsiteHostWithoutWww/watch?v=$videoId"
    private val unknownYoutubeUrl = "http://$unknownYoutubeHost/$videoId"
    private var usedYoutubeVideoUrl = ""

    private val youtubeSuccessData = YoutubeVideoDetailModel()
    private val youtubeRestResponse = RestResponse(youtubeSuccessData, 200, false)
    private val youtubeSuccessRestResponseMap = mapOf<Type, RestResponse>(
            YoutubeVideoDetailModel::class.java to youtubeRestResponse
    )

    private fun getTestProductInputModel(): ProductInputModel {
        return ProductInputModel(
                detailInputModel = DetailInputModel(categoryId = "56"),
                descriptionInputModel = DescriptionInputModel("ini deskripsi"),
                variantInputModel= VariantInputModel(
                        products= listOf(
                                ProductVariantInputModel(combination= listOf(0, 0), price=9999.toBigInteger(), status="ACTIVE", stock=1, isPrimary=false),
                                ProductVariantInputModel(combination= listOf(0, 1), price=9999.toBigInteger(), status="ACTIVE", stock=1, isPrimary=false),
                                ProductVariantInputModel(combination= listOf(1, 0), price=9999.toBigInteger(), status="ACTIVE", stock=1, isPrimary=false),
                                ProductVariantInputModel(combination= listOf(1, 1), price=9999.toBigInteger(), status="ACTIVE", stock=1, isPrimary=false)),
                        selections= listOf(
                                SelectionInputModel(variantId="1", variantName="Warna", unitID="0", identifier="colour", options= listOf(
                                        OptionInputModel(unitValueID="9", value="Merah"),
                                        OptionInputModel(unitValueID="6", value="Biru Muda"))),
                                SelectionInputModel(variantId="29", variantName="Ukuran", unitID="27", unitName="Default", identifier="size", options= listOf(
                                        OptionInputModel(unitValueID="449", value="8"),
                                        OptionInputModel(unitValueID="450", value="10")))),
                        sizecharts= PictureVariantInputModel(),
                        isRemoteDataHasVariant=true)
        )
    }

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
    fun `When user insert url from youtube web without www and usecase is success expect youtube video data`() = runBlocking {
        usedYoutubeVideoUrl = youtubeVideoUrlFromWebsiteWithoutWww

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

        // test empty resource message
        every { resourceProvider.getDuplicateProductVideoErrorMessage() }  returns null

        val resultEmpty = viewModel.validateDuplicateVideo(addedVideoUrls, newVideoUrl)
        assert(resultEmpty.isEmpty())
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

    @Test
    fun `When getVariantSelectedMessage Expect return valid message`() {
        every { resourceProvider.getVariantAddedMessage() }  returns "added message"
        every { resourceProvider.getVariantEmptyMessage() }  returns "empty message"

        viewModel.updateProductInputModel(getTestProductInputModel())
        Assert.assertEquals(viewModel.getVariantSelectedMessage(), "added message")

        viewModel.updateProductInputModel(ProductInputModel())
        Assert.assertEquals(viewModel.getVariantSelectedMessage(), "empty message")
    }

    @Test
    fun `When get message is null and getVariantSelectedMessage Expect return empty message`() {
        every { resourceProvider.getVariantAddedMessage() }  returns null
        every { resourceProvider.getVariantEmptyMessage() }  returns null

        viewModel.updateProductInputModel(getTestProductInputModel())
        Assert.assertTrue(viewModel.getVariantSelectedMessage().isEmpty())

        viewModel.updateProductInputModel(ProductInputModel())
        Assert.assertTrue(viewModel.getVariantSelectedMessage().isEmpty())
    }

    @Test
    fun `When getVariantTypeMessage Expect return variant name`() {
        val productInput = getTestProductInputModel()
        viewModel.updateProductInputModel(productInput)
        var isValid = viewModel.getVariantTypeMessage(0) == productInput.variantInputModel.selections[0].variantName
        assert(isValid)

        viewModel.updateProductInputModel(productInput)
        isValid = viewModel.getVariantTypeMessage(999).isEmpty()
        assert(isValid)
    }

    @Test
    fun `When getVariantCountMessage Expect return variant count message`() {
        every { resourceProvider.getVariantCountSuffix() }  returns "suffix"
        val productInput = getTestProductInputModel()

        viewModel.updateProductInputModel(productInput)
        var isValid = viewModel.getVariantCountMessage(0) ==
                productInput.variantInputModel.selections[0].options.size.toString() + " suffix"
        assert(isValid)

        viewModel.updateProductInputModel(productInput)
        isValid = viewModel.getVariantCountMessage(999).isEmpty()
        assert(isValid)
    }

    @Test
    fun `When getVariantCountMessage and message is null Expect return empty message`() {
        every { resourceProvider.getVariantCountSuffix() } returns null
        val productInput = getTestProductInputModel()

        viewModel.updateProductInputModel(productInput)
        val isValid = viewModel.getVariantCountMessage(0) ==
                productInput.variantInputModel.selections[0].options.size.toString() + " "
        assert(isValid)
    }

    @Test
    fun `constant variables should valid when it's assigned`() {
        // test add mode
        viewModel.isAddMode = true
        viewModel.isDraftMode = true
        viewModel.isFirstMoved = true
        Assert.assertFalse(viewModel.getIsAddMode())
        Assert.assertTrue(viewModel.isFirstMoved)
        Assert.assertTrue(viewModel.isAddMode)
        Assert.assertTrue(viewModel.isDraftMode)

        viewModel.isAddMode = true
        viewModel.isDraftMode = false
        viewModel.isFirstMoved = false
        Assert.assertTrue(viewModel.getIsAddMode())
        Assert.assertFalse(viewModel.isFirstMoved)
        Assert.assertTrue(viewModel.isAddMode)
        Assert.assertFalse(viewModel.isDraftMode)

        viewModel.isAddMode = false
        viewModel.isDraftMode = true
        viewModel.isFirstMoved = true
        Assert.assertFalse(viewModel.getIsAddMode())
        Assert.assertTrue(viewModel.isFirstMoved)
        Assert.assertFalse(viewModel.isAddMode)
        Assert.assertTrue(viewModel.isDraftMode)

        // test edit mode
        viewModel.isEditMode = true
        viewModel.isDraftMode = true
        Assert.assertTrue(viewModel.isEditMode)

        viewModel.isEditMode = true
        viewModel.isDraftMode = false
        Assert.assertTrue(viewModel.isEditMode)

        viewModel.isEditMode = false
        viewModel.isDraftMode = true
        Assert.assertFalse(viewModel.isEditMode)

        // test description input & category id
        Assert.assertTrue(viewModel.descriptionInputModel.productDescription.isEmpty())
        Assert.assertTrue(viewModel.categoryId.isEmpty())

        viewModel.updateProductInputModel(getTestProductInputModel())
        Assert.assertTrue(viewModel.descriptionInputModel.productDescription.isNotEmpty())
        Assert.assertTrue(viewModel.categoryId.isNotEmpty())

        // VideoData
        viewModel.isFetchingVideoData = mutableMapOf(0 to false)
        viewModel.urlToFetch = mutableMapOf(0 to "url")
        viewModel.fetchedUrl = mutableMapOf(0 to "url")
        Assert.assertTrue(viewModel.isFetchingVideoData.isNotEmpty())
        Assert.assertTrue(viewModel.urlToFetch.isNotEmpty())
        Assert.assertTrue(viewModel.fetchedUrl.isNotEmpty())
    }

    @Test
    fun `constant variables should empty when productInputModel is null`() {
        viewModel.setPrivateProperty("_productInputModel", MutableLiveData(null))
        Assert.assertTrue(viewModel.categoryId.isEmpty())
        Assert.assertEquals(viewModel.descriptionInputModel, DescriptionInputModel())
        Assert.assertEquals(viewModel.variantInputModel, VariantInputModel())
        Assert.assertFalse(viewModel.hasVariant)
    }
}