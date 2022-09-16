package com.tokopedia.product.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.product.detail.common.data.model.product.YoutubeVideo
import com.tokopedia.product.detail.data.model.datamodel.product_detail_info.ProductDetailInfoContent
import com.tokopedia.product.detail.data.model.datamodel.product_detail_info.ProductDetailInfoDataModel
import com.tokopedia.product.detail.data.model.datamodel.product_detail_info.ProductDetailInfoSeeMore
import com.tokopedia.product.detail.data.model.productinfo.ProductInfoParcelData
import com.tokopedia.product.info.data.response.BottomSheetItem
import com.tokopedia.product.info.data.response.BottomSheetProductDetailInfoResponse
import com.tokopedia.product.info.data.response.DataShopNotes
import com.tokopedia.product.info.data.response.PdpGetDetailBottomSheet
import com.tokopedia.product.info.data.response.ShopNotesData
import com.tokopedia.product.info.view.models.AnnotationValueDataModel
import com.tokopedia.product.info.view.models.ProductDetailInfoCardDataModel
import com.tokopedia.product.info.view.models.ProductDetailInfoCatalogDataModel
import com.tokopedia.product.info.view.models.ProductDetailInfoDiscussionDataModel
import com.tokopedia.product.info.view.models.ProductDetailInfoExpandableDataModel
import com.tokopedia.product.info.view.models.ProductDetailInfoExpandableImageDataModel
import com.tokopedia.product.info.view.models.ProductDetailInfoExpandableListDataModel
import com.tokopedia.product.info.view.models.ProductDetailInfoHeaderDataModel
import com.tokopedia.product.info.usecase.GetProductDetailBottomSheetUseCase
import com.tokopedia.product.info.view.BsProductDetailInfoViewModel
import com.tokopedia.product.util.ProductDetailTestUtil
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.unit.test.ext.getOrAwaitValue
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created by Yehezkiel on 02/11/20
 */
@ExperimentalCoroutinesApi
class BsProductDetailInfoViewModelTest {

    @RelaxedMockK
    lateinit var getProductDetailBottomSheetUseCase: GetProductDetailBottomSheetUseCase

    @RelaxedMockK
    lateinit var userSessionInterface: UserSessionInterface

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    @After
    fun setupAfter() {
        viewModel.bottomSheetDetailData.removeObserver { }
    }

    private val viewModel by lazy {
        BsProductDetailInfoViewModel(
            CoroutineTestDispatchersProvider,
            getProductDetailBottomSheetUseCase,
            userSessionInterface
        )
    }

    private val bottomSheetOrderItem by lazy {
        listOf(
            BottomSheetItem(componentName = HEADER_DETAIL_KEY),
            BottomSheetItem(componentName = DESCRIPTION_DETAIL_KEY),
            BottomSheetItem(componentName = GUIDELINE_DETAIL_KEY),
            BottomSheetItem(componentName = SHOP_NOTES_DETAIL_KEY),
            BottomSheetItem(componentName = CUSTOM_INFO_KEY)
        )
    }

    private val bottomSheetHeaderItem by lazy {
        listOf(
            ProductDetailInfoContent("", "123", "123", "123"),
            ProductDetailInfoContent("", "123", "123", "123"),
            ProductDetailInfoContent("", "123", "123", "123")
        )
    }

    private val bottomSheetHeaderWithDiscussion by lazy {
        listOf(
            ProductDetailInfoContent("", "123", "123", "123"),
            ProductDetailInfoContent("", "123", "123", "123"),
            ProductDetailInfoContent("", DESCRIPTION_DETAIL_KEY, "123", "123")
        )
    }

    private val listOfVideo by lazy {
        listOf(
            YoutubeVideo("asd", "asd"),
            YoutubeVideo("asd", "asd"),
            YoutubeVideo("asd", "asd")
        )
    }

    private val parcelDataSpecification = ProductInfoParcelData(
        productId = "123",
        shopId = "213",
        productTitle = "123",
        productImageUrl = "123",
        productInfo = ProductDetailInfoDataModel(
            catalogBottomSheet = ProductDetailInfoSeeMore(bottomSheetTitle = SPECIFICATION_BOTTOM_SHEET_TITLE),
            bottomSheet = ProductDetailInfoSeeMore()
        ),
        isOpenSpecification = true
    )

