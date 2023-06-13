package com.tokopedia.mvc.presentation.download

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.mvc.presentation.download.uimodel.VoucherImageEffect
import com.tokopedia.mvc.presentation.download.uimodel.VoucherImageEvent
import com.tokopedia.mvc.presentation.download.uimodel.VoucherImageUiModel
import com.tokopedia.mvc.presentation.download.uimodel.VoucherImageUiState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class DownloadVoucherImageViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.main) {

    private val _uiState = MutableStateFlow(VoucherImageUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEffect = MutableSharedFlow<VoucherImageEffect>(replay = 1)
    val uiEffect = _uiEffect.asSharedFlow()

    private val currentState: VoucherImageUiState
        get() = _uiState.value

    fun processEvent(event: VoucherImageEvent) {
        when(event) {
            is VoucherImageEvent.PopulateInitialData -> handlePopulateInitialData(event.voucherId, event.voucherImages)
            is VoucherImageEvent.AddImageToSelection -> handleAddImageUrlToSelection(event.imageUrl)
            is VoucherImageEvent.RemoveImageFromSelection -> handleRemoveImageUrlFromSelection(event.imageUrl)
            VoucherImageEvent.TapDownloadButton -> handleTapDownloadButton()
            is VoucherImageEvent.TapDropdownIcon -> handleTapDropdown(event.imageUrl)
        }
    }

    private fun handleTapDropdown(imageUrl: String) {
        val isExpanded = currentState.voucherImages.find { it.imageUrl == imageUrl }?.isExpanded.orFalse()
        if (isExpanded) {
            collapseVoucher(imageUrl)
        } else {
            expandVoucher(imageUrl)
        }
    }

    private fun expandVoucher(imageUrl: String) {
        val modifiedVoucherImages = currentState.voucherImages.map { voucherImage ->
            if (voucherImage.imageUrl == imageUrl) {
                voucherImage.copy(isExpanded = true)
            } else {
                voucherImage
            }
        }

        _uiState.update {
            it.copy(voucherImages = modifiedVoucherImages)
        }
    }


    private fun collapseVoucher(imageUrl: String) {
        val modifiedVoucherImages = currentState.voucherImages.map { voucherImage ->
            if (voucherImage.imageUrl == imageUrl) {
                voucherImage.copy(isExpanded = false)
            } else {
                voucherImage
            }
        }

        _uiState.update {
            it.copy(voucherImages = modifiedVoucherImages)
        }
    }

    private fun handlePopulateInitialData(
        voucherId: Long,
        voucherImages: List<VoucherImageUiModel>,
    ) {
        _uiState.update {
            it.copy(
                voucherId = voucherId,
                voucherImages = voucherImages,
                selectedImageUrls = voucherImages.selectedImagesOnly()
            )
        }
    }

    private fun handleAddImageUrlToSelection(imageUrl: String) {
        val modifiedVoucherImages = currentState.voucherImages.map { voucherImage ->
            if (voucherImage.imageUrl == imageUrl) {
                voucherImage.copy(isSelected = true)
            } else {
                voucherImage
            }
        }

        _uiState.update {
            it.copy(
                voucherImages = modifiedVoucherImages,
                selectedImageUrls = modifiedVoucherImages.selectedImagesOnly()
            )
        }
    }


    private fun handleRemoveImageUrlFromSelection(imageUrl: String) {
        val modifiedVoucherImages = currentState.voucherImages.map { voucherImage ->
            if (voucherImage.imageUrl == imageUrl) {
                voucherImage.copy(isSelected = false)
            } else {
                voucherImage
            }
        }

        _uiState.update {
            it.copy(
                voucherImages = modifiedVoucherImages,
                selectedImageUrls = modifiedVoucherImages.selectedImagesOnly()
            )
        }

    }

    private fun handleTapDownloadButton() {
        _uiEffect.tryEmit(
            VoucherImageEffect.DownloadImages(
                currentState.voucherId,
                currentState.selectedImageUrls.selectedImagesOnly()
            )
        )
    }

    private fun List<VoucherImageUiModel>.selectedImagesOnly(): List<VoucherImageUiModel> {
        return filter { it.isSelected }
    }

}
