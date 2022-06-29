package com.tokopedia.media.editor.ui.activity.detail

import android.graphics.ColorMatrixColorFilter
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.media.editor.data.repository.ColorFilterRepository
import com.tokopedia.media.editor.domain.SetRemoveBackgroundUseCase
import com.tokopedia.media.editor.ui.uimodel.EditorDetailUiModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
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

    private var _brightnessValue = MutableLiveData<ColorMatrixColorFilter>()
    val brightnessValue: LiveData<ColorMatrixColorFilter> get() = _brightnessValue

    private var _removeBackground = MutableLiveData<File?>()
    val removeBackground: LiveData<File?> get() = _removeBackground

    fun setIntentDetailUiModel(data: EditorDetailUiModel) {
        _intentUiModel.postValue(data)
    }

    fun setBrightness(value: Float) {
        _brightnessValue.value = colorFilterRepository.brightness(value)
    }

    fun setRemoveBackground(filePath: String) {
        viewModelScope.launch {
            removeBackgroundUseCase(filePath)
                .onStart { _isLoading.value = true }
                .onCompletion { _isLoading.value = false }
                .collect {
                    _removeBackground.value = it
                }
        }
    }

}