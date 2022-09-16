package com.tokopedia.media.editor.ui.activity.main

import android.content.Context
import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tokopedia.media.editor.data.repository.SaveImageRepository
import com.tokopedia.media.editor.ui.uimodel.EditorDetailUiModel
import com.tokopedia.media.editor.ui.uimodel.EditorUiModel
import com.tokopedia.picker.common.EditorParam
import com.tokopedia.picker.common.ImageRatioType
import com.tokopedia.picker.common.types.EditorToolType
import java.io.File
import javax.inject.Inject

class EditorViewModel @Inject constructor(
    private val saveImageRepository: SaveImageRepository
) : ViewModel() {

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

            if (newValue.removeBackgroundUrl != null && state.removeBackgroundStartState == null) {
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

    fun cleanImageCache(){
        saveImageRepository.clearEditorCache()
    }

    fun undoState(activeImageUrl: String): EditorUiModel?{
        getEditState(activeImageUrl)?.let {
            val imageEditStateCount = it.editList.size
            if (it.backValue >= imageEditStateCount) return@let

            it.backValue++
            return it
        }
        return null
    }

    fun redoState(activeImageUrl: String): EditorUiModel? {
        getEditState(activeImageUrl)?.let {
            if (it.backValue == 0) return@let

            it.backValue--
            return it
        }
        return null
    }

    fun cropImage(context: Context, sourceBitmap: Bitmap?, editorDetailUiModel: EditorUiModel) {
        sourceBitmap?.let { it ->
            val bitmapWidth = sourceBitmap.width
            val bitmapHeight = sourceBitmap.height

            val autoCropRatio = editorParam.value?.autoCropRatio?.let {
                it.getRatioY().toFloat() / it.getRatioX()
            } ?: 1f

            var newWidth = bitmapWidth
            var newHeight = (bitmapWidth * autoCropRatio).toInt()

            var topMargin = 0
            var leftMargin = 0

            var scaledTarget = 1f

            if (newHeight <= bitmapHeight && newWidth <= bitmapWidth) {
                leftMargin = (bitmapWidth - newWidth) / 2
                topMargin = (bitmapHeight - newHeight) / 2
            } else if (newHeight > bitmapHeight) {
                scaledTarget = bitmapHeight.toFloat() / newHeight

                // new value after rescale small
                newWidth = (newWidth * scaledTarget).toInt()
                newHeight = (newHeight * scaledTarget).toInt()

                leftMargin = (bitmapWidth - newWidth) / 2
                topMargin = (bitmapHeight - newHeight) / 2
            }

            val bitmapResult = Bitmap.createBitmap(it, leftMargin, topMargin, newWidth, newHeight)
            val savedFile = saveImageRepository.saveToCache(
                context, bitmapResult
            )?.absolutePath ?: ""

            val newEditorDetailUiModel = EditorDetailUiModel(
                originalUrl = editorDetailUiModel.getOriginalUrl(),
                editorToolType = EditorToolType.CROP,
                resultUrl = savedFile,
            )
            newEditorDetailUiModel.cropRotateValue.apply {
                offsetX = leftMargin
                offsetY = topMargin
                imageWidth = newWidth
                imageHeight = newHeight
                scaleX = 1f
                scaleY = 1f
                isCrop = true
                this.isAutoCrop = true
                croppedSourceWidth = it.width
            }

            editorDetailUiModel.editList.add(newEditorDetailUiModel)
        }
    }

    fun saveToGallery(context: Context, imageList: List<String>, onFinish: (result: List<String>) -> Unit){
        saveImageRepository.saveToGallery(
            context,
            imageList
        ) {
            onFinish(it)
        }
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