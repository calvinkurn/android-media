package com.tokopedia.mvc.presentation.download


import com.tokopedia.mvc.domain.entity.Voucher
import com.tokopedia.mvc.domain.entity.enums.ImageRatio
import com.tokopedia.mvc.presentation.download.uimodel.VoucherImageEffect
import com.tokopedia.mvc.presentation.download.uimodel.VoucherImageEvent
import com.tokopedia.mvc.presentation.download.uimodel.VoucherImageUiModel
import com.tokopedia.mvc.presentation.download.uimodel.VoucherImageUiState
import com.tokopedia.mvc.presentation.product.list.uimodel.ProductListEffect
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import io.mockk.MockKAnnotations
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class DownloadVoucherImageViewModelTest {

    private lateinit var viewModel: DownloadVoucherImageViewModel

    private val voucherId : Long = 100

    companion object {
        private const val SQUARE_IMAGE_URL = "https://images.tokopedia.net/img/android/seller-mvc/intro_bg_voucher_type/intro_bg_voucher_type.png"
        private const val HORIZONTAL_IMAGE_URL = "https://images.tokopedia.net/img/android/seller-mvc/intro_bg_voucher_type/intro_bg_voucher_type-horizontal.png"
    }

    private val squareImage =  VoucherImageUiModel(
        ratio = "square",
        description = "",
        isSelected = true,
        isExpanded = false,
        imageRatio = ImageRatio.SQUARE,
        imageUrl = SQUARE_IMAGE_URL
    )

    private val horizontalImage = VoucherImageUiModel(
        ratio = "horizontal",
        description = "",
        isSelected = false,
        isExpanded = false,
        imageRatio = ImageRatio.HORIZONTAL,
        imageUrl = HORIZONTAL_IMAGE_URL
    )

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = DownloadVoucherImageViewModel(CoroutineTestDispatchersProvider)
    }


    //region handleTapDownloadButton
    @Test
    fun `When tap download button, should emit DownloadImages effect with selected voucher images`() {
        runBlockingTest {
            //Given
            val emittedEffects = arrayListOf<VoucherImageEffect>()
            val job = launch {
                viewModel.uiEffect.toList(emittedEffects)
            }

            val voucherImages = listOf(squareImage, horizontalImage)

            viewModel.processEvent(VoucherImageEvent.PopulateInitialData(voucherId, voucherImages))
            viewModel.processEvent(VoucherImageEvent.AddImageToSelection(squareImage.imageUrl))

            //When
            viewModel.processEvent(VoucherImageEvent.TapDownloadButton)

            //Then
            val emittedEffect = emittedEffects.last()

            assertEquals(
                VoucherImageEffect.DownloadImages(
                    voucherId = voucherId,
                    selectedImageUrls = listOf(squareImage.copy(isSelected = true))
                ),
                emittedEffect
            )


            job.cancel()
        }
    }


    @Test
    fun `When tap download button, should filter the selected image urls only`() {
        runBlockingTest {
            //Given
            val emittedValues = arrayListOf<VoucherImageUiState>()
            val job = launch {
                viewModel.uiState.toList(emittedValues)
            }

            val voucherImages = listOf(squareImage, horizontalImage)

            viewModel.processEvent(VoucherImageEvent.PopulateInitialData(voucherId, voucherImages))
            viewModel.processEvent(VoucherImageEvent.AddImageToSelection(squareImage.imageUrl))

            //When
            viewModel.processEvent(VoucherImageEvent.TapDownloadButton)

            //Then
            val actual = emittedValues.last()

            assertEquals(
                listOf(squareImage.copy(isSelected = true), horizontalImage.copy(isSelected = false)),
                actual.voucherImages
            )
            assertEquals(
                listOf(squareImage.copy(isSelected = true)),
                actual.selectedImageUrls
            )

            job.cancel()
        }
    }
    //endregion

    //region RemoveImageFromSelection
    @Test
    fun `When remove image from selection, should uncheck the unselected image`() {
        runBlockingTest {
            //Given
            val emittedValues = arrayListOf<VoucherImageUiState>()
            val job = launch {
                viewModel.uiState.toList(emittedValues)
            }

            val voucherImages = listOf(squareImage, horizontalImage)

            viewModel.processEvent(VoucherImageEvent.PopulateInitialData(voucherId, voucherImages))
            viewModel.processEvent(VoucherImageEvent.AddImageToSelection(squareImage.imageUrl))
            viewModel.processEvent(VoucherImageEvent.AddImageToSelection(horizontalImage.imageUrl))

            //When
            viewModel.processEvent(VoucherImageEvent.RemoveImageFromSelection(squareImage.imageUrl))

            //Then
            val actual = emittedValues.last()

            assertEquals(
                listOf(squareImage.copy(isSelected = false), horizontalImage.copy(isSelected = true)),
                actual.voucherImages
            )
            assertEquals(
                listOf(horizontalImage.copy(isSelected = true)),
                actual.selectedImageUrls
            )

            job.cancel()
        }
    }
    //endregion

    //region handleTapDropdown
    @Test
    fun `When tap square image, square image should be expanded while horizontal image collapsed`() {
        runBlockingTest {
            //Given
            val emittedValues = arrayListOf<VoucherImageUiState>()
            val job = launch {
                viewModel.uiState.toList(emittedValues)
            }

            val voucherImages = listOf(squareImage, horizontalImage)

            viewModel.processEvent(VoucherImageEvent.PopulateInitialData(voucherId, voucherImages))

            //When
            viewModel.processEvent(VoucherImageEvent.TapDropdownIcon(squareImage.imageUrl))

            //Then
            val actual = emittedValues.last()

            assertEquals(
                listOf(squareImage.copy(isExpanded = true), horizontalImage.copy(isExpanded = false)),
                actual.voucherImages
            )

            job.cancel()
        }
    }

    @Test
    fun `When tap square image that already expanded, square image should be collapsed`() {
        runBlockingTest {
            //Given
            val emittedValues = arrayListOf<VoucherImageUiState>()
            val job = launch {
                viewModel.uiState.toList(emittedValues)
            }

            val voucherImages = listOf(squareImage.copy(isExpanded = true), horizontalImage)

            viewModel.processEvent(VoucherImageEvent.PopulateInitialData(voucherId, voucherImages))

            //When
            viewModel.processEvent(VoucherImageEvent.TapDropdownIcon(squareImage.imageUrl))

            //Then
            val actual = emittedValues.last()

            assertEquals(
                listOf(squareImage.copy(isExpanded = false), horizontalImage.copy(isExpanded = false)),
                actual.voucherImages
            )

            job.cancel()
        }
    }

    @Test
    fun `When tap unlisted image url, image should be still collapsed`() {
        runBlockingTest {
            //Given
            val unlistedImageUrl = "some unlisted image url"
            val emittedValues = arrayListOf<VoucherImageUiState>()
            val job = launch {
                viewModel.uiState.toList(emittedValues)
            }

            val voucherImages = listOf(squareImage, horizontalImage)

            viewModel.processEvent(VoucherImageEvent.PopulateInitialData(voucherId, voucherImages))

            //When
            viewModel.processEvent(VoucherImageEvent.TapDropdownIcon(unlistedImageUrl))

            //Then
            val actual = emittedValues.last()

            assertEquals(
                listOf(squareImage.copy(isExpanded = false), horizontalImage.copy(isExpanded = false)),
                actual.voucherImages
            )

            job.cancel()
        }
    }
    //endregion

    //region processEvent
    @Test
    fun `When unlisted event is triggered, should not emit any effect`() {
        runBlockingTest {
            //When
            viewModel.processEvent(mockk())

            val emittedEffects = arrayListOf<VoucherImageEffect>()

            val job = launch {
                viewModel.uiEffect.toList(emittedEffects)
            }

            //Then
            val actualEffect = emittedEffects.lastOrNull()

            assertEquals(null, actualEffect)

            job.cancel()
        }
    }
    //endregion


}
