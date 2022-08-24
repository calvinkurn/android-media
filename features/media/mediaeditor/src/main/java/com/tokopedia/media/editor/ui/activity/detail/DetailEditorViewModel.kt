package com.tokopedia.media.editor.ui.activity.detail

import android.graphics.ColorMatrixColorFilter
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.media.editor.data.repository.ColorFilterRepository
import com.tokopedia.media.editor.domain.SetRemoveBackgroundUseCase
import com.tokopedia.media.editor.ui.uimodel.EditorDetailUiModel
import com.tokopedia.picker.common.EditorParam
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEmpty
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

class DetailEditorViewModel @Inject constructor(
    private val colorFilterRepository: ColorFilterRepository,
    private val removeBackgroundUseCase: SetRemoveBackgroundUseCase
) : ViewModel() {

    private var _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private var _intentUiModel = MutableLiveData<EditorDetailUiModel>()
    val intentUiModel: LiveData<EditorDetailUiModel> get() = _intentUiModel

    private var _brightnessFilter = MutableLiveData<ColorMatrixColorFilter>()
    val brightnessFilter: LiveData<ColorMatrixColorFilter> get() = _brightnessFilter

    private var _removeBackground = MutableLiveData<File?>()
    val removeBackground: LiveData<File?> get() = _removeBackground

    private var _contrastFilter = MutableLiveData<Float>()
    val contrastFilter: LiveData<Float> get() = _contrastFilter

    private var _watermarkFilter = MutableLiveData<Int>()
    val watermarkFilter: LiveData<Int> get() = _watermarkFilter

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

    fun setContrast(value: Float?) {
        if (value == null) return
        _contrastFilter.value = value
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

    fun setWatermark(watermarkType: Int?) {
        if (watermarkType == null) return
        _watermarkFilter.value = watermarkType
    }

}