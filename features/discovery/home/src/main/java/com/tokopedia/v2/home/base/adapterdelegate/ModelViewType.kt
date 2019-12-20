package com.tokopedia.v2.home.base.adapterdelegate

/**
 * [ModelViewType] is a type for model
 */
interface ModelViewType {
    fun getPrimaryKey(): Int
    fun isContentsTheSame(other: ModelViewType): Boolean
}
