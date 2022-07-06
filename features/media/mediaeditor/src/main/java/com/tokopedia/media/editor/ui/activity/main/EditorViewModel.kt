package com.tokopedia.media.editor.ui.activity.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tokopedia.media.editor.ui.uimodel.EditorDetailUiModel
import com.tokopedia.picker.common.EditorParam

class EditorViewModel : ViewModel() {

    private var editStateList = mutableListOf<EditorDetailUiModel>()

    private var _editorParam = MutableLiveData<EditorParam>()
    val editorParam: LiveData<EditorParam> get() = _editorParam

    fun setEditorParam(data: EditorParam) {
        _editorParam.postValue(data)
    }

    fun addEditState(newValue: EditorDetailUiModel){
        editStateList.add(newValue)
    }

    fun getEditState(): List<EditorDetailUiModel>{
        return editStateList
    }

}