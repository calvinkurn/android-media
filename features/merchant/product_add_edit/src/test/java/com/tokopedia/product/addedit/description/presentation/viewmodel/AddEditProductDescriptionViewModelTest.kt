package com.tokopedia.product.addedit.description.presentation.viewmodel

import android.net.Uri
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.tokopedia.common.network.data.model.RestResponse
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.product.addedit.common.constant.AddEditProductConstants
import com.tokopedia.product.addedit.common.util.ResourceProvider
import com.tokopedia.product.addedit.description.domain.model.ValidateProductDescriptionResponse
import com.tokopedia.product.addedit.description.domain.usecase.ValidateProductDescriptionUseCase
import com.tokopedia.product.addedit.description.presentation.model.DescriptionInputModel
import com.tokopedia.product.addedit.description.presentation.model.VideoLinkModel
import com.tokopedia.product.addedit.detail.presentation.model.DetailInputModel
import com.tokopedia.product.addedit.preview.presentation.model.ProductInputModel
import com.tokopedia.product.addedit.util.getOrAwaitValue
import com.tokopedia.product.addedit.util.setPrivateProperty
import com.tokopedia.product.addedit.variant.presentation.model.*
import com.tokopedia.unit.test.rule.CoroutineTestRule
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.youtube_common.data.model.YoutubeVideoDetailModel
import com.tokopedia.youtube_common.domain.usecase.GetYoutubeVideoDetailUseCase
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.junit.*
import java.lang.reflect.Type

@FlowPreview
@ExperimentalCoroutinesApi
class AddEditProductDescriptionViewModelTest {
    @RelaxedMockK
    lateinit var resourceProvider: ResourceProvider

    @RelaxedMockK
    lateinit var getYoutubeVideoUseCase: GetYoutubeVideoDetailUseCase

    @RelaxedMockK
    lateinit var videoUri: Uri

    @RelaxedMockK
    lateinit var validateProductDescriptionUseCase: ValidateProductDescriptionUseCase

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
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

    private fun mockUriParsing() {
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
    }

    @Test
    fun `When user insert product description and usecase is success expect validate product description response`() = coroutineTestRule.runBlockingTest {
        mockkObject(ValidateProductDescriptionUseCase)
        var message = ""
        val validateProductDescriptionResponse = ValidateProductDescriptionResponse().apply {
            productValidateV3.data.validationResults = listOf("nice", "info")
            message = productValidateV3.data.validationResults.joinToString("\n")
        }

        coEvery {
            validateProductDescriptionUseCase.executeOnBackground()
        } returns validateProductDescriptionResponse

        viewModel.validateDescriptionChanged("test")

        val result = viewModel.descriptionValidationMessage.getOrAwaitValue()
        assert(result == message)

        coVerify {
            validateProductDescriptionUseCase.executeOnBackground()
        }
    }

    @Test
    fun `When user insert url from youtube app and usecase is success expect youtube video data`() = coroutineTestRule.runBlockingTest {
        mockUriParsing()
        mockkObject(GetYoutubeVideoDetailUseCase)

        usedYoutubeVideoUrl = youtubeVideoUrlFromApp

        coEvery {
            getYoutubeVideoUseCase.executeOnBackground()
        } returns youtubeSuccessRestResponseMap

        viewModel.urlYoutubeChanged(usedYoutubeVideoUrl)

        val result = viewModel.videoYoutube.getOrAwaitValue()
        assert(result == Success(youtubeSuccessData))
    }

    @Test
    fun `When user insert url from youtube web and usecase is success expect youtube video data`() = coroutineTestRule.runBlockingTest {
        mockUriParsing()
        mockkObject(GetYoutubeVideoDetailUseCase)

        usedYoutubeVideoUrl = youtubeVideoUrlFromWebsite

        coEvery {
            getYoutubeVideoUseCase.executeOnBackground()
        } returns youtubeSuccessRestResponseMap

        viewModel.urlYoutubeChanged(usedYoutubeVideoUrl)

        val result = viewModel.videoYoutube.getOrAwaitValue()
        assert(result == Success(youtubeSuccessData))
    }