    private val parcelDataCatalogDescription = parcelDataSpecification.copy(
        isOpenSpecification = false,
        productInfo = parcelDataSpecification.productInfo.copy(
            bottomSheet = parcelDataSpecification.productInfo.bottomSheet.copy(
                bottomSheetTitle = DESCRIPTION_BOTTOM_SHEET_TITLE
            )
        )
    )

    private val parcelDataDescription = parcelDataSpecification.copy(
        isOpenSpecification = false,
        productInfo = parcelDataSpecification.productInfo.copy(
            catalogBottomSheet = null,
            bottomSheet = parcelDataSpecification.productInfo.bottomSheet.copy(
                bottomSheetTitle = DESCRIPTION_BOTTOM_SHEET_TITLE
            )
        )
    )

    private val bottomSheetData =
        ProductDetailTestUtil.createMockGraphqlSuccessResponse<BottomSheetProductDetailInfoResponse>(
            jsonLocation = RESOURCE_BOTTOM_SHEET_DATA,
            BottomSheetProductDetailInfoResponse::class.java
        ).response

    @Test
    fun `verify bottom sheet title when open specification`() {
        // given and when
        viewModel.setParams(parcelDataSpecification)
        val title = viewModel.bottomSheetTitle.getOrAwaitValue()

        // then
        Assert.assertEquals(SPECIFICATION_BOTTOM_SHEET_TITLE, title)
    }

    @Test
    fun `verify bottom sheet title when open catalog description`() {
        // given and when
        viewModel.setParams(parcelDataCatalogDescription)
        val title = viewModel.bottomSheetTitle.getOrAwaitValue()

        // then
        Assert.assertEquals(DESCRIPTION_BOTTOM_SHEET_TITLE, title)
    }

    @Test
    fun `verify bottom sheet title when open non catalog description`() {
        // given and when
        viewModel.setParams(parcelDataDescription)

        // then
        val title = viewModel.bottomSheetTitle.getOrAwaitValue()
        Assert.assertEquals(DESCRIPTION_BOTTOM_SHEET_TITLE, title)
    }

    @Test
    fun `show specification component when success get data from network`() {
        // given
        coEvery {
            getProductDetailBottomSheetUseCase.execute(any(), any(), any(), any(), any(), any(), any())
        } returns bottomSheetData

        // when
        viewModel.setParams(parcelDataSpecification)

        // then
        val result = viewModel.bottomSheetDetailData.getOrAwaitValue()
        val data = (result as? Success)?.data.orEmpty()

        Assert.assertTrue(result is Success)
        Assert.assertTrue(data.filterIsInstance<ProductDetailInfoHeaderDataModel>().isNotEmpty())
        Assert.assertTrue(data.filterIsInstance<ProductDetailInfoCatalogDataModel>().isNotEmpty())
        Assert.assertTrue(data.filterIsInstance<ProductDetailInfoDiscussionDataModel>().isNotEmpty())

        Assert.assertFalse(data.filterIsInstance<AnnotationValueDataModel>().isNotEmpty())
        Assert.assertFalse(data.filterIsInstance<ProductDetailInfoCardDataModel>().isNotEmpty())
        Assert.assertFalse(data.filterIsInstance<ProductDetailInfoExpandableDataModel>().isNotEmpty())
        Assert.assertFalse(data.filterIsInstance<ProductDetailInfoExpandableListDataModel>().isNotEmpty())
        Assert.assertFalse(data.filterIsInstance<ProductDetailInfoExpandableImageDataModel>().isNotEmpty())
    }

