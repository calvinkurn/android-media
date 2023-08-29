package com.tokopedia.editor.ui.text

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tokopedia.editor.ui.model.InputTextModel
import com.tokopedia.editor.util.FontAlignment
import com.tokopedia.editor.util.FontAlignment.Companion.next
import com.tokopedia.editor.util.FontDetail
import javax.inject.Inject

class InputTextViewModel @Inject constructor(): ViewModel() {
    private val _selectedTextColor = MutableLiveData(-1)
    val selectedTextColor: LiveData<Int> get() = _selectedTextColor

    private val _selectedAlignment = MutableLiveData(FontAlignment.CENTER)
    val selectedAlignment: LiveData<FontAlignment> get() = _selectedAlignment

    // 1st text, 2nd background
    private val _backgroundColorSet = MutableLiveData<Pair<Int, Int>?>(null)
    val backgroundColorSet: LiveData<Pair<Int, Int>?> get() = _backgroundColorSet

    private val _selectedFontStyle: MutableLiveData<FontDetail> = MutableLiveData(FontDetail.OPEN_SAUCE_ONE_REGULAR)
    val selectedFontStyle: LiveData<FontDetail> get() = _selectedFontStyle

    private val _textValue = MutableLiveData("")
    val textValue: LiveData<String> get() = _textValue

    fun updateSelectedColor(colorId: Int) {
        _selectedTextColor.value = colorId
    }

    fun getCurrentSelectedColor(): Int {
        return _selectedTextColor.value ?: -1
    }

    fun increaseAlignment() {
        _selectedAlignment.value = _selectedAlignment.value?.next()
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

        val result = InputTextModel(
            text = text,
            textAlign = textAlignment,
            textColor = textColor
        )

        _backgroundColorSet.value?.let {
            result.textColor = it.first
            result.backgroundColor = it.second
        }

        return result
    }
}
