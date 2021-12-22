package com.tokopedia.picker.ui.fragment.gallery.adapter.utils

import androidx.recyclerview.widget.DiffUtil

typealias ValueCompare<T> = (T, T) -> Boolean

class DefaultGalleryDiffUtil<T> : ValueCompare<T> {
    override fun invoke(p1: T, p2: T) = p1 == p2
}

class GalleryDiffUtil<T>(
    private val areItemTheSame: ValueCompare<T> = DefaultGalleryDiffUtil(),
    private val areContentTheSame: ValueCompare<T> = DefaultGalleryDiffUtil(),
) : DiffUtil.ItemCallback<T>() {

    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
        return areItemTheSame(oldItem, newItem)
    }

    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
        return areContentTheSame(oldItem, newItem)
    }

}