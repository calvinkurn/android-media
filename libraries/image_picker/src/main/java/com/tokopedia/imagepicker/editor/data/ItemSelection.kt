package com.tokopedia.imagepicker.editor.data

import android.graphics.Bitmap

data class ItemSelection(
    val name: String = "",
    val preview: String = "",
    val placeholderText: String = "",
    val placeholderResId: Int = 0,
    val placeholderBitmap: Bitmap? = null,
    val itemType: Int = 0,
    var isSelected: Boolean = false
) {
    companion object {
        @JvmStatic
        fun createWithPlaceholderText(
            name: String,
            preview: String,
            placeholderText: String,
            itemType: Int,
            isSelected: Boolean
        ): ItemSelection {
            return ItemSelection(
                name = name,
                preview = preview,
                placeholderText = placeholderText,
                itemType = itemType,
                isSelected = isSelected
            )
        }

        @JvmStatic
        fun createWithPlaceholderResourceId(
            name: String,
            preview: String,
            placeholderResId: Int,
            itemType: Int,
            isSelected: Boolean
        ): ItemSelection {
            return ItemSelection(
                name = name,
                preview = preview,
                placeholderResId = placeholderResId,
                itemType = itemType,
                isSelected = isSelected
            )
        }

        @JvmStatic
        fun createWithPlaceholderBitmap(
            name: String,
            preview: String,
            placeholderBitmap: Bitmap,
            itemType: Int,
            isSelected: Boolean
        ): ItemSelection {
            return ItemSelection(
                name = name,
                preview = preview,
                placeholderBitmap = placeholderBitmap,
                itemType = itemType,
                isSelected = isSelected
            )
        }
    }
}