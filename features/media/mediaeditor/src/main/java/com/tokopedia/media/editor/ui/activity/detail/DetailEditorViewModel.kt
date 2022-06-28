package com.tokopedia.media.editor.ui.activity.detail

import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tokopedia.media.editor.ui.uimodel.EditorDetailUiModel

class DetailEditorViewModel : ViewModel() {

    private var _intentUiModel = MutableLiveData<EditorDetailUiModel>()
    val intentUiModel: LiveData<EditorDetailUiModel> get() = _intentUiModel

    private var _brightnessValue = MutableLiveData<Int>()
    val brightnessValue: LiveData<Int> get() = _brightnessValue

    fun setIntentOnUiModel(data: EditorDetailUiModel) {
        _intentUiModel.postValue(data)
    }

    fun setBrightness(value: Int) {
        _brightnessValue.value = value
    }

    fun getBrightnessMatrix(brightnessValuePer100: Float): ColorMatrixColorFilter {
        val cmB = ColorMatrix()
        cmB.set(
            floatArrayOf(
                1f, 0f, 0f, 0f, brightnessValuePer100,
                0f, 1f, 0f, 0f, brightnessValuePer100,
                0f, 0f, 1f, 0f, brightnessValuePer100,
                0f, 0f, 0f, 1f, 0f
            )
        )
        return ColorMatrixColorFilter(cmB)
    }

}