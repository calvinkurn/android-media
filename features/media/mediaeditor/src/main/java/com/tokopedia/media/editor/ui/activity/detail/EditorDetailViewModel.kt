package com.tokopedia.media.editor.ui.activity.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tokopedia.media.editor.ui.uimodel.EditorDetailUiModel

class EditorDetailViewModel : ViewModel() {

    private var _intentUiModel = MutableLiveData<EditorDetailUiModel>()
    val intentUiModel: LiveData<EditorDetailUiModel> get() = _intentUiModel

    fun setIntentOnUiModel(data: EditorDetailUiModel) {
        _intentUiModel.postValue(data)
    }

}