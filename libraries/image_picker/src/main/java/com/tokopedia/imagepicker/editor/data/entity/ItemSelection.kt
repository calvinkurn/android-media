package com.tokopedia.imagepicker.editor.data.entity

import android.graphics.Bitmap

data class ItemSelection(
    val name: String = "",
    val preview: String = "",
    val previewWithResId: Int = 0,
    val placeholderText: String = "",
    val placeholderResId: Int = 0,
    val placeholderBitmap: Bitmap? = null,
    val itemType: Int = 0,
    val isSingleLabel: Boolean = false,
    var isSelected: Boolean = false
) {
    companion object {
        @JvmStatic
        fun createWithPlaceholderText(
            name: String,
            preview: String,
            placeholderText: String,
            itemType: Int,
            isSingleLabel: Boolean,
            isSelected: Boolean
        ): ItemSelection {
            return ItemSelection(
                name = name,
                preview = preview,
                placeholderText = placeholderText,
                itemType = itemType,
                isSingleLabel = isSingleLabel,
                isSelected = isSelected
            )
        }

        @JvmStatic
        fun createWithPlaceholderResourceId(
            name: String,
            preview: Int,
            itemType: Int,
            isSelected: Boolean
        ): ItemSelection {
            return ItemSelection(
                name = name,
                previewWithResId = preview,
                placeholderResId = 0,
                itemType = itemType,
                isSingleLabel = false,
                isSelected = isSelected
            )
        }

        @JvmStatic
        fun createWithPlaceholderResourceId(
            name: String,
            preview: String,
            placeholderResId: Int,
            itemType: Int,
            isSingleLabel: Boolean,
            isSelected: Boolean
        ): ItemSelection {
            return ItemSelection(
                name = name,
                preview = preview,
                placeholderResId = placeholderResId,
                itemType = itemType,
                isSingleLabel = isSingleLabel,
                isSelected = isSelected
            )
        }

        @JvmStatic
        fun createWithPlaceholderBitmap(
            name: String,
            preview: String,
            placeholderBitmap: Bitmap,
            itemType: Int,
            isSingleLabel: Boolean,
            isSelected: Boolean
        ): ItemSelection {
            return ItemSelection(
                name = name,
                preview = preview,
                placeholderBitmap = placeholderBitmap,
                itemType = itemType,
                isSingleLabel = isSingleLabel,
                isSelected = isSelected
            )
        }

        @JvmStatic
        fun createWithListPlaceholderBitmap(
            name: String,
            preview: String,
            placeholderBitmap: List<Bitmap>,
            isSingleLabel: Boolean,
            itemType: List<Int>,
        ): List<ItemSelection> {
            return placeholderBitmap.map { bitmap ->
                ItemSelection(
                    name = name,
                    preview = preview,
                    placeholderBitmap = bitmap,
                    itemType = itemType[placeholderBitmap.indexOf(bitmap)],
                    isSingleLabel = isSingleLabel,
                    isSelected = false
                )
            }.apply { first().isSelected = true }
        }

    }
}