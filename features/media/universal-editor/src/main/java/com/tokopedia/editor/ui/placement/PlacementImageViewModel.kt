package com.tokopedia.editor.ui.placement

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.editor.data.repository.ImageSaveRepository
import com.tokopedia.editor.ui.model.ImagePlacementModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

class PlacementImageViewModel @Inject constructor(
    private val imageFlattenRepo: ImageSaveRepository,
    private val dispatchers: CoroutineDispatchers
) : ViewModel() {

    private var _imagePath = MutableLiveData("")
    val imagePath get() = _imagePath

    private var _placementModel = MutableLiveData<ImagePlacementModel?>(null)
    val placementModel get() = _placementModel

    var initialImageMatrix: FloatArray? = null

    var isLoadingShow = MutableLiveData(false)

    private var _placementModelResult = MutableLiveData<ImagePlacementModel>(null)
    val placementModelResult get() = _placementModelResult

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

    fun savePlacementBitmap(outputPath: String, bitmap: Bitmap, translateX: Float, translateY: Float, scale: Float, angle: Float) {
        viewModelScope.launch(dispatchers.io) {
            async {
                // save placement bitmap result
                try {
                    val resultPath = imageFlattenRepo.saveBitmap(outputPath, bitmap)

                    // matrix to model
                    val asdasd = ImagePlacementModel(
                        path = resultPath,
                        scale = scale,
                        angle = angle,
                        translateX = translateX,
                        translateY = translateY
                    )

                    _placementModelResult.postValue(asdasd)
                } catch (_: Exception) {}
            }
        }
    }
}
