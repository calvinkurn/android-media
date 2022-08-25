package com.tokopedia.media.editor.ui.activity.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tokopedia.media.editor.ui.uimodel.EditorDetailUiModel
import com.tokopedia.media.editor.ui.uimodel.EditorUiModel
import com.tokopedia.picker.common.EditorParam

class EditorViewModel : ViewModel() {

    private var _editStateList = mutableMapOf<String, EditorUiModel>()
    val editStateList: Map<String, EditorUiModel> get() = _editStateList

    private var _updatedIndexItem = MutableLiveData(0)
    val updatedIndexItem: LiveData<Int> get() = _updatedIndexItem

    private var _editorParam = MutableLiveData<EditorParam>()
    val editorParam: LiveData<EditorParam> get() = _editorParam

    fun setEditorParam(data: EditorParam) {
        _editorParam.postValue(data)
    }

    fun initStateList(urlCollection: List<String>) {
        urlCollection.forEach {
            _editStateList[it] = EditorUiModel(it)
        }
    }

    fun addEditState(urlKey: String, newValue: EditorDetailUiModel) {
        val state = getEditState(urlKey)
        if (state == null) {
            val newKeyObject = EditorUiModel(urlKey)
            newKeyObject.editList.add(newValue)
            _editStateList[urlKey] = newKeyObject
        } else {
            // if state not last edit (user did undo and do edit again) then we will remove last state until current redo state)
            if (state.backValue != 0) {
                for (i in 0 until state.backValue) {
                    if (state.editList.last().removeBackgroundUrl != null) {
                        state.removedBackgroundUrl = null
                        state.removeBackgroundStartState = 0
                    }
                    state.editList.removeLast()
                }
                state.backValue = 0
            }

            if (newValue.removeBackgroundUrl != null) {
                state.removedBackgroundUrl = newValue.removeBackgroundUrl
                state.removeBackgroundStartState = state.editList.size
            }

            state.editList.add(newValue)
        }

        updateEditedItem(urlKey)
    }

    fun getEditState(urlKey: String): EditorUiModel? {
        return _editStateList[urlKey]
    }

    private fun updateEditedItem(originalUrl: String) {
        _updatedIndexItem.value = getKeyIndex(originalUrl)
    }

    private fun getKeyIndex(urlKey: String): Int? {
        var index = -1

        editStateList.values.forEachIndexed { indexItem, item ->
            val isMatch = item.getOriginalUrl() == urlKey
            if (isMatch) {
                index = indexItem
                return@forEachIndexed
            }
        }

        return if (index == -1) null else index
    }

}