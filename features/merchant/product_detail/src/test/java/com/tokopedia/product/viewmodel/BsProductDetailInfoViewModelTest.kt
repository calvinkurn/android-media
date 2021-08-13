package com.tokopedia.product.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.product.detail.common.data.model.product.YoutubeVideo
import com.tokopedia.product.detail.data.model.datamodel.ProductDetailInfoContent
import com.tokopedia.product.detail.data.model.productinfo.ProductInfoParcelData
import com.tokopedia.product.info.model.productdetail.response.BottomSheetItem
import com.tokopedia.product.info.model.productdetail.response.DataShopNotes
import com.tokopedia.product.info.model.productdetail.response.PdpGetDetailBottomSheet
import com.tokopedia.product.info.model.productdetail.response.ShopNotesData
import com.tokopedia.product.info.model.productdetail.uidata.*
import com.tokopedia.product.info.usecase.GetProductDetailBottomSheetUseCase
import com.tokopedia.product.info.view.BsProductDetailInfoViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.user.session.UserSessionInterface
import org.junit.*

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
        BsProductDetailInfoViewModel(CoroutineTestDispatchersProvider, getProductDetailBottomSheetUseCase, userSessionInterface)
    }

    private val bottomSheetOrderItem by lazy {
        listOf(
                BottomSheetItem(componentName = HEADER_DETAIL_KEY),
                BottomSheetItem(componentName = DESCRIPTION_DETAIL_KEY),
                BottomSheetItem(componentName = GUIDELINE_DETAIL_KEY),
                BottomSheetItem(componentName = SHOP_NOTES_DETAIL_KEY))
    }

    private val bottomSheetHeaderItem by lazy {
        listOf(
                ProductDetailInfoContent("", "123", "123", "123"),
                ProductDetailInfoContent("", "123", "123", "123"),
                ProductDetailInfoContent("", "123", "123", "123"))
    }

    private val bottomSheetHeaderWithDiscussion by lazy {
        listOf(
                ProductDetailInfoContent("", "123", "123", "123"),
                ProductDetailInfoContent("", "123", "123", "123"),
                ProductDetailInfoContent("", DESCRIPTION_DETAIL_KEY, "123", "123"))
    }

    private val listOfVideo by lazy {
        listOf(
                YoutubeVideo("asd", "asd"),
                YoutubeVideo("asd", "asd"),
                YoutubeVideo("asd", "asd")
        )
    }

    companion object {
        const val HEADER_DETAIL_KEY = "detail"
        const val DESCRIPTION_DETAIL_KEY = "deskripsi"
        const val GUIDELINE_DETAIL_KEY = "panduan_ukuran"
        const val SHOP_NOTES_DETAIL_KEY = "informasi_penting"
    }

    @Test
    fun `success get data from network with no expandable item`() {
        viewModel.bottomSheetDetailData.observeForever { }

        coEvery {
            getProductDetailBottomSheetUseCase.executeOnBackground(any(), any())
        } returns PdpGetDetailBottomSheet(bottomsheetData = bottomSheetOrderItem)

        viewModel.setParams(ProductInfoParcelData(productId = "123", shopId = "213", productTitle = "123", productImageUrl = "123", data = bottomSheetHeaderItem))

        Assert.assertTrue(viewModel.bottomSheetDetailData.value is Success)

        //region header
        Assert.assertTrue((viewModel.bottomSheetDetailData.value as Success).data.filterIsInstance<ProductDetailInfoHeaderDataModel>().isNotEmpty())
        Assert.assertTrue((viewModel.bottomSheetDetailData.value as Success).data.filterIsInstance<ProductDetailInfoHeaderDataModel>().first().img.isNotEmpty())
        Assert.assertTrue((viewModel.bottomSheetDetailData.value as Success).data.filterIsInstance<ProductDetailInfoHeaderDataModel>().first().listOfInfo.isNotEmpty())
        //endregion

        //region discussion
        Assert.assertTrue((viewModel.bottomSheetDetailData.value as Success).data.filterIsInstance<ProductDetailInfoDiscussionDataModel>().isNotEmpty())
        //endregion

        // region expandable
        Assert.assertTrue((viewModel.bottomSheetDetailData.value as Success).data.filterIsInstance<ProductDetailInfoExpandableListDataModel>().isEmpty())
        Assert.assertTrue((viewModel.bottomSheetDetailData.value as Success).data.filterIsInstance<ProductDetailInfoExpandableDataModel>().isEmpty())
        Assert.assertTrue((viewModel.bottomSheetDetailData.value as Success).data.filterIsInstance<ProductDetailInfoExpandableImageDataModel>().isEmpty())
        //endregion
    }

    @Test
    fun `success get data from network with expandable item`() {
        val shopNotes = DataShopNotes("", listOf(ShopNotesData("asd", "asd", "asd", false, 1, "")))

        viewModel.bottomSheetDetailData.observeForever { }

        coEvery {
            getProductDetailBottomSheetUseCase.executeOnBackground(any(), any())
        } returns PdpGetDetailBottomSheet(
                bottomsheetData = bottomSheetOrderItem,
                dataShopNotes = shopNotes
        )

        viewModel.setParams(ProductInfoParcelData(productId = "123", shopId = "213", productTitle = "123", productImageUrl = "123", variantGuideline = "123123", listOfYoutubeVideo = listOfVideo, data = bottomSheetHeaderWithDiscussion))

        Assert.assertTrue(viewModel.bottomSheetDetailData.value is Success)

        //region header
        Assert.assertTrue((viewModel.bottomSheetDetailData.value as Success).data.filterIsInstance<ProductDetailInfoHeaderDataModel>().isNotEmpty())
        Assert.assertTrue((viewModel.bottomSheetDetailData.value as Success).data.filterIsInstance<ProductDetailInfoHeaderDataModel>().first().img.isNotEmpty())
        Assert.assertTrue((viewModel.bottomSheetDetailData.value as Success).data.filterIsInstance<ProductDetailInfoHeaderDataModel>().first().listOfInfo.isNotEmpty())
        //endregion

        //region discussion
        Assert.assertTrue((viewModel.bottomSheetDetailData.value as Success).data.filterIsInstance<ProductDetailInfoDiscussionDataModel>().isNotEmpty())
        //endregin

        //region shopnotes
        Assert.assertTrue((viewModel.bottomSheetDetailData.value as Success).data.filterIsInstance<ProductDetailInfoExpandableListDataModel>().isNotEmpty())
        //endregion

        //region discussion
        Assert.assertTrue((viewModel.bottomSheetDetailData.value as Success).data.filterIsInstance<ProductDetailInfoExpandableDataModel>().isNotEmpty())
        Assert.assertTrue((viewModel.bottomSheetDetailData.value as Success).data.filterIsInstance<ProductDetailInfoExpandableDataModel>().first().textValue.isNotEmpty())
        Assert.assertTrue((viewModel.bottomSheetDetailData.value as Success).data.filterIsInstance<ProductDetailInfoExpandableDataModel>().first().youtubeVideo.isNotEmpty())
        //endregion

        //region image
        Assert.assertTrue((viewModel.bottomSheetDetailData.value as Success).data.filterIsInstance<ProductDetailInfoExpandableImageDataModel>().isNotEmpty())
        //endregion
    }

    @Test
    fun `error get data from network`() {
        viewModel.bottomSheetDetailData.observeForever { }

        coEvery {
            getProductDetailBottomSheetUseCase.executeOnBackground(any(), any())
        } throws Throwable()

        viewModel.setParams(ProductInfoParcelData())

        Assert.assertTrue(viewModel.bottomSheetDetailData.value is Fail)
    }
}