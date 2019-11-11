package com.tokopedia.v2.home.model.vo

import com.tokopedia.v2.home.base.adapterdelegate.ModelViewType

data class DynamicIconDataModel (
        val dynamicIcons: List<IconDataModel>,
        val dynamicIconWrap: Boolean
): ModelViewType{
    override fun getPrimaryKey(): Int {
        return 5
    }

    override fun isContentsTheSame(other: ModelViewType): Boolean {
        return true
    }

    data class IconDataModel(
            val id: String,
            val name: String,
            val applink: String,
            val imageUrl: String,
            val url: String,
            val buIdentifier: String
    )
}