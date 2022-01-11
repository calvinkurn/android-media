package com.tokopedia.product_ar.viewmodel

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.modiface.mfemakeupkit.effects.MFEMakeupLook
import com.tokopedia.product_ar.model.ModifaceUiModel
import com.tokopedia.product_ar.model.state.ImageMapMode
import com.tokopedia.product_ar.model.state.ModifaceImageGridState
import com.tokopedia.product_ar.util.ProductArMapper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class ProductArComparisonViewModel @Inject constructor() : ViewModel() {

    var counter = 0

    private val _processedVariantData = MutableLiveData<List<ModifaceUiModel>>()
    val processedVariantData: LiveData<List<ModifaceUiModel>>
        get() = _processedVariantData

    private val _addRemoveImageGrid = MutableStateFlow(ModifaceImageGridState())
    val addRemoveImageGrid: StateFlow<ModifaceImageGridState>
        get() = _addRemoveImageGrid

    private val _generateMakeUpBackground = MutableLiveData(MFEMakeupLook())
    val generateMakeUpBackground: LiveData<MFEMakeupLook>
        get() = _generateMakeUpBackground

    fun addGridImages(processedImage: Bitmap,
                      currentList: List<Bitmap>) {
        val updatedData = currentList.toMutableList()
        updatedData.add(processedImage)

        _addRemoveImageGrid.value = _addRemoveImageGrid.value.copy(
                mode = ImageMapMode.APPEND,
                imagesBitmap = updatedData,
                spanSize = if (updatedData.size == 1) 1 else 2
        )
    }

    fun renderInitialData(data: List<ModifaceUiModel>) {
        counter++
        val updatedData = ProductArMapper.updateInitialListWithCounter(data)
        _processedVariantData.value = updatedData
    }

    fun setSelectedVariant(data: List<ModifaceUiModel>,
                           selectedProductId: String,
                           isSelected: Boolean) {
        val needToDisableSelection = ProductArMapper.needToDisableSelection(
                selectedProductId = selectedProductId,
                data = data,
                isSelected = isSelected,
                currentCounter = counter
        )

        if (needToDisableSelection) return

        val updatedVariants = ProductArMapper.updateListAfterSelectedCounter(
                data = data,
                counterTotal = counter,
                selectedProductId = selectedProductId,
                selectedListener = {
                    counter++
                },
                unselectedListener = {
                    counter--
                }
        )

        if (!isSelected) {
            //append state
            processImageWithModify(updatedVariants, selectedProductId)
        } else {
            //remove state
            removeImageAtPosition(
                    isSelected,
                    data,
                    selectedProductId
            )
        }

        _processedVariantData.value = updatedVariants
    }

    private fun processImageWithModify(updatedVariants: List<ModifaceUiModel>,
                                       selectedProductId: String) {
        val selectedMfLookData = ProductArMapper.getMfMakeUpLookByProductId(
                updatedVariants,
                selectedProductId
        )
        _generateMakeUpBackground.value = selectedMfLookData
    }

    private fun removeImageAtPosition(isSelected: Boolean,
                                      unUpdatedData: List<ModifaceUiModel>,
                                      selectedProductId: String) {
        val removedPosition = ProductArMapper.decideRemovedPosition(
                isSelected = isSelected,
                unUpdatedList = unUpdatedData,
                selectedProductId = selectedProductId)

        if (removedPosition >= 0) {
            val currentBitmaps = _addRemoveImageGrid.value.imagesBitmap.toMutableList()
            currentBitmaps.removeAt(removedPosition)

            _addRemoveImageGrid.value = _addRemoveImageGrid.value.copy(
                    mode = ImageMapMode.REMOVE,
                    imagesBitmap = currentBitmaps,
                    spanSize = ProductArMapper.decideSpanSize(
                            listImages = currentBitmaps,
                            mode = ImageMapMode.REMOVE
                    ),
                    removePosition = removedPosition
            )
        }
    }
}