    @Test
    fun `show catalog description component when success get data from network`() {
        // given
        coEvery {
            getProductDetailBottomSheetUseCase.execute(any(), any(), any(), any(), any(), any(), any())
        } returns bottomSheetData

        // when
        viewModel.setParams(parcelDataCatalogDescription)

        val result = viewModel.bottomSheetDetailData.getOrAwaitValue()
        val data = (result as? Success)?.data.orEmpty()

        // then
        Assert.assertTrue(result is Success)
        Assert.assertTrue(data.filterIsInstance<ProductDetailInfoHeaderDataModel>().isNotEmpty())
        Assert.assertTrue(data.filterIsInstance<ProductDetailInfoExpandableListDataModel>().isNotEmpty())

        Assert.assertFalse(data.filterIsInstance<ProductDetailInfoCatalogDataModel>().isNotEmpty())
    }

    @Test
    fun `show non catalog description component when success get data from network`() {
        // given
        coEvery {
            getProductDetailBottomSheetUseCase.execute(any(), any(), any(), any(), any(), any(), any())
        } returns bottomSheetData

        // when
        viewModel.setParams(parcelDataDescription)

        // then
        val result = viewModel.bottomSheetDetailData.getOrAwaitValue()
        val data = (result as? Success)?.data.orEmpty()

        Assert.assertTrue(result is Success)
        Assert.assertTrue(data.filterIsInstance<ProductDetailInfoHeaderDataModel>().isNotEmpty())
        Assert.assertTrue(data.filterIsInstance<ProductDetailInfoExpandableListDataModel>().isNotEmpty())

        Assert.assertFalse(data.filterIsInstance<ProductDetailInfoCatalogDataModel>().isNotEmpty())
    }

    @Test
    fun `success get data from network with no expandable item`() {
        viewModel.bottomSheetDetailData.observeForever { }

        coEvery {
            getProductDetailBottomSheetUseCase.execute(any(), any(), any(), any(), any(), any(), any())
        } returns PdpGetDetailBottomSheet(bottomsheetData = bottomSheetOrderItem)

        viewModel.setParams(
            ProductInfoParcelData(
                productId = "123",
                shopId = "213",
                productTitle = "123",
                productImageUrl = "123",
                productInfo = ProductDetailInfoDataModel(
                    dataContent = bottomSheetHeaderItem,
                    catalogBottomSheet = null,
                    bottomSheet = ProductDetailInfoSeeMore()
                )
            )
        )

        Assert.assertTrue(viewModel.bottomSheetDetailData.value is Success)

        //region header
        Assert.assertTrue(
            (viewModel.bottomSheetDetailData.value as Success).data.filterIsInstance<ProductDetailInfoHeaderDataModel>()
                .isNotEmpty()
        )
        Assert.assertTrue(
            (viewModel.bottomSheetDetailData.value as Success).data.filterIsInstance<ProductDetailInfoHeaderDataModel>()
                .first().img.isNotEmpty()
        )
        Assert.assertTrue(
            (viewModel.bottomSheetDetailData.value as Success).data.filterIsInstance<ProductDetailInfoHeaderDataModel>()
                .first().listOfInfo.isNotEmpty()
        )
        //endregion

        //region discussion
        Assert.assertTrue(
            (viewModel.bottomSheetDetailData.value as Success).data.filterIsInstance<ProductDetailInfoDiscussionDataModel>()
                .isNotEmpty()
        )
        //endregion

        // region expandable
        Assert.assertTrue(
            (viewModel.bottomSheetDetailData.value as Success).data.filterIsInstance<ProductDetailInfoExpandableListDataModel>()
                .isEmpty()
        )
        Assert.assertTrue(
            (viewModel.bottomSheetDetailData.value as Success).data.filterIsInstance<ProductDetailInfoExpandableDataModel>()
                .isEmpty()
        )
        Assert.assertTrue(
            (viewModel.bottomSheetDetailData.value as Success).data.filterIsInstance<ProductDetailInfoExpandableImageDataModel>()
                .isEmpty()
        )
        //endregion

        //region custom info
        Assert.assertTrue(
            (viewModel.bottomSheetDetailData.value as Success).data.filterIsInstance<ProductDetailInfoCardDataModel>()
                .isNotEmpty()
        )
        //endregion
    }

