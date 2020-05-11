package com.tokopedia.product.addedit.description.presentation.viewmodel

import android.net.Uri
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.common.network.data.model.RestResponse
import com.tokopedia.product.addedit.common.util.ResourceProvider
import com.tokopedia.product.addedit.description.data.remote.model.variantbycat.ProductVariantByCatModel
import com.tokopedia.product.addedit.description.domain.usecase.GetProductVariantUseCase
import com.tokopedia.product.addedit.description.presentation.model.ProductVariantCombinationViewModel
import com.tokopedia.product.addedit.description.presentation.model.ProductVariantOptionParent
import com.tokopedia.product.addedit.description.presentation.model.VideoLinkModel
import com.tokopedia.product.addedit.preview.presentation.model.ProductInputModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.youtube_common.data.model.YoutubeVideoDetailModel
import com.tokopedia.youtube_common.domain.usecase.GetYoutubeVideoDetailUseCase
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
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

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        mockkStatic(Uri::class)

        every {
            Uri.parse(any())
        } returns videoUri

        every {
            videoUri.lastPathSegment
        } returns videoId

        every {
            videoUri.host
        } answers {
            if (usedYoutubeVideoUrl == youtubeVideoUrlFromApp) youtubeAppHost
            else youtubeWebsiteHost
        }

        every {
            videoUri.getQueryParameter(AddEditProductDescriptionViewModel.KEY_YOUTUBE_VIDEO_ID)
        } returns videoId

        viewModel.videoYoutube.observeForever {}
    }

    private val testCoroutineDispatcher = TestCoroutineDispatcher()

    private val viewModel: AddEditProductDescriptionViewModel by lazy {
        AddEditProductDescriptionViewModel(testCoroutineDispatcher, resourceProvider, getProductVariantUseCase, getYoutubeVideoUseCase)
    }

    private val youtubeAppHost = "youtu.be"
    private val youtubeWebsiteHost = "www.youtube.com"
    private val videoId = "8UzbKepncNk"
    private val youtubeVideoUrlFromApp = "https://$youtubeAppHost/$videoId"
    private val youtubeVideoUrlFromWebsite = "https://$youtubeWebsiteHost/watch?v=$videoId"
    private var usedYoutubeVideoUrl = ""

    private val youtubeSuccessData = YoutubeVideoDetailModel()
    private val youtubeRestResponse = RestResponse(youtubeSuccessData, 200, false)
    private val youtubeSuccessRestResponseMap = mapOf<Type, RestResponse>(
            YoutubeVideoDetailModel::class.java to youtubeRestResponse
    )

    @Test
    fun `Should success get product variant`() {
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
    fun `Should failed get product variant`() {
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
    fun `Should success get youtube data using url from youtube app`() {
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
    fun `Should success get youtube data using url from youtube website`() {
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
    fun `Should failed get youtube data`() {
        val throwable = Throwable("")

        coEvery {
            getProductVariantUseCase.executeOnBackground()
        } throws throwable

        viewModel.getVideoYoutube(usedYoutubeVideoUrl, 0)

        Thread.sleep(100)
        val result = viewModel.videoYoutube.value
        assert(result != null && result.second is Fail)
    }

    @Test
    fun `Should produce duplicate video error message`() {
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
    fun `Should not produce duplicate video error message`() {
        val addedVideoUrls = mutableListOf(
                VideoLinkModel("https://youtu.be/d1kf1887aKl")
        )
        val newVideoUrl = "https://youtu.be/$videoId"

        assert(viewModel.validateDuplicateVideo(addedVideoUrls, newVideoUrl).isEmpty())
    }

    @Test
    fun `Should return false on validate video input`() {
        val videoUrls = mutableListOf(
                VideoLinkModel(errorMessage = "Pastikan link Youtube kamu benar")
        )

        assert(!viewModel.validateInputVideo(videoUrls))
    }

    @Test
    fun `Should return true on validate video input`() {
        val videoUrls = mutableListOf(
                VideoLinkModel("https://youtu.be/$videoId")
        )

        assert(viewModel.validateInputVideo(videoUrls))
    }

    @Test
    fun `Should set variant name and count`() {
        val productVariants = arrayListOf(mockk<ProductVariantCombinationViewModel>(relaxed = true))
        val productVariantOptionParent = ProductVariantOptionParent(productVariantOptionChild = arrayListOf(mockk(relaxed = true)))
        val variantOptionParents = arrayListOf(productVariantOptionParent)

        every {
            viewModel["mapVariantOption"](variantOptionParents)
        } returns arrayListOf<ProductVariantOptionParent>()

        every {
            viewModel["mapProductVariant"](productVariants, variantOptionParents)
        } returns arrayListOf<ProductVariantCombinationViewModel>()

        viewModel.setVariantInput(productVariants, variantOptionParents, null)

        verify {
            viewModel["mapVariantOption"](variantOptionParents)
            viewModel["mapProductVariant"](productVariants, variantOptionParents)
            viewModel["setVariantNamesAndCount"](productVariants, variantOptionParents)
        }
    }

    @Test
    fun `Should reset variant name and count`() {
        val productVariant = arrayListOf<ProductVariantCombinationViewModel>()
        val variantOptionParent = arrayListOf<ProductVariantOptionParent>()

        viewModel.setVariantInput(productVariant, variantOptionParent, null)

        assert(viewModel.productInputModel.variantInputModel.variantOptionParent.isEmpty() &&
        viewModel.productInputModel.variantInputModel.productVariant.isEmpty() &&
        viewModel.productInputModel.variantInputModel.productSizeChart == null &&
        viewModel.variantCountList.all { it == 0 } &&
        viewModel.variantNameList.all { it.isEmpty() })
    }

    @Test
    fun `Should return message variant has been added with 2 level`() {
        viewModel.variantNameList[0] = "Variant 1"
        viewModel.variantCountList[0] = 1
        viewModel.variantNameList[1] = "Variant 2"
        viewModel.variantCountList[1] = 2

        every { resourceProvider.getVariantAddedMessage() } returns "Kamu sudah menambahkan varian\n"

        assert(viewModel.getVariantSelectedMessage() == "Kamu sudah menambahkan varian\n1 Variant 1, 2 Variant 2")
    }

    @Test
    fun `Should return message variant has been added with 1 level`() {
        viewModel.variantNameList[0] = "Variant 1"
        viewModel.variantCountList[0] = 1

        every { resourceProvider.getVariantAddedMessage() } returns "Kamu sudah menambahkan varian\n"

        assert(viewModel.getVariantSelectedMessage() == "Kamu sudah menambahkan varian\n1 Variant 1")
    }

    @Test
    fun `Should return message empty selected variant`() {
        viewModel.variantNameList.fill("")

        every { resourceProvider.getVariantEmptyMessage() } returns "Tambah varian warna, ukuran atau tipe lain agar pembeli mudah memilih"

        assert(viewModel.getVariantSelectedMessage() == "Tambah varian warna, ukuran atau tipe lain agar pembeli mudah memilih")
    }

    @Test
    fun `Should return "Ubah Varian" message on variant button`() {
        viewModel.variantNameList.fill("some random string")

        every { resourceProvider.getVariantButtonAddedMessage() } returns "Ubah varian"

        assert(viewModel.getVariantButtonMessage() == "Ubah varian")
    }

    @Test
    fun `Should return "Tambah Varian" message on variant button`() {
        viewModel.variantNameList.fill("")

        every { resourceProvider.getVariantButtonEmptyMessage() } returns "Tambah varian"

        assert(viewModel.getVariantButtonMessage() == "Tambah varian")
    }

    @Test
    fun `Should return status stock view variant type warehouse`() {
        viewModel.productInputModel.detailInputModel.status = 0

        assert(viewModel.getStatusStockViewVariant() == AddEditProductDescriptionViewModel.TYPE_WAREHOUSE)
    }

    @Test
    fun `Should return status stock view variant type active limited`() {
        viewModel.productInputModel.detailInputModel.status = 1
        viewModel.productInputModel.detailInputModel.stock = 1

        assert(viewModel.getStatusStockViewVariant() == AddEditProductDescriptionViewModel.TYPE_ACTIVE_LIMITED)
    }

    @Test
    fun `Should return status stock view variant type active`() {
        viewModel.productInputModel.detailInputModel.status = 1
        viewModel.productInputModel.detailInputModel.stock = 0

        assert(viewModel.getStatusStockViewVariant() == AddEditProductDescriptionViewModel.TYPE_ACTIVE)
    }

    @Test
    fun `Should not disable remove variant in edit mode`() {
        viewModel.isEditMode = true
        assert(!viewModel.checkOriginalVariantLevel())
    }

    @Test
    fun `Should disable remove variant in edit mode`() {
        viewModel.isEditMode = true
        viewModel.variantInputModel.productVariant.add(mockk(relaxed = true))
        viewModel.variantInputModel.variantOptionParent.add(mockk(relaxed = true))
        assert(viewModel.checkOriginalVariantLevel())
    }

    @Test
    fun `Should update variant name and count`() {
        val productVariantOptionParent1 = ProductVariantOptionParent(name = "Warna")
        val productVariantOptionParent2 = ProductVariantOptionParent(name = "Ukuran")
        val productVariant1 = ProductVariantCombinationViewModel(opt = listOf(1, 2))
        val productVariant2 = ProductVariantCombinationViewModel(opt = listOf(3, 4))
        val productVariants = arrayListOf(productVariant1, productVariant2)
        val productVariantOptionParents = arrayListOf(productVariantOptionParent1, productVariantOptionParent2)
        val productInputModel = ProductInputModel().apply {
            variantInputModel.productVariant = productVariants
            variantInputModel.variantOptionParent = productVariantOptionParents
        }

        viewModel.productInputModel = productInputModel

        assert(viewModel.variantCountList[0] == 2 &&
                viewModel.variantCountList[1] == 2 &&
                viewModel.variantNameList[0] == productVariantOptionParent1.name &&
                viewModel.variantNameList[1] == productVariantOptionParent2.name
        )
    }
}