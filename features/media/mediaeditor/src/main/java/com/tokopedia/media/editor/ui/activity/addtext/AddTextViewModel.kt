package com.tokopedia.media.editor.ui.activity.addtext

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tokopedia.media.editor.ui.uimodel.EditorAddTextUiModel
import javax.inject.Inject

class AddTextViewModel @Inject constructor(): ViewModel() {
    private var _imgUrl = MutableLiveData<String>()
    val imgUrl: LiveData<String> get() = _imgUrl

    private var _textInput = MutableLiveData<String>()
    val textInput: LiveData<String> get() = _textInput

    var textData = EditorAddTextUiModel("asd \nKamera Cannon K30")
    set(value) {
        _textInput.value = value.textValue
        field = value
    }

    fun setImageUrl(url: String) {
        _imgUrl.value = url
    }

    fun setTextInput(input: String) {
        _textInput.value = input
    }
}
