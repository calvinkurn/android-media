package com.tokopedia.editor.ui.text

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tokopedia.editor.ui.main.EditorParamFetcher
import com.tokopedia.editor.ui.model.InputTextModel
import com.tokopedia.editor.util.provider.ColorProvider
import com.tokopedia.editor.util.FontAlignment
import com.tokopedia.editor.util.FontAlignment.Companion.next
import com.tokopedia.editor.util.FontDetail
import javax.inject.Inject

class InputTextViewModel @Inject constructor(
    private val colorProvider: ColorProvider
) : ViewModel() {

    private val _selectedTextColor = MutableLiveData(-1)
    val selectedTextColor: LiveData<Int> get() = _selectedTextColor

    private val _selectedAlignment = MutableLiveData(FontAlignment.CENTER)
    val selectedAlignment: LiveData<FontAlignment> get() = _selectedAlignment

    // 1st text, 2nd background
    private val _backgroundColorSet = MutableLiveData<Pair<Int, Int>?>(null)
    val backgroundColorSet: LiveData<Pair<Int, Int>?> get() = _backgroundColorSet

    private val _selectedFontStyle: MutableLiveData<FontDetail> =
        MutableLiveData(FontDetail.OPEN_SAUCE_ONE_REGULAR)
    val selectedFontStyle: LiveData<FontDetail> get() = _selectedFontStyle

    private val _textValue = MutableLiveData("")
    val textValue: LiveData<String> get() = _textValue

    // temporary, will be adjust later when implement (event - effect) pattern so no need extra livedata
    private val _isActivitySave = MutableLiveData(false)
    val isActivitySave: LiveData<Boolean> get() = _isActivitySave

    fun updateSelectedColor(colorId: Int) {
        _selectedTextColor.value = colorId

        if (_backgroundColorSet.value != null) {
            colorProvider.getColorMap()[colorId]?.let {
                updateBackgroundState(Pair(it.textColorAlternate, it.colorInt))
            }
        }
    }

    fun getCurrentSelectedColor(): Int {
        return _selectedTextColor.value ?: -1
    }

    fun increaseAlignment() {
        _selectedAlignment.value = _selectedAlignment.value?.next()
    }

    fun updateAlignment(alignmentTarget: FontAlignment) {
        val diff = alignmentTarget.value - FontAlignment.CENTER.value
        for (i in 0 until diff) {
            increaseAlignment()
        }
    }

    fun updateBackgroundState(backgroundColorSet: Pair<Int, Int>?) {
        _backgroundColorSet.value = backgroundColorSet
    }

    fun updateFontStyle(fontDetail: FontDetail) {
        _selectedFontStyle.value = fontDetail
    }

    fun updateText(newText: String) {
        _textValue.value = newText
    }

    fun getTextDetail(): InputTextModel {
        val text = _textValue.value ?: ""
        val textColor = _selectedTextColor.value ?: -1
        val textAlignment = _selectedAlignment.value ?: FontAlignment.CENTER
        val fontDetail = _selectedFontStyle.value ?: FontDetail.OPEN_SAUCE_ONE_REGULAR

        val result = InputTextModel(
            text = text,
            textAlign = textAlignment,
            textColor = textColor,
            fontDetail = fontDetail
        )

        _backgroundColorSet.value?.let {
            result.textColor = it.first
            result.backgroundColor = it.second
        }

        return result
    }

    fun saveInputText() {
        _isActivitySave.value = true
    }
}
