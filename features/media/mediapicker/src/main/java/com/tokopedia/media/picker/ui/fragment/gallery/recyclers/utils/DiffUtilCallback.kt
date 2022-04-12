package com.tokopedia.media.picker.ui.fragment.gallery.recyclers.utils

import androidx.recyclerview.widget.DiffUtil

typealias ValueCompare<T> = (T, T) -> Boolean

class DefaultMediaDiffUtil<T> : ValueCompare<T> {
    override fun invoke(p1: T, p2: T) = p1 == p2
}

class MediaDiffUtil<T>(
    private val areItemTheSame: ValueCompare<T> = DefaultMediaDiffUtil(),
    private val areContentTheSame: ValueCompare<T> = DefaultMediaDiffUtil(),
) : DiffUtil.ItemCallback<T>() {

    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
        return areItemTheSame(oldItem, newItem)
    }

    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
        return areContentTheSame(oldItem, newItem)
    }

}