package com.tokopedia.media.editor.ui.activity.addtext

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tokopedia.media.editor.data.repository.AddTextFilterRepository
import com.tokopedia.media.editor.ui.uimodel.EditorAddTextUiModel
import com.tokopedia.media.editor.utils.getImageSize
import javax.inject.Inject

class AddTextViewModel @Inject constructor(
    private val addTextFilterRepository: AddTextFilterRepository
): ViewModel() {
    private var _imgUrl = MutableLiveData<String>()
    val imgUrl: LiveData<String> get() = _imgUrl

    private var _textInput = MutableLiveData<String>()
    val textInput: LiveData<String> get() = _textInput

    var textData = EditorAddTextUiModel("")
    set(value) {
        _textInput.value = value.textValue
        field = value
    }

    private var _pageMode = MutableLiveData<Int>()
    val pageMode: LiveData<Int> get() = _pageMode

    fun setImageUrl(url: String) {
        _imgUrl.value = url
    }

    fun setTextInput(input: String) {
        _textInput.value = input
    }

    fun setPageMode(mode: Int) {
        _pageMode.value = mode
    }

    fun getAddTextFilterOverlay(): Bitmap? {
        return addTextFilterRepository.generateOverlayText(
            getImageSize(_imgUrl.value ?: ""),
            textData
        )
    }
}
