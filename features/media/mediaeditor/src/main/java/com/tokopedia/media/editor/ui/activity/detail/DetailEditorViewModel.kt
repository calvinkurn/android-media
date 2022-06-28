package com.tokopedia.media.editor.ui.activity.detail

import android.graphics.ColorMatrixColorFilter
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.media.editor.data.tool.ColorFilterManager
import com.tokopedia.media.editor.domain.SetRemoveBackgroundUseCase
import com.tokopedia.media.editor.ui.uimodel.EditorDetailUiModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

class DetailEditorViewModel @Inject constructor(
    private val colorFilter: ColorFilterManager,
    private val removeBackground: SetRemoveBackgroundUseCase
) : ViewModel() {

    private var _intentUiModel = MutableLiveData<EditorDetailUiModel>()
    val intentUiModel: LiveData<EditorDetailUiModel> get() = _intentUiModel

    private var _brightnessValue = MutableLiveData<ColorMatrixColorFilter>()
    val brightnessValue: LiveData<ColorMatrixColorFilter> get() = _brightnessValue

    private var _removeBackgroundResult = MutableLiveData<File?>()
    val removeBackgroundResult: LiveData<File?> get() = _removeBackgroundResult

    fun setIntentOnUiModel(data: EditorDetailUiModel) {
        _intentUiModel.postValue(data)
    }

    fun setBrightness(value: Float) {
        _brightnessValue.value = colorFilter.brightness(value)
    }

    fun setRemoveBackground(filePath: String) {
        viewModelScope.launch {
            removeBackground(filePath)
                .collect {
                    _removeBackgroundResult.value = it
                }
        }
    }

}