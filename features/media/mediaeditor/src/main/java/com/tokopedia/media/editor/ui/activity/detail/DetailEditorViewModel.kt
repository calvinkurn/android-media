package com.tokopedia.media.editor.ui.activity.detail

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ColorMatrixColorFilter
import android.widget.ImageView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.media.editor.data.repository.ColorFilterRepository
import com.tokopedia.media.editor.data.repository.ContrastFilterRepository
import com.tokopedia.media.editor.data.repository.RotateFilterRepository
import com.tokopedia.media.editor.data.repository.WatermarkFilterRepository
import com.tokopedia.media.editor.domain.SetRemoveBackgroundUseCase
import com.tokopedia.media.editor.ui.component.EditorDetailPreviewImage
import com.tokopedia.media.editor.ui.uimodel.EditorDetailUiModel
import com.tokopedia.picker.common.EditorParam
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

class DetailEditorViewModel @Inject constructor(
    private val colorFilterRepository: ColorFilterRepository,
    private val removeBackgroundUseCase: SetRemoveBackgroundUseCase,
    private val contrastFilterRepository: ContrastFilterRepository,
    private val watermarkFilterRepository: WatermarkFilterRepository,
    private val rotateFilterRepository: RotateFilterRepository
) : ViewModel() {

    private var _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private var _intentUiModel = MutableLiveData<EditorDetailUiModel>()
    val intentUiModel: LiveData<EditorDetailUiModel> get() = _intentUiModel

    private var _brightnessFilter = MutableLiveData<ColorMatrixColorFilter>()
    val brightnessFilter: LiveData<ColorMatrixColorFilter> get() = _brightnessFilter

    private var _removeBackground = MutableLiveData<File?>()
    val removeBackground: LiveData<File?> get() = _removeBackground

    private var _contrastFilter = MutableLiveData<Bitmap>()
    val contrastFilter: LiveData<Bitmap> get() = _contrastFilter

    private var _watermarkFilter = MutableLiveData<Bitmap>()
    val watermarkFilter: LiveData<Bitmap> get() = _watermarkFilter

    private var _editorParam = MutableLiveData<EditorParam>()
    val editorParam: LiveData<EditorParam> get() = _editorParam

    fun setEditorParam(data: EditorParam) {
        _editorParam.postValue(data)
    }

    fun setIntentDetailUiModel(data: EditorDetailUiModel) {
        _intentUiModel.postValue(data)
    }

    fun setBrightness(value: Float?) {
        if (value == null) return
        _brightnessFilter.value = colorFilterRepository.brightness(value)
    }

    fun setContrast(value: Float?, sourceBitmap: Bitmap?) {
        if (value == null || sourceBitmap == null) return
        _contrastFilter.value = contrastFilterRepository.contrast(
            value,
            sourceBitmap.copy(sourceBitmap.config, true)
        )
    }

    fun setRemoveBackground(filePath: String, onError: (t: Throwable) -> Unit) {
        viewModelScope.launch {
            try {
                removeBackgroundUseCase(filePath)
                    .onStart {
                        _isLoading.value = true
                    }
                    .onCompletion {
                        _isLoading.value = false
                    }.collect {
                        _removeBackground.value = it
                    }
            } catch (e: Exception){
                onError(e)
            }
        }
    }

    fun setWatermark(
        context: Context,
        bitmapSource: Bitmap,
        watermarkType: Int,
        shopName: String,
        isThumbnail: Boolean = false
    ) {
        _watermarkFilter.value = watermarkFilterRepository.watermark(
            context,
            bitmapSource,
            watermarkType,
            shopName,
            isThumbnail
        )
    }

    fun setWatermarkFilterThumbnail(
        context: Context,
        implementedBaseBitmap: Bitmap?,
        shopName: String,
        buttonRef: Pair<ImageView, ImageView>
    ) {
        return watermarkFilterRepository.watermarkDrawerItem(
            context,
            implementedBaseBitmap,
            shopName,
            buttonRef
        )
    }

    fun setRotate(ucropRef: EditorDetailPreviewImage?, rotateDegree: Float, isRotateRatio: Boolean){
        ucropRef?.let {
            rotateFilterRepository.rotate(it, rotateDegree, isRotateRatio)
        }
    }

    fun setMirror(ucropRef: EditorDetailPreviewImage?,){
        ucropRef?.let {
            rotateFilterRepository.mirror(it)
        }
    }

    var rotateNumber: Int get() = rotateFilterRepository.rotateNumber
    set(value) {rotateFilterRepository.rotateNumber = value}

    var rotateSliderValue: Float get() = rotateFilterRepository.sliderValue
    set(value) {rotateFilterRepository.sliderValue = value}

    val rotateRotationFinalDegree: Float get() = rotateFilterRepository.getFinalRotationDegree()
}