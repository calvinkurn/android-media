package com.tokopedia.product_ar.viewmodel

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.modiface.mfemakeupkit.effects.MFEMakeupLook
import com.tokopedia.product_ar.model.ComparissonImageUiModel
import com.tokopedia.product_ar.model.ModifaceUiModel
import com.tokopedia.product_ar.model.state.ImageMapMode
import com.tokopedia.product_ar.model.state.ModifaceImageGridState
import com.tokopedia.product_ar.util.ProductArMapper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class ProductArComparissonViewModel @Inject constructor() : ViewModel() {

    private var counter = 0
    private var latestSelectedProductName = ""

    private val _processedVariantData = MutableLiveData<List<ModifaceUiModel>>()
    val processedVariantData: LiveData<List<ModifaceUiModel>>
        get() = _processedVariantData

    private val _addRemoveImageGrid = MutableStateFlow(ModifaceImageGridState())
    val addRemoveImageGrid: StateFlow<ModifaceImageGridState>
        get() = _addRemoveImageGrid

    private val _generateMakeUpBackground = MutableLiveData(MFEMakeupLook())
    val generateMakeUpBackground: LiveData<MFEMakeupLook>
        get() = _generateMakeUpBackground

    fun renderInitialData(
            processedImage: Bitmap,
            data: List<ModifaceUiModel>) {
        try {
            val intialSelectedProductName = ProductArMapper.getInitialSelectedProductName(data)
            latestSelectedProductName = intialSelectedProductName
            addGridImages(processedImage, listOf())
            renderInitialVariantData(data)
        } catch (e: Throwable) {

        }
    }

    fun addGridImages(processedImage: Bitmap,
                      currentList: List<ComparissonImageUiModel>) {
        try {
            val updatedData = currentList.toMutableList()
            if (updatedData.size < 4) {
                updatedData.add(ComparissonImageUiModel(processedImage, latestSelectedProductName))

                _addRemoveImageGrid.value = _addRemoveImageGrid.value.copy(
                        mode = ImageMapMode.APPEND,
                        imagesBitmap = updatedData,
                        spanSize = if (updatedData.size == 1) 1 else 2
                )
            }
        } catch (e: Throwable) {

        }
    }

    fun onVariantClicked(data: List<ModifaceUiModel>,
                         selectedProductId: String,
                         selectedProductName: String,
                         isSelected: Boolean) {
        try {
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
                processImageWithModify(updatedVariants, selectedProductId, selectedProductName)
            } else {
                //remove state
                removeImageAtPosition(
                        isSelected,
                        data,
                        selectedProductId
                )
            }

            _processedVariantData.value = updatedVariants
        } catch (e: Throwable) {

        }
    }

    private fun renderInitialVariantData(data: List<ModifaceUiModel>) {
        counter++
        val updatedData = ProductArMapper.updateInitialListWithCounter(data)
        _processedVariantData.value = updatedData
    }


    private fun processImageWithModify(updatedVariants: List<ModifaceUiModel>,
                                       selectedProductId: String,
                                       selectedProductName: String) {
        val selectedMfLookData = ProductArMapper.getMfMakeUpLookByProductId(
                updatedVariants,
                selectedProductId
        )

        latestSelectedProductName = selectedProductName
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
            try {
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
            } catch (e: Throwable) {
            }
        }
    }
}