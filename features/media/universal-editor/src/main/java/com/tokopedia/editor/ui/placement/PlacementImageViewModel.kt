package com.tokopedia.editor.ui.placement

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tokopedia.editor.data.repository.ImageSaveRepository
import com.tokopedia.editor.ui.model.ImagePlacementModel
import javax.inject.Inject

class PlacementImageViewModel @Inject constructor(
    private val imageFlattenRepo: ImageSaveRepository
) : ViewModel() {

    private var _imagePath = MutableLiveData("")
    val imagePath get() = _imagePath

    private var _placementModel = MutableLiveData<ImagePlacementModel?>(null)
    val placementModel get() = _placementModel

    var initialImageMatrix: FloatArray? = null

    var isLoadingShow = MutableLiveData(false)

    fun updateLoadingState(isShow: Boolean) {
        isLoadingShow.value = isShow
    }

    fun setPlacementModel(placementModel: ImagePlacementModel) {
        _placementModel.value = placementModel
    }

    fun setImagePath(imagePath: String) {
        _imagePath.value = imagePath
    }

    fun isShowExitConfirmation(currentMatrix: FloatArray?): Boolean {
        return !currentMatrix.contentEquals(initialImageMatrix)
    }

    fun savePlacementBitmap(outputPath: String, bitmap: Bitmap): String {
        return imageFlattenRepo.saveBitmap(outputPath, bitmap)
    }
}
