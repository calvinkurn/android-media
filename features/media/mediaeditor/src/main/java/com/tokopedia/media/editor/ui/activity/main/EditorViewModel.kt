package com.tokopedia.media.editor.ui.activity.main

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tokopedia.media.editor.data.repository.AddLogoFilterRepository
import com.tokopedia.media.editor.data.repository.BitmapCreationRepository
import com.tokopedia.media.editor.data.repository.SaveImageRepository
import com.tokopedia.media.editor.ui.uimodel.EditorDetailUiModel
import com.tokopedia.media.editor.ui.uimodel.EditorUiModel
import com.tokopedia.media.editor.utils.getTokopediaCacheDir
import com.tokopedia.picker.common.EditorParam
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.picker.common.PICKER_URL_FILE_CODE
import java.io.File
import javax.inject.Inject

class EditorViewModel @Inject constructor(
    private val saveImageRepository: SaveImageRepository,
    private val addLogoFilterRepository: AddLogoFilterRepository,
    private val userSession: UserSessionInterface,
    private val bitmapCreationRepository: BitmapCreationRepository
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

    fun addEditState(urlKey: String, newValue: EditorDetailUiModel, isUpdateUi: Boolean = true) {
        val state = getEditState(urlKey)
        if (state == null) {
            val newKeyObject = EditorUiModel(urlKey)
            newKeyObject.editList.add(newValue)
            _editStateList[urlKey] = newKeyObject
        } else {
            // if state not last edit (user did undo and do edit again) then we will remove last state until current redo state)
            if (state.backValue != 0) {
                (0 until state.backValue).forEach { _ ->
                    if (state.editList.last().removeBackgroundUrl != null) {
                        state.removedBackgroundUrl = null
                        state.removeBackgroundStartState = 0
                    }
                    state.editList.removeLast()
                }
                state.backValue = 0
            }

            if (!newValue.removeBackgroundUrl.equals(state.removedBackgroundUrl)) {
                state.removedBackgroundUrl = newValue.removeBackgroundUrl
                state.removeBackgroundStartState = state.editList.size
            }

            state.editList.add(newValue)
        }

        if (isUpdateUi) updateEditedItem(urlKey)
    }

    fun getEditState(urlKey: String): EditorUiModel? {
        return _editStateList[urlKey]
    }

    fun undoState(activeImageUrl: String): EditorUiModel? {
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

    suspend fun finishPage(
        dataList: List<EditorUiModel>,
        onFinish: (result: List<String>?) -> Unit
    ) {
        val filteredData = dataList.map {
            if (it.isImageEdited()) {
                // if use 'add logo' & 'add text' feature then need to flatten image first
                var flattenBitmap = it.getImageUrl()
                it.getOverlayTextValue()?.textImagePath?.let { textImagePath ->
                    flattenBitmap = saveImageRepository.flattenImage(
                        it.getImageUrl(),
                        textImagePath,
                        it.getOriginalUrl()
                    )
                }

                it.getOverlayLogoValue()?.let { overlayData ->
                    flattenBitmap = saveImageRepository.flattenImage(
                        flattenBitmap,
                        overlayData.overlayLogoUrl,
                        it.getOriginalUrl()
                    )
                }

                flattenBitmap
            } else {
                ""
            }
        }

        onFinish(filteredData)
    }

    fun saveToCache(
        bitmapParam: Bitmap,
        filename: String? = null,
        sourcePath: String
    ): File? {
        return saveImageRepository.saveToCache(
            bitmapParam, filename, sourcePath
        )
    }

    fun isShopAvailable(): Boolean {
        return userSession.hasShop()
    }

    fun isMemoryOverflow(width: Int, height: Int): Boolean {
        return bitmapCreationRepository.isBitmapOverflow(width, height)
    }

    private fun updateEditedItem(originalUrl: String) {
        var index = 0

        editStateList.values.forEachIndexed { indexItem, item ->
            if (item.getOriginalUrl() == originalUrl) {
                index = indexItem
                return@forEachIndexed
            }
        }

        _updatedIndexItem.value = index
    }
}