    @Test
    fun `success get data from network with expandable item`() {
        val shopNotes = DataShopNotes("", listOf(ShopNotesData("asd", "asd", "asd", false, 1, "")))

        viewModel.bottomSheetDetailData.observeForever { }

        coEvery {
            getProductDetailBottomSheetUseCase.execute(any(), any(), any(), any(), any(), any(), any())
        } returns PdpGetDetailBottomSheet(
            bottomsheetData = bottomSheetOrderItem,
            dataShopNotes = shopNotes
        )

        viewModel.setParams(
            ProductInfoParcelData(
                productId = "123",
                shopId = "213",
                productTitle = "123",
                productImageUrl = "123",
                variantGuideline = "123123",
                listOfYoutubeVideo = listOfVideo,
                productInfo = ProductDetailInfoDataModel(dataContent = bottomSheetHeaderWithDiscussion)
            )
        )

        Assert.assertTrue(viewModel.bottomSheetDetailData.value is Success)

        //region header
        Assert.assertTrue(
            (viewModel.bottomSheetDetailData.value as Success).data.filterIsInstance<ProductDetailInfoHeaderDataModel>()
                .isNotEmpty()
        )
        Assert.assertTrue(
            (viewModel.bottomSheetDetailData.value as Success).data.filterIsInstance<ProductDetailInfoHeaderDataModel>()
                .first().img.isNotEmpty()
        )
        Assert.assertTrue(
            (viewModel.bottomSheetDetailData.value as Success).data.filterIsInstance<ProductDetailInfoHeaderDataModel>()
                .first().listOfInfo.isNotEmpty()
        )
        //endregion

        //region discussion
        Assert.assertTrue(
            (viewModel.bottomSheetDetailData.value as Success).data.filterIsInstance<ProductDetailInfoDiscussionDataModel>()
                .isNotEmpty()
        )
        //endregin

        //region shopnotes
        Assert.assertTrue(
            (viewModel.bottomSheetDetailData.value as Success).data.filterIsInstance<ProductDetailInfoExpandableListDataModel>()
                .isNotEmpty()
        )
        //endregion

        //region discussion
        Assert.assertTrue(
            (viewModel.bottomSheetDetailData.value as Success).data.filterIsInstance<ProductDetailInfoExpandableDataModel>()
                .isNotEmpty()
        )
        Assert.assertTrue(
            (viewModel.bottomSheetDetailData.value as Success).data.filterIsInstance<ProductDetailInfoExpandableDataModel>()
                .first().textValue.isNotEmpty()
        )
        Assert.assertTrue(
            (viewModel.bottomSheetDetailData.value as Success).data.filterIsInstance<ProductDetailInfoExpandableDataModel>()
                .first().youtubeVideo.isNotEmpty()
        )
        //endregion

        //region image
        Assert.assertTrue(
            (viewModel.bottomSheetDetailData.value as Success).data.filterIsInstance<ProductDetailInfoExpandableImageDataModel>()
                .isNotEmpty()
        )
        //endregion

        //region custom info
        Assert.assertTrue(
            (viewModel.bottomSheetDetailData.value as Success).data.filterIsInstance<ProductDetailInfoCardDataModel>()
                .isNotEmpty()
        )
        //endregion
    }

    @Test
    fun `error get data from network`() {
        viewModel.bottomSheetDetailData.observeForever { }

        coEvery {
            getProductDetailBottomSheetUseCase.execute(any(), any(), any(), any(), any(), any(), any())
        } throws Throwable()

        viewModel.setParams(ProductInfoParcelData())

        Assert.assertTrue(viewModel.bottomSheetDetailData.value is Fail)
    }

    companion object {
        const val HEADER_DETAIL_KEY = "detail"
        const val DESCRIPTION_DETAIL_KEY = "deskripsi"
        const val GUIDELINE_DETAIL_KEY = "panduan_ukuran"
        const val SHOP_NOTES_DETAIL_KEY = "informasi_penting"
        const val CUSTOM_INFO_KEY = "custom_info"

        const val SPECIFICATION_BOTTOM_SHEET_TITLE = "Spesifikasi produk"
        const val DESCRIPTION_BOTTOM_SHEET_TITLE = "Detail produk"
        const val RESOURCE_BOTTOM_SHEET_DATA = "json/gql_get_product_info_bottom_sheet_data.json"
    }
}