package com.tokopedia.play.broadcaster.ui.model.pinnedmessage

/**
 * Created by jegul on 12/10/21
 */
data class PinnedMessageUiModel(
    val id: String,
    val message: String,
    val isActive: Boolean,
    val editStatus: PinnedMessageEditStatus,
) {

    val isInvalidId: Boolean
        get() = id.isBlank() || id == "0"

    companion object {
        fun Empty() = PinnedMessageUiModel(
            id = "",
            message = "",
            isActive = false,
            editStatus = PinnedMessageEditStatus.Nothing,
        )
    }
}

enum class PinnedMessageEditStatus {
    Editing,
    Uploading,
    Nothing;

    val isEditing: Boolean
        get() = this == Editing

    val isUploading: Boolean
        get() = this == Uploading

    val isNothing: Boolean
        get() = this == Nothing
}