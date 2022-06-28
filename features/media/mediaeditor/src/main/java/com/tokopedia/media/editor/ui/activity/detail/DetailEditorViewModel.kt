package com.tokopedia.media.editor.ui.activity.detail

import android.graphics.ColorMatrixColorFilter
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tokopedia.media.editor.data.tool.ColorFilterManager
import com.tokopedia.media.editor.ui.uimodel.EditorDetailUiModel
import javax.inject.Inject

class DetailEditorViewModel @Inject constructor(
    private val colorFilter: ColorFilterManager
) : ViewModel() {

    private var _intentUiModel = MutableLiveData<EditorDetailUiModel>()
    val intentUiModel: LiveData<EditorDetailUiModel> get() = _intentUiModel

    private var _brightnessValue = MutableLiveData<ColorMatrixColorFilter>()
    val brightnessValue: LiveData<ColorMatrixColorFilter> get() = _brightnessValue

    fun setIntentOnUiModel(data: EditorDetailUiModel) {
        _intentUiModel.postValue(data)
    }

    fun setBrightness(value: Float) {
        _brightnessValue.value = colorFilter.brightness(value)
    }

}