    @Test
    fun `When user insert url from youtube web without www and usecase is success expect youtube video data`() = coroutineTestRule.runBlockingTest {
        mockUriParsing()
        mockkObject(GetYoutubeVideoDetailUseCase)

        usedYoutubeVideoUrl = youtubeVideoUrlFromWebsiteWithoutWww

        coEvery {
            getYoutubeVideoUseCase.executeOnBackground()
        } returns youtubeSuccessRestResponseMap

        viewModel.urlYoutubeChanged(usedYoutubeVideoUrl)

        val result = viewModel.videoYoutube.getOrAwaitValue()
        assert(result == Success(youtubeSuccessData))
    }

    @Test
    fun `When user insert url with unknown host expect failed get youtube video data`() = coroutineTestRule.runBlockingTest {
        mockUriParsing()
        usedYoutubeVideoUrl = unknownYoutubeUrl

        viewModel.urlYoutubeChanged(usedYoutubeVideoUrl)

        val result = viewModel.videoYoutube.getOrAwaitValue()
        assert(result is Fail)
    }

    @Test
    fun `When the url host is null expect failed get youtube video data`()= coroutineTestRule.runBlockingTest {
        mockUriParsing()
        every {
            videoUri.host
        } returns null

        usedYoutubeVideoUrl = unknownYoutubeUrl

        viewModel.urlYoutubeChanged(usedYoutubeVideoUrl)

        val result = viewModel.videoYoutube.getOrAwaitValue()
        assert(result is Fail)
    }

    @Test
    fun `When the url parsing is failed expect failed get youtube video data`() = coroutineTestRule.runBlockingTest {
        mockkStatic(Uri::class)
        every {
            Uri.parse(any())
        } throws Throwable("")

        usedYoutubeVideoUrl = unknownYoutubeUrl

        viewModel.urlYoutubeChanged(usedYoutubeVideoUrl)

        val result = viewModel.videoYoutube.getOrAwaitValue()
        assert(result is Fail)
    }

    @Test
    fun `When the response is map containing no values expect failed get youtube video data`() = coroutineTestRule.runBlockingTest {
        mockUriParsing()
        mockkObject(GetYoutubeVideoDetailUseCase)

        usedYoutubeVideoUrl = youtubeVideoUrlFromWebsiteWithoutWww

        coEvery {
            getYoutubeVideoUseCase.executeOnBackground()
        } returns mapOf()

        viewModel.urlYoutubeChanged(usedYoutubeVideoUrl)

        val result = viewModel.videoYoutube.getOrAwaitValue()
        assert(result is Fail)
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
    fun `When getVariantTypeMessage Expect return empty string`() {
        viewModel.setPrivateProperty("_productInputModel", MutableLiveData(null))
        var isValid = viewModel.getVariantTypeMessage(0).isEmpty()
        assert(isValid)

        isValid = viewModel.getVariantCountMessage(999).isEmpty()
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
    fun `When update productInputModel Expect return expected isHampersProduct`() {
        viewModel.updateProductInputModel(productInputModel = ProductInputModel(detailInputModel = DetailInputModel(categoryId = "12")))

        viewModel.isHampersProduct.getOrAwaitValue()
        Assert.assertEquals(false, viewModel.isHampersProduct.value)

        viewModel.updateProductInputModel(productInputModel = ProductInputModel(detailInputModel = DetailInputModel(categoryId = "2916")))

        viewModel.isHampersProduct.getOrAwaitValue()
        Assert.assertEquals(true, viewModel.isHampersProduct.value)
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
        Assert.assertTrue(viewModel.descriptionInputModel?.productDescription?.isEmpty() ?: false)

        viewModel.updateProductInputModel(getTestProductInputModel())
        Assert.assertTrue(viewModel.descriptionInputModel?.productDescription?.isNotEmpty() ?: false)
    }

    @Test
    fun `constant variables should empty when productInputModel is null`() {
        viewModel.setPrivateProperty("_productInputModel", MutableLiveData(null))
        Assert.assertEquals(viewModel.descriptionInputModel, null)
        Assert.assertEquals(viewModel.variantInputModel, null)
        Assert.assertFalse(viewModel.hasVariant)
    }
}