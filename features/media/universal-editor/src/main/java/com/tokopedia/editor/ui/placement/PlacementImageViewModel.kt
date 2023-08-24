package com.tokopedia.editor.ui.placement

import android.graphics.Matrix
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tokopedia.editor.ui.model.ImagePlacementModel
import javax.inject.Inject

class PlacementImageViewModel @Inject constructor(): ViewModel() {

    private var _imagePath = MutableLiveData<String>("")
    val imagePath get() = _imagePath

    private var _placementModel = MutableLiveData<ImagePlacementModel?>(null)
    val placementModel get() = _placementModel

    var initialImageMatrix: FloatArray? = null

    fun setPlacementModel(placementModel: ImagePlacementModel) {
        _placementModel.value = placementModel
    }

    fun setImagePath(imagePath: String) {
        _imagePath.value = imagePath
    }

    fun isShowExitConfirmation(currentMatrix: FloatArray?): Boolean {
        return !currentMatrix.contentEquals(initialImageMatrix)
    }
}
