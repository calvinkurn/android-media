package com.tokopedia.media.editor.domain.mapper

import com.tokopedia.media.editor.data.entity.EditorDetailEntity
import com.tokopedia.media.editor.ui.uimodel.EditorDetailUiModel

object EditorDetailEntityMapper {
    fun EditorDetailEntity.map(): EditorDetailUiModel {
        return EditorDetailUiModel(
            watermarkMode = watermarkModeEntityData
        )
    }

    fun EditorDetailUiModel.map(): EditorDetailEntity {
        return EditorDetailEntity(
            watermarkModeEntityData = watermarkMode
        )
    }

}