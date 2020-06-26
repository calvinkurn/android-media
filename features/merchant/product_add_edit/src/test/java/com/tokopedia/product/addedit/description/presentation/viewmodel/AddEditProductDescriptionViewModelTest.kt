package com.tokopedia.product.addedit.description.presentation.viewmodel

import android.net.Uri
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.common.network.data.model.RestResponse
import com.tokopedia.product.addedit.common.util.ResourceProvider
import com.tokopedia.product.addedit.description.data.remote.model.variantbycat.ProductVariantByCatModel
import com.tokopedia.product.addedit.description.domain.usecase.GetProductVariantUseCase
import com.tokopedia.product.addedit.description.presentation.model.ProductVariantCombination
import com.tokopedia.product.addedit.description.presentation.model.ProductVariantOptionChild
import com.tokopedia.product.addedit.description.presentation.model.ProductVariantOptionParent
import com.tokopedia.product.addedit.description.presentation.model.VideoLinkModel
import com.tokopedia.product.addedit.preview.presentation.model.ProductInputModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.youtube_common.data.model.YoutubeVideoDetailModel
import com.tokopedia.youtube_common.domain.usecase.GetYoutubeVideoDetailUseCase
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
    lateinit var getProductVariantUseCase: GetProductVariantUseCase

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
            videoUri.getQueryParameter(AddEditProductDescriptionViewModel.KEY_YOUTUBE_VIDEO_ID)
        } returns videoId

        viewModel.videoYoutube.observeForever(videoYoutubeObserver)
    }

    @After
    fun cleanup() {
        viewModel.videoYoutube.removeObserver(videoYoutubeObserver)
    }

    private val testCoroutineDispatcher = TestCoroutineDispatcher()

    private val viewModel: AddEditProductDescriptionViewModel by lazy {
        AddEditProductDescriptionViewModel(testCoroutineDispatcher, resourceProvider, getProductVariantUseCase, getYoutubeVideoUseCase)
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

    private val productVariantOptionParent1 = ProductVariantOptionParent(name = "Warna", productVariantOptionChild = listOf(ProductVariantOptionChild(value = "Kuning"), ProductVariantOptionChild(value = "Ungu")))
    private val productVariantOptionParent2 = ProductVariantOptionParent(name = "Ukuran", productVariantOptionChild = listOf(ProductVariantOptionChild(value = "XL"), ProductVariantOptionChild(value = "M")))
    private val productVariant1 = ProductVariantCombination(opt = listOf(0, 0), level1String = "Kuning", level2String = "XL")
    private val productVariant2 = ProductVariantCombination(opt = listOf(1, 1), level1String = "Ungu", level2String = "M")
    private val productVariants = arrayListOf(productVariant1, productVariant2)
    private val productVariantOptionParents = arrayListOf(productVariantOptionParent1, productVariantOptionParent2)

    @Test
    fun `When get product variant usecase is success expect return product variant`() {
        val successResult = listOf(ProductVariantByCatModel(), ProductVariantByCatModel(), ProductVariantByCatModel())

        coEvery {
            getProductVariantUseCase.executeOnBackground()
        } returns successResult

        viewModel.getVariants("0")

        coVerify {
            getProductVariantUseCase.executeOnBackground()
        }

        val result = viewModel.productVariant.value
        assert(result != null && result == Success(successResult))
    }

    @Test
    fun `When get product variant usecase is throwing an error expect null on product variant data`() {
        val throwable = Throwable("")

        coEvery {
            getProductVariantUseCase.executeOnBackground()
        } throws throwable

        viewModel.getVariants(viewModel.categoryId)

        coVerify {
            getProductVariantUseCase.executeOnBackground()
        }

        val result = viewModel.productVariantData
        assert(result == null)
    }

    @Test
    fun `When user insert url from youtube app and usecase is success expect youtube video data`() {
        usedYoutubeVideoUrl = youtubeVideoUrlFromApp

        coEvery {
            getYoutubeVideoUseCase.executeOnBackground()
        } returns youtubeSuccessRestResponseMap

        viewModel.getVideoYoutube(usedYoutubeVideoUrl, 0)

        coVerify {
            getYoutubeVideoUseCase.executeOnBackground()
        }

        val result = viewModel.videoYoutube.value
        assert(result != null && result == Pair(0, Success(youtubeSuccessData)))
    }

    @Test
    fun `When user insert url from youtube web and usecase is success expect youtube video data`() {
        usedYoutubeVideoUrl = youtubeVideoUrlFromWebsite

        coEvery {
            getYoutubeVideoUseCase.executeOnBackground()
        } returns youtubeSuccessRestResponseMap

        viewModel.getVideoYoutube(usedYoutubeVideoUrl, 0)

        coVerify {
            getYoutubeVideoUseCase.executeOnBackground()
        }

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

    @Test
    fun `When product variant is not empty expect set variant name and count`() {
        viewModel.setVariantInput(productVariants, productVariantOptionParents, null)

        assert(viewModel.variantCountList[0] == 2 &&
                viewModel.variantCountList[1] == 2 &&
                viewModel.variantNameList[0] == productVariantOptionParent1.name &&
                viewModel.variantNameList[1] == productVariantOptionParent2.name
        )
    }

    @Test
    fun `When product variant is empty expect resetting variant name and count`() {
        val productVariant = arrayListOf<ProductVariantCombination>()
        val variantOptionParent = arrayListOf<ProductVariantOptionParent>()

        viewModel.setVariantInput(productVariant, variantOptionParent, null)

        assert(viewModel.productInputModel.variantInputModel.variantOptionParent.isEmpty() &&
        viewModel.productInputModel.variantInputModel.productVariant.isEmpty() &&
        viewModel.productInputModel.variantInputModel.productSizeChart == null &&
        viewModel.variantCountList.all { it == 0 } &&
        viewModel.variantNameList.all { it.isEmpty() })
    }

    @Test
    fun `When user already add 2 variant expect return message variant has been added with 2 level on getVariantSelectedMessage`() {
        viewModel.variantNameList[0] = "Warna"
        viewModel.variantCountList[0] = 1
        viewModel.variantNameList[1] = "Ukuran"
        viewModel.variantCountList[1] = 2

        every { resourceProvider.getVariantAddedMessage() } returns "Kamu sudah menambahkan varian\n"

        assert(viewModel.getVariantSelectedMessage() == "Kamu sudah menambahkan varian\n1 Warna, 2 Ukuran")
    }

    @Test
    fun `When user already add 1 variant expect return message variant has been added with 1 level on getVariantSelectedMessage`() {
        viewModel.variantNameList[0] = "Warna"
        viewModel.variantCountList[0] = 1

        every { resourceProvider.getVariantAddedMessage() } returns "Kamu sudah menambahkan varian\n"

        assert(viewModel.getVariantSelectedMessage() == "Kamu sudah menambahkan varian\n1 Warna")
    }

    @Test
    fun `When user is not yet add any variant expect return message empty selected variant on getVariantSelectedMessage`() {
        viewModel.variantNameList.fill("")

        every { resourceProvider.getVariantEmptyMessage() } returns "Tambah varian warna, ukuran atau tipe lain agar pembeli mudah memilih"

        assert(viewModel.getVariantSelectedMessage() == "Tambah varian warna, ukuran atau tipe lain agar pembeli mudah memilih")
    }

    @Test
    fun `When user already add variant expect return "Ubah Varian" message on getVariantButtonMessage`() {
        viewModel.variantNameList.fill("some random string")

        every { resourceProvider.getVariantButtonAddedMessage() } returns "Ubah varian"

        assert(viewModel.getVariantButtonMessage() == "Ubah varian")
    }

    @Test
    fun `When user is not yet add variant expect return "Tambah Varian" message on getVariantButtonMessage`() {
        viewModel.variantNameList.fill("")

        every { resourceProvider.getVariantButtonEmptyMessage() } returns "Tambah varian"

        assert(viewModel.getVariantButtonMessage() == "Tambah varian")
    }

    @Test
    fun `When status is non-aktif expect return status stock view variant type warehouse on getStatusStockViewVariant`() {
        viewModel.productInputModel.detailInputModel.status = 0

        assert(viewModel.getStatusStockViewVariant() == AddEditProductDescriptionViewModel.TYPE_WAREHOUSE)
    }

    @Test
    fun `When status is aktif and stock is not empty expect return status stock view variant type active limited on getStatusStockViewVariant`() {
        viewModel.productInputModel.detailInputModel.status = 1
        viewModel.productInputModel.detailInputModel.stock = 1

        assert(viewModel.getStatusStockViewVariant() == AddEditProductDescriptionViewModel.TYPE_ACTIVE_LIMITED)
    }

    @Test
    fun `When status is aktif and stock is empty expect return status stock view variant type active on getStatusStockViewVariant`() {
        viewModel.productInputModel.detailInputModel.status = 1
        viewModel.productInputModel.detailInputModel.stock = 0

        assert(viewModel.getStatusStockViewVariant() == AddEditProductDescriptionViewModel.TYPE_ACTIVE)
    }

    @Test
    fun `When in edit mode and user is not yet add any variant expect return false on checkOriginalVariantLevel`() {
        viewModel.isEditMode = true
        assert(!viewModel.checkOriginalVariantLevel())
    }

    @Test
    fun `When in edit mode and user already add some variant expect return true on checkOriginalVariantLevel`() {
        viewModel.isEditMode = true
        viewModel.variantInputModel.productVariant.add(mockk(relaxed = true))
        viewModel.variantInputModel.variantOptionParent.add(mockk(relaxed = true))
        assert(viewModel.checkOriginalVariantLevel())
    }

    @Test
    fun `when product input model is updated expect update variant name and count`() {
        val productInputModel = ProductInputModel().apply {
            variantInputModel.productVariant = productVariants
            variantInputModel.variantOptionParent = productVariantOptionParents
        }

        // update product input model to trigger setVariantNamesAndCount
        viewModel.productInputModel = productInputModel

        assert(viewModel.variantCountList[0] == 2 &&
                viewModel.variantCountList[1] == 2 &&
                viewModel.variantNameList[0] == productVariantOptionParent1.name &&
                viewModel.variantNameList[1] == productVariantOptionParent2.name
        )
    }
}