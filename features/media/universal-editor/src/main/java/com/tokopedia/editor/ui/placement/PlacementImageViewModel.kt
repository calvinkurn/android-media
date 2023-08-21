package com.tokopedia.editor.ui.placement

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tokopedia.editor.ui.model.ImagePlacementModel
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.concurrent.Flow
import javax.inject.Inject

class PlacementImageViewModel @Inject constructor(): ViewModel() {

    private var _imagePath = MutableLiveData<String>("")
    val imagePath get() = _imagePath

    private var _placementModel = MutableLiveData<ImagePlacementModel?>(null)
    val placementModel get() = _placementModel

    fun setPlacementModel(placementModel: ImagePlacementModel) {
        _placementModel.value = placementModel
    }

    fun setImagePath(imagePath: String) {
        _imagePath.value = imagePath
    }
}
